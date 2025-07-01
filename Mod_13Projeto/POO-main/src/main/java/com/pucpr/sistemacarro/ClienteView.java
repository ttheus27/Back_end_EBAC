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

public class ClienteView {

    private VBox view;
    private ClienteDAO clienteDAO;
    private TableView<Cliente> tabelaClientes;
    private TextField nomeField, cpfField, telefoneField, emailField, cnhField, buscaNomeField;
    private Label statusViewLabel;

    public ClienteView() {
        this.clienteDAO = new ClienteDAO();
        initComponents();
        carregarClientesNaTabela();
    }

    public Node getViewNode() {
        return view;
    }

    private void initComponents() {
        view = new VBox(10);
        view.setPadding(new Insets(15));
        view.setAlignment(Pos.TOP_CENTER);

        Label titulo = new Label("Cadastro de Clientes"); // << TÍTULO ATUALIZADO
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        HBox buscaBox = new HBox(10);
        buscaBox.setAlignment(Pos.CENTER_LEFT);
        buscaNomeField = new TextField();
        buscaNomeField.setPromptText("Buscar por nome...");
        Button btnBuscar = new Button("Buscar");
        btnBuscar.setOnAction(e -> buscarClientes());
        Button btnListarTodos = new Button("Listar Todos");
        btnListarTodos.setOnAction(e -> carregarClientesNaTabela());
        buscaBox.getChildren().addAll(new Label("Buscar:"), buscaNomeField, btnBuscar, btnListarTodos);

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(10));

        formGrid.add(new Label("Nome Completo:"), 0, 0);
        nomeField = new TextField();
        formGrid.add(nomeField, 1, 0);

        formGrid.add(new Label("CPF:"), 0, 1);
        cpfField = new TextField();
        cpfField.setPromptText("000.000.000-00");
        formGrid.add(cpfField, 1, 1);

        formGrid.add(new Label("Telefone:"), 0, 2);
        telefoneField = new TextField();
        telefoneField.setPromptText("(00) 00000-0000");
        formGrid.add(telefoneField, 1, 2);

        formGrid.add(new Label("Email:"), 0, 3);
        emailField = new TextField();
        emailField.setPromptText("cliente@email.com");
        formGrid.add(emailField, 1, 3);

        formGrid.add(new Label("CNH:"), 0, 4);
        cnhField = new TextField();
        cnhField.setPromptText("Número da CNH");
        formGrid.add(cnhField, 1, 4);

        HBox botoesBox = new HBox(10);
        botoesBox.setAlignment(Pos.CENTER);
        Button btnAdicionar = new Button("Adicionar Cliente");
        Button btnAtualizar = new Button("Atualizar Cliente");
        Button btnDeletar = new Button("Deletar Cliente");
        Button btnLimpar = new Button("Limpar Campos");

        btnAdicionar.setOnAction(e -> adicionarCliente());
        btnAtualizar.setOnAction(e -> atualizarCliente());
        btnDeletar.setOnAction(e -> deletarCliente());
        btnLimpar.setOnAction(e -> limparCamposCliente());

        botoesBox.getChildren().addAll(btnAdicionar, btnAtualizar, btnDeletar, btnLimpar);

        tabelaClientes = new TableView<>();
        TableColumn<Cliente, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idCliente")); // Usará o método idClienteProperty()

        TableColumn<Cliente, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colNome.setPrefWidth(200);

        TableColumn<Cliente, String> colCpf = new TableColumn<>("CPF");
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colCpf.setPrefWidth(120);

        TableColumn<Cliente, String> colTelefone = new TableColumn<>("Telefone");
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colTelefone.setPrefWidth(120);

        TableColumn<Cliente, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colEmail.setPrefWidth(180);

        TableColumn<Cliente, String> colCnh = new TableColumn<>("CNH");
        colCnh.setCellValueFactory(new PropertyValueFactory<>("cnh"));
        colCnh.setPrefWidth(120);

