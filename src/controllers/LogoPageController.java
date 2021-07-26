package controllers;

import Validation.FormValidation;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class LogoPageController implements Initializable {
    
    @FXML
    private Label countCours;
    @FXML
    private Label countParson;
    @FXML
    private Label countAll;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        countAll.setText(getCountAll());
        countParson.setText(getCountParson());
        countCours.setText(getCountCours());
    }
    
    public String getCountAll() {
        String value = null;
        try {
            ResultSet rs = DatabaseAccess.getData("SELECT COUNT(DISTINCT `MILITARYID`) AS `counts` FROM `personaldata`;");
            if (rs.next()) {
                value = rs.getString("counts");
            }
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return value;
    }
    
    public String getCountParson() {
        String value = null;
        try {
            ResultSet rs = DatabaseAccess.getData("SELECT COUNT(DISTINCT `MILITARYID`) AS `counts` FROM `coursesdata`;");
            if (rs.next()) {
                value = rs.getString("counts");
            }
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return value;
    }
    
    public String getCountCours() {
        String value = null;
        try {
            ResultSet rs = DatabaseAccess.getData("SELECT COUNT(DISTINCT `ID`) AS `counts` FROM `coursesdata`;");
            if (rs.next()) {
                value = rs.getString("counts");
            }
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return value;
    }
    
}
