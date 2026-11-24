package com.agriflex.projet.controller;

import com.agriflex.projet.dao.*;
import com.agriflex.projet.model.*;
import com.agriflex.projet.service.AudioService;
import com.agriflex.projet.util.Singleton;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;

public class CandidatController {

    @FXML private Label lblNomCandidat;
    @FXML private Label lblStatutActuel;
    @FXML private ComboBox<Region> cbRegion;
    @FXML private TextField txtCompetenceLibre;
    @FXML private ListView<String> listCompetences;

    @FXML private TableView<Affectation> tableAffectations;
    @FXML private TableColumn<Affectation, Integer> colIdAffectation;
    @FXML private TableColumn<Affectation, Integer> colIdMission;
    @FXML private TableColumn<Affectation, LocalDate> colDateProp;
    @FXML private TableColumn<Affectation, String> colStatutOffre;

    @FXML private Button btnEnregistrerAudio;
    @FXML private Button btnArreterAudio;
    @FXML private Button btnEcouterAudio;
    @FXML private Label lblStatutAudio;

    private final TravailleurDAO travailleurDAO = new TravailleurDAO();
    private final DisponibiliteDAO disponibiliteDAO = new DisponibiliteDAO();
    private final RegionDAO regionDAO = new RegionDAO();
    private final CompetenceDAO competenceDAO = new CompetenceDAO();
    private final AffectationDAO affectationDAO = new AffectationDAO();

    private Travailleur travailleurActuel;
    private Disponibilite disponibiliteActuelle;

    private final AudioService audioService = new AudioService();
    private String cheminFichierAudio;

