package com.fantasy.football.web.api.repository;

import com.fantasy.football.model.PlayerGameWeekBreakup;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface PlayerGameweekBreakupRepository extends ReactiveCrudRepository<PlayerGameWeekBreakup, UUID> {
}