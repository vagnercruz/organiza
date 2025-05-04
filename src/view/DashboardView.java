package view;

import controller.TransacaoController;
import model.Transacao;
import model.Usuario;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class DashboardView extends JFrame {
    private Usuario usuario;
    private TransacaoController controller = new TransacaoController();

    public DashboardView(Usuario usuario){
        this.usuario = usuario;

        setTitle("Painel Principal");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Painel de topo com saudação e saldo
        JPanel topoPanel = new JPanel(new BorderLayout());
        JLabel saudacao = new JLabel("Bem-vindo, " + usuario.getNome() + "!", SwingConstants.CENTER);
        saudacao.setFont(new Font("Arial", Font.BOLD, 18));
        topoPanel.add(saudacao, BorderLayout.NORTH);

        JLabel lblTotais = new JLabel("Carregando saldo...", SwingConstants.CENTER);
        topoPanel.add(lblTotais, BorderLayout.SOUTH);
        add(topoPanel, BorderLayout.NORTH);

        // Painel Central com gráfico e tabela
        JPanel centroPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        // Gráfico de categoria
        DefaultPieDataset dataset = new DefaultPieDataset();
        JFreeChart chart = ChartFactory.createPieChart("Gastos por Categoria (Mês Atual)", dataset, true, true, false);
        ChartPanel chartPanel = new ChartPanel(chart);
        centroPanel.add(chartPanel);

        // Tabela últimas transações
        String[] colunas = {"Data", "Tipo", "Valor", "Categoria", "Descrição"};
        DefaultTableModel tableModel = new DefaultTableModel(colunas, 0);
        JTable tabela = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabela);
        centroPanel.add(scrollPane);

        add(centroPanel, BorderLayout.CENTER);

        // Botões no rodapé
        JPanel botoesPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        JButton btnNovaTransacao = new JButton("Nova Transação");
        JButton btnVerTransacoes = new JButton("Ver Transações");
        JButton btnSaldo = new JButton("Consultar Saldo");
        JButton btnRelatorios = new JButton("Ver Relatórios");
        JButton btnLogout = new JButton("Logout");

        btnNovaTransacao.addActionListener(e -> new NovaTransacaoView(usuario));
        btnVerTransacoes.addActionListener(e -> new ListarTransacoesView(usuario));
        btnSaldo.addActionListener(e -> new ConsultarSaldoView(usuario));
        btnRelatorios.addActionListener(e -> new RelatorioTransacoesView(usuario));
        btnLogout.addActionListener(e -> dispose());

        botoesPanel.add(btnNovaTransacao);
        botoesPanel.add(btnVerTransacoes);
        botoesPanel.add(btnSaldo);
        botoesPanel.add(btnRelatorios);
        botoesPanel.add(btnLogout);
        add(botoesPanel, BorderLayout.SOUTH);

        // Carrega dados
        carregarTotais(lblTotais);
        carregarGrafico(dataset);
        carregarUltimasTransacoes(tableModel);

        setVisible(true);
    }

    private void carregarTotais(JLabel lblTotais) {
        List<Transacao> transacoes = controller.buscarPorPeriodo(usuario.getId(), LocalDate.now().withDayOfMonth(1), LocalDate.now());
        double entradas = 0, saidas = 0;

        for (Transacao t : transacoes) {
            if (t.getTipo().name().equalsIgnoreCase("ENTRADA")) entradas += t.getValor();
            else saidas += t.getValor();
        }
        double saldo = entradas - saidas;
        lblTotais.setText(String.format("Entradas: R$ %.2f | Saídas: R$ %.2f | Saldo: R$ %.2f", entradas, saidas, saldo));
    }

    private void carregarGrafico(DefaultPieDataset dataset) {
        Map<String, Double> categorias = controller.agruparPorCategoria(usuario.getId(),
                LocalDate.now().withDayOfMonth(1), LocalDate.now());
        dataset.clear();
        categorias.forEach(dataset::setValue);
    }

    private void carregarUltimasTransacoes(DefaultTableModel model) {
        List<Transacao> ultimas = controller.buscarUltimas(usuario.getId(), 5);
        model.setRowCount(0);
        for (Transacao t : ultimas) {
            model.addRow(new Object[]{
                    t.getData_transacao(),
                    t.getTipo().name(),
                    String.format("R$ %.2f", t.getValor()),
                    t.getCategoria() != null ? t.getCategoria().getNome() : "Não categorizado",
                    t.getDescricao()
            });
        }
    }
}
