package com.fantasy.football.web.api.service;

import com.fantasy.football.model.PlayerGameStatistics;
import reactor.core.publisher.Mono;

public interface PlayerGameStatisticsService {

    Mono<PlayerGameStatistics> validateAndSavePlayerGameStatistics(Mono<PlayerGameStatistics> playerGameStatistics);
}