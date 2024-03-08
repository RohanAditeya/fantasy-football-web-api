package com.fantasy.football.web.api.service.impl;

import com.fantasy.football.model.LeagueTeam;
import com.fantasy.football.model.PlayerBasicInformation;
import com.fantasy.football.web.api.repository.LeagueTeamRepository;
import com.fantasy.football.web.api.repository.PlayerBasicInformationRepository;
import com.fantasy.football.web.api.service.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

import static com.fantasy.football.web.api.utils.ApplicationConstants.PLAYER_CREATE_MARKER;

@Slf4j
@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerBasicInformationRepository playerBasicInformationRepository;
    private final LeagueTeamRepository leagueTeamRepository;

    public PlayerServiceImpl (PlayerBasicInformationRepository playerBasicInformationRepository, LeagueTeamRepository leagueTeamRepository) {
        this.playerBasicInformationRepository = playerBasicInformationRepository;
        this.leagueTeamRepository = leagueTeamRepository;
    }
    @Override
    public void validateAndSavePlayer(PlayerBasicInformation playerRecord) {
        LeagueTeam leagueTeam = leagueTeamRepository.findByCompositeKeyCode(playerRecord.getTeam().getCompositeKey().getCode()).orElseThrow(
                () -> new NoSuchElementException("Could not find team with code 1 while saving player mock-name")
        );
        log.atInfo().addMarker(MarkerFactory.getMarker(PLAYER_CREATE_MARKER)).log("Got Team record for player");
        playerRecord.setTeam(leagueTeam);
        playerBasicInformationRepository.save(playerRecord);
    }
}