package com.evento.application.controller.api;

import com.evento.domain.service.AuthService;
import com.evento.infrastructure.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApiAuthController.class)
@Import(SecurityConfig.class)
@DisplayName("ApiAuthController - Testes de Controller REST")
class ApiAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("GET /api/auth/me autenticado como ADMIN deve retornar dados do usuário")
    void deveRetornarUsuarioAdmin() throws Exception {
        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticated").value(true))
                .andExpect(jsonPath("$.user").value("admin"))
                .andExpect(jsonPath("$.admin").value(true));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("GET /api/auth/me autenticado como USER deve retornar admin=false")
    void deveRetornarUsuarioComum() throws Exception {
        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticated").value(true))
                .andExpect(jsonPath("$.user").value("user"))
                .andExpect(jsonPath("$.admin").value(false));
    }

    @Test
    @DisplayName("GET /api/auth/me sem autenticação deve retornar authenticated=false")
    void deveRetornarNaoAutenticado() throws Exception {
        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticated").value(false));
    }
}
