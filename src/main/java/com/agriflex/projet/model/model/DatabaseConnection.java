package com.agriflex.projet.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Paramètres de connexion MySQL (ajustez le port, le nom de la BD, le user et le mot de passe si besoin)
    private static final String URL = "jdbc:mysql://localhost:3306/agriflex_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Mettez votre mot de passe Wamp/XAMPP si nécessaire

    private static Connection connection = null;

    // Méthode pour obtenir l'instance unique de connexion (Design Pattern Singleton recommandé)
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Chargement explicite du driver MySQL (optionnel avec les versions récentes mais recommandé)
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println(" Connexion à la base de données MySQL 'agriflex_db' établie avec succès !");
            }
        } catch (ClassNotFoundException e) {
            System.err.println(" Pilote JDBC MySQL introuvable : " + e.getMessage());
        } catch (SQLException e) {
            System.err.println(" Erreur de connexion à la base de données : " + e.getMessage());
        }
        return connection;
    }
}

