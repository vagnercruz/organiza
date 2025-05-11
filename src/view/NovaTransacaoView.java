package view;

import controller.TransacaoController;
import dao.ContaDAO;
import model.Conta;
import model.Transacao;
import model.Usuario;
import org.jdatepicker.impl.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class NovaTransacaoView extends JFrame {

    public NovaTransacaoView(Usuario usuario) {
        setTitle("Nova Transação");
        setSize(500, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Layout base
        setLayout(new GridLayout(11, 2, 5, 5));

        // Tipo
        JLabel lblTipo = new JLabel("Tipo (entrada/saída): ");
        String[] tipos = {"entrada", "saída"};
        JComboBox<String> comboTipo = new JComboBox<>(tipos);

        // Conta
        JLabel lblConta = new JLabel("Conta:");
        JComboBox<String> comboContas = new JComboBox<>();
        Map<String, Integer> contasMap = new HashMap<>();

        List<Conta> listaContasDoUsuario = new ContaDAO().listarPorUsuario(usuario.getId());
        for (Conta conta : listaContasDoUsuario) {
            comboContas.addItem(conta.getNome());
            contasMap.put(conta.getNome(), conta.getId());
        }

        // Descrição
        JLabel lblDescricao = new JLabel("Descrição:");
        JTextField txtDescricao = new JTextField();

        // Valor
        JLabel lblValor = new JLabel("Valor:");
        JTextField txtValor = new JTextField();

        // Categoria
        JLabel lblCategoria = new JLabel("Categoria:");
        String[] categorias = {
                "Salário", "Investimentos", "Moradia", "Alimentação", "Transporte", "Contas e Serviços",
                "Saúde", "Educação", "Lazer e Entretenimento", "Vestuário", "Cuidados Pessoais",
                "Dívidas", "Imprevistos", "Doações", "Transferências"
        };
        JComboBox<String> comboCategoria = new JComboBox<>(categorias);

        // Data
        JLabel lblData = new JLabel("Data:");
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Hoje");
        p.put("text.month", "Mês");
        p.put("text.year", "Ano");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateComponentFormatter());

        // Observações
        JLabel lblObservacao = new JLabel("Observações:");
        JTextArea txtObservacao = new JTextArea(3, 20);
        JScrollPane scrollObservacao = new JScrollPane(txtObservacao);

        // Recorrência
        JCheckBox chkRecorrente = new JCheckBox("Transação recorrente");
        JLabel lblFrequencia = new JLabel("Frequência:");
        String[] opcoesFrequencia = {"Diária", "Semanal", "Mensal"};
        JComboBox<String> comboFrequencia = new JComboBox<>(opcoesFrequencia);
        comboFrequencia.setEnabled(false);

        chkRecorrente.addActionListener(e -> comboFrequencia.setEnabled(chkRecorrente.isSelected()));

        // Botão Salvar
        JButton btnSalvar = new JButton("Salvar");

        btnSalvar.addActionListener(e -> {
            try {
                String tipoStr = comboTipo.getSelectedItem().toString().toUpperCase().replace("Í", "I");
                Transacao.Tipo tipo = Transacao.Tipo.valueOf(tipoStr);

                String descricao = txtDescricao.getText().trim();
                String valorStr = txtValor.getText().trim();
                String categoria = comboCategoria.getSelectedItem().toString();
                Date selectedDate = (Date) datePicker.getModel().getValue();
                String observacao = txtObservacao.getText().trim();
                String nomeConta = (String) comboContas.getSelectedItem();

                if (descricao.isEmpty() || valorStr.isEmpty() || selectedDate == null || categoria == null || nomeConta == null) {
                    JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double valor;
                try {
                    valor = Double.parseDouble(valorStr);
                    if (valor <= 0) {
                        JOptionPane.showMessageDialog(this, "O valor deve ser maior que zero.", "Erro", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Valor inválido. Digite um número válido.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                LocalDate dataTransacao = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                Transacao t = new Transacao();
                t.setUsuarioId(usuario.getId());
                t.setTipo(tipo);
                t.setDescricao(descricao);
                t.setValor(valor);
                t.setCategoria(categoria);
                t.setData_transacao(dataTransacao);
                t.setObservacao(observacao);
                t.setContaId(contasMap.get(nomeConta));

                t.setRecorrente(chkRecorrente.isSelected());
                if (chkRecorrente.isSelected()) {
                    t.setFrequencia(comboFrequencia.getSelectedItem().toString());
                }

                TransacaoController tc = new TransacaoController();
                tc.cadastrar(t);

                JOptionPane.showMessageDialog(this, "Transação cadastrada com sucesso!");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar transação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Adiciona ao layout
        add(lblTipo); add(comboTipo);
        add(lblConta); add(comboContas);
        add(lblDescricao); add(txtDescricao);
        add(lblValor); add(txtValor);
        add(lblCategoria); add(comboCategoria);
        add(lblData); add(datePicker);
        add(lblObservacao); add(scrollObservacao);
        add(chkRecorrente); add(new JLabel());
        add(lblFrequencia); add(comboFrequencia);
        add(new JLabel()); add(btnSalvar);

        setVisible(true);
    }
}
