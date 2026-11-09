package com.esprit.job;

import com.esprit.job.entities.Job;
import com.esprit.job.repositories.JobRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class JobApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobApplication.class, args);
    }

    @Bean
    CommandLineRunner start(JobRepository repository) {
        return args -> {
            try {
                if (repository.count() == 0) {
                    repository.save(new Job("Ingénieur DevOps", true, "Poste Cloud & CI/CD"));
                    repository.save(new Job("Développeur FullStack", true, "Spring Boot & Angular"));
                    repository.save(new Job("Architecte Microservices", false, "Conception d'architectures distribuées"));
                }
            } catch (Exception ignored) {
            }
        };
    }
}
