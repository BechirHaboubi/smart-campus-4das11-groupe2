package com.esprit.candidat;

import com.esprit.candidat.entities.Candidat;
import com.esprit.candidat.repositories.CandidatRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class CandidatApplication {

    public static void main(String[] args) {
        SpringApplication.run(CandidatApplication.class, args);
    }

    @Bean
    CommandLineRunner start(CandidatRepository repository) {
        return args -> {
            repository.save(new Candidat("Trabelsi", "Dhia", "dhia@esprit.tn"));
            repository.save(new Candidat("Haboubi", "Bechir", "bechir@esprit.tn"));
            repository.save(new Candidat("Ben Ali", "Yassine", "yassine@esprit.tn"));
        };
    }
}
