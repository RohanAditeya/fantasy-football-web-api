package com.fantasy.football.web.api.service.impl;

import com.fantasy.football.model.LeagueTeam;
import com.fantasy.football.model.LeagueTeamPrimaryKey;
import com.fantasy.football.model.PlayerBasicInformation;
import com.fantasy.football.web.api.repository.LeagueTeamRepository;
import com.fantasy.football.web.api.repository.PlayerBasicInformationRepository;
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
public class PlayerServiceTest {

    @InjectMocks
    private PlayerServiceImpl leaguePlayerApiService;
    @Mock
    private LeagueTeamRepository leagueTeamRepository;
    @Mock
    private PlayerBasicInformationRepository playerInfoRepository;

    @Test
    public void processAndSavePlayerInfoReturnsSuccessForExistingTeam () {
        doReturn(Optional.of(new LeagueTeam.Builder().compositeKey(new LeagueTeamPrimaryKey("Arsenal", 1)).build())).when(leagueTeamRepository).findByCompositeKeyCode(anyInt());
        doReturn(new PlayerBasicInformation.Builder().build()).when(playerInfoRepository).save(any(PlayerBasicInformation.class));
        leaguePlayerApiService.validateAndSavePlayer(new PlayerBasicInformation.Builder().team(new LeagueTeam.Builder().compositeKey(new LeagueTeamPrimaryKey("Arsenal", 1)).build()).build());
        verify(leagueTeamRepository, times(1)).findByCompositeKeyCode(anyInt());
        verify(playerInfoRepository, times(1)).save(any(PlayerBasicInformation.class));
    }

    @Test
    public void processAndSaveThrowsExceptionIfTeamNotPresent () {
        doReturn(Optional.empty()).when(leagueTeamRepository).findByCompositeKeyCode(anyInt());
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(
                () -> leaguePlayerApiService.validateAndSavePlayer(new PlayerBasicInformation.Builder().team(new LeagueTeam.Builder().compositeKey(new LeagueTeamPrimaryKey("Arsenal", 1)).build()).webName("mock-name").build())
        ).withMessage("Could not find team with code 1 while saving player mock-name");
    }
}