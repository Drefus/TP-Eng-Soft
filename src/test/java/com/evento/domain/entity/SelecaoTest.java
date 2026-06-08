package com.evento.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Selecao - Testes de Entidade")
class SelecaoTest {

    @Test
    @DisplayName("Deve calcular saldo de gols corretamente (positivo e negativo)")
    void deveCalcularSaldoDeGols() {
        Selecao selecao = new Selecao("Brasil", "A", "Dorival", "🇧🇷", "BR");
        selecao.setGolsPro(5);
        selecao.setGolsContra(2);
        assertEquals(3, selecao.getSaldoGols());

        selecao.setGolsPro(1);
        selecao.setGolsContra(4);
        assertEquals(-3, selecao.getSaldoGols());
    }

    @Test
    @DisplayName("Deve calcular total de jogos a partir de V/E/D")
    void deveCalcularTotalDeJogos() {
        Selecao selecao = new Selecao("Argentina", "A", "Scaloni", "🇦🇷", "AR");
        assertEquals(0, selecao.getJogos());

        selecao.setVitorias(2);
        selecao.setEmpates(1);
        selecao.setDerrotas(1);
        assertEquals(4, selecao.getJogos());
    }
}
