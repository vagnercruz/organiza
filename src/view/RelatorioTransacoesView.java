package view;

import controller.TransacaoController;
import model.Transacao;
import model.Usuario;
import org.jdatepicker.impl.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.List;

public class RelatorioTransacoesView extends JFrame {

    public RelatorioTransacoesView(Usuario usuario) {
        setTitle("Relatório de Transações");
        setSize(850, 550);
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
        JButton btnGraficoCategoria = new JButton("Gráfico por Categoria");
        JButton btnGraficoTipo = new JButton("Gráfico por Tipo");

        filtrosPanel.add(lblInicio);
        filtrosPanel.add(pickerInicio);
        filtrosPanel.add(lblFim);
        filtrosPanel.add(pickerFim);
        filtrosPanel.add(btnFiltrar);
        filtrosPanel.add(btnGraficoCategoria);
        filtrosPanel.add(btnGraficoTipo);

        add(filtrosPanel, BorderLayout.NORTH);

        // Tabela
        String[] colunas = {"Data", "Tipo", "Valor", "Categoria", "Descrição"};
        DefaultTableModel tableModel = new DefaultTableModel(colunas, 0);
        JTable tabela = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabela);
        add(scrollPane, BorderLayout.CENTER);

        // Totais
        JLabel lblTotais = new JLabel("Entradas: R$ 0.00 | Saídas: R$ 0.00 | Saldo: R$ 0.00");
        lblTotais.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTotais, BorderLayout.SOUTH);

        // Ação do botão Filtrar
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
                List<Transacao> transacoes = controller.buscarPorPeriodo(usuario.getId(), inicio, fim);

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

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao buscar transações: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Gráfico por Categoria
        btnGraficoCategoria.addActionListener(e -> {
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
                Map<String, Double> categorias = controller.agruparPorCategoria(usuario.getId(), inicio, fim);

                if (categorias.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nenhuma transação encontrada para o período.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                DefaultPieDataset dataset = new DefaultPieDataset();
                categorias.forEach(dataset::setValue);

                JFreeChart chart = ChartFactory.createPieChart("Distribuição por Categoria", dataset, true, true, false);
                ChartPanel chartPanel = new ChartPanel(chart);

                JDialog dialog = new JDialog(this, "Gráfico de Categorias", true);
                dialog.setSize(600, 400);
                dialog.setLocationRelativeTo(this);
                dialog.setContentPane(chartPanel);
                dialog.setVisible(true);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao gerar gráfico: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Gráfico por Tipo
        btnGraficoTipo.addActionListener(e -> {
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
                List<Transacao> transacoes = controller.buscarPorPeriodo(usuario.getId(), inicio, fim);

                double totalEntradas = 0;
                double totalSaidas = 0;

                for (Transacao t : transacoes) {
                    if (t.getTipo().name().equalsIgnoreCase("ENTRADA")) {
                        totalEntradas += t.getValor();
                    } else {
                        totalSaidas += t.getValor();
                    }
                }

                if (totalEntradas == 0 && totalSaidas == 0) {
                    JOptionPane.showMessageDialog(this, "Nenhuma transação encontrada para o período.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                DefaultPieDataset dataset = new DefaultPieDataset();
                if (totalEntradas > 0) dataset.setValue("Entradas", totalEntradas);
                if (totalSaidas > 0) dataset.setValue("Saídas", totalSaidas);

                JFreeChart chart = ChartFactory.createPieChart("Distribuição por Tipo", dataset, true, true, false);
                ChartPanel chartPanel = new ChartPanel(chart);

                JDialog dialog = new JDialog(this, "Gráfico por Tipo", true);
                dialog.setSize(600, 400);
                dialog.setLocationRelativeTo(this);
                dialog.setContentPane(chartPanel);
                dialog.setVisible(true);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao gerar gráfico: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }
}
