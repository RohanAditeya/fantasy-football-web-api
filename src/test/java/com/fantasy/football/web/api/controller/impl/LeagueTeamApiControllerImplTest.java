package com.fantasy.football.web.api.controller.impl;

import com.fantasy.football.model.LeagueTeam;
import com.fantasy.football.web.api.service.LeagueTeamApiService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(value = {MockitoExtension.class})
public class LeagueTeamApiControllerImplTest {

    @InjectMocks
    private LeagueTeamApiControllerImpl leagueTeamApiController;
    @Mock
    private LeagueTeamApiService leagueTeamApiService;

    @Test
    public void createLeagueTeamRecordCallsServiceAndReturnsOkStatusTest () {
        doNothing().when(leagueTeamApiService).processAndCreateLeagueTeamRecord(any(LeagueTeam.class));
        ResponseEntity<Void> response = leagueTeamApiController.createLeagueTeamRecord(new LeagueTeam.Builder().build());
        assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.CREATED.value());
        verify(leagueTeamApiService, times(1)).processAndCreateLeagueTeamRecord(any(LeagueTeam.class));
    }
}