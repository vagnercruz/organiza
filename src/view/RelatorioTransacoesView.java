package view;

import controller.TransacaoController;
import model.Transacao;
import model.Usuario;
import org.jdatepicker.impl.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Properties;
import java.util.Date;

public class RelatorioTransacoesView extends JFrame {

    public RelatorioTransacoesView(Usuario usuario) {
        setTitle("Relatório de Transações");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // === Filtros ===
        JPanel filtrosPanel = new JPanel(new FlowLayout());

        JLabel lblInicio = new JLabel("Data Início:");
        UtilDateModel modelInicio = new UtilDateModel();
        JDatePanelImpl datePanelInicio = new JDatePanelImpl(modelInicio, new Properties());
        JDatePickerImpl pickerInicio = new JDatePickerImpl(datePanelInicio, new DateComponentFormatter());

        JLabel lblFim = new JLabel("Data Fim:");
        UtilDateModel modelFim = new UtilDateModel();
        JDatePanelImpl datePanelFim = new JDatePanelImpl(modelFim, new Properties());
        JDatePickerImpl pickerFim = new JDatePickerImpl(datePanelFim, new DateComponentFormatter());

        JComboBox<String> tipoCombo = new JComboBox<>(new String[]{"Todos", "ENTRADA", "SAIDA"});
        JTextField valorMinField = new JTextField(6);
        JTextField valorMaxField = new JTextField(6);
        JButton btnFiltrar = new JButton("Filtrar");

        filtrosPanel.add(lblInicio);
        filtrosPanel.add(pickerInicio);
        filtrosPanel.add(lblFim);
        filtrosPanel.add(pickerFim);
        filtrosPanel.add(new JLabel("Tipo:"));
        filtrosPanel.add(tipoCombo);
        filtrosPanel.add(new JLabel("Valor mín:"));
        filtrosPanel.add(valorMinField);
        filtrosPanel.add(new JLabel("Valor máx:"));
        filtrosPanel.add(valorMaxField);
        filtrosPanel.add(btnFiltrar);

        add(filtrosPanel, BorderLayout.NORTH);

        // === Tabela ===
        String[] colunas = {"Tipo", "Descrição", "Valor", "Data"};
        DefaultTableModel tableModel = new DefaultTableModel(colunas, 0);
        JTable tabela = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabela);
        add(scrollPane, BorderLayout.CENTER);

        // === Totais ===
        JLabel lblTotais = new JLabel("Entradas: R$ 0.00 | Saídas: R$ 0.00 | Saldo: R$ 0.00");
        lblTotais.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTotais, BorderLayout.SOUTH);

        // === Ação do botão ===
        btnFiltrar.addActionListener(e -> {
            try {
                Date inicioDate = (Date) pickerInicio.getModel().getValue();
                Date fimDate = (Date) pickerFim.getModel().getValue();

                if (inicioDate == null || fimDate == null) {
                    JOptionPane.showMessageDialog(this, "Selecione as duas datas.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                LocalDate inicio = inicioDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate fim = fimDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                String tipoSelecionado = tipoCombo.getSelectedItem().toString();
                String valorMinStr = valorMinField.getText().trim();
                String valorMaxStr = valorMaxField.getText().trim();

                Double valorMin = valorMinStr.isEmpty() ? null : Double.parseDouble(valorMinStr);
                Double valorMax = valorMaxStr.isEmpty() ? null : Double.parseDouble(valorMaxStr);

                TransacaoController controller = new TransacaoController();
                List<Transacao> transacoes = controller.buscarPorPeriodo(usuario.getId(), inicio, fim);

                // Aplicar filtros extras (tipo, valor min/max)
                transacoes.removeIf(t -> {
                    if (!tipoSelecionado.equals("Todos") && !t.getTipo().name().equalsIgnoreCase(tipoSelecionado)) {
                        return true;
                    }
                    if (valorMin != null && t.getValor() < valorMin) return true;
                    if (valorMax != null && t.getValor() > valorMax) return true;
                    return false;
                });

                // Atualizar tabela
                tableModel.setRowCount(0);
                double entradas = 0, saidas = 0;

                for (Transacao t : transacoes) {
                    tableModel.addRow(new Object[]{
                            t.getTipo().name().toLowerCase(),
                            t.getDescricao(),
                            String.format("R$ %.2f", t.getValor()),
                            t.getData_transacao()
                    });

                    if (t.getTipo().name().equalsIgnoreCase("ENTRADA")) {
                        entradas += t.getValor();
                    } else {
                        saidas += t.getValor();
                    }
                }

                double saldo = entradas - saidas;
                lblTotais.setText(String.format("Entradas: R$ %.2f | Saídas: R$ %.2f | Saldo: R$ %.2f", entradas, saidas, saldo));

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Valores inválidos nos campos de valor.", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao buscar transações: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }
}
