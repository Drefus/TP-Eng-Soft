package com.evento.domain.service;

import com.evento.domain.entity.Usuario;
import com.evento.infrastructure.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class AuthService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

        String role = "ADMIN".equals(usuario.getRole()) ? "ROLE_ADMIN" : "ROLE_USER";

        return new User(
                usuario.getLogin(),
                usuario.getSenha(),
                Collections.singletonList(new SimpleGrantedAuthority(role))
        );
    }
}
