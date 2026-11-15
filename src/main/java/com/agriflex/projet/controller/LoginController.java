package com.agriflex.projet.controller;

import com.agriflex.projet.model.Utilisateur;
import com.agriflex.projet.service.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField txtCin;
    @FXML private TextField txtTelephone;
    @FXML private TextField txtCodeOtp;

    private final AuthService authService = new AuthService();
    private String codeOtpActuel;
    private Utilisateur utilisateurEnCours;

    // 1. Appelée au clic sur le bouton "Se connecter" / "Recevoir le code"
    @FXML
    public void handleConnexion() {
        String cin = txtCin.getText();
        String telephone = txtTelephone.getText();

        Utilisateur user = authService.verifierIdentifiants(cin, telephone);
        if (user != null) {
            this.utilisateurEnCours = user;
            this.codeOtpActuel = authService.genererCodeOTP();

            // Affiche le code dans une alerte pour faciliter ta démonstration le jour J
            afficherAlerte(Alert.AlertType.INFORMATION, "Code SMS envoyé",
                    "Votre code de vérification AgriFlex est : " + codeOtpActuel);
            
            // Ici : afficher la vue / le panneau de saisie OTP
        } else {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur d'authentification",
                    "CIN ou numéro de téléphone introuvable.");
        }
    }

    // 2. Appelée au clic sur le bouton "Valider le code"
    @FXML
    public void handleValidationOtp() {
        String codeSaisi = txtCodeOtp.getText();

        if (authService.validerOTP(codeSaisi, this.codeOtpActuel)) {
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès",
                    "Bienvenue " + utilisateurEnCours.getNom() + " !");
            

        } else {
            afficherAlerte(Alert.AlertType.ERROR, "Code invalide",
                    "Le code OTP saisi est incorrect.");
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