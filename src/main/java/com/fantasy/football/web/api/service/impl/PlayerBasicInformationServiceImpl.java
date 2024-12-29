package com.fantasy.football.web.api.service.impl;

import com.fantasy.football.dto.CreateLeaguePlayerRequest;
import com.fantasy.football.model.PlayerBasicInformation;
import com.fantasy.football.web.api.exception.BadInputException;
import com.fantasy.football.web.api.repository.PlayerBasicInformationRepository;
import com.fantasy.football.web.api.service.PlayerBasicInformationService;
import com.fantasy.football.web.api.service.PlayerFantasyStatisticsService;
import com.fantasy.football.web.api.service.PlayerGameStatisticsService;
import com.fantasy.football.web.api.service.PlayerMiscellaneousInformationService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class PlayerBasicInformationServiceImpl implements PlayerBasicInformationService {

    private final PlayerBasicInformationRepository playerBasicInformationRepository;
    private final PlayerFantasyStatisticsService playerFantasyStatisticsService;
    private final PlayerGameStatisticsService playerGameStatisticsService;
    private final PlayerMiscellaneousInformationService playerMiscellaneousInformationService;

    @Override
    public Mono<PlayerBasicInformation> validateAndSaveLeaguePlayer(Mono<CreateLeaguePlayerRequest> createLeaguePlayerRequest) {
        return createLeaguePlayerRequest
                .doOnNext(this::assignRecordIdsForEntities)
                .zipWhen(request -> playerFantasyStatisticsService.validateAndSavePlayerFantasyStatistics(Mono.just(request.getFantasyStatistics())), (request, fantasyStatistics) -> request)
                .doOnSuccess(createRequest -> log.info("Saved player fantasy statistics with record id {} for player {}", createRequest.getFantasyStatistics().getRecordId(), createRequest.getBasicInfo().getRecordId()))
                .zipWhen(request -> playerGameStatisticsService.validateAndSavePlayerGameStatistics(Mono.just(request.getGameStatistics())), (request, gameStatistics) -> request)
                .doOnSuccess(createRequest -> log.info("Saved player game statistics with record id {} for player {}", createRequest.getGameStatistics().getRecordId(), createRequest.getBasicInfo().getRecordId()))
                .zipWhen(request -> playerMiscellaneousInformationService.validateAndSavePlayerMiscellaneousInformation(Mono.just(request.getMiscellaneousInfo())), (request, miscellaneousInformation) -> request)
                .doOnSuccess(createRequest -> log.info("Saved player miscellaneous statistics with record id {} for player {}", createRequest.getMiscellaneousInfo().getRecordId(), createRequest.getBasicInfo().getRecordId()))
                .flatMap(request -> playerBasicInformationRepository.save(request.getBasicInfo()));
    }

    @Override
    public Mono<Void> deleteLeaguePlayerBasicInfoRecord(UUID recordId, Long playerCode) {
        Mono<PlayerBasicInformation> recordToBeDeletedMono = Optional.ofNullable(recordId).map(playerBasicInformationRepository::findById)
                .or(() -> Optional.ofNullable(playerCode).map(playerBasicInformationRepository::findByCode)).orElseThrow(() -> new BadInputException("Request must provide either record_id or player_code header", 404));
        return recordToBeDeletedMono
                .doOnNext(recordToBeDeleted -> log.info("Found record for ID: {} and player code: {} to delete", recordId, playerCode))
                .flatMap(recordToBeDeleted -> playerBasicInformationRepository.deleteById(recordToBeDeleted.getRecordId()).thenReturn(recordToBeDeleted))
                .doOnNext(recordToBeDeleted -> log.info("Deleted record with id {} or player code {}", recordToBeDeleted.getRecordId(), recordToBeDeleted.getCode()))
                .flatMap(recordToBeDeleted -> playerFantasyStatisticsService.deleteFantasyStatisticsRecordByRecordId(recordToBeDeleted.getPlayerFantasyStatistics()).thenReturn(recordToBeDeleted))
                .doOnNext(recordToBeDeleted -> log.info("Deleted fantasy statistics record with id {} for player {}", recordToBeDeleted.getPlayerFantasyStatistics(), recordToBeDeleted.getRecordId()))
                .flatMap(recordToBeDeleted -> playerGameStatisticsService.deleteGameStatisticsRecordById(recordToBeDeleted.getPlayerGameStatistics()).thenReturn(recordToBeDeleted))
                .doOnNext(recordToBeDeleted -> log.info("Deleted game statistics record with id {} for player {}", recordToBeDeleted.getPlayerGameStatistics(), recordToBeDeleted.getRecordId()))
                .flatMap(recordToBeDeleted -> playerMiscellaneousInformationService.deleteMiscellaneousInformationRecordById(recordToBeDeleted.getPlayerMiscellaneousInformation()).thenReturn(recordToBeDeleted))
                .doOnNext(recordToBeDeleted -> log.info("Deleted miscellaneous statistics record with id {} for player {}", recordToBeDeleted.getPlayerMiscellaneousInformation(), recordToBeDeleted.getRecordId()))
                .switchIfEmpty(Mono.<PlayerBasicInformation>empty().doOnSuccess(voidType -> log.info("No player record found to delete for id {} and player code {}", recordId, playerCode)))
                .then();
    }

    private void assignRecordIdsForEntities(CreateLeaguePlayerRequest createLeaguePlayerRequest) {
        UUID fantasyStatisticsRecordId = UUID.randomUUID();
        UUID gameStatisticsRecordId = UUID.randomUUID();
        UUID miscellaneousRecordId = UUID.randomUUID();

        createLeaguePlayerRequest.getBasicInfo().setRecordId(UUID.randomUUID());
        createLeaguePlayerRequest.getBasicInfo().setPlayerFantasyStatistics(fantasyStatisticsRecordId);
        createLeaguePlayerRequest.getBasicInfo().setPlayerGameStatistics(gameStatisticsRecordId);
        createLeaguePlayerRequest.getBasicInfo().setPlayerMiscellaneousInformation(miscellaneousRecordId);

        createLeaguePlayerRequest.getFantasyStatistics().setRecordId(fantasyStatisticsRecordId);
        createLeaguePlayerRequest.getGameStatistics().setRecordId(gameStatisticsRecordId);
        createLeaguePlayerRequest.getMiscellaneousInfo().setRecordId(miscellaneousRecordId);
    }
}