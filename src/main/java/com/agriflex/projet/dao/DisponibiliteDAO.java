package com.agriflex.projet.dao;

import com.agriflex.projet.model.Disponibilite;
import com.agriflex.projet.util.Singleton;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DisponibiliteDAO implements DAO<Disponibilite> {

    @Override
    public boolean create(Disponibilite disp) {
        String sql = "INSERT INTO disponibilite (id_travailleur, date_debut, date_fin, statut) VALUES (?, ?, ?, ?)";
        try (Connection conn = Singleton.getConnection();
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

    @Override
    public Disponibilite findById(int id) {
        String sql = "SELECT * FROM disponibilite WHERE id_disponibilite = ?";
        try (Connection conn = Singleton.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Disponibilite(
                    rs.getInt("id_disponibilite"),
                    rs.getInt("id_travailleur"),
                    rs.getDate("date_debut").toLocalDate(),
                    rs.getDate("date_fin").toLocalDate(),
                    rs.getString("statut")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Disponibilite> findAll() {
        List<Disponibilite> liste = new ArrayList<>();
        String sql = "SELECT * FROM disponibilite";
        try (Connection conn = Singleton.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                liste.add(new Disponibilite(
                    rs.getInt("id_disponibilite"),
                    rs.getInt("id_travailleur"),
                    rs.getDate("date_debut").toLocalDate(),
                    rs.getDate("date_fin").toLocalDate(),
                    rs.getString("statut")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    @Override
    public boolean update(Disponibilite disp) {
        String sql = "UPDATE disponibilite SET id_travailleur = ?, date_debut = ?, date_fin = ?, statut = ? WHERE id_disponibilite = ?";
        try (Connection conn = Singleton.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, disp.getIdTravailleur());
            ps.setDate(2, Date.valueOf(disp.getDateDebut()));
            ps.setDate(3, Date.valueOf(disp.getDateFin()));
            ps.setString(4, disp.getStatut());
            ps.setInt(5, disp.getIdDisponibilite());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int idDisponibilite) {
        String sql = "DELETE FROM disponibilite WHERE id_disponibilite = ?";
        try (Connection conn = Singleton.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idDisponibilite);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public long count() {
        return findAll().size();
    }

    @Override
    public boolean exists(int id) {
        return findById(id) != null;
    }

    // Méthodes spécifiques au planning saisonnier des travailleurs
    public List<Disponibilite> getByTravailleur(int idTravailleur) {
        List<Disponibilite> liste = new ArrayList<>();
        String sql = "SELECT * FROM disponibilite WHERE id_travailleur = ?";
        try (Connection conn = Singleton.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTravailleur);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                liste.add(new Disponibilite(
                    rs.getInt("id_disponibilite"),
                    rs.getInt("id_travailleur"),
                    rs.getDate("date_debut").toLocalDate(),
                    rs.getDate("date_fin").toLocalDate(),
                    rs.getString("statut")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    public boolean updateStatut(int idDisponibilite, String nouveauStatut) {
        String sql = "UPDATE disponibilite SET statut = ? WHERE id_disponibilite = ?";
        try (Connection conn = Singleton.getConnection();
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
