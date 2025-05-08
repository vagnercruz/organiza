package dao;

import model.Conta;
import util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContaDAO {

    public void salvar(Conta conta) throws SQLException {
        String sql = "INSERT INTO contas (usuario_id, nome, tipo, saldo_inicial) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, conta.getUsuarioId());
            stmt.setString(2, conta.getNome());
            stmt.setString(3, conta.getTipo().name());
            stmt.setDouble(4, conta.getSaldoInicial());
            stmt.executeUpdate();
        }
    }

    public List<Conta> listarPorUsuario(int usuarioId) throws SQLException {
        String sql = "SELECT * FROM contas WHERE usuario_id = ?";
        List<Conta> contas = new ArrayList<>();

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Conta conta = new Conta(
                        rs.getInt("id"),
                        rs.getInt("usuario_id"),
                        rs.getString("nome"),
                        Conta.Tipo.valueOf(rs.getString("tipo")),
                        rs.getDouble("saldo_inicial")
                );
                contas.add(conta);
            }
        }

        return contas;
    }

    public void atualizar(Conta conta) throws SQLException {
        String sql = "UPDATE contas SET nome = ?, tipo = ?, saldo_inicial = ? WHERE id = ?";
        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, conta.getNome());
            stmt.setString(2, conta.getTipo().name());
            stmt.setDouble(3, conta.getSaldoInicial());
            stmt.setInt(4, conta.getId());
            stmt.executeUpdate();
        }
    }

    public void excluir(int contaId) throws SQLException {
        String sql = "DELETE FROM contas WHERE id = ?";
        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, contaId);
            stmt.executeUpdate();
        }
    }
}
