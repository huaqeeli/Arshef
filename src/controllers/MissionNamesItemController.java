package controllers;

import Serveces.MissionNameListener;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import modeles.UserModel;

public class MissionNamesItemController implements Initializable {

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
    private UserModel userModel;
    private MissionNameListener mylistener;
    String missionID = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setData(UserModel userModel, MissionNameListener mylistener) {
        this.userModel = userModel;
        this.mylistener = mylistener;
        squnce.setText(Integer.toString(userModel.getSqunce()));
        militaryID.setText(userModel.getMilitaryid());
        rank.setText(userModel.getRank());
        name.setText(userModel.getName());
        uint.setText(userModel.getUint());
    }

    @FXML
    private void cilck(MouseEvent event) {
    }

}
