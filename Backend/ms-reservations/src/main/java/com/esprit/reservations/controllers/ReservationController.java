package com.esprit.reservations.controllers;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ReservationController {

    @GetMapping("/rooms")
    public List<Map<String, Object>> getRooms() {
        return List.of(
            Map.of("id", 1, "name", "Amphi A", "capacity", 200, "available", true),
            Map.of("id", 2, "name", "Salle B12", "capacity", 40, "available", true),
            Map.of("id", 3, "name", "Labo Informatique 3", "capacity", 25, "available", false)
        );
    }

    @GetMapping("/equipment-types")
    public List<Map<String, Object>> getEquipmentTypes() {
        return List.of(
            Map.of("id", 1, "name", "Vidéoprojecteur", "totalQuantity", 15),
            Map.of("id", 2, "name", "Micro sans-fil", "totalQuantity", 8),
            Map.of("id", 3, "name", "Tableau interactif", "totalQuantity", 5)
        );
    }

    @GetMapping("/availability")
    public Map<String, Object> checkAvailability(@RequestParam(defaultValue = "1") Long roomId) {
        return Map.of("roomId", roomId, "available", true, "message", "Créneau disponible");
    }

    @PostMapping("/reservations")
    public Map<String, Object> createReservation(@RequestBody Map<String, Object> payload) {
        Map<String, Object> res = new HashMap<>(payload);
        res.put("id", 100);
        res.put("status", "PENDING");
        return res;
    }
}
