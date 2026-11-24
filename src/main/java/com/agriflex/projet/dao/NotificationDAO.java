
package com.agriflex.projet.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.agriflex.projet.model.Notification;
import com.agriflex.projet.util.GestionnaireErreurBDD;
import com.agriflex.projet.util.Singleton;

public class NotificationDAO implements DAO<Notification> {

    @Override
    public boolean create(Notification notification) {
        if (notification == null) {
            return false;
        }

        String sql = "INSERT INTO notification "
                + "(id_candidat, id_mission, canal, message, date_envoi, statut_envoi) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = Singleton.getConnectionOuLeverException("NotificationDAO.create");

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, notification.getIdCandidat());

            if (notification.getIdMission() == null) {
                stmt.setNull(2, Types.INTEGER);
            } else {
                stmt.setInt(2, notification.getIdMission());
            }

            stmt.setString(3, notification.getCanal());
            stmt.setString(4, notification.getMessage());

            if (notification.getDateEnvoi() == null) {
                stmt.setNull(5, Types.TIMESTAMP);
            } else {
                stmt.setTimestamp(5, Timestamp.valueOf(notification.getDateEnvoi()));
            }

            stmt.setString(6, notification.getStatutEnvoi());

            if (stmt.executeUpdate() > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        notification.setIdNotification(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("NotificationDAO.create", e);
        }
    }

    @Override
    public Notification findById(int id) {
        String sql = "SELECT * FROM notification WHERE id_notification = ?";
        Connection conn = Singleton.getConnectionOuLeverException("NotificationDAO.findById");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapResultSet(rs) : null;
            }
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("NotificationDAO.findById", e);
        }
    }

    @Override
    public List<Notification> findAll() {
        List<Notification> liste = new ArrayList<>();
        String sql = "SELECT * FROM notification ORDER BY date_envoi DESC, id_notification DESC";
        Connection conn = Singleton.getConnectionOuLeverException("NotificationDAO.findAll");

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                liste.add(mapResultSet(rs));
            }
            return liste;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("NotificationDAO.findAll", e);
        }
    }

    @Override
    public boolean update(Notification notification) {
        if (notification == null) {
            return false;
        }

        String sql = "UPDATE notification SET id_candidat = ?, id_mission = ?, canal = ?, "
                + "message = ?, date_envoi = ?, statut_envoi = ? WHERE id_notification = ?";

        Connection conn = Singleton.getConnectionOuLeverException("NotificationDAO.update");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, notification.getIdCandidat());

            if (notification.getIdMission() == null) {
                stmt.setNull(2, Types.INTEGER);
            } else {
                stmt.setInt(2, notification.getIdMission());
            }

            stmt.setString(3, notification.getCanal());
            stmt.setString(4, notification.getMessage());

            if (notification.getDateEnvoi() == null) {
                stmt.setNull(5, Types.TIMESTAMP);
            } else {
                stmt.setTimestamp(5, Timestamp.valueOf(notification.getDateEnvoi()));
            }

            stmt.setString(6, notification.getStatutEnvoi());
            stmt.setInt(7, notification.getIdNotification());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("NotificationDAO.update", e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM notification WHERE id_notification = ?";
        Connection conn = Singleton.getConnectionOuLeverException("NotificationDAO.delete");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("NotificationDAO.delete", e);
        }
    }

    @Override
    public boolean exists(int id) {
        String sql = "SELECT 1 FROM notification WHERE id_notification = ?";
        Connection conn = Singleton.getConnectionOuLeverException("NotificationDAO.exists");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("NotificationDAO.exists", e);
        }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM notification";
        Connection conn = Singleton.getConnectionOuLeverException("NotificationDAO.count");

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getLong(1) : 0L;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("NotificationDAO.count", e);
        }
    }

    private Notification mapResultSet(ResultSet rs) throws SQLException {
        Notification notification = new Notification();
        notification.setIdNotification(rs.getInt("id_notification"));
        notification.setIdCandidat(rs.getInt("id_candidat"));

        int idMission = rs.getInt("id_mission");
        notification.setIdMission(rs.wasNull() ? null : idMission);

        notification.setCanal(rs.getString("canal"));
        notification.setMessage(rs.getString("message"));

        Timestamp dateEnvoi = rs.getTimestamp("date_envoi");
        notification.setDateEnvoi(dateEnvoi != null ? dateEnvoi.toLocalDateTime() : null);

        notification.setStatutEnvoi(rs.getString("statut_envoi"));
        return notification;
    }
}