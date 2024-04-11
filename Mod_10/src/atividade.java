package Mod_10.src;
import java.util.Scanner;

public class atividade {
    static Scanner s = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("Digite a primeira nota:");
        int nota1 = s.nextInt();
        
        System.out.println("Digite a segunda nota:");
        int nota2 = s.nextInt();
        
        System.out.println("Digite a terceira nota:");
        int nota3 = s.nextInt();
        
        System.out.println("Digite a quarta nota:");
        int nota4 = s.nextInt();
        
        int media = (nota1 + nota2 + nota3 + nota4) / 4;

        String mediaF = getMedia(media);
    }
    
    public static String getMedia(int media) {
        if (media >= 7){
            System.out.println("Seu desempenho foi acima da media " + media);
    
        } else if (media >= 4 && media < 7 ){
            System.out.println("Seu desempenho foi a baixo da media " + media);
            System.out.println("Por favor faça a rec");
        } else {
            System.out.println("Voce foi reprovado, sua nota foi" + media);
        }
        return null;
        
        
    }

}
