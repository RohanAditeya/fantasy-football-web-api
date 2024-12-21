package com.fantasy.football.web.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.modulith.Modulith;

@Modulith(systemName = "fantasy-football-web-application")
@SpringBootApplication
public class FantasyFootballWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(FantasyFootballWebApplication.class, args);
    }
}