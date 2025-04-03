package com.tests;

import financiai.financiai.services.TipoImovel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TipoImovelTest {

    @Test
    void testEnumValues() {
        // Verifica se os valores do enum existem corretamente
        assertEquals(TipoImovel.CASA, TipoImovel.valueOf("CASA"));
        assertEquals(TipoImovel.APARTAMENTO, TipoImovel.valueOf("APARTAMENTO"));

        // Verifica se há apenas 2 valores no enum
        assertEquals(2, TipoImovel.values().length);
    }
}

