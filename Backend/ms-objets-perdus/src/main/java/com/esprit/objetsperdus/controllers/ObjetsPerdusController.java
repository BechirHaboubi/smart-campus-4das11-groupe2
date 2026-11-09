package com.esprit.objetsperdus.controllers;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ObjetsPerdusController {

    @GetMapping("/items")
    public List<Map<String, Object>> getItems() {
        return List.of(
            Map.of("id", "item-101", "type", "LOST", "title", "Veste noire", "category", "Vêtement", "placeText", "Salle B12", "status", "OPEN"),
            Map.of("id", "item-102", "type", "FOUND", "title", "Calculatrice TI-84", "category", "Électronique", "placeText", "Amphi A", "status", "MATCH_PROPOSED")
        );
    }

    @GetMapping("/items/{id}")
    public Map<String, Object> getItemById(@PathVariable String id) {
        return Map.of("id", id, "type", "LOST", "title", "Veste noire", "description", "Veste Zara taille M", "status", "OPEN");
    }

    @PostMapping("/items")
    public Map<String, Object> declareItem(@RequestBody Map<String, Object> payload) {
        Map<String, Object> res = new HashMap<>(payload);
        res.put("id", "item-" + UUID.randomUUID().toString().substring(0, 8));
        res.put("status", "OPEN");
        return res;
    }

    @PostMapping("/items/{id}/claims")
    public Map<String, Object> submitClaim(@PathVariable String id, @RequestBody Map<String, Object> claim) {
        return Map.of("itemId", id, "claimStatus", "CLAIM_PENDING", "message", "Réclamation enregistrée pour vérification.");
    }
}
