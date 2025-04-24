package dao;

import model.Transacao;
import util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransacaoDAO {

    public void salvar(Transacao t) throws SQLException {
        String sql = "INSERT INTO transacoes (usuario_id, tipo, descricao, valor, data_transacao) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, t.getUsuarioId());
            stmt.setString(2, t.getTipo());
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
                t.setTipo(rs.getString("tipo"));
                t.setDescricao(rs.getString("descricao"));
                t.setValor(rs.getDouble("valor"));
                t.setData_transacao(rs.getDate("data_transacao").toLocalDate());
                lista.add(t);
            }
        }

        return lista;
    }

    public double calcularSaldo(int usuarioId) throws SQLException {
        String sql = "SELECT tipo, SUM(valor) FROM transacoes WHERE usuario_id = ? GROUP BY tipo";
        PreparedStatement stmt = Conexao.conectar().prepareStatement(sql);
        stmt.setInt(1, usuarioId);
        ResultSet rs = stmt.executeQuery();

        double entradas = 0;
        double saidas = 0;

        while (rs.next()) {
            String tipo = rs.getString("tipo");
            double total = rs.getDouble("sum");

            if (tipo.equalsIgnoreCase("entrada")) {
                entradas += total;
            } else if (tipo.equalsIgnoreCase("saida")) {
                saidas += total;
            }
        }

        rs.close();
        stmt.close();

        return entradas - saidas;
    }
}
