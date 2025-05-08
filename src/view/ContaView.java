package view;

import controller.ContaController;
import model.Conta;
import model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ContaView extends JFrame {
    private final DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "Tipo", "Saldo Inicial"}, 0);
    private final JTable tabela = new JTable(tableModel);
    private final ContaController controller = new ContaController();
    private final Usuario usuario;

    public ContaView(Usuario usuario) {
        this.usuario = usuario;
        setTitle("Gerenciar Contas");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");

        btnAdicionar.addActionListener(e -> abrirFormulario(null));
        btnEditar.addActionListener(e -> editarSelecionada());
        btnExcluir.addActionListener(e -> excluirSelecionada());

        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);

        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        carregarContas();
        setVisible(true);
    }

    private void carregarContas() {
        tableModel.setRowCount(0);
        try {
            List<Conta> contas = controller.listarPorUsuario(usuario.getId());
            for (Conta conta : contas) {
                tableModel.addRow(new Object[]{
                        conta.getId(),
                        conta.getNome(),
                        conta.getTipo(),
                        conta.getSaldoInicial()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar contas: " + e.getMessage());
        }
    }

    private void abrirFormulario(Conta conta) {
        JTextField txtNome = new JTextField();
        String[] tipos = {"CORRENTE", "POUPANCA", "CREDITO", "INVESTIMENTO", "DINHEIRO"};
        JComboBox<String> comboTipo = new JComboBox<>(tipos);
        JTextField txtSaldo = new JTextField();

        if (conta != null) {
            txtNome.setText(conta.getNome());
            comboTipo.setSelectedItem(conta.getTipo().name());
            txtSaldo.setText(String.valueOf(conta.getSaldoInicial()));
        }

        JPanel painel = new JPanel(new GridLayout(0, 1));
        painel.add(new JLabel("Nome:"));
        painel.add(txtNome);
        painel.add(new JLabel("Tipo:"));
        painel.add(comboTipo);
        painel.add(new JLabel("Saldo Inicial:"));
        painel.add(txtSaldo);

        int resultado = JOptionPane.showConfirmDialog(this, painel,
                conta == null ? "Nova Conta" : "Editar Conta", JOptionPane.OK_CANCEL_OPTION);

        if (resultado == JOptionPane.OK_OPTION) {
            try {
                String nome = txtNome.getText().trim();
                String tipo = comboTipo.getSelectedItem().toString();
                double saldo = Double.parseDouble(txtSaldo.getText());

                if (conta == null) {
                    Conta novaConta = new Conta();
                    novaConta.setUsuarioId(usuario.getId());
                    novaConta.setNome(nome);
                    novaConta.setTipo(Conta.Tipo.valueOf(tipo));
                    novaConta.setSaldoInicial(saldo);
                    controller.cadastrar(novaConta);
                } else {
                    conta.setNome(nome);
                    conta.setTipo(Conta.Tipo.valueOf(tipo));
                    conta.setSaldoInicial(saldo);
                    controller.atualizar(conta);
                }

                carregarContas();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        }
    }

    private void editarSelecionada() {
        int row = tabela.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma conta para editar.");
            return;
        }

        int contaId = (int) tableModel.getValueAt(row, 0);
        try {
            Conta conta = controller.listarPorUsuario(usuario.getId())
                    .stream().filter(c -> c.getId() == contaId).findFirst().orElse(null);

            if (conta != null) {
                abrirFormulario(conta);
            } else {
                JOptionPane.showMessageDialog(this, "Conta não encontrada.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar conta: " + e.getMessage());
        }
    }

    private void excluirSelecionada() {
        int row = tabela.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma conta para excluir.");
            return;
        }

        int contaId = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir esta conta?",
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controller.excluir(contaId);
                carregarContas();
                JOptionPane.showMessageDialog(this, "Conta excluída com sucesso.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir conta: " + e.getMessage());
            }
        }
    }
}
