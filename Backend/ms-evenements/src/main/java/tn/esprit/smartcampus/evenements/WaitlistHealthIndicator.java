package tn.esprit.smartcampus.evenements;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Component("waitlistHealthIndicator")
public class WaitlistHealthIndicator implements HealthIndicator {

    private final AtomicLong waitlistCount = new AtomicLong(5);
    private final AtomicBoolean databaseUp = new AtomicBoolean(true);
    private final long maxCapacity = 100;

    public void setDatabaseUp(boolean up) {
        this.databaseUp.set(up);
    }

    public void setWaitlistCount(long count) {
        this.waitlistCount.set(count);
    }

    @Override
    public Health health() {
        if (!databaseUp.get()) {
            return Health.down()
                    .withDetail("database", "MySQL (DISCONNECTED - Simulated Failure)")
                    .withDetail("waitlistCount", waitlistCount.get())
                    .build();
        }

        long current = waitlistCount.get();
        if (current > maxCapacity) {
            return Health.down()
                    .withDetail("database", "MySQL (CONNECTED)")
                    .withDetail("waitlistCount", current)
                    .withDetail("maxCapacity", maxCapacity)
                    .withDetail("reason", "Capacité maximale de la liste d'attente dépassée")
                    .build();
        }

        return Health.up()
                .withDetail("database", "MySQL (CONNECTED)")
                .withDetail("waitlistCount", current)
                .withDetail("maxCapacity", maxCapacity)
                .build();
    }
}
