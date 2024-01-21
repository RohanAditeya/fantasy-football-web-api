package com.fantasy.football.web.api.repository;

import com.fantasy.football.model.PlayerBasicInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaguePlayerInfoRepository extends JpaRepository<PlayerBasicInformation, Long> {
}