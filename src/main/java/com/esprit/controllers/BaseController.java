package com.esprit.controllers;

import com.esprit.utils.UIUtils;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class BaseController<T> implements Initializable {

    @Override
    public abstract void initialize(URL location, ResourceBundle resources);

    protected void showAlert(String title, String message) {
        UIUtils.showAlert(title, message);
    }



    protected abstract ObservableList<T> loadData();

}
