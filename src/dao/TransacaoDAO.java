package dao;

import model.Transacao;
import model.TipoTransacao;
import util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;


public class TransacaoDAO {

    public void salvar(Transacao t) throws SQLException {
        String sql = "INSERT INTO transacoes (usuario_id, tipo, descricao, valor, data_transacao) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, t.getUsuarioId());
            stmt.setString(2, t.getTipo().name()); // Usa o nome do enum
            stmt.setString(3, t.getDescricao());
            stmt.setDouble(4, t.getValor());
            stmt.setDate(5, Date.valueOf(t.getData_transacao()));
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
                Transacao t = new Transacao();
                t.setId(rs.getInt("id"));
                t.setUsuarioId(rs.getInt("usuario_id"));
                t.setTipo(TipoTransacao.valueOf(rs.getString("tipo").toUpperCase())); // converte para enum
                t.setDescricao(rs.getString("descricao"));
                t.setValor(rs.getDouble("valor"));
                t.setData_transacao(rs.getDate("data_transacao").toLocalDate());
                lista.add(t);
            }
        }

        return lista;
    }

    public double calcularSaldo(int usuarioId) throws SQLException {
        String sql = """
            SELECT 
                COALESCE(SUM(CASE WHEN UPPER(tipo) = 'ENTRADA' THEN valor ELSE 0 END), 0) -
                COALESCE(SUM(CASE WHEN UPPER(tipo) = 'SAIDA' THEN valor ELSE 0 END), 0) AS saldo
            FROM transacoes
            WHERE usuario_id = ?
        """;
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("saldo");
            }
        }
        return 0;
    }

    public List<Transacao> listarPorPeriodo(int usuarioId, LocalDate inicio, LocalDate fim) throws SQLException {
        String sql = "SELECT * FROM transacoes WHERE usuario_id = ? AND data_transacao BETWEEN ? AND ? ORDER BY data_transacao";
        List<Transacao> lista = new ArrayList<>();

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setDate(2, Date.valueOf(inicio));
            stmt.setDate(3, Date.valueOf(fim));

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Transacao t = new Transacao();
                t.setId(rs.getInt("id"));
                t.setUsuarioId(rs.getInt("usuario_id"));
                t.setTipo(TipoTransacao.valueOf(rs.getString("tipo").toUpperCase()));
                t.setDescricao(rs.getString("descricao"));
                t.setValor(rs.getDouble("valor"));
                t.setData_transacao(rs.getDate("data_transacao").toLocalDate());
                lista.add(t);
            }
        }

        return lista;
    }

    public List<Transacao> listarPorData(int usuarioId, LocalDate data) throws SQLException {
        String sql = "SELECT * FROM transacoes WHERE usuario_id = ? AND data_transacao = ?";
        List<Transacao> lista = new ArrayList<>();

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setDate(2, Date.valueOf(data));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Transacao t = construirTransacao(rs);
                lista.add(t);
            }
        }

        return lista;
    }

    public List<Transacao> listarPorMesEAno(int usuarioId, int mes, int ano) throws SQLException {
        String sql = "SELECT * FROM transacoes WHERE usuario_id = ? AND EXTRACT(MONTH FROM data_transacao) = ? AND EXTRACT(YEAR FROM data_transacao) = ?";
        List<Transacao> lista = new ArrayList<>();

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setInt(2, mes);
            stmt.setInt(3, ano);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Transacao t = construirTransacao(rs);
                lista.add(t);
            }
        }

        return lista;
    }

    public List<Transacao> listarPorAno(int usuarioId, int ano) throws SQLException {
        String sql = "SELECT * FROM transacoes WHERE usuario_id = ? AND EXTRACT(YEAR FROM data_transacao) = ?";
        List<Transacao> lista = new ArrayList<>();

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setInt(2, ano);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Transacao t = construirTransacao(rs);
                lista.add(t);
            }
        }

        return lista;
    }

    private Transacao construirTransacao(ResultSet rs) throws SQLException {
        Transacao t = new Transacao();
        t.setId(rs.getInt("id"));
        t.setUsuarioId(rs.getInt("usuario_id"));
        t.setTipo(TipoTransacao.valueOf(rs.getString("tipo").toUpperCase()));
        t.setDescricao(rs.getString("descricao"));
        t.setValor(rs.getDouble("valor"));
        t.setData_transacao(rs.getDate("data_transacao").toLocalDate());
        return t;
    }
}
