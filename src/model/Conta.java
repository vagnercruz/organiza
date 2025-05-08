package model;

public class Conta {
    private int id;
    private int usuarioId;
    private String nome;
    private Tipo tipo;
    private double saldoInicial;

    public enum Tipo {
        CORRENTE, POUPANCA, CREDITO, INVESTIMENTO, DINHEIRO
    }

    // Construtor padrão
    public Conta() {
    }

    // Construtor completo (com ID)
    public Conta(int id, int usuarioId, String nome, Tipo tipo, double saldoInicial) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.nome = nome;
        this.tipo = tipo;
        this.saldoInicial = saldoInicial;
    }

    // Construtor sem ID (para novas contas)
    public Conta(int usuarioId, String nome, Tipo tipo, double saldoInicial) {
        this.usuarioId = usuarioId;
        this.nome = nome;
        this.tipo = tipo;
        this.saldoInicial = saldoInicial;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Tipo getTipo() { return tipo; }
    public void setTipo(Tipo tipo) { this.tipo = tipo; }

    public double getSaldoInicial() { return saldoInicial; }
    public void setSaldoInicial(double saldoInicial) { this.saldoInicial = saldoInicial; }
}
