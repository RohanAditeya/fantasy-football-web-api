package com.fantasy.football.web.api;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModule;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

public class SpringModulithTests {

    @Test
    public void verifyModulesTest() {
        ApplicationModules modules = ApplicationModules.of(FantasyFootballWebApplication.class);
        modules.verify();
        new Documenter(modules).writeDocumentation();
    }
}