package com.esprit.controllers;

import com.esprit.models.Commentaire;
import com.esprit.services.CommentaireService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDateTime;

public class CommentDashboardController {
    @FXML private Label blogTitleLabel;
    @FXML private TableView<Commentaire> commentTable;
    @FXML private TextArea commentField;
    
    private int blogId;
    private final CommentaireService commentaireService = new CommentaireService();
    
    public void setBlogId(int blogId) {
        this.blogId = blogId;
        blogTitleLabel.setText("Comments for Blog #" + blogId);
        loadComments();
    }
    
    @FXML
    private TableColumn<Commentaire, Integer> authorColumn;
    @FXML
    private TableColumn<Commentaire, String> commentColumn;
    @FXML
    private TableColumn<Commentaire, String> dateColumn;

    @FXML
    private void initialize() {
        commentTable.getColumns().forEach(column -> {
            column.setReorderable(false);
            column.setSortable(false);
        });

        authorColumn.setCellValueFactory(cellData -> 
            new SimpleIntegerProperty(cellData.getValue().getAuteurId()).asObject());
        commentColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getContenu()));
        dateColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getDateCommentaire().toString()));
    }
    
    private void loadComments() {
        ObservableList<Commentaire> comments = FXCollections.observableArrayList(
            commentaireService.recupererByBlogId(blogId)
        );
        commentTable.setItems(comments);
    }
    
    @FXML
    private void handleAddComment() {
        String content = commentField.getText().trim();
        if (!content.isEmpty()) {
            Commentaire comment = new Commentaire();
            comment.setContenu(content);
            comment.setDateCommentaire(LocalDateTime.now());
            comment.setAuteurId(1); // Replace with actual user ID
            comment.setBlogId(blogId);
            
            commentaireService.ajouter(comment);
            commentField.clear();
            loadComments();
        }
    }
    
    @FXML
    private void handleDelete() {
        Commentaire selected = commentTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Delete");
            confirm.setHeaderText("Delete comment");
            confirm.setContentText("Are you sure?");
            
            if (confirm.showAndWait().get() == ButtonType.OK) {
                commentaireService.supprimer(selected);
                loadComments();
            }
        }
    }
    
    @FXML
    private void handleRefresh() {
        loadComments();
    }
}