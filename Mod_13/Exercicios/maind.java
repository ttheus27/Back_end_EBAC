package Mod_13.Exercicios;

public class maind {
    public static void main(String[] args) {
        PessoaFisica fisica = new PessoaFisica();
        fisica.setNome("Matheus");
        fisica.setNumeroTelefone(41995);
        fisica.setCpf(111111);
        fisica.mostrarDados();

        PessoaJuridica juridica = new PessoaJuridica();
        juridica.setNome("Roberta");
        juridica.setNumeroTelefone(551111);
        juridica.setCnpj(1111111);
        juridica.mostrarDados();


    }
}
