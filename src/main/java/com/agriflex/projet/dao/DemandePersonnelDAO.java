package com.agriflex.projet.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.agriflex.projet.model.DemandePersonnel;
import com.agriflex.projet.util.GestionnaireErreurBDD;
import com.agriflex.projet.util.Singleton;

public class DemandePersonnelDAO implements DAO<DemandePersonnel> {

    @Override
    public boolean create(DemandePersonnel demande) {
        if (demande == null) {
            return false;
        }

        String sql = "INSERT INTO demande_personnel "
                + "(id_entreprise, poste, description, nombre_travailleurs, lieu, "
                + "date_debut, date_fin, competence_requise, statut) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = Singleton.getConnectionOuLeverException("DemandePersonnelDAO.create");

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, demande.getIdEntreprise());
            stmt.setString(2, demande.getPoste());
            stmt.setString(3, demande.getDescription());
            stmt.setInt(4, demande.getNombreTravailleurs());
            stmt.setString(5, demande.getLieu());

            if (demande.getDateDebut() == null) {
                stmt.setNull(6, Types.DATE);
            } else {
                stmt.setDate(6, Date.valueOf(demande.getDateDebut()));
            }

            if (demande.getDateFin() == null) {
                stmt.setNull(7, Types.DATE);
            } else {
                stmt.setDate(7, Date.valueOf(demande.getDateFin()));
            }

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
            return false;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("DemandePersonnelDAO.create", e);
        }
    }

    @Override
    public DemandePersonnel findById(int id) {
        String sql = "SELECT * FROM demande_personnel WHERE id_mission = ?";
        Connection conn = Singleton.getConnectionOuLeverException("DemandePersonnelDAO.findById");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapResultSet(rs) : null;
            }
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("DemandePersonnelDAO.findById", e);
        }
    }

    @Override
    public List<DemandePersonnel> findAll() {
        List<DemandePersonnel> demandes = new ArrayList<>();
        String sql = "SELECT * FROM demande_personnel ORDER BY id_mission DESC";
        Connection conn = Singleton.getConnectionOuLeverException("DemandePersonnelDAO.findAll");

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                demandes.add(mapResultSet(rs));
            }
            return demandes;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("DemandePersonnelDAO.findAll", e);
        }
    }

    @Override
    public boolean update(DemandePersonnel demande) {
        if (demande == null) {
            return false;
        }

        String sql = "UPDATE demande_personnel SET id_entreprise = ?, poste = ?, description = ?, "
                + "nombre_travailleurs = ?, lieu = ?, date_debut = ?, date_fin = ?, "
                + "competence_requise = ?, statut = ? WHERE id_mission = ?";

        Connection conn = Singleton.getConnectionOuLeverException("DemandePersonnelDAO.update");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, demande.getIdEntreprise());
            stmt.setString(2, demande.getPoste());
            stmt.setString(3, demande.getDescription());
            stmt.setInt(4, demande.getNombreTravailleurs());
            stmt.setString(5, demande.getLieu());

            if (demande.getDateDebut() == null) {
                stmt.setNull(6, Types.DATE);
            } else {
                stmt.setDate(6, Date.valueOf(demande.getDateDebut()));
            }

            if (demande.getDateFin() == null) {
                stmt.setNull(7, Types.DATE);
            } else {
                stmt.setDate(7, Date.valueOf(demande.getDateFin()));
            }

            stmt.setString(8, demande.getCompetenceRequise());
            stmt.setString(9, demande.getStatut());
            stmt.setInt(10, demande.getIdMission());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("DemandePersonnelDAO.update", e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM demande_personnel WHERE id_mission = ?";
        Connection conn = Singleton.getConnectionOuLeverException("DemandePersonnelDAO.delete");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("DemandePersonnelDAO.delete", e);
        }
    }

    @Override
    public boolean exists(int id) {
        String sql = "SELECT 1 FROM demande_personnel WHERE id_mission = ?";
        Connection conn = Singleton.getConnectionOuLeverException("DemandePersonnelDAO.exists");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("DemandePersonnelDAO.exists", e);
        }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM demande_personnel";
        Connection conn = Singleton.getConnectionOuLeverException("DemandePersonnelDAO.count");

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getLong(1) : 0L;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("DemandePersonnelDAO.count", e);
        }
    }

    private DemandePersonnel mapResultSet(ResultSet rs) throws SQLException {
        DemandePersonnel demande = new DemandePersonnel();
        demande.setIdMission(rs.getInt("id_mission"));
        demande.setIdEntreprise(rs.getInt("id_entreprise"));
        demande.setPoste(rs.getString("poste"));
        demande.setDescription(rs.getString("description"));
        demande.setNombreTravailleurs(rs.getInt("nombre_travailleurs"));
        demande.setLieu(rs.getString("lieu"));

        Date dateDebut = rs.getDate("date_debut");
        demande.setDateDebut(dateDebut == null ? null : dateDebut.toLocalDate());

        Date dateFin = rs.getDate("date_fin");
        demande.setDateFin(dateFin == null ? null : dateFin.toLocalDate());

        demande.setCompetenceRequise(rs.getString("competence_requise"));
        demande.setStatut(rs.getString("statut"));
        return demande;
    }
}