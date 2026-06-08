package com.evento.infrastructure.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WorldCupGameDto {
    private String id;

    @JsonProperty("home_team_name_en")
    private String homeTeamNameEn;

    @JsonProperty("away_team_name_en")
    private String awayTeamNameEn;

    @JsonProperty("home_score")
    private String homeScore;

    @JsonProperty("away_score")
    private String awayScore;

    private String finished;

    @JsonProperty("time_elapsed")
    private String timeElapsed;

    private String group;

    @JsonProperty("local_date")
    private String localDate;

    private String type;

    @JsonProperty("home_team_label")
    private String homeTeamLabel;

    @JsonProperty("away_team_label")
    private String awayTeamLabel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHomeTeamNameEn() {
        return homeTeamNameEn;
    }

    public void setHomeTeamNameEn(String homeTeamNameEn) {
        this.homeTeamNameEn = homeTeamNameEn;
    }

    public String getAwayTeamNameEn() {
        return awayTeamNameEn;
    }

    public void setAwayTeamNameEn(String awayTeamNameEn) {
        this.awayTeamNameEn = awayTeamNameEn;
    }

    public String getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(String homeScore) {
        this.homeScore = homeScore;
    }

    public String getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(String awayScore) {
        this.awayScore = awayScore;
    }

    public String getFinished() {
        return finished;
    }

    public void setFinished(String finished) {
        this.finished = finished;
    }

    public String getTimeElapsed() {
        return timeElapsed;
    }

    public void setTimeElapsed(String timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getLocalDate() {
        return localDate;
    }

    public void setLocalDate(String localDate) {
        this.localDate = localDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHomeTeamLabel() {
        return homeTeamLabel;
    }

    public void setHomeTeamLabel(String homeTeamLabel) {
        this.homeTeamLabel = homeTeamLabel;
    }

    public String getAwayTeamLabel() {
        return awayTeamLabel;
    }

    public void setAwayTeamLabel(String awayTeamLabel) {
        this.awayTeamLabel = awayTeamLabel;
    }
}
