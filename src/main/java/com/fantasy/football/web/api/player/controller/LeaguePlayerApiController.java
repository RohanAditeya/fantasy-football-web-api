package com.fantasy.football.web.api.player.controller;

import com.fantasy.football.dto.CreateLeaguePlayerRequest;
import com.fantasy.football.dto.PlayerBasicInformationPatchDTO;
import com.fantasy.football.model.PlayerBasicInformation;
import com.fantasy.football.reactive.controller.LeaguePlayerApi;
import com.fantasy.football.web.api.player.service.PlayerBasicInformationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@RestController
class LeaguePlayerApiController implements LeaguePlayerApi {

    private final PlayerBasicInformationService playerBasicInformationService;

    LeaguePlayerApiController(PlayerBasicInformationService playerBasicInformationService) {
        this.playerBasicInformationService = playerBasicInformationService;
    }

    @Override
    public Mono<ResponseEntity<PlayerBasicInformation>> createLeaguePlayer(@Valid Mono<CreateLeaguePlayerRequest> createLeaguePlayerRequest, ServerWebExchange exchange) {
        log.info("Received request to save player basic information record");
        return playerBasicInformationService.createPlayerRecord(createLeaguePlayerRequest).map(savedRecord -> ResponseEntity.status(HttpStatus.CREATED).body(savedRecord));
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteLeaguePlayer(UUID recordId, String playerCode, ServerWebExchange exchange) {
        return null;
    }

    @Override
    public Mono<ResponseEntity<Flux<PlayerBasicInformation>>> fetchLeaguePlayer(UUID recordId, String playerCode, String teamId, @Valid Integer pageNumber, ServerWebExchange exchange) {
        return null;
    }

    @Override
    public Mono<ResponseEntity<PlayerBasicInformation>> updateLeaguePlayer(@Valid Mono<PlayerBasicInformationPatchDTO> playerBasicInformationPatchDTO, UUID recordId, String playerCode, ServerWebExchange exchange) {
        return null;
    }
}