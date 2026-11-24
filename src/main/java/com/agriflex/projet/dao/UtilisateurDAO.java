
package com.agriflex.projet.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.agriflex.projet.model.Utilisateur;
import com.agriflex.projet.util.GestionnaireErreurBDD;
import com.agriflex.projet.util.Singleton;

public class UtilisateurDAO implements DAO<Utilisateur> {

    @Override
    public boolean create(Utilisateur utilisateur) {
        if (utilisateur == null) {
            return false;
        }

        String sql = "INSERT INTO utilisateur "
                + "(nom, prenom, cin, telephone, mot_de_passe_hashe, role) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = Singleton.getConnectionOuLeverException("UtilisateurDAO.create");

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, utilisateur.getNom());
            stmt.setString(2, utilisateur.getPrenom());
            stmt.setString(3, utilisateur.getCin());
            stmt.setString(4, utilisateur.getTelephone());
            stmt.setString(5, utilisateur.getMotDePasseHashe());
            stmt.setString(6, utilisateur.getRole());

            if (stmt.executeUpdate() > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        utilisateur.setIdUtilisateur(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("UtilisateurDAO.create", e);
        }
    }

    @Override
    public Utilisateur findById(int id) {
        String sql = "SELECT * FROM utilisateur WHERE id_utilisateur = ?";
        Connection conn = Singleton.getConnectionOuLeverException("UtilisateurDAO.findById");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapResultSet(rs) : null;
            }
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("UtilisateurDAO.findById", e);
        }
    }

    @Override
    public List<Utilisateur> findAll() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM utilisateur ORDER BY id_utilisateur DESC";
        Connection conn = Singleton.getConnectionOuLeverException("UtilisateurDAO.findAll");

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                utilisateurs.add(mapResultSet(rs));
            }
            return utilisateurs;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("UtilisateurDAO.findAll", e);
        }
    }

    @Override
    public boolean update(Utilisateur utilisateur) {
        if (utilisateur == null) {
            return false;
        }

        String sql = "UPDATE utilisateur SET nom = ?, prenom = ?, cin = ?, "
                + "telephone = ?, mot_de_passe_hashe = ?, role = ? WHERE id_utilisateur = ?";

        Connection conn = Singleton.getConnectionOuLeverException("UtilisateurDAO.update");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, utilisateur.getNom());
            stmt.setString(2, utilisateur.getPrenom());
            stmt.setString(3, utilisateur.getCin());
            stmt.setString(4, utilisateur.getTelephone());
            stmt.setString(5, utilisateur.getMotDePasseHashe());
            stmt.setString(6, utilisateur.getRole());
            stmt.setInt(7, utilisateur.getIdUtilisateur());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("UtilisateurDAO.update", e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM utilisateur WHERE id_utilisateur = ?";
        Connection conn = Singleton.getConnectionOuLeverException("UtilisateurDAO.delete");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("UtilisateurDAO.delete", e);
        }
    }

    @Override
    public boolean exists(int id) {
        String sql = "SELECT 1 FROM utilisateur WHERE id_utilisateur = ?";
        Connection conn = Singleton.getConnectionOuLeverException("UtilisateurDAO.exists");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("UtilisateurDAO.exists", e);
        }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM utilisateur";
        Connection conn = Singleton.getConnectionOuLeverException("UtilisateurDAO.count");

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getLong(1) : 0L;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("UtilisateurDAO.count", e);
        }
    }

    public Utilisateur findByTelephone(String telephone) {
        if (telephone == null || telephone.isBlank()) {
            return null;
        }
        String sql = "SELECT * FROM utilisateur WHERE telephone = ?";
        Connection conn = Singleton.getConnectionOuLeverException("UtilisateurDAO.findByTelephone");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, telephone.trim());
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapResultSet(rs) : null;
            }
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("UtilisateurDAO.findByTelephone", e);
        }
    }
    public Utilisateur findByCinAndPhone(String cin, String telephone) {
    if (telephone == null || telephone.isBlank()) {
        return null;
    }

    String telClean = telephone.trim();
    String cinClean = null;
    boolean hasCin = false;
    if (cin != null && !cin.isBlank()) {
        cinClean = cin.trim();
        hasCin = true;
    }

    String sql = hasCin
            ? "SELECT * FROM utilisateur WHERE cin = ? AND telephone = ?"
            : "SELECT * FROM utilisateur WHERE telephone = ?";

    Connection conn = Singleton.getConnectionOuLeverException("UtilisateurDAO.findByCinAndPhone");

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        if (cinClean != null) {
            stmt.setString(1, cinClean);
            stmt.setString(2, telClean);
        } else {
            stmt.setString(1, telClean);
        }

        try (ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? mapResultSet(rs) : null;
        }
    } catch (SQLException e) {
        throw GestionnaireErreurBDD.traduireException("UtilisateurDAO.findByCinAndPhone", e);
    }
}

    private Utilisateur mapResultSet(ResultSet rs) throws SQLException {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdUtilisateur(rs.getInt("id_utilisateur"));
        utilisateur.setNom(rs.getString("nom"));
        utilisateur.setPrenom(rs.getString("prenom"));
        utilisateur.setCin(rs.getString("cin"));
        utilisateur.setTelephone(rs.getString("telephone"));
        utilisateur.setMotDePasseHashe(rs.getString("mot_de_passe_hashe"));
        utilisateur.setRole(rs.getString("role"));
        return utilisateur;
    }
}

