package controllers;

import Validation.FormValidation;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ConfigSettingController implements Initializable {

    @FXML
    private TextField hostName;
    @FXML
    private TextField dbName;
    @FXML
    private TextField userName;
    @FXML
    private PasswordField password;
    @FXML
    private AnchorPane content;
    @FXML
    private TextField appUrl;
    Config config = new Config();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void close(ActionEvent event) {
        Stage stage = (Stage) content.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void save(ActionEvent event) {
        config.setDbName(dbName.getText());
        config.setHostName(hostName.getText());
        config.setUserName(userName.getText());
        config.setPassword(password.getText());
        config.setAppURL(appUrl.getText());
        FormValidation.showAlert(null, "تم حفظ البيانات", Alert.AlertType.CONFIRMATION);
    }

}
