
package com.agriflex.projet.dao;

import com.agriflex.projet.model.EntrepriseAgricole;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EntrepriseAgricoleDAO implements DAO<EntrepriseAgricole> {

    @Override
    public boolean create(EntrepriseAgricole entreprise) {
        String sql = "INSERT INTO entreprise_agricole "
                + "(raison_sociale, contact, telephone, region, sites, statut) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = Singleton.getConnection();
        if (conn == null) return false;

        try (PreparedStatement stmt = conn.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {

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

        } catch (SQLException e) {
            System.err.println("Erreur create EntrepriseAgricole : " + e.getMessage());
        }

        return false;
    }

    @Override
    public EntrepriseAgricole findById(int id) {
        String sql = "SELECT * FROM entreprise_agricole WHERE id_entreprise = ?";

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
            System.err.println("Erreur findById EntrepriseAgricole : " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<EntrepriseAgricole> findAll() {
        List<EntrepriseAgricole> entreprises = new ArrayList<>();
        String sql = "SELECT * FROM entreprise_agricole";

        Connection conn = Singleton.getConnection();
        if (conn == null) return entreprises;

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                entreprises.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erreur findAll EntrepriseAgricole : " + e.getMessage());
        }

        return entreprises;
    }

    @Override
    public boolean update(EntrepriseAgricole entreprise) {
        String sql = "UPDATE entreprise_agricole SET raison_sociale = ?, "
                + "contact = ?, telephone = ?, region = ?, sites = ?, statut = ? "
                + "WHERE id_entreprise = ?";

        Connection conn = Singleton.getConnection();
        if (conn == null) return false;

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
            System.err.println("Erreur update EntrepriseAgricole : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM entreprise_agricole WHERE id_entreprise = ?";

        Connection conn = Singleton.getConnection();
        if (conn == null) return false;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur delete EntrepriseAgricole : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean exists(int id) {
        String sql = "SELECT 1 FROM entreprise_agricole WHERE id_entreprise = ?";

        Connection conn = Singleton.getConnection();
        if (conn == null) return false;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.err.println("Erreur exists EntrepriseAgricole : " + e.getMessage());
        }

        return false;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM entreprise_agricole";

        Connection conn = Singleton.getConnection();
        if (conn == null) return 0;

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }

        } catch (SQLException e) {
            System.err.println("Erreur count EntrepriseAgricole : " + e.getMessage());
        }

        return 0;
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

