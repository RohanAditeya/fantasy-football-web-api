package com.fantasy.football.web.api.controller;

import com.fantasy.football.model.LeagueTeam;
import com.fantasy.football.servlet.controller.LeagueTeamApi;
import com.fantasy.football.web.api.service.LeagueTeamApiService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LeagueTeamController implements LeagueTeamApi {

    private final LeagueTeamApiService leagueTeamApiService;

    public LeagueTeamController (LeagueTeamApiService leagueTeamApiService) {
        this.leagueTeamApiService = leagueTeamApiService;
    }

    @Override
    public ResponseEntity<Void> createLeagueTeam(@Valid LeagueTeam body) {
        leagueTeamApiService.processAndSaveLeagueTeam(body);
        log.atInfo().log("Successfully saved Football team record");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}