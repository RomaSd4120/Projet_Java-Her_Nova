package com.agriflex.projet.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.agriflex.projet.model.Competence;
import com.agriflex.projet.util.GestionnaireErreurBDD;
import com.agriflex.projet.util.Singleton;

public class CompetenceDAO implements DAO<Competence> {

    @Override
    public boolean create(Competence competence) {
        if (competence == null) {
            return false;
        }
        String sql = "INSERT INTO competence (nom_competence, description, id_travailleur) VALUES (?, ?, ?)";
        Connection conn = Singleton.getConnectionOuLeverException("CompetenceDAO.create");

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, competence.getNomCompetence());
            stmt.setString(2, competence.getDescription());
            stmt.setInt(3, competence.getIdTravailleur());

            if (stmt.executeUpdate() > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        competence.setIdCompetence(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("CompetenceDAO.create", e);
        }
    }

    @Override
    public Competence findById(int id) {
        String sql = "SELECT * FROM competence WHERE id_competence = ?";
        Connection conn = Singleton.getConnectionOuLeverException("CompetenceDAO.findById");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapResultSet(rs) : null;
            }
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("CompetenceDAO.findById", e);
        }
    }

    @Override
    public List<Competence> findAll() {
        List<Competence> liste = new ArrayList<>();
        String sql = "SELECT * FROM competence ORDER BY nom_competence";
        Connection conn = Singleton.getConnectionOuLeverException("CompetenceDAO.findAll");

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                liste.add(mapResultSet(rs));
            }
            return liste;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("CompetenceDAO.findAll", e);
        }
    }

    @Override
    public boolean update(Competence competence) {
        if (competence == null) {
            return false;
        }
        String sql = "UPDATE competence SET nom_competence = ?, description = ?, id_travailleur = ? WHERE id_competence = ?";
        Connection conn = Singleton.getConnectionOuLeverException("CompetenceDAO.update");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, competence.getNomCompetence());
            stmt.setString(2, competence.getDescription());
            stmt.setInt(3, competence.getIdTravailleur());
            stmt.setInt(4, competence.getIdCompetence());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("CompetenceDAO.update", e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM competence WHERE id_competence = ?";
        Connection conn = Singleton.getConnectionOuLeverException("CompetenceDAO.delete");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("CompetenceDAO.delete", e);
        }
    }

    @Override
    public boolean exists(int id) {
        String sql = "SELECT 1 FROM competence WHERE id_competence = ?";
        Connection conn = Singleton.getConnectionOuLeverException("CompetenceDAO.exists");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("CompetenceDAO.exists", e);
        }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM competence";
        Connection conn = Singleton.getConnectionOuLeverException("CompetenceDAO.count");

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getLong(1) : 0L;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("CompetenceDAO.count", e);
        }
    }

    public List<Competence> findByTravailleurId(int idTravailleur) {
        List<Competence> liste = new ArrayList<>();
        String sql = "SELECT * FROM competence WHERE id_travailleur = ? ORDER BY nom_competence";
        Connection conn = Singleton.getConnectionOuLeverException("CompetenceDAO.findByTravailleurId");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idTravailleur);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    liste.add(mapResultSet(rs));
                }
            }
            return liste;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("CompetenceDAO.findByTravailleurId", e);
        }
    }

    private Competence mapResultSet(ResultSet rs) throws SQLException {
        Competence c = new Competence();
        c.setIdCompetence(rs.getInt("id_competence"));
        c.setNomCompetence(rs.getString("nom_competence"));
        c.setDescription(rs.getString("description"));
        c.setIdTravailleur(rs.getInt("id_travailleur"));
        return c;
    }
}