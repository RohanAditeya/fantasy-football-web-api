package com.fantasy.football.web.api.service;

import com.fantasy.football.dto.GameWeekScoreDTO;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

public interface PlayerGameweekScoresService {
    @Transactional
    Mono<GameWeekScoreDTO> createGameweekScoreRecord(Mono<GameWeekScoreDTO> gameWeekScoreDTOMono);
}