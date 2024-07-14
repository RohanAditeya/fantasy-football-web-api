package com.fantasy.football.web.api.game.repository;

import com.fantasy.football.model.PlayerGameStatistics;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlayerGameStatisticsRepository extends ReactiveCrudRepository<PlayerGameStatistics, UUID>, ReactiveSortingRepository<PlayerGameStatistics, UUID> {
}