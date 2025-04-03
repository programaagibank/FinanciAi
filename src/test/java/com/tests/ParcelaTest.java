package com.tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import financiai.financiai.model.Parcela;

class ParcelaTest {

    @Test
    void testCriacaoParcela() {
        Parcela parcela = new Parcela(1, 1000.0, 800.0, 200.0, 5000.0, "123.456.789-00");

        assertEquals(1, parcela.getNumeroParcela());
        assertEquals(1000.0, parcela.getValorParcela());
        assertEquals(800.0, parcela.getValorAmortizacao());
        assertEquals(200.0, parcela.getValorJuros());
        assertEquals(5000.0, parcela.getSaldoDevedor());
        assertEquals("12345678900", parcela.getCpf());
    }
}
