package com.agriflex.projet.model;

import java.time.LocalDate;

public class Affectation {
    private int idAffectation;
    private int idCandidat;
    private int idMission;
    private LocalDate dateProposition;
    private String statut;

    public Affectation() {}

    public Affectation(int idAffectation, int idCandidat, int idMission, LocalDate dateProposition, String statut) {
        this.idAffectation = idAffectation;
        this.idCandidat = idCandidat;
        this.idMission = idMission;
        this.dateProposition = dateProposition;
        this.statut = statut;
    }

    public Affectation(int idCandidat, int idMission, LocalDate dateProposition, String statut) {
        this.idCandidat = idCandidat;
        this.idMission = idMission;
        this.dateProposition = dateProposition;
        this.statut = statut;
    }

    public int getIdAffectation() { return idAffectation; }
    public void setIdAffectation(int idAffectation) { this.idAffectation = idAffectation; }

    public int getIdCandidat() { return idCandidat; }
    public void setIdCandidat(int idCandidat) { this.idCandidat = idCandidat; }

    public int getIdMission() { return idMission; }
    public void setIdMission(int idMission) { this.idMission = idMission; }

    public LocalDate getDateProposition() { return dateProposition; }
    public void setDateProposition(LocalDate dateProposition) { this.dateProposition = dateProposition; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
}