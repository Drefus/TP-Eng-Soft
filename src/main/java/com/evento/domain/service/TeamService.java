package com.evento.domain.service;

import com.evento.domain.entity.Selecao;
import com.evento.infrastructure.repository.SelecaoRepository;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TeamService {

    private final SelecaoRepository selecaoRepository;

    public TeamService(SelecaoRepository selecaoRepository) {
        this.selecaoRepository = selecaoRepository;
    }

    public List<Selecao> listarSelecoes() {
        return selecaoRepository.findAll().stream()
                .filter(s -> s.getGrupo() != null && !s.getGrupo().equals("??") && !s.getGrupo().equals("?"))
                .collect(Collectors.toList());
    }

    public Optional<Selecao> buscarSelecao(Long id) {
        return selecaoRepository.findById(id);
    }

    public List<Selecao> listarPorGrupo(String grupo) {
        return selecaoRepository.findByGrupo(grupo);
    }

    public Map<String, List<Selecao>> listarPorGrupos() {
        List<Selecao> todas = selecaoRepository.findAll();
        return todas.stream()
                .filter(s -> s.getGrupo() != null && !s.getGrupo().equals("??") && !s.getGrupo().equals("?"))
                .sorted(Comparator.comparingInt(Selecao::getPontos).reversed()
                        .thenComparingInt(Selecao::getSaldoGols).reversed()
                        .thenComparingInt(Selecao::getGolsPro).reversed())
                .collect(Collectors.groupingBy(
                        Selecao::getGrupo,
                        TreeMap::new,
                        Collectors.toList()
                ));
    }

    public List<String> listarGrupos() {
        return selecaoRepository.findAll().stream()
                .map(Selecao::getGrupo)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}
