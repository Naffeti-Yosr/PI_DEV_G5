package com.esprit.controllers;

import com.esprit.models.Blog;
import com.esprit.services.BlogService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

public class BlogFormController {
    public enum Mode { ADD, EDIT }
    
    @FXML private TextField titleField;
    @FXML private TextField summaryField;
    @FXML private TextArea contentArea;
    @FXML private ComboBox<String> statusCombo;
    @FXML private Label imageNameLabel;
    @FXML private ImageView imagePreview;
    
    private Mode mode = Mode.ADD;
    private Blog blog;
    private boolean saved = false;
    private File selectedImage;
    
    public void setMode(Mode mode) {
        this.mode = mode;
    }
    
    public void setBlog(Blog blog) {
        this.blog = blog;
        if (blog != null) {
            titleField.setText(blog.getTitre());
            summaryField.setText(blog.getResume());
            contentArea.setText(blog.getContenu());
            statusCombo.setValue(blog.getStatus());
            if (blog.getImage() != null && !blog.getImage().isEmpty()) {
                imageNameLabel.setText(blog.getImage());
                // Load and display the image from images folder
                File imageFile = new File(getImageStoragePath(), blog.getImage());
                if (imageFile.exists()) {
                    imagePreview.setImage(new Image(imageFile.toURI().toString()));
                }
            }
        }
    }
    
    public boolean isSaved() {
        return saved;
    }
    
    @FXML
    private void initialize() {
        statusCombo.getItems().addAll("draft", "published");
        statusCombo.setValue("draft");
    }
    
    @FXML
    private void handleChooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Blog Image");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        selectedImage = fileChooser.showOpenDialog(imagePreview.getScene().getWindow());
        
        if (selectedImage != null) {
            imageNameLabel.setText(selectedImage.getName());
            imagePreview.setImage(new Image(selectedImage.toURI().toString()));
        }
    }
    
    @FXML
    private void handleSave() {
        if (validateInput()) {
            BlogService blogService = new BlogService();

            if (mode == Mode.ADD) {
                blog = new Blog();
                blog.setViews(0); // Initialize views to 0 on add
                blog.setDatePublication(LocalDateTime.now());
            } else {
                // Keep original datePublication on edit
                if (blog.getDatePublication() == null) {
                    blog.setDatePublication(LocalDateTime.now());
                }
            }

            blog.setTitre(titleField.getText());
            blog.setResume(summaryField.getText());
            blog.setContenu(contentArea.getText());
            blog.setStatus(statusCombo.getValue());
            blog.setAuteurId(getCurrentUserId()); // Use method to get current user ID

            if (selectedImage != null) {
                // Save image file to images folder with title name
                try {
                    File destDir = new File(getImageStoragePath());
                    if (!destDir.exists()) {
                        destDir.mkdirs();
                    }
                    // Get file extension
                    String extension = "";
                    String originalName = selectedImage.getName();
                    int i = originalName.lastIndexOf('.');
                    if (i > 0) {
                        extension = originalName.substring(i);
                    }
                    // Create new file name based on title (sanitize to remove invalid chars)
                    String sanitizedTitle = titleField.getText().trim().replaceAll("[^a-zA-Z0-9\\-_]", "_");
                    String newFileName = sanitizedTitle + extension;
                    File destFile = new File(destDir, newFileName);
                    Files.copy(selectedImage.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    blog.setImage(newFileName);
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert("Image Save Error", "Failed to save the selected image.");
                    return;
                }
            }

            if (mode == Mode.ADD) {
                blogService.ajouter(blog);
            } else {
                blogService.modifier(blog);
            }

            saved = true;
            closeWindow();
        }
    }
    
    @FXML
    private void handleCancel() {
        saved = false;
        closeWindow();
    }
    
    private boolean validateInput() {
        if (titleField.getText().isEmpty()) {
            showAlert("Validation Error", "Title is required");
            return false;
        }
        if (summaryField.getText().isEmpty()) {
            showAlert("Validation Error", "Summary is required");
            return false;
        }
        if (contentArea.getText().isEmpty()) {
            showAlert("Validation Error", "Content is required");
            return false;
        }
        return true;
    }
    
    private void closeWindow() {
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String getImageStoragePath() {
        // Define the folder to store images relative to user directory or project directory
        return String.join(File.separator,
            System.getProperty("user.dir"),
            "src", "main", "resources", "images", "blogs");
    }

    private int getCurrentUserId() {
        // TODO: Replace with actual user session or authentication logic
        return 1;
    }
}
