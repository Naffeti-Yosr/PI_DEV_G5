package com.esprit.Services;

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

public class BlogService implements IService<Blog> {

    private Connection connection;

    public BlogService() {
        connection = DataSource.getInstance().getConnection();
    }

    @Override
    public void ajouter(Blog blog) {
        String query = "INSERT INTO blog (titre, description, imageUrl, date, auteur_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, blog.getTitre());
            pst.setString(2, blog.getDescription());
            pst.setString(3, blog.getImageUrl());
            pst.setDate(4, new java.sql.Date(blog.getDate().getTime()));
            pst.setInt(5, blog.getAuteur().getId());
            pst.executeUpdate();
            System.out.println("Blog added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding blog: " + e.getMessage());
        }
    }

    @Override
    public void modifier(Blog blog) {
        String query = "UPDATE blog SET titre = ?, description = ?, imageUrl = ?, date = ?, auteur_id = ? WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, blog.getTitre());
            pst.setString(2, blog.getDescription());
            pst.setString(3, blog.getImageUrl());
            pst.setDate(4, new java.sql.Date(blog.getDate().getTime()));
            pst.setInt(5, blog.getAuteur().getId());
            pst.setInt(6, blog.getId());
            pst.executeUpdate();
            System.out.println("Blog updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating blog: " + e.getMessage());
        }
    }

    @Override
    public void supprimer(Blog blog) {
        String query = "DELETE FROM blog WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, blog.getId());
            pst.executeUpdate();
            System.out.println("Blog deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting blog: " + e.getMessage());
        }
    }

    @Override
    public List<Blog> recuperer() {
        List<Blog> blogs = new ArrayList<>();
        String query = "SELECT * FROM blog";
        try (PreparedStatement pst = connection.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                User auteur = getUserById(rs.getInt("auteur_id"));


                Blog blog = new Blog(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getString("imageUrl"),
                        rs.getDate("date"),
                        auteur
                );
                blogs.add(blog);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving blogs: " + e.getMessage());
        }
        return blogs;
    }

    public Blog getBlogById(int id) {
        String query = "SELECT * FROM blog WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
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
            System.out.println("Error retrieving blog by ID: " + e.getMessage());
        }
        return null;
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
}