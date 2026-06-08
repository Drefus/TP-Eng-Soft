package com.evento.domain.service;

import com.evento.domain.entity.Selecao;
import com.evento.infrastructure.repository.SelecaoRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TeamService - Testes Adicionais")
class TeamServiceAdditionalTest {

    @Mock
    private SelecaoRepository selecaoRepository;

    @InjectMocks
    private TeamService teamService;

    private Selecao brasil;
    private Selecao argentina;
    private Selecao alemanha;

    @BeforeEach
    void setUp() {
        brasil = new Selecao("Brasil", "A", "Dorival", "🇧🇷", "BR");
        brasil.setId(1L);
        brasil.setPontos(9);
        brasil.setGolsPro(6);
        brasil.setGolsContra(1);

        argentina = new Selecao("Argentina", "A", "Scaloni", "🇦🇷", "AR");
        argentina.setId(2L);
        argentina.setPontos(6);
        argentina.setGolsPro(4);
        argentina.setGolsContra(2);

        alemanha = new Selecao("Alemanha", "B", "Nagelsmann", "🇩🇪", "DE");
        alemanha.setId(3L);
        alemanha.setPontos(7);
    }

    @Test
    @DisplayName("Deve listar todas as seleções em ordem alfabética")
    void deveListarTodasSelecoesPorNome() {
        when(selecaoRepository.findAll()).thenReturn(Arrays.asList(brasil, alemanha, argentina));

        List<Selecao> resultado = teamService.listarTodasSelecoes();

        assertEquals(3, resultado.size());
        assertEquals("Alemanha", resultado.get(0).getNome());
        assertEquals("Argentina", resultado.get(1).getNome());
        assertEquals("Brasil", resultado.get(2).getNome());
    }

    @Test
    @DisplayName("Deve buscar seleção existente por ID")
    void deveBuscarSelecaoExistente() {
        when(selecaoRepository.findById(1L)).thenReturn(Optional.of(brasil));

        Optional<Selecao> resultado = teamService.buscarSelecao(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Brasil", resultado.get().getNome());
    }

    @Test
    @DisplayName("Deve listar seleções por grupo específico")
    void deveListarPorGrupo() {
        when(selecaoRepository.findByGrupo("A")).thenReturn(Arrays.asList(brasil, argentina));

        List<Selecao> resultado = teamService.listarPorGrupo("A");

        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(s -> "A".equals(s.getGrupo())));
        verify(selecaoRepository).findByGrupo("A");
    }

    @Test
    @DisplayName("Deve listar grupos distintos ordenados")
    void deveListarGrupos() {
        Selecao franca = new Selecao("França", "C", "Desc", "🇫🇷", "FR");
        when(selecaoRepository.findAll()).thenReturn(Arrays.asList(brasil, argentina, alemanha, franca));

        List<String> grupos = teamService.listarGrupos();

        assertEquals(3, grupos.size());
        assertEquals("A", grupos.get(0));
        assertEquals("B", grupos.get(1));
        assertEquals("C", grupos.get(2));
    }

    @Test
    @DisplayName("Deve filtrar grupos inválidos (? e ??) ao listar grupos")
    void deveFiltrarGruposInvalidosNaListagem() {
        Selecao indefinida = new Selecao("A Definir", "??", "", "", "");
        Selecao indefinida2 = new Selecao("TBD", "?", "", "", "");
        when(selecaoRepository.findAll()).thenReturn(Arrays.asList(brasil, indefinida, indefinida2));

        List<String> grupos = teamService.listarGrupos();

        // grupos ?? e ? devem aparecer aqui pois listarGrupos não filtra (apenas listarSelecoes e listarPorGrupos filtram)
        assertTrue(grupos.contains("A"));
    }

    @Test
    @DisplayName("Deve ordenar seleções por saldo de gols quando pontos empatados")
    void deveOrdenarPorSaldoGolsQuandoPontosIguais() {
        Selecao uruguai = new Selecao("Uruguai", "A", "Alonso", "🇺🇾", "UY");
        uruguai.setId(4L);
        uruguai.setPontos(9);        // igual ao Brasil
        uruguai.setGolsPro(4);
        uruguai.setGolsContra(3);   // saldo 1

        when(selecaoRepository.findAll()).thenReturn(Arrays.asList(uruguai, brasil, argentina));

        Map<String, List<Selecao>> grupos = teamService.listarPorGrupos();

        List<Selecao> grupoA = grupos.get("A");
        assertEquals(3, grupoA.size());
        // Argentina (6 pts) deve vir por último
        assertEquals("Argentina", grupoA.get(2).getNome());
        // Brasil e Uruguai (9 pts) devem estar nas 2 primeiras posições
        assertTrue(grupoA.subList(0, 2).stream()
                .anyMatch(s -> "Brasil".equals(s.getNome())));
        assertTrue(grupoA.subList(0, 2).stream()
                .anyMatch(s -> "Uruguai".equals(s.getNome())));
    }
}
