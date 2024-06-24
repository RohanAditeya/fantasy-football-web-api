package com.fantasy.football.web.api.leagueTeam.repository;

import com.fantasy.football.model.LeagueTeam;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LeagueTeamRepository extends ReactiveCrudRepository<LeagueTeam, UUID> {
}