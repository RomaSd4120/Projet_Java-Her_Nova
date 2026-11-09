package com.agriflex.projet.model;

import java.time.LocalDate;

public class DemandePersonnel {
    private int idMission;
    private int idEntreprise;
    private String poste;
    private String description;
    private int nombreTravailleurs;
    private String lieu;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String competenceRequise;
    private String statut;

    public DemandePersonnel() {}

    public DemandePersonnel(int idMission, int idEntreprise, String poste, String description, int nombreTravailleurs, String lieu, LocalDate dateDebut, LocalDate dateFin, String competenceRequise, String statut) {
        this.idMission = idMission;
        this.idEntreprise = idEntreprise;
        this.poste = poste;
        this.description = description;
        this.nombreTravailleurs = nombreTravailleurs;
        this.lieu = lieu;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.competenceRequise = competenceRequise;
        this.statut = statut;
    }

    public DemandePersonnel(int idEntreprise, String poste, String description, int nombreTravailleurs, String lieu, LocalDate dateDebut, LocalDate dateFin, String competenceRequise, String statut) {
        this.idEntreprise = idEntreprise;
        this.poste = poste;
        this.description = description;
        this.nombreTravailleurs = nombreTravailleurs;
        this.lieu = lieu;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.competenceRequise = competenceRequise;
        this.statut = statut;
    }

    public int getIdMission() { return idMission; }
    public void setIdMission(int idMission) { this.idMission = idMission; }

    public int getIdEntreprise() { return idEntreprise; }
    public void setIdEntreprise(int idEntreprise) { this.idEntreprise = idEntreprise; }

    public String getPoste() { return poste; }
    public void setPoste(String poste) { this.poste = poste; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getNombreTravailleurs() { return nombreTravailleurs; }
    public void setNombreTravailleurs(int nombreTravailleurs) { this.nombreTravailleurs = nombreTravailleurs; }

    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public String getCompetenceRequise() { return competenceRequise; }
    public void setCompetenceRequise(String competenceRequise) { this.competenceRequise = competenceRequise; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
}