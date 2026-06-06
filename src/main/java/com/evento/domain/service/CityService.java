package com.evento.domain.service;

import com.evento.domain.entity.CidadeSede;
import com.evento.infrastructure.repository.CidadeSedeRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CityService {

    private final CidadeSedeRepository cidadeSedeRepository;

    public CityService(CidadeSedeRepository cidadeSedeRepository) {
        this.cidadeSedeRepository = cidadeSedeRepository;
    }

    public List<CidadeSede> listarCidades() {
        return cidadeSedeRepository.findAll();
    }

    public Optional<CidadeSede> buscarCidade(Long id) {
        return cidadeSedeRepository.findById(id);
    }
}
