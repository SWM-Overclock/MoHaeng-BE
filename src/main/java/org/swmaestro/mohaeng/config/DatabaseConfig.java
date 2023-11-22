package org.swmaestro.mohaeng.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@RequiredArgsConstructor
@Component
@Profile("local")
public class DatabaseConfig {

    private final DataSource dataSource;

    @PostConstruct
    public void initDb() {
        try (Connection conn = dataSource.getConnection();
            Statement stat = conn.createStatement()) {
            stat.execute("CREATE ALIAS IF NOT EXISTS point FOR \"org.swmaestro.mohaeng.config.CustomH2Functions.createPoint\"");
            stat.execute("CREATE ALIAS IF NOT EXISTS ST_DISTANCE_SPHERE FOR \"org.swmaestro.mohaeng.config.CustomH2Functions.h2DistanceSphere\"");
        } catch (Exception e) {
            throw new RuntimeException("Error initializing custom H2 functions", e);
        }
    }

}
