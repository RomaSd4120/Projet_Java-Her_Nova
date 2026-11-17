package com.agriflex.projet.dao;

import com.agriflex.projet.model.Disponibilite;
import com.agriflex.projet.util.Singleton;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DisponibiliteDAO {

    public boolean ajouter(Disponibilite disp) {
        String sql = "INSERT INTO disponibilite (id_travailleur, date_debut, date_fin, statut) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, disp.getIdTravailleur());
            ps.setDate(2, Date.valueOf(disp.getDateDebut()));
            ps.setDate(3, Date.valueOf(disp.getDateFin()));
            ps.setString(4, disp.getStatut());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Disponibilite> getByTravailleur(int idTravailleur) {
        List<Disponibilite> liste = new ArrayList<>();
        String sql = "SELECT * FROM disponibilite WHERE id_travailleur = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTravailleur);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Disponibilite d = new Disponibilite(
                    rs.getInt("id_disponibilite"),
                    rs.getInt("id_travailleur"),
                    rs.getDate("date_debut").toLocalDate(),
                    rs.getDate("date_fin").toLocalDate(),
                    rs.getString("statut")
                );
                liste.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    public boolean updateStatut(int idDisponibilite, String nouveauStatut) {
        String sql = "UPDATE disponibilite SET statut = ? WHERE id_disponibilite = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nouveauStatut);
            ps.setInt(2, idDisponibilite);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
