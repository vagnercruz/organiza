package view;

import controller.TransacaoController;
import controller.UsuarioController;
import model.Transacao;
import model.Usuario;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UsuarioController uc = new UsuarioController();
        Usuario usuarioLogado = null;

        System.out.println("=== Sistema de Gestão Financeira ===");
        System.out.println("1 - Cadastrar novo usuário");
        System.out.println("2 - Fazer login");
        System.out.print("Escolha uma opção: ");
        int opcao = sc.nextInt();
        sc.nextLine(); // limpar buffer

        switch (opcao) {
            case 1:
                System.out.print("Nome: ");
                String nome = sc.nextLine();
                System.out.print("Email: ");
                String email = sc.nextLine();
                System.out.print("Senha: ");
                String senha = sc.nextLine();
                uc.cadastrar(nome, email, senha);
                break;

            case 2:
                System.out.print("Email: ");
                String emailLogin = sc.nextLine();
                System.out.print("Senha: ");
                String senhaLogin = sc.nextLine();
                usuarioLogado = uc.autenticar(emailLogin, senhaLogin);

                if (usuarioLogado != null) {
                    System.out.println("Acesso autorizado!");
                    TransacaoController tc = new TransacaoController();

                    while (true) {
                        System.out.println("\n--- Menu de Transações ---");
                        System.out.println("1 - Cadastrar nova transação");
                        System.out.println("2 - Listar transações");
                        System.out.println("0 - Sair");
                        System.out.print("Escolha uma opção: ");
                        int op = sc.nextInt();
                        sc.nextLine(); // limpar buffer

                        if (op == 1) {
                            Transacao t = new Transacao();
                            t.setUsuarioId(usuarioLogado.getId());

                            System.out.print("Tipo (entrada/saida): ");
                            t.setTipo(sc.nextLine());

                            System.out.print("Descrição: ");
                            t.setDescricao(sc.nextLine());

                            System.out.print("Valor: ");
                            t.setValor(sc.nextDouble());

                            System.out.print("Data (AAAA-MM-DD): ");
                            t.setData_transacao(LocalDate.parse(sc.next()));

                            tc.cadastrar(t);
                            sc.nextLine(); // limpar buffer extra

                        } else if (op == 2) {
                            List<Transacao> lista = tc.Listar(usuarioLogado.getId());
                            if (lista != null && !lista.isEmpty()) {
                                System.out.println("\n--- Suas transações ---");
                                for (Transacao t : lista) {
                                    System.out.printf("%s | %s | R$ %.2f | %s\n",
                                            t.getData_transacao(), t.getTipo().toUpperCase(), t.getValor(), t.getDescricao());
                                }
                            } else {
                                System.out.println("Nenhuma transação encontrada.");
                            }

                        } else if (op == 0) {
                            System.out.println("Saindo...");
                            break;
                        } else {
                            System.out.println("Opção inválida.");
                        }
                    }

                } else {
                    System.out.println("Falha no login.");
                }
                break;

            default:
                System.out.println("Opção inválida.");
        }
    }
}
