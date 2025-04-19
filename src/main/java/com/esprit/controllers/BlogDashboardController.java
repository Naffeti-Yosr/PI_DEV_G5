package com.esprit.controllers;

import com.esprit.models.Blog;
import com.esprit.services.BlogService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BlogDashboardController {
    @FXML private TableView<Blog> blogTable;
    // Removed idColumn declaration as it is no longer used since ID column is removed from FXML
    // @FXML private TableColumn<Blog, Integer> idColumn;
    @FXML private TableColumn<Blog, Integer> viewColumn;
    @FXML private TableColumn<Blog, String> titleColumn;
    @FXML private TableColumn<Blog, String> summaryColumn;
    @FXML private TableColumn<Blog, String> statusColumn;
    @FXML private TableColumn<Blog, String> dateColumn;
    @FXML private TableColumn<Blog, Integer> authorColumn;
    @FXML private TableColumn<Blog, String> imageColumn;
    @FXML private ComboBox<String> statusCombo;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button publishButton;

    private final BlogService blogService = new BlogService();
    private final ObservableList<Blog> blogs = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadBlogs();
        setupSelectionListener();
        setupStatusFilter();
    }

    private void setupTableColumns() {
        // Convert POJO fields to JavaFX properties for TableView
        viewColumn.setCellValueFactory(cellData -> 
            new SimpleIntegerProperty(cellData.getValue().getViews()).asObject());
        
        titleColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getTitre()));
        
        summaryColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getResume()));
        
        statusColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getStatus()));
        
        dateColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getDatePublication() != null) {
                return new SimpleStringProperty(cellData.getValue().getDatePublication().toString());
            } else {
                return new SimpleStringProperty("");
            }
        });
        
        authorColumn.setCellValueFactory(cellData -> 
            new SimpleIntegerProperty(cellData.getValue().getAuteurId()).asObject());
        
        // Display image in imageColumn
        imageColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getImage()));
        imageColumn.setCellFactory(column -> new javafx.scene.control.TableCell<>() {
            private final javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView();
            {
                imageView.setFitWidth(100);
                imageView.setFitHeight(75);
                imageView.setPreserveRatio(true);
            }
            @Override
            protected void updateItem(String imageName, boolean empty) {
                super.updateItem(imageName, empty);
                if (empty || imageName == null || imageName.isEmpty()) {
                    setGraphic(null);
                } else {
                    java.io.File imageFile = new java.io.File(
                        String.join(java.io.File.separator,
                            System.getProperty("user.dir"),
                            "src", "main", "resources", "images", "blogs"),
                        imageName);
                    if (imageFile.exists()) {
                        imageView.setImage(new javafx.scene.image.Image(imageFile.toURI().toString()));
                        setGraphic(imageView);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });

        // Set column widths to fill 100% of table width
        double tableWidth = blogTable.getPrefWidth();
        viewColumn.setPrefWidth(tableWidth * 0.1);
        titleColumn.setPrefWidth(tableWidth * 0.2);
        summaryColumn.setPrefWidth(tableWidth * 0.25);
        statusColumn.setPrefWidth(tableWidth * 0.13);
        dateColumn.setPrefWidth(tableWidth * 0.15);
        authorColumn.setPrefWidth(tableWidth * 0.15);
        imageColumn.setPrefWidth(tableWidth * 0.18);
    }

    private void loadBlogs() {
        blogs.setAll(blogService.recuperer());
        blogTable.setItems(blogs);
    }

    private void setupSelectionListener() {
        blogTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                boolean isSelected = newSelection != null;
                updateButton.setDisable(!isSelected);
                deleteButton.setDisable(!isSelected);
                publishButton.setDisable(!isSelected);
            }
        );
    }

    private void setupStatusFilter() {
        statusCombo.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> filterBlogsByStatus(newVal)
        );
    }

    private void filterBlogsByStatus(String status) {
        if (status.equals("All")) {
            blogTable.setItems(blogs);
        } else {
            ObservableList<Blog> filtered = blogs.filtered(
                blog -> blog.getStatus().equalsIgnoreCase(status)
            );
            blogTable.setItems(filtered);
        }
    }

    @FXML
    private void handleAdd() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/BlogFormView.fxml"));
            Parent root = loader.load();

            BlogFormController controller = loader.getController();
            controller.setMode(BlogFormController.Mode.ADD);

            Stage stage = new Stage();
            stage.setTitle("Add Blog");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            if (controller.isSaved()) {
                loadBlogs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdate() {
        Blog selected = blogTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/BlogFormView.fxml"));
                Parent root = loader.load();

                BlogFormController controller = loader.getController();
                controller.setMode(BlogFormController.Mode.EDIT);
                controller.setBlog(selected);

                Stage stage = new Stage();
                stage.setTitle("Edit Blog");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(root));
                stage.showAndWait();

                if (controller.isSaved()) {
                    loadBlogs();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleDelete() {
        Blog selected = blogTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            blogService.supprimer(selected);
            loadBlogs(); // Refresh table
        }
    }

    @FXML
    private void handlePublish() {
        Blog selected = blogTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setStatus("Published");
            blogService.modifier(selected);
            loadBlogs(); // Refresh table
        }
    }

    @FXML
    private void handleViewComments() {
        Blog selected = blogTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CommentDashboardView.fxml"));
                Parent root = loader.load();

                CommentDashboardController controller = loader.getController();
                controller.setBlogId(selected.getId());

                Stage stage = new Stage();
                stage.setTitle("Comments for Blog: " + selected.getTitre());
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(root));
                stage.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Blog Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a blog to view comments.");
            alert.showAndWait();
        }
    }
}
