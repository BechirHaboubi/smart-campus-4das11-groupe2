package tn.esprit.smartcampus.annonces;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Component("scheduledContentHealthIndicator")
public class ScheduledContentHealthIndicator implements HealthIndicator {

    private final AtomicLong overdueScheduledPosts = new AtomicLong(0);
    private final AtomicBoolean databaseUp = new AtomicBoolean(true);

    public void setDatabaseUp(boolean up) {
        this.databaseUp.set(up);
    }

    public void setOverdueScheduledPosts(long count) {
        this.overdueScheduledPosts.set(count);
    }

    @Override
    public Health health() {
        if (!databaseUp.get()) {
            return Health.down()
                    .withDetail("database", "H2 File (UNAVAILABLE - Simulated Failure)")
                    .build();
        }

        long overdue = overdueScheduledPosts.get();
        if (overdue > 10) {
            return Health.down()
                    .withDetail("database", "H2 File (ACCESSIBLE)")
                    .withDetail("overdueScheduledPosts", overdue)
                    .withDetail("threshold", 10)
                    .withDetail("reason", "Trop de contenus planifies non publies")
                    .build();
        }

        return Health.up()
                .withDetail("database", "H2 File (ACCESSIBLE)")
                .withDetail("overdueScheduledPosts", overdue)
                .build();
    }
}
