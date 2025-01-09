package com.fantasy.football.web.api.controller;

import com.fantasy.football.dto.PlayerFantasyStatisticsPatchDTO;
import com.fantasy.football.model.PlayerFantasyStatistics;
import com.fantasy.football.reactive.controller.LeaguePlayerFantasyStatisticsApi;
import com.fantasy.football.web.api.service.PlayerFantasyStatisticsService;
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
class PlayerFantasyStatisticsApiController implements LeaguePlayerFantasyStatisticsApi {

    private final PlayerFantasyStatisticsService playerFantasyStatisticsService;

    @Override
    public Mono<ResponseEntity<PlayerFantasyStatistics>> createFantasyStatistics(@Valid Mono<PlayerFantasyStatistics> body, ServerWebExchange exchange) {
        Mono<PlayerFantasyStatistics> requestWithIdGenerated = body.doOnNext(requestBody -> requestBody.setRecordId(UUID.randomUUID()));
        return playerFantasyStatisticsService.validateAndSavePlayerFantasyStatistics(requestWithIdGenerated)
                .doOnNext(savedRecord -> log.info("Saved league Player fantasy statistics record with ID {}", savedRecord.getRecordId()))
                .map(savedRecord -> ResponseEntity.status(HttpStatus.CREATED).body(savedRecord));
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteFantasyStatistics(UUID recordId, UUID playerId, ServerWebExchange exchange) {
        // TODO playerId can be removed from header.
        // TODO return 400 when record_id header is missing
        return playerFantasyStatisticsService.deleteFantasyStatisticsRecordByRecordId(recordId)
                .then(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).<Void>build()))
                .doOnSuccess(voidType -> log.info("Deleted league player fantasy statistics record with ID {}", recordId));
    }

    @Override
    public Mono<ResponseEntity<Flux<PlayerFantasyStatistics>>> fetchFantasyStatistics(UUID recordId, UUID playerId, @Valid Integer pageNumber, ServerWebExchange exchange) {
        // TODO Remove page number and player ID. No longer need those headers.
        // TODO return 404 when record is not found
        // TODO return 400 when record_id header is missing
        return Mono.just(ResponseEntity.ok(playerFantasyStatisticsService.fetchFantasyStatisticsRecord(recordId)));
    }

    @Override
    public Mono<ResponseEntity<PlayerFantasyStatistics>> updateFantasyStatistics(@Valid Mono<PlayerFantasyStatisticsPatchDTO> playerFantasyStatisticsPatchDTO, UUID recordId, UUID playerId, ServerWebExchange exchange) {
        // TODO Remove player ID from header.
        // TODO Patch DTO object has transfersIn/transfersOut in Integer and entity has transfersInOut in long type. Need to correct it.
        // TODO 404 when record to update is not found
        return playerFantasyStatisticsPatchDTO
                .flatMap(updateRequest -> playerFantasyStatisticsService.updateFantasyStatisticsRecord(updateRequest, recordId))
                .map(updatedRecord -> ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedRecord))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.ACCEPTED).build()));
    }
}