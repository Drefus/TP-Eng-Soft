package com.evento.application.controller.api;

import com.evento.domain.entity.Selecao;
import com.evento.domain.service.GameService;
import com.evento.domain.service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/selecoes")
public class ApiTeamController {

    private final TeamService teamService;
    private final GameService gameService;

    public ApiTeamController(TeamService teamService, GameService gameService) {
        this.teamService = teamService;
        this.gameService = gameService;
    }

    @GetMapping
    public Map<String, Object> listar() {
        return Map.of(
                "grupos", teamService.listarPorGrupos(),
                "todas", teamService.listarTodasSelecoes()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> detalhe(@PathVariable Long id) {
        return teamService.buscarSelecao(id)
                .map(selecao -> ResponseEntity.ok(Map.of(
                        "selecao", selecao,
                        "partidas", gameService.buscarPorSelecao(id)
                )))
                .orElse(ResponseEntity.notFound().build());
    }
}
