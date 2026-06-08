package com.evento.application.controller;

import com.evento.domain.entity.CidadeSede;
import com.evento.domain.entity.Estadio;
import com.evento.domain.service.CityService;
import com.evento.infrastructure.repository.PartidaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CityController.class)
@DisplayName("CityController - Testes de Controller")
class CityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CityService cityService;

    @MockitoBean
    private PartidaRepository partidaRepository;

    private CidadeSede cidade;

    @BeforeEach
    void setUp() {
        Estadio estadio = new Estadio("Maracanã", 78000);
        cidade = new CidadeSede("Rio de Janeiro", "Brasil", "Cidade maravilhosa");
        cidade.setId(1L);
        cidade.setEstadio(estadio);
    }

    @Test
    @WithMockUser
    @DisplayName("GET /cidades deve listar todas as cidades")
    void deveListarCidades() throws Exception {
        when(cityService.listarCidades()).thenReturn(List.of(cidade));

        mockMvc.perform(get("/cidades"))
                .andExpect(status().isOk())
                .andExpect(view().name("cidades"))
                .andExpect(model().attributeExists("cidades"))
                .andExpect(model().attribute("cidades",
                        org.hamcrest.Matchers.hasSize(1)));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /cidades deve retornar lista vazia quando não há cidades")
    void deveListarCidadesVazias() throws Exception {
        when(cityService.listarCidades()).thenReturn(List.of());

        mockMvc.perform(get("/cidades"))
                .andExpect(status().isOk())
                .andExpect(view().name("cidades"))
                .andExpect(model().attribute("cidades",
                        org.hamcrest.Matchers.hasSize(0)));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /cidades/{id} deve retornar detalhe da cidade com partidas")
    void deveRetornarDetalheCidade() throws Exception {
        when(cityService.buscarCidade(1L)).thenReturn(Optional.of(cidade));
        when(partidaRepository.findByCidade(1L)).thenReturn(List.of());

        mockMvc.perform(get("/cidades/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("cidade-detalhe"))
                .andExpect(model().attributeExists("cidade", "partidas"))
                .andExpect(model().attribute("cidade", cidade));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /cidades/{id} deve lançar exceção para cidade inexistente")
    void deveLancarExcecaoParaCidadeInexistente() {
        when(cityService.buscarCidade(999L)).thenReturn(Optional.empty());

        jakarta.servlet.ServletException thrown =
                assertThrows(jakarta.servlet.ServletException.class,
                        () -> mockMvc.perform(get("/cidades/999")));
        assertTrue(thrown.getCause() instanceof RuntimeException);
        assertEquals("Cidade não encontrada", thrown.getCause().getMessage());
    }
}
