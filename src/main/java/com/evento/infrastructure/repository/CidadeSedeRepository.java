package com.evento.infrastructure.repository;

import com.evento.domain.entity.CidadeSede;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CidadeSedeRepository extends JpaRepository<CidadeSede, Long> {
}
