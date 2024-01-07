package com.fantasy.football.web.api.service.impl;

import com.fantasy.football.model.LeagueTeam;
import com.fantasy.football.web.api.repository.LeagueTeamRepository;
import com.fantasy.football.web.api.service.LeagueTeamApiService;
import org.springframework.stereotype.Service;

@Service
public class LeagueTeamApiServiceImpl implements LeagueTeamApiService {

    private final LeagueTeamRepository leagueTeamRepository;

    public LeagueTeamApiServiceImpl (LeagueTeamRepository leagueTeamRepository) {
        this.leagueTeamRepository = leagueTeamRepository;
    }

    @Override
    public void processAndCreateLeagueTeamRecord (LeagueTeam leagueTeamRecord) {
        leagueTeamRepository.save(leagueTeamRecord);
    }
}