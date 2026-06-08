package com.evento.application.controller;

import com.evento.domain.entity.Chaveamento;
import com.evento.domain.service.BracketService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/chaveamento")
public class BracketController {

    private final BracketService bracketService;

    public BracketController(BracketService bracketService) {
        this.bracketService = bracketService;
    }

    @GetMapping
    public String chaveamento(Model model) {
        Map<String, List<Chaveamento>> fases = bracketService.listarPorFases();
        model.addAttribute("fases", fases);
        return "chaveamento";
    }
}
