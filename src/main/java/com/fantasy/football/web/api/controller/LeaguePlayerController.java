package com.fantasy.football.web.api.controller;

import com.fantasy.football.model.PlayerBasicInformation;
import com.fantasy.football.servlet.controller.LeaguePlayerApi;
import com.fantasy.football.web.api.service.LeaguePlayerInfoApiService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LeaguePlayerController implements LeaguePlayerApi {

    private final LeaguePlayerInfoApiService playerInfoApiService;

    public LeaguePlayerController (LeaguePlayerInfoApiService playerInfoApiService) {
        this.playerInfoApiService = playerInfoApiService;
    }

    @Override
    public ResponseEntity<Void> createLeaguePlayer(@Valid PlayerBasicInformation body) {
        playerInfoApiService.processAndSavePlayerInfo(body);
        log.atInfo().log("Successfully saved Football player info record");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}