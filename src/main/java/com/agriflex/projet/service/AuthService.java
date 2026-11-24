package com.agriflex.projet.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import com.agriflex.projet.dao.CodeOTPDAO;
import com.agriflex.projet.dao.UtilisateurDAO;
import com.agriflex.projet.model.CodeOTP;
import com.agriflex.projet.model.Utilisateur;

public class AuthService {
    private static final int DUREE_OTP_DEFAUT_MINUTES = 5;

    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    private final CodeOTPDAO codeOTPDAO = new CodeOTPDAO();
    private final SecureRandom secureRandom = new SecureRandom();
    private String dernierTelephoneOTP;

    public Utilisateur authentifierParTelephone(String telephone, String motDePasse) {
        if (telephone == null || telephone.trim().isEmpty() || motDePasse == null || motDePasse.isEmpty()) {
            return null;
        }

        Utilisateur utilisateur = utilisateurDAO.findByTelephone(telephone.trim());
        if (utilisateur == null) {
            return null;
        }

        if (motDePasse.equals(utilisateur.getMotDePasseHashe())) {
            return utilisateur;
        }
        return null;
    }

    public String genererEtEnregistrerOTP(String telephone, int dureeValiditeMinutes) {
        if (telephone == null || telephone.trim().isEmpty() || dureeValiditeMinutes <= 0) {
            return null;
        }

        this.dernierTelephoneOTP = telephone.trim();
        String code = String.valueOf(1000 + secureRandom.nextInt(9000));
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(dureeValiditeMinutes);

        CodeOTP otp = new CodeOTP();
        otp.setTelephone(telephone.trim());
        otp.setCode(code);
        otp.setDateExpiration(expiration);
        otp.setEstUtilise(false);

        boolean cree = codeOTPDAO.create(otp);
        return cree ? code : null;
    }

    public boolean validerEtConsommerOTP(String telephone, String codeSaisi) {
        if (telephone == null || telephone.trim().isEmpty() || codeSaisi == null || codeSaisi.trim().isEmpty()) {
            return false;
        }

        CodeOTP otpValide = codeOTPDAO.verifierCode(telephone.trim(), codeSaisi.trim());
        if (otpValide == null) {
            return false;
        }

        return codeOTPDAO.marquerCommeUtilise(otpValide.getIdOtp());
    }
    public Utilisateur verifierIdentifiants(String cinOuTelephone, String motDePasse) {
    if (cinOuTelephone == null || cinOuTelephone.isBlank() || motDePasse == null || motDePasse.isEmpty()) {
        return null;
    }
    String identifiant = cinOuTelephone.trim();
    Utilisateur utilisateur = utilisateurDAO.findByTelephone(identifiant);
    if (utilisateur == null) {
        utilisateur = utilisateurDAO.findByCinAndPhone(identifiant, identifiant);
    }
    if (utilisateur != null && motDePasse.equals(utilisateur.getMotDePasseHashe())) {
        return utilisateur;
    }
    return null;
}

    public String genererCodeOTP() {
        String code = String.valueOf(1000 + secureRandom.nextInt(9000));
        if (dernierTelephoneOTP != null && !dernierTelephoneOTP.isBlank()) {
            LocalDateTime expiration = LocalDateTime.now().plusMinutes(DUREE_OTP_DEFAUT_MINUTES);
            CodeOTP otp = new CodeOTP();
            otp.setTelephone(dernierTelephoneOTP.trim());
            otp.setCode(code);
            otp.setDateExpiration(expiration);
            otp.setEstUtilise(false);
            codeOTPDAO.create(otp);
        }
        return code;
    }

    public String genererCodeOTP(String telephone) {
        if (telephone != null && !telephone.isBlank()) {
            this.dernierTelephoneOTP = telephone.trim();
        }
        return genererEtEnregistrerOTP(this.dernierTelephoneOTP, DUREE_OTP_DEFAUT_MINUTES);
    }

    public boolean validerOTP(String telephone, String codeSaisi) {
        return validerEtConsommerOTP(telephone, codeSaisi);
    }
}