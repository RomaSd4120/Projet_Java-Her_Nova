package com.agriflex.projet.controller;

import com.agriflex.projet.model.Utilisateur;
import com.agriflex.projet.service.AuthService;
import com.agriflex.projet.util.DatabaseException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML private TextField txtTelephone;
    @FXML private PasswordField txtMotDePasse;
    @FXML private TextField txtCodeOtp;

    private final AuthService authService = new AuthService();
    private String telephoneActif;
    private Utilisateur utilisateurEnCours;

    // 1. Appelée au clic sur le bouton "Se connecter" / "Recevoir le code"
    @FXML
    public void handleConnexion() {
        String telephone = txtTelephone != null ? txtTelephone.getText().trim() : "";
        String motDePasse = txtMotDePasse != null ? txtMotDePasse.getText() : "";

        if (telephone.isEmpty() || motDePasse.isEmpty()) {
            afficherAlerte(Alert.AlertType.WARNING, "Champs requis",
                    "Veuillez saisir votre numéro de téléphone et votre mot de passe.");
            return;
        }

        try {
            Utilisateur user = authService.authentifierParTelephone(telephone, motDePasse);
            if (user != null) {
                this.utilisateurEnCours = user;
                this.telephoneActif = telephone;

                String codeOtp = authService.genererCodeOTP(telephone);
                if (codeOtp == null) {
                    afficherAlerte(Alert.AlertType.ERROR, "Erreur OTP",
                            "Impossible de générer et enregistrer le code OTP.");
                    return;
                }

                // Affiche le code dans une alerte pour faciliter la démonstration
                afficherAlerte(Alert.AlertType.INFORMATION, "Code SMS envoyé",
                        "Votre code de vérification AgriFlex est : " + codeOtp);
            } else {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur d'authentification",
                        "Numéro de téléphone ou mot de passe incorrect.");
            }
        } catch (DatabaseException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur base de données", e.getMessage());
        }
    }

    // 2. Appelée au clic sur le bouton "Valider le code"
    @FXML
    public void handleValidationOtp() {
        String codeSaisi = txtCodeOtp != null ? txtCodeOtp.getText().trim() : "";
        String telephone = (this.telephoneActif != null && !this.telephoneActif.isBlank())
                ? this.telephoneActif
                : (txtTelephone != null ? txtTelephone.getText().trim() : "");

        try {
            boolean valide = authService.validerOTP(telephone, codeSaisi);
            if (valide) {
                String nomAffichage = (utilisateurEnCours != null && utilisateurEnCours.getNom() != null)
                        ? utilisateurEnCours.getNom()
                        : telephone;
                afficherAlerte(Alert.AlertType.INFORMATION, "Succès",
                        "Bienvenue " + nomAffichage + " !");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/espace_candidat.fxml"));
                Parent root = loader.load();

                CandidatController controller = loader.getController();
                if (utilisateurEnCours != null) {
                    controller.initData(utilisateurEnCours);
                }

                Stage stage = (Stage) txtCodeOtp.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("AgriFlex — Espace Candidat");
                stage.show();
            } else {
                afficherAlerte(Alert.AlertType.ERROR, "Code invalide",
                        "Le code OTP saisi est incorrect, expiré ou déjà utilisé.");
            }
        } catch (DatabaseException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur base de données", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir l'Espace Candidat.");
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