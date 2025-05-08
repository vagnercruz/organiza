package controller;

import dao.ContaDAO;
import model.Conta;

import java.sql.SQLException;
import java.util.List;

public class ContaController {
    private ContaDAO dao = new ContaDAO();

    public void cadastrar(Conta conta) throws SQLException {
        dao.salvar(conta);
    }

    public List<Conta> listarPorUsuario(int usuarioId) throws SQLException {
        return dao.listarPorUsuario(usuarioId);
    }

    public void atualizar(Conta conta) throws SQLException {
        dao.atualizar(conta);
    }

    public void excluir(int contaId) throws SQLException {
        dao.excluir(contaId);
    }
}
