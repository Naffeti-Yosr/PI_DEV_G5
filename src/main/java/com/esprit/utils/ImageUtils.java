package com.esprit.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageUtils {
    private static final String DEFAULT_IMAGE_PATH = "/images/products/default_renewable.png";
    
    public static ImageView createImageView(String imagePath, int width, int height) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(true);
        imageView.setImage(loadImage(imagePath));
        return imageView;
    }

    public static Image loadImage(String imagePath) {
        try {
            if (imagePath == null || imagePath.isEmpty() || imagePath.startsWith("data:image/svg+xml")) {
                return getDefaultImage();
            }

            // Check if imagePath is the default image filename, return default image directly
            if ("default_renewable.png".equals(imagePath) || DEFAULT_IMAGE_PATH.endsWith(imagePath)) {
                return getDefaultImage();
            }

            // Try loading from file system first
            File file = new File("src/main/resources/images/products/" + imagePath);
            if (file.exists()) {
                return new Image(file.toURI().toString());
            }

            // Fallback to resource stream
            String resourcePath = imagePath.startsWith("/") ? imagePath : "/images/products/" + imagePath;
            InputStream stream = ImageUtils.class.getResourceAsStream(resourcePath);
            
            if (stream != null) {
                return new Image(stream);
            }
            return getDefaultImage();
        } catch (Exception e) {
            System.err.println("Error loading image: " + imagePath + " - " + e.getMessage());
            return getDefaultImage();
        }
    }

    
    private static Image getDefaultImage() {
        try {
            InputStream stream = ImageUtils.class.getResourceAsStream(DEFAULT_IMAGE_PATH);
            if (stream != null) {
                return new Image(stream);
            }
            System.err.println("Default image not found at: " + DEFAULT_IMAGE_PATH);
            return createColorPlaceholder();
        } catch (Exception e) {
            System.err.println("Error loading default image: " + e.getMessage());
            return createColorPlaceholder();
        }
    }
    
    private static Image createColorPlaceholder() {
        return new Image(new ByteArrayInputStream(new byte[0]), 150, 150, true, true);
    }

    public static String saveProductImage(File imageFile, String productName) {
        try {
            String resourcesPath = "src/main/resources/images/products/";
            Files.createDirectories(Paths.get(resourcesPath));
            
            String originalName = imageFile.getName();
            String extension = originalName.contains(".") ? 
                originalName.substring(originalName.lastIndexOf(".")) : ".jpg";
            
            String cleanName = productName.replaceAll("[^a-zA-Z0-9]", "_").toLowerCase();
            String timestamp = String.valueOf(System.currentTimeMillis());
            String fileName = cleanName + "_" + timestamp + extension;
            String destPath = resourcesPath + fileName;
            
            Files.copy(imageFile.toPath(), Paths.get(destPath), StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (Exception e) {
            System.err.println("Error saving product image: " + e.getMessage());
            return DEFAULT_IMAGE_PATH;
        }
    }

    public static boolean deleteProductImage(String imagePath) {
        try {
            String resourcesPath = "src/main/resources/images/products/";
            File file = new File(resourcesPath + imagePath);
            if (file.exists() && file.isFile()) {
                return file.delete();
            }
        } catch (Exception e) {
            System.err.println("Error deleting product image: " + e.getMessage());
        }
        return false;
    }

}
