package com.evento.domain.service;

import com.evento.domain.entity.Chaveamento;
import com.evento.infrastructure.repository.ChaveamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BracketService - Testes de Serviço")
class BracketServiceTest {

    @Mock
    private ChaveamentoRepository chaveamentoRepository;

    @InjectMocks
    private BracketService bracketService;

    private Chaveamento quartas1;
    private Chaveamento quartas2;
    private Chaveamento semi1;
    private Chaveamento final1;

    @BeforeEach
    void setUp() {
        quartas1 = new Chaveamento("Quartas", 1, null);
        quartas1.setId(1L);

        quartas2 = new Chaveamento("Quartas", 2, null);
        quartas2.setId(2L);

        semi1 = new Chaveamento("Semifinal", 1, null);
        semi1.setId(3L);

        final1 = new Chaveamento("Final", 1, null);
        final1.setId(4L);
    }

    @Test
    @DisplayName("Deve listar todos os chaveamentos ordenados por fase e ordem")
    void deveListarTodos() {
        List<Chaveamento> todos = Arrays.asList(quartas1, quartas2, semi1, final1);
        when(chaveamentoRepository.findAllByOrderByFaseAscOrdemAsc()).thenReturn(todos);

        List<Chaveamento> resultado = bracketService.listarTodos();

        assertEquals(4, resultado.size());
        verify(chaveamentoRepository).findAllByOrderByFaseAscOrdemAsc();
    }

    @Test
    @DisplayName("Deve buscar chaveamentos por fase")
    void deveBuscarPorFase() {
        when(chaveamentoRepository.findByFaseOrderByOrdemAsc("Quartas"))
                .thenReturn(Arrays.asList(quartas1, quartas2));

        List<Chaveamento> resultado = bracketService.buscarPorFase("Quartas");

        assertEquals(2, resultado.size());
        assertEquals("Quartas", resultado.get(0).getFase());
        verify(chaveamentoRepository).findByFaseOrderByOrdemAsc("Quartas");
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há chaveamentos na fase")
    void deveRetornarVazioParaFaseSemChaveamentos() {
        when(chaveamentoRepository.findByFaseOrderByOrdemAsc("Oitavas")).thenReturn(List.of());

        List<Chaveamento> resultado = bracketService.buscarPorFase("Oitavas");

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Deve agrupar por fases na ordem correta: Quartas -> Semifinal -> Final")
    void deveListarPorFasesNaOrdemCorreta() {
        List<Chaveamento> todos = Arrays.asList(quartas1, quartas2, semi1, final1);
        when(chaveamentoRepository.findAllByOrderByFaseAscOrdemAsc()).thenReturn(todos);

        Map<String, List<Chaveamento>> fases = bracketService.listarPorFases();

        assertTrue(fases.containsKey("Quartas"));
        assertTrue(fases.containsKey("Semifinal"));
        assertTrue(fases.containsKey("Final"));
        assertEquals(2, fases.get("Quartas").size());
        assertEquals(1, fases.get("Semifinal").size());
        assertEquals(1, fases.get("Final").size());

        // Verifica a ordem das chaves (LinkedHashMap deve manter a ordem Quartas, Semifinal, Final)
        List<String> keys = List.copyOf(fases.keySet());
        assertEquals("Quartas", keys.get(0));
        assertEquals("Semifinal", keys.get(1));
        assertEquals("Final", keys.get(2));
    }

    @Test
    @DisplayName("Deve omitir fases sem chaveamentos do mapa")
    void deveOmitirFasesSemChaveamentos() {
        // Apenas Quartas e Final, sem Semifinal
        List<Chaveamento> semSemi = Arrays.asList(quartas1, final1);
        when(chaveamentoRepository.findAllByOrderByFaseAscOrdemAsc()).thenReturn(semSemi);

        Map<String, List<Chaveamento>> fases = bracketService.listarPorFases();

        assertTrue(fases.containsKey("Quartas"));
        assertFalse(fases.containsKey("Semifinal"));
        assertTrue(fases.containsKey("Final"));
        assertEquals(2, fases.size());
    }

    @Test
    @DisplayName("Deve retornar mapa vazio quando não há chaveamentos")
    void deveRetornarMapaVazioSemChaveamentos() {
        when(chaveamentoRepository.findAllByOrderByFaseAscOrdemAsc()).thenReturn(List.of());

        Map<String, List<Chaveamento>> fases = bracketService.listarPorFases();

        assertTrue(fases.isEmpty());
    }
}
