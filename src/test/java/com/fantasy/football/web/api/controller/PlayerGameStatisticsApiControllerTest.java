package com.fantasy.football.web.api.controller;

import com.fantasy.football.model.PlayerGameStatistics;
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
public class PlayerGameStatisticsApiControllerTest extends BaseTestExtension {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName(value = "POST request must save the record properly")
    void createPlayerGameStatisticsRequest(CapturedOutput capturedOutput) {
        Flux<PlayerGameStatistics> response = webTestClient.post().uri("/api/fantasy/football/v1/league-player-game-statistics")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PlayerGameStatistics.Builder().build())
                .exchange().expectStatus().isCreated()
                .returnResult(PlayerGameStatistics.class).getResponseBody();
        StepVerifier.create(response)
                .assertNext(
                        savedRecord -> {
                            assertThat(savedRecord.getRecordId()).isNotNull();
                            assertThat(capturedOutput.getAll().contains(String.format("Saved league player game statistics record with ID %s", savedRecord.getRecordId()))).isTrue();
                        }
                ).verifyComplete();
    }

    @Test
    @DisplayName(value = "delete request should delete existing record")
    void deletePlayerFantasyStatisticsRequestDeletesExistingRecordTest(CapturedOutput capturedOutput) {
        webTestClient.delete().uri("/api/fantasy/football/v1/league-player-game-statistics")
                .header("record_id", "e77d38a8-8069-4137-908d-68c55f4c5a96")
                .exchange().expectStatus().isNoContent();
        assertThat(capturedOutput.getAll().contains("Deleted league player game statistics record with ID e77d38a8-8069-4137-908d-68c55f4c5a96")).isTrue();
    }

    @Test
    @DisplayName(value = "delete request returns 500 when record_id header is missing")
    void deleteRequestThrows5xxErrorWhenHeaderIsOmitted() {
        webTestClient.delete().uri("/api/fantasy/football/v1/league-player-game-statistics")
                .exchange().expectStatus().is5xxServerError();
    }

    @Test
    @DisplayName(value = "Fetch game statistics record request returns existing record")
    void fetchExistingRecordIsReturnedTest() {
        Flux<PlayerGameStatistics> response = webTestClient.get().uri("/api/fantasy/football/v1/league-player-game-statistics")
                .header("record_id", "ef6a7ab6-6b98-44ba-b648-a2cc38806126")
                .exchange().expectStatus().isOk().returnResult(PlayerGameStatistics.class).getResponseBody();
        StepVerifier.create(response)
                .assertNext(fetchedRecord -> {
                    assertThat(fetchedRecord.getRecordId()).isEqualByComparingTo(UUID.fromString("ef6a7ab6-6b98-44ba-b648-a2cc38806126"));
                }).verifyComplete();
    }

    @Test
    @DisplayName(value = "Fetch player statistics record request prints log if no record found")
    void fetchNotExistingRecordPrintsLogTest(CapturedOutput capturedOutput) {
        webTestClient.get().uri("/api/fantasy/football/v1/league-player-game-statistics")
                .header("record_id", "ef6a7ab6-6b98-44ba-b648-a2cc38806127")
                .exchange().expectStatus().isOk();
        assertThat(capturedOutput.getAll().contains("Found no player game statistics record with ID ef6a7ab6-6b98-44ba-b648-a2cc38806127")).isTrue();
    }

    @Test
    @DisplayName(value = "Error 500 when record_id header is missing when making fetch game statistics request")
    void fetchRequestThrows5xxErrorWhenHeaderIsOmitted() {
        webTestClient.get().uri("/api/fantasy/football/v1/league-player-game-statistics")
                .exchange().expectStatus().is5xxServerError();
    }
}
