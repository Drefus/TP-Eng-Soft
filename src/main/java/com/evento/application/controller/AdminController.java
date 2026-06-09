package com.evento.application.controller;

import com.evento.domain.service.CityService;
import com.evento.domain.service.GameService;
import com.evento.domain.service.TeamService;
import com.evento.infrastructure.api.WorldCupApiSyncService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final GameService gameService;
    private final TeamService teamService;
    private final CityService cityService;
    private final WorldCupApiSyncService syncService;

    public AdminController(GameService gameService, TeamService teamService,
                           CityService cityService, WorldCupApiSyncService syncService) {
        this.gameService = gameService;
        this.teamService = teamService;
        this.cityService = cityService;
        this.syncService = syncService;
    }

    @GetMapping("/painel")
    public ResponseEntity<Map<String, Object>> painel() {
        return ResponseEntity.ok(Map.of(
                "partidas", gameService.listarPartidas(),
                "selecoes", teamService.listarTodasSelecoes()
        ));
    }

    @PostMapping("/resultado")
    public ResponseEntity<Map<String, String>> atualizarResultado(
            @RequestBody Map<String, Object> body) {
        try {
            Long partidaId = Long.valueOf(body.get("partidaId").toString());
            int golsTime1 = Integer.parseInt(body.get("golsTime1").toString());
            int golsTime2 = Integer.parseInt(body.get("golsTime2").toString());

            gameService.atualizarResultado(partidaId, golsTime1, golsTime2);
            return ResponseEntity.ok(Map.of("status", "Resultado atualizado com sucesso!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/cidade/editar")
    public ResponseEntity<Map<String, String>> editarCidade(@RequestBody Map<String, Object> body) {
        try {
            Long cidadeId = Long.valueOf(body.get("cidadeId").toString());
            String descricao = body.get("descricaoCidade") != null ? body.get("descricaoCidade").toString() : null;
            Integer capacidade = body.get("capacidadeEstadio") != null
                    ? Integer.parseInt(body.get("capacidadeEstadio").toString()) : null;

            cityService.atualizarDetalhes(cidadeId, descricao, capacidade);
            return ResponseEntity.ok(Map.of("status", "Cidade atualizada com sucesso!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/sync")
    public ResponseEntity<Map<String, String>> sync() {
        syncService.syncMatches();
        return ResponseEntity.ok(Map.of("status", "Sincronização concluída"));
    }
}
