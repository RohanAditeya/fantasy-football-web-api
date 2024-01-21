package com.fantasy.football.web.api.integration.step.definitions;

import com.fantasy.football.model.LeagueTeam;
import com.fantasy.football.web.api.repository.LeagueTeamRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LeagueTeamApiStepDefinitions {

    @Autowired
    private LeagueTeamRepository leagueTeamRepository;
    @Autowired
    private MockMvc mvcClient;

    @When(value = "A user calls the POST {string} API with values")
    public void user_calls_post_api_with_request_body_as (String path, String requestBody) throws Exception {
        mvcClient.perform(post(path).content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
    }

    @Given(value = "The check for team with code {int} existing in league team table returns {boolean}")
    public void team_with_team_code_is_present_in_db (int teamCode, boolean isPresent) {
        if (isPresent)
            leagueTeamRepository.save(new LeagueTeam.Builder().code(teamCode).build());
    }

    @Then(value = "The record with team code {int} is successfully stored in DB")
    public void record_with_team_code_is_present (int teamCode) {
        assertThat(leagueTeamRepository.findById(teamCode).isPresent()).isTrue();
    }

    @Then(value = "the player record with code {int} is successfully stored in DB")
    public void player_record_is_stored_in_db (int playerCode) {}

    @Then(value = "A user calls the POST {string} API with values returns exception")
    public void user_calls_returns_exception (String path, String requestBody) throws Exception {
        assertThatExceptionOfType(ServletException.class).isThrownBy( () -> mvcClient.perform(post(path).content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is5xxServerError()));
    }
}