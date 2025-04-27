package view;

import model.Usuario;

import javax.swing.*;
import java.awt.*;

public class DashboardView extends JFrame {
    private Usuario usuario;
    public DashboardView(Usuario usuario){
        this.usuario = usuario;

        setTitle("Painel Principal");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel bemVindo = new JLabel("Bem-vindo, " + usuario.getNome() + "!");
        bemVindo.setHorizontalAlignment(SwingConstants.CENTER);

        JButton btnNovaTransacao = new JButton("Nova Transação");
        btnNovaTransacao.addActionListener(e -> {
            new NovaTransacaoView(usuario);
        });
        JButton btnVerTransacoes = new JButton("Ver Transações");
        btnVerTransacoes.addActionListener(e ->{
            new ListarTransacoesView(usuario);
        });
        JButton btnSaldo = new JButton("Consultar Saldo");
        btnSaldo.addActionListener(e ->{
            new ConsultarSaldoView(usuario);
        });
        JButton btnLogout = new JButton("Logout");

        JPanel painel = new JPanel();
        painel.setLayout(new GridLayout(5,1,10,10));
        painel.add(bemVindo);
        painel.add(btnNovaTransacao);
        painel.add(btnVerTransacoes);
        painel.add(btnSaldo);
        painel.add(btnLogout);

        add(painel);
        setVisible(true);

    }
}
