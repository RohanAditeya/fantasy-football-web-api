package com.fantasy.football.web.api.config;

import liquibase.changelog.ChangeLogHistoryServiceFactory;
import liquibase.changelog.FastCheckService;
import liquibase.changelog.StandardChangeLogHistoryService;
import liquibase.changelog.visitor.ValidatingVisitorGeneratorFactory;
import liquibase.database.LiquibaseTableNamesFactory;
import liquibase.parser.SqlParserFactory;
import liquibase.report.ShowSummaryGeneratorFactory;
import liquibase.ui.LoggerUIService;
import org.springframework.aot.hint.ExecutableMode;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

import java.util.Collections;

/**
 * Class to register runtime hints used when generating native image.
 * Required since liquibase hasn't been fully configured with reachability metadata
 * according to this issue -> <a href="https://github.com/oracle/graalvm-reachability-metadata/issues/431">Reachability metadata missing for liquibase</a>
 */
public class LiquibaseRuntimeHintsRegistrar implements RuntimeHintsRegistrar {
    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.reflection().registerType(LoggerUIService.class, type -> type.withConstructor(Collections.emptyList(), ExecutableMode.INVOKE));
        hints.reflection().registerType(LiquibaseTableNamesFactory.class, type -> type.withConstructor(Collections.emptyList(), ExecutableMode.INVOKE));
        hints.reflection().registerType(ValidatingVisitorGeneratorFactory.class, type -> type.withConstructor(Collections.emptyList(), ExecutableMode.INVOKE));
        hints.reflection().registerType(FastCheckService.class, type -> type.withConstructor(Collections.emptyList(), ExecutableMode.INVOKE));
        hints.reflection().registerType(ChangeLogHistoryServiceFactory.class, type -> type.withConstructor(Collections.emptyList(), ExecutableMode.INVOKE));
        hints.reflection().registerType(StandardChangeLogHistoryService.class, type -> type.withConstructor(Collections.emptyList(), ExecutableMode.INVOKE));
        hints.reflection().registerType(SqlParserFactory.class, type -> type.withConstructor(Collections.emptyList(), ExecutableMode.INVOKE));
        hints.reflection().registerType(ShowSummaryGeneratorFactory.class, type -> type.withConstructor(Collections.emptyList(), ExecutableMode.INVOKE));
    }
}