package com.fantasy.football.web.api.leagueTeam.controller;

import com.fantasy.football.model.LeagueTeam;
import com.fantasy.football.servlet.controller.LeagueTeamApi;
import com.fantasy.football.web.api.leagueTeam.service.LeagueTeamApiService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
public class LeagueTeamApiController implements LeagueTeamApi {

	private final LeagueTeamApiService leagueTeamApiService;

	public LeagueTeamApiController(LeagueTeamApiService leagueTeamApiService) {
		this.leagueTeamApiService = leagueTeamApiService;
	}

	@Override
	public Mono<ResponseEntity<Void>> createLeagueTeam(@Valid Mono<LeagueTeam> body, ServerWebExchange exchange) {
		log.info("Received request to add league team");
		return leagueTeamApiService.validateAndSaveLeagueTeam(body).map(voidElement -> ResponseEntity.status(HttpStatus.CREATED).build());
	}
}