package com.fantasy.football.web.api.controller;

import com.fantasy.football.dto.GameWeekScoreDTO;
import com.fantasy.football.reactive.controller.LeaguePlayerGameweekScoresApi;
import com.fantasy.football.web.api.service.PlayerGameweekScoresService;
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
class PlayerGameweekScoresApiController implements LeaguePlayerGameweekScoresApi {

    private final PlayerGameweekScoresService playerGameweekScoresService;

    @Override
    public Mono<ResponseEntity<GameWeekScoreDTO>> createGameWeekScores(@Valid Mono<GameWeekScoreDTO> gameWeekScoreDTO, ServerWebExchange exchange) {
        // TODO must change error code when exception before saving record.
        return playerGameweekScoresService.createGameweekScoreRecord(gameWeekScoreDTO).map(savedRecords -> ResponseEntity.status(HttpStatus.CREATED).body(savedRecords));
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteGameWeekScores(UUID recordId, UUID playerId, ServerWebExchange exchange) {
        // TODO return proper response when both headers are missing
        // TODO return 404 when record when not there to delete.
        return playerGameweekScoresService.deleteGameweekRecordsForPlayer(recordId, playerId).then(Mono.just(ResponseEntity.noContent().build()));
    }

    @Override
    public Mono<ResponseEntity<Flux<GameWeekScoreDTO>>> fetchGameWeekScores(UUID recordId, UUID playerId, @Valid Integer gameWeek, @Valid Integer pageNumber, ServerWebExchange exchange) {
        // TODO return 404 when record is not found
        return Mono.just(ResponseEntity.ok(playerGameweekScoresService.fetchPlayerGameweekScores(recordId, playerId, gameWeek, pageNumber)));
    }
}