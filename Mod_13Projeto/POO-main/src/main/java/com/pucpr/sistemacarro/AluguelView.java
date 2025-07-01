package com.pucpr.sistemacarro;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class AluguelView {

    private VBox view;
    private AluguelDAO aluguelDAO;
    private TableView<Aluguel> tabelaAlugueis;
    private TextField idClienteField, idCarroField, valorTotalField;
    private DatePicker dataInicioPicker, dataFimPicker;
    private ComboBox<String> statusComboBox;
    private Label statusViewLabel;

    private final ObservableList<String> statusOptions =
            FXCollections.observableArrayList("Em andamento", "Finalizado", "Cancelado");

    public AluguelView() {
        this.aluguelDAO = new AluguelDAO();
        initComponents();
        carregarAlugueisNaTabela();
    }

    public Node getViewNode() {
        return view;
    }

    private void initComponents() {
        view = new VBox(10);
        view.setPadding(new Insets(15));
        view.setAlignment(Pos.TOP_CENTER);

        Label titulo = new Label("Gerenciamento de Aluguéis"); // << TÍTULO ATUALIZADO
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(10));

        formGrid.add(new Label("ID Cliente:"), 0, 0);
        idClienteField = new TextField();
        idClienteField.setPromptText("ID numérico do cliente");
        formGrid.add(idClienteField, 1, 0);

        formGrid.add(new Label("ID Carro:"), 0, 1);
        idCarroField = new TextField();
        idCarroField.setPromptText("ID numérico do carro");
        formGrid.add(idCarroField, 1, 1);

        formGrid.add(new Label("Data Início:"), 0, 2);
        dataInicioPicker = new DatePicker(LocalDate.now());
        formGrid.add(dataInicioPicker, 1, 2);

        formGrid.add(new Label("Data Fim:"), 0, 3);
        dataFimPicker = new DatePicker();
        dataFimPicker.setPromptText("Opcional");
        formGrid.add(dataFimPicker, 1, 3);

        formGrid.add(new Label("Valor Total:"), 0, 4);
        valorTotalField = new TextField();
        valorTotalField.setPromptText("0.00");
        formGrid.add(valorTotalField, 1, 4);

        formGrid.add(new Label("Status:"), 0, 5);
        statusComboBox = new ComboBox<>(statusOptions);
        statusComboBox.setValue("Em andamento");
        formGrid.add(statusComboBox, 1, 5);


        HBox botoesBox = new HBox(10);
        botoesBox.setAlignment(Pos.CENTER);
        Button btnAdicionar = new Button("Iniciar Aluguel");
        Button btnAtualizar = new Button("Atualizar Status/Fim");
        Button btnDeletar = new Button("Deletar Registro");
        Button btnLimpar = new Button("Limpar Campos");

        btnAdicionar.setOnAction(e -> adicionarAluguel());
        btnAtualizar.setOnAction(e -> atualizarAluguel());
        btnDeletar.setOnAction(e -> deletarAluguel());
        btnLimpar.setOnAction(e -> limparCamposAluguel());

        botoesBox.getChildren().addAll(btnAdicionar, btnAtualizar, btnDeletar, btnLimpar);

        tabelaAlugueis = new TableView<>();
        TableColumn<Aluguel, Integer> colId = new TableColumn<>("ID Aluguel");
        colId.setCellValueFactory(new PropertyValueFactory<>("idAluguel")); // Usará o método idAluguelProperty()

        TableColumn<Aluguel, Integer> colIdCliente = new TableColumn<>("ID Cliente");
        colIdCliente.setCellValueFactory(new PropertyValueFactory<>("idCliente"));

        TableColumn<Aluguel, Integer> colIdCarro = new TableColumn<>("ID Carro");
        colIdCarro.setCellValueFactory(new PropertyValueFactory<>("idCarro"));

        TableColumn<Aluguel, LocalDate> colDataInicio = new TableColumn<>("Data Início");
        colDataInicio.setCellValueFactory(new PropertyValueFactory<>("dataInicio"));

        TableColumn<Aluguel, LocalDate> colDataFim = new TableColumn<>("Data Fim");
        colDataFim.setCellValueFactory(new PropertyValueFactory<>("dataFim"));

        TableColumn<Aluguel, Double> colValor = new TableColumn<>("Valor Total");
        colValor.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));

        TableColumn<Aluguel, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tabelaAlugueis.getColumns().addAll(colId, colIdCliente, colIdCarro, colDataInicio, colDataFim, colValor, colStatus);
        tabelaAlugueis.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        tabelaAlugueis.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                preencherCamposComAluguel(newSelection);
            }
        });

        statusViewLabel = new Label("Pronto.");
        HBox statusBoxLocal = new HBox(statusViewLabel);
        statusBoxLocal.setPadding(new Insets(5,0,0,0));
        statusBoxLocal.setAlignment(Pos.CENTER_LEFT);

        view.getChildren().addAll(titulo, formGrid, botoesBox, tabelaAlugueis, statusBoxLocal);
    }

    private void carregarAlugueisNaTabela() {
        try {
            ObservableList<Aluguel> lista = aluguelDAO.listarAlugueis();
            tabelaAlugueis.setItems(lista);
            statusViewLabel.setText("Aluguéis carregados (binário). Total: " + lista.size());
        } catch (IOException | ClassNotFoundException e) { // << TRATAR ClassNotFoundException
            mostrarAlertaErro("Erro ao carregar aluguéis do arquivo binário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void adicionarAluguel() {
        int idCliente, idCarro;
        double valorTotal;
        LocalDate dataInicio = dataInicioPicker.getValue();
        LocalDate dataFim = dataFimPicker.getValue();
        String status = statusComboBox.getValue();

        if (idClienteField.getText().isEmpty() || idCarroField.getText().isEmpty() ||
                dataInicio == null || valorTotalField.getText().isEmpty() || status == null) {
            mostrarAlertaAviso("ID Cliente, ID Carro, Data Início, Valor Total e Status são obrigatórios.");
            return;
        }

        try {
            idCliente = Integer.parseInt(idClienteField.getText());
            idCarro = Integer.parseInt(idCarroField.getText());
            valorTotal = Double.parseDouble(valorTotalField.getText().replace(",", "."));
        } catch (NumberFormatException e) {
            mostrarAlertaAviso("ID Cliente, ID Carro e Valor Total devem ser números válidos.");
            return;
        }

        Aluguel a = new Aluguel(idCliente, idCarro, dataInicio, dataFim, valorTotal, status); // Usa construtor sem ID
        try {
            aluguelDAO.adicionarAluguel(a);
            carregarAlugueisNaTabela();
            limparCamposAluguel();
            statusViewLabel.setText("Aluguel ID " + a.getIdAluguel() + " iniciado e salvo (binário)!");
        } catch (IOException | ClassNotFoundException e) { // << TRATAR ClassNotFoundException
            mostrarAlertaErro("Erro ao salvar aluguel no arquivo binário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void atualizarAluguel() {
        Aluguel selecionado = tabelaAlugueis.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlertaAviso("Selecione um aluguel na tabela para atualizar.");
            return;
        }

        int idCliente, idCarro;
        double valorTotal;
        LocalDate dataInicio = dataInicioPicker.getValue();
        LocalDate dataFim = dataFimPicker.getValue();
        String status = statusComboBox.getValue();

        if (idClienteField.getText().isEmpty() || idCarroField.getText().isEmpty() ||
                dataInicio == null || valorTotalField.getText().isEmpty() || status == null) {
            mostrarAlertaAviso("Todos os campos devem ser preenchidos.");
            return;
        }
        try {
            idCliente = Integer.parseInt(idClienteField.getText());
            idCarro = Integer.parseInt(idCarroField.getText());
            valorTotal = Double.parseDouble(valorTotalField.getText().replace(",", "."));
        } catch (NumberFormatException e) {
            mostrarAlertaAviso("ID Cliente, ID Carro e Valor Total devem ser números válidos.");
            return;
        }

        // Atualiza os campos do objeto 'selecionado' diretamente
        selecionado.setIdCliente(idCliente);
        selecionado.setIdCarro(idCarro);
        selecionado.setDataInicio(dataInicio);
        selecionado.setDataFim(dataFim);
        selecionado.setValorTotal(valorTotal);
        selecionado.setStatus(status);

        try {
            aluguelDAO.atualizarAluguel(selecionado);
            carregarAlugueisNaTabela();
            limparCamposAluguel();
            statusViewLabel.setText("Aluguel ID " + selecionado.getIdAluguel() + " atualizado (binário)!");
        } catch (IOException | ClassNotFoundException e) { // << TRATAR ClassNotFoundException
            mostrarAlertaErro("Erro ao atualizar aluguel no arquivo binário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void deletarAluguel() {
        Aluguel selecionado = tabelaAlugueis.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlertaAviso("Selecione um aluguel na tabela para deletar.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION,
                "Tem certeza que deseja deletar o registro do aluguel ID " + selecionado.getIdAluguel() + "?",
                ButtonType.YES, ButtonType.NO);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText(null);
        confirmacao.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    aluguelDAO.deletarAluguel(selecionado.getIdAluguel());
                    carregarAlugueisNaTabela();
                    limparCamposAluguel();
                    statusViewLabel.setText("Aluguel ID " + selecionado.getIdAluguel() + " deletado (binário)!");
                } catch (IOException | ClassNotFoundException e) { // << TRATAR ClassNotFoundException
                    mostrarAlertaErro("Erro ao deletar aluguel do arquivo binário: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private void preencherCamposComAluguel(Aluguel a) {
        idClienteField.setText(String.valueOf(a.getIdCliente()));
        idCarroField.setText(String.valueOf(a.getIdCarro()));
        dataInicioPicker.setValue(a.getDataInicio());
        dataFimPicker.setValue(a.getDataFim());
        valorTotalField.setText(String.format("%.2f", a.getValorTotal()).replace(",", "."));
        statusComboBox.setValue(a.getStatus());
        statusViewLabel.setText("Aluguel ID " + a.getIdAluguel() + " selecionado.");
    }

    private void limparCamposAluguel() {
        idClienteField.clear();
        idCarroField.clear();
        dataInicioPicker.setValue(LocalDate.now());
        dataFimPicker.setValue(null);
        valorTotalField.clear();
        statusComboBox.setValue("Em andamento");
        tabelaAlugueis.getSelectionModel().clearSelection();
        idClienteField.requestFocus();
        statusViewLabel.setText("Campos limpos. Pronto para novo aluguel.");
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