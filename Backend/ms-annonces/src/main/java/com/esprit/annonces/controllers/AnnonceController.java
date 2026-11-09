package com.esprit.annonces.controllers;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class AnnonceController {

    @GetMapping("/contents")
    public List<Map<String, Object>> getContents() {
        return List.of(
            Map.of("id", 1, "kind", "ANNOUNCEMENT", "title", "Hackathon ESPRIT 2026", "category", "EVENT", "status", "PUBLISHED"),
            Map.of("id", 2, "kind", "BLOG_POST", "title", "Retour sur la semaine d'intégration", "category", "OFFICIAL", "status", "PUBLISHED")
        );
    }

    @GetMapping("/contents/{id}")
    public Map<String, Object> getContentById(@PathVariable Long id) {
        return Map.of("id", id, "title", "Hackathon ESPRIT 2026", "body", "Inscriptions ouvertes pour le grand événement annuel.", "status", "PUBLISHED");
    }

    @PostMapping("/contents")
    public Map<String, Object> createContent(@RequestBody Map<String, Object> payload) {
        Map<String, Object> res = new HashMap<>(payload);
        res.put("id", 3);
        res.put("status", "PENDING_REVIEW");
        return res;
    }

    @PatchMapping("/contents/{id}/publish")
    public Map<String, Object> publishContent(@PathVariable Long id) {
        return Map.of("id", id, "status", "PUBLISHED", "message", "Contenu publié avec succès !");
    }
}
