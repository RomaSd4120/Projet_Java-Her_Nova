
package com.agriflex.projet.dao;

import com.agriflex.projet.model.Utilisateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO implements DAO<Utilisateur> {

    @Override
    public boolean create(Utilisateur utilisateur) {
        String sql = "INSERT INTO utilisateur "
                + "(nom, prenom, telephone, mot_de_passe_hashe, role) "
                + "VALUES (?, ?, ?, ?, ?)";

        Connection conn = Singleton.getConnection();

        if (conn == null) {
            return false;
        }

        try (PreparedStatement stmt = conn.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, utilisateur.getNom());
            stmt.setString(2, utilisateur.getPrenom());
            stmt.setString(3, utilisateur.getTelephone());
            stmt.setString(4, utilisateur.getMotDePasseHashe());
            stmt.setString(5, utilisateur.getRole());

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        utilisateur.setIdUtilisateur(rs.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erreur create Utilisateur : " + e.getMessage());
        }

        return false;
    }

    @Override
    public Utilisateur findById(int id) {
        String sql = "SELECT * FROM utilisateur WHERE id_utilisateur = ?";

        Connection conn = Singleton.getConnection();

        if (conn == null) {
            return null;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur findById Utilisateur : " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<Utilisateur> findAll() {
        List<Utilisateur> utilisateurs = new ArrayList<>();

        String sql = "SELECT * FROM utilisateur";

        Connection conn = Singleton.getConnection();

        if (conn == null) {
            return utilisateurs;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                utilisateurs.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erreur findAll Utilisateur : " + e.getMessage());
        }

        return utilisateurs;
    }

    @Override
    public boolean update(Utilisateur utilisateur) {
        String sql = "UPDATE utilisateur SET nom = ?, prenom = ?, "
                + "telephone = ?, mot_de_passe_hashe = ?, role = ? "
                + "WHERE id_utilisateur = ?";

        Connection conn = Singleton.getConnection();

        if (conn == null) {
            return false;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, utilisateur.getNom());
            stmt.setString(2, utilisateur.getPrenom());
            stmt.setString(3, utilisateur.getTelephone());
            stmt.setString(4, utilisateur.getMotDePasseHashe());
            stmt.setString(5, utilisateur.getRole());
            stmt.setInt(6, utilisateur.getIdUtilisateur());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur update Utilisateur : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM utilisateur WHERE id_utilisateur = ?";

        Connection conn = Singleton.getConnection();

        if (conn == null) {
            return false;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur delete Utilisateur : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean exists(int id) {
        String sql = "SELECT 1 FROM utilisateur WHERE id_utilisateur = ?";

        Connection conn = Singleton.getConnection();

        if (conn == null) {
            return false;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.err.println("Erreur exists Utilisateur : " + e.getMessage());
        }

        return false;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM utilisateur";

        Connection conn = Singleton.getConnection();

        if (conn == null) {
            return 0;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }

        } catch (SQLException e) {
            System.err.println("Erreur count Utilisateur : " + e.getMessage());
        }

        return 0;
    }

    // Conversion du ResultSet vers un objet Utilisateur
    private Utilisateur mapResultSet(ResultSet rs) throws SQLException {
        Utilisateur utilisateur = new Utilisateur();

        utilisateur.setIdUtilisateur(rs.getInt("id_utilisateur"));
        utilisateur.setNom(rs.getString("nom"));
        utilisateur.setPrenom(rs.getString("prenom"));
        utilisateur.setTelephone(rs.getString("telephone"));
        utilisateur.setMotDePasseHashe(rs.getString("mot_de_passe_hashe"));
        utilisateur.setRole(rs.getString("role"));

        return utilisateur;
    }
}

