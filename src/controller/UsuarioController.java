package controller;

import dao.UsuarioDAO;
import model.Usuario;

public class UsuarioController {
    private UsuarioDAO dao = new UsuarioDAO();

    public void cadastrar(String nome, String email, String senha){
        Usuario u = new Usuario();
        u.setNome(nome);
        u.setEmail(email);
        u.setSenha(senha);

        try{
            dao.salvar(u);
            System.out.println("Usuário Cadastrado com sucesso!");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Usuario autenticar(String email, String senha){
        try{
            Usuario usuario = dao.login(email,senha);
            if (usuario != null){
                System.out.println("Login bem-sucedido. Bem-vindo, " + usuario.getNome() + "+");
                return usuario;
            }else {
                System.out.println("Email ou senha inválidos.");
                return null;
            }
        }catch (Exception e){
            System.err.println("Erro ao fazer login: " + e.getMessage());
            return null;
        }
    }
}