        tabelaClientes.getColumns().addAll(colId, colNome, colCpf, colTelefone, colEmail, colCnh);
        tabelaClientes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        tabelaClientes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                preencherCamposComCliente(newSelection);
            }
        });

        statusViewLabel = new Label("Pronto.");
        HBox statusBoxLocal = new HBox(statusViewLabel);
        statusBoxLocal.setPadding(new Insets(5,0,0,0));
        statusBoxLocal.setAlignment(Pos.CENTER_LEFT);

        view.getChildren().addAll(titulo, buscaBox, formGrid, botoesBox, tabelaClientes, statusBoxLocal);
    }

    private void carregarClientesNaTabela() {
        try {
            ObservableList<Cliente> lista = clienteDAO.listarClientes();
            tabelaClientes.setItems(lista);
            statusViewLabel.setText("Clientes carregados (binário). Total: " + lista.size());
            buscaNomeField.clear();
        } catch (IOException | ClassNotFoundException e) { // << TRATAR ClassNotFoundException
            mostrarAlertaErro("Erro ao carregar clientes do arquivo binário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void buscarClientes() {
        String nomeBusca = buscaNomeField.getText();
        try {
            ObservableList<Cliente> listaFiltrada = clienteDAO.buscarClientePorNome(nomeBusca);
            tabelaClientes.setItems(listaFiltrada);
            statusViewLabel.setText(listaFiltrada.size() + " cliente(s) encontrado(s) para '" + nomeBusca + "'.");
        } catch (IOException | ClassNotFoundException e) { // << TRATAR ClassNotFoundException
            mostrarAlertaErro("Erro ao buscar clientes no arquivo binário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void adicionarCliente() {
        String nome = nomeField.getText();
        String cpf = cpfField.getText();
        String telefone = telefoneField.getText();
        String email = emailField.getText();
        String cnh = cnhField.getText();

        if (nome.isEmpty() || cpf.isEmpty() || telefone.isEmpty() || cnh.isEmpty()) {
            mostrarAlertaAviso("Nome, CPF, Telefone e CNH são campos obrigatórios.");
            return;
        }

        Cliente c = new Cliente(nome, cpf, telefone, email, cnh); // Usa construtor sem ID
        try {
            clienteDAO.adicionarCliente(c);
            carregarClientesNaTabela();
            limparCamposCliente();
            statusViewLabel.setText("Cliente '" + c.getNome() + "' adicionado (binário)!");
        } catch (IOException | ClassNotFoundException e) { // << TRATAR ClassNotFoundException
            mostrarAlertaErro("Erro ao salvar cliente no arquivo binário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void atualizarCliente() {
        Cliente selecionado = tabelaClientes.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlertaAviso("Selecione um cliente na tabela para atualizar.");
            return;
        }

        String nome = nomeField.getText();
        String cpf = cpfField.getText();
        String telefone = telefoneField.getText();
        String email = emailField.getText();
        String cnh = cnhField.getText();

        if (nome.isEmpty() || cpf.isEmpty() || telefone.isEmpty() || cnh.isEmpty()) {
            mostrarAlertaAviso("Nome, CPF, Telefone e CNH são campos obrigatórios.");
            return;
        }

        // Atualiza os campos do objeto 'selecionado' diretamente
        selecionado.setNome(nome);
        selecionado.setCpf(cpf);
        selecionado.setTelefone(telefone);
        selecionado.setEmail(email);
        selecionado.setCnh(cnh);

        try {
            clienteDAO.atualizarCliente(selecionado);
            carregarClientesNaTabela();
            limparCamposCliente();
            statusViewLabel.setText("Cliente '" + selecionado.getNome() + "' atualizado (binário)!");
        } catch (IOException | ClassNotFoundException e) { // << TRATAR ClassNotFoundException
            mostrarAlertaErro("Erro ao atualizar cliente no arquivo binário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void deletarCliente() {
        Cliente selecionado = tabelaClientes.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlertaAviso("Selecione um cliente na tabela para deletar.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION,
                "Tem certeza que deseja deletar o cliente " + selecionado.getNome() + "?",
                ButtonType.YES, ButtonType.NO);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText(null);
        confirmacao.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    clienteDAO.deletarCliente(selecionado.getIdCliente());
                    carregarClientesNaTabela();
                    limparCamposCliente();
                    statusViewLabel.setText("Cliente '" + selecionado.getNome() + "' deletado (binário)!");
                } catch (IOException | ClassNotFoundException e) { // << TRATAR ClassNotFoundException
                    mostrarAlertaErro("Erro ao deletar cliente do arquivo binário: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private void preencherCamposComCliente(Cliente c) {
        nomeField.setText(c.getNome());
        cpfField.setText(c.getCpf());
        telefoneField.setText(c.getTelefone());
        emailField.setText(c.getEmail());
        cnhField.setText(c.getCnh());
        statusViewLabel.setText("Cliente '" + c.getNome() + "' selecionado.");
    }

    private void limparCamposCliente() {
        nomeField.clear();
        cpfField.clear();
        telefoneField.clear();
        emailField.clear();
        cnhField.clear();
        tabelaClientes.getSelectionModel().clearSelection();
        nomeField.requestFocus();
        statusViewLabel.setText("Campos limpos. Pronto para novo cliente.");
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