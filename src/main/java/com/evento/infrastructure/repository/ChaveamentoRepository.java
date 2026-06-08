package com.evento.infrastructure.repository;

import com.evento.domain.entity.Chaveamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChaveamentoRepository extends JpaRepository<Chaveamento, Long> {
    List<Chaveamento> findByFaseOrderByOrdemAsc(String fase);
    List<Chaveamento> findAllByOrderByFaseAscOrdemAsc();
}
