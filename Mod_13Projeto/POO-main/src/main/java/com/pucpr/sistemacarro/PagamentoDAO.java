package com.pucpr.sistemacarro;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;

public class PagamentoDAO {

    private static final String FILE_NAME = "pagamentos.dat"; // << MUDAR NOME DO ARQUIVO

    public PagamentoDAO() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("Arquivo de dados " + FILE_NAME + " será criado na primeira escrita, se necessário.");
        }
    }

    private int getNextId(List<Pagamento> pagamentos) {
        OptionalInt maxId = pagamentos.stream().mapToInt(Pagamento::getId).max();
        return maxId.orElse(0) + 1;
    }

    @SuppressWarnings("unchecked")
    private List<Pagamento> readAllPagamentosFromFile() throws IOException, ClassNotFoundException {
        List<Pagamento> pagamentos = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (file.exists() && file.length() > 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
                Object obj = ois.readObject();
                if (obj instanceof List) {
                    pagamentos = (List<Pagamento>) obj;
                }
            }
        }
        return pagamentos;
    }

    private void writeAllPagamentosToFile(List<Pagamento> pagamentos) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME, false))) {
            oos.writeObject(pagamentos);
        }
    }

    public void adicionarPagamento(Pagamento pagamento) throws IOException, ClassNotFoundException {
        List<Pagamento> pagamentos = readAllPagamentosFromFile();
        pagamento.setId(getNextId(pagamentos));
        pagamentos.add(pagamento);
        writeAllPagamentosToFile(pagamentos);
    }

    public ObservableList<Pagamento> listarPagamentos() throws IOException, ClassNotFoundException {
        return FXCollections.observableArrayList(readAllPagamentosFromFile());
    }

    public void atualizarPagamento(Pagamento pagamentoParaAtualizar) throws IOException, ClassNotFoundException {
        List<Pagamento> pagamentos = readAllPagamentosFromFile();
        boolean found = false;
        for (int i = 0; i < pagamentos.size(); i++) {
            if (pagamentos.get(i).getId() == pagamentoParaAtualizar.getId()) {
                pagamentos.set(i, pagamentoParaAtualizar);
                found = true;
                break;
            }
        }
        if (found) {
            writeAllPagamentosToFile(pagamentos);
        } else {
            System.err.println("Pagamento com ID " + pagamentoParaAtualizar.getId() + " não encontrado para atualização.");
        }
    }

    public void deletarPagamento(int id) throws IOException, ClassNotFoundException {
        List<Pagamento> pagamentos = readAllPagamentosFromFile();
        List<Pagamento> pagamentosAtualizados = pagamentos.stream()
                .filter(p -> p.getId() != id)
                .collect(Collectors.toList());

        if (pagamentos.size() != pagamentosAtualizados.size()) {
            writeAllPagamentosToFile(pagamentosAtualizados);
        } else {
            System.err.println("Pagamento com ID " + id + " não encontrado para deleção.");
        }
    }
}