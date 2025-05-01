package model;

import java.time.LocalDate;
import model.TipoTransacao;


public class Transacao {
    private int id;
    private int usuarioId;
    private TipoTransacao tipo;
    private String descricao;
    private double valor;
    private LocalDate data_transacao;

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public LocalDate getData_transacao() {return data_transacao;}
    public void setData_transacao(LocalDate data_transacao) {this.data_transacao = data_transacao;}

    public double getValor() {return valor;}
    public void setValor(double valor) {this.valor = valor;}

    public String getDescricao() {return descricao;}
    public void setDescricao(String descricao) {this.descricao = descricao;}

    public TipoTransacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoTransacao tipo) {
        this.tipo = tipo;
    }
    public int getUsuarioId() {return usuarioId;}
    public void setUsuarioId(int usuarioId) {this.usuarioId = usuarioId;}
}
