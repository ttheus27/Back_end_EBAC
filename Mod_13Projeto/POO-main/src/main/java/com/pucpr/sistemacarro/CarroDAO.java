package com.pucpr.sistemacarro;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;

public class CarroDAO {

    private static final String FILE_NAME = "carros.dat"; // << NOME DO ARQUIVO

    public CarroDAO() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("Arquivo de dados " + FILE_NAME + " será criado na primeira escrita, se necessário.");
        }
    }

    private int getNextId(List<Carro> carros) {
        OptionalInt maxId = carros.stream().mapToInt(Carro::getIdCarro).max();
        return maxId.orElse(0) + 1;
    }

    @SuppressWarnings("unchecked")
    private List<Carro> readAllCarrosFromFile() throws IOException, ClassNotFoundException {
        List<Carro> carros = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (file.exists() && file.length() > 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
                Object obj = ois.readObject();
                if (obj instanceof List) {
                    carros = (List<Carro>) obj;
                }
            }
        }
        return carros;
    }

    private void writeAllCarrosToFile(List<Carro> carros) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME, false))) {
            oos.writeObject(carros);
        }
    }

    public void adicionarCarro(Carro carro) throws IOException, ClassNotFoundException {
        List<Carro> carros = readAllCarrosFromFile();
        carro.setIdCarro(getNextId(carros));
        carros.add(carro);
        writeAllCarrosToFile(carros);
    }

    public ObservableList<Carro> listarCarros() throws IOException, ClassNotFoundException {
        return FXCollections.observableArrayList(readAllCarrosFromFile());
    }

    // Busca simples por modelo ou placa
    public ObservableList<Carro> buscarCarros(String termoBusca) throws IOException, ClassNotFoundException {
        List<Carro> todosCarros = readAllCarrosFromFile();
        if (termoBusca == null || termoBusca.trim().isEmpty()) {
            return FXCollections.observableArrayList(todosCarros);
        }
        String termoBuscaLower = termoBusca.toLowerCase();
        List<Carro> carrosFiltrados = todosCarros.stream()
                .filter(c -> (c.getModelo() != null && c.getModelo().toLowerCase().contains(termoBuscaLower)) ||
                        (c.getPlaca() != null && c.getPlaca().toLowerCase().contains(termoBuscaLower)) ||
                        (c.getMarca() != null && c.getMarca().toLowerCase().contains(termoBuscaLower)))
                .collect(Collectors.toList());
        return FXCollections.observableArrayList(carrosFiltrados);
    }


    public void atualizarCarro(Carro carroParaAtualizar) throws IOException, ClassNotFoundException {
        List<Carro> carros = readAllCarrosFromFile();
        boolean found = false;
        for (int i = 0; i < carros.size(); i++) {
            if (carros.get(i).getIdCarro() == carroParaAtualizar.getIdCarro()) {
                carros.set(i, carroParaAtualizar);
                found = true;
                break;
            }
        }
        if (found) {
            writeAllCarrosToFile(carros);
        } else {
            System.err.println("Carro com ID " + carroParaAtualizar.getIdCarro() + " não encontrado para atualização.");
        }
    }

    public void deletarCarro(int idCarro) throws IOException, ClassNotFoundException {
        List<Carro> carros = readAllCarrosFromFile();
        List<Carro> carrosAtualizados = carros.stream()
                .filter(c -> c.getIdCarro() != idCarro)
                .collect(Collectors.toList());

        if (carros.size() != carrosAtualizados.size()) {
            writeAllCarrosToFile(carrosAtualizados);
        } else {
            System.err.println("Carro com ID " + idCarro + " não encontrado para deleção.");
        }
    }
}