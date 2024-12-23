package com.fantasy.football.web.api.service;

import com.fantasy.football.dto.LeagueTeamPatchDto;
import com.fantasy.football.model.LeagueTeam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface LeagueTeamApiService {

    Mono<LeagueTeam> validateAndSaveLeagueTeam(Mono<LeagueTeam> requestBody);

    Mono<Void> deleteLeagueTeamRecord(String teamName, Integer teamCode, UUID recordId);

    Mono<LeagueTeam> updateLeagueTeamRecord(String teamName, Integer teamCode, UUID recordId, Mono<LeagueTeamPatchDto> leagueTeamPatchDto);

    Flux<LeagueTeam> fetchLeagueTeamRecords(String teamName, Integer teamCode, UUID recordId, Integer pageNumber, Integer pageSize);
}