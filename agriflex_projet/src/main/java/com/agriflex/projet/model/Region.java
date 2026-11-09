package com.agriflex.projet.model;

import java.util.Objects;

public class Region {
    private int idRegion;
    private String nomRegion;

    public Region() {}

    public Region(int idRegion, String nomRegion) {
        this.idRegion = idRegion;
        this.nomRegion = nomRegion;
    }

    public Region(String nomRegion) {
        this.nomRegion = nomRegion;
    }

    public int getIdRegion() { return idRegion; }
    public void setIdRegion(int idRegion) { this.idRegion = idRegion; }

    public String getNomRegion() { return nomRegion; }
    public void setNomRegion(String nomRegion) { this.nomRegion = nomRegion; }

    @Override
    public String toString() { return nomRegion; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Region region = (Region) o;
        return idRegion == region.idRegion;
    }

    @Override
    public int hashCode() { return Objects.hash(idRegion); }
} 