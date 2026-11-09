package com.agriflex.projet.model;

public class Travailleur {
    private int idCandidat;
    private int idUtilisateur;
    private Integer idRegion; // Integer permet d'avoir la valeur null
    private String competences;
    private String experience;
    private String disponibilite;
    private boolean mobilite;

    public Travailleur() {}

    public Travailleur(int idCandidat, int idUtilisateur, Integer idRegion, String competences, String experience, String disponibilite, boolean mobilite) {
        this.idCandidat = idCandidat;
        this.idUtilisateur = idUtilisateur;
        this.idRegion = idRegion;
        this.competences = competences;
        this.experience = experience;
        this.disponibilite = disponibilite;
        this.mobilite = mobilite;
    }

    public Travailleur(int idUtilisateur, Integer idRegion, String competences, String experience, String disponibilite, boolean mobilite) {
        this.idUtilisateur = idUtilisateur;
        this.idRegion = idRegion;
        this.competences = competences;
        this.experience = experience;
        this.disponibilite = disponibilite;
        this.mobilite = mobilite;
    }

    public int getIdCandidat() { return idCandidat; }
    public void setIdCandidat(int idCandidat) { this.idCandidat = idCandidat; }

    public int getIdUtilisateur() { return idUtilisateur; }
    public void setIdUtilisateur(int idUtilisateur) { this.idUtilisateur = idUtilisateur; }

    public Integer getIdRegion() { return idRegion; }
    public void setIdRegion(Integer idRegion) { this.idRegion = idRegion; }

    public String getCompetences() { return competences; }
    public void setCompetences(String competences) { this.competences = competences; }

    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }

    public String getDisponibilite() { return disponibilite; }
    public void setDisponibilite(String disponibilite) { this.disponibilite = disponibilite; }

    public boolean isMobilite() { return mobilite; }
    public void setMobilite(boolean mobilite) { this.mobilite = mobilite; }
}