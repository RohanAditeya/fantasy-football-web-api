package com.fantasy.football.web.api.game.service;

import com.fantasy.football.model.PlayerGameStatistics;
import reactor.core.publisher.Mono;

public interface PlayerGameStatisticsService {
    Mono<PlayerGameStatistics> validateAndSaveLeagueTeam(Mono<PlayerGameStatistics> playerGameStatistics);
}