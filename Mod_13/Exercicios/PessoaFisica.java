package Mod_13.Exercicios;

public class PessoaFisica extends Pessoa{
    private long cpf;

    public PessoaFisica() {
        super();
    }

    public void setCpf(long cpf) {
        this.cpf = cpf;
    }

    @Override
    public void mostrarDados() {
        super.mostrarDados();
        System.out.println("Cpf"+ cpf);
    }
}
