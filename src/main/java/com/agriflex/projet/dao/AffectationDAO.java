package com.agriflex.projet.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.agriflex.projet.model.Affectation;
import com.agriflex.projet.util.GestionnaireErreurBDD;
import com.agriflex.projet.util.Singleton;

public class AffectationDAO implements DAO<Affectation> {

    @Override
    public boolean create(Affectation affectation) {
        if (affectation == null) {
            return false;
        }

        String sql = "INSERT INTO affectation "
                + "(id_candidat, id_mission, date_proposition, statut) "
                + "VALUES (?, ?, ?, ?)";

        Connection conn = Singleton.getConnectionOuLeverException("AffectationDAO.create");

        try (PreparedStatement stmt = conn.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, affectation.getIdCandidat());
            stmt.setInt(2, affectation.getIdMission());

            if (affectation.getDateProposition() == null) {
                stmt.setNull(3, Types.DATE);
            } else {
                stmt.setDate(3, Date.valueOf(affectation.getDateProposition()));
            }

            stmt.setString(4, affectation.getStatut());

            if (stmt.executeUpdate() > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        affectation.setIdAffectation(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("AffectationDAO.create", e);
        }
    }

    @Override
    public Affectation findById(int id) {
        String sql = "SELECT * FROM affectation WHERE id_affectation = ?";

        Connection conn = Singleton.getConnectionOuLeverException("AffectationDAO.findById");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
            return null;

        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("AffectationDAO.findById", e);
        }
    }

    @Override
    public List<Affectation> findAll() {
        List<Affectation> affectations = new ArrayList<>();
        String sql = "SELECT * FROM affectation";

        Connection conn = Singleton.getConnectionOuLeverException("AffectationDAO.findAll");

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                affectations.add(mapResultSet(rs));
            }
            return affectations;

        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("AffectationDAO.findAll", e);
        }
    }

    @Override
    public boolean update(Affectation affectation) {
        if (affectation == null) {
            return false;
        }

        String sql = "UPDATE affectation SET id_candidat = ?, id_mission = ?, "
                + "date_proposition = ?, statut = ? WHERE id_affectation = ?";

        Connection conn = Singleton.getConnectionOuLeverException("AffectationDAO.update");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, affectation.getIdCandidat());
            stmt.setInt(2, affectation.getIdMission());

            if (affectation.getDateProposition() == null) {
                stmt.setNull(3, Types.DATE);
            } else {
                stmt.setDate(3, Date.valueOf(affectation.getDateProposition()));
            }

            stmt.setString(4, affectation.getStatut());
            stmt.setInt(5, affectation.getIdAffectation());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("AffectationDAO.update", e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM affectation WHERE id_affectation = ?";

        Connection conn = Singleton.getConnectionOuLeverException("AffectationDAO.delete");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("AffectationDAO.delete", e);
        }
    }

    @Override
    public boolean exists(int id) {
        String sql = "SELECT 1 FROM affectation WHERE id_affectation = ?";

        Connection conn = Singleton.getConnectionOuLeverException("AffectationDAO.exists");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("AffectationDAO.exists", e);
        }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM affectation";

        Connection conn = Singleton.getConnectionOuLeverException("AffectationDAO.count");

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0L;

        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("AffectationDAO.count", e);
        }
    }

    public boolean aUnConflitDeDates(int idCandidat, LocalDate debut, LocalDate fin) {
        if (debut == null && fin == null) {
            return false;
        }
        LocalDate dateDebut = (debut != null) ? debut : fin;
        LocalDate dateFin = (fin != null) ? fin : debut;
        if (dateDebut.isAfter(dateFin)) {
            LocalDate temp = dateDebut;
            dateDebut = dateFin;
            dateFin = temp;
        }

        String sql = "SELECT COUNT(*) FROM affectation a "
                + "JOIN demande_personnel d ON a.id_mission = d.id_mission "
                + "WHERE a.id_candidat = ? AND UPPER(TRIM(a.statut)) IN ('PROPOSEE', 'ACCEPTEE') "
                + "AND d.date_debut IS NOT NULL AND d.date_fin IS NOT NULL "
                + "AND d.date_debut <= ? AND d.date_fin >= ?";

        Connection conn = Singleton.getConnectionOuLeverException("AffectationDAO.aUnConflitDeDates");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCandidat);
            stmt.setDate(2, Date.valueOf(dateFin));
            stmt.setDate(3, Date.valueOf(dateDebut));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            return false;

        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("AffectationDAO.aUnConflitDeDates", e);
        }
    }

    private Affectation mapResultSet(ResultSet rs) throws SQLException {
        Affectation affectation = new Affectation();

        affectation.setIdAffectation(rs.getInt("id_affectation"));
        affectation.setIdCandidat(rs.getInt("id_candidat"));
        affectation.setIdMission(rs.getInt("id_mission"));

        Date date = rs.getDate("date_proposition");
        affectation.setDateProposition(
                date == null ? null : date.toLocalDate()
        );

        affectation.setStatut(rs.getString("statut"));

        return affectation;
    }
}