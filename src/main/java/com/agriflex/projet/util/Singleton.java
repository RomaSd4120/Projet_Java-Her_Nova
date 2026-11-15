package com.agriflex.projet.util; 

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class Singleton {
    private static Connection connection;

    private Singleton() {}

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                InputStream input = Singleton.class.getClassLoader().getResourceAsStream("config.properties");
                if (input == null) {
                    System.err.println("Impossible de trouver le fichier config.properties");
                    return null;
                }
                Properties prop = new Properties();
                prop.load(input);

                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(
                    prop.getProperty("db.url"),
                    prop.getProperty("db.user"),
                    prop.getProperty("db.password")
                );
                System.out.println("Connexion à la base de données 'agriflex_db' réussie !");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la connexion à la BDD : " + e.getMessage());
        }
        return connection;
    }
}