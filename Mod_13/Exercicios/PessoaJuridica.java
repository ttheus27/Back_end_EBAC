package Mod_13.Exercicios;

public class PessoaJuridica extends Pessoa{
    private long cnpj;

    public PessoaJuridica() {
        super();
    }

    public void setCnpj(long cnpj) {
        this.cnpj = cnpj;
    }
    @Override
    public void mostrarDados() {
        super.mostrarDados();
        System.out.println("cnpj"+ cnpj);
    }
}
