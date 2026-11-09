package com.esprit.evenements.controllers;

import com.esprit.evenements.entities.Evenement;
import com.esprit.evenements.repositories.EvenementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/evenements")
public class EvenementRestApi {

    @Autowired
    private EvenementRepository evenementRepository;

    @GetMapping("/hello")
    public String sayHello() {
        return "Bienvenue sur le microservice Événements Smart Campus (Géré par Dhia) !";
    }

    @GetMapping
    public List<Evenement> getAllEvenements() {
        return evenementRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evenement> getEvenementById(@PathVariable Long id) {
        Optional<Evenement> ev = evenementRepository.findById(id);
        return ev.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Evenement createEvenement(@RequestBody Evenement evenement) {
        return evenementRepository.save(evenement);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evenement> updateEvenement(@PathVariable Long id, @RequestBody Evenement updated) {
        Optional<Evenement> optionalEv = evenementRepository.findById(id);
        if (optionalEv.isPresent()) {
            Evenement existing = optionalEv.get();
            existing.setTitre(updated.getTitre());
            existing.setDescription(updated.getDescription());
            existing.setDateEvenement(updated.getDateEvenement());
            existing.setLieu(updated.getLieu());
            existing.setClubOrganisateur(updated.getClubOrganisateur());
            existing.setPlacesDisponibles(updated.getPlacesDisponibles());
            return ResponseEntity.ok(evenementRepository.save(existing));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvenement(@PathVariable Long id) {
        if (evenementRepository.existsById(id)) {
            evenementRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
