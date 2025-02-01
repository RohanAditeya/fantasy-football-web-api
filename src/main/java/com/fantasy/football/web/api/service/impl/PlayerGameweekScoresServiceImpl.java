package com.fantasy.football.web.api.service.impl;

import com.fantasy.football.dto.GameWeekScoreDTO;
import com.fantasy.football.model.PlayerGameWeekBreakup;
import com.fantasy.football.model.PlayerGameWeekStatistics;
import com.fantasy.football.web.api.exception.BadInputException;
import com.fantasy.football.web.api.repository.PlayerGameweekBreakupRepository;
import com.fantasy.football.web.api.repository.PlayerGameweekStatisticsRepository;
import com.fantasy.football.web.api.service.PlayerGameweekScoresService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class PlayerGameweekScoresServiceImpl implements PlayerGameweekScoresService {

    private final PlayerGameweekStatisticsRepository gameweekStatisticsRepository;
    private final PlayerGameweekBreakupRepository gameweekBreakupRepository;
    public static final int DEFAULT_PAGE_SIZE = 5;

    @Override
    public Mono<GameWeekScoreDTO> createGameweekScoreRecord(Mono<GameWeekScoreDTO> gameWeekScoreDTOMono) {
        return gameWeekScoreDTOMono
                .map(this::setRecordIds)
                .zipWhen(
                        createRequestBody -> gameweekStatisticsRepository.save(createRequestBody.getGameWeekStatistics())
                        , (createBody, savedRecord) -> {
                            createBody.setGameWeekStatistics(savedRecord);
                            return createBody;
                        }
                )
                .doOnNext(createRequestBody -> log.info("Saved Game week statistics record with ID {} for player ID {}", createRequestBody.getGameWeekStatistics().getRecordId(), createRequestBody.getGameWeekStatistics().getPlayerId()))
                .zipWhen(
                        createRequestBody -> gameweekBreakupRepository.saveAll(createRequestBody.getGameWeekBreakUp()).doOnNext(savedRecord -> log.info("Saved game week breakup record")).collectList()
                        , (createRequestBody, savedRecord) -> {
                            createRequestBody.setGameWeekBreakUp(savedRecord);
                            return createRequestBody;
                        }
                )
                .doOnNext(createRequestBody -> log.info("Saved all game week breakup records for player ID {} and gameweek statistics record ID {}", createRequestBody.getGameWeekStatistics().getPlayerId(), createRequestBody.getGameWeekStatistics().getRecordId()));
    }

    @Override
    public Mono<Void> deleteGameweekRecordsForPlayer(UUID recordId, UUID playerId) {
        Flux<PlayerGameWeekStatistics> recordToBeDeletedFlux;
        if (recordId != null)
            recordToBeDeletedFlux = gameweekStatisticsRepository.findById(recordId).flux();
        else
            recordToBeDeletedFlux = gameweekStatisticsRepository.findByPlayerId(playerId, Pageable.unpaged(Sort.by(Sort.Order.desc("gameWeek"))));
        return recordToBeDeletedFlux
                .flatMap(
                        recordToBeDeleted ->
                                gameweekBreakupRepository.deleteByGameWeek(recordToBeDeleted.getRecordId())
                                        .doOnNext(noOfRecordsDeleted -> log.info("Deleted {} number of game week breakup records", noOfRecordsDeleted))
                                        .thenReturn(recordToBeDeleted)
                                        .flux()
                )
                .flatMap(gameweekStatisticsRepository::delete)
                .doOnComplete(() -> log.info("Successfully deleted all gameweek stats and breakup records for recordId {} and playerId {}", recordId, playerId))
                .then();
    }

    @Override
    public Flux<GameWeekScoreDTO> fetchPlayerGameweekScores(UUID gameweekStatsRecordId, UUID playerId, Integer gameweek, Integer pageNumber) {
        Flux<GameWeekScoreDTO> gameWeekScoreDTOFlux;
        if (gameweekStatsRecordId != null)
            gameWeekScoreDTOFlux = gameweekStatisticsRepository.findById(gameweekStatsRecordId)
                    .zipWhen(
                            fetchedGameweekStatsRecord -> gameweekBreakupRepository.findByGameWeek(fetchedGameweekStatsRecord.getRecordId()).collectList(),
                            (statsRecords, breakupRecords) -> new GameWeekScoreDTO().gameWeekStatistics(statsRecords).gameWeekBreakUp(breakupRecords)
                    )
                    .flux();
        else if (playerId != null) {
            Flux<PlayerGameWeekStatistics> gameWeekStatisticsFlux;
            if (gameweek != null)
                gameWeekStatisticsFlux = gameweekStatisticsRepository.findByPlayerIdAndGameWeek(playerId, gameweek, PageRequest.of(pageNumber - 1, DEFAULT_PAGE_SIZE, Sort.by(Sort.Order.desc("gameWeek"))));
            else
                gameWeekStatisticsFlux = gameweekStatisticsRepository.findByPlayerId(playerId, PageRequest.of(pageNumber - 1, DEFAULT_PAGE_SIZE, Sort.by(Sort.Order.desc("gameWeek"))));
            gameWeekScoreDTOFlux = gameWeekStatisticsFlux
                    .flatMap(
                            statsRecord -> gameweekBreakupRepository
                                    .findByGameWeek(statsRecord.getRecordId())
                                    .collectList()
                                    .map(breakupRecordsList -> new GameWeekScoreDTO().gameWeekBreakUp(breakupRecordsList).gameWeekStatistics(statsRecord))
                                    .flux()
                    );
        } else
            throw new BadInputException("Request must provide either record_id or player_id header", 400);
        return gameWeekScoreDTOFlux.doFirst(() -> log.info("Fetched Gameweek records successfully for ID {}, playerID {}, gameweek {}, pagenumber {}", gameweekStatsRecordId, playerId, gameweek, pageNumber))
                .switchIfEmpty(Flux.<GameWeekScoreDTO>empty().doFirst(() -> log.info("Did not find any record for ID {}, playerID {}, gameweek {}, pagenumber {}", gameweekStatsRecordId, playerId, gameweek, pageNumber)));
    }

    private GameWeekScoreDTO setRecordIds (GameWeekScoreDTO gameWeekScoreDTO) {
        gameWeekScoreDTO.setGameWeekStatistics(setRecordIdForGamewekStatistics(gameWeekScoreDTO.getGameWeekStatistics()));
        UUID gameWeekStatisticsRecordId = gameWeekScoreDTO.getGameWeekStatistics().getRecordId();
        gameWeekScoreDTO.setGameWeekBreakUp(
                gameWeekScoreDTO.getGameWeekBreakUp().stream()
                        .map(gameWeekBreakupRecords -> setRecordIdForGameweekBreakup(gameWeekBreakupRecords, gameWeekStatisticsRecordId))
                        .toList()
        );
        return gameWeekScoreDTO;
    }

    private PlayerGameWeekStatistics setRecordIdForGamewekStatistics(PlayerGameWeekStatistics createEntity) {
        return new PlayerGameWeekStatistics.Builder()
                .recordId(UUID.randomUUID())
                .playerId(createEntity.getPlayerId())
                .gameWeek(createEntity.getGameWeek())
                .minutes(createEntity.getMinutes())
                .goalsScored(createEntity.getGoalsScored())
                .assists(createEntity.getAssists())
                .cleanSheets(createEntity.getCleanSheets())
                .goalsConceded(createEntity.getGoalsConceded())
                .ownGoals(createEntity.getOwnGoals())
                .penaltiesSaved(createEntity.getPenaltiesSaved())
                .penaltiesMissed(createEntity.getPenaltiesMissed())
                .yellowCards(createEntity.getYellowCards())
                .redCards(createEntity.getRedCards())
                .saves(createEntity.getSaves())
                .bonus(createEntity.getBonus())
                .bps(createEntity.getBps())
                .influence(createEntity.getInfluence())
                .creativity(createEntity.getCreativity())
                .threat(createEntity.getThreat())
                .ictIndex(createEntity.getIctIndex())
                .starts(createEntity.getStarts())
                .expectedGoals(createEntity.getExpectedGoals())
                .expectedAssists(createEntity.getExpectedAssists())
                .expectedGoalInvolvements(createEntity.getExpectedGoalInvolvements())
                .expectedGoalsConceded(createEntity.getExpectedGoalsConceded())
                .totalPoints(createEntity.getTotalPoints())
                .inDreamTeam(createEntity.isInDreamTeam())
                .build();
    }

    private PlayerGameWeekBreakup setRecordIdForGameweekBreakup(PlayerGameWeekBreakup playerGameWeekBreakup, UUID playerGameweekStatisticsRecordId) {
        return new PlayerGameWeekBreakup(UUID.randomUUID(), playerGameweekStatisticsRecordId, playerGameWeekBreakup.getIdentifier(), playerGameWeekBreakup.getPoints(), playerGameWeekBreakup.getValue());
    }
}