package com.pucpr.sistemacarro;

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

public class FuncionarioView {

    private VBox view;
    private FuncionarioDAO funcionarioDAO;
    private TableView<Funcionario> tabelaFuncionarios;
    private TextField nomeField, cargoField, salarioField;
    private DatePicker dataContratacaoPicker;
    private Label statusViewLabel;

    public FuncionarioView() {
        this.funcionarioDAO = new FuncionarioDAO();
        initComponents();
        carregarFuncionariosNaTabela();
    }

    public Node getViewNode() {
        return view;
    }

    private void initComponents() {
        view = new VBox(10);
        view.setPadding(new Insets(15));
        view.setAlignment(Pos.TOP_CENTER);

        Label titulo = new Label("Cadastro de Funcionários (Dados Binários)"); // << TÍTULO ATUALIZADO
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(10));

        formGrid.add(new Label("Nome:"), 0, 0);
        nomeField = new TextField();
        nomeField.setPromptText("Nome do funcionário");
        formGrid.add(nomeField, 1, 0);

        formGrid.add(new Label("Cargo:"), 0, 1);
        cargoField = new TextField();
        cargoField.setPromptText("Cargo");
        formGrid.add(cargoField, 1, 1);

        formGrid.add(new Label("Salário:"), 0, 2);
        salarioField = new TextField();
        salarioField.setPromptText("0.00");
        formGrid.add(salarioField, 1, 2);

        formGrid.add(new Label("Data Contratação:"), 0, 3);
        dataContratacaoPicker = new DatePicker(LocalDate.now());
        formGrid.add(dataContratacaoPicker, 1, 3);

        HBox botoesBox = new HBox(10);
        botoesBox.setAlignment(Pos.CENTER);
        Button btnAdicionar = new Button("Adicionar");
        Button btnAtualizar = new Button("Atualizar");
        Button btnDeletar = new Button("Deletar");
        Button btnLimpar = new Button("Limpar Campos");

        btnAdicionar.setOnAction(e -> adicionarFuncionario());
        btnAtualizar.setOnAction(e -> atualizarFuncionario());
        btnDeletar.setOnAction(e -> deletarFuncionario());
        btnLimpar.setOnAction(e -> limparCamposFuncionario());

        botoesBox.getChildren().addAll(btnAdicionar, btnAtualizar, btnDeletar, btnLimpar);

        tabelaFuncionarios = new TableView<>();
        TableColumn<Funcionario, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id")); // Usará o método idProperty()

        TableColumn<Funcionario, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colNome.setPrefWidth(200);

        TableColumn<Funcionario, String> colCargo = new TableColumn<>("Cargo");
        colCargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        colCargo.setPrefWidth(150);

        TableColumn<Funcionario, Double> colSalario = new TableColumn<>("Salário");
        colSalario.setCellValueFactory(new PropertyValueFactory<>("salario"));

        TableColumn<Funcionario, LocalDate> colDataContratacao = new TableColumn<>("Data Contratação");
        colDataContratacao.setCellValueFactory(new PropertyValueFactory<>("dataContratacao"));
        colDataContratacao.setPrefWidth(150);

