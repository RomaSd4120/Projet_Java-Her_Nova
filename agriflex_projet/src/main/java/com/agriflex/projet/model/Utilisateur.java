package com.agriflex.projet.model;

import java.util.Objects;

public class Utilisateur {
    private int idUtilisateur;
    private String nom;
    private String prenom;
    private String telephone;
    private String motDePasseHashe;
    private String role;

    public Utilisateur() {}

    public Utilisateur(int idUtilisateur, String nom, String prenom, String telephone, String motDePasseHashe, String role) {
        this.idUtilisateur = idUtilisateur;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.motDePasseHashe = motDePasseHashe;
        this.role = role;
    }

    public Utilisateur(String nom, String prenom, String telephone, String motDePasseHashe, String role) {
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.motDePasseHashe = motDePasseHashe;
        this.role = role;
    }

    public int getIdUtilisateur() { return idUtilisateur; }
    public void setIdUtilisateur(int idUtilisateur) { this.idUtilisateur = idUtilisateur; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getMotDePasseHashe() { return motDePasseHashe; }
    public void setMotDePasseHashe(String motDePasseHashe) { this.motDePasseHashe = motDePasseHashe; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    @Override
    public String toString() {
        return nom + " " + prenom + " (" + role + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utilisateur user = (Utilisateur) o;
        return idUtilisateur == user.idUtilisateur && Objects.equals(telephone, user.telephone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUtilisateur, telephone);
    }
}