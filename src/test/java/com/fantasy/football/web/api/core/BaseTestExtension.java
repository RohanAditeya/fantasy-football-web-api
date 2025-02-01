package com.fantasy.football.web.api.core;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;
import reactor.blockhound.BlockHound;

import javax.sql.DataSource;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles(profiles = {"unit"})
@Import(value = BaseTestExtension.JdbcBeansConfiguration.class)
public class BaseTestExtension {

    static {
        BlockHound.install(builder -> {
            builder.allowBlockingCallsInside(UUID.class.getName(), "randomUUID");
        });
    }

    @TestConfiguration
    static class JdbcBeansConfiguration {
        @Bean
        public DataSource dataSource() {
            return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
        }
    }
}