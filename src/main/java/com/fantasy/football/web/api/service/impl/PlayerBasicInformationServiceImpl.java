package com.fantasy.football.web.api.service.impl;

import com.fantasy.football.dto.CreateLeaguePlayerRequest;
import com.fantasy.football.model.PlayerBasicInformation;
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