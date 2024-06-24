package com.fantasy.football.web.api.leagueTeam.service;

import com.fantasy.football.model.LeagueTeam;
import reactor.core.publisher.Mono;

public interface LeagueTeamApiService {

	Mono<Void> validateAndSaveLeagueTeam(Mono<LeagueTeam> requestBody);
}