package com.agriflex.projet.model;

import java.time.LocalDate;

public class CritereRechercheTravailleur {
    private Integer idRegion;
    private String competence;
    private String disponibilite;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Boolean mobilite;

    public CritereRechercheTravailleur() {}

    public Integer getIdRegion() { return idRegion; }
    public void setIdRegion(Integer idRegion) { this.idRegion = idRegion; }

    public String getCompetence() { return competence; }
    public void setCompetence(String competence) { this.competence = competence; }

    public String getDisponibilite() { return disponibilite; }
    public void setDisponibilite(String disponibilite) { this.disponibilite = disponibilite; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public Boolean getMobilite() { return mobilite; }
    public void setMobilite(Boolean mobilite) { this.mobilite = mobilite; }
}