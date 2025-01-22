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
    void createGameweekScoreSavesRecordsSuccessfully() {
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
    void gameweekStatRecordsAreNotSavedUponFailureToSaveBreakupRecords() {
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
}