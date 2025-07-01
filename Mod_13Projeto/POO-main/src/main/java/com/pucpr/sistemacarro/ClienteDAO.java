package com.pucpr.sistemacarro;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;

public class ClienteDAO {

    private static final String FILE_NAME = "clientes.dat"; // << MUDAR NOME DO ARQUIVO

    public ClienteDAO() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("Arquivo de dados " + FILE_NAME + " será criado na primeira escrita, se necessário.");
        }
    }

    private int getNextId(List<Cliente> clientes) {
        OptionalInt maxId = clientes.stream().mapToInt(Cliente::getIdCliente).max();
        return maxId.orElse(0) + 1;
    }

    @SuppressWarnings("unchecked")
    private List<Cliente> readAllClientesFromFile() throws IOException, ClassNotFoundException {
        List<Cliente> clientes = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (file.exists() && file.length() > 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
                Object obj = ois.readObject();
                if (obj instanceof List) {
                    clientes = (List<Cliente>) obj;
                }
            }
        }
        return clientes;
    }

    private void writeAllClientesToFile(List<Cliente> clientes) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME, false))) {
            oos.writeObject(clientes);
        }
    }

    public void adicionarCliente(Cliente cliente) throws IOException, ClassNotFoundException {
        List<Cliente> clientes = readAllClientesFromFile();
        cliente.setIdCliente(getNextId(clientes));
        clientes.add(cliente);
        writeAllClientesToFile(clientes);
    }

    public ObservableList<Cliente> listarClientes() throws IOException, ClassNotFoundException {
        return FXCollections.observableArrayList(readAllClientesFromFile());
    }

    public ObservableList<Cliente> buscarClientePorNome(String nomeBusca) throws IOException, ClassNotFoundException {
        List<Cliente> todosClientes = readAllClientesFromFile();
        if (nomeBusca == null || nomeBusca.trim().isEmpty()) {
            return FXCollections.observableArrayList(todosClientes);
        }
        String nomeBuscaLower = nomeBusca.toLowerCase();
        List<Cliente> clientesFiltrados = todosClientes.stream()
                .filter(c -> c.getNome() != null && c.getNome().toLowerCase().contains(nomeBuscaLower))
                .collect(Collectors.toList());
        return FXCollections.observableArrayList(clientesFiltrados);
    }

    public void atualizarCliente(Cliente clienteParaAtualizar) throws IOException, ClassNotFoundException {
        List<Cliente> clientes = readAllClientesFromFile();
        boolean found = false;
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getIdCliente() == clienteParaAtualizar.getIdCliente()) {
                clientes.set(i, clienteParaAtualizar);
                found = true;
                break;
            }
        }
        if (found) {
            writeAllClientesToFile(clientes);
        } else {
            System.err.println("Cliente com ID " + clienteParaAtualizar.getIdCliente() + " não encontrado para atualização.");
        }
    }

    public void deletarCliente(int idCliente) throws IOException, ClassNotFoundException {
        List<Cliente> clientes = readAllClientesFromFile();
        List<Cliente> clientesAtualizados = clientes.stream()
                .filter(c -> c.getIdCliente() != idCliente)
                .collect(Collectors.toList());

        if (clientes.size() != clientesAtualizados.size()) {
            writeAllClientesToFile(clientesAtualizados);
        } else {
            System.err.println("Cliente com ID " + idCliente + " não encontrado para deleção.");
        }
    }
}