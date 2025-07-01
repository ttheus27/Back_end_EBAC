package com.pucpr.sistemacarro;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private BorderPane rootLayout;
    private Label statusLabelGlobal;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sistema de Gerenciamento");
        rootLayout = new BorderPane();

        MenuBar menuBar = new MenuBar();
        Menu gerenciarMenu = new Menu("Gerenciar");

        MenuItem carroItem = new MenuItem("Carro");
        carroItem.setOnAction(e -> {
            CarroView carroView = new CarroView(); // Cria a view de Carro
            rootLayout.setCenter(carroView.getViewNode()); // Define como conteúdo central
            statusLabelGlobal.setText("Tela de Carros carregada .");
        });

        MenuItem clienteItem = new MenuItem("Cliente");
        clienteItem.setOnAction(e -> {
            ClienteView clienteView = new ClienteView(); // Cria a view de Cliente
            rootLayout.setCenter(clienteView.getViewNode()); // Define como conteúdo central
            statusLabelGlobal.setText("Tela de Clientes carregada.");
        });

        MenuItem aluguelItem = new MenuItem("Aluguel");
        aluguelItem.setOnAction(e -> {
            AluguelView aluguelView = new AluguelView(); // Cria a view de Aluguel
            rootLayout.setCenter(aluguelView.getViewNode()); // Define como conteúdo central
            statusLabelGlobal.setText("Tela de Aluguéis carregada.");
        });

        MenuItem funcionarioItem = new MenuItem("Funcionário");
        funcionarioItem.setOnAction(e -> {
            FuncionarioView funcionarioView = new FuncionarioView();
            rootLayout.setCenter(funcionarioView.getViewNode());
            statusLabelGlobal.setText("Tela de Funcionários carregada.");
        });

        MenuItem pagamentoItem = new MenuItem("Pagamento");
        pagamentoItem.setOnAction(e -> {
            PagamentoView pagamentoView = new PagamentoView();
            rootLayout.setCenter(pagamentoView.getViewNode());
            statusLabelGlobal.setText("Tela de Pagamentos carregada.");
        });

        gerenciarMenu.getItems().addAll(carroItem, clienteItem, aluguelItem, funcionarioItem, pagamentoItem);
        menuBar.getMenus().add(gerenciarMenu);
        rootLayout.setTop(menuBar);

        statusLabelGlobal = new Label("Bem-vindo ao Sistema!");
        HBox statusBar = new HBox(statusLabelGlobal);
        statusBar.setPadding(new Insets(5));
        statusBar.setAlignment(Pos.CENTER_LEFT);
        statusBar.setStyle("-fx-background-color: #e0e0e0;");
        rootLayout.setBottom(statusBar);

        mostrarPlaceholder("Bem-vindo! Selecione uma opção no menu para começar.");

        Scene scene = new Scene(rootLayout, 950, 750);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void mostrarPlaceholder(String mensagem) {
        Label placeholderLabel = new Label(mensagem);
        placeholderLabel.setStyle("-fx-font-size: 20px; -fx-padding: 30px; -fx-text-fill: #555;");
        VBox centerContent = new VBox(placeholderLabel);
        centerContent.setAlignment(Pos.CENTER);
        centerContent.setStyle("-fx-background-color: #f9f9f9;");
        rootLayout.setCenter(centerContent);
        statusLabelGlobal.setText(mensagem);
    }

    public static void main(String[] args) {
        launch(args);
    }
}