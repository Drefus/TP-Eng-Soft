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
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GameService - Testes de Serviço")
class GameServiceTest {

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
    @DisplayName("Deve listar partidas ordenadas por data")
    void deveListarPartidas() {
        when(partidaRepository.findAllByOrderByDataAscHorarioAsc()).thenReturn(List.of(partida));
        List<Partida> resultado = gameService.listarPartidas();
        assertEquals(1, resultado.size());
        verify(partidaRepository).findAllByOrderByDataAscHorarioAsc();
    }

    @Test
    @DisplayName("Deve atualizar resultado com vitória do time 1 (+3 pts, +1 vitória, +1 derrota)")
    void deveAtualizarResultadoVitoriaTime1() {
        when(partidaRepository.findById(1L)).thenReturn(Optional.of(partida));
        when(partidaRepository.save(any())).thenReturn(partida);

        gameService.atualizarResultado(1L, 2, 1);

        assertEquals("FINALIZADA", partida.getStatus());
        assertEquals(3, brasil.getPontos());
        assertEquals(1, brasil.getVitorias());
        assertEquals(2, brasil.getGolsPro());
        assertEquals(0, argentina.getPontos());
        assertEquals(1, argentina.getDerrotas());
    }

    @Test
    @DisplayName("Deve atualizar resultado com vitória do time 2")
    void deveAtualizarResultadoVitoriaTime2() {
        when(partidaRepository.findById(1L)).thenReturn(Optional.of(partida));
        when(partidaRepository.save(any())).thenReturn(partida);

        gameService.atualizarResultado(1L, 0, 3);

        assertEquals(3, argentina.getPontos());
        assertEquals(1, argentina.getVitorias());
        assertEquals(0, brasil.getPontos());
        assertEquals(1, brasil.getDerrotas());
    }

    @Test
    @DisplayName("Deve atualizar resultado com empate (+1 pt para cada)")
    void deveAtualizarResultadoEmpate() {
        when(partidaRepository.findById(1L)).thenReturn(Optional.of(partida));
        when(partidaRepository.save(any())).thenReturn(partida);

        gameService.atualizarResultado(1L, 1, 1);

        assertEquals(1, brasil.getPontos());
        assertEquals(1, brasil.getEmpates());
        assertEquals(1, argentina.getPontos());
        assertEquals(1, argentina.getEmpates());
    }

    @Test
    @DisplayName("Deve reverter estatísticas ao corrigir resultado de partida já finalizada")
    void deveReverterEstatisticasQuandoJaFinalizada() {
        // Partida já finalizada 2x0
        partida.setStatus("FINALIZADA");
        partida.setGolsTime1(2);
        partida.setGolsTime2(0);
        brasil.setPontos(3);
        brasil.setVitorias(1);
        brasil.setGolsPro(2);
        argentina.setDerrotas(1);
        argentina.setGolsContra(2);

        when(partidaRepository.findById(1L)).thenReturn(Optional.of(partida));
        when(partidaRepository.save(any())).thenReturn(partida);

        // Corrige para 1x1
        gameService.atualizarResultado(1L, 1, 1);

        assertEquals(1, brasil.getPontos());    // -3 (revert) + 1 (empate)
        assertEquals(0, brasil.getVitorias());
        assertEquals(1, brasil.getEmpates());
        assertEquals(0, argentina.getDerrotas());
        assertEquals(1, argentina.getEmpates());
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar partida inexistente")
    void deveLancarExcecaoPartidaInexistente() {
        when(partidaRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> gameService.atualizarResultado(999L, 1, 0));
    }
}
