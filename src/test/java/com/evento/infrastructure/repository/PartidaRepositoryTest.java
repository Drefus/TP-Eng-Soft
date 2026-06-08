package com.evento.infrastructure.repository;

import com.evento.domain.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(properties = "spring.sql.init.mode=never")
@DisplayName("PartidaRepository - Testes de Repositório")
class PartidaRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private PartidaRepository partidaRepository;

    private Selecao brasil;
    private Selecao argentina;
    private Selecao alemanha;
    private Estadio estadio;
    private CidadeSede cidade1;
    private CidadeSede cidade2;
    private Partida partida1;
    private Partida partida2;
    private Partida partida3;

    @BeforeEach
    void setUp() {
        brasil = em.persist(new Selecao("Brasil", "A", "Dorival", "🇧🇷", "BR"));
        argentina = em.persist(new Selecao("Argentina", "A", "Scaloni", "🇦🇷", "AR"));
        alemanha = em.persist(new Selecao("Alemanha", "B", "Nagelsmann", "🇩🇪", "DE"));

        estadio = em.persist(new Estadio("MetLife", 82500));
        cidade1 = em.persist(new CidadeSede("Nova York", "EUA", "Desc NY"));
        cidade2 = em.persist(new CidadeSede("Los Angeles", "EUA", "Desc LA"));

        // Partida 1: Brasil vs Argentina, Grupo, AGENDADA, cidade1, 14/06
        partida1 = em.persist(new Partida(
                LocalDate.of(2026, 6, 14), LocalTime.of(18, 0),
                brasil, argentina, "Grupo", "AGENDADA", estadio, cidade1));

        // Partida 2: Brasil vs Alemanha, Grupo, FINALIZADA, cidade2, 18/06
        partida2 = em.persistAndFlush(new Partida(
                LocalDate.of(2026, 6, 18), LocalTime.of(21, 0),
                brasil, alemanha, "Grupo", "FINALIZADA", estadio, cidade2));
        partida2.setGolsTime1(2);
        partida2.setGolsTime2(1);
        partida2 = em.merge(partida2);

        // Partida 3: Argentina vs Alemanha, Final, AGENDADA, cidade1, 19/07
        partida3 = em.persistAndFlush(new Partida(
                LocalDate.of(2026, 7, 19), LocalTime.of(16, 0),
                argentina, alemanha, "Final", "AGENDADA", estadio, cidade1));

        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("Deve retornar partidas ordenadas por data e horário")
    void deveListarPartidasOrdenadasPorDataEHorario() {
        List<Partida> resultado = partidaRepository.findAllByOrderByDataAscHorarioAsc();

        assertEquals(3, resultado.size());
        assertEquals(LocalDate.of(2026, 6, 14), resultado.get(0).getData());
        assertEquals(LocalDate.of(2026, 6, 18), resultado.get(1).getData());
        assertEquals(LocalDate.of(2026, 7, 19), resultado.get(2).getData());
    }

    @Test
    @DisplayName("Deve buscar partidas por seleção (time1 ou time2)")
    void deveBuscarPartidasPorSelecao() {
        List<Partida> resultadoBrasil = partidaRepository.findBySelecao(brasil.getId());
        List<Partida> resultadoArgentina = partidaRepository.findBySelecao(argentina.getId());

        // Brasil aparece em partida1 (time1) e partida2 (time1)
        assertEquals(2, resultadoBrasil.size());
        // Argentina aparece em partida1 (time2) e partida3 (time1)
        assertEquals(2, resultadoArgentina.size());
    }

    @Test
    @DisplayName("Deve buscar partidas por cidade")
    void deveBuscarPartidasPorCidade() {
        List<Partida> resultadoCidade1 = partidaRepository.findByCidade(cidade1.getId());
        List<Partida> resultadoCidade2 = partidaRepository.findByCidade(cidade2.getId());

        // cidade1: partida1 e partida3
        assertEquals(2, resultadoCidade1.size());
        // cidade2: apenas partida2
        assertEquals(1, resultadoCidade2.size());
    }

    @Test
    @DisplayName("Deve buscar partidas por fase")
    void deveBuscarPartidasPorFase() {
        List<Partida> grupoPartidas = partidaRepository.findByFaseOrderByDataAsc("Grupo");
        List<Partida> finalPartidas = partidaRepository.findByFaseOrderByDataAsc("Final");

        assertEquals(2, grupoPartidas.size());
        assertEquals(1, finalPartidas.size());
        assertEquals("Final", finalPartidas.get(0).getFase());
    }

    @Test
    @DisplayName("Deve buscar partidas por status")
    void deveBuscarPartidasPorStatus() {
        List<Partida> agendadas = partidaRepository.findByStatusOrderByDataAsc("AGENDADA");
        List<Partida> finalizadas = partidaRepository.findByStatusOrderByDataAsc("FINALIZADA");

        assertEquals(2, agendadas.size());
        assertEquals(1, finalizadas.size());
    }

    @Test
    @DisplayName("Deve filtrar partidas por data")
    void deveFiltrarPartidasPorData() {
        List<Partida> resultado = partidaRepository.filtrar(
                LocalDate.of(2026, 6, 14), null, null);

        assertEquals(1, resultado.size());
        assertEquals(LocalDate.of(2026, 6, 14), resultado.get(0).getData());
    }

    @Test
    @DisplayName("Deve filtrar partidas por seleção")
    void deveFiltrarPartidasPorSelecao() {
        List<Partida> resultado = partidaRepository.filtrar(null, alemanha.getId(), null);

        assertEquals(2, resultado.size());
    }

    @Test
    @DisplayName("Deve filtrar partidas por cidade")
    void deveFiltrarPartidasPorCidade() {
        List<Partida> resultado = partidaRepository.filtrar(null, null, cidade2.getId());

        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("Deve filtrar com todos os parâmetros combinados")
    void deveFiltrarComTodosParametros() {
        List<Partida> resultado = partidaRepository.filtrar(
                LocalDate.of(2026, 6, 14), brasil.getId(), cidade1.getId());

        assertEquals(1, resultado.size());
        assertEquals(brasil.getId(), resultado.get(0).getTime1().getId());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando combinação de filtros não tem resultado")
    void deveRetornarVazioParaFiltroSemResultado() {
        List<Partida> resultado = partidaRepository.filtrar(
                LocalDate.of(2026, 6, 14), alemanha.getId(), null);

        assertTrue(resultado.isEmpty());
    }
}
