package com.evento.application.controller;

import com.evento.domain.entity.Partida;
import com.evento.domain.service.GameService;
import com.evento.domain.service.TeamService;
import com.evento.domain.service.CityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminController {

    private final GameService gameService;
    private final TeamService teamService;
    private final CityService cityService;

    public AdminController(GameService gameService, TeamService teamService, CityService cityService) {
        this.gameService = gameService;
        this.teamService = teamService;
        this.cityService = cityService;
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) Boolean error, Model model) {
        if (Boolean.TRUE.equals(error)) {
            model.addAttribute("errorMsg", "Login ou senha inválidos!");
        }
        return "login";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("partidas", gameService.listarPartidas());
        model.addAttribute("selecoes", teamService.listarSelecoes());
        return "admin";
    }

    @PostMapping("/admin/resultado")
    public String atualizarResultado(
            @RequestParam Long partidaId,
            @RequestParam int golsTime1,
            @RequestParam int golsTime2,
            RedirectAttributes redirectAttributes) {
        try {
            gameService.atualizarResultado(partidaId, golsTime1, golsTime2);
            redirectAttributes.addFlashAttribute("successMsg", "Resultado atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Erro ao atualizar: " + e.getMessage());
        }
        return "redirect:/admin";
    }
}
