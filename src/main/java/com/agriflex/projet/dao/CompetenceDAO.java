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
        String sql = "INSERT INTO competence (libelle, id_candidat) VALUES (?, ?)";
        Connection conn = Singleton.getConnection();
        if (conn == null) {
            return false;
        }
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, competence.getNomCompetence());
            ps.setInt(2, competence.getIdTravailleur());
            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
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
        Connection conn = Singleton.getConnection();
        if (conn == null) {
            return null;
        }
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("CompetenceDAO.findById", e);
        }
    }

    @Override
    public List<Competence> findAll() {
        List<Competence> liste = new ArrayList<>();
        String sql = "SELECT * FROM competence";
        Connection conn = Singleton.getConnection();
        if (conn == null) {
            return liste;
        }
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
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
        String sql = "UPDATE competence SET libelle = ?, id_candidat = ? WHERE id_competence = ?";
        Connection conn = Singleton.getConnection();
        if (conn == null) {
            return false;
        }
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, competence.getNomCompetence());
            ps.setInt(2, competence.getIdTravailleur());
            ps.setInt(3, competence.getIdCompetence());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("CompetenceDAO.update", e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM competence WHERE id_competence = ?";
        Connection conn = Singleton.getConnection();
        if (conn == null) {
            return false;
        }
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("CompetenceDAO.delete", e);
        }
    }

    @Override
    public boolean exists(int id) {
        String sql = "SELECT 1 FROM competence WHERE id_competence = ?";
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
            throw GestionnaireErreurBDD.traduireException("CompetenceDAO.exists", e);
        }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM competence";
        Connection conn = Singleton.getConnection();
        if (conn == null) {
            return 0L;
        }
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getLong(1) : 0L;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("CompetenceDAO.count", e);
        }
    }

    public List<Competence> getByTravailleur(int idTravailleur) {
        List<Competence> liste = new ArrayList<>();
        String sql = "SELECT * FROM competence WHERE id_candidat = ?";
        Connection conn = Singleton.getConnection();
        if (conn == null) {
            return liste;
        }
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTravailleur);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    liste.add(mapResultSet(rs));
                }
            }
            return liste;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("CompetenceDAO.getByTravailleur", e);
        }
    }

    private Competence mapResultSet(ResultSet rs) throws SQLException {
        return new Competence(
            rs.getInt("id_competence"),
            rs.getString("libelle"),
            "",
            rs.getInt("id_candidat")
        );
    }
}