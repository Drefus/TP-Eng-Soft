package com.evento.domain.service;

import com.evento.domain.entity.Partida;
import com.evento.domain.entity.Selecao;
import com.evento.infrastructure.repository.PartidaRepository;
import com.evento.infrastructure.repository.SelecaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    private final PartidaRepository partidaRepository;
    private final SelecaoRepository selecaoRepository;

    public GameService(PartidaRepository partidaRepository, SelecaoRepository selecaoRepository) {
        this.partidaRepository = partidaRepository;
        this.selecaoRepository = selecaoRepository;
    }

    public List<Partida> listarPartidas() {
        return partidaRepository.findAllByOrderByDataAscHorarioAsc();
    }

    public Optional<Partida> buscarPartida(Long id) {
        return partidaRepository.findById(id);
    }

    public List<Partida> filtrarPartidas(LocalDate data, Long selecaoId, Long cidadeId) {
        return partidaRepository.filtrar(data, selecaoId, cidadeId);
    }

    public List<Partida> buscarPorFase(String fase) {
        return partidaRepository.findByFaseOrderByDataAsc(fase);
    }

    public List<Partida> buscarPorSelecao(Long selecaoId) {
        return partidaRepository.findBySelecao(selecaoId);
    }

    public List<Partida> buscarProximasPartidas() {
        return partidaRepository.findByStatusOrderByDataAsc("AGENDADA");
    }

    @Transactional
    public Partida atualizarResultado(Long partidaId, int golsTime1, int golsTime2) {
        Partida partida = partidaRepository.findById(partidaId)
                .orElseThrow(() -> new RuntimeException("Partida não encontrada"));

        // Reverter estatísticas anteriores se já tinha resultado
        if (partida.isFinalizada()) {
            reverterEstatisticas(partida);
        }

        partida.setGolsTime1(golsTime1);
        partida.setGolsTime2(golsTime2);
        partida.setStatus("FINALIZADA");

        // Atualizar estatísticas das seleções
        atualizarEstatisticas(partida);

        return partidaRepository.save(partida);
    }

    private void atualizarEstatisticas(Partida partida) {
        Selecao time1 = partida.getTime1();
        Selecao time2 = partida.getTime2();
        int g1 = partida.getGolsTime1();
        int g2 = partida.getGolsTime2();

        // Gols
        time1.setGolsPro(time1.getGolsPro() + g1);
        time1.setGolsContra(time1.getGolsContra() + g2);
        time2.setGolsPro(time2.getGolsPro() + g2);
        time2.setGolsContra(time2.getGolsContra() + g1);

        if (g1 > g2) {
            // Time 1 venceu
            time1.setVitorias(time1.getVitorias() + 1);
            time1.setPontos(time1.getPontos() + 3);
            time2.setDerrotas(time2.getDerrotas() + 1);
        } else if (g2 > g1) {
            // Time 2 venceu
            time2.setVitorias(time2.getVitorias() + 1);
            time2.setPontos(time2.getPontos() + 3);
            time1.setDerrotas(time1.getDerrotas() + 1);
        } else {
            // Empate
            time1.setEmpates(time1.getEmpates() + 1);
            time1.setPontos(time1.getPontos() + 1);
            time2.setEmpates(time2.getEmpates() + 1);
            time2.setPontos(time2.getPontos() + 1);
        }

        selecaoRepository.save(time1);
        selecaoRepository.save(time2);
    }

    private void reverterEstatisticas(Partida partida) {
        Selecao time1 = partida.getTime1();
        Selecao time2 = partida.getTime2();
        int g1 = partida.getGolsTime1();
        int g2 = partida.getGolsTime2();

        time1.setGolsPro(time1.getGolsPro() - g1);
        time1.setGolsContra(time1.getGolsContra() - g2);
        time2.setGolsPro(time2.getGolsPro() - g2);
        time2.setGolsContra(time2.getGolsContra() - g1);

        if (g1 > g2) {
            time1.setVitorias(time1.getVitorias() - 1);
            time1.setPontos(time1.getPontos() - 3);
            time2.setDerrotas(time2.getDerrotas() - 1);
        } else if (g2 > g1) {
            time2.setVitorias(time2.getVitorias() - 1);
            time2.setPontos(time2.getPontos() - 3);
            time1.setDerrotas(time1.getDerrotas() - 1);
        } else {
            time1.setEmpates(time1.getEmpates() - 1);
            time1.setPontos(time1.getPontos() - 1);
            time2.setEmpates(time2.getEmpates() - 1);
            time2.setPontos(time2.getPontos() - 1);
        }

        selecaoRepository.save(time1);
        selecaoRepository.save(time2);
    }
}
