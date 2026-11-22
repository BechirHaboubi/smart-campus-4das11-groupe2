package tn.esprit.smartcampus.reservations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
@RequestMapping("/api/reservations")
public class ReservationsApplication {

    private static final Logger log = LoggerFactory.getLogger(ReservationsApplication.class);
    private final ReservationExpiryHealthIndicator healthIndicator;

    public ReservationsApplication(ReservationExpiryHealthIndicator healthIndicator) {
        this.healthIndicator = healthIndicator;
    }

    public static void main(String[] args) {
        SpringApplication.run(ReservationsApplication.class, args);
    }

    @GetMapping
    public Map<String, Object> listReservations() {
        log.info("Appel de listReservations (GET /api/reservations)");
        log.debug("DEBUG: Verification de l'etat des reservations en base...");
        return Map.of(
            "service", "MS-RESERVATIONS",
            "count", 2,
            "reservations", new String[]{"Reservation Salle A1", "Reservation Video-projecteur"}
        );
    }

    @GetMapping("/hello")
    public Map<String, String> hello() {
        log.info("Appel de /api/reservations/hello");
        log.debug("DEBUG: Trace detaillee de l'appel hello");
        return Map.of(
            "service", "MS-RESERVATIONS",
            "status", "UP",
            "message", "Service des réservations opérationnel"
        );
    }

    @PostMapping("/simulate-db")
    public Map<String, Object> simulateDatabase(@RequestParam(name = "up", defaultValue = "false") boolean up) {
        healthIndicator.setDatabaseUp(up);
        log.warn("SIMULATION: Etat base PostgreSQL modifie a UP = {}", up);
        return Map.of(
            "simulatedDatabaseUp", up,
            "message", up ? "PostgreSQL retabli (UP)" : "PostgreSQL en panne (DOWN)"
        );
    }

    @PostMapping("/simulate-expired")
    public Map<String, Object> simulateExpired(@RequestParam(name = "count", defaultValue = "60") long count) {
        healthIndicator.setPendingExpired(count);
        log.warn("SIMULATION: Nombre de reservations expirees modifie a {}", count);
        return Map.of(
            "pendingExpired", count,
            "threshold", 50,
            "healthStatus", count > 50 ? "DOWN" : "UP"
        );
    }
}
