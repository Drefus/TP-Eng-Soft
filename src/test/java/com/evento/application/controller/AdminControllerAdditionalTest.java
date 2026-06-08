package com.evento.application.controller;

import com.evento.domain.service.CityService;
import com.evento.domain.service.GameService;
import com.evento.domain.service.TeamService;
import com.evento.infrastructure.config.SecurityConfig;
import com.evento.infrastructure.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@Import(SecurityConfig.class)
@DisplayName("AdminController - Testes Adicionais")
class AdminControllerAdditionalTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GameService gameService;

    @MockitoBean
    private TeamService teamService;

    @MockitoBean
    private CityService cityService;

    // Needed because SecurityConfig imports DataLoader which requires UsuarioRepository
    @MockitoBean
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("GET /login deve retornar página de login sem mensagem de erro")
    void deveRetornarPaginaLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeDoesNotExist("errorMsg"));
    }

    @Test
    @DisplayName("GET /login?error=true deve retornar mensagem de erro")
    void deveRetornarMensagemDeErroPorLoginInvalido() throws Exception {
        mockMvc.perform(get("/login").param("error", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("errorMsg"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("GET /admin com role USER deve ser negado (403)")
    void deveNegarAcessoParaRoleUser() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /admin/cidade/editar deve atualizar e redirecionar para a cidade")
    void deveEditarCidadeComSucesso() throws Exception {
        doNothing().when(cityService).atualizarDetalhes(1L, "Nova descrição", 90000);

        mockMvc.perform(post("/admin/cidade/editar")
                .with(csrf())
                .param("cidadeId", "1")
                .param("descricaoCidade", "Nova descrição")
                .param("capacidadeEstadio", "90000"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cidades/1"))
                .andExpect(flash().attributeExists("successMsg"));

        verify(cityService).atualizarDetalhes(1L, "Nova descrição", 90000);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /admin/cidade/editar com erro deve redirecionar com mensagem de erro")
    void deveRedirecionarComErroAoFalharEdicaoCidade() throws Exception {
        doThrow(new RuntimeException("Cidade não encontrada"))
                .when(cityService).atualizarDetalhes(anyLong(), any(), any());

        mockMvc.perform(post("/admin/cidade/editar")
                .with(csrf())
                .param("cidadeId", "999")
                .param("descricaoCidade", "Desc"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cidades/999"))
                .andExpect(flash().attributeExists("errorMsg"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /admin/cidade/editar com parâmetros opcionais nulos deve funcionar")
    void deveEditarCidadeSemParametrosOpcionais() throws Exception {
        mockMvc.perform(post("/admin/cidade/editar")
                .with(csrf())
                .param("cidadeId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cidades/1"))
                .andExpect(flash().attributeExists("successMsg"));

        verify(cityService).atualizarDetalhes(1L, null, null);
    }
}
