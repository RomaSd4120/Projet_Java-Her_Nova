package com.agriflex.projet;

import com.agriflex.projet.dao.Singleton;
import java.sql.Connection;

public class App {
    public static void main(String[] args) {
        Connection conn = Singleton.getConnection();
        if (conn != null) {
            System.out.println("Test Singleton validé avec succès !");
        }
    }
}