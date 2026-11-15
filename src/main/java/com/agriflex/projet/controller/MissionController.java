package com.agriflex.projet.controller;

import com.agriflex.projet.model.DemandePersonnel;
import com.agriflex.projet.model.Travailleur;
import com.agriflex.projet.service.MatchingService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import java.util.List;

public class MissionController {

    @FXML private TableView<Travailleur> tableTravailleursCompatibles;

    private final MatchingService matchingService = new MatchingService();

    // Appelée quand l'entreprise sélectionne une mission pour voir les ouvriers disponibles
    public void afficherTravailleursCompatibles(DemandePersonnel demande) {
        List<Travailleur> resultats = matchingService.trouverTravailleursCompatibles(demande);
        ObservableList<Travailleur> observableList = FXCollections.observableArrayList(resultats);
        tableTravailleursCompatibles.setItems(observableList);
    }
}