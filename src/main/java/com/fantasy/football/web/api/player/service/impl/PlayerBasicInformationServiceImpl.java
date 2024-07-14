package com.fantasy.football.web.api.player.service.impl;

import com.fantasy.football.dto.CreateLeaguePlayerRequest;
import com.fantasy.football.model.PlayerBasicInformation;
import com.fantasy.football.web.api.events.PlayerBasicInformationChangedEvents;
import com.fantasy.football.web.api.player.repository.PlayerBasicInformationRepository;
import com.fantasy.football.web.api.player.service.PlayerBasicInformationService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionContextManager;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
class PlayerBasicInformationServiceImpl implements PlayerBasicInformationService {

    private final PlayerBasicInformationRepository playerBasicInformationRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    PlayerBasicInformationServiceImpl(PlayerBasicInformationRepository playerBasicInformationRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.playerBasicInformationRepository = playerBasicInformationRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    @Transactional
    public Mono<PlayerBasicInformation> createPlayerRecord(Mono<CreateLeaguePlayerRequest> createLeaguePlayerRequest) {
        return createLeaguePlayerRequest.doOnNext(createPlayerRequestDto -> {
                    UUID fantasyStatisticsGeneratedId = UUID.randomUUID();
                    UUID gameStatisticsGeneratedId = UUID.randomUUID();
                    UUID miscellaneousInformationGeneratedId = UUID.randomUUID();

                    createPlayerRequestDto.getBasicInfo().setRecordId(UUID.randomUUID());
                    createPlayerRequestDto.getBasicInfo().setPlayerFantasyStatistics(fantasyStatisticsGeneratedId);
                    createPlayerRequestDto.getBasicInfo().setPlayerGameStatistics(gameStatisticsGeneratedId);
                    createPlayerRequestDto.getBasicInfo().setPlayerMiscellaneousInformation(miscellaneousInformationGeneratedId);

                    createPlayerRequestDto.getFantasyStatistics().setRecordId(fantasyStatisticsGeneratedId);
                    createPlayerRequestDto.getGameStatistics().setRecordId(gameStatisticsGeneratedId);
                    createPlayerRequestDto.getMiscellaneousInfo().setRecordId(miscellaneousInformationGeneratedId);
                })
                .zipWith(TransactionContextManager.currentContext())
                .map(
                        (combinedTuple) -> {
                            PlayerBasicInformationChangedEvents.PlayerBasicInformationSaveRequested eventPayload = new PlayerBasicInformationChangedEvents.PlayerBasicInformationSaveRequested(combinedTuple.getT1());
                            PayloadApplicationEvent<?> event = new PayloadApplicationEvent<>(combinedTuple.getT2(), eventPayload);
                            applicationEventPublisher.publishEvent(event);
                            return combinedTuple.getT1().getBasicInfo();
                        }
                )
                .doOnSuccess(basicInfoRecord -> log.info("Successfully Published event {}", PlayerBasicInformationChangedEvents.PlayerBasicInformationSaveRequested.class.getSimpleName()));
    }

    @ApplicationModuleListener
    Mono<Void> handlePlayerAuxiliaryDataSavedEvent(PlayerBasicInformationChangedEvents.PlayerMiscellaneousInformationSaved event) {
        return Mono.just(event).map(PlayerBasicInformationChangedEvents.PlayerMiscellaneousInformationSaved::createLeaguePlayerRequest)
                .map(CreateLeaguePlayerRequest::getBasicInfo)
                .doOnNext(request -> log.info("Saving player basic info record after consuming event {}", PlayerBasicInformationChangedEvents.PlayerMiscellaneousInformationSaved.class.getSimpleName()))
                .flatMap(playerBasicInformationRepository::save)
                .doOnNext(request -> log.info("Saved player basic info record after consuming event {}", PlayerBasicInformationChangedEvents.PlayerMiscellaneousInformationSaved.class.getSimpleName()))
                .then();
    }
}