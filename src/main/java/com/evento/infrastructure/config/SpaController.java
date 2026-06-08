package com.evento.infrastructure.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Redireciona todas as rotas SPA para o index.html do React,
 * permitindo que o React Router gerencie a navegação client-side.
 */
@Controller
public class SpaController {

    @RequestMapping(value = {
        "/partidas", "/partidas/{id}",
        "/selecoes", "/selecoes/{id}",
        "/cidades",  "/cidades/{id}",
        "/chaveamento",
        "/login",
        "/admin",
    })
    public String spa() {
        return "forward:/index.html";
    }
}
