package com.fantasy.football.web.api.service.impl;

import com.fantasy.football.model.LeagueTeam;
import com.fantasy.football.web.api.repository.LeagueTeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(value = MockitoExtension.class)
public class LeagueTeamApiServiceImplTest {

    @InjectMocks
    private LeagueTeamApiServiceImpl leagueTeamApiService;
    @Mock
    private LeagueTeamRepository leagueTeamRepository;

    @Test
    public void fetchAndReturnLeagueTeamRecordByTeamCodeReturnsNonEmptyOptionalTest () {
        doReturn(Optional.of(new LeagueTeam())).when(leagueTeamRepository).findById(anyInt());
        Optional<LeagueTeam> record = leagueTeamApiService.fetchAndReturnLeagueTeamRecordByTeamCode(1);
        assertThat(record.isEmpty()).isEqualTo(false);
        verify(leagueTeamRepository, times(1)).findById(anyInt());
    }

    @Test
    public void processAndCreateLeagueTeamRecordCallsSaveAfterInitializingPrimaryKeyTest () {
        ArgumentCaptor<LeagueTeam> saveCallCaptor = ArgumentCaptor.forClass(LeagueTeam.class);
        doReturn(new LeagueTeam()).when(leagueTeamRepository).save(saveCallCaptor.capture());
        LeagueTeam mockLeagueTeam = new LeagueTeam();
        mockLeagueTeam.setCode(1);
        mockLeagueTeam.setName("mock-team");
        leagueTeamApiService.processAndCreateLeagueTeamRecord(mockLeagueTeam);
        verify(leagueTeamRepository, times(1)).save(any(LeagueTeam.class));
        assertThat(saveCallCaptor.getValue().getTeamKey().getCode()).isEqualTo(mockLeagueTeam.getCode());
        assertThat(saveCallCaptor.getValue().getTeamKey().getName()).isEqualTo(mockLeagueTeam.getName());
    }

    @Test
    public void processAndUpdateLeagueTeamRecordCallsSaveTest () {
        doReturn(new LeagueTeam()).when(leagueTeamRepository).save(any(LeagueTeam.class));
        leagueTeamApiService.processAndUpdateLeagueTeamRecord(new LeagueTeam());
        verify(leagueTeamRepository, times(1)).save(any(LeagueTeam.class));
    }

    @Test
    public void processAndDeleteLeagueTeamRecordCallsDeleteByIdTest () {
        doNothing().when(leagueTeamRepository).deleteById(anyInt());
        leagueTeamApiService.processAndDeleteLeagueTeamRecord(1);
        verify(leagueTeamRepository, times(1)).deleteById(anyInt());
    }
}