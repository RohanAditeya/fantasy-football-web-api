package com.fantasy.football.web.api.controller;

import com.fantasy.football.model.LeagueTeam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface LeagueTeamApiController {
    ResponseEntity<Void> createLeagueTeamRecord (@RequestBody LeagueTeam leagueTeamRecord);
}