package financiai.financiai.services;
import financiai.financiai.model.Parcela;
import java.util.List;

public enum TipoAmortizacao {
    SAC {
        @Override
        public List<Parcela> calcularParcela(Double valor, Double taxa, int prazo, String cpf) {
            return new SAC().calcularParcela(valor, taxa, prazo, cpf);
        }
    },
    PRICE {
        @Override
        public List<Parcela> calcularParcela(Double valor, Double taxa, int prazo, String cpf) {
            return new Price().calcularParcela(valor, taxa, prazo, cpf);
        }
    };

    public abstract List<Parcela> calcularParcela(Double valor, Double taxa, int prazo, String cpf);
}