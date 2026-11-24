package com.agriflex.projet.controller;

import java.util.List;

import com.agriflex.projet.model.DemandePersonnel;
import com.agriflex.projet.model.Travailleur;
import com.agriflex.projet.service.AffectationService;
import com.agriflex.projet.service.MatchingService;
import com.agriflex.projet.util.DatabaseException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;

public class MissionController {

    @FXML private TableView<Travailleur> tableTravailleursCompatibles;

    private final MatchingService matchingService = new MatchingService();
    private final AffectationService affectationService = new AffectationService();
    private DemandePersonnel missionSelectionnee;

    // Appelée quand l'entreprise sélectionne une mission pour voir les ouvriers disponibles
    public void afficherTravailleursCompatibles(DemandePersonnel demande) {
        this.missionSelectionnee = demande;

        if (tableTravailleursCompatibles == null) {
            return;
        }

        if (demande == null) {
            tableTravailleursCompatibles.setItems(FXCollections.observableArrayList());
            return;
        }

        try {
            List<Travailleur> resultats = matchingService.trouverTravailleursCompatibles(demande);
            ObservableList<Travailleur> observableList = FXCollections.observableArrayList(
                    resultats != null ? resultats : List.of()
            );
            tableTravailleursCompatibles.setItems(observableList);
        } catch (DatabaseException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur base de données", e.getMessage());
        }
    }

    // Exemple d'action liée à la sélection d'un travailleur dans le TableView
    @FXML
    public void proposerMissionAuTravailleurSelectionne() {
        if (tableTravailleursCompatibles == null || missionSelectionnee == null) {
            afficherAlerte(Alert.AlertType.WARNING, "Sélection requise",
                    "Veuillez sélectionner une mission et un travailleur.");
            return;
        }

        Travailleur travailleurSelectionne = tableTravailleursCompatibles.getSelectionModel().getSelectedItem();
        if (travailleurSelectionne == null) {
            afficherAlerte(Alert.AlertType.WARNING, "Travailleur non sélectionné",
                    "Veuillez sélectionner un travailleur dans la liste.");
            return;
        }

        try {
            boolean proposee = affectationService.proposerMission(
                    travailleurSelectionne.getIdCandidat(),
                    missionSelectionnee.getIdMission()
            );
            if (proposee) {
                afficherAlerte(Alert.AlertType.INFORMATION, "Proposition envoyée",
                        "La mission a bien été proposée au travailleur.");
                afficherTravailleursCompatibles(missionSelectionnee);
            } else {
                afficherAlerte(Alert.AlertType.ERROR, "Échec de la proposition",
                        "Impossible de proposer cette mission pour le moment.");
            }
        } catch (IllegalStateException e) {
            afficherAlerte(Alert.AlertType.WARNING, "Conflit de disponibilité", e.getMessage());
        } catch (DatabaseException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur base de données", e.getMessage());
        }
    }

    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}