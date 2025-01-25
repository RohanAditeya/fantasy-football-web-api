package com.fantasy.football.web.api.controller;

import com.fantasy.football.dto.GameWeekScoreDTO;
import com.fantasy.football.model.PlayerGameWeekBreakup;
import com.fantasy.football.model.PlayerGameWeekStatistics;
import com.fantasy.football.web.api.core.BaseTestExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@AutoConfigureMockMvc
@ExtendWith(value = OutputCaptureExtension.class)
public class PlayerGameweekScoresApiControllerTest extends BaseTestExtension {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Test
    @DisplayName(value = "create gameweek score request saves both record successfully")
    void createGameweekScoreSavesRecordsSuccessfullyTest() {
        PlayerGameWeekBreakup gameWeekBreakup = new PlayerGameWeekBreakup(null, null, "Goals", 0, 0);
        PlayerGameWeekStatistics gameWeekStatistics = new PlayerGameWeekStatistics.Builder()
                .playerId(UUID.fromString("8b7ee960-6636-4e9e-91ff-81f986d58f4a"))
                .build();
        GameWeekScoreDTO requestBody = new GameWeekScoreDTO().addGameWeekBreakUpItem(gameWeekBreakup).gameWeekStatistics(gameWeekStatistics);
        Flux<GameWeekScoreDTO> response = webTestClient.post().uri("/api/fantasy/football/v1/league-player-gameweek-scores")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange().expectStatus().isCreated()
                .returnResult(GameWeekScoreDTO.class).getResponseBody();
        StepVerifier.create(response).assertNext(
                savedRecords -> {
                    assertThat(savedRecords.getGameWeekStatistics().getRecordId()).isNotNull();
                    assertThat(savedRecords.getGameWeekBreakUp().stream().allMatch(breakUpRecords -> breakUpRecords.getRecordId() != null)).isTrue();
                    assertThat(savedRecords.getGameWeekBreakUp().stream().allMatch(breakUpRecords -> breakUpRecords.getGameWeek().equals(savedRecords.getGameWeekStatistics().getRecordId()))).isTrue();
                }
        ).verifyComplete();
    }

    @Test
    @DisplayName(value = "gameweek stat record is not saved on failure to save breakup records")
    void gameweekStatRecordsAreNotSavedUponFailureToSaveBreakupRecordsTest() {
        // Intentionally setting the identifier as null
        PlayerGameWeekBreakup gameWeekBreakup = new PlayerGameWeekBreakup(null, null, null, 0, 0);
        PlayerGameWeekStatistics gameWeekStatistics = new PlayerGameWeekStatistics.Builder()
                .playerId(UUID.fromString("c6c4a549-2d9d-4070-a166-c9556765e831"))
                .build();
        GameWeekScoreDTO requestBody = new GameWeekScoreDTO().addGameWeekBreakUpItem(gameWeekBreakup).gameWeekStatistics(gameWeekStatistics);
        webTestClient.post().uri("/api/fantasy/football/v1/league-player-gameweek-scores")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange().expectStatus().is5xxServerError();
        Mono<PlayerGameWeekStatistics> savedRecordMono = r2dbcEntityTemplate.select(PlayerGameWeekStatistics.class).from("PLYR_GAME_WK_STCS")
                .matching(query(where("PLYR_ID").is(gameWeekStatistics.getPlayerId()))).one();
        // Select query must not return any record and Mono should not emit any events and complete.
        StepVerifier.create(savedRecordMono).verifyComplete();
    }

    @Test
    @DisplayName(value = "delete request for gameweek record deletes both gameweek stats and breakup records")
    void deleteRequestsDeletesAllStatsAndBreakUpRecordsTest(CapturedOutput capturedOutput) {
        webTestClient.delete().uri("/api/fantasy/football/v1/league-player-gameweek-scores")
                .header("record_id", "b5cca747-f799-49a2-a7c6-2da03eef7f36")
                .exchange().expectStatus().isNoContent();
        assertThat(capturedOutput.getAll().contains("Deleted 2 number of game week breakup records")).isTrue();
        assertThat(capturedOutput.getAll().contains("Successfully deleted all gameweek stats and breakup records for recordId b5cca747-f799-49a2-a7c6-2da03eef7f36 and playerId null")).isTrue();
    }

    @Test
    @DisplayName(value = "delete request for gameweek record deletes both gameweek stats and breakup records with player_id in header")
    void deleteRequestsDeletesAllStatsAndBreakUpRecordsWithPlayerIdTest(CapturedOutput capturedOutput) {
        webTestClient.delete().uri("/api/fantasy/football/v1/league-player-gameweek-scores")
                .header("player_id", "70136047-d5f6-443d-89b0-b84fc1d0c66d")
                .exchange().expectStatus().isNoContent();
        assertThat(capturedOutput.getAll().contains("Deleted 2 number of game week breakup records")).isTrue();
        assertThat(capturedOutput.getAll().contains("Successfully deleted all gameweek stats and breakup records for recordId null and playerId 70136047-d5f6-443d-89b0-b84fc1d0c66d")).isTrue();
    }

