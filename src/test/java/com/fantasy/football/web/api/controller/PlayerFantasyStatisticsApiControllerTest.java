package com.fantasy.football.web.api.controller;

import com.fantasy.football.dto.PlayerFantasyStatisticsPatchDTO;
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

import java.util.UUID;

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

    @Test
    @DisplayName(value = "delete request returns 500 when record_id header is missing")
    void deleteRequestThrows5xxErrorWhenHeaderIsOmitted() {
        webTestClient.delete().uri("/api/fantasy/football/v1/league-player-fantasy-statistics")
                .exchange().expectStatus().is5xxServerError();
    }

    @Test
    @DisplayName(value = "Fetch player statistics record request returns existing record")
    void fetchExistingRecordIsReturnedTest() {
        Flux<PlayerFantasyStatistics> response = webTestClient.get().uri("/api/fantasy/football/v1/league-player-fantasy-statistics")
                .header("record_id", "a35fb2f4-1c40-4c96-ab02-2a7eb0afcfa4")
                .exchange().expectStatus().isOk().returnResult(PlayerFantasyStatistics.class).getResponseBody();
        StepVerifier.create(response)
                .assertNext(fetchedRecord -> {
                    assertThat(fetchedRecord.getRecordId()).isEqualByComparingTo(UUID.fromString("a35fb2f4-1c40-4c96-ab02-2a7eb0afcfa4"));
                }).verifyComplete();
    }

    @Test
    @DisplayName(value = "Fetch player statistics record request prints log if no record found")
    void fetchNotExistingRecordPrintsLogTest(CapturedOutput capturedOutput) {
        webTestClient.get().uri("/api/fantasy/football/v1/league-player-fantasy-statistics")
                .header("record_id", "a35fb2f4-1c40-4c96-ab02-2a7eb0afcfa5")
                .exchange().expectStatus().isOk();
        assertThat(capturedOutput.getAll().contains("Found no player fantasy statistics record with ID a35fb2f4-1c40-4c96-ab02-2a7eb0afcfa5")).isTrue();
    }

    @Test
    @DisplayName(value = "Error 500 when record_id header is missing when making fetch fantasy statistics request")
    void fetchRequestThrows5xxErrorWhenHeaderIsOmitted() {
        webTestClient.get().uri("/api/fantasy/football/v1/league-player-fantasy-statistics")
                .exchange().expectStatus().is5xxServerError();
    }

    @Test
    @DisplayName(value = "Update request updates and saves existing record successfully")
    void updateRequestsUpdatesExistingRecordAndSavesTest() {
        Flux<PlayerFantasyStatistics> response = webTestClient.patch().uri("/api/fantasy/football/v1/league-player-fantasy-statistics")
                .header("record_id", "fe4549a8-c14b-4859-a78f-0fad18a8bf3e")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PlayerFantasyStatisticsPatchDTO().dreamTeamCount(5))
                .exchange().expectStatus().isAccepted()
                .returnResult(PlayerFantasyStatistics.class).getResponseBody();
        StepVerifier.create(response)
                .assertNext(updatedRecord -> {
                    assertThat(updatedRecord.getDreamTeamCount()).isEqualTo(5);
                }).verifyComplete();
    }

    @Test
    @DisplayName(value = "Update request prints log if no record is found to update")
    void updateRequestsPrintsLogIfNoRecordFoundForUpdateTest(CapturedOutput capturedOutput) {
        webTestClient.patch().uri("/api/fantasy/football/v1/league-player-fantasy-statistics")
                .header("record_id", "fe4549a8-c14b-4859-a78f-0fad18a8bf37")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PlayerFantasyStatisticsPatchDTO().dreamTeamCount(5))
                .exchange().expectStatus().isAccepted();
        assertThat(capturedOutput.getAll().contains("Cannot find any player fantasy statistics record with ID fe4549a8-c14b-4859-a78f-0fad18a8bf37 for updating")).isTrue();
    }

    @Test
    @DisplayName(value = "Update request throws 500 error when record_id header is missing")
    void updateRequestsThrows5xxErrorWhenHeaderMissingTest() {
        webTestClient.patch().uri("/api/fantasy/football/v1/league-player-fantasy-statistics")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PlayerFantasyStatisticsPatchDTO().dreamTeamCount(5))
                .exchange().expectStatus().is5xxServerError();
    }
}