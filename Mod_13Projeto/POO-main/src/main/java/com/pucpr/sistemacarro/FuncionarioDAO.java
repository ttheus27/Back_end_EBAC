package com.pucpr.sistemacarro;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;

public class FuncionarioDAO {

    private static final String FILE_NAME = "funcionarios.dat"; // << MUDAR NOME DO ARQUIVO

    public FuncionarioDAO() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("Arquivo de dados " + FILE_NAME + " será criado na primeira escrita, se necessário.");
        }
    }

    private int getNextId(List<Funcionario> funcionarios) {
        OptionalInt maxId = funcionarios.stream().mapToInt(Funcionario::getId).max();
        return maxId.orElse(0) + 1;
    }

    @SuppressWarnings("unchecked")
    private List<Funcionario> readAllFuncionariosFromFile() throws IOException, ClassNotFoundException {
        List<Funcionario> funcionarios = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (file.exists() && file.length() > 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
                Object obj = ois.readObject();
                if (obj instanceof List) {
                    funcionarios = (List<Funcionario>) obj;
                }
            }
        }
        return funcionarios;
    }

    private void writeAllFuncionariosToFile(List<Funcionario> funcionarios) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME, false))) {
            oos.writeObject(funcionarios);
        }
    }

    public void adicionarFuncionario(Funcionario funcionario) throws IOException, ClassNotFoundException {
        List<Funcionario> funcionarios = readAllFuncionariosFromFile();
        funcionario.setId(getNextId(funcionarios));
        funcionarios.add(funcionario);
        writeAllFuncionariosToFile(funcionarios);
    }

    public ObservableList<Funcionario> listarFuncionarios() throws IOException, ClassNotFoundException {
        return FXCollections.observableArrayList(readAllFuncionariosFromFile());
    }

    public void atualizarFuncionario(Funcionario funcionarioParaAtualizar) throws IOException, ClassNotFoundException {
        List<Funcionario> funcionarios = readAllFuncionariosFromFile();
        boolean found = false;
        for (int i = 0; i < funcionarios.size(); i++) {
            if (funcionarios.get(i).getId() == funcionarioParaAtualizar.getId()) {
                funcionarios.set(i, funcionarioParaAtualizar);
                found = true;
                break;
            }
        }
        if (found) {
            writeAllFuncionariosToFile(funcionarios);
        } else {
            System.err.println("Funcionário com ID " + funcionarioParaAtualizar.getId() + " não encontrado para atualização.");
        }
    }

    public void deletarFuncionario(int id) throws IOException, ClassNotFoundException {
        List<Funcionario> funcionarios = readAllFuncionariosFromFile();
        List<Funcionario> funcionariosAtualizados = funcionarios.stream()
                .filter(f -> f.getId() != id)
                .collect(Collectors.toList());

        if (funcionarios.size() != funcionariosAtualizados.size()) {
            writeAllFuncionariosToFile(funcionariosAtualizados);
        } else {
            System.err.println("Funcionário com ID " + id + " não encontrado para deleção.");
        }
    }
}