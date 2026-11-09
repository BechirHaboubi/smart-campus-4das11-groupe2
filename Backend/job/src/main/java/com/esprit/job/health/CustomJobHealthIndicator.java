package com.esprit.job.health;

import com.esprit.job.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Component
public class CustomJobHealthIndicator implements HealthIndicator {

    @Autowired(required = false)
    private DataSource dataSource;

    @Autowired(required = false)
    private JobRepository jobRepository;

    @Override
    public Health health() {
        try {
            if (dataSource != null) {
                try (Connection conn = dataSource.getConnection();
                     Statement stmt = conn.createStatement()) {
                    stmt.execute("SELECT 1");
                }
            }

            long count = jobRepository != null ? jobRepository.count() : 0;

            return Health.up()
                    .withDetail("service", "Job Microservice")
                    .withDetail("database", "Accessible")
                    .withDetail("jobsCount", count)
                    .withDetail("message", "Le service Job et sa base de données fonctionnent normalement")
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("service", "Job Microservice")
                    .withDetail("error", e.getMessage())
                    .withDetail("message", "Impossible de joindre la base de données du service Job")
                    .build();
        }
    }
}
