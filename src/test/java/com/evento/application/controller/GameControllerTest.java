package com.evento.application.controller;

import com.evento.domain.entity.*;
import com.evento.domain.service.CityService;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameController.class)
@DisplayName("GameController - Testes de Controller")
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GameService gameService;

    @MockitoBean
    private TeamService teamService;

    @MockitoBean
    private CityService cityService;

    private Partida partida;
    private Selecao brasil;
    private Selecao argentina;

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
    @WithMockUser
    @DisplayName("GET / deve retornar index com dados da home")
    void deveRetornarIndexComDados() throws Exception {
        when(gameService.buscarProximasPartidas()).thenReturn(List.of(partida));
        when(gameService.listarPartidas()).thenReturn(List.of(partida));
        when(teamService.listarPorGrupos()).thenReturn(Map.of("A", List.of(brasil)));
        when(teamService.listarSelecoes()).thenReturn(List.of(brasil));
        when(cityService.listarCidades()).thenReturn(List.of());

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("proximasPartidas", "grupos",
                        "totalPartidas", "partidasFinalizadas", "totalSelecoes", "totalCidades"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET / deve limitar próximas partidas a 4 quando houver mais de 4")
    void deveLimitarProximasPartidasA4() throws Exception {
        Partida p1 = criarPartida(1L);
        Partida p2 = criarPartida(2L);
        Partida p3 = criarPartida(3L);
        Partida p4 = criarPartida(4L);
        Partida p5 = criarPartida(5L);

        when(gameService.buscarProximasPartidas()).thenReturn(List.of(p1, p2, p3, p4, p5));
        when(gameService.listarPartidas()).thenReturn(List.of());
        when(teamService.listarPorGrupos()).thenReturn(Map.of());
        when(teamService.listarSelecoes()).thenReturn(List.of());
        when(cityService.listarCidades()).thenReturn(List.of());

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("proximasPartidas",
                        org.hamcrest.Matchers.hasSize(4)));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /partidas deve listar todas as partidas sem filtros")
    void deveListarPartidasSemFiltros() throws Exception {
        when(gameService.listarPartidas()).thenReturn(List.of(partida));
        when(teamService.listarTodasSelecoes()).thenReturn(List.of(brasil));
        when(cityService.listarCidades()).thenReturn(List.of());

        mockMvc.perform(get("/partidas"))
                .andExpect(status().isOk())
                .andExpect(view().name("partidas"))
                .andExpect(model().attributeExists("partidas", "selecoes", "cidades"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /partidas com filtro de seleção deve chamar filtrarPartidas")
    void deveListarPartidasComFiltroSelecao() throws Exception {
        when(gameService.filtrarPartidas(null, 1L, null)).thenReturn(List.of(partida));
        when(teamService.listarTodasSelecoes()).thenReturn(List.of(brasil));
        when(cityService.listarCidades()).thenReturn(List.of());

        mockMvc.perform(get("/partidas").param("selecaoId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("partidas"))
                .andExpect(model().attributeExists("partidas"));

        verify(gameService).filtrarPartidas(null, 1L, null);
        verify(gameService, never()).listarPartidas();
    }

    @Test
    @WithMockUser
    @DisplayName("GET /partidas com filtro de data deve chamar filtrarPartidas")
    void deveListarPartidasComFiltroData() throws Exception {
        LocalDate data = LocalDate.of(2026, 6, 14);
        when(gameService.filtrarPartidas(data, null, null)).thenReturn(List.of(partida));
        when(teamService.listarTodasSelecoes()).thenReturn(List.of());
        when(cityService.listarCidades()).thenReturn(List.of());

        mockMvc.perform(get("/partidas").param("data", "2026-06-14"))
                .andExpect(status().isOk())
                .andExpect(view().name("partidas"));

        verify(gameService).filtrarPartidas(data, null, null);
    }

    @Test
    @WithMockUser
    @DisplayName("GET /partidas/{id} deve retornar detalhe da partida")
    void deveRetornarDetalhePartida() throws Exception {
        when(gameService.buscarPartida(1L)).thenReturn(Optional.of(partida));
        when(gameService.buscarPorSelecao(1L)).thenReturn(List.of(partida));
        when(gameService.buscarPorSelecao(2L)).thenReturn(List.of());

        mockMvc.perform(get("/partidas/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("partida-detalhe"))
                .andExpect(model().attributeExists("partida", "partidasTime1", "partidasTime2"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /partidas/{id} deve lançar exceção para partida inexistente")
    void deveLancarExcecaoParaPartidaInexistente() {
        when(gameService.buscarPartida(999L)).thenReturn(Optional.empty());

        jakarta.servlet.ServletException thrown =
                assertThrows(jakarta.servlet.ServletException.class,
                        () -> mockMvc.perform(get("/partidas/999")));
        assertTrue(thrown.getCause() instanceof RuntimeException);
        assertEquals("Partida não encontrada", thrown.getCause().getMessage());
    }

    private Partida criarPartida(Long id) {
        Partida p = new Partida(
                LocalDate.of(2026, 6, 14), LocalTime.of(18, 0),
                brasil, argentina, "Grupo", "AGENDADA",
                new Estadio("MetLife", 82500), new CidadeSede("NY", "EUA", "Desc"));
        p.setId(id);
        return p;
    }
}
