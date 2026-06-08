package com.evento.application.controller.api;

import com.evento.domain.entity.Selecao;
import com.evento.domain.service.GameService;
import com.evento.domain.service.TeamService;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApiTeamController.class)
@DisplayName("ApiTeamController - Testes de Controller REST")
class ApiTeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TeamService teamService;

    @MockitoBean
    private GameService gameService;

    @Test
    @WithMockUser
    @DisplayName("GET /api/selecoes deve retornar grupos e todas as seleções")
    void deveListarSelecoes() throws Exception {
        Selecao brasil = new Selecao("Brasil", "A", "Dorival", "🇧🇷", "BR");
        brasil.setId(1L);

        when(teamService.listarPorGrupos()).thenReturn(Map.of("A", List.of(brasil)));
        when(teamService.listarTodasSelecoes()).thenReturn(List.of(brasil));

        mockMvc.perform(get("/api/selecoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grupos.A").isArray())
                .andExpect(jsonPath("$.todas[0].nome").value("Brasil"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/selecoes/{id} deve retornar detalhe da seleção")
    void deveRetornarDetalheSelecao() throws Exception {
        Selecao brasil = new Selecao("Brasil", "A", "Dorival", "🇧🇷", "BR");
        brasil.setId(1L);

        when(teamService.buscarSelecao(1L)).thenReturn(Optional.of(brasil));
        when(gameService.buscarPorSelecao(1L)).thenReturn(List.of());

        mockMvc.perform(get("/api/selecoes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.selecao.nome").value("Brasil"))
                .andExpect(jsonPath("$.selecao.bandeira").value("🇧🇷"))
                .andExpect(jsonPath("$.partidas").isArray());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/selecoes/{id} inexistente deve retornar 404")
    void deveRetornar404ParaSelecaoInexistente() throws Exception {
        when(teamService.buscarSelecao(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/selecoes/999"))
                .andExpect(status().isNotFound());
    }
}
