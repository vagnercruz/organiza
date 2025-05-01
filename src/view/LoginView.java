package view;

import controller.UsuarioController;
import model.Usuario;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    private JTextField usuarioField;
    private JPasswordField senhaField;
    private JButton loginButton;
    private JButton cadastrarButton;

    private UsuarioController usuarioController = new UsuarioController();

    public LoginView() {
        setTitle("Login - Gestão Financeira");
        setSize(350, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Usuário:"));
        usuarioField = new JTextField();
        panel.add(usuarioField);

        panel.add(new JLabel("Senha:"));
        senhaField = new JPasswordField();
        panel.add(senhaField);

        loginButton = new JButton("Entrar");
        cadastrarButton = new JButton("Cadastrar");

        panel.add(loginButton);
        panel.add(cadastrarButton);

        add(panel);

        // Ação do botão de "Entrar"
        loginButton.addActionListener(e -> {
            String nomeUsuario = usuarioField.getText().trim();
            String senha = new String(senhaField.getPassword()).trim();

            Usuario usuario = usuarioController.autenticar(nomeUsuario, senha);

            if (usuario != null) {
                JOptionPane.showMessageDialog(null, "Bem-vindo, " + usuario.getNome() + "!");
                dispose();
                new DashboardView(usuario);
            } else {
                JOptionPane.showMessageDialog(null, "Usuário ou senha inválidos.");
            }
        });

        // Ação do botão de "Cadastrar" (você pode substituir quando implementar)
        cadastrarButton.addActionListener(e -> {
            new CadastroUsuarioView();
        });

        setVisible(true);
    }
}
