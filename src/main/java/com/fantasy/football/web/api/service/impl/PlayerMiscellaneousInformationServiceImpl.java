package com.fantasy.football.web.api.service.impl;

import com.fantasy.football.model.PlayerMiscellaneousInformation;
import com.fantasy.football.web.api.repository.PlayerMiscellaneousInformationRepository;
import com.fantasy.football.web.api.service.PlayerMiscellaneousInformationService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class PlayerMiscellaneousInformationServiceImpl implements PlayerMiscellaneousInformationService {

    private final PlayerMiscellaneousInformationRepository playerMiscellaneousInformationRepository;

    @Override
    public Mono<PlayerMiscellaneousInformation> validateAndSavePlayerMiscellaneousInformation(Mono<PlayerMiscellaneousInformation> playerMiscellaneousInformation) {
        return playerMiscellaneousInformation
                .flatMap(playerMiscellaneousInformationRepository::save);
    }

    @Override
    public Mono<Void> deleteMiscellaneousInformationRecordById(UUID recordId) {
        return playerMiscellaneousInformationRepository.deleteById(recordId);
    }

    @Override
    public Flux<PlayerMiscellaneousInformation> fetchPlayerMiscRecord(UUID recordId) {
        return playerMiscellaneousInformationRepository.findById(recordId)
                .doOnNext(fetchedRecord -> log.info("Fetched player fantasy statistics record with ID {}", recordId))
                .flux().switchIfEmpty(Flux.<PlayerMiscellaneousInformation>empty().doOnComplete(() -> log.info("Found no player fantasy statistics record with ID {}", recordId)));
    }
}