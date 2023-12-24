package com.fantasy.football.web.api.controller.impl;

import com.fantasy.football.model.LeagueTeam;
import com.fantasy.football.web.api.controller.LeagueTeamApiController;
import com.fantasy.football.web.api.service.LeagueTeamApiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = {"/fantasy/football/league/team"})
public class LeagueTeamApiControllerImpl implements LeagueTeamApiController {

    private final LeagueTeamApiService leagueTeamApiService;

    public LeagueTeamApiControllerImpl (LeagueTeamApiService leagueTeamApiService) {
        this.leagueTeamApiService = leagueTeamApiService;
    }

    @Override
    @GetMapping(path = {"/fetch/{teamCode}"})
    public ResponseEntity<LeagueTeam> fetchLeagueTeamInfo(int teamCode) {
        return ResponseEntity.of(leagueTeamApiService.fetchAndReturnLeagueTeamRecordByTeamCode(teamCode));
    }

    @Override
    @PostMapping(path = {"/create"})
    public ResponseEntity<Void> createLeagueTeamRecord(LeagueTeam leagueTeamRecord) {
        leagueTeamApiService.processAndCreateLeagueTeamRecord(leagueTeamRecord);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @PutMapping(path = {"/update"})
    public ResponseEntity<Void> updateLeagueTeamRecord(LeagueTeam leagueTeamRecord) {
        leagueTeamApiService.processAndUpdateLeagueTeamRecord(leagueTeamRecord);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    @DeleteMapping(path = {"/delete/{teamCode}"})
    public ResponseEntity<Void> deleteLeagueTeamRecord(int teamCode) {
        leagueTeamApiService.processAndDeleteLeagueTeamRecord(teamCode);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}