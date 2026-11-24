package com.agriflex.projet.service;

import java.time.LocalDate;

import com.agriflex.projet.dao.AffectationDAO;
import com.agriflex.projet.dao.DemandePersonnelDAO;
import com.agriflex.projet.dao.TravailleurDAO;
import com.agriflex.projet.model.Affectation;
import com.agriflex.projet.model.DemandePersonnel;
import com.agriflex.projet.model.Travailleur;

public class AffectationService {
    private final AffectationDAO affectationDAO = new AffectationDAO();
    private final TravailleurDAO travailleurDAO = new TravailleurDAO();
    private final DemandePersonnelDAO demandeDAO = new DemandePersonnelDAO();
    private final NotificationService notificationService = new NotificationService();

    public boolean proposerMission(int idCandidat, int idMission) {
        if (idCandidat <= 0 || idMission <= 0) {
            return false;
        }

        DemandePersonnel mission = demandeDAO.findById(idMission);
        if (mission == null) {
            return false;
        }

        if (affectationDAO.aUnConflitDeDates(idCandidat, mission.getDateDebut(), mission.getDateFin())) {
            throw new IllegalStateException("Ce travailleur est déjà affecté sur cette période.");
        }

        Affectation affectation = new Affectation();
        affectation.setIdCandidat(idCandidat);
        affectation.setIdMission(idMission);
        affectation.setDateProposition(LocalDate.now());
        affectation.setStatut("PROPOSEE");

        boolean cree = affectationDAO.create(affectation);
        if (cree) {
            String poste = mission.getPoste() != null ? mission.getPoste() : "Mission";
            String lieu = mission.getLieu() != null ? mission.getLieu() : "";
            String message = lieu.isBlank()
                    ? "Nouvelle proposition de mission : " + poste
                    : "Nouvelle proposition de mission : " + poste + " à " + lieu;

            notificationService.envoyerNotificationMission(idCandidat, idMission, message);
        }
        return cree;
    }

    public boolean repondreProposition(int idAffectation, boolean acceptee) {
        if (idAffectation <= 0) {
            return false;
        }

        Affectation affectation = affectationDAO.findById(idAffectation);
        if (affectation == null) {
            return false;
        }

        affectation.setStatut(acceptee ? "ACCEPTEE" : "REFUSEE");
        boolean maj = affectationDAO.update(affectation);

        if (maj && acceptee) {
            Travailleur travailleur = travailleurDAO.findById(affectation.getIdCandidat());
            if (travailleur != null) {
                travailleur.setDisponibilite("OCCUPE");
                travailleurDAO.update(travailleur);
            }
        }
        return maj;
    }
}