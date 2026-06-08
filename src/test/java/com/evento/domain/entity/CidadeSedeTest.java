package com.evento.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CidadeSede - Testes de Entidade")
class CidadeSedeTest {

    @Test
    @DisplayName("Deve criar cidade com construtor de 3 parâmetros")
    void deveCriarCidadeComConstrutor() {
        CidadeSede cidade = new CidadeSede("Rio de Janeiro", "Brasil", "Cidade maravilhosa");

        assertEquals("Rio de Janeiro", cidade.getNome());
        assertEquals("Brasil", cidade.getPais());
        assertEquals("Cidade maravilhosa", cidade.getDescricao());
        assertNotNull(cidade.getHoteis());
        assertNotNull(cidade.getAeroportos());
        assertTrue(cidade.getHoteis().isEmpty());
        assertTrue(cidade.getAeroportos().isEmpty());
    }

    @Test
    @DisplayName("Deve associar estádio à cidade")
    void deveAssociarEstadio() {
        CidadeSede cidade = new CidadeSede("São Paulo", "Brasil", "Desc");
        Estadio estadio = new Estadio("Morumbi", 66000);

        cidade.setEstadio(estadio);

        assertNotNull(cidade.getEstadio());
        assertEquals("Morumbi", cidade.getEstadio().getNome());
        assertEquals(66000, cidade.getEstadio().getCapacidade());
    }

    @Test
    @DisplayName("Deve aceitar null para estádio")
    void deveAceitarEstadioNull() {
        CidadeSede cidade = new CidadeSede("NY", "EUA", "Desc");

        assertNull(cidade.getEstadio());
    }

    @Test
    @DisplayName("Deve atualizar descrição via setter")
    void deveAtualizarDescricao() {
        CidadeSede cidade = new CidadeSede("Buenos Aires", "Argentina", "Capital");
        cidade.setDescricao("Nova descrição");

        assertEquals("Nova descrição", cidade.getDescricao());
    }
}
