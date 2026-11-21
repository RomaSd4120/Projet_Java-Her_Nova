
package com.agriflex.projet.model;

public class Competence {

    private int idCompetence;
    private String nomCompetence;
    private String description;
    private int idTravailleur;

    public Competence() {
    }

    public Competence(int idCompetence, String nomCompetence,
                      String description, int idTravailleur) {
        this.idCompetence = idCompetence;
        this.nomCompetence = nomCompetence;
        this.description = description;
        this.idTravailleur = idTravailleur;
    }

    public Competence(String nomCompetence, String description,
                      int idTravailleur) {
        this.nomCompetence = nomCompetence;
        this.description = description;
        this.idTravailleur = idTravailleur;
    }

    public int getIdCompetence() {
        return idCompetence;
    }

    public void setIdCompetence(int idCompetence) {
        this.idCompetence = idCompetence;
    }

    public String getNomCompetence() {
        return nomCompetence;
    }

    public void setNomCompetence(String nomCompetence) {
        this.nomCompetence = nomCompetence;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIdTravailleur() {
        return idTravailleur;
    }

    public void setIdTravailleur(int idTravailleur) {
        this.idTravailleur = idTravailleur;
    }
}
