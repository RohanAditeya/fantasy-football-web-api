package com.fantasy.football.web.api.controller;

import com.fantasy.football.dto.LeagueTeamPatchDto;
import com.fantasy.football.model.LeagueTeam;
import com.fantasy.football.web.api.core.BaseTestExtension;
import com.fantasy.football.web.api.repository.LeagueTeamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.*;

@AutoConfigureMockMvc
public class LeagueTeamApiControllerTest extends BaseTestExtension {

    @Autowired
    private WebTestClient webClient;
    @Autowired
    private LeagueTeamRepository leagueTeamRepository;

    @Test
    public void createLeagueTeamTest() {
        Flux<LeagueTeam> response = webClient.post().uri("/api/fantasy/football/v1/league-team")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(new LeagueTeam.Builder().name("Arsenal").code(1500).shortName("ARS").form(10F).build()), LeagueTeam.class)
                .exchange().expectStatus().isCreated()
                .returnResult(LeagueTeam.class).getResponseBody();
        StepVerifier.create(response)
                .assertNext(responseEntity -> {
                    assertThat(responseEntity.getVersionNumber()).isEqualTo(1L);
                })
                .verifyComplete();
    }

    @Test
    public void deleteLeagueTeamTest() {
        webClient.delete().uri("/api/fantasy/football/v1/league-team").header("team_name", "Chelsea")
                .exchange().expectStatus().isNoContent();
        Mono<Boolean> afterDelete = leagueTeamRepository.findAll().any(leagueTeam -> leagueTeam.getName().equals("Chelsea"));
        StepVerifier.create(afterDelete)
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    public void updateLeagueTeamTest() {
        Flux<LeagueTeam> updateResponse = webClient.patch().uri("/api/fantasy/football/v1/league-team").header("team_code", "2000")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(new LeagueTeamPatchDto().played(10).loss(10)), LeagueTeamPatchDto.class)
                .exchange().expectStatus().isAccepted().returnResult(LeagueTeam.class).getResponseBody();
        StepVerifier.create(updateResponse)
                .assertNext(updatedRecord -> {
                    assertThat(updatedRecord.getVersionNumber()).isEqualTo(2);
                    assertThat(updatedRecord.getPlayed()).isEqualTo(10);
                    assertThat(updatedRecord.getLoss()).isEqualTo(10);
                }).verifyComplete();
    }

    @Test
    public void fetchLeagueTeamRecordsWithRecordIdTest() {
        Flux<LeagueTeam> fetchResponse = webClient.get().uri("/api/fantasy/football/v1/league-team").header("record_id", "30fd21fe-4a22-4c15-bf2d-c456f606ea45")
                .exchange().expectStatus().isOk().returnResult(LeagueTeam.class).getResponseBody();
        StepVerifier.create(fetchResponse)
                .assertNext(fetchedRecord -> {
                    assertThat(fetchedRecord.getName()).isEqualTo("Tottenham");
                })
                .verifyComplete();
    }

    @Test
    public void fetchLeagueTeamRecordWithTeamCodeTest() {
        Flux<LeagueTeam> fetchResponse = webClient.get().uri("/api/fantasy/football/v1/league-team").header("team_code", "3000")
                .exchange().expectStatus().isOk().returnResult(LeagueTeam.class).getResponseBody();
        StepVerifier.create(fetchResponse)
                .assertNext(fetchedRecord -> {
                    assertThat(fetchedRecord.getName()).isEqualTo("Tottenham");
                }).verifyComplete();
    }

    @Test
    public void fetchLeagueTeamRecordWithTeamNameTest() {
        Flux<LeagueTeam> fetchResponse = webClient.get().uri("/api/fantasy/football/v1/league-team").header("team_name", "Tottenham")
                .exchange().expectStatus().isOk().returnResult(LeagueTeam.class).getResponseBody();
        StepVerifier.create(fetchResponse)
                .assertNext(fetchedRecord -> {
                    assertThat(fetchedRecord.getName()).isEqualTo("Tottenham");
                }).verifyComplete();
    }

    @Test
    public void fetchLeagueTeamWithDefaultPageNumberTest() {
        Flux<LeagueTeam> fetchedRecords = webClient.get().uri("/api/fantasy/football/v1/league-team?page_size=5")
                .exchange().expectStatus().isOk().returnResult(LeagueTeam.class).getResponseBody();
        StepVerifier.create(fetchedRecords)
                .assertNext(fetchedRecord -> {
                    assertThat(fetchedRecord.getName()).isEqualTo("Crystal Palace");
                    assertThat(fetchedRecord.getCode()).isEqualTo(1);
                })
                .assertNext(fetchedRecord -> {
                    assertThat(fetchedRecord.getName()).isEqualTo("Aston Villa");
                    assertThat(fetchedRecord.getCode()).isEqualTo(2);
                })
                .assertNext(fetchedRecord -> {
                    assertThat(fetchedRecord.getName()).isEqualTo("Liverpool");
                    assertThat(fetchedRecord.getCode()).isEqualTo(3);
                })
                .assertNext(fetchedRecord -> {
                    assertThat(fetchedRecord.getName()).isEqualTo("Wolves");
                    assertThat(fetchedRecord.getCode()).isEqualTo(4);
                })
                .assertNext(fetchedRecord -> {
                    assertThat(fetchedRecord.getName()).isEqualTo("Everton");
                    assertThat(fetchedRecord.getCode()).isEqualTo(5);
                })
                .verifyComplete();
    }

    @Test
    public void fetchLeagueTeamWithPageNumberAndSizeTest() {
        Flux<LeagueTeam> fetchedResponse = webClient.get().uri("/api/fantasy/football/v1/league-team?page_size=2&page_number=2")
                .exchange().expectStatus().isOk().returnResult(LeagueTeam.class).getResponseBody();
        StepVerifier.create(fetchedResponse)
                .assertNext(fetchedRecord -> {
                    assertThat(fetchedRecord.getName()).isEqualTo("Liverpool");
                    assertThat(fetchedRecord.getCode()).isEqualTo(3);
                })
                .assertNext(fetchedRecord -> {
                    assertThat(fetchedRecord.getName()).isEqualTo("Wolves");
                    assertThat(fetchedRecord.getCode()).isEqualTo(4);
                }).verifyComplete();
    }
}