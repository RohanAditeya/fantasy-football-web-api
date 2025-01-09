package com.fantasy.football.web.api.service.impl;

import com.fantasy.football.dto.PlayerGameStatisticsPatchDTO;
import com.fantasy.football.model.PlayerGameStatistics;
import com.fantasy.football.web.api.repository.PlayerGameStatisticsRepository;
import com.fantasy.football.web.api.service.PlayerGameStatisticsService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
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

    @Override
    public Mono<PlayerGameStatistics> updateGameStatisticsRecord(PlayerGameStatisticsPatchDTO updateDto, UUID recordId) {
        return playerGameStatisticsRepository.findById(recordId).doOnNext(
                        fetchedRecord -> {
                            Optional.ofNullable(updateDto.getMinutes()).ifPresent(fetchedRecord::setMinutes);
                            Optional.ofNullable(updateDto.getGoalsScored()).ifPresent(fetchedRecord::setGoalsScored);
                            Optional.ofNullable(updateDto.getAssists()).ifPresent(fetchedRecord::setAssists);
                            Optional.ofNullable(updateDto.getCleanSheets()).ifPresent(fetchedRecord::setCleanSheets);
                            Optional.ofNullable(updateDto.getGoalsConceded()).ifPresent(fetchedRecord::setGoalsConceded);
                            Optional.ofNullable(updateDto.getOwnGoals()).ifPresent(fetchedRecord::setOwnGoals);
                            Optional.ofNullable(updateDto.getPenaltiesSaved()).ifPresent(fetchedRecord::setPenaltiesSaved);
                            Optional.ofNullable(updateDto.getPenaltiesMissed()).ifPresent(fetchedRecord::setPenaltiesMissed);
                            Optional.ofNullable(updateDto.getYellowCards()).ifPresent(fetchedRecord::setYellowCards);
                            Optional.ofNullable(updateDto.getRedCards()).ifPresent(fetchedRecord::setRedCards);
                            Optional.ofNullable(updateDto.getSaves()).ifPresent(fetchedRecord::setSaves);
                            Optional.ofNullable(updateDto.getInfluence()).ifPresent(fetchedRecord::setInfluence);
                            Optional.ofNullable(updateDto.getCreativity()).ifPresent(fetchedRecord::setCreativity);
                            Optional.ofNullable(updateDto.getThreat()).ifPresent(fetchedRecord::setThreat);
                            Optional.ofNullable(updateDto.getStarts()).ifPresent(fetchedRecord::setStarts);
                            Optional.ofNullable(updateDto.getTotalPoints()).ifPresent(fetchedRecord::setTotalPoints);
                            Optional.ofNullable(updateDto.getStartsPer90()).ifPresent(fetchedRecord::setStartsPer90);
                            Optional.ofNullable(updateDto.getCleanSheetsPer90()).ifPresent(fetchedRecord::setCleanSheetsPer90);
                            Optional.ofNullable(updateDto.getSavesPer90()).ifPresent(fetchedRecord::setSavesPer90);
                            Optional.ofNullable(updateDto.getGoalsConcededPer90()).ifPresent(fetchedRecord::setGoalsConcededPer90);
                        }
                )
                .flatMap(playerGameStatisticsRepository::save)
                .doOnNext(updatedRecord -> log.info("Updated player game statistics record with ID {}", updatedRecord.getRecordId()))
                .switchIfEmpty(Mono.<PlayerGameStatistics>empty().doOnSuccess(voidType -> log.info("Cannot find any player game statistics record with ID {} for updating", recordId)));
    }
}