package view;

import controller.TransacaoController;
import model.Transacao;
import model.Usuario;
import org.jdatepicker.impl.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Properties;
import java.util.Date;

public class RelatorioTransacoesView extends JFrame {

    private DefaultTableModel tableModel;
    private JTable tabela;
    private JLabel lblTotais;
    private List<Transacao> transacoesFiltradas; // guardamos aqui para exportar

    public RelatorioTransacoesView(Usuario usuario) {
        setTitle("Relatório de Transações");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Filtros
        JPanel filtrosPanel = new JPanel(new FlowLayout());

        JLabel lblInicio = new JLabel("Data Início:");
        UtilDateModel modelInicio = new UtilDateModel();
        JDatePanelImpl datePanelInicio = new JDatePanelImpl(modelInicio, new Properties());
        JDatePickerImpl pickerInicio = new JDatePickerImpl(datePanelInicio, new DateComponentFormatter());

        JLabel lblFim = new JLabel("Data Fim:");
        UtilDateModel modelFim = new UtilDateModel();
        JDatePanelImpl datePanelFim = new JDatePanelImpl(modelFim, new Properties());
        JDatePickerImpl pickerFim = new JDatePickerImpl(datePanelFim, new DateComponentFormatter());

        JButton btnFiltrar = new JButton("Filtrar");
        JButton btnExportarCSV = new JButton("Exportar CSV");

        filtrosPanel.add(lblInicio);
        filtrosPanel.add(pickerInicio);
        filtrosPanel.add(lblFim);
        filtrosPanel.add(pickerFim);
        filtrosPanel.add(btnFiltrar);
        filtrosPanel.add(btnExportarCSV);

        add(filtrosPanel, BorderLayout.NORTH);

        // Tabela
        String[] colunas = {"Tipo", "Descrição", "Valor", "Data"};
        tableModel = new DefaultTableModel(colunas, 0);
        tabela = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabela);
        add(scrollPane, BorderLayout.CENTER);

        // Totais
        lblTotais = new JLabel("Entradas: R$ 0.00 | Saídas: R$ 0.00 | Saldo: R$ 0.00");
        lblTotais.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTotais, BorderLayout.SOUTH);

        // Ação do botão de filtro
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

                TransacaoController controller = new TransacaoController();
                transacoesFiltradas = controller.buscarPorPeriodo(usuario.getId(), inicio, fim);

                // Limpar tabela
                tableModel.setRowCount(0);
                double entradas = 0, saidas = 0;

                for (Transacao t : transacoesFiltradas) {
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

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao buscar transações: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Ação do botão de exportar CSV
        btnExportarCSV.addActionListener(e -> {
            if (transacoesFiltradas == null || transacoesFiltradas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhuma transação para exportar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Salvar Relatório CSV");
            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                try (FileWriter writer = new FileWriter(fileChooser.getSelectedFile() + ".csv")) {
                    writer.write("Tipo,Descrição,Valor,Data\n");
                    for (Transacao t : transacoesFiltradas) {
                        writer.write(String.format("%s,%s,%.2f,%s\n",
                                t.getTipo().name().toLowerCase(),
                                t.getDescricao().replace(",", " "), // evitar quebrar CSV
                                t.getValor(),
                                t.getData_transacao()));
                    }
                    JOptionPane.showMessageDialog(this, "Relatório exportado com sucesso!");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Erro ao exportar CSV: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setVisible(true);
    }
}
