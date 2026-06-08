package com.evento.application.controller.api;

import com.evento.domain.entity.Partida;
import com.evento.domain.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/partidas")
public class ApiGameController {

    private final GameService gameService;

    public ApiGameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public List<Partida> listar(
            @RequestParam(required = false) LocalDate data,
            @RequestParam(required = false) Long selecaoId,
            @RequestParam(required = false) Long cidadeId) {

        if (data != null || selecaoId != null || cidadeId != null) {
            return gameService.filtrarPartidas(data, selecaoId, cidadeId);
        }
        return gameService.listarPartidas();
    }

    @GetMapping("/proximas")
    public List<Partida> proximas() {
        return gameService.buscarProximasPartidas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> detalhe(@PathVariable Long id) {
        return gameService.buscarPartida(id)
                .map(partida -> {
                    List<Partida> partidasTime1 = gameService.buscarPorSelecao(partida.getTime1().getId());
                    List<Partida> partidasTime2 = gameService.buscarPorSelecao(partida.getTime2().getId());
                    return ResponseEntity.ok(Map.of(
                            "partida", partida,
                            "partidasTime1", partidasTime1,
                            "partidasTime2", partidasTime2
                    ));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
