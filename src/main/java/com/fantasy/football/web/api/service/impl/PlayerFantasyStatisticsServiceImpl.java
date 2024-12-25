package com.fantasy.football.web.api.service.impl;

import com.fantasy.football.model.PlayerFantasyStatistics;
import com.fantasy.football.web.api.repository.PlayerFantasyStatisticsRepository;
import com.fantasy.football.web.api.service.PlayerFantasyStatisticsService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class PlayerFantasyStatisticsServiceImpl implements PlayerFantasyStatisticsService {

    private final PlayerFantasyStatisticsRepository playerFantasyStatisticsRepository;

    @Override
    public Mono<PlayerFantasyStatistics> validateAndSavePlayerFantasyStatistics(Mono<PlayerFantasyStatistics> playerFantasyStatistics) {
        return playerFantasyStatistics
                .flatMap(playerFantasyStatisticsRepository::save);
    }
}