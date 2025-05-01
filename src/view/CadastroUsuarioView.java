package view;

import controller.UsuarioController;
import model.Usuario;

import javax.swing.*;
import java.awt.*;

public class CadastroUsuarioView extends JFrame {
    private JTextField nomeField;
    private JTextField nomeUsuarioField;
    private JTextField emailField;
    private JPasswordField senhaField;
    private JButton cadastrarButton;

    private UsuarioController usuarioController = new UsuarioController();

    public CadastroUsuarioView() {
        setTitle("Cadastro de Usuário");
        setSize(400, 250);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Nome completo:"));
        nomeField = new JTextField();
        panel.add(nomeField);

        panel.add(new JLabel("Nome de usuário:"));
        nomeUsuarioField = new JTextField();
        panel.add(nomeUsuarioField);

        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Senha:"));
        senhaField = new JPasswordField();
        panel.add(senhaField);

        cadastrarButton = new JButton("Cadastrar");
        panel.add(new JLabel());  // Espaço vazio
        panel.add(cadastrarButton);

        add(panel);

        cadastrarButton.addActionListener(e -> {
            String nome = nomeField.getText().trim();
            String nomeUsuario = nomeUsuarioField.getText().trim();
            String email = emailField.getText().trim();
            String senha = new String(senhaField.getPassword()).trim();

            if (nome.isEmpty() || nomeUsuario.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Usuario u = new Usuario();
            u.setNome(nome);
            u.setNomeUsuario(nomeUsuario);
            u.setEmail(email);
            u.setSenha(senha);

            boolean sucesso = usuarioController.cadastrar(u);

            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Cadastro realizado com sucesso!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar usuário.");
            }
        });

        setVisible(true);
    }
}
