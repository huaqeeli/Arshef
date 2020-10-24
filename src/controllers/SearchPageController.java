/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import trainingdata.App;

public class SearchPageController implements Initializable {

    @FXML
    private TextField milataryid;
    @FXML
    private ComboBox<?> coursname;
    @FXML
    private TextField coursplace;

    @FXML
    private BorderPane content;
    @FXML
    private TextField IdentityMilatryId;
    ObservableList<String> coursComboBoxlist = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        refreshListCombobox();
    }

    @FXML
    private void lodSearchByMiltaryidPage(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/view/searchPages/searchByMiltaryId.fxml"));
            Parent root = fxmlLoader.load();
            SearchByMiltaryIdController controller = new SearchByMiltaryIdController();
            controller = fxmlLoader.getController();
            controller.setMiltaryId(milataryid.getText());
            content.setCenter(root);
        } catch (IOException ex) {
            Logger.getLogger(SearchPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void lodSearchByCoursNamePage(ActionEvent event) {
    }

    @FXML
    private void lodSearchByCoursplacePage(ActionEvent event) {
    }

    @FXML
    private void lodIdentityPage(ActionEvent event) {
    }

    private ObservableList filleCoursNames(ObservableList list) {
        try {
            ResultSet rs = DatabaseAccess.select("coursnames");
            try {
                while (rs.next()) {
                    list.add(rs.getString("CORSNAME"));
                }
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(TrainingDataPageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(TrainingDataPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public void refreshListCombobox() {
        coursname.setItems(filleCoursNames(coursComboBoxlist));
    }

    public void clearListCombobox() {
        coursname.getItems().clear();
    }
}
