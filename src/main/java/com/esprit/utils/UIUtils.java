package com.esprit.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class UIUtils {

    public static void showAlert(String title, String header, String message, String alertTypeStr) {
        Alert.AlertType alertType;
        try {
            alertType = Alert.AlertType.valueOf(alertTypeStr.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            alertType = Alert.AlertType.INFORMATION; // default
        }
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static boolean showConfirmation(String title, String message) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle(title);
        confirm.setHeaderText(message);
        confirm.setContentText("Are you sure?");
        Optional<ButtonType> result = confirm.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

}
