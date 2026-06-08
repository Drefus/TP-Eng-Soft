package com.evento.infrastructure.api;

import com.evento.domain.entity.CidadeSede;
import com.evento.domain.entity.Partida;
import com.evento.domain.entity.Selecao;
import com.evento.domain.service.GameService;
import com.evento.infrastructure.api.dto.WorldCupApiResponse;
import com.evento.infrastructure.api.dto.WorldCupGameDto;
import com.evento.infrastructure.repository.CidadeSedeRepository;
import com.evento.infrastructure.repository.PartidaRepository;
import com.evento.infrastructure.repository.SelecaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class WorldCupApiSyncService {

    private static final Logger log = LoggerFactory.getLogger(WorldCupApiSyncService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");

    private final RestTemplate restTemplate;
    private final PartidaRepository partidaRepository;
    private final SelecaoRepository selecaoRepository;
    private final GameService gameService;
    private final CidadeSedeRepository cidadeSedeRepository;
    
    private List<CidadeSede> todasAsCidades;

    @Value("${api.worldcup.url}")
    private String apiUrl;

    public WorldCupApiSyncService(RestTemplate restTemplate, PartidaRepository partidaRepository,
            SelecaoRepository selecaoRepository, GameService gameService,
            CidadeSedeRepository cidadeSedeRepository) {
        this.restTemplate = restTemplate;
        this.partidaRepository = partidaRepository;
        this.selecaoRepository = selecaoRepository;
        this.gameService = gameService;
        this.cidadeSedeRepository = cidadeSedeRepository;
    }

    @Scheduled(fixedRate = 900000)
    public void syncMatches() {
        log.info("Starting auto-import synchronization with worldcup26.ir API...");

        try {
            if (todasAsCidades == null || todasAsCidades.isEmpty()) {
                todasAsCidades = cidadeSedeRepository.findAll();
            }

            WorldCupApiResponse response = restTemplate.getForObject(apiUrl, WorldCupApiResponse.class);

            if (response != null && response.getGames() != null) {
                List<WorldCupGameDto> matches = response.getGames();
                for (WorldCupGameDto apiMatch : matches) {
                    processMatch(apiMatch);
                }
                log.info("Auto-import completed successfully. Processed {} matches.", matches.size());
            }

        } catch (Exception e) {
            log.error("Error during API synchronization: ", e);
        }
    }

    private void processMatch(WorldCupGameDto apiMatch) {
        if (apiMatch.getId() == null)
            return;

        Long apiId = Long.parseLong(apiMatch.getId());

        // 1. Resolve Teams
        String homeName = resolveTeamName(apiMatch.getHomeTeamNameEn(), apiMatch.getHomeTeamLabel());
        String awayName = resolveTeamName(apiMatch.getAwayTeamNameEn(), apiMatch.getAwayTeamLabel());

        if (homeName == null || awayName == null)
            return;

        Selecao homeSelecao = getOrCreateTeam(homeName, apiMatch.getGroup());
        Selecao awaySelecao = getOrCreateTeam(awayName, apiMatch.getGroup());

        // 2. Resolve Match
        Partida p = partidaRepository.findByApiId(apiId).orElse(new Partida());

        if (p.getApiId() == null) {
            p.setApiId(apiId);
            p.setTime1(homeSelecao);
            p.setTime2(awaySelecao);

            // Parse Date
            if (apiMatch.getLocalDate() != null && !apiMatch.getLocalDate().equals("null")) {
                try {
                    LocalDateTime ldt = LocalDateTime.parse(apiMatch.getLocalDate(), DATE_FORMATTER);
                    p.setData(ldt.toLocalDate());
                    p.setHorario(ldt.toLocalTime());
                } catch (Exception e) {
                    log.warn("Failed to parse date {} for match {}", apiMatch.getLocalDate(), apiId);
                    p.setData(java.time.LocalDate.now());
                    p.setHorario(java.time.LocalTime.NOON);
                }
            }

            p.setFase(mapPhase(apiMatch.getType(), apiMatch.getGroup()));
        }

        // Atribui uma cidade/estádio caso ainda não tenha (usando o ID da API para distribuir de forma previsível)
        if (p.getCidade() == null && todasAsCidades != null && !todasAsCidades.isEmpty()) {
            int index = (int) (apiId % todasAsCidades.size());
            CidadeSede cidade = todasAsCidades.get(index);
            p.setCidade(cidade);
            p.setEstadio(cidade.getEstadio());
        }

        // 3. Update score & status
        updateMatchScoreAndStatus(p, apiMatch);
        partidaRepository.save(p);

        // 4. Update Group Stats
        if ("Grupo".equals(p.getFase()) && "FINALIZADA".equals(p.getStatus())) {
            try {
                gameService.atualizarResultado(p.getId(), p.getGolsTime1(), p.getGolsTime2());
            } catch (Exception e) {
                // Ignore silent errors for groups
            }
        }
    }

    private String resolveTeamName(String nameEn, String label) {
        if (nameEn != null && !nameEn.trim().isEmpty() && !nameEn.equalsIgnoreCase("null")) {
            return nameEn;
        }
        if (label != null && !label.trim().isEmpty() && !label.equalsIgnoreCase("null")) {
            return label; // e.g. "Winner Group A"
        }
        return "TBD";
    }

    private Selecao getOrCreateTeam(String name, String groupLabel) {
        Optional<Selecao> opt = selecaoRepository.findByNome(name);
        if (opt.isPresent()) {
            return opt.get();
        }

        Selecao s = new Selecao();
        s.setNome(name);
        // If it's a "TBD" or "Winner", don't assign a group to avoid cluttering the
        // group stage tables
        if (name.startsWith("Winner") || name.startsWith("Runner") || name.startsWith("Loser") || name.equals("TBD")
                || name.startsWith("3rd")) {
            s.setGrupo("??");
        } else {
            // "group" in API could be "A", "B", etc. Only use first letter if it's a group
            // stage.
            s.setGrupo(groupLabel != null && groupLabel.length() == 1 ? groupLabel : "?");
        }

        s.setPontos(0);
        s.setVitorias(0);
        s.setEmpates(0);
        s.setDerrotas(0);
        s.setGolsPro(0);
        s.setGolsContra(0);

        return selecaoRepository.save(s);
    }

    private String mapPhase(String type, String groupLabel) {
        if ("group".equalsIgnoreCase(type))
            return "Grupo";
        if ("r32".equalsIgnoreCase(type))
            return "16-avos";
        if ("r16".equalsIgnoreCase(type))
            return "Oitavas";
        if ("qf".equalsIgnoreCase(type))
            return "Quartas";
        if ("sf".equalsIgnoreCase(type))
            return "Semifinal";
        if ("third".equalsIgnoreCase(type))
            return "Terceiro Lugar";
        if ("final".equalsIgnoreCase(type))
            return "Final";
        return groupLabel != null ? groupLabel : "Fase Desconhecida";
    }

    private void updateMatchScoreAndStatus(Partida p, WorldCupGameDto apiMatch) {
        if (apiMatch.getHomeScore() == null || apiMatch.getAwayScore() == null)
            return;
        if ("null".equals(apiMatch.getHomeScore()) || "null".equals(apiMatch.getAwayScore()))
            return;

        try {
            p.setGolsTime1(Integer.parseInt(apiMatch.getHomeScore()));
            p.setGolsTime2(Integer.parseInt(apiMatch.getAwayScore()));

            String finishedStr = apiMatch.getFinished();
            String timeElapsed = apiMatch.getTimeElapsed();

            if ("TRUE".equalsIgnoreCase(finishedStr) || "finished".equalsIgnoreCase(timeElapsed)) {
                p.setStatus("FINALIZADA");
            } else if ("notstarted".equalsIgnoreCase(timeElapsed)) {
                p.setStatus("AGENDADA");
            } else {
                p.setStatus("EM_ANDAMENTO");
            }
        } catch (NumberFormatException e) {
            log.warn("Invalid score format for match {}: home={}, away={}",
                    apiMatch.getId(), apiMatch.getHomeScore(), apiMatch.getAwayScore());
        }
    }
}
