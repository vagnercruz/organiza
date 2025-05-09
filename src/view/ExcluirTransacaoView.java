package view;

import controller.TransacaoController;
import model.Transacao;
import model.Usuario;



import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ExcluirTransacaoView extends JFrame {

    public ExcluirTransacaoView(Usuario usuario) {
        setTitle("Excluir Transação");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] colunas = {"ID", "Data", "Tipo", "Valor", "Categoria", "Descrição"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        JTable tabela = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tabela);

        JButton btnExcluir = new JButton("Excluir Selecionada");

        // Carrega transações
        TransacaoController controller = new TransacaoController();
        try {
            List<Transacao> transacoes = controller.buscarPorUsuario(usuario.getId());
            for (Transacao t : transacoes) {
                model.addRow(new Object[]{
                        t.getId(),
                        t.getData_transacao(),
                        t.getTipo().name(),
                        String.format("R$ %.2f", t.getValor()),
                        t.getCategoria(),
                        t.getDescricao()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar transações: " + ex.getMessage());
        }

        btnExcluir.addActionListener(e -> {
            int selectedRow = tabela.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Selecione uma transação para excluir.");
                return;
            }

            int transacaoId = (int) model.getValueAt(selectedRow, 0);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja excluir esta transação?",
                    "Confirmação", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    controller.excluir(transacaoId);
                    model.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(this, "Transação excluída com sucesso.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir transação: " + ex.getMessage());
                }
            }
        });

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(btnExcluir, BorderLayout.SOUTH);

        setVisible(true);
    }
}
