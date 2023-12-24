package com.fantasy.football.web.api.controller.impl;

import com.fantasy.football.model.LeagueTeam;
import com.fantasy.football.web.api.service.LeagueTeamApiService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(value = {MockitoExtension.class})
public class LeagueTeamApiControllerImplTest {

    @InjectMocks
    private LeagueTeamApiControllerImpl leagueTeamApiController;
    @Mock
    private LeagueTeamApiService leagueTeamApiService;

    @ParameterizedTest
    @MethodSource(value = "fetchLeagueTeamTestArguments")
    @DisplayName(value = "fetch_request_returns_proper_status_code_for_optional_returned_from_service")
    public void fetchLeagueTeamRecordReturnStatusTest (Optional<LeagueTeam> returnValue, int statusCode) {
        doReturn(returnValue).when(leagueTeamApiService).fetchAndReturnLeagueTeamRecordByTeamCode(anyInt());
        ResponseEntity<LeagueTeam> response = leagueTeamApiController.fetchLeagueTeamInfo(1);
        assertThat(response.getStatusCode().value()).isEqualTo(statusCode);
        verify(leagueTeamApiService, times(1)).fetchAndReturnLeagueTeamRecordByTeamCode(anyInt());
    }

    @Test
    public void createLeagueTeamRecordCallsServiceAndReturnsOkStatusTest () {
        doNothing().when(leagueTeamApiService).processAndCreateLeagueTeamRecord(any(LeagueTeam.class));
        ResponseEntity<Void> response = leagueTeamApiController.createLeagueTeamRecord(new LeagueTeam());
        assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.CREATED.value());
        verify(leagueTeamApiService, times(1)).processAndCreateLeagueTeamRecord(any(LeagueTeam.class));
    }

    @Test
    public void updateLeagueTeamRecordCallsServiceAndReturnsOkStatusTest () {
        doNothing().when(leagueTeamApiService).processAndUpdateLeagueTeamRecord(any(LeagueTeam.class));
        ResponseEntity<Void> response = leagueTeamApiController.updateLeagueTeamRecord(new LeagueTeam());
        assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.NO_CONTENT.value());
        verify(leagueTeamApiService, times(1)).processAndUpdateLeagueTeamRecord(any(LeagueTeam.class));
    }

    @Test
    public void deleteLeagueTeamRecordCallsServiceAndReturnsOkStatusTest () {
        doNothing().when(leagueTeamApiService).processAndDeleteLeagueTeamRecord(anyInt());
        ResponseEntity<Void> response = leagueTeamApiController.deleteLeagueTeamRecord(1);
        assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.NO_CONTENT.value());
        verify(leagueTeamApiService, times(1)).processAndDeleteLeagueTeamRecord(anyInt());
    }

    public static Stream<? extends Arguments> fetchLeagueTeamTestArguments () {
        return Stream.of(
                Arguments.of(Optional.empty(), HttpStatus.NOT_FOUND.value()),
                Arguments.of(Optional.of(new LeagueTeam()), HttpStatus.OK.value())
        );
    }
}