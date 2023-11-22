public class Primeira_classe {

    public static void main(String[] args) {
        System.out.println("Ola mundo");
        Cliente cliente = new Cliente();
        cliente.cadastrarEndereco("Rua 1");
        cliente.setCodigo(1);
        System.out.println(cliente.getCodigo());
        System.out.println(cliente.retornarNomeCliente());
        System.out.println(cliente.getValorTotal());
    }
}