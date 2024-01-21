package com.fantasy.football.web.api.service.impl;

import com.fantasy.football.model.PlayerBasicInformation;
import com.fantasy.football.web.api.repository.LeaguePlayerInfoRepository;
import com.fantasy.football.web.api.repository.LeagueTeamRepository;
import com.fantasy.football.web.api.service.LeaguePlayerInfoApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Slf4j
@Service
public class LeaguePlayerApiServiceImpl implements LeaguePlayerInfoApiService {

    private final LeaguePlayerInfoRepository playerInfoRepository;
    private final LeagueTeamRepository leagueTeamRepository;

    public LeaguePlayerApiServiceImpl (LeaguePlayerInfoRepository playerInfoRepository, LeagueTeamRepository leagueTeamRepository) {
        this.playerInfoRepository = playerInfoRepository;
        this.leagueTeamRepository = leagueTeamRepository;
    }

    @Override
    public void processAndSavePlayerInfo(PlayerBasicInformation playerBasicInformationRecord) {
        playerBasicInformationRecord.setTeam(
                leagueTeamRepository.findById(playerBasicInformationRecord.getTeamCode()).orElseThrow(
                        () -> new NoSuchElementException(String.format("Could not find team with code %s while saving player %s", playerBasicInformationRecord.getTeamCode(), playerBasicInformationRecord.getWebName()))
                )
        );
        playerInfoRepository.save(playerBasicInformationRecord);
    }
}