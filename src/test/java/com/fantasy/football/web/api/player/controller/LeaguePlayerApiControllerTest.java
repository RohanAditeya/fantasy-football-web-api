package com.fantasy.football.web.api.player.controller;

import com.fantasy.football.dto.CreateLeaguePlayerRequest;
import com.fantasy.football.model.PlayerBasicInformation;
import com.fantasy.football.model.PlayerFantasyStatistics;
import com.fantasy.football.model.PlayerGameStatistics;
import com.fantasy.football.model.PlayerMiscellaneousInformation;
import com.fantasy.football.web.api.common.events.PlayerBasicInformationChangedEvents;
import com.fantasy.football.web.api.core.BaseTestExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@ApplicationModuleTest
public class LeaguePlayerApiControllerTest extends BaseTestExtension {

    @Autowired
    private WebTestClient webClient;

    @Test
    @DisplayName(value = "module test to verify if create player request publishes save requested event")
    public void createLeaguePlayerTest(Scenario scenario) {
        PlayerBasicInformation basicInfoRecord = new PlayerBasicInformation.Builder().team(UUID.fromString("75fd0dd0-b829-4a9c-8d06-5eb0dc4347d8")).firstName("Martin").secondName("Odegaard").code(1000L).build();
        PlayerFantasyStatistics fantasyStatistics = new PlayerFantasyStatistics.Builder().build();
        PlayerGameStatistics gameStatistics = new PlayerGameStatistics.Builder().build();
        PlayerMiscellaneousInformation miscellaneousInformation = new PlayerMiscellaneousInformation.Builder().build();
        scenario.stimulate(
                        () -> {
                            Flux<PlayerBasicInformation> response = webClient.post().uri("/api/fantasy/football/v1/league-player")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(new CreateLeaguePlayerRequest().basicInfo(basicInfoRecord).fantasyStatistics(fantasyStatistics).gameStatistics(gameStatistics).miscellaneousInfo(miscellaneousInformation))
                                    .exchange().expectStatus().isCreated().returnResult(PlayerBasicInformation.class).getResponseBody();
                            StepVerifier.create(response)
                                    .expectNextCount(1).verifyComplete();
                        }
                ).andWaitForEventOfType(PlayerBasicInformationChangedEvents.PlayerBasicInformationSaveRequested.class)
                .matching(
                        saveRequestedEvent ->
                                saveRequestedEvent.createLeaguePlayerRequest() != null
                ).toArriveAndVerify(
                        saveRequestedEvent -> {
                            // Verify if the published event has set the record IDs for the player
                            assertThat(saveRequestedEvent.createLeaguePlayerRequest().getBasicInfo().getRecordId()).isNotNull();
                            assertThat(saveRequestedEvent.createLeaguePlayerRequest().getBasicInfo().getPlayerFantasyStatistics()).isNotNull();
                            assertThat(saveRequestedEvent.createLeaguePlayerRequest().getBasicInfo().getPlayerGameStatistics()).isNotNull();
                            assertThat(saveRequestedEvent.createLeaguePlayerRequest().getBasicInfo().getPlayerMiscellaneousInformation()).isNotNull();
                        }
                );
    }
}