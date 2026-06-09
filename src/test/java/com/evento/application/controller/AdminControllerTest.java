package com.evento.application.controller;

import com.evento.domain.entity.Partida;
import com.evento.domain.entity.Selecao;
import com.evento.domain.service.AuthService;
import com.evento.domain.service.CityService;
import com.evento.domain.service.GameService;
import com.evento.domain.service.TeamService;
import com.evento.infrastructure.api.WorldCupApiSyncService;
import com.evento.infrastructure.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@Import(SecurityConfig.class)
@DisplayName("AdminController REST - Testes de Controller")
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GameService gameService;

    @MockitoBean
    private TeamService teamService;

    @MockitoBean
    private CityService cityService;

    @MockitoBean
    private WorldCupApiSyncService syncService;

    @MockitoBean
    private AuthService authService;

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/admin/painel deve retornar partidas e seleções")
    void deveRetornarPainel() throws Exception {
        when(gameService.listarPartidas()).thenReturn(List.of());
        when(teamService.listarTodasSelecoes()).thenReturn(List.of());

        mockMvc.perform(get("/api/admin/painel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.partidas").isArray())
                .andExpect(jsonPath("$.selecoes").isArray());
    }

    @Test
    @DisplayName("GET /api/admin/painel sem autenticação deve retornar 401")
    void deveNegarPainelSemAuth() throws Exception {
        mockMvc.perform(get("/api/admin/painel"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("GET /api/admin/painel com role USER deve retornar 403")
    void deveNegarPainelComRoleUser() throws Exception {
        mockMvc.perform(get("/api/admin/painel"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/admin/resultado deve atualizar resultado com sucesso")
    void deveAtualizarResultado() throws Exception {
        Partida p = new Partida();
        p.setId(1L);
        when(gameService.atualizarResultado(1L, 2, 1)).thenReturn(p);

        mockMvc.perform(post("/api/admin/resultado")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"partidaId\":1,\"golsTime1\":2,\"golsTime2\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Resultado atualizado com sucesso!"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/admin/resultado com erro deve retornar 400")
    void deveRetornarErroAoFalharResultado() throws Exception {
        when(gameService.atualizarResultado(anyLong(), anyInt(), anyInt()))
                .thenThrow(new RuntimeException("Partida não encontrada"));

        mockMvc.perform(post("/api/admin/resultado")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"partidaId\":999,\"golsTime1\":0,\"golsTime2\":0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Partida não encontrada"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/admin/cidade/editar deve atualizar cidade")
    void deveEditarCidade() throws Exception {
        doNothing().when(cityService).atualizarDetalhes(1L, "Nova desc", 90000);

        mockMvc.perform(post("/api/admin/cidade/editar")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"cidadeId\":1,\"descricaoCidade\":\"Nova desc\",\"capacidadeEstadio\":90000}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Cidade atualizada com sucesso!"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /api/admin/sync deve executar sincronização")
    void deveExecutarSync() throws Exception {
        doNothing().when(syncService).syncMatches();

        mockMvc.perform(post("/api/admin/sync"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Sincronização concluída"));

        verify(syncService).syncMatches();
    }

    @Test
    @DisplayName("POST /api/admin/resultado sem auth deve retornar 401")
    void deveNegarResultadoSemAuth() throws Exception {
        mockMvc.perform(post("/api/admin/resultado")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"partidaId\":1,\"golsTime1\":0,\"golsTime2\":0}"))
                .andExpect(status().isUnauthorized());
    }
}
