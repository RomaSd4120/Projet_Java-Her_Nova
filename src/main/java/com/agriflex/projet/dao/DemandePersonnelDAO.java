
package com.agriflex.projet.dao;

import com.agriflex.projet.model.DemandePersonnel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class DemandePersonnelDAO implements DAO<DemandePersonnel> {

    @Override
    public boolean create(DemandePersonnel demande) {
        String sql = "INSERT INTO demande_personnel "
                + "(id_entreprise, poste, description, nombre_travailleurs, "
                + "lieu, date_debut, date_fin, competence_requise, statut) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = Singleton.getConnection();
        if (conn == null) return false;

        try (PreparedStatement stmt = conn.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, demande.getIdEntreprise());
            stmt.setString(2, demande.getPoste());
            stmt.setString(3, demande.getDescription());
            stmt.setInt(4, demande.getNombreTravailleurs());
            stmt.setString(5, demande.getLieu());
            stmt.setDate(6, Date.valueOf(demande.getDateDebut()));
            stmt.setDate(7, Date.valueOf(demande.getDateFin()));
            stmt.setString(8, demande.getCompetenceRequise());
            stmt.setString(9, demande.getStatut());

            if (stmt.executeUpdate() > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        demande.setIdMission(rs.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erreur create DemandePersonnel : " + e.getMessage());
        }

        return false;
    }

    @Override
    public DemandePersonnel findById(int id) {
        String sql = "SELECT * FROM demande_personnel WHERE id_mission = ?";

        Connection conn = Singleton.getConnection();
        if (conn == null) return null;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur findById DemandePersonnel : " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<DemandePersonnel> findAll() {
        List<DemandePersonnel> demandes = new ArrayList<>();
        String sql = "SELECT * FROM demande_personnel";

        Connection conn = Singleton.getConnection();
        if (conn == null) return demandes;

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                demandes.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erreur findAll DemandePersonnel : " + e.getMessage());
        }

        return demandes;
    }

    @Override
    public boolean update(DemandePersonnel demande) {
        String sql = "UPDATE demande_personnel SET id_entreprise = ?, poste = ?, "
                + "description = ?, nombre_travailleurs = ?, lieu = ?, "
                + "date_debut = ?, date_fin = ?, competence_requise = ?, statut = ? "
                + "WHERE id_mission = ?";

        Connection conn = Singleton.getConnection();
        if (conn == null) return false;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, demande.getIdEntreprise());
            stmt.setString(2, demande.getPoste());
            stmt.setString(3, demande.getDescription());
            stmt.setInt(4, demande.getNombreTravailleurs());
            stmt.setString(5, demande.getLieu());
            stmt.setDate(6, Date.valueOf(demande.getDateDebut()));
            stmt.setDate(7, Date.valueOf(demande.getDateFin()));
            stmt.setString(8, demande.getCompetenceRequise());
            stmt.setString(9, demande.getStatut());
            stmt.setInt(10, demande.getIdMission());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur update DemandePersonnel : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM demande_personnel WHERE id_mission = ?";

        Connection conn = Singleton.getConnection();
        if (conn == null) return false;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur delete DemandePersonnel : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean exists(int id) {
        String sql = "SELECT 1 FROM demande_personnel WHERE id_mission = ?";

        Connection conn = Singleton.getConnection();
        if (conn == null) return false;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.err.println("Erreur exists DemandePersonnel : " + e.getMessage());
        }

        return false;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM demande_personnel";

        Connection conn = Singleton.getConnection();
        if (conn == null) return 0;

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }

        } catch (SQLException e) {
            System.err.println("Erreur count DemandePersonnel : " + e.getMessage());
        }

        return 0;
    }

    private DemandePersonnel mapResultSet(ResultSet rs) throws SQLException {
        DemandePersonnel demande = new DemandePersonnel();

        demande.setIdMission(rs.getInt("id_mission"));
        demande.setIdEntreprise(rs.getInt("id_entreprise"));
        demande.setPoste(rs.getString("poste"));
        demande.setDescription(rs.getString("description"));
        demande.setNombreTravailleurs(rs.getInt("nombre_travailleurs"));
        demande.setLieu(rs.getString("lieu"));
        demande.setDateDebut(rs.getDate("date_debut").toLocalDate());
        demande.setDateFin(rs.getDate("date_fin").toLocalDate());
        demande.setCompetenceRequise(rs.getString("competence_requise"));
        demande.setStatut(rs.getString("statut"));

        return demande;
    }
}
