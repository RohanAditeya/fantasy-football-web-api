package com.fantasy.football.web.api.integration.step.definitions;

import com.fantasy.football.web.api.repository.LeagueTeamRepository;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LeagueTeamApiStepDefinitions {

    @Autowired
    private LeagueTeamRepository leagueTeamRepository;
    @Autowired
    private MockMvc mvcClient;

    @When(value = "A user calls the POST API with values")
    public void user_calls_post_api_with_request_body_as (String requestBody) throws Exception {
        mvcClient.perform(post("/fantasy/football/league/team/create").content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
    }

    @Then(value = "The record with team code {int} is successfully stored in DB")
    public void record_with_team_code_is_present (int teamCode) {
        assertThat(leagueTeamRepository.findByCode(teamCode).isPresent()).isTrue();
    }
}