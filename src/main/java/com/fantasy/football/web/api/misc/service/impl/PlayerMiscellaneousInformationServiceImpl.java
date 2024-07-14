package com.fantasy.football.web.api.misc.service.impl;

import com.fantasy.football.model.PlayerMiscellaneousInformation;
import com.fantasy.football.web.api.events.PlayerBasicInformationChangedEvents;
import com.fantasy.football.web.api.misc.repository.PlayerMiscellaneousInformationRepository;
import com.fantasy.football.web.api.misc.service.PlayerMiscellaneousInformationService;
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
class PlayerMiscellaneousInformationServiceImpl implements PlayerMiscellaneousInformationService {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final PlayerMiscellaneousInformationRepository playerMiscellaneousInformationRepository;

    PlayerMiscellaneousInformationServiceImpl(ApplicationEventPublisher applicationEventPublisher, PlayerMiscellaneousInformationRepository playerMiscellaneousInformationRepository) {
        this.playerMiscellaneousInformationRepository = playerMiscellaneousInformationRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public Mono<PlayerMiscellaneousInformation> validateAndSaveLeagueTeam(Mono<PlayerMiscellaneousInformation> playerMiscellaneousInformationRequest) {
        return playerMiscellaneousInformationRequest
                .doOnNext(request -> request.setRecordId(Optional.ofNullable(request.getRecordId()).orElse(UUID.randomUUID())))
                .flatMap(playerMiscellaneousInformationRepository::save)
                .doOnSuccess(savedRecord -> log.info("Player Miscellaneous Information record saved successfully"));
    }

    @ApplicationModuleListener
    Mono<Void> handlePlayerGameStatisticsSavedEvent(PlayerBasicInformationChangedEvents.PlayerGameStatisticsSaved gameStatisticsSaved) {
        return this.validateAndSaveLeagueTeam(Mono.just(gameStatisticsSaved.createLeaguePlayerRequest().getMiscellaneousInfo()))
                .map(
                        savedRecord -> {
                            gameStatisticsSaved.createLeaguePlayerRequest().setMiscellaneousInfo(savedRecord);
                            return gameStatisticsSaved;
                        }
                )
                .zipWith(TransactionContextManager.currentContext())
                .doOnSuccess(
                        combinedTuple -> {
                            PlayerBasicInformationChangedEvents.PlayerMiscellaneousInformationSaved eventPayload = new PlayerBasicInformationChangedEvents.PlayerMiscellaneousInformationSaved(combinedTuple.getT1().createLeaguePlayerRequest());
                            PayloadApplicationEvent<?> event = new PayloadApplicationEvent<>(combinedTuple.getT2(), eventPayload);
                            applicationEventPublisher.publishEvent(event);
                        }
                )
                .doOnSuccess(combinedTuple -> log.info("Successfully published event {}", PlayerBasicInformationChangedEvents.PlayerMiscellaneousInformationSaved.class.getSimpleName()))
                .then();
    }
}