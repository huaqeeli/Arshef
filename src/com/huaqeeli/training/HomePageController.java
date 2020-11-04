package com.huaqeeli.training;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import trainingdata.App;

public class HomePageController implements Initializable {

    @FXML
    private BorderPane content;
    public boolean logOut;
    private Object userNameLabel;
    private String userid;

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
        content.setCenter(App.loadFXML("/view/trainingDataPage"));
    }

    @FXML
    private void searchPage(ActionEvent event) throws IOException {
        content.setCenter(App.loadFXML("/view/searchPage"));
    }

    public void close() {
         Stage stage = (Stage) content.getScene().getWindow();
        stage.close();
    }

    public void setUserName(String userName, String userid) {
//        this.userNameLabel.setText(userName);
        this.userid = userid;
        content.setCenter(App.lodHomePage(userid));
    }

    public void setUserId(String userid) {
       
    }

}
