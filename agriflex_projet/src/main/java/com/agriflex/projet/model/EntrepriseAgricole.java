package com.agriflex.projet.model;

public class EntrepriseAgricole {
    private int idEntreprise;
    private String raisonSociale;
    private String contact;
    private String telephone;
    private String region;
    private String sites;
    private String statut;

    public EntrepriseAgricole() {}

    public EntrepriseAgricole(int idEntreprise, String raisonSociale, String contact, String telephone, String region, String sites, String statut) {
        this.idEntreprise = idEntreprise;
        this.raisonSociale = raisonSociale;
        this.contact = contact;
        this.telephone = telephone;
        this.region = region;
        this.sites = sites;
        this.statut = statut;
    }

    public EntrepriseAgricole(String raisonSociale, String contact, String telephone, String region, String sites, String statut) {
        this.raisonSociale = raisonSociale;
        this.contact = contact;
        this.telephone = telephone;
        this.region = region;
        this.sites = sites;
        this.statut = statut;
    }

    public int getIdEntreprise() { return idEntreprise; }
    public void setIdEntreprise(int idEntreprise) { this.idEntreprise = idEntreprise; }

    public String getRaisonSociale() { return raisonSociale; }
    public void setRaisonSociale(String raisonSociale) { this.raisonSociale = raisonSociale; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getSites() { return sites; }
    public void setSites(String sites) { this.sites = sites; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    @Override
    public String toString() { return raisonSociale; }
}