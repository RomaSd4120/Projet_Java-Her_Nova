package com.agriflex.projet.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.agriflex.projet.model.Disponibilite;
import com.agriflex.projet.util.GestionnaireErreurBDD;
import com.agriflex.projet.util.Singleton;

public class DisponibiliteDAO implements DAO<Disponibilite> {

    @Override
    public boolean create(Disponibilite disponibilite) {
        if (disponibilite == null) {
            return false;
        }
        String sql = "INSERT INTO disponibilite (id_travailleur, date_debut, date_fin, statut) VALUES (?, ?, ?, ?)";
        Connection conn = Singleton.getConnection();
        if (conn == null) {
            return false;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, disponibilite.getIdTravailleur());

            if (disponibilite.getDateDebut() == null) {
                stmt.setNull(2, Types.DATE);
            } else {
                stmt.setDate(2, Date.valueOf(disponibilite.getDateDebut()));
            }

            if (disponibilite.getDateFin() == null) {
                stmt.setNull(3, Types.DATE);
            } else {
                stmt.setDate(3, Date.valueOf(disponibilite.getDateFin()));
            }

            stmt.setString(4, disponibilite.getStatut());

            if (stmt.executeUpdate() > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        disponibilite.setIdDisponibilite(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("DisponibiliteDAO.create", e);
        }
    }

    @Override
    public Disponibilite findById(int id) {
        String sql = "SELECT * FROM disponibilite WHERE id_disponibilite = ?";
        Connection conn = Singleton.getConnection();
        if (conn == null) {
            return null;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapResultSet(rs) : null;
            }
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("DisponibiliteDAO.findById", e);
        }
    }

    @Override
    public List<Disponibilite> findAll() {
        List<Disponibilite> liste = new ArrayList<>();
        String sql = "SELECT * FROM disponibilite ORDER BY date_debut DESC";
        Connection conn = Singleton.getConnection();
        if (conn == null) {
            return liste;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                liste.add(mapResultSet(rs));
            }
            return liste;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("DisponibiliteDAO.findAll", e);
        }
    }

    @Override
    public boolean update(Disponibilite disponibilite) {
        if (disponibilite == null) {
            return false;
        }
        String sql = "UPDATE disponibilite SET id_travailleur = ?, date_debut = ?, date_fin = ?, statut = ? WHERE id_disponibilite = ?";
        Connection conn = Singleton.getConnection();
        if (conn == null) {
            return false;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, disponibilite.getIdTravailleur());

            if (disponibilite.getDateDebut() == null) {
                stmt.setNull(2, Types.DATE);
            } else {
                stmt.setDate(2, Date.valueOf(disponibilite.getDateDebut()));
            }

            if (disponibilite.getDateFin() == null) {
                stmt.setNull(3, Types.DATE);
            } else {
                stmt.setDate(3, Date.valueOf(disponibilite.getDateFin()));
            }

            stmt.setString(4, disponibilite.getStatut());
            stmt.setInt(5, disponibilite.getIdDisponibilite());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("DisponibiliteDAO.update", e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM disponibilite WHERE id_disponibilite = ?";
        Connection conn = Singleton.getConnection();
        if (conn == null) {
            return false;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("DisponibiliteDAO.delete", e);
        }
    }

    @Override
    public boolean exists(int id) {
        String sql = "SELECT 1 FROM disponibilite WHERE id_disponibilite = ?";
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
            throw GestionnaireErreurBDD.traduireException("DisponibiliteDAO.exists", e);
        }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM disponibilite";
        Connection conn = Singleton.getConnection();
        if (conn == null) {
            return 0L;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getLong(1) : 0L;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("DisponibiliteDAO.count", e);
        }
    }

    public List<Disponibilite> findByTravailleurId(int idTravailleur) {
        List<Disponibilite> liste = new ArrayList<>();
        String sql = "SELECT * FROM disponibilite WHERE id_travailleur = ? ORDER BY date_debut ASC";
        Connection conn = Singleton.getConnection();
        if (conn == null) {
            return liste;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idTravailleur);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    liste.add(mapResultSet(rs));
                }
            }
            return liste;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("DisponibiliteDAO.findByTravailleurId", e);
        }
    }

    // Récupérer uniquement les disponibilités actives ("Disponible")
    public List<Disponibilite> findAllDisponibles() {
        List<Disponibilite> liste = new ArrayList<>();
        String sql = "SELECT * FROM disponibilite WHERE statut = 'Disponible'";
        Connection conn = Singleton.getConnection();
        if (conn == null) {
            return liste;
        }

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                liste.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    // Compter les travailleurs selon leur statut ("Disponible" ou "Indisponible") pour les KPIs
    public int countByStatut(String statut) {
        String sql = "SELECT COUNT(*) FROM disponibilite WHERE statut = ?";
        Connection conn = Singleton.getConnection();
        if (conn == null) {
            return 0;
        }

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statut);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Disponibilite mapResultSet(ResultSet rs) throws SQLException {
        Disponibilite d = new Disponibilite();
        d.setIdDisponibilite(rs.getInt("id_disponibilite"));
        d.setIdTravailleur(rs.getInt("id_travailleur"));

        Date debut = rs.getDate("date_debut");
        d.setDateDebut(debut != null ? debut.toLocalDate() : null);

        Date fin = rs.getDate("date_fin");
        d.setDateFin(fin != null ? fin.toLocalDate() : null);

        d.setStatut(rs.getString("statut"));
        return d;
    }
}