package com.evento.application.controller.api;

import com.evento.domain.entity.CidadeSede;
import com.evento.domain.entity.Estadio;
import com.evento.domain.service.CityService;
import com.evento.infrastructure.repository.PartidaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApiCityController.class)
@DisplayName("ApiCityController - Testes de Controller REST")
class ApiCityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CityService cityService;

    @MockitoBean
    private PartidaRepository partidaRepository;

    @Test
    @WithMockUser
    @DisplayName("GET /api/cidades deve listar cidades")
    void deveListarCidades() throws Exception {
        CidadeSede cidade = new CidadeSede("Rio", "Brasil", "Desc");
        cidade.setId(1L);
        cidade.setEstadio(new Estadio("Maracanã", 78000));

        when(cityService.listarCidades()).thenReturn(List.of(cidade));

        mockMvc.perform(get("/api/cidades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Rio"))
                .andExpect(jsonPath("$[0].estadio.nome").value("Maracanã"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/cidades/{id} deve retornar detalhe")
    void deveRetornarDetalheCidade() throws Exception {
        CidadeSede cidade = new CidadeSede("NY", "EUA", "Desc");
        cidade.setId(1L);

        when(cityService.buscarCidade(1L)).thenReturn(Optional.of(cidade));
        when(partidaRepository.findByCidade(1L)).thenReturn(List.of());

        mockMvc.perform(get("/api/cidades/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cidade.nome").value("NY"))
                .andExpect(jsonPath("$.partidas").isArray());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/cidades/{id} inexistente deve retornar 404")
    void deveRetornar404() throws Exception {
        when(cityService.buscarCidade(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/cidades/999"))
                .andExpect(status().isNotFound());
    }
}
