package view;

import controller.TransacaoController;
import model.Transacao;
import model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ListarTransacoesView extends JFrame {
    private Usuario usuario;
    private JTable table;
    private DefaultTableModel tableModel;
    private TransacaoController transacaoController = new TransacaoController();

    public ListarTransacoesView(Usuario usuario) {
        this.usuario = usuario;

        setTitle("Minhas Transações");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Configura tabela
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Data");
        tableModel.addColumn("Tipo");
        tableModel.addColumn("Valor");
        tableModel.addColumn("Categoria");
        tableModel.addColumn("Descrição");

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(e -> carregarTransacoes());

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnAtualizar, BorderLayout.SOUTH);

        add(panel);

        carregarTransacoes(); // Carrega as transações na tabela ao abrir

        setVisible(true);
    }

    private void carregarTransacoes() {
        tableModel.setRowCount(0); // Limpa a tabela
        List<Transacao> lista = transacaoController.Listar(usuario.getId());
        for (Transacao t : lista) {
            tableModel.addRow(new Object[]{
                    t.getData_transacao(),
                    t.getTipo().name(),
                    String.format("R$ %.2f", t.getValor()),
                    t.getCategoria(),
                    t.getDescricao()
            });
        }
    }
}
