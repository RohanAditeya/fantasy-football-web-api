package com.fantasy.football.web.api.service;

import com.fantasy.football.dto.CreateLeaguePlayerRequest;
import com.fantasy.football.model.PlayerBasicInformation;
import reactor.core.publisher.Mono;

public interface PlayerBasicInformationService {

    Mono<PlayerBasicInformation> validateAndSaveLeaguePlayer(Mono<CreateLeaguePlayerRequest> createLeaguePlayerRequest);
}