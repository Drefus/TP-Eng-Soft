package com.evento.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Chaveamento - Testes de Entidade")
class ChaveamentoTest {

    @Test
    @DisplayName("Deve criar chaveamento com construtor")
    void deveCriarChaveamentoComConstrutor() {
        Partida partida = new Partida();
        Chaveamento chaveamento = new Chaveamento("Quartas", 1, partida);

        assertEquals("Quartas", chaveamento.getFase());
        assertEquals(1, chaveamento.getOrdem());
        assertEquals(partida, chaveamento.getPartida());
    }

    @Test
    @DisplayName("Deve aceitar partida nula (vaga ainda não definida)")
    void deveAceitarPartidaNula() {
        Chaveamento chaveamento = new Chaveamento("Final", 1, null);

        assertEquals("Final", chaveamento.getFase());
        assertNull(chaveamento.getPartida());
    }

    @Test
    @DisplayName("Deve atualizar fase via setter")
    void deveAtualizarFase() {
        Chaveamento chaveamento = new Chaveamento("Quartas", 1, null);
        chaveamento.setFase("Semifinal");

        assertEquals("Semifinal", chaveamento.getFase());
    }

    @Test
    @DisplayName("Deve associar partida real ao chaveamento")
    void deveAssociarPartida() {
        Selecao t1 = new Selecao("Brasil", "A", "D", "🇧🇷", "BR");
        Selecao t2 = new Selecao("Argentina", "A", "S", "🇦🇷", "AR");
        Partida partida = new Partida(
                LocalDate.of(2026, 7, 4), LocalTime.of(21, 0),
                t1, t2, "Quartas", "AGENDADA",
                new Estadio("MetLife", 82500), new CidadeSede("NY", "EUA", "Desc"));

        Chaveamento chaveamento = new Chaveamento("Quartas", 1, partida);

        assertNotNull(chaveamento.getPartida());
        assertEquals("Quartas", chaveamento.getPartida().getFase());
        assertEquals(t1, chaveamento.getPartida().getTime1());
    }
}
