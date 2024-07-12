package com.fantasy.football.web.api.leagueteam.service.impl;

import com.fantasy.football.model.LeagueTeam;
import com.fantasy.football.web.api.exception.BadInputException;
import com.fantasy.football.web.api.leagueteam.repository.LeagueTeamRepository;
import com.fantasy.football.web.api.leagueteam.service.LeagueTeamApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
public class LeagueTeamApiServiceImpl implements LeagueTeamApiService {

	private final LeagueTeamRepository leagueTeamRepository;

	public LeagueTeamApiServiceImpl(LeagueTeamRepository leagueTeamRepository) {
		this.leagueTeamRepository = leagueTeamRepository;
	}

	@Override
	public Mono<LeagueTeam> validateAndSaveLeagueTeam(Mono<LeagueTeam> requestBody) {
		log.info("Saving league team entity");
		return requestBody.map(leagueTeamRecord -> {
			leagueTeamRecord.setRecordId(UUID.randomUUID());
			return leagueTeamRecord;
		}).flatMap(leagueTeamRepository::save);
	}

	@Override
	public Mono<Void> deleteLeagueTeamRecord(String teamName, Integer teamCode, UUID recordId) {
		if (recordId != null)
			return leagueTeamRepository.deleteById(recordId);
		else if (teamCode != null)
			return leagueTeamRepository.deleteByCode(teamCode);
		else if (teamName != null)
			return leagueTeamRepository.deleteByName(teamName);
		else
			throw new BadInputException("Request needs to provided with at least one of [teamName, teamCode, recordId] headers", HttpStatus.BAD_REQUEST.value());
	}

	@Override
	public Mono<LeagueTeam> updateLeagueTeamRecord(String teamName, Integer teamCode, UUID recordId) {
		return null;
	}
}