    @Test
    @DisplayName(value = "delete request does not throw error when no player gameweek or breakup records are deleted")
    void deleteRequestsReturnsSuccessWhenNoRecordIsDeletedTest() {
        webTestClient.delete().uri("/api/fantasy/football/v1/league-player-gameweek-scores")
                .header("player_id", "70136047-d5f6-443d-89b0-b84fc1d0c661")
                .exchange().expectStatus().isNoContent();
    }

    @Test
    @DisplayName(value = "fetch request returns gameweek records")
    void fetchResponseReturnsGameweekRecordsTest() {
        Flux<GameWeekScoreDTO> response = webTestClient.get().uri("/api/fantasy/football/v1/league-player-gameweek-scores")
                .header("record_id", "775fbc00-80c7-4ae4-990b-2441b081720d")
                .exchange().expectStatus().isOk()
                .returnResult(GameWeekScoreDTO.class).getResponseBody();
        StepVerifier.create(response)
                .assertNext(
                    fetchedRecord -> {
                        assertThat(fetchedRecord.getGameWeekStatistics().getRecordId()).isEqualTo(UUID.fromString("775fbc00-80c7-4ae4-990b-2441b081720d"));
                        assertThat(fetchedRecord.getGameWeekBreakUp().stream().anyMatch(breakupRecords -> breakupRecords.getRecordId().equals(UUID.fromString("1b5b8718-cbfa-49b0-b89b-267cc90a1265")))).isTrue();
                        assertThat(fetchedRecord.getGameWeekBreakUp().stream().anyMatch(breakupRecords -> breakupRecords.getRecordId().equals(UUID.fromString("75ca059d-4b24-4ee3-ae93-23172cdaa6a7")))).isTrue();
                    }
                )
                .verifyComplete();
    }

    @Test
    @DisplayName(value = "fetch request with player_id and page_number fetches correct page with records")
    void fetchResponseReturnsCorrectPageBasedOnPlayerIdAndPageNumberTest() {
        Flux<GameWeekScoreDTO> response = webTestClient.get().uri("/api/fantasy/football/v1/league-player-gameweek-scores?page_number=2")
                .header("player_id", "a71242aa-be5d-4dd5-9ebc-70c631476bf1")
                .exchange().expectStatus().isOk()
                .returnResult(GameWeekScoreDTO.class).getResponseBody();
        StepVerifier.create(response)
                .assertNext(
                        fetchedRecord -> {
                            assertThat(fetchedRecord.getGameWeekStatistics().getGameWeek()).isEqualTo(1);
                        }
                )
                .verifyComplete();
    }

    @Test
    @DisplayName(value = "fetch request with player_id and game_week returns propert gameweek scores records")
    void fetchResponseReturnsProperRecordBasedOnPlayerIdAndGameweekTest() {
        Flux<GameWeekScoreDTO> response = webTestClient.get().uri("/api/fantasy/football/v1/league-player-gameweek-scores?game_week=2")
                .header("player_id", "6f4e1808-b836-4d2f-8f38-b52faf1ef7cd")
                .exchange().expectStatus().isOk()
                .returnResult(GameWeekScoreDTO.class).getResponseBody();
        StepVerifier.create(response)
                .assertNext(
                        fetchedRecord -> {
                            assertThat(fetchedRecord.getGameWeekStatistics().getGameWeek()).isEqualTo(2);
                        }
                )
                .verifyComplete();
    }

    @Test
    @DisplayName(value = "fetch gameweek scores request with player_id returns empty but success response")
    void fetchResponseReturns200BasedOnPlayerIdTest() {
        Flux<GameWeekScoreDTO> response = webTestClient.get().uri("/api/fantasy/football/v1/league-player-gameweek-scores")
                .header("player_id", "6f4e1808-b836-4d2f-8f38-b52faf1ef7ce")
                .exchange().expectStatus().isOk()
                .returnResult(GameWeekScoreDTO.class).getResponseBody();
        StepVerifier.create(response)
                .verifyComplete();
    }

    @Test
    @DisplayName(value = "fetch gameweek scores request without any filter criteria headers returns error response")
    void fetchResponseReturns5xxWhenNoHeadersProvidedTest() {
        webTestClient.get().uri("/api/fantasy/football/v1/league-player-gameweek-scores")
                .exchange().expectStatus().is5xxServerError();
    }
}