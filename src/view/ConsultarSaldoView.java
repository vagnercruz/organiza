package view;

import controller.TransacaoController;
import model.Usuario;

import javax.swing.*;
import java.awt.*;

public class ConsultarSaldoView extends JFrame {
    public ConsultarSaldoView(Usuario usuario){
        setTitle("Saldo Atual");
        setSize(300,150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        TransacaoController tc = new TransacaoController();
        Double saldo = tc.consultarSaldo(usuario.getId());
        if (saldo == null) {
            saldo = 0.0;
        }

        JLabel lblSaldo = new JLabel(String.format("Seu saldo atual é: R$ %.2f", saldo));
        lblSaldo.setFont(new Font("Arial", Font.BOLD, 16));
        lblSaldo.setHorizontalAlignment(SwingConstants.CENTER);

        setLayout(new BorderLayout());
        add(lblSaldo, BorderLayout.CENTER);

        setVisible(true);
    }
}
