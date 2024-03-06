package com.fantasy.football.web.api.service.impl;

import com.fantasy.football.model.LeagueTeam;
import com.fantasy.football.model.LeagueTeamPrimaryKey;
import com.fantasy.football.web.api.repository.LeagueTeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(value = MockitoExtension.class)
public class LeagueTeamApiServiceImplTest {

    @InjectMocks
    private LeagueTeamServiceImpl leagueTeamApiService;
    @Mock
    private LeagueTeamRepository leagueTeamRepository;

    @Test
    public void processAndCreateLeagueTeamRecordCallsSaveAfterInitializingPrimaryKeyTest () {
        ArgumentCaptor<LeagueTeam> saveCallCaptor = ArgumentCaptor.forClass(LeagueTeam.class);
        doReturn(new LeagueTeam.Builder().build()).when(leagueTeamRepository).save(saveCallCaptor.capture());
        LeagueTeam mockLeagueTeam = new LeagueTeam.Builder().compositeKey(new LeagueTeamPrimaryKey("Arsenal", 1)).build();
        leagueTeamApiService.validateAndSaveLeagueTeam(mockLeagueTeam);
        verify(leagueTeamRepository, times(1)).save(any(LeagueTeam.class));
    }
}