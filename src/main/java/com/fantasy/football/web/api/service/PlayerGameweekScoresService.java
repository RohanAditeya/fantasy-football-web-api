package com.fantasy.football.web.api.service;

import com.fantasy.football.dto.GameWeekScoreDTO;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PlayerGameweekScoresService {
    @Transactional
    Mono<GameWeekScoreDTO> createGameweekScoreRecord(Mono<GameWeekScoreDTO> gameWeekScoreDTOMono);
    Mono<Void> deleteGameweekRecordsForPlayer(UUID recordId, UUID playerId);
    Flux<GameWeekScoreDTO> fetchPlayerGameweekScores(UUID gameweekStatsRecordId, UUID playerId, Integer gameweek, Integer pageNumber);
}