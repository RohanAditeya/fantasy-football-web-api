package com.fantasy.football.web.api.service.impl;

import com.fantasy.football.model.LeagueTeam;
import com.fantasy.football.model.PlayerBasicInformation;
import com.fantasy.football.web.api.repository.LeaguePlayerInfoRepository;
import com.fantasy.football.web.api.repository.LeagueTeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(value = MockitoExtension.class)
public class LeaguePlayerApiServiceImplTest {

    @InjectMocks
    private LeaguePlayerApiServiceImpl leaguePlayerApiService;
    @Mock
    private LeagueTeamRepository leagueTeamRepository;
    @Mock
    private LeaguePlayerInfoRepository playerInfoRepository;

    @Test
    public void processAndSavePlayerInfoReturnsSuccessForExistingTeam () {
        doReturn(Optional.of(new LeagueTeam.Builder().code(1).name("mock-team").build())).when(leagueTeamRepository).findById(anyInt());
        doReturn(new PlayerBasicInformation.Builder().build()).when(playerInfoRepository).save(any(PlayerBasicInformation.class));
        leaguePlayerApiService.processAndSavePlayerInfo(new PlayerBasicInformation.Builder().teamCode(1).build());
        verify(leagueTeamRepository, times(1)).findById(anyInt());
        verify(playerInfoRepository, times(1)).save(any(PlayerBasicInformation.class));
    }

    @Test
    public void processAndSaveThrowsExceptionIfTeamNotPresent () {
        doReturn(Optional.empty()).when(leagueTeamRepository).findById(anyInt());
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(
                () -> leaguePlayerApiService.processAndSavePlayerInfo(new PlayerBasicInformation.Builder().teamCode(1).webName("mock-name").build())
        ).withMessage("Could not find team with code 1 while saving player mock-name");
    }
}