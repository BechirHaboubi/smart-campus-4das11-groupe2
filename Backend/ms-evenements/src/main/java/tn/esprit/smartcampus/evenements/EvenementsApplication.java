package tn.esprit.smartcampus.evenements;

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
public class EvenementsApplication {

    private static final Logger log = LoggerFactory.getLogger(EvenementsApplication.class);
    private final WaitlistHealthIndicator healthIndicator;

    public EvenementsApplication(WaitlistHealthIndicator healthIndicator) {
        this.healthIndicator = healthIndicator;
    }

    public static void main(String[] args) {
        SpringApplication.run(EvenementsApplication.class, args);
    }

    @GetMapping("/api/events")
    public Map<String, Object> listEvents() {
        log.info("Appel de listEvents (GET /api/events)");
        return Map.of(
            "service", "MS-EVENEMENTS",
            "events", new String[]{"Hackathon AWD", "Workshop Cloud Spring", "Forum Entreprises"}
        );
    }

    @GetMapping("/api/evenements/hello")
    public Map<String, String> hello() {
        log.info("Appel de /api/evenements/hello");
        return Map.of(
            "service", "MS-EVENEMENTS",
            "status", "UP",
            "message", "Service des événements opérationnel"
        );
    }

    @PostMapping("/api/evenements/simulate-db")
    public Map<String, Object> simulateDatabase(@RequestParam(name = "up", defaultValue = "false") boolean up) {
        healthIndicator.setDatabaseUp(up);
        log.warn("SIMULATION: Etat base MySQL modifie a UP = {}", up);
        return Map.of(
            "simulatedDatabaseUp", up,
            "message", up ? "MySQL retabli (UP)" : "MySQL en panne (DOWN)"
        );
    }
}
