package com.fantasy.football.web.api.service;

import com.fantasy.football.model.LeagueTeam;

public interface LeagueTeamService {

    void validateAndSaveLeagueTeam (LeagueTeam leagueTeamRecord);
}