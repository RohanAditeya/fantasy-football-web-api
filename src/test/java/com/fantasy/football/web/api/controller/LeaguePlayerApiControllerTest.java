package com.fantasy.football.web.api.controller;

import com.fantasy.football.dto.CreateLeaguePlayerRequest;
import com.fantasy.football.model.*;
import com.fantasy.football.web.api.core.BaseTestExtension;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
public class LeaguePlayerApiControllerTest extends BaseTestExtension {

    @Autowired
    private WebTestClient webClient;

    @Test
    public void createLeaguePlayerTest() {
        PlayerBasicInformation basicInfoRecord = new PlayerBasicInformation.Builder().team(UUID.fromString("75fd0dd0-b829-4a9c-8d06-5eb0dc4347d8")).firstName("Martin").secondName("Odegaard").code(1000L).build();
        PlayerFantasyStatistics fantasyStatistics = new PlayerFantasyStatistics.Builder().build();
        PlayerGameStatistics gameStatistics = new PlayerGameStatistics.Builder().build();
        PlayerMiscellaneousInformation miscellaneousInformation = new PlayerMiscellaneousInformation.Builder().build();

        Flux<PlayerBasicInformation> response = webClient.post().uri("/api/fantasy/football/v1/league-player")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateLeaguePlayerRequest().basicInfo(basicInfoRecord).fantasyStatistics(fantasyStatistics).gameStatistics(gameStatistics).miscellaneousInfo(miscellaneousInformation))
                .exchange().expectStatus().isCreated().returnResult(PlayerBasicInformation.class).getResponseBody();

        StepVerifier.create(response)
                .assertNext(
                        fetchedRecord -> {
                            assertThat(fetchedRecord.getRecordId()).isNotNull();
                            assertThat(fetchedRecord.getPlayerFantasyStatistics()).isNotNull();
                            assertThat(fetchedRecord.getPlayerGameStatistics()).isNotNull();
                            assertThat(fetchedRecord.getPlayerMiscellaneousInformation()).isNotNull();
                        }
                )
                .verifyComplete();
    }
}