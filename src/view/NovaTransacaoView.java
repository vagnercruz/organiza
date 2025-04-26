package view;

import controller.TransacaoController;
import model.Transacao;
import model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class NovaTransacaoView extends JFrame {
    public NovaTransacaoView(Usuario usuario){
        setTitle("Nova Transação");
        setSize(350, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // COMPONENTES
        JLabel lblTipo = new JLabel("Tipo (entrada/saída): ");
        String[] tipos = {"entrada", "saida"};
        JComboBox<String> comboTipo = new JComboBox<>(tipos);

        JLabel lblDescricao = new JLabel("Descrição:");
        JTextField txtDescricao = new JTextField();

        JLabel lblValor = new JLabel("Valor:");
        JTextField txtValor = new JTextField();

        JLabel lblData = new JLabel("Data (AAAA-MM-DD):");
        JTextField txtData = new JTextField(LocalDate.now().toString()); // já deixa preenchido com a data de hoje

        JButton btnSalvar = new JButton("Salvar");

        // AÇÃO BOTÃO SALVAR
        btnSalvar.addActionListener(e -> {
            try {
                Transacao t = new Transacao();
                t.setUsuarioId(usuario.getId());
                t.setTipo((String) comboTipo.getSelectedItem());
                t.setDescricao(txtDescricao.getText());
                t.setValor(Double.parseDouble(txtValor.getText()));
                t.setData_transacao(LocalDate.parse(txtData.getText()));

                TransacaoController tc = new TransacaoController();
                tc.cadastrar(t);

                JOptionPane.showMessageDialog(this, "Transação cadastrada com sucesso!");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar transação: " + ex.getMessage());
            }
        });

        // LAYOUT
        setLayout(new GridLayout(5, 2, 5, 5));
        add(lblTipo); add(comboTipo);
        add(lblDescricao); add(txtDescricao);
        add(lblValor); add(txtValor);
        add(lblData); add(txtData);
        add(new JLabel()); add(btnSalvar);

        setVisible(true);
    }
}
