package com.agriflex.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/agriflex_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Succès de la connexion à la base de données AgriFlex !");
            return connection;
        } catch (SQLException e) {
            System.err.println("Échec de la connexion à la base de données : " + e.getMessage());
            return null;
        }
    }
}