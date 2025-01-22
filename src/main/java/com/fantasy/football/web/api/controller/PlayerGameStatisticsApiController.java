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
class PlayerGameStatisticsApiController implements LeaguePlayerGameStatisticsApi {

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
        // TODO remove player_id header
        // TODO return 400 when record_id header is missing
        return playerGameStatisticsService.deleteGameStatisticsRecordById(recordId)
                .then(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).<Void>build()))
                .doOnSuccess(voidType -> log.info("Deleted league player game statistics record with ID {}", recordId));
    }

    @Override
    public Mono<ResponseEntity<Flux<PlayerGameStatistics>>> fetchGameStatistics(UUID recordId, UUID playerId, @Valid Integer pageNumber, ServerWebExchange exchange) {
        // TODO Remove page number and player ID. No longer need those headers.
        // TODO Return 404 when record not found
        // TODO return 400 when record_id header is missing
        return Mono.just(ResponseEntity.ok(playerGameStatisticsService.fetchFantasyStatisticsRecord(recordId)));
    }

    @Override
    public Mono<ResponseEntity<PlayerGameStatistics>> updateGameStatistics(@Valid Mono<PlayerGameStatisticsPatchDTO> playerGameStatisticsPatchDTO, UUID recordId, UUID playerId, ServerWebExchange exchange) {
        // TODO Remove player ID from header.
        // TODO 404 when record to update is not found
        return playerGameStatisticsPatchDTO
                .flatMap(updateRequest -> playerGameStatisticsService.updateGameStatisticsRecord(updateRequest, recordId))
                .map(updateRecord -> ResponseEntity.status(HttpStatus.ACCEPTED).body(updateRecord))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.ACCEPTED).build()));
    }
}