package com.agriflex.projet.dao;

import com.agriflex.projet.model.Competence;
import com.agriflex.projet.util.Singleton;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompetenceDAO implements DAO<Competence> {

    @Override
    public boolean create(Competence competence) {
        String sql = "INSERT INTO competence (libelle, id_candidat) VALUES (?, ?)";
        Connection conn = Singleton.getConnection();
        if (conn == null) return false;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, competence.getNomCompetence());
            ps.setInt(2, competence.getIdTravailleur());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Competence findById(int id) {
        String sql = "SELECT * FROM competence WHERE id_competence = ?";
        Connection conn = Singleton.getConnection();
        if (conn == null) return null;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Competence(
                        rs.getInt("id_competence"),
                        rs.getString("libelle"),
                        "",
                        rs.getInt("id_candidat")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Competence> findAll() {
        List<Competence> liste = new ArrayList<>();
        String sql = "SELECT * FROM competence";
        Connection conn = Singleton.getConnection();
        if (conn == null) return liste;
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                liste.add(new Competence(
                    rs.getInt("id_competence"),
                    rs.getString("libelle"),
                    "",
                    rs.getInt("id_candidat")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    @Override
    public boolean update(Competence competence) {
        String sql = "UPDATE competence SET libelle = ?, id_candidat = ? WHERE id_competence = ?";
        Connection conn = Singleton.getConnection();
        if (conn == null) return false;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, competence.getNomCompetence());
            ps.setInt(2, competence.getIdTravailleur());
            ps.setInt(3, competence.getIdCompetence());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int idCompetence) {
        String sql = "DELETE FROM competence WHERE id_competence = ?";
        Connection conn = Singleton.getConnection();
        if (conn == null) return false;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCompetence);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public long count() {
        return findAll().size();
    }

    @Override
    public boolean exists(int id) {
        return findById(id) != null;
    }

    // Méthode spécifique pour récupérer les compétences d'un travailleur
    public List<Competence> getByTravailleur(int idTravailleur) {
        List<Competence> liste = new ArrayList<>();
        String sql = "SELECT * FROM competence WHERE id_candidat = ?";
        Connection conn = Singleton.getConnection();
        if (conn == null) return liste;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTravailleur);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    liste.add(new Competence(
                        rs.getInt("id_competence"),
                        rs.getString("libelle"),
                        "",
                        rs.getInt("id_candidat")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }
}