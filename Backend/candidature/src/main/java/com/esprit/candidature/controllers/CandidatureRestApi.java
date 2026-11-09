package com.esprit.candidature.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/candidatures")
public class CandidatureRestApi {

    @GetMapping("/hello")
    public String sayHello() {
        return "Bonjour depuis le microservice Candidature !";
    }

    @GetMapping
    public Map<String, Object> listSummary() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "Candidature");
        response.put("status", "UP");
        response.put("total", 0);
        return response;
    }
}
