package Mod_11.src; 

import java.text.CollationElementIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class list {
    
    public static void main(String[] args) {
        exemploListaSimples();
        exemploListaSimplesOrdemAscendente();
        exemploNumeros();
    }

    private static void exemploListaSimples(){
        System.out.println("Exemplo de lista Simples");
        List<String> lista = new ArrayList<>();
        lista.add("matheus");
        lista.add("matheuss");
        lista.add("matheusss");
        lista.add("matheussss");
        lista.add("matheusssss");
        System.out.println(lista);
        System.out.println("");

    }

    private static void exemploListaSimplesOrdemAscendente(){
        System.out.println("Exemple Lista Simples Ordem Ascendente");
        List<String> lista = new ArrayList<>();
        lista.add("cmatheus");
        lista.add("amatheuss");
        lista.add("matheusss");
        lista.add("matheussss");
        lista.add("tmatheusssss");
        Collections.sort(lista);
        System.out.println(lista);
        System.out.println("");
    }

    private static void exemploNumeros(){
        
        List<Integer> lista = new ArrayList<>();
        for(int i = 0; i <= 10; ++i){
            lista.add(i);
        }

        System.out.println(lista);

    }
    
}  