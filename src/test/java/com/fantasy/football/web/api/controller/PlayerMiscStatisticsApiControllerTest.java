package com.fantasy.football.web.api.controller;

import com.fantasy.football.model.PlayerMiscellaneousInformation;
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
public class PlayerMiscStatisticsApiControllerTest extends BaseTestExtension {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName(value = "create request should save misc statistics record")
    void createLeaguePlayerMiscStatisticsSavesRecordTest(CapturedOutput capturedOutput) {
        Flux<PlayerMiscellaneousInformation> response = webTestClient.post().uri("/api/fantasy/football/v1/league-player-misc-statistics")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PlayerMiscellaneousInformation.Builder().build())
                .exchange().expectStatus().isCreated()
                .returnResult(PlayerMiscellaneousInformation.class).getResponseBody();
        StepVerifier.create(response)
                .assertNext(savedRecord -> {
                    assertThat(savedRecord.getRecordId()).isNotNull();
                    assertThat(capturedOutput.getAll().contains(String.format("Saved league Player misc statistics record with ID %s", savedRecord.getRecordId()))).isTrue();
                }).verifyComplete();
    }

    @Test
    @DisplayName(value = "delete misc request should delete existing misc record")
    void deletePlayerMiscStatisticsRequestDeletesExistingRecordTest(CapturedOutput capturedOutput) {
        webTestClient.delete().uri("/api/fantasy/football/v1/league-player-misc-statistics")
                .header("record_id", "0aea62b2-356b-4390-a170-6c3babc80f8b")
                .exchange().expectStatus().isNoContent();
        assertThat(capturedOutput.getAll().contains("Deleted league player misc statistics record with ID 0aea62b2-356b-4390-a170-6c3babc80f8b")).isTrue();
    }

    @Test
    @DisplayName(value = "delete misc request returns 500 when record_id header is missing")
    void deleteRequestThrows5xxErrorWhenHeaderIsOmitted() {
        webTestClient.delete().uri("/api/fantasy/football/v1/league-player-misc-statistics")
                .exchange().expectStatus().is5xxServerError();
    }

    @Test
    @DisplayName(value = "Fetch player misc record request returns existing record")
    void fetchExistingRecordIsReturnedTest() {
        Flux<PlayerMiscellaneousInformation> response = webTestClient.get().uri("/api/fantasy/football/v1/league-player-misc-statistics")
                .header("record_id", "3c95d9d0-6675-4bef-b984-82361531974b")
                .exchange().expectStatus().isOk().returnResult(PlayerMiscellaneousInformation.class).getResponseBody();
        StepVerifier.create(response)
                .assertNext(fetchedRecord -> {
                    assertThat(fetchedRecord.getRecordId()).isEqualByComparingTo(UUID.fromString("3c95d9d0-6675-4bef-b984-82361531974b"));
                }).verifyComplete();
    }

    @Test
    @DisplayName(value = "Fetch misc statistics record request prints log if no record found")
    void fetchNotExistingRecordPrintsLogTest(CapturedOutput capturedOutput) {
        webTestClient.get().uri("/api/fantasy/football/v1/league-player-misc-statistics")
                .header("record_id", "3c95d9d0-6675-4bef-b984-82361531974c")
                .exchange().expectStatus().isOk();
        assertThat(capturedOutput.getAll().contains("Found no player fantasy statistics record with ID 3c95d9d0-6675-4bef-b984-82361531974c")).isTrue();
    }

    @Test
    @DisplayName(value = "Fetch misc request without header returns 500")
    void fetchMiscStatisticsRequestWithoutHeaderReturnsErrorTest() {
        webTestClient.get().uri("/api/fantasy/football/v1/league-player-misc-statistics")
                .exchange().expectStatus().is5xxServerError();
    }
}