package com.fantasy.football.web.api.controller;

import com.fantasy.football.dto.LeagueTeamPatchDto;
import com.fantasy.football.model.LeagueTeam;
import com.fantasy.football.reactive.controller.LeagueTeamApi;
import com.fantasy.football.web.api.service.LeagueTeamApiService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class LeagueTeamApiController implements LeagueTeamApi {

    private final LeagueTeamApiService leagueTeamApiService;

    @Override
    public Mono<ResponseEntity<LeagueTeam>> createLeagueTeam(@Valid Mono<LeagueTeam> body, ServerWebExchange exchange) {
        log.info("Received request to save league team record");
        return leagueTeamApiService.validateAndSaveLeagueTeam(body).map(savedRecord -> ResponseEntity.status(HttpStatus.CREATED).body(savedRecord));
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteLeagueTeam(String teamName, Integer teamCode, UUID recordId, ServerWebExchange exchange) {
        // TODO Return 400 when no headers are provided
        log.info("Received request to delete league team record");
        return leagueTeamApiService.deleteLeagueTeamRecord(teamName, teamCode, recordId).then(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).build()));
    }

    @Override
    public Mono<ResponseEntity<Flux<LeagueTeam>>> fetchLeagueTeam(String teamName, Integer teamCode, UUID recordId, @Valid Integer pageNumber, @Valid Integer pageSize, ServerWebExchange exchange) {
        // TODO return 404 when no record is found
        // TODO return 400 when no headers are provided
        log.info("Received request to fetch league team record");
        return Mono.just(ResponseEntity.ok(leagueTeamApiService.fetchLeagueTeamRecords(teamName, teamCode, recordId, pageNumber, pageSize)));
    }

    @Override
    public Mono<ResponseEntity<LeagueTeam>> updateLeagueTeam(@Valid Mono<LeagueTeamPatchDto> leagueTeamPatchDto, String teamName, Integer teamCode, UUID recordId, ServerWebExchange exchange) {
        // TODO return 400 when no headers are provided.
        // TODO return 404 when record to update is not found.
        log.info("Received request to update league team record");
        return leagueTeamApiService.updateLeagueTeamRecord(teamName, teamCode, recordId, leagueTeamPatchDto)
                .map(updatedLeagueTeam -> ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedLeagueTeam));
    }
}