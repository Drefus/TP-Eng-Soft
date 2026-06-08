package com.evento.domain.service;

import com.evento.domain.entity.CidadeSede;
import com.evento.domain.entity.Estadio;
import com.evento.infrastructure.repository.CidadeSedeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CityService - Testes de Serviço")
class CityServiceTest {

    @Mock
    private CidadeSedeRepository cidadeSedeRepository;

    @InjectMocks
    private CityService cityService;

    private CidadeSede cidadeComEstadio;
    private CidadeSede cidadeSemEstadio;

    @BeforeEach
    void setUp() {
        Estadio estadio = new Estadio("Maracanã", 78000);

        cidadeComEstadio = new CidadeSede("Rio de Janeiro", "Brasil", "Cidade maravilhosa");
        cidadeComEstadio.setId(1L);
        cidadeComEstadio.setEstadio(estadio);

        cidadeSemEstadio = new CidadeSede("São Paulo", "Brasil", "Metrópole");
        cidadeSemEstadio.setId(2L);
    }

    @Test
    @DisplayName("Deve listar todas as cidades")
    void deveListarCidades() {
        when(cidadeSedeRepository.findAll()).thenReturn(List.of(cidadeComEstadio, cidadeSemEstadio));

        List<CidadeSede> resultado = cityService.listarCidades();

        assertEquals(2, resultado.size());
        verify(cidadeSedeRepository).findAll();
    }

    @Test
    @DisplayName("Deve buscar cidade por ID quando existe")
    void deveBuscarCidadeExistente() {
        when(cidadeSedeRepository.findById(1L)).thenReturn(Optional.of(cidadeComEstadio));

        Optional<CidadeSede> resultado = cityService.buscarCidade(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Rio de Janeiro", resultado.get().getNome());
    }

    @Test
    @DisplayName("Deve retornar Optional vazio para cidade inexistente")
    void deveRetornarVazioParaCidadeInexistente() {
        when(cidadeSedeRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<CidadeSede> resultado = cityService.buscarCidade(999L);

        assertFalse(resultado.isPresent());
    }

    @Test
    @DisplayName("Deve atualizar descrição da cidade quando fornecida")
    void deveAtualizarDescricao() {
        when(cidadeSedeRepository.findById(1L)).thenReturn(Optional.of(cidadeComEstadio));

        cityService.atualizarDetalhes(1L, "Nova descrição", null);

        assertEquals("Nova descrição", cidadeComEstadio.getDescricao());
        verify(cidadeSedeRepository).save(cidadeComEstadio);
    }

    @Test
    @DisplayName("Deve atualizar capacidade do estádio quando fornecida e positiva")
    void deveAtualizarCapacidadeEstadio() {
        when(cidadeSedeRepository.findById(1L)).thenReturn(Optional.of(cidadeComEstadio));

        cityService.atualizarDetalhes(1L, null, 90000);

        assertEquals(90000, cidadeComEstadio.getEstadio().getCapacidade());
        verify(cidadeSedeRepository).save(cidadeComEstadio);
    }

    @Test
    @DisplayName("Deve atualizar descrição e capacidade simultaneamente")
    void deveAtualizarDescricaoECapacidade() {
        when(cidadeSedeRepository.findById(1L)).thenReturn(Optional.of(cidadeComEstadio));

        cityService.atualizarDetalhes(1L, "Descrição atualizada", 95000);

        assertEquals("Descrição atualizada", cidadeComEstadio.getDescricao());
        assertEquals(95000, cidadeComEstadio.getEstadio().getCapacidade());
        verify(cidadeSedeRepository).save(cidadeComEstadio);
    }

    @Test
    @DisplayName("Não deve alterar descrição quando null é fornecido")
    void naoDeveAlterarDescricaoComNull() {
        when(cidadeSedeRepository.findById(1L)).thenReturn(Optional.of(cidadeComEstadio));
        String descricaoOriginal = cidadeComEstadio.getDescricao();

        cityService.atualizarDetalhes(1L, null, null);

        assertEquals(descricaoOriginal, cidadeComEstadio.getDescricao());
        verify(cidadeSedeRepository).save(cidadeComEstadio);
    }

    @Test
    @DisplayName("Não deve alterar capacidade quando valor zero ou negativo é fornecido")
    void naoDeveAtualizarCapacidadeComValorInvalido() {
        when(cidadeSedeRepository.findById(1L)).thenReturn(Optional.of(cidadeComEstadio));
        int capacidadeOriginal = cidadeComEstadio.getEstadio().getCapacidade();

        cityService.atualizarDetalhes(1L, null, 0);

        assertEquals(capacidadeOriginal, cidadeComEstadio.getEstadio().getCapacidade());
        verify(cidadeSedeRepository).save(cidadeComEstadio);
    }

    @Test
    @DisplayName("Não deve atualizar capacidade quando cidade não possui estádio")
    void naoDeveAtualizarCapacidadeSemEstadio() {
        when(cidadeSedeRepository.findById(2L)).thenReturn(Optional.of(cidadeSemEstadio));

        // cidade sem estadio — não deve lançar NullPointerException
        assertDoesNotThrow(() -> cityService.atualizarDetalhes(2L, "Desc", 50000));
        verify(cidadeSedeRepository).save(cidadeSemEstadio);
    }

    @Test
    @DisplayName("Não deve fazer nada quando cidade não existe")
    void naoDeveFazerNadaParaCidadeInexistente() {
        when(cidadeSedeRepository.findById(999L)).thenReturn(Optional.empty());

        cityService.atualizarDetalhes(999L, "Desc", 50000);

        verify(cidadeSedeRepository, never()).save(any());
    }
}
