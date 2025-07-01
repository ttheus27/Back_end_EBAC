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

public class CarroView {

    private VBox view;
    private CarroDAO carroDAO;
    private TableView<Carro> tabelaCarros;
    private TextField modeloField, marcaField, placaField, anoField, valorDiariaField, buscaField;
    private ComboBox<String> statusComboBox;
    private Label statusViewLabel;

    private final ObservableList<String> statusOptions =
            FXCollections.observableArrayList("Disponível", "Alugado", "Manutenção");

    public CarroView() {
        this.carroDAO = new CarroDAO();
        initComponents();
        carregarCarrosNaTabela();
    }

    public Node getViewNode() {
        return view;
    }

    private void initComponents() {
        view = new VBox(10);
        view.setPadding(new Insets(15));
        view.setAlignment(Pos.TOP_CENTER);

        Label titulo = new Label("Cadastro de Carros");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        HBox buscaBox = new HBox(10);
        buscaBox.setAlignment(Pos.CENTER_LEFT);
        buscaField = new TextField();
        buscaField.setPromptText("Buscar por Modelo, Marca ou Placa...");
        Button btnBuscar = new Button("Buscar");
        btnBuscar.setOnAction(e -> buscarCarros());
        Button btnListarTodos = new Button("Listar Todos");
        btnListarTodos.setOnAction(e -> carregarCarrosNaTabela());
        buscaBox.getChildren().addAll(new Label("Buscar:"), buscaField, btnBuscar, btnListarTodos);

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(10));

        formGrid.add(new Label("Modelo:"), 0, 0);
        modeloField = new TextField();
        formGrid.add(modeloField, 1, 0);

        formGrid.add(new Label("Marca:"), 0, 1);
        marcaField = new TextField();
        formGrid.add(marcaField, 1, 1);

        formGrid.add(new Label("Placa:"), 0, 2);
        placaField = new TextField();
        placaField.setPromptText("AAA-0000 ou AAA0X00");
        formGrid.add(placaField, 1, 2);

        formGrid.add(new Label("Ano:"), 0, 3);
        anoField = new TextField();
        anoField.setPromptText("YYYY");
        formGrid.add(anoField, 1, 3);

        formGrid.add(new Label("Status:"), 0, 4);
        statusComboBox = new ComboBox<>(statusOptions);
        statusComboBox.setValue("Disponível"); // Default
        formGrid.add(statusComboBox, 1, 4);

        formGrid.add(new Label("Valor Diária:"), 0, 5);
        valorDiariaField = new TextField();
        valorDiariaField.setPromptText("0.00");
        formGrid.add(valorDiariaField, 1, 5);

        HBox botoesBox = new HBox(10);
        botoesBox.setAlignment(Pos.CENTER);
        Button btnAdicionar = new Button("Adicionar Carro");
        Button btnAtualizar = new Button("Atualizar Carro");
        Button btnDeletar = new Button("Deletar Carro");
        Button btnLimpar = new Button("Limpar Campos");

        btnAdicionar.setOnAction(e -> adicionarCarro());
        btnAtualizar.setOnAction(e -> atualizarCarro());
        btnDeletar.setOnAction(e -> deletarCarro());
        btnLimpar.setOnAction(e -> limparCamposCarro());

        botoesBox.getChildren().addAll(btnAdicionar, btnAtualizar, btnDeletar, btnLimpar);

        tabelaCarros = new TableView<>();
        TableColumn<Carro, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idCarro"));

        TableColumn<Carro, String> colModelo = new TableColumn<>("Modelo");
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colModelo.setPrefWidth(150);

        TableColumn<Carro, String> colMarca = new TableColumn<>("Marca");
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colMarca.setPrefWidth(120);