    @FXML
    public void initialize() {
        colIdAffectation.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getIdAffectation()));
        colIdMission.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getIdMission()));
        colDateProp.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDateProposition()));
        colStatutOffre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatut()));
        cbRegion.setItems(FXCollections.observableArrayList(regionDAO.findAll()));
    }

    /**
     * Appelée par LoginController après validation OTP réussie
     */
    public void initData(Utilisateur user) {
        lblNomCandidat.setText("Bienvenue, " + user.getNom() + " " + user.getPrenom());

        // Récupérer ou créer automatiquement la fiche Travailleur liée à cet Utilisateur
        this.travailleurActuel = travailleurDAO.findByUtilisateurId(user.getIdUtilisateur());
        if (this.travailleurActuel == null) {
            Travailleur nouveau = new Travailleur(user.getIdUtilisateur(), null, "", "", "Disponible", true);
            travailleurDAO.create(nouveau);
            this.travailleurActuel = travailleurDAO.findByUtilisateurId(user.getIdUtilisateur());
        }

        // Sélectionner la région actuelle du travailleur si déjà enregistrée
        if (travailleurActuel != null && travailleurActuel.getIdRegion() != null) {
            Region r = regionDAO.findById(travailleurActuel.getIdRegion());
            if (r != null) {
                cbRegion.setValue(r);
            }
        }

        // Initialiser le chemin du fichier audio du candidat
        int idCand = (travailleurActuel != null) ? travailleurActuel.getIdCandidat() : 1;
        this.cheminFichierAudio = "audio_profils/candidat_" + idCand + ".wav";
        if (lblStatutAudio != null && new File(cheminFichierAudio).exists()) {
            lblStatutAudio.setText("Note vocale disponible ✅");
            lblStatutAudio.setStyle("-fx-text-fill: #2e7d32; -fx-font-weight: bold;");
        }

        chargerDisponibilite();
        chargerCompetences();
        chargerAffectations();
    }

    private void chargerDisponibilite() {
        if (travailleurActuel == null) return;
        int idCandidat = travailleurActuel.getIdCandidat();
        List<Disponibilite> disps = disponibiliteDAO.findByTravailleurId(idCandidat);

        if (!disps.isEmpty()) {
            disponibiliteActuelle = disps.get(0);
            mettreAJourBadgeStatut(disponibiliteActuelle.getStatut());
        } else {
            Disponibilite nouvelle = new Disponibilite(
                    idCandidat, LocalDate.now(), LocalDate.now().plusMonths(3), "Disponible"
            );
            disponibiliteDAO.create(nouvelle);
            List<Disponibilite> recharge = disponibiliteDAO.findByTravailleurId(idCandidat);
            if (!recharge.isEmpty()) {
                disponibiliteActuelle = recharge.get(0);
            }
            mettreAJourBadgeStatut("Disponible");
        }
    }

    private void mettreAJourBadgeStatut(String statut) {
        lblStatutActuel.setText(statut);
        if ("Disponible".equalsIgnoreCase(statut)) {
            lblStatutActuel.setStyle("-fx-background-color: #e8f5e9; -fx-text-fill: #2e7d32; -fx-padding: 5 12; -fx-background-radius: 15; -fx-font-weight: bold;");
        } else {
            lblStatutActuel.setStyle("-fx-background-color: #ffebee; -fx-text-fill: #c62828; -fx-padding: 5 12; -fx-background-radius: 15; -fx-font-weight: bold;");
        }
    }

    @FXML
    private void handleSetDisponible() {
        if (disponibiliteActuelle != null) {
            disponibiliteActuelle.setStatut("Disponible");
            if (disponibiliteDAO.update(disponibiliteActuelle)) {
                mettreAJourBadgeStatut("Disponible");
                if (travailleurActuel != null) {
                    travailleurActuel.setDisponibilite("Disponible");
                    travailleurDAO.update(travailleurActuel);
                }
            }
        }
    }

    @FXML
    private void handleSetIndisponible() {
        if (disponibiliteActuelle != null) {
            disponibiliteActuelle.setStatut("Indisponible");
            if (disponibiliteDAO.update(disponibiliteActuelle)) {
                mettreAJourBadgeStatut("Indisponible");
                if (travailleurActuel != null) {
                    travailleurActuel.setDisponibilite("Indisponible");
                    travailleurDAO.update(travailleurActuel);
                }
            }
        }
    }

    @FXML
    private void handleSaveRegion() {
        Region selected = cbRegion.getValue();
        if (selected != null && travailleurActuel != null) {
            travailleurActuel.setIdRegion(selected.getIdRegion());
            if (travailleurDAO.update(travailleurActuel)) {
                afficherAlerte("Région mise à jour", "Votre région actuelle est enregistrée : " + selected.getNomRegion());
            }
        }
    }

    @FXML private void handleAddTomates() { ajouterCompetence("Récolte de tomates", "Compétence sélectionnée via icône"); }
    @FXML private void handleAddAgrumes() { ajouterCompetence("Cueillette des agrumes", "Compétence sélectionnée via icône"); }
    @FXML private void handleAddTracteur() { ajouterCompetence("Conduite de tracteur", "Compétence sélectionnée via icône"); }

    @FXML
    private void handleAddCompetenceLibre() {
        String nom = txtCompetenceLibre.getText().trim();
        if (!nom.isEmpty()) {
            ajouterCompetence(nom, "Saisie manuelle");
            txtCompetenceLibre.clear();
        }
    }

    private void ajouterCompetence(String nom, String desc) {
        if (travailleurActuel == null) return;
        Competence c = new Competence(nom, desc, travailleurActuel.getIdCandidat());
        if (competenceDAO.create(c)) {
            chargerCompetences();
        }
    }

    private void chargerCompetences() {
        if (travailleurActuel == null) return;
        ObservableList<String> items = FXCollections.observableArrayList();
        for (Competence c : competenceDAO.getByTravailleur(travailleurActuel.getIdCandidat())) {
            items.add("✔ " + c.getNomCompetence());
        }
        listCompetences.setItems(items);
    }

    // ==================== PROFIL AUDIO / NOTE VOCALE ====================

    @FXML
    private void handleEnregistrerAudio() {
        try {
            if (cheminFichierAudio == null) {
                int idCand = (travailleurActuel != null) ? travailleurActuel.getIdCandidat() : 1;
                cheminFichierAudio = "audio_profils/candidat_" + idCand + ".wav";
            }
            audioService.demarrerEnregistrement(cheminFichierAudio);
            btnEnregistrerAudio.setDisable(true);
            btnArreterAudio.setDisable(false);
            lblStatutAudio.setText("🔴 Enregistrement en cours... Parlez dans le micro");
            lblStatutAudio.setStyle("-fx-text-fill: #d32f2f; -fx-font-weight: bold;");
        } catch (Exception e) {
            lblStatutAudio.setText("Erreur micro : " + e.getMessage());
        }
    }

    @FXML
    private void handleArreterAudio() {
        audioService.arreterEnregistrement();
        btnEnregistrerAudio.setDisable(false);
        btnArreterAudio.setDisable(true);
        lblStatutAudio.setText("Note vocale enregistrée avec succès ✅");
        lblStatutAudio.setStyle("-fx-text-fill: #2e7d32; -fx-font-weight: bold;");

        sauvegarderCheminAudioEnBase(cheminFichierAudio);
    }

    @FXML
    private void handleEcouterAudio() {
        try {
            audioService.lireAudio(cheminFichierAudio);
            lblStatutAudio.setText("▶ Lecture de votre note vocale...");
            lblStatutAudio.setStyle("-fx-text-fill: #1565c0; -fx-font-weight: bold;");
        } catch (Exception e) {
            lblStatutAudio.setText("Aucune note vocale trouvée (enregistrez d'abord).");
        }
    }

    private void sauvegarderCheminAudioEnBase(String chemin) {
        if (travailleurActuel == null) return;
        String sql = "UPDATE travailleur SET chemin_audio = ? WHERE id_candidat = ?";
        Connection conn = Singleton.getConnection();
        if (conn == null) return;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, chemin);
            ps.setInt(2, travailleurActuel.getIdCandidat());
            ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("Erreur sauvegarde chemin_audio : " + e.getMessage());
        }
    }

    // ==================== OFFRES DE MISSION ====================

    private void chargerAffectations() {
        if (travailleurActuel == null) return;
        tableAffectations.setItems(FXCollections.observableArrayList(
                affectationDAO.getByCandidat(travailleurActuel.getIdCandidat())
        ));
    }

    @FXML
    private void handleAccepterMission() {
        Affectation selected = tableAffectations.getSelectionModel().getSelectedItem();
        if (selected != null && affectationDAO.updateStatut(selected.getIdAffectation(), "Acceptée")) {
            chargerAffectations();
        }
    }

    @FXML
    private void handleRefuserMission() {
        Affectation selected = tableAffectations.getSelectionModel().getSelectedItem();
        if (selected != null && affectationDAO.updateStatut(selected.getIdAffectation(), "Refusée")) {
            chargerAffectations();
        }
    }

    @FXML
    private void handleDeconnexion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) lblNomCandidat.getScene().getWindow();
            stage.setTitle("AgriFlex — Connexion");
            stage.setScene(new javafx.scene.Scene(root));
            stage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void afficherAlerte(String titre, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}