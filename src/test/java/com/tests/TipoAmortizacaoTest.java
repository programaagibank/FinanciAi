package com.tests;

import financiai.financiai.model.Parcela;
import financiai.financiai.services.TipoAmortizacao;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class TipoAmortizacaoTest {

    @Test
    void testSAC_CalcularParcela() {
        double valor = 100000.0;
        double taxa = 0.01;
        int prazo = 120;
        String cpf = "12345678900";

        // Chamando o método do enum
        List<Parcela> parcelas = TipoAmortizacao.SAC.calcularParcela(valor, taxa, prazo, cpf);

        // Verificando se a lista não está vazia
        assertNotNull(parcelas);
        assertFalse(parcelas.isEmpty(), "A lista de parcelas não pode estar vazia.");
    }

    @Test
    void testPRICE_CalcularParcela() {
        double valor = 100000.0;
        double taxa = 0.01;
        int prazo = 120;
        String cpf = "12345678900";

        // Chamando o método do enum
        List<Parcela> parcelas = TipoAmortizacao.PRICE.calcularParcela(valor, taxa, prazo, cpf);

        // Verificando se a lista não está vazia
        assertNotNull(parcelas);
        assertFalse(parcelas.isEmpty(), "A lista de parcelas não pode estar vazia.");
    }
}
