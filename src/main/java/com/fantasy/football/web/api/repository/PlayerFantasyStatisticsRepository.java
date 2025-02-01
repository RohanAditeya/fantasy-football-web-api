package com.fantasy.football.web.api.repository;

import com.fantasy.football.model.PlayerFantasyStatistics;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlayerFantasyStatisticsRepository extends ReactiveCrudRepository<PlayerFantasyStatistics, UUID> {
}