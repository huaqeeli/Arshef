package com.huaqeeli.training;

import Validation.FormValidation;
import controllers.LoginPageController;
import controllers.TrainingDataPageController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import trainingdata.App;

public class HomePageController implements Initializable {

    @FXML
    private BorderPane content;
    public boolean logOut;

    @FXML
    private Label rankLable;
    @FXML
    private Label usernameLable;
    String username;
    String userrank;
    String usertype;
    String userId;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    private void close(MouseEvent event) {
        Stage stage = (Stage) content.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void personalData(ActionEvent event) throws IOException {
        content.setCenter(App.loadFXML("/view/personalDataPage"));
    }

    @FXML
    private void trainingData(ActionEvent event) throws IOException {
         try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/view/trainingDataPage.fxml"));
            Parent root = fxmlLoader.load();
            TrainingDataPageController controller = new TrainingDataPageController();
            controller = (TrainingDataPageController) fxmlLoader.getController();
            controller.setUserId(userId);
            content.setCenter(root);
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }

    }

    @FXML
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
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void logoPageLod(MouseEvent event) throws IOException {
        content.setCenter(App.loadFXML("/view/LogoPage"));
    }

}
