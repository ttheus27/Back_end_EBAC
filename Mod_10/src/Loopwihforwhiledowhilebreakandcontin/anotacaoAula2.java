package Mod_10.src.Loopwihforwhiledowhilebreakandcontin;
import java.util.Scanner;

public class anotacaoAula2 {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        System.out.println("Digite um numero para fazer tabuada:");
           int num = s.nextInt();
        for (int i = 0; i <= 10; i++){
            System.out.println(num + " X " + i + " = " + num*i);
        }
    }
}
