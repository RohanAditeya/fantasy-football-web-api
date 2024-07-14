package com.fantasy.football.web.api.events;

import com.fantasy.football.dto.CreateLeaguePlayerRequest;

public class PlayerBasicInformationChangedEvents {

    public record PlayerBasicInformationSaveRequested(CreateLeaguePlayerRequest createLeaguePlayerRequest) {
    }

    ;

    public record PlayerFantasyStatisticsSaved(CreateLeaguePlayerRequest createLeaguePlayerRequest) {
    }

    ;

    public record PlayerGameStatisticsSaved(CreateLeaguePlayerRequest createLeaguePlayerRequest) {
    }

    ;

    public record PlayerMiscellaneousInformationSaved(CreateLeaguePlayerRequest createLeaguePlayerRequest) {
    }

    ;
}