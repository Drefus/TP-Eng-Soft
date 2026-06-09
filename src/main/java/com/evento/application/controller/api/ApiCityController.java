package com.evento.application.controller.api;

import com.evento.domain.entity.CidadeSede;
import com.evento.domain.service.CityService;
import com.evento.infrastructure.repository.PartidaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cidades")
public class ApiCityController {

    private final CityService cityService;
    private final PartidaRepository partidaRepository;

    public ApiCityController(CityService cityService, PartidaRepository partidaRepository) {
        this.cityService = cityService;
        this.partidaRepository = partidaRepository;
    }

    @GetMapping
    public List<CidadeSede> listar() {
        return cityService.listarCidades();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> detalhe(@PathVariable Long id) {
        return cityService.buscarCidade(id)
                .map(cidade -> ResponseEntity.ok(Map.of(
                        "cidade", cidade,
                        "partidas", partidaRepository.findByCidade(id)
                )))
                .orElse(ResponseEntity.notFound().build());
    }
}
