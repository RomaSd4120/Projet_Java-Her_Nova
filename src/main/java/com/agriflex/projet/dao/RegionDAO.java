package com.agriflex.projet.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.agriflex.projet.model.Region;
import com.agriflex.projet.model.StatistiqueRegion;
import com.agriflex.projet.util.GestionnaireErreurBDD;
import com.agriflex.projet.util.Singleton;

public class RegionDAO implements DAO<Region> {

    @Override
    public boolean create(Region entity) {
        if (entity == null) {
            return false;
        }
        String sql = "INSERT INTO region (nom_region) VALUES (?)";
        Connection conn = Singleton.getConnectionOuLeverException("RegionDAO.create");

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, entity.getNomRegion());
            if (stmt.executeUpdate() > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        entity.setIdRegion(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("RegionDAO.create", e);
        }
    }

    @Override
    public Region findById(int id) {
        String sql = "SELECT * FROM region WHERE id_region = ?";
        Connection conn = Singleton.getConnectionOuLeverException("RegionDAO.findById");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapResultSet(rs) : null;
            }
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("RegionDAO.findById", e);
        }
    }

    @Override
    public List<Region> findAll() {
        List<Region> liste = new ArrayList<>();
        String sql = "SELECT * FROM region ORDER BY nom_region";
        Connection conn = Singleton.getConnectionOuLeverException("RegionDAO.findAll");

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                liste.add(mapResultSet(rs));
            }
            return liste;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("RegionDAO.findAll", e);
        }
    }

    @Override
    public boolean update(Region entity) {
        if (entity == null) {
            return false;
        }
        String sql = "UPDATE region SET nom_region = ? WHERE id_region = ?";
        Connection conn = Singleton.getConnectionOuLeverException("RegionDAO.update");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entity.getNomRegion());
            stmt.setInt(2, entity.getIdRegion());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("RegionDAO.update", e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM region WHERE id_region = ?";
        Connection conn = Singleton.getConnectionOuLeverException("RegionDAO.delete");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("RegionDAO.delete", e);
        }
    }

    @Override
    public boolean exists(int id) {
        String sql = "SELECT 1 FROM region WHERE id_region = ?";
        Connection conn = Singleton.getConnectionOuLeverException("RegionDAO.exists");

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("RegionDAO.exists", e);
        }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM region";
        Connection conn = Singleton.getConnectionOuLeverException("RegionDAO.count");

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getLong(1) : 0L;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("RegionDAO.count", e);
        }
    }

    public List<StatistiqueRegion> getStatistiquesParRegion() {
        List<StatistiqueRegion> stats = new ArrayList<>();
        String sql = "SELECT r.id_region, r.nom_region, "
                + "COUNT(DISTINCT t.id_candidat) AS total_travailleurs, "
                + "COUNT(DISTINCT CASE WHEN UPPER(TRIM(t.disponibilite)) = 'DISPONIBLE' THEN t.id_candidat END) AS disponibles, "
                + "COUNT(DISTINCT CASE WHEN UPPER(TRIM(d.statut)) = 'OUVERTE' THEN d.id_mission END) AS missions_ouvertes "
                + "FROM region r "
                + "LEFT JOIN travailleur t ON t.id_region = r.id_region "
                + "LEFT JOIN demande_personnel d ON LOWER(TRIM(d.lieu)) LIKE CONCAT('%', LOWER(TRIM(r.nom_region)), '%') "
                + "GROUP BY r.id_region, r.nom_region "
                + "ORDER BY r.nom_region";

        Connection conn = Singleton.getConnectionOuLeverException("RegionDAO.getStatistiquesParRegion");

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                stats.add(new StatistiqueRegion(
                        rs.getInt("id_region"),
                        rs.getString("nom_region"),
                        rs.getInt("total_travailleurs"),
                        rs.getInt("disponibles"),
                        rs.getInt("missions_ouvertes")
                ));
            }
            return stats;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("RegionDAO.getStatistiquesParRegion", e);
        }
    }

    private Region mapResultSet(ResultSet rs) throws SQLException {
        Region r = new Region();
        r.setIdRegion(rs.getInt("id_region"));
        r.setNomRegion(rs.getString("nom_region"));
        return r;
    }
}