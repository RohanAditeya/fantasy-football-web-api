package com.fantasy.football.web.api.service;

import com.fantasy.football.model.LeagueTeam;

import java.util.Optional;

public interface LeagueTeamApiService {
    Optional<LeagueTeam> fetchAndReturnLeagueTeamRecordByTeamCode (int teamCode);
    void processAndCreateLeagueTeamRecord (LeagueTeam leagueTeamRecord);
    void processAndUpdateLeagueTeamRecord (LeagueTeam leagueTeamRecord);
    void processAndDeleteLeagueTeamRecord (int teamCode);
}