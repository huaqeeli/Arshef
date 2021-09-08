package controllers;

import Serveces.FormationPageListener;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import modeles.FormationModel;

public class FormationItemController implements Initializable {

    @FXML
    private Label militaryID;
    @FXML
    private Label personalID;
    @FXML
    private Label rank;
    @FXML
    private Label name;
    @FXML
    private Label uint;
    @FXML
    private Label note;

    private FormationModel formationModel;
    private FormationPageListener myListener;
    @FXML
    private Label squnce;
    @FXML
    private HBox content;
    @FXML
    private Label specializ;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setData(FormationModel formationModel, FormationPageListener mylistener) {
        this.formationModel = formationModel;
        this.myListener = mylistener;
        squnce.setText(Integer.toString(formationModel.getSqunce()));
        militaryID.setText(formationModel.getMilitaryID());
        personalID.setText(formationModel.getPersonalID());
        rank.setText(formationModel.getRank());
        name.setText(formationModel.getName());
        uint.setText(formationModel.getUint());
        note.setText(formationModel.getNote());
        specializ.setText(formationModel.getSpecializ());
        if (formationModel.getMarkState() == 0) {
            content.setStyle("-fx-background-color: #E9E9E9;");
        } else {
            content.setStyle("-fx-background-color: #8CA598;");
        }
    }

    @FXML
    private void cilck(MouseEvent event) {
        myListener.onClickListener(formationModel);
    }

    @FXML
    private void addMark(ActionEvent event) {

        try {
            DatabaseAccess.updat("personaldata", " MARK = 1", "MILITARYID = '" + militaryID.getText() + "'");
            content.setStyle("-fx-background-color: #8CA598;");
        } catch (IOException ex) {
            Logger.getLogger(FormationItemController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void deleteMark(ActionEvent event) {
        try {
            DatabaseAccess.updat("personaldata", " MARK = 0", "MILITARYID = '" + militaryID.getText() + "'");
            content.setStyle("-fx-background-color: #E9E9E9;");
        } catch (IOException ex) {
            Logger.getLogger(FormationItemController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
