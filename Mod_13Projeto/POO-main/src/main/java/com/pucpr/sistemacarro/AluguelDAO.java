package com.pucpr.sistemacarro;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;

public class AluguelDAO {

    private static final String FILE_NAME = "alugueis.dat"; // << MUDAR NOME DO ARQUIVO

    public AluguelDAO() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            // Não precisa criar explicitamente, ObjectOutputStream fará isso.
            // Apenas para log, se desejar.
            System.out.println("Arquivo de dados " + FILE_NAME + " será criado na primeira escrita, se necessário.");
        }
    }

    private int getNextId(List<Aluguel> alugueis) { // Recebe a lista para otimizar
        OptionalInt maxId = alugueis.stream().mapToInt(Aluguel::getIdAluguel).max();
        return maxId.orElse(0) + 1;
    }

    @SuppressWarnings("unchecked") // Para o cast de (List<Aluguel>) ois.readObject()
    private List<Aluguel> readAllAlugueisFromFile() throws IOException, ClassNotFoundException {
        List<Aluguel> alugueis = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (file.exists() && file.length() > 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
                Object obj = ois.readObject();
                if (obj instanceof List) {
                    alugueis = (List<Aluguel>) obj;
                }
            }
        }
        return alugueis;
    }

    private void writeAllAlugueisToFile(List<Aluguel> alugueis) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME, false))) {
            oos.writeObject(alugueis);
        }
    }

    public void adicionarAluguel(Aluguel aluguel) throws IOException, ClassNotFoundException {
        List<Aluguel> alugueis = readAllAlugueisFromFile();
        aluguel.setIdAluguel(getNextId(alugueis));
        alugueis.add(aluguel);
        writeAllAlugueisToFile(alugueis);
    }

    public ObservableList<Aluguel> listarAlugueis() throws IOException, ClassNotFoundException {
        return FXCollections.observableArrayList(readAllAlugueisFromFile());
    }

    public void atualizarAluguel(Aluguel aluguelParaAtualizar) throws IOException, ClassNotFoundException {
        List<Aluguel> alugueis = readAllAlugueisFromFile();
        boolean found = false;
        for (int i = 0; i < alugueis.size(); i++) {
            if (alugueis.get(i).getIdAluguel() == aluguelParaAtualizar.getIdAluguel()) {
                alugueis.set(i, aluguelParaAtualizar);
                found = true;
                break;
            }
        }
        if (found) {
            writeAllAlugueisToFile(alugueis);
        } else {
            System.err.println("Aluguel com ID " + aluguelParaAtualizar.getIdAluguel() + " não encontrado para atualização.");
        }
    }

    public void deletarAluguel(int idAluguel) throws IOException, ClassNotFoundException {
        List<Aluguel> alugueis = readAllAlugueisFromFile();
        List<Aluguel> alugueisAtualizados = alugueis.stream()
                .filter(a -> a.getIdAluguel() != idAluguel)
                .collect(Collectors.toList());

        if (alugueis.size() != alugueisAtualizados.size()) {
            writeAllAlugueisToFile(alugueisAtualizados);
        } else {
            System.err.println("Aluguel com ID " + idAluguel + " não encontrado para deleção.");
        }
    }
}