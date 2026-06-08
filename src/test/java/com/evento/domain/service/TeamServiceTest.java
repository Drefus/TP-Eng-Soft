package com.evento.domain.service;

import com.evento.domain.entity.Selecao;
import com.evento.infrastructure.repository.SelecaoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TeamService - Testes de Serviço")
class TeamServiceTest {

    @Mock
    private SelecaoRepository selecaoRepository;

    @InjectMocks
    private TeamService teamService;

    private List<Selecao> criarSelecoes() {
        Selecao brasil = new Selecao("Brasil", "A", "Dorival", "🇧🇷", "BR");
        brasil.setId(1L);
        brasil.setPontos(9);
        brasil.setGolsPro(7);
        brasil.setGolsContra(1);

        Selecao argentina = new Selecao("Argentina", "A", "Scaloni", "🇦🇷", "AR");
        argentina.setId(2L);
        argentina.setPontos(6);
        argentina.setGolsPro(4);
        argentina.setGolsContra(2);

        Selecao alemanha = new Selecao("Alemanha", "B", "Nagelsmann", "🇩🇪", "DE");
        alemanha.setId(3L);
        alemanha.setPontos(7);

        Selecao indefinida = new Selecao("A Definir", "??", "", "", "");
        indefinida.setId(4L);

        return List.of(brasil, argentina, alemanha, indefinida);
    }

    @Test
    @DisplayName("Deve filtrar seleções com grupo inválido (?? e ?)")
    void deveFiltrarGruposInvalidos() {
        when(selecaoRepository.findAll()).thenReturn(criarSelecoes());

        List<Selecao> resultado = teamService.listarSelecoes();

        assertEquals(3, resultado.size());
        assertTrue(resultado.stream().noneMatch(s -> "??".equals(s.getGrupo())));
    }

    @Test
    @DisplayName("Deve agrupar seleções por grupo ordenadas por pontos (maior primeiro)")
    void deveAgruparPorGrupoOrdenadoPorPontos() {
        when(selecaoRepository.findAll()).thenReturn(criarSelecoes());

        Map<String, List<Selecao>> grupos = teamService.listarPorGrupos();

        assertTrue(grupos.containsKey("A"));
        assertTrue(grupos.containsKey("B"));
        assertFalse(grupos.containsKey("??"));
        // Brasil (9 pts) antes de Argentina (6 pts)
        assertEquals("Brasil", grupos.get("A").get(0).getNome());
        assertEquals("Argentina", grupos.get("A").get(1).getNome());
    }

    @Test
    @DisplayName("Deve retornar Optional vazio para seleção inexistente")
    void deveRetornarVazioParaSelecaoInexistente() {
        when(selecaoRepository.findById(999L)).thenReturn(Optional.empty());
        assertFalse(teamService.buscarSelecao(999L).isPresent());
    }
}
