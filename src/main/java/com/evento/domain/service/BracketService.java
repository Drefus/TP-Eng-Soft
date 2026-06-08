package com.evento.domain.service;

import com.evento.domain.entity.Chaveamento;
import com.evento.infrastructure.repository.ChaveamentoRepository;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BracketService {

    private final ChaveamentoRepository chaveamentoRepository;

    public BracketService(ChaveamentoRepository chaveamentoRepository) {
        this.chaveamentoRepository = chaveamentoRepository;
    }

    public List<Chaveamento> listarTodos() {
        return chaveamentoRepository.findAllByOrderByFaseAscOrdemAsc();
    }

    public List<Chaveamento> buscarPorFase(String fase) {
        return chaveamentoRepository.findByFaseOrderByOrdemAsc(fase);
    }

    public Map<String, List<Chaveamento>> listarPorFases() {
        List<Chaveamento> todos = chaveamentoRepository.findAllByOrderByFaseAscOrdemAsc();
        // Use LinkedHashMap to maintain insertion order
        Map<String, List<Chaveamento>> mapa = new LinkedHashMap<>();
        List<String> ordemFases = Arrays.asList("Quartas", "Semifinal", "Final");

        for (String fase : ordemFases) {
            List<Chaveamento> daFase = todos.stream()
                    .filter(c -> c.getFase().equals(fase))
                    .collect(Collectors.toList());
            if (!daFase.isEmpty()) {
                mapa.put(fase, daFase);
            }
        }

        return mapa;
    }
}
