package com.esprit.evenements;

import com.esprit.evenements.entities.Evenement;
import com.esprit.evenements.repositories.EvenementRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class MsEvenementsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsEvenementsApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(EvenementRepository repository) {
        return args -> {
            try {
                if (repository.count() == 0) {
                    repository.save(new Evenement(
                            "Hackathon ESPRIT 2026",
                            "Compétition d'innovation et développement de solutions Smart City",
                            "2026-12-15",
                            "Amphi B, ESPRIT",
                            "IEEE ESPRIT Student Branch",
                            150
                    ));
                    repository.save(new Evenement(
                            "Forum Entreprises & Stages",
                            "Rencontre des étudiants 4ème et 5ème année avec les recruteurs",
                            "2026-11-25",
                            "Hall Central",
                            "ESPRIT Career Center",
                            300
                    ));
                }
            } catch (Exception ignored) {
            }
        };
    }
}
