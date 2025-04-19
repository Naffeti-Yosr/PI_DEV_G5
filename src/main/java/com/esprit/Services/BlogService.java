package com.esprit.services;

import com.esprit.models.Blog;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BlogService implements IService<Blog> {

    private Connection conn;

    public BlogService() {
        conn = DataSource.getInstance().getConnection();
    }

    @Override
    public void ajouter(Blog blog) {
        String sql = "INSERT INTO blog (titre, resume, contenu, image, datePublication, auteurId, views, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, blog.getTitre());
            pst.setString(2, blog.getResume());
            pst.setString(3, blog.getContenu());
            pst.setString(4, blog.getImage());
            pst.setTimestamp(5, Timestamp.valueOf(blog.getDatePublication()));
            pst.setInt(6, blog.getAuteurId());
            pst.setInt(7, blog.getViews());
            pst.setString(8, blog.getStatus());
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Blog blog) {
        String sql = "UPDATE blog SET titre=?, resume=?, contenu=?, image=?, datePublication=?, auteurId=?, views=?, status=? WHERE id=?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, blog.getTitre());
            pst.setString(2, blog.getResume());
            pst.setString(3, blog.getContenu());
            pst.setString(4, blog.getImage());
            pst.setTimestamp(5, Timestamp.valueOf(blog.getDatePublication()));
            pst.setInt(6, blog.getAuteurId());
            pst.setInt(7, blog.getViews());
            pst.setString(8, blog.getStatus());
            pst.setInt(9, blog.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(Blog blog) {
        String sql = "DELETE FROM blog WHERE id=?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, blog.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Blog> recuperer() {
        List<Blog> blogs = new ArrayList<>();
        String sql = "SELECT * FROM blog";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Blog blog = new Blog();
                blog.setId(rs.getInt("id"));
                blog.setTitre(rs.getString("titre"));
                blog.setResume(rs.getString("resume"));
                blog.setContenu(rs.getString("contenu"));
                blog.setImage(rs.getString("image"));
                Timestamp timestamp = rs.getTimestamp("datePublication");
                if (timestamp != null) {
                    blog.setDatePublication(timestamp.toLocalDateTime());
                } else {
                    blog.setDatePublication(null);
                }
                blog.setAuteurId(rs.getInt("auteurId"));
                blog.setViews(rs.getInt("views"));
                blog.setStatus(rs.getString("status"));
                blogs.add(blog);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return blogs;
    }
}