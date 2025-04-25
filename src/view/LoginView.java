package view;

import controller.UsuarioController;
import model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {
    private JTextField emailField;
    private JPasswordField senhaField;
    private JButton loginButton;
    private JButton cadastrarButton;

    private UsuarioController usuarioController = new UsuarioController();

    public LoginView(){
        setTitle("Login - Gestão Financeira");
        setSize(350, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // layout

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Senha:"));
        senhaField = new JPasswordField();
        panel.add(senhaField);

        loginButton = new JButton("Entrar");
        cadastrarButton = new JButton("Cadastrar");

        panel.add(loginButton);
        panel.add(cadastrarButton);

        add(panel);

        // Ação do botão de "Entrar"

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String senha = new String(senhaField.getPassword());
                Usuario usuario = usuarioController.autenticar(email, senha);
                if (usuario != null){
                    JOptionPane.showMessageDialog(null, "Bem-vindo, " + usuario.getNome() + "!");
                    dispose();
                }else{
                    JOptionPane.showMessageDialog(null, "Email ou senha inválidos.");
                }
            }
        });

        cadastrarButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Cadastro ainda não implementado!");
        });

        setVisible(true);
    }
}
