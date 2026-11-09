package com.esprit.reservations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsReservationsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsReservationsApplication.class, args);
    }
}
