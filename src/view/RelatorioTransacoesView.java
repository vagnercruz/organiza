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
import java.util.*;
import java.util.List;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import java.io.FileOutputStream;

public class RelatorioTransacoesView extends JFrame {

    private DefaultTableModel tableModel;
    private JTable tabela;
    private JLabel lblTotais;
    private Usuario usuario;
    private JDatePickerImpl pickerInicio;
    private JDatePickerImpl pickerFim;
    private List<Transacao> transacoes;

    public RelatorioTransacoesView(Usuario usuario) {
        this.usuario = usuario;
        this.transacoes = new ArrayList<>();

        setTitle("Relatório de Transações");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Filtros
        JPanel filtrosPanel = new JPanel(new FlowLayout());

        JLabel lblInicio = new JLabel("Data Início:");
        UtilDateModel modelInicio = new UtilDateModel();
        pickerInicio = new JDatePickerImpl(new JDatePanelImpl(modelInicio, new Properties()), new DateComponentFormatter());

        JLabel lblFim = new JLabel("Data Fim:");
        UtilDateModel modelFim = new UtilDateModel();
        pickerFim = new JDatePickerImpl(new JDatePanelImpl(modelFim, new Properties()), new DateComponentFormatter());

        JButton btnFiltrar = new JButton("Filtrar");

        filtrosPanel.add(lblInicio);
        filtrosPanel.add(pickerInicio);
        filtrosPanel.add(lblFim);
        filtrosPanel.add(pickerFim);
        filtrosPanel.add(btnFiltrar);

        // Botão exportar com menu suspenso
        JButton btnExportar = new JButton("Exportar");
        JPopupMenu exportarMenu = new JPopupMenu();
        JMenuItem pdfItem = new JMenuItem("Exportar como PDF");
        JMenuItem csvItem = new JMenuItem("Exportar como CSV");
        JMenuItem xlsxItem = new JMenuItem("Exportar como XLSX (em breve)");
        exportarMenu.add(pdfItem);
        exportarMenu.add(csvItem);
        exportarMenu.add(xlsxItem);
        btnExportar.addActionListener(e -> exportarMenu.show(btnExportar, 0, btnExportar.getHeight()));
        filtrosPanel.add(btnExportar);

        add(filtrosPanel, BorderLayout.NORTH);

        // Tabela
        String[] colunas = {"Tipo", "Descrição", "Valor", "Data"};
        tableModel = new DefaultTableModel(colunas, 0);
        tabela = new JTable(tableModel);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // Totais
        lblTotais = new JLabel("Entradas: R$ 0.00 | Saídas: R$ 0.00 | Saldo: R$ 0.00");
        lblTotais.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTotais, BorderLayout.SOUTH);

        // Botão Filtrar
        btnFiltrar.addActionListener(e -> filtrar());

        // Exportações
        pdfItem.addActionListener(e -> exportarPDF());
        csvItem.addActionListener(e -> exportarCSV());

        setVisible(true);
    }

    private void filtrar() {
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
            transacoes = controller.buscarPorPeriodo(usuario.getId(), inicio, fim);

            // Limpar tabela
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
    }

    private void exportarCSV() {
        if (transacoes == null || transacoes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não há dados para exportar.");
            return;
        }

        try (FileWriter writer = new FileWriter("relatorio_transacoes.csv")) {
            writer.append("Tipo,Descrição,Valor,Data\n");
            for (Transacao t : transacoes) {
                writer.append(t.getTipo().name().toLowerCase()).append(",");
                writer.append(t.getDescricao()).append(",");
                writer.append(String.format("R$ %.2f", t.getValor())).append(",");
                writer.append(t.getData_transacao().toString()).append("\n");
            }
            writer.flush();
            JOptionPane.showMessageDialog(this, "Arquivo CSV exportado com sucesso!");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao exportar CSV: " + ex.getMessage());
        }
    }

    private void exportarPDF() {
        if (transacoes == null || transacoes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não há dados para exportar.");
            return;
        }

        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream("relatorio_transacoes.pdf"));
            document.open();

            document.add(new Paragraph("Relatório de Transações"));
            document.add(new Paragraph("Usuário: " + usuario.getNome()));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2, 4, 2, 3});

            table.addCell("Tipo");
            table.addCell("Descrição");
            table.addCell("Valor");
            table.addCell("Data");

            double entradas = 0, saidas = 0;

            for (Transacao t : transacoes) {
                table.addCell(t.getTipo().name().toLowerCase());
                table.addCell(t.getDescricao());
                table.addCell(String.format("R$ %.2f", t.getValor()));
                table.addCell(t.getData_transacao().toString());

                if (t.getTipo().name().equalsIgnoreCase("ENTRADA")) {
                    entradas += t.getValor();
                } else {
                    saidas += t.getValor();
                }
            }

            document.add(table);
            document.add(new Paragraph(" "));
            double saldo = entradas - saidas;
            document.add(new Paragraph(String.format("Entradas: R$ %.2f | Saídas: R$ %.2f | Saldo: R$ %.2f", entradas, saidas, saldo)));

            document.close();
            JOptionPane.showMessageDialog(this, "Arquivo PDF exportado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao exportar PDF: " + e.getMessage());
        }
    }
}
