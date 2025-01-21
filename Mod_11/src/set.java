package Mod_11.src;



import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class set {
    public static void main(String[] args) {
        listaSimples();
        listaSilmpesOrdem();
    }
    private static void listaSimples() {
        Set<String> lista = new HashSet<>();
        lista.add("pals");
        lista.add("pals1");
        lista.add("pals2");
        lista.add("pals");
        System.out.println(lista);
        System.out.println("");
    }
    private static void listaSilmpesOrdem(){
        Set<String> lista = new HashSet<>();
        lista.add("palsa");
        lista.add("pals2");
        lista.add("pals1");
        lista.add("pals3");
        System.out.println(lista);
        System.out.println("");
    }

}
