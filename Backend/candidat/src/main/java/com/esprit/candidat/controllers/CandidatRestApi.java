package com.esprit.candidat.controllers;

import com.esprit.candidat.entities.Candidat;
import com.esprit.candidat.repositories.CandidatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/candidats")
public class CandidatRestApi {

    @Autowired
    private CandidatRepository candidatRepository;

    @GetMapping("/hello")
    public String sayHello() {
        return "Bonjour depuis le microservice Candidat !";
    }

    @GetMapping
    public List<Candidat> getAllCandidats() {
        return candidatRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Candidat> getCandidatById(@PathVariable Long id) {
        Optional<Candidat> candidat = candidatRepository.findById(id);
        return candidat.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Candidat createCandidat(@RequestBody Candidat candidat) {
        return candidatRepository.save(candidat);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Candidat> updateCandidat(@PathVariable Long id, @RequestBody Candidat candidatDetails) {
        Optional<Candidat> optionalCandidat = candidatRepository.findById(id);
        if (optionalCandidat.isPresent()) {
            Candidat existing = optionalCandidat.get();
            existing.setNom(candidatDetails.getNom());
            existing.setPrenom(candidatDetails.getPrenom());
            existing.setEmail(candidatDetails.getEmail());
            return ResponseEntity.ok(candidatRepository.save(existing));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidat(@PathVariable Long id) {
        if (candidatRepository.existsById(id)) {
            candidatRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
