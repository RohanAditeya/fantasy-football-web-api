package com.fantasy.football.web.api.repository;

import com.fantasy.football.model.PlayerBasicInformation;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface PlayerBasicInformationRepository extends ReactiveCrudRepository<PlayerBasicInformation, UUID> {
    Mono<PlayerBasicInformation> findByCode(Long code);
    Flux<PlayerBasicInformation> findByTeam(UUID teamId);
}