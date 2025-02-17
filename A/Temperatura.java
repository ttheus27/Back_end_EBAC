package A;

import java.util.Scanner;

public class Temperatura {
    public static void main(String[] args) {
        temperatura();
    }
    public static void  temperatura(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Escreva a atemperatura para a converção");
        int c = sc.nextInt();
        System.out.println("++++++++++++++++++++");
        int f = (int) (c * 1.8 + 32);
        int k = (int) (c + 273.15);
        System.out.print("\n Aqui esta em Fahrenheit: "+f );
        System.out.print("\n Aqui esta em Kelvin: "+k+"\n");


    }
}
