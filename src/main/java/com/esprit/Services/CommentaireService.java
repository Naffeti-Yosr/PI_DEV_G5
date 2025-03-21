package com.esprit.Services;

import com.esprit.models.Commentaire;
import com.esprit.models.Blog;
import com.esprit.models.User;
import com.esprit.utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentaireService implements IService<Commentaire> {

    private Connection connection;

    public CommentaireService() {
        connection = DataSource.getInstance().getConnection();
    }

    @Override
    public void ajouter(Commentaire commentaire) {
        String query = "INSERT INTO commentaire (content, date, auteur_id, blog_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, commentaire.getContent());
            pst.setDate(2, new java.sql.Date(commentaire.getDate().getTime()));
            pst.setInt(3, commentaire.getAuteur().getId());
            pst.setInt(4, commentaire.getBlog().getId());
            pst.executeUpdate();
            System.out.println("Comment added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding comment: " + e.getMessage());
        }
    }

    @Override
    public void modifier(Commentaire commentaire) {
        String query = "UPDATE commentaire SET content = ?, date = ?, auteur_id = ?, blog_id = ? WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, commentaire.getContent());
            pst.setDate(2, new java.sql.Date(commentaire.getDate().getTime()));
            pst.setInt(3, commentaire.getAuteur().getId());
            pst.setInt(4, commentaire.getBlog().getId());
            pst.setInt(5, commentaire.getId());
            pst.executeUpdate();
            System.out.println("Comment updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating comment: " + e.getMessage());
        }
    }

    @Override
    public void supprimer(Commentaire commentaire) {
        String query = "DELETE FROM commentaire WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, commentaire.getId());
            pst.executeUpdate();
            System.out.println("Comment deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting comment: " + e.getMessage());
        }
    }

    @Override
    public List<Commentaire> recuperer() {
        List<Commentaire> commentaires = new ArrayList<>();
        String query = "SELECT * FROM commentaire";
        try (PreparedStatement pst = connection.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {

                User auteur = getUserById(rs.getInt("auteur_id"));

                Blog blog = getBlogById(rs.getInt("blog_id"));

                // Create the Commentaire object
                Commentaire commentaire = new Commentaire(
                        rs.getInt("id"),
                        rs.getString("content"),
                        rs.getDate("date"),
                        auteur,
                        blog
                );
                commentaires.add(commentaire);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving comments: " + e.getMessage());
        }
        return commentaires;
    }

    public List<Commentaire> getCommentsForBlog(int blogId) {
        List<Commentaire> commentsForBlog = new ArrayList<>();
        String query = "SELECT * FROM commentaire WHERE blog_id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, blogId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {

                User auteur = getUserById(rs.getInt("auteur_id"));

                Blog blog = getBlogById(rs.getInt("blog_id"));


                Commentaire commentaire = new Commentaire(
                        rs.getInt("id"),
                        rs.getString("content"),
                        rs.getDate("date"),
                        auteur,
                        blog
                );
                commentsForBlog.add(commentaire);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving comments for blog: " + e.getMessage());
        }
        return commentsForBlog;
    }


    private User getUserById(int userId) {
        String query = "SELECT * FROM user WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getDate("birth_date"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user by ID: " + e.getMessage());
        }
        return null;
    }


    private Blog getBlogById(int blogId) {
        String query = "SELECT * FROM blog WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, blogId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {

                User auteur = getUserById(rs.getInt("auteur_id"));


                return new Blog(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getString("imageUrl"),
                        rs.getDate("date"),
                        auteur
                );
            }
        } catch (SQLException e) {
            System.out.println("Error fetching blog by ID: " + e.getMessage());
        }
        return null;
    }
}
