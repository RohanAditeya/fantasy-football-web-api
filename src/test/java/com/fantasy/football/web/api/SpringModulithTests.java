package com.fantasy.football.web.api;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModule;
import org.springframework.modulith.core.ApplicationModules;

public class SpringModulithTests {

    @Test
    public void verifyModulesTest() {
        ApplicationModules modules = ApplicationModules.of(FantasyFootballWebApplication.class);
        modules.verify();
        for (ApplicationModule module : modules) {
            System.out.println(module);
        }
    }
}