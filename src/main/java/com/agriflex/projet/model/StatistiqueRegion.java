package com.agriflex.projet.model;

import java.util.Objects;

public class StatistiqueRegion {
    private int idRegion;
    private String nomRegion;
    private int totalTravailleurs;
    private int travailleursDisponibles;
    private int missionsOuvertes;

    public StatistiqueRegion() {
    }

    public StatistiqueRegion(int idRegion, String nomRegion, int totalTravailleurs,
                             int travailleursDisponibles, int missionsOuvertes) {
        this.idRegion = idRegion;
        this.nomRegion = nomRegion;
        this.totalTravailleurs = totalTravailleurs;
        this.travailleursDisponibles = travailleursDisponibles;
        this.missionsOuvertes = missionsOuvertes;
    }

    public int getIdRegion() {
        return idRegion;
    }

    public void setIdRegion(int idRegion) {
        this.idRegion = idRegion;
    }

    public String getNomRegion() {
        return nomRegion;
    }

    public void setNomRegion(String nomRegion) {
        this.nomRegion = nomRegion;
    }

    public int getTotalTravailleurs() {
        return totalTravailleurs;
    }

    public void setTotalTravailleurs(int totalTravailleurs) {
        this.totalTravailleurs = totalTravailleurs;
    }

    public int getTravailleursDisponibles() {
        return travailleursDisponibles;
    }

    public void setTravailleursDisponibles(int travailleursDisponibles) {
        this.travailleursDisponibles = travailleursDisponibles;
    }

    public int getMissionsOuvertes() {
        return missionsOuvertes;
    }

    public void setMissionsOuvertes(int missionsOuvertes) {
        this.missionsOuvertes = missionsOuvertes;
    }

    @Override
    public String toString() {
        return "StatistiqueRegion{" +
                "idRegion=" + idRegion +
                ", nomRegion='" + nomRegion + '\'' +
                ", totalTravailleurs=" + totalTravailleurs +
                ", travailleursDisponibles=" + travailleursDisponibles +
                ", missionsOuvertes=" + missionsOuvertes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatistiqueRegion that)) return false;
        return idRegion == that.idRegion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRegion);
    }
}