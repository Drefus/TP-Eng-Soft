package com.evento.domain.service;

import com.evento.domain.entity.*;
import com.evento.infrastructure.repository.PartidaRepository;
import com.evento.infrastructure.repository.SelecaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GameService - Testes Adicionais")
class GameServiceAdditionalTest {

    @Mock
    private PartidaRepository partidaRepository;

    @Mock
    private SelecaoRepository selecaoRepository;

    @InjectMocks
    private GameService gameService;

    private Selecao brasil;
    private Selecao argentina;
    private Partida partida;

    @BeforeEach
    void setUp() {
        brasil = new Selecao("Brasil", "A", "Dorival", "🇧🇷", "BR");
        brasil.setId(1L);

        argentina = new Selecao("Argentina", "A", "Scaloni", "🇦🇷", "AR");
        argentina.setId(2L);

        partida = new Partida(
                LocalDate.of(2026, 6, 14), LocalTime.of(18, 0),
                brasil, argentina, "Grupo", "AGENDADA",
                new Estadio("MetLife", 82500), new CidadeSede("NY", "EUA", "Desc"));
        partida.setId(1L);
    }

    @Test
    @DisplayName("Deve buscar partida por ID quando existe")
    void deveBuscarPartidaExistente() {
        when(partidaRepository.findById(1L)).thenReturn(Optional.of(partida));

        Optional<Partida> resultado = gameService.buscarPartida(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
    }

    @Test
    @DisplayName("Deve retornar Optional vazio para partida inexistente")
    void deveRetornarVazioParaPartidaInexistente() {
        when(partidaRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Partida> resultado = gameService.buscarPartida(999L);

        assertFalse(resultado.isPresent());
    }

    @Test
    @DisplayName("Deve filtrar partidas com todos os parâmetros")
    void deveFiltrarPartidasComTodosParametros() {
        LocalDate data = LocalDate.of(2026, 6, 14);
        when(partidaRepository.filtrar(data, 1L, 1L)).thenReturn(List.of(partida));

        List<Partida> resultado = gameService.filtrarPartidas(data, 1L, 1L);

        assertEquals(1, resultado.size());
        verify(partidaRepository).filtrar(data, 1L, 1L);
    }

    @Test
    @DisplayName("Deve filtrar partidas com parâmetros nulos")
    void deveFiltrarPartidasSemParametros() {
        when(partidaRepository.filtrar(null, null, null)).thenReturn(List.of(partida));

        List<Partida> resultado = gameService.filtrarPartidas(null, null, null);

        assertEquals(1, resultado.size());
        verify(partidaRepository).filtrar(null, null, null);
    }

    @Test
    @DisplayName("Deve buscar partidas por fase")
    void deveBuscarPorFase() {
        when(partidaRepository.findByFaseOrderByDataAsc("Grupo")).thenReturn(List.of(partida));

        List<Partida> resultado = gameService.buscarPorFase("Grupo");

        assertEquals(1, resultado.size());
        verify(partidaRepository).findByFaseOrderByDataAsc("Grupo");
    }

    @Test
    @DisplayName("Deve buscar partidas por seleção")
    void deveBuscarPorSelecao() {
        when(partidaRepository.findBySelecao(1L)).thenReturn(List.of(partida));

        List<Partida> resultado = gameService.buscarPorSelecao(1L);

        assertEquals(1, resultado.size());
        verify(partidaRepository).findBySelecao(1L);
    }

    @Test
    @DisplayName("Deve buscar próximas partidas com status AGENDADA")
    void deveBuscarProximasPartidas() {
        when(partidaRepository.findByStatusOrderByDataAsc("AGENDADA")).thenReturn(List.of(partida));

        List<Partida> resultado = gameService.buscarProximasPartidas();

        assertEquals(1, resultado.size());
        assertEquals("AGENDADA", resultado.get(0).getStatus());
        verify(partidaRepository).findByStatusOrderByDataAsc("AGENDADA");
    }

    @Test
    @DisplayName("Deve reverter estatísticas de empate ao corrigir resultado")
    void deveReverterEstatisticasDeEmpate() {
        // Partida já finalizada 1x1 (empate)
        partida.setStatus("FINALIZADA");
        partida.setGolsTime1(1);
        partida.setGolsTime2(1);
        brasil.setPontos(1);
        brasil.setEmpates(1);
        brasil.setGolsPro(1);
        brasil.setGolsContra(1);
        argentina.setPontos(1);
        argentina.setEmpates(1);
        argentina.setGolsPro(1);
        argentina.setGolsContra(1);

        when(partidaRepository.findById(1L)).thenReturn(Optional.of(partida));
        when(partidaRepository.save(any())).thenReturn(partida);

        // Corrige para vitória do Brasil 3x0
        gameService.atualizarResultado(1L, 3, 0);

        assertEquals(3, brasil.getPontos());  // -1 (revert empate) + 3 (vitória)
        assertEquals(1, brasil.getVitorias());
        assertEquals(0, brasil.getEmpates());
        assertEquals(0, argentina.getPontos()); // -1 (revert empate) + 0
        assertEquals(1, argentina.getDerrotas());
        assertEquals(0, argentina.getEmpates());
    }

    @Test
    @DisplayName("Deve reverter derrota ao corrigir resultado")
    void deveReverterDerrotaAoCorrigirResultado() {
        // Partida já finalizada, Time2 venceu 2x0
        partida.setStatus("FINALIZADA");
        partida.setGolsTime1(0);
        partida.setGolsTime2(2);
        brasil.setPontos(0);
        brasil.setDerrotas(1);
        brasil.setGolsContra(2);
        argentina.setPontos(3);
        argentina.setVitorias(1);
        argentina.setGolsPro(2);

        when(partidaRepository.findById(1L)).thenReturn(Optional.of(partida));
        when(partidaRepository.save(any())).thenReturn(partida);

        // Corrige para vitória do Brasil 1x0
        gameService.atualizarResultado(1L, 1, 0);

        assertEquals(3, brasil.getPontos());   // +3 (vitória)
        assertEquals(1, brasil.getVitorias());
        assertEquals(0, brasil.getDerrotas());
        assertEquals(0, argentina.getPontos()); // -3 (revert vitória)
        assertEquals(0, argentina.getVitorias());
        assertEquals(1, argentina.getDerrotas());
    }
}
