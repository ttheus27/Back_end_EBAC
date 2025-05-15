package Mod_13.scr;

public class Motoca implements ICarro{
    @Override
    public void Andar() {
        System.out.println("Esta fazendo RAMRAMRAMRAMDAMDAM");
    }

    @Override
    public void parar(){
        System.out.println("Motoca morreu:(");
    }
}
