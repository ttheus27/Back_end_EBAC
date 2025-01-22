package Mod_11;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


public class TarefaColecoes {

    public static void main(String[] args) {
        parteUm();
        parteDois();
    }

    private static void parteUm(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Escreva os nomes desejados separando por virgulas");
        String nome = sc.nextLine();
        String[] array = nome.split(",");

        List<String> lista = new ArrayList<>(Arrays.asList(array));
        lista.replaceAll(String::trim);
        Collections.sort(lista);

        System.err.println(lista);
    }

    static class Pessoa {
        String nome;
        String sexo;

        Pessoa(String nome, String sexo) {
            this.nome = nome;
            this.sexo = sexo;
        }

        @Override
        public String toString() {
            return "Nome: " + nome + ", Sexo: " + sexo;
        }
    }

    private static void parteDois() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Insira os objetos no formato Nome:Sexo, separados por vírgulas:");
        String entrada = sc.nextLine();

        String[] objetos = entrada.split(",");

        List<Pessoa> lista = new ArrayList<>();

        for (String obj : objetos) {
            String[] atributos = obj.split(":");
            if (atributos.length == 2) {
                String nome = atributos[0].trim();
                String sexo = atributos[1].trim().toLowerCase();

                lista.add(new Pessoa(nome, sexo));
            } else {
                System.out.println("Formato inválido para o objeto: " + obj);
            }
        }

        System.out.println("Lista de pessoas:");
        for (Pessoa pessoa : lista) {
            System.out.println(pessoa);
        }
    }
    
}