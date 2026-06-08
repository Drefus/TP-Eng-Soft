package com.evento.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Estadio - Testes de Entidade")
class EstadioTest {

    @Test
    @DisplayName("Deve criar estádio com construtor de 2 parâmetros")
    void deveCriarEstadioComConstrutor() {
        Estadio estadio = new Estadio("Maracanã", 78000);

        assertEquals("Maracanã", estadio.getNome());
        assertEquals(78000, estadio.getCapacidade());
    }

    @Test
    @DisplayName("Deve atualizar capacidade via setter")
    void deveAtualizarCapacidade() {
        Estadio estadio = new Estadio("MetLife", 82500);
        estadio.setCapacidade(85000);

        assertEquals(85000, estadio.getCapacidade());
    }

    @Test
    @DisplayName("Deve armazenar URL da imagem")
    void deveArmazenarImagemUrl() {
        Estadio estadio = new Estadio("Wembley", 90000);
        estadio.setImagemUrl("https://example.com/wembley.jpg");

        assertEquals("https://example.com/wembley.jpg", estadio.getImagemUrl());
    }
}
