package tn.esprit.smartcampus.reservations;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Component("pendingExpiredReservations")
public class ReservationExpiryHealthIndicator implements HealthIndicator {

    private final AtomicLong pendingExpired = new AtomicLong(0);
    private final AtomicBoolean databaseUp = new AtomicBoolean(true);
    private final long threshold = 50;

    public void setDatabaseUp(boolean up) {
        this.databaseUp.set(up);
    }

    public boolean isDatabaseUp() {
        return this.databaseUp.get();
    }

    public void setPendingExpired(long count) {
        this.pendingExpired.set(count);
    }

    @Override
    public Health health() {
        long currentExpired = pendingExpired.get();
        boolean dbStatus = databaseUp.get();

        if (!dbStatus) {
            return Health.down()
                    .withDetail("database", "PostgreSQL (DISCONNECTED - Simulated Failure)")
                    .withDetail("pendingExpired", currentExpired)
                    .withDetail("threshold", threshold)
                    .build();
        }

        if (currentExpired > threshold) {
            return Health.down()
                    .withDetail("database", "PostgreSQL (CONNECTED)")
                    .withDetail("pendingExpired", currentExpired)
                    .withDetail("threshold", threshold)
                    .withDetail("reason", "Seuil de reservations expirees depasse (> 50)")
                    .build();
        }

        return Health.up()
                .withDetail("database", "PostgreSQL (CONNECTED)")
                .withDetail("pendingExpired", currentExpired)
                .withDetail("threshold", threshold)
                .build();
    }
}
