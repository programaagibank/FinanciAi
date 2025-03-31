package financiai.financiai.model;

public class Parcela {
    private int financiamentoId;
    private int numeroParcela;
    private double valorParcela;
    private double valorJuros;
    private double valorAmortizacao;
    private double saldoDevedor;

    public Parcela(int numeroParcela, double valorParcela, double valorAmortizacao,
                   double valorJuros, double saldoDevedor) {
        this.numeroParcela = numeroParcela;
        this.valorParcela = valorParcela;
        this.valorAmortizacao = valorAmortizacao;
        this.valorJuros = valorJuros;
        this.saldoDevedor = saldoDevedor;
    }

    // Getters e Setters
    public int getFinanciamentoId() {
        return financiamentoId;
    }

    public void setFinanciamentoId(int financiamentoId) {
        this.financiamentoId = financiamentoId;
    }

    public int getNumeroParcela() {
        return numeroParcela;
    }

    public double getValorParcela() {
        return valorParcela;
    }

    public double getValorJuros() {
        return valorJuros;
    }

    public double getValorAmortizacao() {
        return valorAmortizacao;
    }

    public double getSaldoDevedor() {
        return saldoDevedor;
    }
}