        tabelaFuncionarios.getColumns().addAll(colId, colNome, colCargo, colSalario, colDataContratacao);
        tabelaFuncionarios.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        tabelaFuncionarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                preencherCamposComFuncionario(newSelection);
            }
        });

        statusViewLabel = new Label("Pronto.");
        HBox statusBoxLocal = new HBox(statusViewLabel);
        statusBoxLocal.setPadding(new Insets(5,0,0,0));
        statusBoxLocal.setAlignment(Pos.CENTER_LEFT);

        view.getChildren().addAll(titulo, formGrid, botoesBox, tabelaFuncionarios, statusBoxLocal);
    }

    private void carregarFuncionariosNaTabela() {
        try {
            ObservableList<Funcionario> lista = funcionarioDAO.listarFuncionarios();
            tabelaFuncionarios.setItems(lista);
            statusViewLabel.setText("Funcionários carregados (binário). Total: " + lista.size());
        } catch (IOException | ClassNotFoundException e) { // << TRATAR ClassNotFoundException
            mostrarAlertaErro("Erro ao carregar funcionários do arquivo binário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void adicionarFuncionario() {
        String nome = nomeField.getText();
        String cargo = cargoField.getText();
        LocalDate dataCont = dataContratacaoPicker.getValue();

        if (nome.isEmpty() || cargo.isEmpty() || dataCont == null || salarioField.getText().isEmpty()) {
            mostrarAlertaAviso("Preencha todos os campos obrigatórios.");
            return;
        }
        double salario;
        try {
            salario = Double.parseDouble(salarioField.getText().replace(",", "."));
        } catch (NumberFormatException e) {
            mostrarAlertaAviso("Salário inválido. Use formato numérico (ex: 1500.50).");
            return;
        }

        Funcionario f = new Funcionario(nome, cargo, salario, dataCont); // Usa construtor sem ID
        try {
            funcionarioDAO.adicionarFuncionario(f);
            carregarFuncionariosNaTabela();
            limparCamposFuncionario();
            statusViewLabel.setText("Funcionário '" + f.getNome() + "' adicionado (binário)!");
        } catch (IOException | ClassNotFoundException e) { // << TRATAR ClassNotFoundException
            mostrarAlertaErro("Erro ao salvar funcionário no arquivo binário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void atualizarFuncionario() {
        Funcionario selecionado = tabelaFuncionarios.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlertaAviso("Selecione um funcionário na tabela para atualizar.");
            return;
        }

        String nome = nomeField.getText();
        String cargo = cargoField.getText();
        LocalDate dataCont = dataContratacaoPicker.getValue();

        if (nome.isEmpty() || cargo.isEmpty() || dataCont == null || salarioField.getText().isEmpty()) {
            mostrarAlertaAviso("Preencha todos os campos obrigatórios.");
            return;
        }
        double salario;
        try {
            salario = Double.parseDouble(salarioField.getText().replace(",", "."));
        } catch (NumberFormatException e) {
            mostrarAlertaAviso("Salário inválido. Use formato numérico (ex: 1500.50).");
            return;
        }

        // Atualiza os campos do objeto 'selecionado' diretamente
        selecionado.setNome(nome);
        selecionado.setCargo(cargo);
        selecionado.setSalario(salario);
        selecionado.setDataContratacao(dataCont);

        try {
            funcionarioDAO.atualizarFuncionario(selecionado);
            carregarFuncionariosNaTabela();
            limparCamposFuncionario();
            statusViewLabel.setText("Funcionário '" + selecionado.getNome() + "' atualizado (binário)!");
        } catch (IOException | ClassNotFoundException e) { // << TRATAR ClassNotFoundException
            mostrarAlertaErro("Erro ao atualizar funcionário no arquivo binário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void deletarFuncionario() {
        Funcionario selecionado = tabelaFuncionarios.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlertaAviso("Selecione um funcionário na tabela para deletar.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION,
                "Tem certeza que deseja deletar o funcionário " + selecionado.getNome() + "?",
                ButtonType.YES, ButtonType.NO);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText(null);
        confirmacao.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    funcionarioDAO.deletarFuncionario(selecionado.getId());
                    carregarFuncionariosNaTabela();
                    limparCamposFuncionario();
                    statusViewLabel.setText("Funcionário '" + selecionado.getNome() + "' deletado (binário)!");
                } catch (IOException | ClassNotFoundException e) { // << TRATAR ClassNotFoundException
                    mostrarAlertaErro("Erro ao deletar funcionário do arquivo binário: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private void preencherCamposComFuncionario(Funcionario f) {
        nomeField.setText(f.getNome());
        cargoField.setText(f.getCargo());
        salarioField.setText(String.format("%.2f", f.getSalario()).replace(",", "."));
        dataContratacaoPicker.setValue(f.getDataContratacao());
        statusViewLabel.setText("Funcionário '" + f.getNome() + "' selecionado.");
    }

    private void limparCamposFuncionario() {
        nomeField.clear();
        cargoField.clear();
        salarioField.clear();
        dataContratacaoPicker.setValue(LocalDate.now());
        tabelaFuncionarios.getSelectionModel().clearSelection();
        nomeField.requestFocus();
        statusViewLabel.setText("Campos limpos. Pronto para nova entrada.");
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