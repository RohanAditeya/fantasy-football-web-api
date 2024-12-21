package com.fantasy.football.web.api.fantasy.service.impl;

import com.fantasy.football.model.PlayerFantasyStatistics;
import com.fantasy.football.web.api.common.events.PlayerBasicInformationChangedEvents;
import com.fantasy.football.web.api.fantasy.repository.PlayerFantasyStatisticsRepository;
import com.fantasy.football.web.api.fantasy.service.PlayerFantasyStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionContextManager;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
class PlayerFantasyStatisticsServiceImpl implements PlayerFantasyStatisticsService {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final PlayerFantasyStatisticsRepository playerFantasyStatisticsRepository;

    public PlayerFantasyStatisticsServiceImpl(ApplicationEventPublisher applicationEventPublisher, PlayerFantasyStatisticsRepository playerFantasyStatisticsRepository) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.playerFantasyStatisticsRepository = playerFantasyStatisticsRepository;
    }


    @Override
    public Mono<PlayerFantasyStatistics> validateAndSaveLeagueTeam(Mono<PlayerFantasyStatistics> fantasyStatisticsRequest) {
        return fantasyStatisticsRequest
                .doOnNext(request -> request.setRecordId(Optional.ofNullable(request.getRecordId()).orElse(UUID.randomUUID())))
                .flatMap(playerFantasyStatisticsRepository::save)
                .doOnSuccess(savedRecord -> log.info("Player Fantasy Statistics record saved successfully"));
    }

    @ApplicationModuleListener
    Mono<Void> handleSavePlayerBasicInformationRequestEvent(PlayerBasicInformationChangedEvents.PlayerBasicInformationSaveRequested saveRequestedEvent) {
        return this.validateAndSaveLeagueTeam(Mono.just(saveRequestedEvent.createLeaguePlayerRequest().getFantasyStatistics()))
                .map(
                        savedRecord -> {
                            saveRequestedEvent.createLeaguePlayerRequest().setFantasyStatistics(savedRecord);
                            return saveRequestedEvent;
                        }
                )
                .zipWith(TransactionContextManager.currentContext())
                .doOnSuccess(
                        combinedTuple -> {
                            PlayerBasicInformationChangedEvents.PlayerFantasyStatisticsSaved eventPayload = new PlayerBasicInformationChangedEvents.PlayerFantasyStatisticsSaved(combinedTuple.getT1().createLeaguePlayerRequest());
                            PayloadApplicationEvent<?> event = new PayloadApplicationEvent<>(combinedTuple.getT2(), eventPayload);
                            applicationEventPublisher.publishEvent(event);
                        }
                )
                .doOnSuccess(combinedTuple -> log.info("Successfully published event {}", PlayerBasicInformationChangedEvents.PlayerFantasyStatisticsSaved.class.getSimpleName()))
                .then();
    }
}