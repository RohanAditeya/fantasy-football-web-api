package com.fantasy.football.web.api.repository;

import com.fantasy.football.model.PlayerGameWeekStatistics;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface PlayerGameweekStatisticsRepository extends ReactiveCrudRepository<PlayerGameWeekStatistics, UUID> {
}