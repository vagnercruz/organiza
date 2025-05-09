package view;

import controller.TransacaoController;
import controller.ContaController;
import model.Conta;
import model.Transacao;
import model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListarTransacoesView extends JFrame {
    private final Usuario usuario;
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final TransacaoController transacaoController = new TransacaoController();
    private final ContaController contaController = new ContaController();
    private final Map<Integer, String> contasMap = new HashMap<>();

    public ListarTransacoesView(Usuario usuario) {
        this.usuario = usuario;

        setTitle("Minhas Transações");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Adiciona coluna da conta
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Data");
        tableModel.addColumn("Tipo");
        tableModel.addColumn("Valor");
        tableModel.addColumn("Categoria");
        tableModel.addColumn("Descrição");
        tableModel.addColumn("Observações");
        tableModel.addColumn("Conta"); // nova coluna

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(e -> carregarTransacoes());

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnAtualizar, BorderLayout.SOUTH);

        add(panel);

        carregarContas();        // Carrega nomes de contas para o mapa
        carregarTransacoes();    // Carrega transações na tabela

        setVisible(true);
    }

    private void carregarContas() {
        try {
            List<Conta> contas = contaController.listarPorUsuario(usuario.getId());
            for (Conta conta : contas) {
                contasMap.put(conta.getId(), conta.getNome());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar contas: " + e.getMessage());
        }
    }

    private void carregarTransacoes() {
        tableModel.setRowCount(0); // Limpa a tabela
        List<Transacao> lista = transacaoController.listar(usuario.getId());
        for (Transacao t : lista) {
            String nomeConta = contasMap.getOrDefault(t.getContaId(), "N/A");
            tableModel.addRow(new Object[]{
                    t.getData_transacao(),
                    t.getTipo().name(),
                    String.format("R$ %.2f", t.getValor()),
                    t.getCategoria(),
                    t.getDescricao(),
                    t.getObservacao(),
                    nomeConta
            });
        }
    }
}
