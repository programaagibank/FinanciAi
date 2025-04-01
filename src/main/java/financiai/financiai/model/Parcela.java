package financiai.financiai.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Parcela {
    private final SimpleIntegerProperty numeroParcela;
    private final SimpleDoubleProperty valorParcela;
    private final SimpleDoubleProperty valorAmortizacao;
    private final SimpleDoubleProperty valorJuros;
    private final SimpleDoubleProperty saldoDevedor;
    private String cpf;
    private int financiamentoId;

    public Parcela(int numeroParcela, double valorParcela, double valorAmortizacao,
                   double valorJuros, double saldoDevedor, String cpf) {
        this.numeroParcela = new SimpleIntegerProperty(numeroParcela);
        this.valorParcela = new SimpleDoubleProperty(valorParcela);
        this.valorAmortizacao = new SimpleDoubleProperty(valorAmortizacao);
        this.valorJuros = new SimpleDoubleProperty(valorJuros);
        this.saldoDevedor = new SimpleDoubleProperty(saldoDevedor);
        this.cpf = cpf.replaceAll("[^0-9]", "");
    }

    // Getters para as propriedades (necessários para o TableView)
    public int getNumeroParcela() {
        return numeroParcela.get();
    }

    public double getValorParcela() {
        return valorParcela.get();
    }

    public double getValorAmortizacao() {
        return valorAmortizacao.get();
    }

    public double getValorJuros() {
        return valorJuros.get();
    }

    public double getSaldoDevedor() {
        return saldoDevedor.get();
    }

     // Getters e Setters
    public int getFinanciamentoId() {
        return financiamentoId;
    }

    public void setFinanciamentoId(int financiamentoId) {
        this.financiamentoId = financiamentoId;
    }


    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

}