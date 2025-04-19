package com.esprit.services;

import com.esprit.models.Commentaire;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentaireService implements IService<Commentaire> {

    private Connection conn;

    public CommentaireService() {
        conn = DataSource.getInstance().getConnection();
    }

    @Override
    public void ajouter(Commentaire commentaire) {
        String sql = "INSERT INTO commentaire (contenu, date, auteurId, blogId) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, commentaire.getContenu());
            pst.setTimestamp(2, Timestamp.valueOf(commentaire.getDateCommentaire()));
            pst.setInt(3, commentaire.getAuteurId());
            pst.setInt(4, commentaire.getBlogId());
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Commentaire commentaire) {
        String sql = "UPDATE commentaire SET contenu=?, date=?, auteurId=?, blogId=? WHERE id=?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, commentaire.getContenu());
            pst.setTimestamp(2, Timestamp.valueOf(commentaire.getDateCommentaire()));
            pst.setInt(3, commentaire.getAuteurId());
            pst.setInt(4, commentaire.getBlogId());
            pst.setInt(5, commentaire.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(Commentaire commentaire) {
        String sql = "DELETE FROM commentaire WHERE id=?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, commentaire.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Commentaire> recuperer() {
        List<Commentaire> commentaires = new ArrayList<>();
        String sql = "SELECT * FROM commentaire";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Commentaire commentaire = new Commentaire();
                commentaire.setId(rs.getInt("id"));
                commentaire.setContenu(rs.getString("contenu"));
                commentaire.setDateCommentaire(rs.getTimestamp("date").toLocalDateTime());
                commentaire.setAuteurId(rs.getInt("auteurId"));
                commentaire.setBlogId(rs.getInt("blogId"));
                commentaires.add(commentaire);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commentaires;
    }

    public List<Commentaire> recupererByBlogId(int blogId) {
        List<Commentaire> commentaires = new ArrayList<>();
        String sql = "SELECT * FROM commentaire WHERE blogId = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, blogId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Commentaire commentaire = new Commentaire();
                    commentaire.setId(rs.getInt("id"));
                    commentaire.setContenu(rs.getString("contenu"));
                    commentaire.setDateCommentaire(rs.getTimestamp("date").toLocalDateTime());
                    commentaire.setAuteurId(rs.getInt("auteurId"));
                    commentaire.setBlogId(rs.getInt("blogId"));
                    commentaires.add(commentaire);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commentaires;
    }
}
