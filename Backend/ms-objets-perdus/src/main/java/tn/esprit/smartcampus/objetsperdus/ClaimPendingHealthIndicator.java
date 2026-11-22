package tn.esprit.smartcampus.objetsperdus;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Component("claimPendingHealthIndicator")
public class ClaimPendingHealthIndicator implements HealthIndicator {

    private final AtomicLong pendingClaimsOver7Days = new AtomicLong(0);
    private final AtomicBoolean databaseUp = new AtomicBoolean(true);
    private final long threshold = 20;

    public void setDatabaseUp(boolean up) {
        this.databaseUp.set(up);
    }

    public void setPendingClaimsOver7Days(long count) {
        this.pendingClaimsOver7Days.set(count);
    }

    @Override
    public Health health() {
        if (!databaseUp.get()) {
            return Health.down()
                    .withDetail("database", "MongoDB (DISCONNECTED - Simulated Failure)")
                    .build();
        }

        long count = pendingClaimsOver7Days.get();
        if (count > threshold) {
            return Health.down()
                    .withDetail("database", "MongoDB (CONNECTED)")
                    .withDetail("pendingClaimsOver7Days", count)
                    .withDetail("threshold", threshold)
                    .withDetail("reason", "Trop de reclamations en attente depuis plus de 7 jours")
                    .build();
        }

        return Health.up()
                .withDetail("database", "MongoDB (CONNECTED)")
                .withDetail("pendingClaimsOver7Days", count)
                .withDetail("threshold", threshold)
                .build();
    }
}
