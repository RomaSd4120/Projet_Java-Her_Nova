package com.agriflex.projet.service;

import com.agriflex.projet.dao.TravailleurDAO;
import com.agriflex.projet.model.DemandePersonnel;
import com.agriflex.projet.model.Travailleur;
import java.util.List;
import java.util.stream.Collectors;

public class MatchingService {
    private final TravailleurDAO travailleurDAO = new TravailleurDAO();

    public List<Travailleur> trouverTravailleursCompatibles(DemandePersonnel demande) {
        List<Travailleur> tousLesTravailleurs = travailleurDAO.findAll();

        return tousLesTravailleurs.stream()
                // 1. Vérifier la disponibilité (String)
                .filter(t -> t.getDisponibilite() != null && 
                             t.getDisponibilite().equalsIgnoreCase("Disponible"))
                // 2. Vérifier les compétences requises
                .filter(t -> demande.getCompetenceRequise() == null || 
                            (t.getCompetences() != null && 
                             t.getCompetences().toLowerCase().contains(demande.getCompetenceRequise().toLowerCase())))
                // 3. Limiter au nombre de travailleurs demandés
                .limit(demande.getNombreTravailleurs())
                .collect(Collectors.toList());
    }
}