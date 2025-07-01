package Mod_13Projeto.DAO;


import Mod_13Projeto.Cliente;

import java.util.Collection;

/**
 * @author rodrigo.pires
 */
public interface IClienteDAO {

    public Boolean cadastrar(Cliente cliente);

    public void excluir(Long cpf);

    public void alterar(Cliente cliente);

    public Cliente consultar(Long cpf);

    public Collection<Cliente> buscarTodos();
}