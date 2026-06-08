package com.evento.application.controller.api;

import com.evento.domain.entity.*;
import com.evento.domain.service.GameService;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApiGameController.class)
@DisplayName("ApiGameController - Testes de Controller REST")
class ApiGameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GameService gameService;

    private Partida partida;
    private Selecao brasil;
    private Selecao argentina;

    @BeforeEach
    void setUp() {
        brasil = new Selecao("Brasil", "A", "Dorival", "🇧🇷", "BR");
        brasil.setId(1L);
        argentina = new Selecao("Argentina", "A", "Scaloni", "🇦🇷", "AR");
        argentina.setId(2L);

        partida = new Partida(LocalDate.of(2026, 6, 14), LocalTime.of(18, 0),
                brasil, argentina, "Grupo", "AGENDADA",
                new Estadio("MetLife", 82500), new CidadeSede("NY", "EUA", "Desc"));
        partida.setId(1L);
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/partidas deve listar todas as partidas")
    void deveListarPartidas() throws Exception {
        when(gameService.listarPartidas()).thenReturn(List.of(partida));

        mockMvc.perform(get("/api/partidas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].fase").value("Grupo"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/partidas com filtro de selecaoId deve chamar filtrar")
    void deveFiltrarPorSelecao() throws Exception {
        when(gameService.filtrarPartidas(null, 1L, null)).thenReturn(List.of(partida));

        mockMvc.perform(get("/api/partidas").param("selecaoId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(gameService).filtrarPartidas(null, 1L, null);
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/partidas/proximas deve retornar partidas agendadas")
    void deveRetornarProximas() throws Exception {
        when(gameService.buscarProximasPartidas()).thenReturn(List.of(partida));

        mockMvc.perform(get("/api/partidas/proximas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("AGENDADA"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/partidas/{id} deve retornar detalhe da partida")
    void deveRetornarDetalhe() throws Exception {
        when(gameService.buscarPartida(1L)).thenReturn(Optional.of(partida));
        when(gameService.buscarPorSelecao(1L)).thenReturn(List.of(partida));
        when(gameService.buscarPorSelecao(2L)).thenReturn(List.of());

        mockMvc.perform(get("/api/partidas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.partida.id").value(1))
                .andExpect(jsonPath("$.partidasTime1").isArray())
                .andExpect(jsonPath("$.partidasTime2").isArray());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/partidas/{id} inexistente deve retornar 404")
    void deveRetornar404ParaPartidaInexistente() throws Exception {
        when(gameService.buscarPartida(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/partidas/999"))
                .andExpect(status().isNotFound());
    }
}
