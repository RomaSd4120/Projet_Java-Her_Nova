package com.agriflex.projet.dao;

import com.agriflex.projet.model.Competence;
import com.agriflex.projet.util.Singleton;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompetenceDAO {

    public boolean ajouter(Competence competence) {
        String sql = "INSERT INTO competence (nom_competence, description, id_travailleur) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, competence.getNomCompetence());
            ps.setString(2, competence.getDescription());
            ps.setInt(3, competence.getIdTravailleur());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Competence> getByTravailleur(int idTravailleur) {
        List<Competence> liste = new ArrayList<>();
        String sql = "SELECT * FROM competence WHERE id_travailleur = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTravailleur);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Competence c = new Competence(
                    rs.getInt("id_competence"),
                    rs.getString("nom_competence"),
                    rs.getString("description"),
                    rs.getInt("id_travailleur")
                );
                liste.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    public boolean supprimer(int idCompetence) {
        String sql = "DELETE FROM competence WHERE id_competence = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCompetence);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
