package com.fantasy.football.web.api.controller;

import com.fantasy.football.model.PlayerBasicInformation;
import com.fantasy.football.web.api.service.LeaguePlayerInfoApiService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(value = MockitoExtension.class)
public class LeaguePlayerControllerTest {

    @InjectMocks
    private LeaguePlayerController leaguePlayerController;
    @Mock
    private LeaguePlayerInfoApiService leaguePlayerInfoApiService;

    @Test
    public void createLeaguePlayerSuccessfullyTest () {
        doNothing().when(leaguePlayerInfoApiService).processAndSavePlayerInfo(any(PlayerBasicInformation.class));
        ResponseEntity<Void> response = leaguePlayerController.createLeaguePlayer(new PlayerBasicInformation.Builder().teamCode(1).build());
        assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.CREATED.value());
        verify(leaguePlayerInfoApiService, times(1)).processAndSavePlayerInfo(any(PlayerBasicInformation.class));
    }
}