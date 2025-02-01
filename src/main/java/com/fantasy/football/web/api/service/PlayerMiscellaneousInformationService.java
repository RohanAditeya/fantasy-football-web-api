package com.fantasy.football.web.api.service;

import com.fantasy.football.dto.PlayerMiscellaneousInformationPatchDTO;
import com.fantasy.football.model.PlayerMiscellaneousInformation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PlayerMiscellaneousInformationService {

    Mono<PlayerMiscellaneousInformation> validateAndSavePlayerMiscellaneousInformation(Mono<PlayerMiscellaneousInformation> playerMiscellaneousInformation);
    Mono<Void> deleteMiscellaneousInformationRecordById(UUID recordId);
    Flux<PlayerMiscellaneousInformation> fetchPlayerMiscRecord(UUID recordId);
    Mono<PlayerMiscellaneousInformation> updateMiscStatisticsInformation(PlayerMiscellaneousInformationPatchDTO updateDto, UUID recordId);
}