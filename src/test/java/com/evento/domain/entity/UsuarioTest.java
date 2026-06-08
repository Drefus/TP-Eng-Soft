package com.evento.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Usuario e Administrador - Testes de Entidade")
class UsuarioTest {

    @Test
    @DisplayName("Deve criar usuário com construtor de 3 parâmetros")
    void deveCriarUsuario() {
        Usuario usuario = new Usuario("João", "joao", "senha123");

        assertEquals("João", usuario.getNome());
        assertEquals("joao", usuario.getLogin());
        assertEquals("senha123", usuario.getSenha());
    }

    @Test
    @DisplayName("Deve criar administrador com herança correta")
    void deveCriarAdministrador() {
        Administrador admin = new Administrador("Admin", "admin", "admin123");

        assertEquals("Admin", admin.getNome());
        assertEquals("admin", admin.getLogin());
        assertEquals("admin123", admin.getSenha());
        assertInstanceOf(Usuario.class, admin);
    }

    @Test
    @DisplayName("Deve atualizar role via setter")
    void deveAtualizarRole() {
        Usuario usuario = new Usuario("Maria", "maria", "senha");
        usuario.setRole("ADMIN");

        assertEquals("ADMIN", usuario.getRole());
    }

    @Test
    @DisplayName("Deve ter role null por padrão ao criar usuário")
    void deveRoleNullPorPadrao() {
        Usuario usuario = new Usuario("Pedro", "pedro", "senha");

        assertNull(usuario.getRole());
    }

    @Test
    @DisplayName("Deve permitir alterar login e senha via setter")
    void deveAlterarLoginESenha() {
        Usuario usuario = new Usuario("Ana", "ana", "senhaAntiga");
        usuario.setLogin("ana_nova");
        usuario.setSenha("senhaNova");

        assertEquals("ana_nova", usuario.getLogin());
        assertEquals("senhaNova", usuario.getSenha());
    }
}
