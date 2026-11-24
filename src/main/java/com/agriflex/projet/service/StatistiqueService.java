package com.agriflex.projet.service;

import com.agriflex.projet.dao.RegionDAO;
import com.agriflex.projet.model.StatistiqueRegion;

import java.util.List;

public class StatistiqueService {
    private final RegionDAO regionDAO = new RegionDAO();

    public List<StatistiqueRegion> obtenirTableauDeBordRegional() {
        List<StatistiqueRegion> statistiques = regionDAO.getStatistiquesParRegion();
        return statistiques != null ? statistiques : List.of();
    }
}