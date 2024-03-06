package com.fantasy.football.web.api.service.impl;

import com.fantasy.football.model.LeagueTeam;
import com.fantasy.football.web.api.repository.LeagueTeamRepository;
import com.fantasy.football.web.api.service.LeagueTeamService;
import org.springframework.stereotype.Service;

@Service
public class LeagueTeamServiceImpl implements LeagueTeamService {

    private final LeagueTeamRepository leagueTeamRepository;

    public LeagueTeamServiceImpl (LeagueTeamRepository leagueTeamRepository) {
        this.leagueTeamRepository = leagueTeamRepository;
    }
    @Override
    public void validateAndSaveLeagueTeam(LeagueTeam leagueTeamRecord) {
        leagueTeamRepository.save(leagueTeamRecord);
    }
}