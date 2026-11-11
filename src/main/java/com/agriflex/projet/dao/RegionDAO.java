
package com.agriflex.projet.dao;

import com.agriflex.projet.model.Region;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegionDAO implements DAO<Region> {

    @Override
    public boolean create(Region region) {
        String sql = "INSERT INTO region (nom_region) VALUES (?)";

        Connection conn = Singleton.getConnection();

        if (conn == null) return false;

        try (PreparedStatement stmt = conn.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, region.getNomRegion());

            if (stmt.executeUpdate() > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        region.setIdRegion(rs.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erreur create Region : " + e.getMessage());
        }

        return false;
    }

    @Override
    public Region findById(int id) {
        String sql = "SELECT * FROM region WHERE id_region = ?";

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
            System.err.println("Erreur findById Region : " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<Region> findAll() {
        List<Region> regions = new ArrayList<>();

        String sql = "SELECT * FROM region";

        Connection conn = Singleton.getConnection();

        if (conn == null) return regions;

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                regions.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erreur findAll Region : " + e.getMessage());
        }

        return regions;
    }

    @Override
    public boolean update(Region region) {
        String sql = "UPDATE region SET nom_region = ? WHERE id_region = ?";

        Connection conn = Singleton.getConnection();

        if (conn == null) return false;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, region.getNomRegion());
            stmt.setInt(2, region.getIdRegion());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur update Region : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM region WHERE id_region = ?";

        Connection conn = Singleton.getConnection();

        if (conn == null) return false;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur delete Region : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean exists(int id) {
        String sql = "SELECT 1 FROM region WHERE id_region = ?";

        Connection conn = Singleton.getConnection();

        if (conn == null) return false;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.err.println("Erreur exists Region : " + e.getMessage());
        }

        return false;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM region";

        Connection conn = Singleton.getConnection();

        if (conn == null) return 0;

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }

        } catch (SQLException e) {
            System.err.println("Erreur count Region : " + e.getMessage());
        }

        return 0;
    }

    private Region mapResultSet(ResultSet rs) throws SQLException {
        Region region = new Region();

        region.setIdRegion(rs.getInt("id_region"));
        region.setNomRegion(rs.getString("nom_region"));

        return region;
    }
}

