package com.agriflex.projet.dao;

import com.agriflex.projet.model.CodeOTP;
import com.agriflex.projet.util.Singleton;

import java.sql.*;
import java.time.LocalDateTime;

public class CodeOTPDAO {

    public boolean enregistrer(CodeOTP otp) {
        String sql = "INSERT INTO code_otp (telephone, code, date_expiration, est_utilise) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, otp.getTelephone());
            ps.setString(2, otp.getCode());
            ps.setTimestamp(3, Timestamp.valueOf(otp.getDateExpiration()));
            ps.setBoolean(4, otp.isEstUtilise());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public CodeOTP verifierCode(String telephone, String code) {
        String sql = "SELECT * FROM code_otp WHERE telephone = ? AND code = ? AND est_utilise = FALSE ORDER BY id_otp DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, telephone);
            ps.setString(2, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                LocalDateTime expiration = rs.getTimestamp("date_expiration").toLocalDateTime();
                if (expiration.isAfter(LocalDateTime.now())) {
                    return new CodeOTP(
                        rs.getInt("id_otp"),
                        rs.getString("telephone"),
                        rs.getString("code"),
                        expiration,
                        rs.getBoolean("est_utilise")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean marquerCommeUtilise(int idOtp) {
        String sql = "UPDATE code_otp SET est_utilise = TRUE WHERE id_otp = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idOtp);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
