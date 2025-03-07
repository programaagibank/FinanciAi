package model.entities;

public class Parcelas {

    private int id;
    private int idParcela;
    private int numeroParcela;
    private Double valorParcela;
    private Double valorAmortizacao;

    public Parcelas(int id, int idParcela, int numeroParcela, Double valorParcela) {
        this.id = id;
        this.idParcela = idParcela;
        this.numeroParcela = numeroParcela;
        this.valorParcela = valorParcela;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdParcela() {
        return idParcela;
    }

    public void setIdParcela(int idParcela) {
        this.idParcela = idParcela;
    }

    public int getNumeroParcela() {
        return numeroParcela;
    }

    public void setNumeroParcela(int numeroParcela) {
        this.numeroParcela = numeroParcela;
    }

    public Double getValorParcela() {
        return valorParcela;
    }

    public void setValorParcela(Double valorParcela) {
        this.valorParcela = valorParcela;
    }


}
