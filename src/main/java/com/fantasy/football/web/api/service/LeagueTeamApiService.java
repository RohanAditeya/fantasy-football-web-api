package com.fantasy.football.web.api.service;

import com.fantasy.football.model.LeagueTeam;

import java.util.Optional;

public interface LeagueTeamApiService {
    void processAndCreateLeagueTeamRecord (LeagueTeam leagueTeamRecord);
}