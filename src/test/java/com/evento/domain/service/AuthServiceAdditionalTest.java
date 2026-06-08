package com.evento.domain.service;

import com.evento.domain.entity.Usuario;
import com.evento.infrastructure.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService - Testes Adicionais")
class AuthServiceAdditionalTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("Deve atribuir ROLE_USER para usuário comum (role não é ADMIN)")
    void deveAtribuirRoleUserParaUsuarioComum() {
        Usuario usuario = new Usuario("João", "joao", "senha123");
        usuario.setRole("USUARIO");
        when(usuarioRepository.findByLogin("joao")).thenReturn(Optional.of(usuario));

        UserDetails userDetails = authService.loadUserByUsername("joao");

        assertEquals("joao", userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        assertFalse(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    @DisplayName("Deve atribuir ROLE_USER quando role é null")
    void deveAtribuirRoleUserQuandoRoleNull() {
        Usuario usuario = new Usuario("Maria", "maria", "senha456");
        // role é null (discriminator padrão)
        when(usuarioRepository.findByLogin("maria")).thenReturn(Optional.of(usuario));

        UserDetails userDetails = authService.loadUserByUsername("maria");

        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    @DisplayName("Deve retornar UserDetails com senha do banco")
    void deveRetornarUserDetailsComSenha() {
        Usuario usuario = new Usuario("Admin", "admin", "$2a$10$hashedpassword");
        usuario.setRole("ADMIN");
        when(usuarioRepository.findByLogin("admin")).thenReturn(Optional.of(usuario));

        UserDetails userDetails = authService.loadUserByUsername("admin");

        assertEquals("$2a$10$hashedpassword", userDetails.getPassword());
    }
}
