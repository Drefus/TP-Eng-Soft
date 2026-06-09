package com.evento.application.controller.api;

import com.evento.domain.entity.*;
import com.evento.domain.service.CityService;
import com.evento.domain.service.GameService;
import com.evento.domain.service.TeamService;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApiStatsController.class)
@DisplayName("ApiStatsController - Testes de Controller REST")
class ApiStatsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GameService gameService;

    @MockitoBean
    private TeamService teamService;

    @MockitoBean
    private CityService cityService;

    @Test
    @WithMockUser
    @DisplayName("GET /api/stats deve retornar estatísticas gerais")
    void deveRetornarStats() throws Exception {
        Selecao brasil = new Selecao("Brasil", "A", "D", "🇧🇷", "BR");
        brasil.setId(1L);

        Partida p = new Partida(LocalDate.now(), LocalTime.NOON,
                brasil, brasil, "Grupo", "FINALIZADA",
                new Estadio("E", 1000), new CidadeSede("C", "P", "D"));
        p.setId(1L);
        p.setGolsTime1(1);
        p.setGolsTime2(0);

        when(gameService.listarPartidas()).thenReturn(List.of(p));
        when(gameService.buscarProximasPartidas()).thenReturn(List.of());
        when(teamService.listarSelecoes()).thenReturn(List.of(brasil));
        when(teamService.listarPorGrupos()).thenReturn(Map.of("A", List.of(brasil)));
        when(cityService.listarCidades()).thenReturn(List.of());

        mockMvc.perform(get("/api/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPartidas").value(1))
                .andExpect(jsonPath("$.partidasFinalizadas").value(1))
                .andExpect(jsonPath("$.totalSelecoes").value(1))
                .andExpect(jsonPath("$.totalCidades").value(0))
                .andExpect(jsonPath("$.proximasPartidas").isArray())
                .andExpect(jsonPath("$.grupos.A").isArray());
    }
}
