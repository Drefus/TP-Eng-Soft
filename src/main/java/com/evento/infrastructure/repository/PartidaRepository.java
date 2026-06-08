package com.evento.infrastructure.repository;

import com.evento.domain.entity.Partida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, Long> {

    List<Partida> findAllByOrderByDataAscHorarioAsc();

    java.util.Optional<Partida> findByApiId(Long apiId);

    List<Partida> findByFaseOrderByDataAsc(String fase);

    @Query("SELECT p FROM Partida p WHERE p.data = :data ORDER BY p.horario ASC")
    List<Partida> findByData(@Param("data") LocalDate data);

    @Query("SELECT p FROM Partida p WHERE p.time1.id = :selecaoId OR p.time2.id = :selecaoId ORDER BY p.data ASC")
    List<Partida> findBySelecao(@Param("selecaoId") Long selecaoId);

    @Query("SELECT p FROM Partida p WHERE p.cidade.id = :cidadeId ORDER BY p.data ASC")
    List<Partida> findByCidade(@Param("cidadeId") Long cidadeId);

    @Query("SELECT p FROM Partida p WHERE " +
           "(:data IS NULL OR p.data = :data) AND " +
           "(:selecaoId IS NULL OR p.time1.id = :selecaoId OR p.time2.id = :selecaoId) AND " +
           "(:cidadeId IS NULL OR p.cidade.id = :cidadeId) " +
           "ORDER BY p.data ASC, p.horario ASC")
    List<Partida> filtrar(@Param("data") LocalDate data,
                          @Param("selecaoId") Long selecaoId,
                          @Param("cidadeId") Long cidadeId);

    List<Partida> findByStatusOrderByDataAsc(String status);
}
