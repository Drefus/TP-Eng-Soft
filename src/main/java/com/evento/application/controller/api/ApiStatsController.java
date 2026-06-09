package com.evento.application.controller.api;

import com.evento.domain.entity.Partida;
import com.evento.domain.service.CityService;
import com.evento.domain.service.GameService;
import com.evento.domain.service.TeamService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
public class ApiStatsController {

    private final GameService gameService;
    private final TeamService teamService;
    private final CityService cityService;

    public ApiStatsController(GameService gameService, TeamService teamService, CityService cityService) {
        this.gameService = gameService;
        this.teamService = teamService;
        this.cityService = cityService;
    }

    @GetMapping
    public Map<String, Object> stats() {
        List<Partida> todas = gameService.listarPartidas();
        long finalizadas = todas.stream().filter(Partida::isFinalizada).count();

        return Map.of(
                "totalPartidas", todas.size(),
                "partidasFinalizadas", finalizadas,
                "totalSelecoes", teamService.listarSelecoes().size(),
                "totalCidades", cityService.listarCidades().size(),
                "proximasPartidas", gameService.buscarProximasPartidas(),
                "grupos", teamService.listarPorGrupos()
        );
    }
}
