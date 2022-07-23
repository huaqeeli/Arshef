package controllers;

import Serveces.AnalyticesListener;
import Validation.FormValidation;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import modeles.AnalyticsModel;

public class AnalyticsItemController implements Initializable {

    @FXML
    private HBox content;
    @FXML
    private Label squnce;
    @FXML
    private Label militaryID;
    @FXML
    private Label rank;
    @FXML
    private Label name;
    @FXML
    private Label uint;
    @FXML
    private Label stage;
    @FXML
    private Label analyticsdate;
    private AnalyticesListener mylistener;
    private AnalyticsModel analyticsModel;
    String id = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setData(AnalyticsModel analyticsModel, AnalyticesListener mylistener) {
        this.analyticsModel = analyticsModel;
        this.mylistener = mylistener;
        squnce.setText(Integer.toString(analyticsModel.getSqunce()));
        militaryID.setText(analyticsModel.getMilitaryid());
        rank.setText(analyticsModel.getRank());
        name.setText(analyticsModel.getName());
        uint.setText(analyticsModel.getUint());
        stage.setText(analyticsModel.getStage());
        analyticsdate.setText(analyticsModel.getAnalyticesDate());
        id = analyticsModel.getId();
    }

    @FXML
    private void delete(ActionEvent event) {
        try {
            boolean state = DatabaseAccess.deleteAll("analytics", "ID = '" + id + "'");
            if (state) {
                FormValidation.showAlert(null, "تم حذف البيانات ", Alert.AlertType.INFORMATION);
            }else{
             FormValidation.showAlert(null, "حدثت مشكلة ف عملية الحذف ", Alert.AlertType.ERROR);
            }
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void cilck(MouseEvent event) {
        mylistener.onClickListener(analyticsModel);
    }

}
