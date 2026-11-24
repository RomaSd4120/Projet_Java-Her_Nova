package com.agriflex.projet.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.agriflex.projet.dao.AffectationDAO;
import com.agriflex.projet.dao.TravailleurDAO;
import com.agriflex.projet.model.CritereRechercheTravailleur;
import com.agriflex.projet.model.DemandePersonnel;
import com.agriflex.projet.model.Travailleur;

public class MatchingService {
    private final TravailleurDAO travailleurDAO = new TravailleurDAO();
    private final AffectationDAO affectationDAO = new AffectationDAO();

    public List<Travailleur> rechercherTravailleurs(CritereRechercheTravailleur critere) {
        List<Travailleur> candidats = (critere == null)
                ? travailleurDAO.findAll()
                : travailleurDAO.rechercherParCriteres(critere);

        if (candidats == null || candidats.isEmpty()) {
            return List.of();
        }

        if (critere != null && (critere.getDateDebut() != null || critere.getDateFin() != null)) {
            LocalDate debut = (critere.getDateDebut() != null) ? critere.getDateDebut() : critere.getDateFin();
            LocalDate fin = (critere.getDateFin() != null) ? critere.getDateFin() : critere.getDateDebut();

            return candidats.stream()
                    .filter(t -> !affectationDAO.aUnConflitDeDates(t.getIdCandidat(), debut, fin))
                    .collect(Collectors.toList());
        }

        return candidats;
    }

    public List<Travailleur> trouverTravailleursPourMission(DemandePersonnel demande) {
        if (demande == null) {
            return List.of();
        }
        CritereRechercheTravailleur critere = new CritereRechercheTravailleur();
        critere.setCompetence(demande.getCompetenceRequise());
        critere.setDisponibilite("DISPONIBLE");
        critere.setDateDebut(demande.getDateDebut());
        critere.setDateFin(demande.getDateFin());

        return rechercherTravailleurs(critere).stream()
                .limit(Math.max(0, demande.getNombreTravailleurs()))
                .collect(Collectors.toList());
    }

    public List<Travailleur> trouverTravailleursCompatibles(DemandePersonnel demande) {
        return trouverTravailleursPourMission(demande);
    }
}