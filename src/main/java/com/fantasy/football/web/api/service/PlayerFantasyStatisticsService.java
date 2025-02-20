package com.fantasy.football.web.api.service;

import com.fantasy.football.dto.PlayerFantasyStatisticsPatchDTO;
import com.fantasy.football.model.PlayerFantasyStatistics;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PlayerFantasyStatisticsService {

    Mono<PlayerFantasyStatistics> validateAndSavePlayerFantasyStatistics(Mono<PlayerFantasyStatistics> playerFantasyStatistics);
    Mono<Void> deleteFantasyStatisticsRecordByRecordId(UUID recordId);
    Flux<PlayerFantasyStatistics> fetchFantasyStatisticsRecord(UUID recordId);
    Mono<PlayerFantasyStatistics> updateFantasyStatisticsRecord(PlayerFantasyStatisticsPatchDTO playerFantasyStatisticsPatchDTO, UUID recordId);
}