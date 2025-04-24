package controller;

import dao.TransacaoDAO;
import model.Transacao;

import java.sql.SQLException;
import java.util.List;

public class TransacaoController {
    private TransacaoDAO dao;

    public TransacaoController(){
        dao = new TransacaoDAO();
    }

    public void cadastrar(Transacao t){
        try{
            dao.salvar(t);
            System.out.println("Transação cadastrada com sucesso!");
        }catch (SQLException e){
            System.err.println("Erro ao cadastrar transação: " + e.getMessage());
        }
    }

    public List<Transacao> Listar(int usuarioId){
        try{
            return dao.listarPorUsuario(usuarioId);
        }catch (SQLException e){
            System.err.println("Erro ao listar transações: " + e.getMessage());
            return null;
        }
    }

    public double consultarSaldo(int usuarioId){
        try{
            return dao.calcularSaldo(usuarioId);
        } catch (SQLException e) {
            System.err.println("Erro ao calcular saldo: " + e.getMessage());
            return 0;
        }
    }
}
