package com.fantasy.football.web.api.controller;

import com.fantasy.football.dto.PlayerGameStatisticsPatchDTO;
import com.fantasy.football.model.PlayerGameStatistics;
import com.fantasy.football.reactive.controller.LeaguePlayerGameStatisticsApi;
import com.fantasy.football.web.api.service.PlayerGameStatisticsService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class PlayerGameStatisticsApiController implements LeaguePlayerGameStatisticsApi {

    private final PlayerGameStatisticsService playerGameStatisticsService;

    @Override
    public Mono<ResponseEntity<PlayerGameStatistics>> createGameStatistics(@Valid Mono<PlayerGameStatistics> body, ServerWebExchange exchange) {
        Mono<PlayerGameStatistics> bodyWithId = body.doOnNext(bodyEntity -> bodyEntity.setRecordId(UUID.randomUUID()));
        return playerGameStatisticsService.validateAndSavePlayerGameStatistics(bodyWithId)
                .doOnNext(savedRecord -> log.info("Saved league player game statistics record with ID {}", savedRecord.getRecordId()))
                .map(savedRecord -> ResponseEntity.status(HttpStatus.CREATED).body(savedRecord));
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteGameStatistics(UUID recordId, UUID playerId, ServerWebExchange exchange) {
        return null;
    }

    @Override
    public Mono<ResponseEntity<Flux<PlayerGameStatistics>>> fetchGameStatistics(UUID recordId, UUID playerId, @Valid Integer pageNumber, ServerWebExchange exchange) {
        return null;
    }

    @Override
    public Mono<ResponseEntity<PlayerGameStatistics>> updateGameStatistics(@Valid Mono<PlayerGameStatisticsPatchDTO> playerGameStatisticsPatchDTO, UUID recordId, UUID playerId, ServerWebExchange exchange) {
        return null;
    }
}