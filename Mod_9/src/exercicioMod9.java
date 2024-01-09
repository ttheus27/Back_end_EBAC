package Mod_9.src;
import java.util.Scanner;

public class exercicioMod9 {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        System.out.println("Digite sua idade: ");
        int idade = s.nextInt();
        System.out.println("Vc tem " + idade + " anos.");
        Integer idade1 = idade;
        System.out.println("Agora com o casting");
        System.out.println("Vc tem " + idade1 + " anos.");
     }


}
