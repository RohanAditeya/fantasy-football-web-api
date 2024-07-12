package com.fantasy.football.web.api.leagueteam.service;

import com.fantasy.football.model.LeagueTeam;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface LeagueTeamApiService {

	Mono<LeagueTeam> validateAndSaveLeagueTeam(Mono<LeagueTeam> requestBody);
	Mono<Void> deleteLeagueTeamRecord(String teamName, Integer teamCode, UUID recordId);
	Mono<LeagueTeam> updateLeagueTeamRecord(String teamName, Integer teamCode, UUID recordId);
	Flux<LeagueTeam>
}