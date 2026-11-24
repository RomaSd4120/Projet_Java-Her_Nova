package com.agriflex.projet.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.agriflex.projet.model.EntrepriseAgricole;
import com.agriflex.projet.util.GestionnaireErreurBDD;
import com.agriflex.projet.util.Singleton;

public class EntrepriseAgricoleDAO implements DAO<EntrepriseAgricole> {

    @Override
    public boolean create(EntrepriseAgricole entreprise) {
        if (entreprise == null) {
            return false;
        }

        String sql = "INSERT INTO entreprise_agricole "
                + "(raison_sociale, contact, telephone, region, sites, statut) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = Singleton.getConnectionOuLeverException("EntrepriseAgricoleDAO.create");

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, entreprise.getRaisonSociale());
            stmt.setString(2, entreprise.getContact());
            stmt.setString(3, entreprise.getTelephone());
            stmt.setString(4, entreprise.getRegion());
            stmt.setString(5, entreprise.getSites());
            stmt.setString(6, entreprise.getStatut());

            if (stmt.executeUpdate() > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        entreprise.setIdEntreprise(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("EntrepriseAgricoleDAO.create", e);
        }
    }

    @Override
    public EntrepriseAgricole findById(int id) {
        String sql = "SELECT * FROM entreprise_agricole WHERE id_entreprise = ?";
        Connection conn = Singleton.getConnectionOuLeverException("EntrepriseAgricoleDAO.findById");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapResultSet(rs) : null;
            }
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("EntrepriseAgricoleDAO.findById", e);
        }
    }

    @Override
    public List<EntrepriseAgricole> findAll() {
        List<EntrepriseAgricole> liste = new ArrayList<>();
        String sql = "SELECT * FROM entreprise_agricole ORDER BY raison_sociale";
        Connection conn = Singleton.getConnectionOuLeverException("EntrepriseAgricoleDAO.findAll");

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                liste.add(mapResultSet(rs));
            }
            return liste;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("EntrepriseAgricoleDAO.findAll", e);
        }
    }

    @Override
    public boolean update(EntrepriseAgricole entreprise) {
        if (entreprise == null) {
            return false;
        }

        String sql = "UPDATE entreprise_agricole SET raison_sociale = ?, contact = ?, "
                + "telephone = ?, region = ?, sites = ?, statut = ? WHERE id_entreprise = ?";

        Connection conn = Singleton.getConnectionOuLeverException("EntrepriseAgricoleDAO.update");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entreprise.getRaisonSociale());
            stmt.setString(2, entreprise.getContact());
            stmt.setString(3, entreprise.getTelephone());
            stmt.setString(4, entreprise.getRegion());
            stmt.setString(5, entreprise.getSites());
            stmt.setString(6, entreprise.getStatut());
            stmt.setInt(7, entreprise.getIdEntreprise());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("EntrepriseAgricoleDAO.update", e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM entreprise_agricole WHERE id_entreprise = ?";
        Connection conn = Singleton.getConnectionOuLeverException("EntrepriseAgricoleDAO.delete");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("EntrepriseAgricoleDAO.delete", e);
        }
    }

    @Override
    public boolean exists(int id) {
        String sql = "SELECT 1 FROM entreprise_agricole WHERE id_entreprise = ?";
        Connection conn = Singleton.getConnectionOuLeverException("EntrepriseAgricoleDAO.exists");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("EntrepriseAgricoleDAO.exists", e);
        }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM entreprise_agricole";
        Connection conn = Singleton.getConnectionOuLeverException("EntrepriseAgricoleDAO.count");

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getLong(1) : 0L;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("EntrepriseAgricoleDAO.count", e);
        }
    }

    private EntrepriseAgricole mapResultSet(ResultSet rs) throws SQLException {
        EntrepriseAgricole entreprise = new EntrepriseAgricole();
        entreprise.setIdEntreprise(rs.getInt("id_entreprise"));
        entreprise.setRaisonSociale(rs.getString("raison_sociale"));
        entreprise.setContact(rs.getString("contact"));
        entreprise.setTelephone(rs.getString("telephone"));
        entreprise.setRegion(rs.getString("region"));
        entreprise.setSites(rs.getString("sites"));
        entreprise.setStatut(rs.getString("statut"));
        return entreprise;
    }
}