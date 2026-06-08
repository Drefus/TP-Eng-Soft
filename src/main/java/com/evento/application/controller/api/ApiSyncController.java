package com.evento.application.controller.api;

import com.evento.infrastructure.api.WorldCupApiSyncService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class ApiSyncController {

    private final WorldCupApiSyncService syncService;

    public ApiSyncController(WorldCupApiSyncService syncService) {
        this.syncService = syncService;
    }

    @PostMapping("/sync")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> sync() {
        syncService.syncMatches();
        return ResponseEntity.ok(Map.of("status", "Sincronização concluída"));
    }
}
