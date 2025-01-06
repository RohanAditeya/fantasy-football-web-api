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
}
