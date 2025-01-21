package Mod_11.src;

import java.util.PriorityQueue;
import java.util.Queue;

public class queue {

    public static void main(String[] args) {
        Queue<String> fila = new PriorityQueue<>();
        fila.add("pau2");
        fila.add("pau1");
        fila.add("pau");
        while (fila.size()!= 0) {
            System.out.println(fila.remove());
        }
    }
}