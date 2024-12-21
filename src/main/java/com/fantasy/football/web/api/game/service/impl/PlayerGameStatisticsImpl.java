package com.fantasy.football.web.api.game.service.impl;

import com.fantasy.football.model.PlayerGameStatistics;
import com.fantasy.football.web.api.common.events.PlayerBasicInformationChangedEvents;
import com.fantasy.football.web.api.game.repository.PlayerGameStatisticsRepository;
import com.fantasy.football.web.api.game.service.PlayerGameStatisticsService;
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
class PlayerGameStatisticsImpl implements PlayerGameStatisticsService {

    private final PlayerGameStatisticsRepository playerGameStatisticsRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    PlayerGameStatisticsImpl (PlayerGameStatisticsRepository playerGameStatisticsRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.playerGameStatisticsRepository = playerGameStatisticsRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public Mono<PlayerGameStatistics> validateAndSaveLeagueTeam(Mono<PlayerGameStatistics> playerGameStatistics) {
        return playerGameStatistics
                .doOnNext(request -> request.setRecordId(Optional.ofNullable(request.getRecordId()).orElse(UUID.randomUUID())))
                .flatMap(playerGameStatisticsRepository::save)
                .doOnSuccess(savedRecord -> log.info("Player game statistics record saved successfully"));
    }

    @ApplicationModuleListener
    Mono<Void> handlePlayerFantasyStatisticsSavedEvent (PlayerBasicInformationChangedEvents.PlayerFantasyStatisticsSaved event) {
        return this.validateAndSaveLeagueTeam(Mono.just(event.createLeaguePlayerRequest().getGameStatistics()))
                .map(
                        savedRecord -> {
                            event.createLeaguePlayerRequest().setGameStatistics(savedRecord);
                            return event;
                        }
                )
                .zipWith(TransactionContextManager.currentContext())
                .doOnSuccess(
                        combinedTuple -> {
                            PlayerBasicInformationChangedEvents.PlayerGameStatisticsSaved eventPayload = new PlayerBasicInformationChangedEvents.PlayerGameStatisticsSaved(combinedTuple.getT1().createLeaguePlayerRequest());
                            PayloadApplicationEvent<?> eventToBePublished = new PayloadApplicationEvent<>(combinedTuple.getT2(), eventPayload);
                            applicationEventPublisher.publishEvent(eventToBePublished);
                        }
                )
                .doOnSuccess(combinedTuple -> log.info("Successfully published event {}", PlayerBasicInformationChangedEvents.PlayerGameStatisticsSaved.class.getSimpleName()))
                .then();
    }
}