package controller;

import dao.TransacaoDAO;
import model.Transacao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TransacaoController {
    private final TransacaoDAO dao;

    public TransacaoController() {
        this.dao = new TransacaoDAO();
    }

    public void cadastrar(Transacao t) {
        try {
            dao.salvar(t);
            System.out.println("Transação cadastrada com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar transação: " + e.getMessage());
        }
    }

    public List<Transacao> listar(int usuarioId) {
        try {
            return dao.listarPorUsuario(usuarioId);
        } catch (SQLException e) {
            System.err.println("Erro ao listar transações: " + e.getMessage());
            return Collections.emptyList();
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

    public List<Transacao> buscarPorPeriodo(int usuarioId, LocalDate inicio, LocalDate fim) {
        try {
            return dao.listarPorPeriodo(usuarioId, inicio, fim);
        } catch (SQLException e) {
            System.err.println("Erro ao buscar por período: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Transacao> listarPorData(int usuarioId, LocalDate data) {
        try {
            return dao.listarPorData(usuarioId, data);
        } catch (SQLException e) {
            System.err.println("Erro ao listar por data: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Transacao> listarPorMesEAno(int usuarioId, int mes, int ano) {
        try {
            return dao.listarPorMesEAno(usuarioId, mes, ano);
        } catch (SQLException e) {
            System.err.println("Erro ao listar por mês e ano: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Transacao> listarPorAno(int usuarioId, int ano) {
        try {
            return dao.listarPorAno(usuarioId, ano);
        } catch (SQLException e) {
            System.err.println("Erro ao listar por ano: " + e.getMessage());
            return Collections.emptyList();
        }
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

    public void excluir(int id) {
        try {
            dao.excluir(id);
            System.out.println("Transação excluída com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao excluir transação: " + e.getMessage());
        }
    }
}
