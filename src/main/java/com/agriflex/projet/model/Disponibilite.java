
package com.agriflex.projet.model;

import java.time.LocalDate;

public class Disponibilite {

    private int idDisponibilite;
    private int idTravailleur;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String statut;

    public Disponibilite() {
    }

    public Disponibilite(int idDisponibilite, int idTravailleur,
                         LocalDate dateDebut, LocalDate dateFin,
                         String statut) {
        this.idDisponibilite = idDisponibilite;
        this.idTravailleur = idTravailleur;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = statut;
    }

    public Disponibilite(int idTravailleur, LocalDate dateDebut,
                         LocalDate dateFin, String statut) {
        this.idTravailleur = idTravailleur;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = statut;
    }

    public int getIdDisponibilite() {
        return idDisponibilite;
    }

    public void setIdDisponibilite(int idDisponibilite) {
        this.idDisponibilite = idDisponibilite;
    }

    public int getIdTravailleur() {
        return idTravailleur;
    }

    public void setIdTravailleur(int idTravailleur) {
        this.idTravailleur = idTravailleur;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
