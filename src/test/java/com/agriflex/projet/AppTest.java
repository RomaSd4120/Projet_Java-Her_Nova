package com.agriflex.projet;

import java.sql.Connection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import com.agriflex.projet.dao.UtilisateurDAO;
import com.agriflex.projet.model.DemandePersonnel;
import com.agriflex.projet.model.Travailleur;
import com.agriflex.projet.model.Utilisateur;
import com.agriflex.projet.service.AuthService;
import com.agriflex.projet.service.MatchingService;
import com.agriflex.projet.util.Singleton;

public class AppTest {

    // 1. Test du Design Pattern Singleton (Connexion BDD)
    @Test
    public void testSingletonConnection() {
        Connection conn1 = Singleton.getConnection();
        Connection conn2 = Singleton.getConnection();

        assertNotNull("La connexion ne doit pas être nulle", conn1);
        assertSame("Singleton doit retourner la même instance de connexion", conn1, conn2);
    }

    // 2. Test de la couche DAO (Authentification CIN + Téléphone)
    @Test
    public void testUtilisateurDaoFindByCinAndPhone() {
        UtilisateurDAO dao = new UtilisateurDAO();

        // Test avec un utilisateur inexistant
        Utilisateur inexistant = dao.findByCinAndPhone("XX000000", "0600000000");
        assertNull("Doit retourner null pour un utilisateur inexistant", inexistant);
    }

    // 3. Test de la couche Service : AuthService (Génération et validation OTP)
    @Test
    public void testAuthServiceOtp() {
        AuthService authService = new AuthService();
        String telephone = "0612345678";

        // Vérification de la génération d'un code OTP à 4 chiffres
        String otp = authService.genererCodeOTP(telephone);
        assertNotNull("Le code OTP ne doit pas être null", otp);
        assertEquals("Le code OTP doit contenir exactement 4 chiffres", 4, otp.length());
        assertTrue("Le code OTP doit être uniquement numérique", otp.matches("\\d{4}"));

        // Vérification de la validation OTP
        assertTrue("La validation doit réussir avec le bon code", authService.validerOTP(telephone, otp));
        assertFalse("La validation doit échouer avec un mauvais code", authService.validerOTP(telephone, "0000"));
    }

    // 4. Test de la couche Service : MatchingService (Filtrage disponibilité + compétence + effectif)
    @Test
    public void testMatchingServiceFiltrage() {
        MatchingService matchingService = new MatchingService();

        DemandePersonnel demande = new DemandePersonnel();
        demande.setCompetenceRequise("Récolte");
        demande.setNombreTravailleurs(2);

        List<Travailleur> resultat = matchingService.trouverTravailleursCompatibles(demande);

        assertNotNull("La liste retournée ne doit pas être nulle", resultat);
        assertTrue("Le nombre de travailleurs ne doit pas dépasser l'effectif demandé", resultat.size() <= 2);

        for (Travailleur t : resultat) {
            assertEquals("Tous les travailleurs filtrés doivent être Disponibles", "Disponible", t.getDisponibilite());
        }
    }
}