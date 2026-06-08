package com.evento.infrastructure.repository;

import com.evento.domain.entity.Selecao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SelecaoRepository extends JpaRepository<Selecao, Long> {
    List<Selecao> findByGrupo(String grupo);
    java.util.Optional<Selecao> findByNome(String nome);

    @Query("SELECT s FROM Selecao s ORDER BY s.grupo ASC, s.pontos DESC, (s.golsPro - s.golsContra) DESC")
    List<Selecao> findAllOrderByGrupoAndPontos();
}
