package com.fantasy.football.web.api.service.impl;

import com.fantasy.football.model.PlayerGameStatistics;
import com.fantasy.football.web.api.repository.PlayerGameStatisticsRepository;
import com.fantasy.football.web.api.service.PlayerGameStatisticsService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class PlayerGameStatisticsServiceImpl implements PlayerGameStatisticsService {

    private final PlayerGameStatisticsRepository playerGameStatisticsRepository;

    @Override
    public Mono<PlayerGameStatistics> validateAndSavePlayerGameStatistics(Mono<PlayerGameStatistics> playerGameStatistics) {
        return playerGameStatistics
                .flatMap(playerGameStatisticsRepository::save);
    }

    @Override
    public Mono<Void> deleteGameStatisticsRecordById(UUID recordId) {
        return playerGameStatisticsRepository.deleteById(recordId);
    }
}