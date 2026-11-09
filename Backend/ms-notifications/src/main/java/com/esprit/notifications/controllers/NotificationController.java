package com.esprit.notifications.controllers;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class NotificationController {

    @GetMapping("/notifications")
    public List<Map<String, Object>> getNotifications() {
        return List.of(
            Map.of("id", 1, "type", "RESERVATION", "title", "Réservation confirmée", "message", "Votre réservation pour l'Amphi A a été approuvée.", "read", false),
            Map.of("id", 2, "type", "EVENT", "title", "Rappel Événement", "message", "Le Hackathon ESPRIT commence demain à 09h00.", "read", true)
        );
    }

    @GetMapping("/notifications/{id}")
    public Map<String, Object> getNotificationById(@PathVariable Long id) {
        return Map.of("id", id, "title", "Réservation confirmée", "message", "Votre réservation pour l'Amphi A a été approuvée.");
    }

    @GetMapping("/notification-preferences")
    public Map<String, Object> getPreferences() {
        return Map.of("emailNotifications", true, "inAppNotifications", true, "smsReminders", false);
    }

    @PutMapping("/notification-preferences")
    public Map<String, Object> updatePreferences(@RequestBody Map<String, Object> prefs) {
        return prefs;
    }
}
