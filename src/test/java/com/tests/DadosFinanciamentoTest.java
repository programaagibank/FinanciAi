package com.tests;

import financiai.financiai.services.TipoAmortizacao;
import financiai.financiai.services.TipoImovel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClassePrincipal {

    private static class DadosFinanciamento {
        String nome;
        String cpf;
        double renda;
        double valorImovel;
        double valorEntrada;
        double taxaJuros;
        int prazo;
        TipoImovel tipoImovel;
        TipoAmortizacao tipoAmortizacao;
    }

    @Test
    void testCriacaoDadosFinanciamento() {
        DadosFinanciamento dados = new DadosFinanciamento();
        dados.nome = "João Silva";
        dados.cpf = "12345678900";
        dados.renda = 5000.0;
        dados.valorImovel = 300000.0;
        dados.valorEntrada = 60000.0;
        dados.taxaJuros = 0.008;
        dados.prazo = 240;
        dados.tipoImovel = TipoImovel.CASA;
        dados.tipoAmortizacao = TipoAmortizacao.SAC;

        assertEquals("João Silva", dados.nome);
        assertEquals("12345678900", dados.cpf);
    }
}

