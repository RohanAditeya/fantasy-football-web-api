package com.fantasy.football.web.api.service;

import com.fantasy.football.model.PlayerBasicInformation;

public interface LeaguePlayerInfoApiService {
    void processAndSavePlayerInfo (PlayerBasicInformation playerBasicInformationRecord);
}