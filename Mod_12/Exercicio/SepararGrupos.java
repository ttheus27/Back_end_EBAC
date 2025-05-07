package Mod_12.Exercicio;

import java.util.ArrayList;
import java.util.Scanner;

public class SepararGrupos {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        ArrayList<Pessoa>  grupoMacho = new ArrayList<>();
        ArrayList<Pessoa>  grupoFemea = new ArrayList<>();

        String nome, sexo;
        char continuar;
        do{
            System.out.print("Digite o nome: ");
            nome = sc.nextLine();

            System.out.print("Digite o sexo (M para masculino, F para feminino): ");
            sexo = sc.nextLine().toUpperCase();  

            if (sexo.equals("M")) {
                grupoMacho.add(new Pessoa(nome, sexo));
            } else if (sexo.equals("F")) {
                grupoFemea.add(new Pessoa(nome, sexo));
            } else {
                System.out.println("Sexo inválido! Use 'M' para masculino ou 'F' para feminino.");
            }
            System.out.print("Deseja adicionar outra pessoa? (S para sim, N para não): ");
            continuar = sc.nextLine().toUpperCase().charAt(0);
        } while (continuar == 'S');

        System.out.println("\nGrupo Masculino:");
        for (Pessoa p : grupoMacho) {
            System.out.println(p.nome);
        }

        System.out.println("\nGrupo Feminino:");
        for (Pessoa p : grupoFemea) {
            System.out.println(p.nome);
        }

        sc.close();

    }
}
