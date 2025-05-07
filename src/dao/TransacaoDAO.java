package dao;

import model.Transacao;
import util.Conexao;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.time.LocalDate;

public class TransacaoDAO {

    public void salvar(Transacao t) throws SQLException {
        String sql = "INSERT INTO transacoes (usuario_id, tipo, descricao, valor, data_transacao, categoria, observacao, recorrente, frequencia) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, t.getUsuarioId());
            stmt.setString(2, t.getTipo().name());
            stmt.setString(3, t.getDescricao());
            stmt.setDouble(4, t.getValor());
            stmt.setDate(5, java.sql.Date.valueOf(t.getData_transacao()));
            stmt.setString(6, t.getCategoria());
            stmt.setString(7, t.getObservacao());
            stmt.setBoolean(8, t.isRecorrente());
            stmt.setString(9, t.getFrequencia());
            stmt.executeUpdate();
        }
    }

    public List<Transacao> listarPorUsuario(int usuarioId) throws SQLException {
        String sql = "SELECT * FROM transacoes WHERE usuario_id = ?";
        List<Transacao> lista = new ArrayList<>();

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapearTransacao(rs));
            }
        }

        return lista;
    }

    public List<Transacao> listarPorPeriodo(int usuarioId, LocalDate inicio, LocalDate fim) throws SQLException {
        String sql = "SELECT * FROM transacoes WHERE usuario_id = ? AND data_transacao BETWEEN ? AND ?";
        List<Transacao> lista = new ArrayList<>();

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setDate(2, java.sql.Date.valueOf(inicio));
            stmt.setDate(3, java.sql.Date.valueOf(fim));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapearTransacao(rs));
            }
        }

        return lista;
    }

    public List<Transacao> listarPorData(int usuarioId, LocalDate data) throws SQLException {
        String sql = "SELECT * FROM transacoes WHERE usuario_id = ? AND data_transacao = ?";
        List<Transacao> lista = new ArrayList<>();

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setDate(2, java.sql.Date.valueOf(data));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapearTransacao(rs));
            }
        }

        return lista;
    }

    public List<Transacao> listarPorMesEAno(int usuarioId, int mes, int ano) throws SQLException {
        String sql = "SELECT * FROM transacoes WHERE usuario_id = ? AND strftime('%m', data_transacao) = ? AND strftime('%Y', data_transacao) = ?";
        List<Transacao> lista = new ArrayList<>();

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setString(2, String.format("%02d", mes));
            stmt.setString(3, String.valueOf(ano));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapearTransacao(rs));
            }
        }

        return lista;
    }

    public List<Transacao> listarPorAno(int usuarioId, int ano) throws SQLException {
        String sql = "SELECT * FROM transacoes WHERE usuario_id = ? AND strftime('%Y', data_transacao) = ?";
        List<Transacao> lista = new ArrayList<>();

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setString(2, String.valueOf(ano));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapearTransacao(rs));
            }
        }

        return lista;
    }

    public double calcularSaldo(int usuarioId) throws SQLException {
        String sql = "SELECT tipo, valor FROM transacoes WHERE usuario_id = ?";
        double saldo = 0;

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String tipo = rs.getString("tipo");
                double valor = rs.getDouble("valor");
                saldo += tipo.equalsIgnoreCase("ENTRADA") ? valor : -valor;
            }
        }

        return saldo;
    }

    public Map<String, Double> agruparPorCategoria(int usuarioId, LocalDate inicio, LocalDate fim) throws SQLException {
        String sql = "SELECT categoria, SUM(valor) as total FROM transacoes " +
                "WHERE usuario_id = ? AND data_transacao BETWEEN ? AND ? " +
                "GROUP BY categoria";

        Map<String, Double> resultado = new HashMap<>();

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setDate(2, java.sql.Date.valueOf(inicio));
            stmt.setDate(3, java.sql.Date.valueOf(fim));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                resultado.put(rs.getString("categoria"), rs.getDouble("total"));
            }
        }

        return resultado;
    }

    private Transacao mapearTransacao(ResultSet rs) throws SQLException {
        Transacao t = new Transacao();
        t.setId(rs.getInt("id"));
        t.setUsuarioId(rs.getInt("usuario_id"));
        t.setTipo(Transacao.Tipo.valueOf(rs.getString("tipo")));
        t.setDescricao(rs.getString("descricao"));
        t.setValor(rs.getDouble("valor"));
        t.setData_transacao(rs.getDate("data_transacao").toLocalDate());
        t.setCategoria(rs.getString("categoria"));
        t.setObservacao(rs.getString("observacao"));
        t.setRecorrente(rs.getBoolean("recorrente"));
        t.setFrequencia(rs.getString("frequencia"));
        return t;
    }

    public List<Transacao> buscarUltimas(int usuarioId, int limite) throws SQLException {
        List<Transacao> transacoes = new ArrayList<>();
        String sql = "SELECT * FROM transacoes WHERE usuario_id = ? ORDER BY data_transacao DESC LIMIT ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            stmt.setInt(2, limite);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                transacoes.add(mapearTransacao(rs));
            }
        }

        return transacoes;
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM transacoes WHERE id = ?";

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

}
