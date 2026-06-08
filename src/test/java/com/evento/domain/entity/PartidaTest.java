package com.evento.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Partida - Testes de Entidade")
class PartidaTest {

    @Test
    @DisplayName("Deve retornar placar '- x -' quando gols não registrados e formatado quando registrados")
    void deveRetornarPlacarCorretamente() {
        Partida partida = new Partida();
        assertEquals("- x -", partida.getPlacar());

        partida.setGolsTime1(2);
        partida.setGolsTime2(1);
        assertEquals("2 x 1", partida.getPlacar());
    }

    @Test
    @DisplayName("Deve identificar partida finalizada vs não finalizada")
    void deveIdentificarStatusFinalizada() {
        Partida partida = new Partida();
        partida.setStatus("AGENDADA");
        assertFalse(partida.isFinalizada());

        partida.setStatus("FINALIZADA");
        assertTrue(partida.isFinalizada());
    }

    @Test
    @DisplayName("Deve criar partida com todos os campos via construtor")
    void deveCriarPartidaComConstrutor() {
        Selecao t1 = new Selecao("Brasil", "A", "D", "🇧🇷", "BR");
        Selecao t2 = new Selecao("Argentina", "A", "S", "🇦🇷", "AR");
        Estadio estadio = new Estadio("MetLife", 82500);
        CidadeSede cidade = new CidadeSede("New York", "EUA", "Desc");

        Partida partida = new Partida(
                LocalDate.of(2026, 7, 19), LocalTime.of(16, 0),
                t1, t2, "Final", "AGENDADA", estadio, cidade);

        assertEquals("Final", partida.getFase());
        assertEquals(t1, partida.getTime1());
        assertEquals(t2, partida.getTime2());
        assertEquals("AGENDADA", partida.getStatus());
    }
}
