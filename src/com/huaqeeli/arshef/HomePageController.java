package com.huaqeeli.arshef;

import Validation.FormValidation;
import controllers.LoginPageController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import arshef.App;

public class HomePageController implements Initializable {

    @FXML
    private BorderPane content;

    @FXML
    private Label rankLable;
    @FXML
    private Label usernameLable;
    String username;
    String userrank;
    String usertype;
    String userId;
    public boolean logOut;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            content.setCenter(App.loadFXML("/view/ExternalIncomingPage"));
        } catch (IOException ex) {
           FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    private void close(MouseEvent event) {
        Stage stage = (Stage) content.getScene().getWindow();
        stage.close();
    }

    private void personalData(ActionEvent event) throws IOException {
        content.setCenter(App.loadFXML("/view/personalDataPage"));
    }

    private void searchPage(ActionEvent event) throws IOException {
        content.setCenter(App.loadFXML("/view/searchPage"));
    }

    public void close() {
        Stage stage = (Stage) content.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void userPage(ActionEvent event) throws IOException {
        if ("مدير".equals(usertype)) {
            content.setCenter(App.loadFXML("/view/UsersPage"));
        } else {
            FormValidation.showAlert(null, "ليس لديك الصلاحية في الدخول", Alert.AlertType.ERROR);
        }

    }

    public void setData(String userName, String rank, String userType, String userid) throws IOException {
        username = userName;
        userrank = rank;
        usertype = userType;
        userId = userid;
        rankLable.setText(userrank);
        usernameLable.setText(username);

    }

    @FXML
    private void logoPageLod(MouseEvent event) throws IOException {
        content.setCenter(App.loadFXML("/view/LogoPage"));
    }

    @FXML
    private void logout(ActionEvent event) {
        try {
            close();
            LoginPageController login = new LoginPageController();
            login.lodLogingPage();
            logOut = true;
        } catch (IOException ex) {
            Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void insertDataPage(ActionEvent event) throws IOException {
        content.setCenter(App.loadFXML("/view/ExternalIncomingPage"));
    }

    @FXML
    private void addNewDaetination(ActionEvent event) throws IOException {
        content.setCenter(App.loadFXML("/view/AddNewDestination"));
    }

    @FXML
    private void addExportsPage(ActionEvent event) throws IOException {
        content.setCenter(App.loadFXML("/view/ExternalExportsPage"));
    }

    @FXML
    private void addDisplayPage(ActionEvent event) throws IOException {
         content.setCenter(App.loadFXML("/view/DisplayPage"));
    }

    @FXML
    private void secretPage(ActionEvent event) throws IOException {
         content.setCenter(App.loadFXML("/view/secretPage"));
    }

    @FXML
    private void censusPage(ActionEvent event) throws IOException {
        content.setCenter(App.loadCensusPage(usertype));
    }

    @FXML
    private void InternalIncomingPage(ActionEvent event) throws IOException {//InternalExportsPage
         content.setCenter(App.loadFXML("/view/InternalIncomingPage"));
    }

    @FXML
    private void InternalExportsPage(ActionEvent event) throws IOException {
         content.setCenter(App.loadFXML("/view/InternalExportsPage"));
    }

    @FXML
    private void followupPage(ActionEvent event) throws IOException {
             content.setCenter(App.loadFXML("/view/FollowupPage"));
    }

    @FXML
    private void formationPage(ActionEvent event) throws IOException {
        content.setCenter(App.loadFXML("/view/FormationPage"));
    }

}
