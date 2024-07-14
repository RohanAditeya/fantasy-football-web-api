package com.fantasy.football.web.api.player.repository;

import com.fantasy.football.model.PlayerBasicInformation;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlayerBasicInformationRepository extends ReactiveCrudRepository<PlayerBasicInformation, UUID>, ReactiveSortingRepository<PlayerBasicInformation, UUID> {
}