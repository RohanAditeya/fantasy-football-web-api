package com.fantasy.football.web.api.repository;

import com.fantasy.football.model.PlayerBasicInformation;
import com.fantasy.football.model.PlayerBasicInformationPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerBasicInformationRepository extends JpaRepository<PlayerBasicInformation, PlayerBasicInformationPrimaryKey> {
}