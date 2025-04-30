package model;

public enum TipoTransacao {
    ENTRADA,
    SAIDA;

    public static TipoTransacao fromString(String tipo) {
        if (tipo == null) return null;
        return TipoTransacao.valueOf(tipo.toUpperCase().replace("Í", "I"));
    }
}