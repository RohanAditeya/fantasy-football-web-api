package com.fantasy.football.web.api.player.service;

import com.fantasy.football.dto.CreateLeaguePlayerRequest;
import com.fantasy.football.model.PlayerBasicInformation;
import reactor.core.publisher.Mono;

public interface PlayerBasicInformationService {

    Mono<PlayerBasicInformation> createPlayerRecord(Mono<CreateLeaguePlayerRequest> createLeaguePlayerRequest);
}