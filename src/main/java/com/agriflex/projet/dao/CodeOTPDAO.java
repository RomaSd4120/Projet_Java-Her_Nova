package com.agriflex.projet.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.agriflex.projet.model.CodeOTP;
import com.agriflex.projet.util.GestionnaireErreurBDD;
import com.agriflex.projet.util.Singleton;

public class CodeOTPDAO implements DAO<CodeOTP> {

    @Override
    public boolean create(CodeOTP otp) {
        if (otp == null) {
            return false;
        }
        String sql = "INSERT INTO code_otp (telephone, code, date_expiration, est_utilise) VALUES (?, ?, ?, ?)";
        Connection conn = Singleton.getConnectionOuLeverException("CodeOTPDAO.create");

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, otp.getTelephone());
            stmt.setString(2, otp.getCode());
            stmt.setTimestamp(3, otp.getDateExpiration() != null ? Timestamp.valueOf(otp.getDateExpiration()) : null);
            stmt.setBoolean(4, otp.isEstUtilise());

            if (stmt.executeUpdate() > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        otp.setIdOtp(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("CodeOTPDAO.create", e);
        }
    }

    @Override
    public CodeOTP findById(int id) {
        String sql = "SELECT * FROM code_otp WHERE id_otp = ?";
        Connection conn = Singleton.getConnectionOuLeverException("CodeOTPDAO.findById");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapResultSet(rs) : null;
            }
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("CodeOTPDAO.findById", e);
        }
    }

    @Override
    public List<CodeOTP> findAll() {
        List<CodeOTP> liste = new ArrayList<>();
        String sql = "SELECT * FROM code_otp ORDER BY date_expiration DESC";
        Connection conn = Singleton.getConnectionOuLeverException("CodeOTPDAO.findAll");

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                liste.add(mapResultSet(rs));
            }
            return liste;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("CodeOTPDAO.findAll", e);
        }
    }

    @Override
    public boolean update(CodeOTP otp) {
        if (otp == null) {
            return false;
        }
        String sql = "UPDATE code_otp SET telephone = ?, code = ?, date_expiration = ?, est_utilise = ? WHERE id_otp = ?";
        Connection conn = Singleton.getConnectionOuLeverException("CodeOTPDAO.update");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, otp.getTelephone());
            stmt.setString(2, otp.getCode());
            stmt.setTimestamp(3, otp.getDateExpiration() != null ? Timestamp.valueOf(otp.getDateExpiration()) : null);
            stmt.setBoolean(4, otp.isEstUtilise());
            stmt.setInt(5, otp.getIdOtp());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("CodeOTPDAO.update", e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM code_otp WHERE id_otp = ?";
        Connection conn = Singleton.getConnectionOuLeverException("CodeOTPDAO.delete");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("CodeOTPDAO.delete", e);
        }
    }

    @Override
    public boolean exists(int id) {
        String sql = "SELECT 1 FROM code_otp WHERE id_otp = ?";
        Connection conn = Singleton.getConnectionOuLeverException("CodeOTPDAO.exists");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("CodeOTPDAO.exists", e);
        }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM code_otp";
        Connection conn = Singleton.getConnectionOuLeverException("CodeOTPDAO.count");

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getLong(1) : 0L;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("CodeOTPDAO.count", e);
        }
    }

    public CodeOTP verifierCode(String telephone, String code) {
        String sql = "SELECT * FROM code_otp WHERE telephone = ? AND code = ? "
                + "AND est_utilise = FALSE AND date_expiration > CURRENT_TIMESTAMP "
                + "ORDER BY id_otp DESC LIMIT 1";
        Connection conn = Singleton.getConnectionOuLeverException("CodeOTPDAO.verifierCode");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, telephone);
            stmt.setString(2, code);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapResultSet(rs) : null;
            }
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("CodeOTPDAO.verifierCode", e);
        }
    }

    public boolean marquerCommeUtilise(int idOtp) {
        String sql = "UPDATE code_otp SET est_utilise = TRUE WHERE id_otp = ?";
        Connection conn = Singleton.getConnectionOuLeverException("CodeOTPDAO.marquerCommeUtilise");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idOtp);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("CodeOTPDAO.marquerCommeUtilise", e);
        }
    }

    private CodeOTP mapResultSet(ResultSet rs) throws SQLException {
        CodeOTP otp = new CodeOTP();
        otp.setIdOtp(rs.getInt("id_otp"));
        otp.setTelephone(rs.getString("telephone"));
        otp.setCode(rs.getString("code"));
        Timestamp exp = rs.getTimestamp("date_expiration");
        otp.setDateExpiration(exp != null ? exp.toLocalDateTime() : null);
        otp.setEstUtilise(rs.getBoolean("est_utilise"));
        return otp;
    }
}