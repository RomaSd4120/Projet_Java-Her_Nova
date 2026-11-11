
package com.agriflex.projet.dao;

import com.agriflex.projet.model.Affectation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class AffectationDAO implements DAO<Affectation> {

    @Override
    public boolean create(Affectation affectation) {
        String sql = "INSERT INTO affectation "
                + "(id_candidat, id_mission, date_proposition, statut) "
                + "VALUES (?, ?, ?, ?)";

        Connection conn = Singleton.getConnection();
        if (conn == null) return false;

        try (PreparedStatement stmt = conn.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, affectation.getIdCandidat());
            stmt.setInt(2, affectation.getIdMission());

            if (affectation.getDateProposition() == null) {
                stmt.setNull(3, java.sql.Types.DATE);
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

        } catch (SQLException e) {
            System.err.println("Erreur create Affectation : " + e.getMessage());
        }

        return false;
    }

    @Override
    public Affectation findById(int id) {
        String sql = "SELECT * FROM affectation WHERE id_affectation = ?";

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
            System.err.println("Erreur findById Affectation : " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<Affectation> findAll() {
        List<Affectation> affectations = new ArrayList<>();
        String sql = "SELECT * FROM affectation";

        Connection conn = Singleton.getConnection();
        if (conn == null) return affectations;

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                affectations.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erreur findAll Affectation : " + e.getMessage());
        }

        return affectations;
    }

    @Override
    public boolean update(Affectation affectation) {
        String sql = "UPDATE affectation SET id_candidat = ?, id_mission = ?, "
                + "date_proposition = ?, statut = ? WHERE id_affectation = ?";

        Connection conn = Singleton.getConnection();
        if (conn == null) return false;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, affectation.getIdCandidat());
            stmt.setInt(2, affectation.getIdMission());

            if (affectation.getDateProposition() == null) {
                stmt.setNull(3, java.sql.Types.DATE);
            } else {
                stmt.setDate(3, Date.valueOf(affectation.getDateProposition()));
            }

            stmt.setString(4, affectation.getStatut());
            stmt.setInt(5, affectation.getIdAffectation());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur update Affectation : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM affectation WHERE id_affectation = ?";

        Connection conn = Singleton.getConnection();
        if (conn == null) return false;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur delete Affectation : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean exists(int id) {
        String sql = "SELECT 1 FROM affectation WHERE id_affectation = ?";

        Connection conn = Singleton.getConnection();
        if (conn == null) return false;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.err.println("Erreur exists Affectation : " + e.getMessage());
        }

        return false;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM affectation";

        Connection conn = Singleton.getConnection();
        if (conn == null) return 0;

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }

        } catch (SQLException e) {
            System.err.println("Erreur count Affectation : " + e.getMessage());
        }

        return 0;
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
