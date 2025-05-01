package view;

import controller.TransacaoController;
import model.Transacao;
import model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class RelatoriosView extends JFrame {

    private JComboBox<String> filtroCombo;
    private JTextField campoData;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private JLabel lblResumo;

    public RelatoriosView(Usuario usuario) {
        setTitle("Relatórios de Transações");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Filtros
        filtroCombo = new JComboBox<>(new String[]{"Dia", "Mês", "Ano"});
        campoData = new JTextField(10); // Exemplo: "2024-04-01", "2024-04", "2024"
        JButton btnGerar = new JButton("Gerar Relatório");

        JPanel painelFiltros = new JPanel();
        painelFiltros.add(new JLabel("Filtrar por:"));
        painelFiltros.add(filtroCombo);
        painelFiltros.add(new JLabel("Data:"));
        painelFiltros.add(campoData);
        painelFiltros.add(btnGerar);

        // Tabela
        modeloTabela = new DefaultTableModel(new Object[]{"Descrição", "Tipo", "Valor", "Data"}, 0);
        tabela = new JTable(modeloTabela);
        JScrollPane scroll = new JScrollPane(tabela);

        // Resumo
        lblResumo = new JLabel("Total de Entradas: R$0.00 | Saídas: R$0.00 | Saldo: R$0.00");

        // Layout principal
        setLayout(new BorderLayout());
        add(painelFiltros, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(lblResumo, BorderLayout.SOUTH);

        btnGerar.addActionListener(e -> {
            try {
                gerarRelatorio(usuario.getId());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        setVisible(true);
    }

    private void gerarRelatorio(int usuarioId) throws Exception {
        String tipoFiltro = filtroCombo.getSelectedItem().toString();
        String valor = campoData.getText().trim();

        TransacaoController controller = new TransacaoController();
        List<Transacao> transacoes;

        switch (tipoFiltro) {
            case "Dia" -> transacoes = controller.listarPorDia(usuarioId, LocalDate.parse(valor));
            case "Mês" -> transacoes = controller.listarPorMes(usuarioId, valor);
            case "Ano" -> transacoes = controller.listarPorAno(usuarioId, Integer.parseInt(valor));
            default -> throw new IllegalArgumentException("Filtro inválido");
        }

        modeloTabela.setRowCount(0); // Limpa a tabela
        double totalEntradas = 0;
        double totalSaidas = 0;

        for (Transacao t : transacoes) {
            modeloTabela.addRow(new Object[]{
                    t.getDescricao(),
                    t.getTipo(),
                    t.getValor(),
                    t.getData_transacao()
            });

            if (t.getTipo().isEntrada()) {
                totalEntradas += t.getValor();
            } else {
                totalSaidas += t.getValor();
            }
        }

        double saldo = totalEntradas - totalSaidas;
        lblResumo.setText(String.format("Total de Entradas: R$%.2f | Saídas: R$%.2f | Saldo: R$%.2f",
                totalEntradas, totalSaidas, saldo));
    }
}
