package com.fantasy.football.web.api.leagueTeam.service.impl;

import com.fantasy.football.model.LeagueTeam;
import com.fantasy.football.web.api.leagueTeam.repository.LeagueTeamRepository;
import com.fantasy.football.web.api.leagueTeam.service.LeagueTeamApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class LeagueTeamApiServiceImpl implements LeagueTeamApiService {

	private final LeagueTeamRepository leagueTeamRepository;

	public LeagueTeamApiServiceImpl(LeagueTeamRepository leagueTeamRepository) {
		this.leagueTeamRepository = leagueTeamRepository;
	}

	@Override
	public Mono<Void> validateAndSaveLeagueTeam(Mono<LeagueTeam> requestBody) {
		log.info("Saving league team entity");
		return requestBody.doOnNext(leagueTeamRepository::save).then();
	}
}