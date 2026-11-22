package tn.esprit.smartcampus.annonces;

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
@RequestMapping("/api/annonces")
public class AnnoncesApplication {

    private static final Logger log = LoggerFactory.getLogger(AnnoncesApplication.class);
    private final ScheduledContentHealthIndicator healthIndicator;

    public AnnoncesApplication(ScheduledContentHealthIndicator healthIndicator) {
        this.healthIndicator = healthIndicator;
    }

    public static void main(String[] args) {
        SpringApplication.run(AnnoncesApplication.class, args);
    }

    @GetMapping
    public Map<String, Object> listAnnonces() {
        log.info("Appel de listAnnonces (GET /api/annonces)");
        return Map.of(
            "service", "MS-ANNONCES",
            "annonces", new String[]{"Nouvelle cantine ouverte", "Semaine de revisions"}
        );
    }

    @GetMapping("/hello")
    public Map<String, String> hello() {
        log.info("Appel de /api/annonces/hello");
        return Map.of(
            "service", "MS-ANNONCES",
            "status", "UP",
            "message", "Service des annonces et blog opérationnel"
        );
    }

    @PostMapping("/simulate-db")
    public Map<String, Object> simulateDatabase(@RequestParam(name = "up", defaultValue = "false") boolean up) {
        healthIndicator.setDatabaseUp(up);
        log.warn("SIMULATION: Etat base H2 modifie a UP = {}", up);
        return Map.of(
            "simulatedDatabaseUp", up,
            "message", up ? "H2 retabli (UP)" : "H2 en panne (DOWN)"
        );
    }
}
