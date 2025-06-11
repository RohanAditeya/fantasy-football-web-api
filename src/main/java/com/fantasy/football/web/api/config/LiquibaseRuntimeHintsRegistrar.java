package com.fantasy.football.web.api.config;

import liquibase.changelog.FastCheckService;
import liquibase.changelog.visitor.ValidatingVisitorGeneratorFactory;
import liquibase.database.LiquibaseTableNamesFactory;
import liquibase.report.ShowSummaryGeneratorFactory;
import liquibase.ui.LoggerUIService;
import org.springframework.aot.hint.ExecutableMode;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

import java.util.Collections;

public class LiquibaseRuntimeHintsRegistrar implements RuntimeHintsRegistrar {
    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.reflection().registerType(LoggerUIService.class, type -> type.withConstructor(Collections.emptyList(), ExecutableMode.INVOKE));
        hints.reflection().registerType(LiquibaseTableNamesFactory.class, type -> type.withConstructor(Collections.emptyList(), ExecutableMode.INVOKE));
        hints.reflection().registerType(ValidatingVisitorGeneratorFactory.class, type -> type.withConstructor(Collections.emptyList(), ExecutableMode.INVOKE));
        hints.reflection().registerType(FastCheckService.class, type -> type.withConstructor(Collections.emptyList(), ExecutableMode.INVOKE));
        hints.reflection().registerType(ShowSummaryGeneratorFactory.class, type -> type.withConstructor(Collections.emptyList(), ExecutableMode.INVOKE));
    }
}