        TableColumn<Carro, String> colPlaca = new TableColumn<>("Placa");
        colPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));
        colPlaca.setPrefWidth(100);

        TableColumn<Carro, Integer> colAno = new TableColumn<>("Ano");
        colAno.setCellValueFactory(new PropertyValueFactory<>("ano"));

        TableColumn<Carro, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colStatus.setPrefWidth(100);

        TableColumn<Carro, Double> colValorDiaria = new TableColumn<>("Diária (R$)");
        colValorDiaria.setCellValueFactory(new PropertyValueFactory<>("valorDiaria"));

        tabelaCarros.getColumns().addAll(colId, colModelo, colMarca, colPlaca, colAno, colStatus, colValorDiaria);
        tabelaCarros.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        tabelaCarros.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                preencherCamposComCarro(newSelection);
            }
        });

        statusViewLabel = new Label("Pronto.");
        HBox statusBoxLocal = new HBox(statusViewLabel);
        statusBoxLocal.setPadding(new Insets(5,0,0,0));
        statusBoxLocal.setAlignment(Pos.CENTER_LEFT);

        view.getChildren().addAll(titulo, buscaBox, formGrid, botoesBox, tabelaCarros, statusBoxLocal);
    }

    private void carregarCarrosNaTabela() {
        try {
            ObservableList<Carro> lista = carroDAO.listarCarros();
            tabelaCarros.setItems(lista);
            statusViewLabel.setText("Carros carregados (binário). Total: " + lista.size());
            buscaField.clear();
        } catch (IOException | ClassNotFoundException e) {
            mostrarAlertaErro("Erro ao carregar carros do arquivo binário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void buscarCarros() {
        String termoBusca = buscaField.getText();
        try {
            ObservableList<Carro> listaFiltrada = carroDAO.buscarCarros(termoBusca);
            tabelaCarros.setItems(listaFiltrada);
            statusViewLabel.setText(listaFiltrada.size() + " carro(s) encontrado(s) para '" + termoBusca + "'.");
        } catch (IOException | ClassNotFoundException e) {
            mostrarAlertaErro("Erro ao buscar carros no arquivo binário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void adicionarCarro() {
        String modelo = modeloField.getText();
        String marca = marcaField.getText();
        String placa = placaField.getText();
        String status = statusComboBox.getValue();
        int ano;
        double valorDiaria;

        if (modelo.isEmpty() || marca.isEmpty() || placa.isEmpty() || anoField.getText().isEmpty() ||
                status == null || valorDiariaField.getText().isEmpty()) {
            mostrarAlertaAviso("Todos os campos são obrigatórios.");
            return;
        }

        try {
            ano = Integer.parseInt(anoField.getText());
            valorDiaria = Double.parseDouble(valorDiariaField.getText().replace(",", "."));
        } catch (NumberFormatException e) {
            mostrarAlertaAviso("Ano e Valor da Diária devem ser números válidos.");
            return;
        }

        Carro c = new Carro(modelo, marca, placa, ano, status, valorDiaria);
        try {
            carroDAO.adicionarCarro(c);
            carregarCarrosNaTabela();
            limparCamposCarro();
            statusViewLabel.setText("Carro " + c.getModelo() + " adicionado (binário)!");
        } catch (IOException | ClassNotFoundException e) {
            mostrarAlertaErro("Erro ao salvar carro no arquivo binário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void atualizarCarro() {
        Carro selecionado = tabelaCarros.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlertaAviso("Selecione um carro na tabela para atualizar.");
            return;
        }

        String modelo = modeloField.getText();
        String marca = marcaField.getText();
        String placa = placaField.getText();
        String status = statusComboBox.getValue();
        int ano;
        double valorDiaria;

        if (modelo.isEmpty() || marca.isEmpty() || placa.isEmpty() || anoField.getText().isEmpty() ||
                status == null || valorDiariaField.getText().isEmpty()) {
            mostrarAlertaAviso("Todos os campos são obrigatórios.");
            return;
        }
        try {
            ano = Integer.parseInt(anoField.getText());
            valorDiaria = Double.parseDouble(valorDiariaField.getText().replace(",", "."));
        } catch (NumberFormatException e) {
            mostrarAlertaAviso("Ano e Valor da Diária devem ser números válidos.");
            return;
        }

        selecionado.setModelo(modelo);
        selecionado.setMarca(marca);
        selecionado.setPlaca(placa);
        selecionado.setAno(ano);
        selecionado.setStatus(status);
        selecionado.setValorDiaria(valorDiaria);

        try {
            carroDAO.atualizarCarro(selecionado);
            carregarCarrosNaTabela();
            limparCamposCarro();
            statusViewLabel.setText("Carro " + selecionado.getModelo() + " atualizado (binário)!");
        } catch (IOException | ClassNotFoundException e) {
            mostrarAlertaErro("Erro ao atualizar carro no arquivo binário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void deletarCarro() {
        Carro selecionado = tabelaCarros.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlertaAviso("Selecione um carro na tabela para deletar.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION,
                "Tem certeza que deseja deletar o carro " + selecionado.getModelo() + " (" + selecionado.getPlaca() + ")?",
                ButtonType.YES, ButtonType.NO);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText(null);
        confirmacao.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    carroDAO.deletarCarro(selecionado.getIdCarro());
                    carregarCarrosNaTabela();
                    limparCamposCarro();
                    statusViewLabel.setText("Carro " + selecionado.getModelo() + " deletado (binário)!");
                } catch (IOException | ClassNotFoundException e) {
                    mostrarAlertaErro("Erro ao deletar carro do arquivo binário: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private void preencherCamposComCarro(Carro c) {
        modeloField.setText(c.getModelo());
        marcaField.setText(c.getMarca());
        placaField.setText(c.getPlaca());
        anoField.setText(String.valueOf(c.getAno()));
        statusComboBox.setValue(c.getStatus());
        valorDiariaField.setText(String.format("%.2f", c.getValorDiaria()).replace(",", "."));
        statusViewLabel.setText("Carro " + c.getModelo() + " selecionado.");
    }

    private void limparCamposCarro() {
        modeloField.clear();
        marcaField.clear();
        placaField.clear();
        anoField.clear();
        statusComboBox.setValue("Disponível");
        valorDiariaField.clear();
        tabelaCarros.getSelectionModel().clearSelection();
        modeloField.requestFocus();
        statusViewLabel.setText("Campos limpos. Pronto para novo carro.");
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