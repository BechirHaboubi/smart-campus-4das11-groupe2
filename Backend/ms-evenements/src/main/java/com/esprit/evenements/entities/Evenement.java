package com.esprit.evenements.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "evenements")
public class Evenement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String description;
    private String dateEvenement;
    private String lieu;
    private String clubOrganisateur;
    private Integer placesDisponibles;

    public Evenement() {
    }

    public Evenement(String titre, String description, String dateEvenement, String lieu, String clubOrganisateur, Integer placesDisponibles) {
        this.titre = titre;
        this.description = description;
        this.dateEvenement = dateEvenement;
        this.lieu = lieu;
        this.clubOrganisateur = clubOrganisateur;
        this.placesDisponibles = placesDisponibles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateEvenement() {
        return dateEvenement;
    }

    public void setDateEvenement(String dateEvenement) {
        this.dateEvenement = dateEvenement;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getClubOrganisateur() {
        return clubOrganisateur;
    }

    public void setClubOrganisateur(String clubOrganisateur) {
        this.clubOrganisateur = clubOrganisateur;
    }

    public Integer getPlacesDisponibles() {
        return placesDisponibles;
    }

    public void setPlacesDisponibles(Integer placesDisponibles) {
        this.placesDisponibles = placesDisponibles;
    }
}
