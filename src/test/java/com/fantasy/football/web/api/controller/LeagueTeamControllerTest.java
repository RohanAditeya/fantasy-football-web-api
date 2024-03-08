package com.fantasy.football.web.api.controller;

import com.fantasy.football.model.LeagueTeam;
import com.fantasy.football.web.api.service.LeagueTeamService;
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
public class LeagueTeamControllerTest {

    @InjectMocks
    private LeagueTeamController leagueTeamApiController;
    @Mock
    private LeagueTeamService leagueTeamApiService;

    @Test
    public void createLeagueTeamRecordCallsServiceAndReturnsOkStatusTest () {
        doNothing().when(leagueTeamApiService).validateAndSaveLeagueTeam(any(LeagueTeam.class));
        ResponseEntity<Void> response = leagueTeamApiController.createLeagueTeam(new LeagueTeam.Builder().build());
        assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.CREATED.value());
        verify(leagueTeamApiService, times(1)).validateAndSaveLeagueTeam(any(LeagueTeam.class));
    }
}