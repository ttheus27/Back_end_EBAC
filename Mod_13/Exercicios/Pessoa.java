package Mod_13.Exercicios;

public abstract class Pessoa {
    private String nome;
    private long numeroTelefone;

    public Pessoa (){
        this.nome = nome;
        this.numeroTelefone = numeroTelefone;
    }

    public void mostrarDados(){
        System.out.println("Dados" + nome + "\n"+numeroTelefone);
    }

    public long getNumeroTelefone() {
        return numeroTelefone;
    }

    public void setNumeroTelefone(int numeroTelefone) {
        this.numeroTelefone = numeroTelefone;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
