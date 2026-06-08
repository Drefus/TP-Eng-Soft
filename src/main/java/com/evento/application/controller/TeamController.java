package com.evento.application.controller;

import com.evento.domain.entity.Partida;
import com.evento.domain.entity.Selecao;
import com.evento.domain.service.GameService;
import com.evento.domain.service.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/selecoes")
public class TeamController {

    private final TeamService teamService;
    private final GameService gameService;

    public TeamController(TeamService teamService, GameService gameService) {
        this.teamService = teamService;
        this.gameService = gameService;
    }

    @GetMapping
    public String listar(Model model) {
        Map<String, List<Selecao>> grupos = teamService.listarPorGrupos();
        model.addAttribute("grupos", grupos);
        model.addAttribute("selecoes", teamService.listarTodasSelecoes());
        return "selecoes";
    }

    @GetMapping("/{id}")
    public String detalhe(@PathVariable Long id, Model model) {
        Selecao selecao = teamService.buscarSelecao(id)
                .orElseThrow(() -> new RuntimeException("Seleção não encontrada"));

        List<Partida> partidas = gameService.buscarPorSelecao(id);

        model.addAttribute("selecao", selecao);
        model.addAttribute("partidas", partidas);
        return "selecao-detalhe";
    }
}
