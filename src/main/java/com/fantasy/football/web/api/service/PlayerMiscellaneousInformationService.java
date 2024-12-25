package com.fantasy.football.web.api.service;

import com.fantasy.football.model.PlayerMiscellaneousInformation;
import reactor.core.publisher.Mono;

public interface PlayerMiscellaneousInformationService {

    Mono<PlayerMiscellaneousInformation> validateAndSavePlayerMiscellaneousInformation(Mono<PlayerMiscellaneousInformation> playerMiscellaneousInformation);
}