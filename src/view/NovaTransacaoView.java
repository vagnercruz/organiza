package view;

import controller.TransacaoController;
import model.Transacao;
import model.Usuario;
import org.jdatepicker.impl.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Properties;
import java.util.Date;

public class NovaTransacaoView extends JFrame {

    public NovaTransacaoView(Usuario usuario) {
        setTitle("Nova Transação");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Tipo - Agora com JComboBox
        JLabel lblTipo = new JLabel("Tipo (entrada/saída): ");
        String[] tipos = {"entrada", "saída"};
        JComboBox<String> comboTipo = new JComboBox<>(tipos);

        // Descrição
        JLabel lblDescricao = new JLabel("Descrição:");
        JTextField txtDescricao = new JTextField();

        // Valor
        JLabel lblValor = new JLabel("Valor:");
        JTextField txtValor = new JTextField();

        // Data - Agora com JDatePicker
        JLabel lblData = new JLabel("Data:");
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Hoje");
        p.put("text.month", "Mês");
        p.put("text.year", "Ano");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateComponentFormatter());

        // Botão Salvar
        JButton btnSalvar = new JButton("Salvar");

        btnSalvar.addActionListener(e -> {
            try {
                Transacao t = new Transacao();
                t.setUsuarioId(usuario.getId());
                t.setTipo(comboTipo.getSelectedItem().toString());
                t.setDescricao(txtDescricao.getText());
                t.setValor(Double.parseDouble(txtValor.getText()));

                Date selectedDate = (Date) datePicker.getModel().getValue();
                if (selectedDate != null) {
                    LocalDate localDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    t.setData_transacao(localDate);
                } else {
                    throw new IllegalArgumentException("Data não selecionada.");
                }

                TransacaoController tc = new TransacaoController();
                tc.cadastrar(t);

                JOptionPane.showMessageDialog(this, "Transação cadastrada com sucesso!");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar transação: " + ex.getMessage());
            }
        });

        // Layout
        setLayout(new GridLayout(5, 2, 5, 5));
        add(lblTipo); add(comboTipo);
        add(lblDescricao); add(txtDescricao);
        add(lblValor); add(txtValor);
        add(lblData); add(datePicker);
        add(new JLabel()); add(btnSalvar);

        setVisible(true);
    }
}
