package model;

import java.time.LocalDate;

public class Transacao {
    private int id;
    private int usuarioId;
    private Tipo tipo;
    private String descricao;
    private double valor;
    private LocalDate data_transacao;
    private String categoria;

    public enum Tipo {
        ENTRADA, SAIDA
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDate getData_transacao() {
        return data_transacao;
    }

    public void setData_transacao(LocalDate data_transacao) {
        this.data_transacao = data_transacao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
