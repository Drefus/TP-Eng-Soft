package com.evento.application.controller.api;

import com.evento.domain.entity.Chaveamento;
import com.evento.domain.service.BracketService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chaveamento")
public class ApiBracketController {

    private final BracketService bracketService;

    public ApiBracketController(BracketService bracketService) {
        this.bracketService = bracketService;
    }

    @GetMapping
    public Map<String, List<Chaveamento>> listar() {
        return bracketService.listarPorFases();
    }
}
