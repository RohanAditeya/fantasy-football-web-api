package com.fantasy.football.web.api.repository;

import com.fantasy.football.model.PlayerGameWeekBreakup;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PlayerGameweekBreakupRepository extends ReactiveCrudRepository<PlayerGameWeekBreakup, UUID> {
    Mono<Integer> deleteByGameWeek(UUID gameWeekRecordId);
    Flux<PlayerGameWeekBreakup> findByGameWeek(UUID gameweekRecordId);
}