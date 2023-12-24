package com.fantasy.football.web.api.service.impl;

import com.fantasy.football.model.LeagueTeam;
import com.fantasy.football.web.api.repository.LeagueTeamRepository;
import com.fantasy.football.web.api.service.LeagueTeamApiService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LeagueTeamApiServiceImpl implements LeagueTeamApiService {

    private final LeagueTeamRepository leagueTeamRepository;

    public LeagueTeamApiServiceImpl (LeagueTeamRepository leagueTeamRepository) {
        this.leagueTeamRepository = leagueTeamRepository;
    }

    @Override
    public Optional<LeagueTeam> fetchAndReturnLeagueTeamRecordByTeamCode(int teamCode) {
        return leagueTeamRepository.findByTeamKeyCode(teamCode);
    }

    @Override
    public void processAndCreateLeagueTeamRecord (LeagueTeam leagueTeamRecord) {
        leagueTeamRecord.initializePrimaryKey();
        leagueTeamRepository.save(leagueTeamRecord);
    }

    @Override
    public void processAndUpdateLeagueTeamRecord(LeagueTeam leagueTeamRecord) {
        leagueTeamRepository.save(leagueTeamRecord);
    }

    @Override
    public void processAndDeleteLeagueTeamRecord(int teamCode) {
        leagueTeamRepository.deleteByTeamKeyCode(teamCode);
    }
}