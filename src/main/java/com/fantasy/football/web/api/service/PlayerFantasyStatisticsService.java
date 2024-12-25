package com.fantasy.football.web.api.service;

import com.fantasy.football.model.PlayerFantasyStatistics;
import reactor.core.publisher.Mono;

public interface PlayerFantasyStatisticsService {

    Mono<PlayerFantasyStatistics> validateAndSavePlayerFantasyStatistics(Mono<PlayerFantasyStatistics> playerFantasyStatistics);
}