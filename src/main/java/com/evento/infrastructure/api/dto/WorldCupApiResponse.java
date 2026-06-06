package com.evento.infrastructure.api.dto;

import java.util.List;

public class WorldCupApiResponse {
    private List<WorldCupGameDto> games;

    public List<WorldCupGameDto> getGames() {
        return games;
    }

    public void setGames(List<WorldCupGameDto> games) {
        this.games = games;
    }
}
