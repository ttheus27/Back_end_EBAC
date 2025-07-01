package com.pucpr.sistemacarro;

import javafx.beans.property.*;
import java.io.Serializable;

public class Carro implements Serializable {

    private static final long serialVersionUID = 5L; // << Adicionar/Atualizar serialVersionUID

    private int idCarro;
    private String modelo;
    private String marca;
    private String placa;
    private int ano;
    private String status; // "Disponível", "Alugado", "Manutenção"
    private double valorDiaria;

    public Carro() {}

    public Carro(int idCarro, String modelo, String marca, String placa, int ano, String status, double valorDiaria) {
        this.idCarro = idCarro;
        this.modelo = modelo;
        this.marca = marca;
        this.placa = placa;
        this.ano = ano;
        this.status = status;
        this.valorDiaria = valorDiaria;
    }

    // Construtor para novos carros onde o ID é gerado depois
    public Carro(String modelo, String marca, String placa, int ano, String status, double valorDiaria) {
        this.modelo = modelo;
        this.marca = marca;
        this.placa = placa;
        this.ano = ano;
        this.status = status;
        this.valorDiaria = valorDiaria;
    }

    // Getters e Setters para os campos
    public int getIdCarro() { return idCarro; }
    public void setIdCarro(int idCarro) { this.idCarro = idCarro; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public int getAno() { return ano; }
    public void setAno(int ano) { this.ano = ano; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getValorDiaria() { return valorDiaria; }
    public void setValorDiaria(double valorDiaria) { this.valorDiaria = valorDiaria; }

    // Métodos Property para JavaFX TableView
    public IntegerProperty idCarroProperty() { return new SimpleIntegerProperty(this.idCarro); }
    public StringProperty modeloProperty() { return new SimpleStringProperty(this.modelo); }
    public StringProperty marcaProperty() { return new SimpleStringProperty(this.marca); }
    public StringProperty placaProperty() { return new SimpleStringProperty(this.placa); }
    public IntegerProperty anoProperty() { return new SimpleIntegerProperty(this.ano); }
    public StringProperty statusProperty() { return new SimpleStringProperty(this.status); }
    public DoubleProperty valorDiariaProperty() { return new SimpleDoubleProperty(this.valorDiaria); }

    @Override
    public String toString() {
        return getMarca() + " " + getModelo() + " (" + getPlaca() + ")";
    }
}