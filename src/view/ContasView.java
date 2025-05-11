package view;

import dao.ContaDAO;
import model.Conta;
import model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ContasView extends JFrame {

    private Usuario usuario;
    private JTable tabela;
    private DefaultTableModel modelo;

    public ContasView(Usuario usuario) {
        this.usuario = usuario;
        setTitle("Minhas Contas");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        modelo = new DefaultTableModel(new Object[]{"ID", "Nome", "Saldo"}, 0);
        tabela = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabela);

        JButton btnAdicionar = new JButton("Adicionar Conta");
        JButton btnEditar = new JButton("Editar Conta");
        JButton btnRemover = new JButton("Remover Conta");

        btnAdicionar.addActionListener(e -> adicionarConta());
        btnEditar.addActionListener(e -> editarConta());
        btnRemover.addActionListener(e -> removerConta());

        JPanel botoes = new JPanel();
        botoes.add(btnAdicionar);
        botoes.add(btnEditar);
        botoes.add(btnRemover);

        add(scrollPane, BorderLayout.CENTER);
        add(botoes, BorderLayout.SOUTH);

        carregarContas();

        setVisible(true);
    }

    private void carregarContas() {
        modelo.setRowCount(0); // limpa tabela
        ContaDAO dao = new ContaDAO();
        List<Conta> contas = dao.listarPorUsuario(usuario.getId());

        for (Conta conta : contas) {
            modelo.addRow(new Object[]{conta.getId(), conta.getNome(), conta.getSaldo()});
        }
    }

    private void adicionarConta() {
        JTextField nomeField = new JTextField();
        JTextField saldoField = new JTextField();

        Object[] inputs = {
                "Nome:", nomeField,
                "Saldo Inicial:", saldoField
        };

        int result = JOptionPane.showConfirmDialog(this, inputs, "Nova Conta", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String nome = nomeField.getText().trim();
                double saldo = Double.parseDouble(saldoField.getText().trim());

                Conta conta = new Conta();
                conta.setUsuarioId(usuario.getId());
                conta.setNome(nome);
                conta.setSaldo(saldo);

                new ContaDAO().inserir(conta);
                carregarContas();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao adicionar conta: " + ex.getMessage());
            }
        }
    }

    private void editarConta() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma conta para editar.");
            return;
        }

        int id = (int) modelo.getValueAt(linhaSelecionada, 0);
        String nomeAtual = (String) modelo.getValueAt(linhaSelecionada, 1);
        double saldoAtual = (double) modelo.getValueAt(linhaSelecionada, 2);

        JTextField nomeField = new JTextField(nomeAtual);
        JTextField saldoField = new JTextField(String.valueOf(saldoAtual));

        Object[] inputs = {
                "Nome:", nomeField,
                "Saldo:", saldoField
        };

        int result = JOptionPane.showConfirmDialog(this, inputs, "Editar Conta", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String novoNome = nomeField.getText().trim();
                double novoSaldo = Double.parseDouble(saldoField.getText().trim());

                Conta conta = new Conta();
                conta.setId(id);
                conta.setUsuarioId(usuario.getId());
                conta.setNome(novoNome);
                conta.setSaldo(novoSaldo);

                new ContaDAO().atualizar(conta);
                carregarContas();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao editar conta: " + ex.getMessage());
            }
        }
    }

    private void removerConta() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma conta para remover.");
            return;
        }

        int id = (int) modelo.getValueAt(linhaSelecionada, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente remover esta conta?", "Confirmar Remoção", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            new ContaDAO().excluir(id);
            carregarContas();
        }
    }
}
