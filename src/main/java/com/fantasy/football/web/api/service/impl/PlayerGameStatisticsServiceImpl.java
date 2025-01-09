package com.fantasy.football.web.api.service.impl;

import com.fantasy.football.model.PlayerGameStatistics;
import com.fantasy.football.web.api.repository.PlayerGameStatisticsRepository;
import com.fantasy.football.web.api.service.PlayerGameStatisticsService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
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

    @Override
    public Flux<PlayerGameStatistics> fetchFantasyStatisticsRecord(UUID recordId) {
        return playerGameStatisticsRepository.findById(recordId).doOnNext(fetchedRecord -> log.info("Fetched player game statistics record with ID {}", recordId))
                .flux().switchIfEmpty(Flux.<PlayerGameStatistics>empty().doOnComplete(() -> log.info("Found no player game statistics record with ID {}", recordId)));
    }
}