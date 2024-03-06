package com.fantasy.football.web.api.controller;

import com.fantasy.football.model.LeagueTeam;
import com.fantasy.football.servlet.controller.LeagueTeamApi;
import com.fantasy.football.web.api.service.LeagueTeamService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MarkerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static com.fantasy.football.web.api.utils.ApplicationConstants.LEAGUE_TEAM_CREATE;

@Slf4j
@RestController
public class LeagueTeamController implements LeagueTeamApi {

    public final LeagueTeamService leagueTeamService;

    public LeagueTeamController (LeagueTeamService leagueTeamService) {
        this.leagueTeamService = leagueTeamService;
    }
    @Override
    public ResponseEntity<Void> createLeagueTeam(@Valid LeagueTeam body) {
        log.atInfo().addMarker(MarkerFactory.getMarker(LEAGUE_TEAM_CREATE)).log("Handling POST request for league team API");
        leagueTeamService.validateAndSaveLeagueTeam(body);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}