package dao;

import model.Usuario;
import util.Conexao;
import util.Seguranca;

import java.sql.*;

public class UsuarioDAO {

    public void salvar(Usuario u) throws SQLException {
        String sql = "INSERT INTO usuarios (nome, email, senha) VALUES (?, ?, ?)";

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, u.getNome());
            stmt.setString(2, u.getEmail());
            stmt.setString(2, Seguranca.hashSenha(u.getSenha()));
            stmt.executeUpdate();
        }

    }

    public Usuario login(String nomeUsuario, String senha) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE nome_usuario = ? AND senha = ?";

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nomeUsuario);
            stmt.setString(2, Seguranca.hashSenha(senha));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNome(rs.getString("nome"));
                u.setEmail(rs.getString("email"));
                u.setSenha(rs.getString("senha"));
                u.setNomeUsuario(rs.getString("nome_usuario"));
                return u;
            } else {
                return null;
            }
        }
    }

    public Usuario buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        PreparedStatement stmt = Conexao.conectar().prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Usuario u = new Usuario();
            u.setId(rs.getInt("id"));
            u.setNome(rs.getString("nome"));
            u.setEmail(rs.getString("email"));
            u.setSenha(rs.getString("senha"));
            return u;
        }

        return null;
    }
}
