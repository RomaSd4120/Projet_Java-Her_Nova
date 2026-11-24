package com.agriflex.projet.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.agriflex.projet.model.CritereRechercheTravailleur;
import com.agriflex.projet.model.Travailleur;
import com.agriflex.projet.util.GestionnaireErreurBDD;
import com.agriflex.projet.util.Singleton;

public class TravailleurDAO implements DAO<Travailleur> {

    @Override
    public Travailleur findById(int id) {
        String sql = "SELECT * FROM travailleur WHERE id_candidat = ?";

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
            System.err.println("Erreur findById Travailleur : " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<Travailleur> findAll() {
        List<Travailleur> travailleurs = new ArrayList<>();

        String sql = "SELECT * FROM travailleur";

        Connection conn = Singleton.getConnection();
        if (conn == null) return travailleurs;

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                travailleurs.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erreur findAll Travailleur : " + e.getMessage());
        }

        return travailleurs;
    }

    @Override
    public boolean update(Travailleur travailleur) {
        if (travailleur == null) {
            return false;
        }

        String sql = "UPDATE travailleur SET id_utilisateur = ?, id_region = ?, "
                + "competences = ?, experience = ?, disponibilite = ?, mobilite = ? "
                + "WHERE id_candidat = ?";

        Connection conn = Singleton.getConnection();
        if (conn == null) return false;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, travailleur.getIdUtilisateur());

            if (travailleur.getIdRegion() == null) {
                stmt.setNull(2, Types.INTEGER);
            } else {
                stmt.setInt(2, travailleur.getIdRegion());
            }

            stmt.setString(3, travailleur.getCompetences());
            stmt.setString(4, travailleur.getExperience());
            stmt.setString(5, travailleur.getDisponibilite());
            stmt.setBoolean(6, travailleur.isMobilite());
            stmt.setInt(7, travailleur.getIdCandidat());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur update Travailleur : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM travailleur WHERE id_candidat = ?";

        Connection conn = Singleton.getConnection();
        if (conn == null) return false;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur delete Travailleur : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean exists(int id) {
        String sql = "SELECT 1 FROM travailleur WHERE id_candidat = ?";

        Connection conn = Singleton.getConnection();
        if (conn == null) return false;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.err.println("Erreur exists Travailleur : " + e.getMessage());
        }

        return false;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM travailleur";

        Connection conn = Singleton.getConnection();
        if (conn == null) return 0;

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }

        } catch (SQLException e) {
            System.err.println("Erreur count Travailleur : " + e.getMessage());
        }

        return 0;
    }

    public List<Travailleur> rechercherParCriteres(CritereRechercheTravailleur critere) {
        if (critere == null) {
            return findAll();
        }

        List<Travailleur> resultats = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT DISTINCT t.* FROM travailleur t "
                + "LEFT JOIN competence c ON c.id_travailleur = t.id_candidat "
                + "WHERE 1=1 "
        );
        List<Object> params = new ArrayList<>();

        if (critere.getIdRegion() != null) {
            if (Boolean.TRUE.equals(critere.getMobilite())) {
                sql.append("AND (t.id_region = ? OR t.mobilite = TRUE) ");
            } else {
                sql.append("AND t.id_region = ? ");
            }
            params.add(critere.getIdRegion());
        } else if (Boolean.TRUE.equals(critere.getMobilite())) {
            sql.append("AND t.mobilite = TRUE ");
        }

        if (critere.getDisponibilite() != null && !critere.getDisponibilite().isBlank()) {
            sql.append("AND UPPER(TRIM(t.disponibilite)) = UPPER(?) ");
            params.add(critere.getDisponibilite().trim());
        }

        if (critere.getCompetence() != null && !critere.getCompetence().isBlank()) {
            sql.append("AND (LOWER(t.competences) LIKE ? OR LOWER(c.nom_competence) LIKE ?) ");
            String motif = "%" + critere.getCompetence().toLowerCase().trim() + "%";
            params.add(motif);
            params.add(motif);
        }

        Connection conn = Singleton.getConnection();
        if (conn == null) return resultats;

        try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    resultats.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur recherche travailleurs : " + e.getMessage());
        }

        return resultats;
    }

    private Travailleur mapResultSet(ResultSet rs) throws SQLException {
        Travailleur travailleur = new Travailleur();

        travailleur.setIdCandidat(rs.getInt("id_candidat"));
        travailleur.setIdUtilisateur(rs.getInt("id_utilisateur"));

        int idRegion = rs.getInt("id_region");
        if (rs.wasNull()) {
            travailleur.setIdRegion(null);
        } else {
            travailleur.setIdRegion(idRegion);
        }

        travailleur.setCompetences(rs.getString("competences"));
        travailleur.setExperience(rs.getString("experience"));
        travailleur.setDisponibilite(rs.getString("disponibilite"));
        travailleur.setMobilite(rs.getBoolean("mobilite"));

        return travailleur;
    }
@Override
    public boolean create(Travailleur travailleur) {
        if (travailleur == null) {
            return false;
        }

        String sql = "INSERT INTO travailleur "
                + "(id_utilisateur, id_region, competences, experience, disponibilite, mobilite) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = Singleton.getConnection();
        if (conn == null) {
            return false;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, travailleur.getIdUtilisateur());
            if (travailleur.getIdRegion() == null) {
                stmt.setNull(2, Types.INTEGER);
            } else {
                stmt.setInt(2, travailleur.getIdRegion());
            }
            stmt.setString(3, travailleur.getCompetences());
            stmt.setString(4, travailleur.getExperience());
            stmt.setString(5, travailleur.getDisponibilite());
            stmt.setBoolean(6, travailleur.isMobilite());

            if (stmt.executeUpdate() > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        travailleur.setIdCandidat(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw GestionnaireErreurBDD.traduireException("TravailleurDAO.create", e);
        }
    }

    public Travailleur findByUtilisateurId(int idUtilisateur) {
        String sql = "SELECT * FROM travailleur WHERE id_utilisateur = ?";
        Connection conn = Singleton.getConnection();
        if (conn == null) return null;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUtilisateur);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur findByUtilisateurId Travailleur : " + e.getMessage());
        }
        return null;
    }
 }
  