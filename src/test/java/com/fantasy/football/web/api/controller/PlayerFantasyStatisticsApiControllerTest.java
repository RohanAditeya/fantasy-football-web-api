package com.fantasy.football.web.api.controller;

import com.fantasy.football.model.PlayerFantasyStatistics;
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

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@ExtendWith(value = OutputCaptureExtension.class)
public class PlayerFantasyStatisticsApiControllerTest extends BaseTestExtension {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName(value = "create request should save fantasy statistics record")
    void createLeaguePlayerFantasyStatisticsSavesRecordTest(CapturedOutput capturedOutput) {
        Flux<PlayerFantasyStatistics> response = webTestClient.post().uri("/api/fantasy/football/v1/league-player-fantasy-statistics")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PlayerFantasyStatistics.Builder().build())
                .exchange().expectStatus().isCreated()
                .returnResult(PlayerFantasyStatistics.class).getResponseBody();
        StepVerifier.create(response)
                .assertNext(savedRecord -> {
                    assertThat(savedRecord.getRecordId()).isNotNull();
                    assertThat(capturedOutput.getAll().contains(String.format("Saved league Player fantasy statistics record with ID %s", savedRecord.getRecordId()))).isTrue();
                }).verifyComplete();
    }

    @Test
    @DisplayName(value = "delete request should delete existing record")
    void deletePlayerFantasyStatisticsRequestDeletesExistingRecordTest(CapturedOutput capturedOutput) {
        webTestClient.delete().uri("/api/fantasy/football/v1/league-player-fantasy-statistics")
                .header("record_id", "e97170e8-b112-40cc-918e-10465392c4c3")
                .exchange().expectStatus().isNoContent();
        assertThat(capturedOutput.getAll().contains("Deleted league player fantasy statistics record with ID e97170e8-b112-40cc-918e-10465392c4c3")).isTrue();
    }
}