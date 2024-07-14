package com.fantasy.football.web.api.misc.service;

import com.fantasy.football.model.PlayerMiscellaneousInformation;
import reactor.core.publisher.Mono;

public interface PlayerMiscellaneousInformationService {
    Mono<PlayerMiscellaneousInformation> validateAndSaveLeagueTeam(Mono<PlayerMiscellaneousInformation> playerMiscellaneousInformationRequest);
}