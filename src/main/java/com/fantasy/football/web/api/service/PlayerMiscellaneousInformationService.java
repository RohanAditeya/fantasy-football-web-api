package com.fantasy.football.web.api.service;

import com.fantasy.football.model.PlayerMiscellaneousInformation;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PlayerMiscellaneousInformationService {

    Mono<PlayerMiscellaneousInformation> validateAndSavePlayerMiscellaneousInformation(Mono<PlayerMiscellaneousInformation> playerMiscellaneousInformation);
    Mono<Void> deleteMiscellaneousInformationRecordById(UUID recordId);
}