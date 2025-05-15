package Mod_13.scr;

public class Main {
    public static void main(String[] args) {
        ICarro carro = new CarroDePasseio();
        carro.Andar();
        carro.parar();

        Motoca motoca = new Motoca();
        motoca.Andar();
        motoca.parar();

    }
}
