package controller;

import dao.UsuarioDAO;
import model.Usuario;

public class UsuarioController {
    private UsuarioDAO dao = new UsuarioDAO();

    public boolean cadastrar(Usuario u) {
        try {
            dao.salvar(u);
            System.out.println("Usuário cadastrado com sucesso!");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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

    public Usuario buscarPorId(int id) {
        try {
            return dao.buscarPorId(id);
        } catch (Exception e) {
            return null;
        }
    }
}
