package com.agriflex.projet.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Singleton {
    private static Connection connection;

    private Singleton() {}

    public static Connection getConnection() {
        if (connection == null) {
            try (InputStream input = Singleton.class.getClassLoader().getResourceAsStream("config.properties")) {
                if (input == null) {
                    System.err.println("Impossible de trouver le fichier config.properties");
                    return null;
                }
                Properties prop = new Properties();
                prop.load(input);

                String url = prop.getProperty("db.url");
                String user = prop.getProperty("db.user");
                String password = prop.getProperty("db.password");

                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Connexion à la base de données réussie !");
            } catch (Exception e) {
                System.err.println("Erreur lors de la connexion à la BDD : " + e.getMessage());
            }
        }
        return connection;
    }
}