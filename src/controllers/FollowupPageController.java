package controllers;

import Serveces.FollowupPageListener;
import Validation.FormValidation;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import modeles.FollowupModel;
import modeles.FormationModel;

public class FollowupPageController implements Initializable {

    @FXML
    private ComboBox<String> searchType;
    @FXML
    private ComboBox<String> year;
    @FXML
    private TextField searchText;
    @FXML
    private ComboBox<String> circularType;
    @FXML
    private TextField circularid;
    @FXML
    private ComboBox<String> circularDateDay;
    @FXML
    private ComboBox<String> circularDateMonth;
    @FXML
    private ComboBox<String> circularDateYear;
    @FXML
    private TextField topic;
    @FXML
    private TextField Required;
    @FXML
    private TextField Status;
    @FXML
    private ComboBox<String> CompletionDateDay;
    @FXML
    private ComboBox<String> CompletionDateMonth;
    @FXML
    private ComboBox<String> CompletionDateYear;
    @FXML
    private VBox vbox;
    List<FollowupModel> followupObject = new ArrayList<>();
    private FollowupPageListener followupPageListener;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void searchData(ActionEvent event) {
    }

    @FXML
    private void save(ActionEvent event) {
    }

    @FXML
    private void edit(ActionEvent event) {
    }

    @FXML
    private void delete(ActionEvent event) {
    }

    @FXML
    private void clear(ActionEvent event) {
    }

    private void refreshData(String uint) {
        try {
            followupObject.clear();
            vbox.getChildren().clear();
            viewdata(DatabaseAccess.getData("SELECT * FROM personaldata WHERE UNIT ='" + uint + "' ORDER BY MILITARYID ASC"));
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    private void viewdata(ResultSet rs) {
        followupObject.addAll(getData(rs));
       
    }

    private List<FollowupModel> getData(ResultSet rs) {
        List<FollowupModel> followupObjects = new ArrayList<>();
       FollowupModel followupModel;
        try {
            int squnce = 0 ;
            while(rs.next()){
                squnce++;
               followupModel = new FollowupModel();
               followupModel.setSqunce(squnce);
               followupModel.setCirculardate(rs.getString(""));
               followupModel.setCircularid(rs.getString(""));
               followupModel.setTopic(rs.getString(""));
               followupModel.setRequired(rs.getString(""));
               followupModel.setStatus(rs.getString(""));
               followupModel.setCompletiondate(rs.getString(""));
               followupObjects.add(followupModel);
            }
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return followupObjects;
    }
    private void setChosendata(FollowupModel followupModel){
        circularid.setText(followupModel.getCircularid());
        setCircularDate(followupModel.getCirculardate());
        topic.setText(followupModel.getTopic());
        Required.setText(followupModel.getRequired());
        Status.setText(followupModel.getStatus());
        setCompletionDate(followupModel.getCompletiondate());
        
    }
    public void setCircularDate(String date) {
        AppDate.setSeparateDate(circularDateDay, circularDateMonth,circularDateYear, date);
    }
    public void setCompletionDate(String date) {
        AppDate.setSeparateDate(CompletionDateDay, CompletionDateMonth,CompletionDateYear, date);
    }
}
