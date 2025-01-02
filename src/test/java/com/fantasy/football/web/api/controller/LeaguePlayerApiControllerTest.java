package com.fantasy.football.web.api.controller;

import com.fantasy.football.dto.CreateLeaguePlayerRequest;
import com.fantasy.football.dto.PlayerBasicInformationPatchDTO;
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

    @Test
    @DisplayName(value = "Fetch player with record_id fetches existing record from DB")
    void fetchPlayerWithRecordIdFetchedFromDBTest(){
        Flux<PlayerBasicInformation> fetchedRecords = webClient.get().uri("/api/fantasy/football/v1/league-player")
                .header("record_id", "f387132e-588b-4b89-ab3e-207231489122")
                .exchange().expectStatus().isOk().returnResult(PlayerBasicInformation.class).getResponseBody();
        StepVerifier.create(fetchedRecords)
                .assertNext(record -> {
                    assertThat(record.getRecordId()).isEqualByComparingTo(UUID.fromString("f387132e-588b-4b89-ab3e-207231489122"));
                    assertThat(record.getPlayerFantasyStatistics()).isEqualByComparingTo(UUID.fromString("8ee8e58e-5320-4d55-b73f-4d40fae16bc1"));
                    assertThat(record.getPlayerGameStatistics()).isEqualByComparingTo(UUID.fromString("8186e89b-0457-4001-9ddd-450663a37cf3"));
                    assertThat(record.getPlayerMiscellaneousInformation()).isEqualByComparingTo(UUID.fromString("aa1605ba-077a-4631-983d-05b37125e3a0"));
                    assertThat(record.getFirstName()).isEqualTo("Leandro");
                    assertThat(record.getSecondName()).isEqualTo("Trossard");
                    assertThat(record.getCode()).isEqualTo(4000L);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName(value = "Fetch player with player_code fetches existing record from DB")
    void fetchPlayerWithPlayerCodeFetchedFromDBTest(){
        Flux<PlayerBasicInformation> fetchedRecords = webClient.get().uri("/api/fantasy/football/v1/league-player")
                .header("player_code", "5000")
                .exchange().expectStatus().isOk().returnResult(PlayerBasicInformation.class).getResponseBody();
        StepVerifier.create(fetchedRecords)
                .assertNext(record -> {
                    assertThat(record.getRecordId()).isEqualByComparingTo(UUID.fromString("87611fb0-0d25-40af-80fd-422b07b2dd75"));
                    assertThat(record.getPlayerFantasyStatistics()).isEqualByComparingTo(UUID.fromString("1569af3b-5b29-431a-80b4-471dd82fe1ba"));
                    assertThat(record.getPlayerGameStatistics()).isEqualByComparingTo(UUID.fromString("adfcd7b6-487c-4504-894f-fb2a98d97a72"));
                    assertThat(record.getPlayerMiscellaneousInformation()).isEqualByComparingTo(UUID.fromString("b43ad5d7-7c44-4dd9-9727-ff9d7a9d5803"));
                    assertThat(record.getFirstName()).isEqualTo("Gabriel");
                    assertThat(record.getSecondName()).isEqualTo("Martinelli");
                    assertThat(record.getCode()).isEqualTo(5000L);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName(value = "Fetch player with team_id fetches existing records from DB")
    void fetchPlayerWithTeamIdFetchedFromDBTest(){
        Flux<PlayerBasicInformation> fetchedRecords = webClient.get().uri("/api/fantasy/football/v1/league-player")
                .header("player_code", "5000")
                .exchange().expectStatus().isOk().returnResult(PlayerBasicInformation.class).getResponseBody();
        StepVerifier.create(fetchedRecords)
                .assertNext(record -> {
                    assertThat(record.getRecordId()).isEqualByComparingTo(UUID.fromString("87611fb0-0d25-40af-80fd-422b07b2dd75"));
                    assertThat(record.getPlayerFantasyStatistics()).isEqualByComparingTo(UUID.fromString("1569af3b-5b29-431a-80b4-471dd82fe1ba"));
                    assertThat(record.getPlayerGameStatistics()).isEqualByComparingTo(UUID.fromString("adfcd7b6-487c-4504-894f-fb2a98d97a72"));
                    assertThat(record.getPlayerMiscellaneousInformation()).isEqualByComparingTo(UUID.fromString("b43ad5d7-7c44-4dd9-9727-ff9d7a9d5803"));
                    assertThat(record.getFirstName()).isEqualTo("Gabriel");
                    assertThat(record.getSecondName()).isEqualTo("Martinelli");
                    assertThat(record.getCode()).isEqualTo(5000L);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName(value = "Fetch player with team_id and fetches existing records from DB")
    void fetchPlayerWithTeamIdAndFetchedFromDBTest(){
        Flux<PlayerBasicInformation> response = webClient.get().uri("/api/fantasy/football/v1/league-player")
                .header("team_id", "684e39fc-dec1-4b8c-b8d8-a816256ceaf6")
                .exchange().expectStatus().isOk().returnResult(PlayerBasicInformation.class).getResponseBody();
        StepVerifier.create(response)
                .expectNextCount(5)
                .verifyComplete();
    }

    @Test
    @DisplayName(value = "Fetch player without any headers throws error")
    void fetchPlayerWithoutHeadersThrowsErrorTest(){
        webClient.get().uri("/api/fantasy/football/v1/league-player")
                .exchange().expectStatus().is5xxServerError();
    }

    @Test
    @DisplayName(value = "Update player with record_id header updates record")
    void updatePlayerWithRecordIdUpdatesRecordTest(){
        Flux<PlayerBasicInformation> response = webClient.patch().uri("/api/fantasy/football/v1/league-player")
                .header("record_id", "0707d18b-e7e1-4e62-a41c-7c03dd9ea355")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PlayerBasicInformationPatchDTO().firstName("Mikel").secondName("Merino").status("A"))
                .exchange().expectStatus().isAccepted().returnResult(PlayerBasicInformation.class).getResponseBody();
        StepVerifier.create(response)
                .assertNext(updatedRecord -> {
                    assertThat(updatedRecord.getStatus()).isEqualTo("A");
                }).verifyComplete();
    }

    @Test
    @DisplayName(value = "Update player with player_code header updates record")
    void updatePlayerWithPlayerCodeUpdatesRecordTest(){
        Flux<PlayerBasicInformation> response = webClient.patch().uri("/api/fantasy/football/v1/league-player")
                .header("player_code", "13000")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PlayerBasicInformationPatchDTO().firstName("Declan").secondName("Rice").status("U"))
                .exchange().expectStatus().isAccepted().returnResult(PlayerBasicInformation.class).getResponseBody();
        StepVerifier.create(response)
                .assertNext(updatedRecord -> {
                    assertThat(updatedRecord.getStatus()).isEqualTo("U");
                }).verifyComplete();
    }

    @Test
    @DisplayName(value = "Update player with record_id header does not update record when not found")
    void updatePlayerWithRecordIdDoesNotUpdateRecordTest(CapturedOutput capturedOutput){
        Flux<PlayerBasicInformation> response = webClient.patch().uri("/api/fantasy/football/v1/league-player")
                .header("record_id", "3c2bcd43-5a87-42fb-8cb1-b43df0219ff4")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PlayerBasicInformationPatchDTO().firstName("Unknown").secondName("Unknown").status("U"))
                .exchange().expectStatus().isAccepted().returnResult(PlayerBasicInformation.class).getResponseBody();
        StepVerifier.create(response).verifyComplete();
        assertThat(capturedOutput.getAll().contains("Cannot find player to update with ID 3c2bcd43-5a87-42fb-8cb1-b43df0219ff4 and player code null")).isTrue();
    }

    @Test
    @DisplayName(value = "Update player with no header throws error")
    void updatePlayerWithNoHeaderThrowsErrorTest(){
        webClient.patch().uri("/api/fantasy/football/v1/league-player")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PlayerBasicInformationPatchDTO().firstName("Unknown").secondName("Unknown").status("U"))
                .exchange().expectStatus().is5xxServerError();
    }
}