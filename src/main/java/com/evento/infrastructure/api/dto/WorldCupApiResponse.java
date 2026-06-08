package com.evento.infrastructure.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WorldCupApiResponse {
    private List<WorldCupGameDto> games;

    public List<WorldCupGameDto> getGames() {
        return games;
    }

    public void setGames(List<WorldCupGameDto> games) {
        this.games = games;
    }
}
