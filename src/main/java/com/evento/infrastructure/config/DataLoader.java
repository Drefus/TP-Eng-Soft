package com.evento.infrastructure.config;

import com.evento.domain.entity.Administrador;
import com.evento.infrastructure.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initAdmin(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Update admin password with proper BCrypt hash
            usuarioRepository.findByLogin("admin").ifPresent(admin -> {
                admin.setSenha(passwordEncoder.encode("admin123"));
                usuarioRepository.save(admin);
            });
        };
    }
}
