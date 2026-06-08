package com.evento.application.controller;

import com.evento.domain.entity.Partida;
import com.evento.domain.service.GameService;
import com.evento.domain.service.TeamService;
import com.evento.domain.service.CityService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@DisplayName("AdminController - Testes de Controller")
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GameService gameService;

    @MockitoBean
    private TeamService teamService;

    @MockitoBean
    private CityService cityService;

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /admin deve retornar painel admin para usuário ADMIN")
    void deveRetornarPainelAdmin() throws Exception {
        when(gameService.listarPartidas()).thenReturn(List.of());
        when(teamService.listarSelecoes()).thenReturn(List.of());

        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin"))
                .andExpect(model().attributeExists("partidas", "selecoes"));
    }

    @Test
    @DisplayName("GET /admin sem autenticação deve retornar 401")
    void deveNegarAcessoSemAutenticacao() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /admin/resultado deve atualizar e redirecionar com sucesso")
    void deveAtualizarResultadoComSucesso() throws Exception {
        Partida p = new Partida();
        p.setId(1L);
        when(gameService.atualizarResultado(1L, 2, 1)).thenReturn(p);

        mockMvc.perform(post("/admin/resultado")
                .with(csrf())
                .param("partidaId", "1")
                .param("golsTime1", "2")
                .param("golsTime2", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"))
                .andExpect(flash().attributeExists("successMsg"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /admin/resultado com erro deve redirecionar com mensagem de erro")
    void deveRedirecionarComErroAoFalharAtualizacao() throws Exception {
        when(gameService.atualizarResultado(anyLong(), anyInt(), anyInt()))
                .thenThrow(new RuntimeException("Partida não encontrada"));

        mockMvc.perform(post("/admin/resultado")
                .with(csrf())
                .param("partidaId", "999")
                .param("golsTime1", "0")
                .param("golsTime2", "0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"))
                .andExpect(flash().attributeExists("errorMsg"));
    }
}
