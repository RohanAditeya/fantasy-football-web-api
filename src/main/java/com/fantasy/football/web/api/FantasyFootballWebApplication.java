package com.fantasy.football.web.api;

import com.fantasy.football.web.api.config.LiquibaseRuntimeHintsRegistrar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;

@SpringBootApplication
@ImportRuntimeHints(value = {LiquibaseRuntimeHintsRegistrar.class})
public class FantasyFootballWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(FantasyFootballWebApplication.class, args);
    }
}