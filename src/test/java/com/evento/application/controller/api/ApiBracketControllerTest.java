package com.evento.application.controller.api;

import com.evento.domain.entity.Chaveamento;
import com.evento.domain.service.BracketService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApiBracketController.class)
@DisplayName("ApiBracketController - Testes de Controller REST")
class ApiBracketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BracketService bracketService;

    @Test
    @WithMockUser
    @DisplayName("GET /api/chaveamento deve retornar mapa de fases")
    void deveRetornarChaveamento() throws Exception {
        Chaveamento quartas = new Chaveamento("Quartas", 1, null);
        quartas.setId(1L);

        Map<String, List<Chaveamento>> fases = new LinkedHashMap<>();
        fases.put("Quartas", List.of(quartas));

        when(bracketService.listarPorFases()).thenReturn(fases);

        mockMvc.perform(get("/api/chaveamento"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Quartas").isArray())
                .andExpect(jsonPath("$.Quartas[0].fase").value("Quartas"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/chaveamento vazio deve retornar mapa vazio")
    void deveRetornarVazio() throws Exception {
        when(bracketService.listarPorFases()).thenReturn(Map.of());

        mockMvc.perform(get("/api/chaveamento"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
