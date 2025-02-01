package com.fantasy.football.web.api.repository;

import com.fantasy.football.model.PlayerGameWeekStatistics;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PlayerGameweekStatisticsRepository extends ReactiveSortingRepository<PlayerGameWeekStatistics, UUID>, ReactiveCrudRepository<PlayerGameWeekStatistics, UUID> {
    Flux<PlayerGameWeekStatistics> findByPlayerId(UUID playerId, Pageable pageable);
    Flux<PlayerGameWeekStatistics> findByPlayerIdAndGameWeek(UUID playerId, Integer gameweek, Pageable pageable);
}