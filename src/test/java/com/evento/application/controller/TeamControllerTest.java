package com.evento.application.controller;

import com.evento.domain.entity.Partida;
import com.evento.domain.entity.Selecao;
import com.evento.domain.service.GameService;
import com.evento.domain.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TeamController.class)
@DisplayName("TeamController - Testes de Controller")
class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TeamService teamService;

    @MockitoBean
    private GameService gameService;

    private Selecao brasil;

    @BeforeEach
    void setUp() {
        brasil = new Selecao("Brasil", "A", "Dorival", "🇧🇷", "BR");
        brasil.setId(1L);
    }

    @Test
    @WithMockUser
    @DisplayName("GET /selecoes deve listar seleções agrupadas por grupo")
    void deveListarSelecoes() throws Exception {
        when(teamService.listarPorGrupos()).thenReturn(Map.of("A", List.of(brasil)));
        when(teamService.listarTodasSelecoes()).thenReturn(List.of(brasil));

        mockMvc.perform(get("/selecoes"))
                .andExpect(status().isOk())
                .andExpect(view().name("selecoes"))
                .andExpect(model().attributeExists("grupos", "selecoes"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /selecoes/{id} deve retornar detalhe da seleção")
    void deveRetornarDetalheSelecao() throws Exception {
        when(teamService.buscarSelecao(1L)).thenReturn(Optional.of(brasil));
        when(gameService.buscarPorSelecao(1L)).thenReturn(List.of());

        mockMvc.perform(get("/selecoes/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("selecao-detalhe"))
                .andExpect(model().attributeExists("selecao", "partidas"))
                .andExpect(model().attribute("selecao", brasil));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /selecoes/{id} deve retornar partidas da seleção")
    void deveRetornarPartidasDaSelecao() throws Exception {
        Selecao argentina = new Selecao("Argentina", "A", "Scaloni", "🇦🇷", "AR");
        argentina.setId(2L);
        Partida partida = new Partida();
        partida.setId(1L);
        partida.setTime1(brasil);
        partida.setTime2(argentina);

        when(teamService.buscarSelecao(1L)).thenReturn(Optional.of(brasil));
        when(gameService.buscarPorSelecao(1L)).thenReturn(List.of(partida));

        mockMvc.perform(get("/selecoes/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("partidas",
                        org.hamcrest.Matchers.hasSize(1)));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /selecoes/{id} deve lançar exceção para seleção inexistente")
    void deveLancarExcecaoParaSelecaoInexistente() {
        when(teamService.buscarSelecao(999L)).thenReturn(Optional.empty());

        jakarta.servlet.ServletException thrown =
                assertThrows(jakarta.servlet.ServletException.class,
                        () -> mockMvc.perform(get("/selecoes/999")));
        assertTrue(thrown.getCause() instanceof RuntimeException);
        assertEquals("Seleção não encontrada", thrown.getCause().getMessage());
    }
}
