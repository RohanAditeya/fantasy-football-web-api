package com.fantasy.football.web.api.leagueteam.repository;

import com.fantasy.football.model.LeagueTeam;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface LeagueTeamRepository extends ReactiveCrudRepository<LeagueTeam, UUID> {
	Mono<Void> deleteByName(String teamName);
	Mono<Void> deleteByCode(int code);
}