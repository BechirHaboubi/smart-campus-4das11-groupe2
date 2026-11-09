package com.esprit.objetsperdus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsObjetsPerdusApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsObjetsPerdusApplication.class, args);
    }
}
