package com.fantasy.football.web.api.service.impl;

import com.fantasy.football.model.PlayerFantasyStatistics;
import com.fantasy.football.web.api.repository.PlayerFantasyStatisticsRepository;
import com.fantasy.football.web.api.service.PlayerFantasyStatisticsService;
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
class PlayerFantasyStatisticsServiceImpl implements PlayerFantasyStatisticsService {

    private final PlayerFantasyStatisticsRepository playerFantasyStatisticsRepository;

    @Override
    public Mono<PlayerFantasyStatistics> validateAndSavePlayerFantasyStatistics(Mono<PlayerFantasyStatistics> playerFantasyStatistics) {
        return playerFantasyStatistics
                .flatMap(playerFantasyStatisticsRepository::save);
    }

    @Override
    public Mono<Void> deleteFantasyStatisticsRecordByRecordId(UUID recordId) {
        return playerFantasyStatisticsRepository.deleteById(recordId);
    }

    @Override
    public Flux<PlayerFantasyStatistics> fetchFantasyStatisticsRecord(UUID recordId) {
        return playerFantasyStatisticsRepository.findById(recordId).doOnNext(fetchedRecord -> log.info("Fetched player fantasy statistics record with ID {}", recordId))
                .flux().switchIfEmpty(Flux.<PlayerFantasyStatistics>empty().doOnComplete(() -> log.info("Found no player fantasy statistics record with ID {}", recordId)));
    }
}