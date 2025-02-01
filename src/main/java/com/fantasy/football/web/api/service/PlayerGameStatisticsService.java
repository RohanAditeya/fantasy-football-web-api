package com.fantasy.football.web.api.service;

import com.fantasy.football.dto.PlayerGameStatisticsPatchDTO;
import com.fantasy.football.model.PlayerFantasyStatistics;
import com.fantasy.football.model.PlayerGameStatistics;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PlayerGameStatisticsService {

    Mono<PlayerGameStatistics> validateAndSavePlayerGameStatistics(Mono<PlayerGameStatistics> playerGameStatistics);
    Mono<Void> deleteGameStatisticsRecordById(UUID recordId);
    Flux<PlayerGameStatistics> fetchFantasyStatisticsRecord(UUID recordId);
    Mono<PlayerGameStatistics> updateGameStatisticsRecord(PlayerGameStatisticsPatchDTO updateDto, UUID recordId);
}