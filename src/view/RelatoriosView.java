package view;

import controller.TransacaoController;
import model.Transacao;
import model.Usuario;
import org.jdatepicker.impl.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class RelatoriosView extends JFrame {

    public RelatoriosView(Usuario usuario) {
        setTitle("Relatórios de Transações");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Date Picker para data específica
        JLabel lblData = new JLabel("Filtrar por data:");
        UtilDateModel dateModel = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Hoje");
        p.put("text.month", "Mês");
        p.put("text.year", "Ano");
        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateComponentFormatter());
        JButton btnBuscarData = new JButton("Buscar por data");

        // Campo de mês e ano
        JLabel lblMesAno = new JLabel("Filtrar por mês e ano:");
        JTextField txtMes = new JTextField(2);
        JTextField txtAnoMes = new JTextField(4);
        JButton btnBuscarMesAno = new JButton("Buscar por mês/ano");

        // Campo de ano
        JLabel lblAno = new JLabel("Filtrar por ano:");
        JTextField txtAno = new JTextField(4);
        JButton btnBuscarAno = new JButton("Buscar por ano");

        // Área de resultados
        JTextArea areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaResultados);

        TransacaoController controller = new TransacaoController();

        // Ação: buscar por data específica
        btnBuscarData.addActionListener(e -> {
            try {
                Date dataSelecionada = (Date) datePicker.getModel().getValue();
                if (dataSelecionada == null) {
                    JOptionPane.showMessageDialog(this, "Selecione uma data.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                LocalDate data = dataSelecionada.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                List<Transacao> transacoes = controller.listarPorData(usuario.getId(), data);
                mostrarTransacoes(transacoes, areaResultados);
            } catch (Exception ex) {
                mostrarErro(ex);
            }
        });

        // Ação: buscar por mês e ano
        btnBuscarMesAno.addActionListener(e -> {
            try {
                int mes = Integer.parseInt(txtMes.getText());
                int ano = Integer.parseInt(txtAnoMes.getText());
                List<Transacao> transacoes = controller.listarPorMesEAno(usuario.getId(), mes, ano);
                mostrarTransacoes(transacoes, areaResultados);
            } catch (Exception ex) {
                mostrarErro(ex);
            }
        });

        // Ação: buscar por ano
        btnBuscarAno.addActionListener(e -> {
            try {
                int ano = Integer.parseInt(txtAno.getText());
                List<Transacao> transacoes = controller.listarPorAno(usuario.getId(), ano);
                mostrarTransacoes(transacoes, areaResultados);
            } catch (Exception ex) {
                mostrarErro(ex);
            }
        });

        // Layout
        JPanel painelFiltros = new JPanel(new GridLayout(4, 3, 5, 5));
        painelFiltros.add(lblData); painelFiltros.add(datePicker); painelFiltros.add(btnBuscarData);
        painelFiltros.add(lblMesAno); painelFiltros.add(new JPanel(new FlowLayout()) {{
            add(new JLabel("Mês:")); add(txtMes); add(new JLabel("Ano:")); add(txtAnoMes);
        }}); painelFiltros.add(btnBuscarMesAno);
        painelFiltros.add(lblAno); painelFiltros.add(txtAno); painelFiltros.add(btnBuscarAno);

        getContentPane().setLayout(new BorderLayout());
        add(painelFiltros, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        setVisible(true);
    }

    private void mostrarTransacoes(List<Transacao> transacoes, JTextArea area) {
        if (transacoes.isEmpty()) {
            area.setText("Nenhuma transação encontrada para o filtro selecionado.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Transacao t : transacoes) {
            sb.append(String.format("[%s] %s - %.2f (%s)%n", t.getData_transacao(), t.getDescricao(), t.getValor(), t.getTipo()));
        }
        area.setText(sb.toString());
    }

    private void mostrarErro(Exception ex) {
        JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
