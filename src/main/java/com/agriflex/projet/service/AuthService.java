package com.agriflex.projet.service;

import com.agriflex.projet.dao.UtilisateurDAO;
import com.agriflex.projet.model.Utilisateur;
import java.util.Random;

public class AuthService {
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    
    public Utilisateur verifierIdentifiants(String cin, String telephone) {
        if (cin == null || cin.trim().isEmpty() || telephone == null || telephone.trim().isEmpty()) {
            return null;
        }
        return utilisateurDAO.findByCinAndPhone(cin.trim(), telephone.trim());
    }

    
    public String genererCodeOTP() {
        int code = 1000 + new Random().nextInt(9000);
        return String.valueOf(code);
    }

   
    public boolean validerOTP(String codeSaisi, String codeEnvoye) {
        return codeSaisi != null && codeSaisi.trim().equals(codeEnvoye);
    }
}