package com.fantasy.football.web.api.fantasy.service;

import com.fantasy.football.model.PlayerFantasyStatistics;
import reactor.core.publisher.Mono;

public interface PlayerFantasyStatisticsService {

    Mono<PlayerFantasyStatistics> validateAndSaveLeagueTeam(Mono<PlayerFantasyStatistics> fantasyStatisticsRequest);
}