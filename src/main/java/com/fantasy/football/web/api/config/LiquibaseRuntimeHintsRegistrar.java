package com.fantasy.football.web.api.config;

import liquibase.ui.LoggerUIService;
import org.springframework.aot.hint.ExecutableMode;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

import java.util.Collections;

public class LiquibaseRuntimeHintsRegistrar implements RuntimeHintsRegistrar {
    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.reflection().registerType(LoggerUIService.class, type -> type.withConstructor(Collections.emptyList(), ExecutableMode.INVOKE));
    }
}