package com.agriflex.projet.util;

import java.sql.SQLException;

public class GestionnaireErreurBDD {

    private static final int MYSQL_ERR_DUPLICATE_ENTRY = 1062;
    private static final int MYSQL_ERR_NO_REFERENCED_ROW = 1452;
    private static final int MYSQL_ERR_ROW_IS_REFERENCED = 1451;

    private GestionnaireErreurBDD() {
    }

    public static DatabaseException traduireException(String contexte, SQLException e) {
        String messageTechnique = (e != null && e.getMessage() != null) ? e.getMessage() : "Erreur SQL inconnue";
        System.err.println("[ERREUR BDD - " + contexte + "] " + messageTechnique);

        if (e == null) {
            return new DatabaseException("Une erreur de base de données est survenue (" + contexte + ").");
        }

        int codeErreur = e.getErrorCode();
        String sqlState = e.getSQLState();

        if (codeErreur == MYSQL_ERR_DUPLICATE_ENTRY || "23505".equals(sqlState)) {
            return new DatabaseException("Cet enregistrement existe déjà (doublon détecté).", e);
        }

        if (codeErreur == MYSQL_ERR_NO_REFERENCED_ROW) {
            return new DatabaseException("Référence introuvable : l'élément lié n'existe pas en base.", e);
        }

        if (codeErreur == MYSQL_ERR_ROW_IS_REFERENCED) {
            return new DatabaseException("Impossible de supprimer cet élément car il est utilisé ailleurs.", e);
        }

        if ("08001".equals(sqlState) || "08003".equals(sqlState) || "08006".equals(sqlState)) {
            return new DatabaseException("Connexion à la base de données indisponible ou interrompue.", e);
        }

        return new DatabaseException("Erreur lors de l'opération en base de données (" + contexte + ").", e);
    }

    public static void verifierConnexion(Object conn, String contexte) {
        if (conn == null) {
            throw new DatabaseException("Impossible d'établir la connexion à la base de données (" + contexte + ").");
        }
    }
}