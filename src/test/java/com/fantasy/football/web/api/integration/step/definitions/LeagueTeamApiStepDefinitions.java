package com.fantasy.football.web.api.integration.step.definitions;

import com.fantasy.football.model.LeagueTeam;
import com.fantasy.football.model.LeagueTeamPrimaryKey;
import com.fantasy.football.web.api.repository.LeagueTeamRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LeagueTeamApiStepDefinitions {

    @Autowired
    private LeagueTeamRepository leagueTeamRepository;
    @Autowired
    private MockMvc mvcClient;

    @When(value = "The record for {int} is {boolean} in DB")
    public void is_record_present_in_db (int teamCode, boolean isAdded) {
        if (isAdded) {
            leagueTeamRepository.save(new LeagueTeam.Builder().teamKey(new LeagueTeamPrimaryKey(teamCode, "mock-football-club")).build());
        }
    }

    @And(value = "A league team record is queried using a {int} and API returns {int}")
    public void query_a_league_team_record (int teamCode, int status) throws Exception {
        mvcClient.perform(get("/fantasy/football/league/team/fetch/".concat(String.valueOf(teamCode)))).andExpect(status().is(status));
    }

    @And(value = "The record with {int} if {boolean} is deleted")
    public void record_if_created_is_deleted (int teamCode, boolean isAdded) {
        if (isAdded)
            leagueTeamRepository.deleteByTeamKeyCode(teamCode);
    }
}