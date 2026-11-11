
package com.agriflex.projet.dao;

import com.agriflex.projet.model.Notification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO implements DAO<Notification> {

    @Override
    public boolean create(Notification notification) {
        String sql = "INSERT INTO notification "
                + "(id_candidat, id_mission, canal, message, statut_envoi) "
                + "VALUES (?, ?, ?, ?, ?)";

        Connection conn = Singleton.getConnection();
        if (conn == null) return false;

        try (PreparedStatement stmt = conn.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, notification.getIdCandidat());

            if (notification.getIdMission() == null) {
                stmt.setNull(2, Types.INTEGER);
            } else {
                stmt.setInt(2, notification.getIdMission());
            }

            stmt.setString(3, notification.getCanal());
            stmt.setString(4, notification.getMessage());
            stmt.setString(5, notification.getStatutEnvoi());

            if (stmt.executeUpdate() > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        notification.setIdNotification(rs.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erreur create Notification : " + e.getMessage());
        }

        return false;
    }

    @Override
    public Notification findById(int id) {
        String sql = "SELECT * FROM notification WHERE id_notification = ?";

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
            System.err.println("Erreur findById Notification : " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<Notification> findAll() {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM notification";

        Connection conn = Singleton.getConnection();
        if (conn == null) return notifications;

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                notifications.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erreur findAll Notification : " + e.getMessage());
        }

        return notifications;
    }

    @Override
    public boolean update(Notification notification) {
        String sql = "UPDATE notification SET id_candidat = ?, id_mission = ?, "
                + "canal = ?, message = ?, statut_envoi = ? "
                + "WHERE id_notification = ?";

        Connection conn = Singleton.getConnection();
        if (conn == null) return false;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, notification.getIdCandidat());

            if (notification.getIdMission() == null) {
                stmt.setNull(2, Types.INTEGER);
            } else {
                stmt.setInt(2, notification.getIdMission());
            }

            stmt.setString(3, notification.getCanal());
            stmt.setString(4, notification.getMessage());
            stmt.setString(5, notification.getStatutEnvoi());
            stmt.setInt(6, notification.getIdNotification());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur update Notification : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM notification WHERE id_notification = ?";

        Connection conn = Singleton.getConnection();
        if (conn == null) return false;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur delete Notification : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean exists(int id) {
        String sql = "SELECT 1 FROM notification WHERE id_notification = ?";

        Connection conn = Singleton.getConnection();
        if (conn == null) return false;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.err.println("Erreur exists Notification : " + e.getMessage());
        }

        return false;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM notification";

        Connection conn = Singleton.getConnection();
        if (conn == null) return 0;

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }

        } catch (SQLException e) {
            System.err.println("Erreur count Notification : " + e.getMessage());
        }

        return 0;
    }

    private Notification mapResultSet(ResultSet rs) throws SQLException {
        Notification notification = new Notification();

        notification.setIdNotification(rs.getInt("id_notification"));
        notification.setIdCandidat(rs.getInt("id_candidat"));

        int idMission = rs.getInt("id_mission");
        if (rs.wasNull()) {
            notification.setIdMission(null);
        } else {
            notification.setIdMission(idMission);
        }

        notification.setCanal(rs.getString("canal"));
        notification.setMessage(rs.getString("message"));

        Timestamp timestamp = rs.getTimestamp("date_envoi");
        notification.setDateEnvoi(
                timestamp == null ? null : timestamp.toLocalDateTime()
        );

        notification.setStatutEnvoi(rs.getString("statut_envoi"));

        return notification;
    }
}

