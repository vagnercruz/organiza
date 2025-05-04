package controller;

import model.Transacao;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.time.LocalDate;
import java.util.Map;
import dao.TransacaoDAO;

public class TransacaoController {
    private TransacaoDAO dao;

    public TransacaoController() {
        dao = new TransacaoDAO();
    }

    public void cadastrar(Transacao t) {
        try {
            dao.salvar(t);
            System.out.println("Transação cadastrada com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar transação: " + e.getMessage());
        }
    }

    public List<Transacao> Listar(int usuarioId) {
        try {
            return dao.listarPorUsuario(usuarioId);
        } catch (SQLException e) {
            System.err.println("Erro ao listar transações: " + e.getMessage());
            return null;
        }
    }

    public double consultarSaldo(int usuarioId) {
        try {
            return dao.calcularSaldo(usuarioId);
        } catch (SQLException e) {
            System.err.println("Erro ao calcular saldo: " + e.getMessage());
            return 0;
        }
    }

    public List<Transacao> buscarPorPeriodo(int usuarioId, LocalDate inicio, LocalDate fim) throws SQLException {
        TransacaoDAO dao = new TransacaoDAO();
        return dao.listarPorPeriodo(usuarioId, inicio, fim);
    }

    public List<Transacao> listarPorData(int usuarioId, LocalDate data) throws Exception {
        TransacaoDAO dao = new TransacaoDAO();
        return dao.listarPorData(usuarioId, data);
    }

    public List<Transacao> listarPorMesEAno(int usuarioId, int mes, int ano) throws Exception {
        TransacaoDAO dao = new TransacaoDAO();
        return dao.listarPorMesEAno(usuarioId, mes, ano);
    }

    public List<Transacao> listarPorAno(int usuarioId, int ano) throws Exception {
        TransacaoDAO dao = new TransacaoDAO();
        return dao.listarPorAno(usuarioId, ano);
    }

    public Map<String, Double> agruparPorCategoria(int usuarioId, LocalDate inicio, LocalDate fim) {
        try {
            return dao.agruparPorCategoria(usuarioId, inicio, fim);
        } catch (SQLException e) {
            System.err.println("Erro ao agrupar por categoria: " + e.getMessage());
            return Collections.emptyMap();
        }
    }

    public List<Transacao> buscarUltimas(int usuarioId, int limite) {
        try {
            return dao.buscarUltimas(usuarioId, limite);
        } catch (SQLException e) {
            System.err.println("Erro ao buscar últimas transações: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
