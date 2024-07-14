package com.fantasy.football.web.api.player.controller;

import com.fantasy.football.dto.CreateLeaguePlayerRequest;
import com.fantasy.football.model.PlayerBasicInformation;
import com.fantasy.football.model.PlayerFantasyStatistics;
import com.fantasy.football.model.PlayerGameStatistics;
import com.fantasy.football.model.PlayerMiscellaneousInformation;
import com.fantasy.football.web.api.core.BaseTestExtension;
import com.fantasy.football.web.api.player.repository.PlayerBasicInformationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.modulith.test.EnableScenarios;
import org.springframework.modulith.test.Scenario;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@EnableScenarios
@AutoConfigureMockMvc
public class LeaguePlayerApiControllerTest extends BaseTestExtension {

    @Autowired
    private WebTestClient webClient;
    @Autowired
    private PlayerBasicInformationRepository playerBasicInformationRepository;

    @Test
    public void createLeaguePlayerTest(Scenario scenario) {
        PlayerBasicInformation basicInfoRecord = new PlayerBasicInformation.Builder().team(UUID.fromString("75fd0dd0-b829-4a9c-8d06-5eb0dc4347d8")).firstName("Martin").secondName("Odegaard").code(1000L).build();
        PlayerFantasyStatistics fantasyStatistics = new PlayerFantasyStatistics.Builder().build();
        PlayerGameStatistics gameStatistics = new PlayerGameStatistics.Builder().build();
        PlayerMiscellaneousInformation miscellaneousInformation = new PlayerMiscellaneousInformation.Builder().build();
        scenario.stimulate(
                        () -> {
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
                )
                .andWaitAtMost(Duration.ofSeconds(10L))
                .andWaitForStateChange(() -> playerBasicInformationRepository.findAll()
                        , recordsFlux -> recordsFlux.filter(savedRecord -> savedRecord.getSecondName().equals("Odegaard")).count().block() == 1)
                .andVerify(savedRecord -> {
                    Flux<PlayerBasicInformation> filteredFlux = savedRecord.filter(fetchedRecord -> fetchedRecord.getSecondName().equals("Odegaard"));
                    StepVerifier.create(filteredFlux)
                            .assertNext(
                                    fetchedRecord -> {
                                        assertThat(fetchedRecord.getSecondName()).isEqualTo("Odegaard");
                                        assertThat(fetchedRecord.getFirstName()).isEqualTo("Martin");
                                        assertThat(fetchedRecord.getTeam()).isEqualTo(UUID.fromString("75fd0dd0-b829-4a9c-8d06-5eb0dc4347d8"));
                                        assertThat(fetchedRecord.getCode()).isEqualTo(1000L);
                                    }
                            )
                            .verifyComplete();
                });
    }
}