package com.pucpr.sistemacarro;

import javafx.beans.property.*;
import java.io.Serializable; // << IMPORTAR
import java.time.LocalDate;
// DateTimeFormatter e CSV_DELIMITER não são mais necessários

public class Pagamento implements Serializable { // << IMPLEMENTAR Serializable

    private static final long serialVersionUID = 4L; // << Adicionar/Atualizar serialVersionUID

    // Campos alterados de Properties para tipos primitivos/objetos para serialização
    private int id;
    private int idAluguel;
    private LocalDate dataPagamento; // LocalDate é serializável
    private double valorPago;
    private String formaPagamento;

    public Pagamento() {}

    public Pagamento(int id, int idAluguel, LocalDate dataPagamento, double valorPago, String formaPagamento) {
        this.id = id;
        this.idAluguel = idAluguel;
        this.dataPagamento = dataPagamento;
        this.valorPago = valorPago;
        this.formaPagamento = formaPagamento;
    }

    // Construtor para novos pagamentos onde o ID é gerado depois
    public Pagamento(int idAluguel, LocalDate dataPagamento, double valorPago, String formaPagamento) {
        this.idAluguel = idAluguel;
        this.dataPagamento = dataPagamento;
        this.valorPago = valorPago;
        this.formaPagamento = formaPagamento;
    }

    // Getters e Setters para os campos
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdAluguel() { return idAluguel; }
    public void setIdAluguel(int idAluguel) { this.idAluguel = idAluguel; }

    public LocalDate getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDate dataPagamento) { this.dataPagamento = dataPagamento; }

    public double getValorPago() { return valorPago; }
    public void setValorPago(double valorPago) { this.valorPago = valorPago; }

    public String getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(String formaPagamento) { this.formaPagamento = formaPagamento; }

    // Métodos Property para JavaFX TableView
    public IntegerProperty idProperty() { return new SimpleIntegerProperty(this.id); }
    public IntegerProperty idAluguelProperty() { return new SimpleIntegerProperty(this.idAluguel); }
    public ObjectProperty<LocalDate> dataPagamentoProperty() { return new SimpleObjectProperty<>(this.dataPagamento); }
    public DoubleProperty valorPagoProperty() { return new SimpleDoubleProperty(this.valorPago); }
    public StringProperty formaPagamentoProperty() { return new SimpleStringProperty(this.formaPagamento); }

    // toCsvString e fromCsvString são removidos

    @Override
    public String toString() {
        return "Pagamento ID: " + getId() + " para Aluguel ID: " + getIdAluguel() + " (" + getFormaPagamento() + ")";
    }
}