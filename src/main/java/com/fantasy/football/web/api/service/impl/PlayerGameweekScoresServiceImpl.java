package com.fantasy.football.web.api.service.impl;

import com.fantasy.football.dto.GameWeekScoreDTO;
import com.fantasy.football.model.PlayerGameWeekBreakup;
import com.fantasy.football.model.PlayerGameWeekStatistics;
import com.fantasy.football.web.api.repository.PlayerGameweekBreakupRepository;
import com.fantasy.football.web.api.repository.PlayerGameweekStatisticsRepository;
import com.fantasy.football.web.api.service.PlayerGameweekScoresService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class PlayerGameweekScoresServiceImpl implements PlayerGameweekScoresService {

    private final PlayerGameweekStatisticsRepository gameweekStatisticsRepository;
    private final PlayerGameweekBreakupRepository gameweekBreakupRepository;

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