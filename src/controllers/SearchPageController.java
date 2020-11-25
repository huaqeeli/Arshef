/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import Validation.FormValidation;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import modeles.CoursesModel;
import trainingdata.App;

public class SearchPageController implements Initializable {

    @FXML
    private TextField milataryid;
    @FXML
    private ComboBox<String> coursname;
    @FXML
    private TextField coursplace;

    @FXML
    private BorderPane content;
    @FXML
    private TextField IdentityMilatryId;
    ObservableList<String> coursComboBoxlist = FXCollections.observableArrayList();
    ObservableList<String> coursComboBoxlist1 = FXCollections.observableArrayList();
    @FXML
    private ComboBox<String> coursname1;
    @FXML
    private TextField coursplace1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clearListCombobox();
        refreshListCombobox();
//        refreshListCombobox1();
    }

    @FXML
    private void lodSearchByMiltaryidPage(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/view/searchPages/searchByMiltaryId.fxml"));
            Parent root = fxmlLoader.load();
            SearchByMiltaryIdController controller = new SearchByMiltaryIdController();
            controller = (SearchByMiltaryIdController) fxmlLoader.getController();
            controller.setMiltaryId(milataryid.getText());
            content.setCenter(root);
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void lodSearchByCoursNamePage(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/view/searchPages/searchByCoursName.fxml"));
            Parent root = fxmlLoader.load();
            SearchByCoursNameController controller = new SearchByCoursNameController();
            controller = (SearchByCoursNameController) fxmlLoader.getController();
            controller.setCuoursId(CoursesModel.getCoursId(coursname.getValue()), coursname.getValue());
            content.setCenter(root);
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void lodSearchByCoursplacePage(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/view/searchPages/searchByCoursPlace.fxml"));
            Parent root = fxmlLoader.load();
            SearchByCoursPlaceController controller = new SearchByCoursPlaceController();
            controller = (SearchByCoursPlaceController) fxmlLoader.getController();
            controller.setCuoursId(coursplace.getText());
            content.setCenter(root);
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void lodSearchByCoursplaceAndCoursNamePage(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/view/searchPages/searchByCoursplaceAndCoursName.fxml"));
            Parent root = fxmlLoader.load();
            SearchByCoursplaceAndCoursNamePageController controller = new SearchByCoursplaceAndCoursNamePageController();
            controller = (SearchByCoursplaceAndCoursNamePageController) fxmlLoader.getController();
            controller.setCuoursId(CoursesModel.getCoursId(coursname1.getValue()), coursplace1.getText());
            content.setCenter(root);
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void lodIdentityPage(ActionEvent event) {
        try {
            //        try {
//            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/view/searchPages/prentIdentiti.fxml"));
//            Parent root = fxmlLoader.load();
//            PrentIdentitiController controller = new PrentIdentitiController();
//            controller = (PrentIdentitiController) fxmlLoader.getController();
//            controller.setMiltaryId(IdentityMilatryId.getText());
//            content.setCenter(root);
//        } catch (IOException ex) {
//             FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
//             System.out.println(ex);
//        }

            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/view/searchPages/prentIdentiti.fxml"));
            Parent root = fxmlLoader.load();
            PrentIdentitiController controller = new PrentIdentitiController();
            controller = (PrentIdentitiController) fxmlLoader.getController();
            controller.setMiltaryId(IdentityMilatryId.getText());
            content.setCenter(root);
        } catch (IOException ex) {
            Logger.getLogger(SearchPageController.class.getName()).log(Level.SEVERE, null, ex);
        }

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
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return list;
    }

    public void refreshListCombobox() {
        coursname.setItems(filleCoursNames(coursComboBoxlist));
        coursname1.setItems(filleCoursNames(coursComboBoxlist1));
    }

    public void refreshListCombobox1() {
//        coursname.setItems(filleCoursNames(coursComboBoxlist));
        coursname1.setItems(filleCoursNames(coursComboBoxlist1));
    }

    public void clearListCombobox() {
        coursname.getItems().clear();
        coursname1.getItems().clear();
    }

}
