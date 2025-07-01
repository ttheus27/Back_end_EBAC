package com.pucpr.sistemacarro;

import javafx.beans.property.*;
import java.io.Serializable; // << IMPORTAR
import java.time.LocalDate;
// DateTimeFormatter e CSV_DELIMITER não são mais necessários para serialização binária

public class Funcionario implements Serializable { // << IMPLEMENTAR Serializable

    private static final long serialVersionUID = 3L; // << Adicionar/Atualizar serialVersionUID

    // Campos alterados de Properties para tipos primitivos/objetos para serialização
    private int id;
    private String nome;
    private String cargo;
    private double salario;
    private LocalDate dataContratacao; // LocalDate é serializável

    public Funcionario() {}

    public Funcionario(int id, String nome, String cargo, double salario, LocalDate dataContratacao) {
        this.id = id;
        this.nome = nome;
        this.cargo = cargo;
        this.salario = salario;
        this.dataContratacao = dataContratacao;
    }

    // Construtor para novos funcionários onde o ID é gerado depois
    public Funcionario(String nome, String cargo, double salario, LocalDate dataContratacao) {
        this.nome = nome;
        this.cargo = cargo;
        this.salario = salario;
        this.dataContratacao = dataContratacao;
    }

    // Getters e Setters para os campos
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public double getSalario() { return salario; }
    public void setSalario(double salario) { this.salario = salario; }

    public LocalDate getDataContratacao() { return dataContratacao; }
    public void setDataContratacao(LocalDate dataContratacao) { this.dataContratacao = dataContratacao; }

    // Métodos Property para JavaFX TableView
    public IntegerProperty idProperty() { return new SimpleIntegerProperty(this.id); }
    public StringProperty nomeProperty() { return new SimpleStringProperty(this.nome); }
    public StringProperty cargoProperty() { return new SimpleStringProperty(this.cargo); }
    public DoubleProperty salarioProperty() { return new SimpleDoubleProperty(this.salario); }
    public ObjectProperty<LocalDate> dataContratacaoProperty() { return new SimpleObjectProperty<>(this.dataContratacao); }

    // toCsvString e fromCsvString são removidos

    @Override
    public String toString() {
        return getNome() + " (" + getCargo() + ")";
    }
}