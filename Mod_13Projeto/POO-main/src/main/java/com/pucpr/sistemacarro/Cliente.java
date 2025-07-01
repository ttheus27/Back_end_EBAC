package com.pucpr.sistemacarro;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable; // << IMPORTAR

public class Cliente implements Serializable { // << IMPLEMENTAR Serializable

    private static final long serialVersionUID = 1L; // << Adicionar serialVersionUID

    // Campos alterados de Properties para tipos primitivos/String para serialização
    private int idCliente;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private String cnh;

    // Construtores
    public Cliente() {}

    public Cliente(int idCliente, String nome, String cpf, String telefone, String email, String cnh) {
        this.idCliente = idCliente;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.cnh = cnh;
    }

    public Cliente(String nome, String cpf, String telefone, String email, String cnh) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.cnh = cnh;
        // O ID será definido pelo DAO ao adicionar
    }

    // Getters e Setters para os campos
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCnh() { return cnh; }
    public void setCnh(String cnh) { this.cnh = cnh; }

    // Métodos Property para JavaFX TableView
    public IntegerProperty idClienteProperty() { return new SimpleIntegerProperty(this.idCliente); }
    public StringProperty nomeProperty() { return new SimpleStringProperty(this.nome); }
    public StringProperty cpfProperty() { return new SimpleStringProperty(this.cpf); }
    public StringProperty telefoneProperty() { return new SimpleStringProperty(this.telefone); }
    public StringProperty emailProperty() { return new SimpleStringProperty(this.email); }
    public StringProperty cnhProperty() { return new SimpleStringProperty(this.cnh); }

    // Métodos toCsvString e fromCsvString são removidos

    @Override
    public String toString() {
        return getNome() + " (CPF: " + getCpf() + ")";
    }
}