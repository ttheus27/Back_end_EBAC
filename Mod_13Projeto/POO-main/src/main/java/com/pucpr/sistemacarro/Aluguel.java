package com.pucpr.sistemacarro;

import javafx.beans.property.*;
import java.io.Serializable; // << IMPORTAR
import java.time.LocalDate;
// DateTimeFormatter e DateTimeParseException não são mais necessários aqui para serialização
// CSV_DELIMITER também não é mais necessário

public class Aluguel implements Serializable { // << IMPLEMENTAR Serializable

    private static final long serialVersionUID = 2L; // << Adicionar serialVersionUID

    // Campos alterados de Properties para tipos primitivos/objetos para serialização
    private int idAluguel;
    private int idCliente;
    private int idCarro;
    private LocalDate dataInicio; // LocalDate é serializável
    private LocalDate dataFim;
    private double valorTotal;
    private String status;

    public Aluguel() {}

    public Aluguel(int idAluguel, int idCliente, int idCarro, LocalDate dataInicio, LocalDate dataFim, double valorTotal, String status) {
        this.idAluguel = idAluguel;
        this.idCliente = idCliente;
        this.idCarro = idCarro;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.valorTotal = valorTotal;
        this.status = status;
    }

    public Aluguel(int idCliente, int idCarro, LocalDate dataInicio, LocalDate dataFim, double valorTotal, String status) {
        this.idCliente = idCliente;
        this.idCarro = idCarro;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.valorTotal = valorTotal;
        this.status = status;
    }

    // Getters e Setters para os campos
    public int getIdAluguel() { return idAluguel; }
    public void setIdAluguel(int idAluguel) { this.idAluguel = idAluguel; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public int getIdCarro() { return idCarro; }
    public void setIdCarro(int idCarro) { this.idCarro = idCarro; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }

    public double getValorTotal() { return valorTotal; }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Métodos Property para JavaFX TableView
    public IntegerProperty idAluguelProperty() { return new SimpleIntegerProperty(this.idAluguel); }
    public IntegerProperty idClienteProperty() { return new SimpleIntegerProperty(this.idCliente); }
    public IntegerProperty idCarroProperty() { return new SimpleIntegerProperty(this.idCarro); }
    public ObjectProperty<LocalDate> dataInicioProperty() { return new SimpleObjectProperty<>(this.dataInicio); }
    public ObjectProperty<LocalDate> dataFimProperty() { return new SimpleObjectProperty<>(this.dataFim); }
    public DoubleProperty valorTotalProperty() { return new SimpleDoubleProperty(this.valorTotal); }
    public StringProperty statusProperty() { return new SimpleStringProperty(this.status); }

    // toCsvString e fromCsvString são removidos pois não são mais usados para serialização binária

    @Override
    public String toString() {
        return "Aluguel ID: " + getIdAluguel() + ", Cliente ID: " + getIdCliente() + ", Carro ID: " + getIdCarro();
    }
}