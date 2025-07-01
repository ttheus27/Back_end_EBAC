package com.pucpr.sistemacarro;

// Removi o import de javafx.beans.property.* pois não é diretamente usado aqui agora
import javafx.collections.ObservableList; // Já existe
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;

public class PagamentoView {

    private VBox view;
    private PagamentoDAO pagamentoDAO;
    private TableView<Pagamento> tabelaPagamentos;
    private TextField idAluguelField, formaPagamentoField, valorPagoField;
    private DatePicker dataPagamentoPicker;
    private Label statusViewLabel;

    public PagamentoView() {
        this.pagamentoDAO = new PagamentoDAO();
        initComponents();
        carregarPagamentosNaTabela();
    }

    public Node getViewNode() {
        return view;
    }

    private void initComponents() {
        view = new VBox(10);
        view.setPadding(new Insets(15));
        view.setAlignment(Pos.TOP_CENTER);

        Label titulo = new Label("Cadastro de Pagamentos"); // << TÍTULO ATUALIZADO
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(10));

        formGrid.add(new Label("ID Aluguel:"), 0, 0); // Alterado de "Aluguel:" para "ID Aluguel:"
        idAluguelField = new TextField();
        idAluguelField.setPromptText("ID do aluguel associado");
        formGrid.add(idAluguelField, 1, 0);

        formGrid.add(new Label("Forma de Pagamento:"), 0, 1);
        formaPagamentoField = new TextField();
        formaPagamentoField.setPromptText("Ex: Cartão, Dinheiro, Pix");
        formGrid.add(formaPagamentoField, 1, 1);

        formGrid.add(new Label("Valor Pago:"), 0, 2);
        valorPagoField = new TextField();
        valorPagoField.setPromptText("0.00");
        formGrid.add(valorPagoField, 1, 2);

        formGrid.add(new Label("Data Pagamento:"), 0, 3);
        dataPagamentoPicker = new DatePicker(LocalDate.now());
        formGrid.add(dataPagamentoPicker, 1, 3);

        HBox botoesBox = new HBox(10);
        botoesBox.setAlignment(Pos.CENTER);
        Button btnAdicionar = new Button("Adicionar Pagamento");
        Button btnAtualizar = new Button("Atualizar Pagamento");
        Button btnDeletar = new Button("Deletar Pagamento");
        Button btnLimpar = new Button("Limpar Campos");

        btnAdicionar.setOnAction(e -> adicionarPagamento());
        btnAtualizar.setOnAction(e -> atualizarPagamento());
        btnDeletar.setOnAction(e -> deletarPagamento());
        btnLimpar.setOnAction(e -> limparCamposPagamento());

        botoesBox.getChildren().addAll(btnAdicionar, btnAtualizar, btnDeletar, btnLimpar);

        tabelaPagamentos = new TableView<>();
        TableColumn<Pagamento, Integer> colId = new TableColumn<>("ID Pag."); // ID do Pagamento
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Pagamento, Integer> colIdAluguel = new TableColumn<>("ID Aluguel"); // Alterado para Integer
        colIdAluguel.setCellValueFactory(new PropertyValueFactory<>("idAluguel"));
        colIdAluguel.setPrefWidth(100);

        TableColumn<Pagamento, String> colFormaPagamento = new TableColumn<>("Forma Pagamento");
        colFormaPagamento.setCellValueFactory(new PropertyValueFactory<>("formaPagamento"));
        colFormaPagamento.setPrefWidth(150);

        TableColumn<Pagamento, Double> colValorPago = new TableColumn<>("Valor Pago");
        colValorPago.setCellValueFactory(new PropertyValueFactory<>("valorPago"));

        TableColumn<Pagamento, LocalDate> colDataPagamento = new TableColumn<>("Data Pagamento");
        colDataPagamento.setCellValueFactory(new PropertyValueFactory<>("dataPagamento"));
        colDataPagamento.setPrefWidth(150);

