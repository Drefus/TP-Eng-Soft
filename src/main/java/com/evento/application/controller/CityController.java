package com.evento.application.controller;

import com.evento.domain.entity.CidadeSede;
import com.evento.domain.entity.Partida;
import com.evento.domain.service.CityService;
import com.evento.infrastructure.repository.PartidaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/cidades")
public class CityController {

    private final CityService cityService;
    private final PartidaRepository partidaRepository;

    public CityController(CityService cityService, PartidaRepository partidaRepository) {
        this.cityService = cityService;
        this.partidaRepository = partidaRepository;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("cidades", cityService.listarCidades());
        return "cidades";
    }

    @GetMapping("/{id}")
    public String detalhe(@PathVariable Long id, Model model) {
        CidadeSede cidade = cityService.buscarCidade(id)
                .orElseThrow(() -> new RuntimeException("Cidade não encontrada"));

        List<Partida> partidas = partidaRepository.findByCidade(id);

        model.addAttribute("cidade", cidade);
        model.addAttribute("partidas", partidas);
        return "cidade-detalhe";
    }
}
