package com.fantasy.football.web.api.controller;

import com.fantasy.football.model.PlayerBasicInformation;
import com.fantasy.football.servlet.controller.LeaguePlayerApi;
import com.fantasy.football.web.api.service.PlayerService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MarkerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static com.fantasy.football.web.api.utils.ApplicationConstants.PLAYER_CREATE_MARKER;

@Slf4j
@RestController
public class PlayerController implements LeaguePlayerApi {

    private final PlayerService playerService;

    public PlayerController (PlayerService playerService) {
        this.playerService = playerService;
    }
    @Override
    public ResponseEntity<Void> createLeaguePlayer(@Valid PlayerBasicInformation body) {
        log.atInfo().addMarker(MarkerFactory.getMarker(PLAYER_CREATE_MARKER)).log("Handling POST request for league player API");
        playerService.validateAndSavePlayer(body);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}