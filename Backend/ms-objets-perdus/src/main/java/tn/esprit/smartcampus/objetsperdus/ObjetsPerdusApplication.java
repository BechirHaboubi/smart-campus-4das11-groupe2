package tn.esprit.smartcampus.objetsperdus;

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
@RequestMapping("/api/objets-perdus")
public class ObjetsPerdusApplication {

    private static final Logger log = LoggerFactory.getLogger(ObjetsPerdusApplication.class);
    private final ClaimPendingHealthIndicator healthIndicator;

    public ObjetsPerdusApplication(ClaimPendingHealthIndicator healthIndicator) {
        this.healthIndicator = healthIndicator;
    }

    public static void main(String[] args) {
        SpringApplication.run(ObjetsPerdusApplication.class, args);
    }

    @GetMapping
    public Map<String, Object> listObjets() {
        log.info("Appel de listObjets (GET /api/objets-perdus)");
        return Map.of(
            "service", "MS-OBJETS-PERDUS",
            "objets", new String[]{"Badge etudiant n°123", "Gourde bleue"}
        );
    }

    @GetMapping("/hello")
    public Map<String, String> hello() {
        log.info("Appel de /api/objets-perdus/hello");
        return Map.of(
            "service", "MS-OBJETS-PERDUS",
            "status", "UP",
            "message", "Service des objets perdus opérationnel"
        );
    }

    @PostMapping("/simulate-db")
    public Map<String, Object> simulateDatabase(@RequestParam(name = "up", defaultValue = "false") boolean up) {
        healthIndicator.setDatabaseUp(up);
        log.warn("SIMULATION: Etat base MongoDB modifie a UP = {}", up);
        return Map.of(
            "simulatedDatabaseUp", up,
            "message", up ? "MongoDB retabli (UP)" : "MongoDB en panne (DOWN)"
        );
    }
}
