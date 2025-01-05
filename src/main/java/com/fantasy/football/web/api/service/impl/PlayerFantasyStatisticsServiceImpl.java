package com.fantasy.football.web.api.service.impl;

import com.fantasy.football.dto.PlayerFantasyStatisticsPatchDTO;
import com.fantasy.football.model.PlayerFantasyStatistics;
import com.fantasy.football.web.api.repository.PlayerFantasyStatisticsRepository;
import com.fantasy.football.web.api.service.PlayerFantasyStatisticsService;
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

    @Override
    public Mono<PlayerFantasyStatistics> updateFantasyStatisticsRecord(PlayerFantasyStatisticsPatchDTO playerFantasyStatisticsPatchDTO, UUID recordId) {
        return playerFantasyStatisticsRepository.findById(recordId).doOnNext(
                        fetchedRecord -> {
                            Optional.ofNullable(playerFantasyStatisticsPatchDTO.getChanceOfPlayingNextRound()).ifPresent(fetchedRecord::setChanceOfPlayingNextRound);
                            Optional.ofNullable(playerFantasyStatisticsPatchDTO.getChanceOfPlayingThisRound()).ifPresent(fetchedRecord::setChanceOfPlayingThisRound);
                            Optional.ofNullable(playerFantasyStatisticsPatchDTO.getDreamTeamCount()).ifPresent(fetchedRecord::setDreamTeamCount);
                            Optional.ofNullable(playerFantasyStatisticsPatchDTO.getExpectedPointsNext()).ifPresent(fetchedRecord::setExpectedPointsNext);
                            Optional.ofNullable(playerFantasyStatisticsPatchDTO.getExpectedPointsThis()).ifPresent(fetchedRecord::setExpectedPointsThis);
                            Optional.ofNullable(playerFantasyStatisticsPatchDTO.getEventPoints()).ifPresent(fetchedRecord::setEventPoints);
                            Optional.ofNullable(playerFantasyStatisticsPatchDTO.getIsInDreamTeam()).ifPresent(fetchedRecord::setInDreamTeam);
                            Optional.ofNullable(playerFantasyStatisticsPatchDTO.getForm()).ifPresent(fetchedRecord::setForm);
                            Optional.ofNullable(playerFantasyStatisticsPatchDTO.getNowCost()).ifPresent(fetchedRecord::setNowCost);
                            Optional.ofNullable(playerFantasyStatisticsPatchDTO.getPointsPerGame()).ifPresent(fetchedRecord::setPointsPerGame);
                            Optional.ofNullable(playerFantasyStatisticsPatchDTO.getSelectedByPercent()).ifPresent(fetchedRecord::setSelectedByPercent);
                            Optional.ofNullable(playerFantasyStatisticsPatchDTO.getTotalPoints()).ifPresent(fetchedRecord::setTotalPoints);
                            Optional.ofNullable(playerFantasyStatisticsPatchDTO.getTransfersIn()).map(Integer::longValue).ifPresent(fetchedRecord::setTransfersIn);
                            Optional.ofNullable(playerFantasyStatisticsPatchDTO.getTransfersOut()).map(Integer::longValue).ifPresent(fetchedRecord::setTransfersOut);
                            Optional.ofNullable(playerFantasyStatisticsPatchDTO.getValueForm()).ifPresent(fetchedRecord::setValueForm);
                            Optional.ofNullable(playerFantasyStatisticsPatchDTO.getBonus()).ifPresent(fetchedRecord::setBonus);
                            Optional.ofNullable(playerFantasyStatisticsPatchDTO.getBps()).ifPresent(fetchedRecord::setBps);
                            Optional.ofNullable(playerFantasyStatisticsPatchDTO.getExpectedGoals()).ifPresent(fetchedRecord::setExpectedGoals);
                            Optional.ofNullable(playerFantasyStatisticsPatchDTO.getExpectedAssists()).ifPresent(fetchedRecord::setExpectedAssists);
                            Optional.ofNullable(playerFantasyStatisticsPatchDTO.getExpectedGoalInvolvements()).ifPresent(fetchedRecord::setExpectedGoalInvolvements);
                            Optional.ofNullable(playerFantasyStatisticsPatchDTO.getExpectedGoalsConceded()).ifPresent(fetchedRecord::setExpectedGoalsConceded);
                            Optional.ofNullable(playerFantasyStatisticsPatchDTO.getExpectedGoalsPer90()).ifPresent(fetchedRecord::setExpectedGoalsPer90);
                            Optional.ofNullable(playerFantasyStatisticsPatchDTO.getExpectedAssistsPer90()).ifPresent(fetchedRecord::setExpectedAssistsPer90);
                            Optional.ofNullable(playerFantasyStatisticsPatchDTO.getExpectedGoalInvolvementsPer90()).ifPresent(fetchedRecord::setExpectedGoalInvolvementsPer90);
                            Optional.ofNullable(playerFantasyStatisticsPatchDTO.getExpectedGoalsConcededPer90()).ifPresent(fetchedRecord::setExpectedGoalConcededPer90);
                        }
                )
                .flatMap(playerFantasyStatisticsRepository::save)
                .doOnNext(updatedRecord -> log.info("Updated player fantasy statistics record with ID {}", updatedRecord.getRecordId()))
                .switchIfEmpty(Mono.<PlayerFantasyStatistics>empty().doOnSuccess(voidType -> log.info("Cannot find any player fantasy statistics record with ID {} for updating", recordId)));
    }
}