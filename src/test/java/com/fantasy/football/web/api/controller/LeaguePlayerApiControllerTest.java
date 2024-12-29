package com.fantasy.football.web.api.controller;

import com.fantasy.football.dto.CreateLeaguePlayerRequest;
import com.fantasy.football.model.*;
import com.fantasy.football.web.api.core.BaseTestExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@ExtendWith(value = OutputCaptureExtension.class)
public class LeaguePlayerApiControllerTest extends BaseTestExtension {

    @Autowired
    private WebTestClient webClient;

    @Test
    @DisplayName(value = "create league player and verify record saved in repository")
    void createLeaguePlayerTest() {
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

    @Test
    @DisplayName(value = "delete league player using record id and record in db is removed")
    void deleteLeaguePlayerWithRecordIdAlreadyExistingTest(CapturedOutput capturedOutput) {
        webClient.delete().uri("/api/fantasy/football/v1/league-player").header("record_id", "87fdc851-a293-4ca8-9a80-abcb367d7d4f")
                .exchange().expectStatus().isNoContent();
        assertThat(capturedOutput).contains("Found record for ID: 87fdc851-a293-4ca8-9a80-abcb367d7d4f and player code: null to delete");
        assertThat(capturedOutput).contains("Deleted record with id 87fdc851-a293-4ca8-9a80-abcb367d7d4f or player code 2000");
        assertThat(capturedOutput).contains("Deleted fantasy statistics record with id e994d032-35aa-48cb-886d-d1bd42b9717e for player 87fdc851-a293-4ca8-9a80-abcb367d7d4f");
        assertThat(capturedOutput).contains("Deleted game statistics record with id 66cc42d8-5afa-4826-8e5f-2d45eac24dff for player 87fdc851-a293-4ca8-9a80-abcb367d7d4f");
        assertThat(capturedOutput).contains("Deleted miscellaneous statistics record with id 32ee66b4-ee3f-4a22-a59a-e9c1fef50109 for player 87fdc851-a293-4ca8-9a80-abcb367d7d4f");
    }

    @Test
    @DisplayName(value = "delete league player using player code and record in db is removed")
    void deleteLeaguePlayerWithPlayerCodeAlreadyExistingTest(CapturedOutput capturedOutput) {
        webClient.delete().uri("/api/fantasy/football/v1/league-player").header("player_code", "3000")
                .exchange().expectStatus().isNoContent();
        assertThat(capturedOutput).contains("Found record for ID: null and player code: 3000 to delete");
        assertThat(capturedOutput).contains("Deleted record with id 0acf5138-f932-482c-91bb-b1f83cbb88a7 or player code 3000");
        assertThat(capturedOutput).contains("Deleted fantasy statistics record with id 58d49d40-b658-4d56-9309-07f0f79ef8ec for player 0acf5138-f932-482c-91bb-b1f83cbb88a7");
        assertThat(capturedOutput).contains("Deleted game statistics record with id 02c2b0d3-8944-469a-90ed-ffc852456259 for player 0acf5138-f932-482c-91bb-b1f83cbb88a7");
        assertThat(capturedOutput).contains("Deleted miscellaneous statistics record with id dcebf1c1-01dc-4d23-a1f4-1d3d9227501f for player 0acf5138-f932-482c-91bb-b1f83cbb88a7");
    }

    @Test
    @DisplayName(value = "delete league player using record id and log is printed saying no record to delete")
    void deleteLeaguePlayerWithRecordIdWithoutRecordTest(CapturedOutput capturedOutput) {
        webClient.delete().uri("/api/fantasy/football/v1/league-player").header("player_code", "3001")
                .exchange().expectStatus().isNoContent();
        assertThat(capturedOutput).contains("No player record found to delete for id null and player code 3001");
    }

    @Test
    @DisplayName(value = "delete league player throws error when both headers are absent")
    void deleteLeaguePlayerWithoutHeadersThrowsErrorTest() {
        webClient.delete().uri("/api/fantasy/football/v1/league-player")
                .exchange().expectStatus().is5xxServerError();
    }
}