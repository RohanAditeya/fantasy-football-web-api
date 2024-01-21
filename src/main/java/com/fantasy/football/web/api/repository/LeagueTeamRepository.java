package com.fantasy.football.web.api.repository;

import com.fantasy.football.model.LeagueTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeagueTeamRepository extends JpaRepository<LeagueTeam, Integer> {
}