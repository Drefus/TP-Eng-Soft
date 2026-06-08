package com.evento.domain.service;

import com.evento.domain.entity.Administrador;
import com.evento.infrastructure.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService - Testes de Serviço")
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("Deve atribuir ROLE_ADMIN para administrador")
    void deveAtribuirRoleAdmin() {
        Administrador admin = new Administrador("Admin", "admin", "admin123");
        admin.setRole("ADMIN");
        when(usuarioRepository.findByLogin("admin")).thenReturn(Optional.of(admin));

        UserDetails userDetails = authService.loadUserByUsername("admin");

        assertEquals("admin", userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    @DisplayName("Deve lançar UsernameNotFoundException para login inexistente")
    void deveLancarExcecaoLoginInexistente() {
        when(usuarioRepository.findByLogin("naoexiste")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> authService.loadUserByUsername("naoexiste"));
    }
}
