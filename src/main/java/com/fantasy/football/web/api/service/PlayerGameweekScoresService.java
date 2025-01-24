package com.fantasy.football.web.api.service;

import com.fantasy.football.dto.GameWeekScoreDTO;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PlayerGameweekScoresService {
    @Transactional
    Mono<GameWeekScoreDTO> createGameweekScoreRecord(Mono<GameWeekScoreDTO> gameWeekScoreDTOMono);
    Mono<Void> deleteGameweekRecordsForPlayer(UUID recordId, UUID playerId);
}