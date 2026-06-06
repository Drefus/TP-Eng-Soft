package com.evento.application.controller;

import com.evento.domain.entity.Partida;
import com.evento.domain.entity.Selecao;
import com.evento.domain.entity.CidadeSede;
import com.evento.domain.service.GameService;
import com.evento.domain.service.TeamService;
import com.evento.domain.service.CityService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
public class GameController {

    private final GameService gameService;
    private final TeamService teamService;
    private final CityService cityService;

    public GameController(GameService gameService, TeamService teamService, CityService cityService) {
        this.gameService = gameService;
        this.teamService = teamService;
        this.cityService = cityService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Partida> proximasPartidas = gameService.buscarProximasPartidas();
        Map<String, List<Selecao>> grupos = teamService.listarPorGrupos();
        List<Partida> todasPartidas = gameService.listarPartidas();

        model.addAttribute("proximasPartidas", proximasPartidas.size() > 4 ? proximasPartidas.subList(0, 4) : proximasPartidas);
        model.addAttribute("grupos", grupos);
        model.addAttribute("totalPartidas", todasPartidas.size());
        model.addAttribute("partidasFinalizadas", todasPartidas.stream().filter(Partida::isFinalizada).count());
        model.addAttribute("totalSelecoes", teamService.listarSelecoes().size());
        model.addAttribute("totalCidades", cityService.listarCidades().size());
        return "index";
    }

    @GetMapping("/partidas")
    public String partidas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam(required = false) Long selecaoId,
            @RequestParam(required = false) Long cidadeId,
            Model model) {

        List<Partida> partidas;
        if (data != null || selecaoId != null || cidadeId != null) {
            partidas = gameService.filtrarPartidas(data, selecaoId, cidadeId);
        } else {
            partidas = gameService.listarPartidas();
        }

        model.addAttribute("partidas", partidas);
        model.addAttribute("selecoes", teamService.listarSelecoes());
        model.addAttribute("cidades", cityService.listarCidades());
        model.addAttribute("dataFiltro", data);
        model.addAttribute("selecaoFiltro", selecaoId);
        model.addAttribute("cidadeFiltro", cidadeId);
        return "partidas";
    }

    @GetMapping("/partidas/{id}")
    public String partidaDetalhe(@PathVariable Long id, Model model) {
        Partida partida = gameService.buscarPartida(id)
                .orElseThrow(() -> new RuntimeException("Partida não encontrada"));

        List<Partida> partidasTime1 = gameService.buscarPorSelecao(partida.getTime1().getId());
        List<Partida> partidasTime2 = gameService.buscarPorSelecao(partida.getTime2().getId());

        model.addAttribute("partida", partida);
        model.addAttribute("partidasTime1", partidasTime1);
        model.addAttribute("partidasTime2", partidasTime2);
        return "partida-detalhe";
    }
}
