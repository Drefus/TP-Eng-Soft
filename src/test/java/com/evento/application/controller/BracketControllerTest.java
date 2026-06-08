package com.evento.application.controller;

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

@WebMvcTest(BracketController.class)
@DisplayName("BracketController - Testes de Controller")
class BracketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BracketService bracketService;

    @Test
    @WithMockUser
    @DisplayName("GET /chaveamento deve retornar view com fases agrupadas")
    void deveRetornarChaveamento() throws Exception {
        Chaveamento quartas = new Chaveamento("Quartas", 1, null);
        Chaveamento semi = new Chaveamento("Semifinal", 1, null);
        Chaveamento finalC = new Chaveamento("Final", 1, null);

        Map<String, List<Chaveamento>> fases = new LinkedHashMap<>();
        fases.put("Quartas", List.of(quartas));
        fases.put("Semifinal", List.of(semi));
        fases.put("Final", List.of(finalC));

        when(bracketService.listarPorFases()).thenReturn(fases);

        mockMvc.perform(get("/chaveamento"))
                .andExpect(status().isOk())
                .andExpect(view().name("chaveamento"))
                .andExpect(model().attributeExists("fases"));

        verify(bracketService).listarPorFases();
    }

    @Test
    @WithMockUser
    @DisplayName("GET /chaveamento deve retornar mapa vazio quando sem chaveamentos")
    void deveRetornarChaveamentoVazio() throws Exception {
        when(bracketService.listarPorFases()).thenReturn(Map.of());

        mockMvc.perform(get("/chaveamento"))
                .andExpect(status().isOk())
                .andExpect(view().name("chaveamento"))
                .andExpect(model().attributeExists("fases"));
    }
}
