package com.fantasy.football.web.api.leagueteam;

import com.fantasy.football.model.LeagueTeam;
import com.fantasy.football.web.api.core.BaseTestExtension;
import com.fantasy.football.web.api.leagueteam.repository.LeagueTeamRepository;
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
	public void createLeagueTeamTest () {
		Flux<LeagueTeam> response = webClient.post().uri("/api/fantasy/football/v1/league-team")
				.contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(new LeagueTeam.Builder().name("Arsenal").code(1).shortName("ARS").form(10F).build()), LeagueTeam.class)
				.exchange().expectStatus().isCreated()
				.returnResult(LeagueTeam.class).getResponseBody();
		StepVerifier.create(response)
				.assertNext(responseEntity -> {
					assertThat(responseEntity.getVersionNumber()).isEqualTo(1L);
				})
				.verifyComplete();
	}

	@Test
	public void deleteLeagueTeamTest () {
		webClient.delete().uri("/api/fantasy/football/v1/league-team").header("team_name", "Chelsea")
				.exchange().expectStatus().isNoContent();
		Mono<Boolean> afterDelete = leagueTeamRepository.findAll().any(leagueTeam -> leagueTeam.getName().equals("Chelsea"));
		StepVerifier.create(afterDelete)
				.expectNext(false)
				.verifyComplete();
	}
}