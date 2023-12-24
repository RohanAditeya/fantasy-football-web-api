package com.fantasy.football.web.api.controller;

import com.fantasy.football.model.LeagueTeam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface LeagueTeamApiController {
    ResponseEntity<LeagueTeam> fetchLeagueTeamInfo (@PathVariable int teamCode);
    ResponseEntity<Void> createLeagueTeamRecord (@RequestBody LeagueTeam leagueTeamRecord);
    ResponseEntity<Void> updateLeagueTeamRecord (@RequestBody LeagueTeam leagueTeamRecord);
    ResponseEntity<Void> deleteLeagueTeamRecord (@PathVariable int teamCode);
}