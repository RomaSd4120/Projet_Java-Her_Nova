package com.agriflex.projet.util;



import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Singleton {
    private static Connection connection;

    private Singleton() {
    }

    public static synchronized Connection getConnection() {
        try {
            if (connection == null || connection.isClosed() || !connection.isValid(2)) {
                Properties prop = chargerConfiguration();
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(
                        prop.getProperty("db.url"),
                        prop.getProperty("db.user"),
                        prop.getProperty("db.password")
                );
                System.out.println("Connexion à la base de données 'agriflex_db' réussie !");
            }
            return connection;
        } catch (ClassNotFoundException e) {
            connection = null;
            System.err.println("[ERREUR BDD - Driver] Pilote MySQL introuvable : " + e.getMessage());
            return null;
        } catch (IOException e) {
            connection = null;
            System.err.println("[ERREUR BDD - Config] Lecture config.properties impossible : " + e.getMessage());
            return null;
        } catch (SQLException e) {
            connection = null;
            System.err.println("[ERREUR BDD - Connexion] Échec connexion BDD : " + e.getMessage());
            return null;
        }
    }

    public static Connection getConnectionOuLeverException(String contexte) throws DatabaseException {
        Connection conn = getConnection();
        if (conn == null) {
            throw new DatabaseException("Impossible de se connecter à la base de données (" + contexte + ").");
        }
        return conn;
    }

    private static Properties chargerConfiguration() throws IOException {
        try (InputStream input = Singleton.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IOException("Fichier config.properties introuvable dans le classpath.");
            }
            Properties prop = new Properties();
            prop.load(input);
            return prop;
        }
    }
}