        tabelaPagamentos.getColumns().addAll(colId, colIdAluguel, colFormaPagamento, colValorPago, colDataPagamento);
        tabelaPagamentos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        tabelaPagamentos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                preencherCamposComPagamento(newSelection);
            }
        });

        statusViewLabel = new Label("Pronto.");
        HBox statusBoxLocal = new HBox(statusViewLabel);
        statusBoxLocal.setPadding(new Insets(5,0,0,0));
        statusBoxLocal.setAlignment(Pos.CENTER_LEFT);

        view.getChildren().addAll(titulo, formGrid, botoesBox, tabelaPagamentos, statusBoxLocal);
    }

    private void carregarPagamentosNaTabela() {
        try {
            ObservableList<Pagamento> lista = pagamentoDAO.listarPagamentos();
            tabelaPagamentos.setItems(lista);
            statusViewLabel.setText("Pagamentos carregados (binário). Total: " + lista.size());
        } catch (IOException | ClassNotFoundException e) { // << TRATAR ClassNotFoundException
            mostrarAlertaErro("Erro ao carregar pagamentos do arquivo binário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void adicionarPagamento() {
        int idAluguel;
        double valorPago;
        String formaPagamento = formaPagamentoField.getText();
        LocalDate dataPag = dataPagamentoPicker.getValue();

        if (idAluguelField.getText().isEmpty() || formaPagamento.isEmpty() || dataPag == null || valorPagoField.getText().isEmpty()) {
            mostrarAlertaAviso("ID Aluguel, Forma de Pagamento, Data e Valor Pago são obrigatórios.");
            return;
        }

        try {
            idAluguel = Integer.parseInt(idAluguelField.getText().trim());
            valorPago = Double.parseDouble(valorPagoField.getText().replace(",", "."));
        } catch (NumberFormatException e) {
            mostrarAlertaAviso("ID Aluguel e Valor Pago devem ser números válidos.");
            return;
        }

        Pagamento p = new Pagamento(idAluguel, dataPag, valorPago, formaPagamento); // Usa construtor sem ID de pagamento
        try {
            pagamentoDAO.adicionarPagamento(p);
            carregarPagamentosNaTabela();
            limparCamposPagamento();
            statusViewLabel.setText("Pagamento ID " + p.getId() + " adicionado (binário)!");
        } catch (IOException | ClassNotFoundException e) { // << TRATAR ClassNotFoundException
            mostrarAlertaErro("Erro ao salvar pagamento no arquivo binário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void atualizarPagamento() {
        Pagamento selecionado = tabelaPagamentos.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlertaAviso("Selecione um pagamento na tabela para atualizar.");
            return;
        }

        int idAluguel;
        double valorPago;
        String formaPagamento = formaPagamentoField.getText();
        LocalDate dataPag = dataPagamentoPicker.getValue();

        if (idAluguelField.getText().isEmpty() || formaPagamento.isEmpty() || dataPag == null || valorPagoField.getText().isEmpty()) {
            mostrarAlertaAviso("Todos os campos são obrigatórios.");
            return;
        }
        try {
            idAluguel = Integer.parseInt(idAluguelField.getText().trim());
            valorPago = Double.parseDouble(valorPagoField.getText().replace(",", "."));
        } catch (NumberFormatException e) {
            mostrarAlertaAviso("ID Aluguel e Valor Pago devem ser números válidos.");
            return;
        }

        selecionado.setIdAluguel(idAluguel);
        selecionado.setFormaPagamento(formaPagamento);
        selecionado.setValorPago(valorPago);
        selecionado.setDataPagamento(dataPag);

        try {
            pagamentoDAO.atualizarPagamento(selecionado);
            carregarPagamentosNaTabela();
            limparCamposPagamento();
            statusViewLabel.setText("Pagamento ID " + selecionado.getId() + " atualizado (binário)!");
        } catch (IOException | ClassNotFoundException e) { // << TRATAR ClassNotFoundException
            mostrarAlertaErro("Erro ao atualizar pagamento no arquivo binário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void deletarPagamento() {
        Pagamento selecionado = tabelaPagamentos.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlertaAviso("Selecione um pagamento na tabela para deletar.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION,
                "Tem certeza que deseja deletar o pagamento ID " + selecionado.getId() + "?",
                ButtonType.YES, ButtonType.NO);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText(null);
        confirmacao.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    pagamentoDAO.deletarPagamento(selecionado.getId());
                    carregarPagamentosNaTabela();
                    limparCamposPagamento();
                    statusViewLabel.setText("Pagamento ID " + selecionado.getId() + " deletado (binário)!");
                } catch (IOException | ClassNotFoundException e) { // << TRATAR ClassNotFoundException
                    mostrarAlertaErro("Erro ao deletar pagamento do arquivo binário: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private void preencherCamposComPagamento(Pagamento p) {
        idAluguelField.setText(String.valueOf(p.getIdAluguel()));
        formaPagamentoField.setText(p.getFormaPagamento());
        valorPagoField.setText(String.format("%.2f", p.getValorPago()).replace(",", "."));
        dataPagamentoPicker.setValue(p.getDataPagamento());
        statusViewLabel.setText("Pagamento ID " + p.getId() + " selecionado.");
    }

    private void limparCamposPagamento() {
        idAluguelField.clear();
        formaPagamentoField.clear();
        valorPagoField.clear();
        dataPagamentoPicker.setValue(LocalDate.now());
        tabelaPagamentos.getSelectionModel().clearSelection();
        idAluguelField.requestFocus();
        statusViewLabel.setText("Campos limpos. Pronto para novo pagamento.");
    }

    private void mostrarAlertaErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro no Sistema");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarAlertaAviso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atenção");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}