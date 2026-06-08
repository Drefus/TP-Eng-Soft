package com.evento.infrastructure.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Redireciona todas as rotas desconhecidas para o index.html do React,
 * permitindo que o React Router gerencie a navegação client-side.
 *
 * Rotas excluídas: /api/**, /admin/**, /login, /logout, /css/**, /js/**,
 * /assets/** (tratadas por outros controllers ou como recursos estáticos).
 */
@Controller
public class SpaController {

    @RequestMapping(value = {
        "/partidas", "/partidas/**",
        "/selecoes", "/selecoes/**",
        "/cidades",  "/cidades/**",
        "/chaveamento",
    })
    public String spa() {
        return "forward:/index.html";
    }
}
