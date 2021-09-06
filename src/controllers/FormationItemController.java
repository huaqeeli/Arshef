package controllers;

import Serveces.FormationPageListener;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
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
    }
    @FXML
    private void cilck(MouseEvent event) {
        myListener.onClickListener(formationModel);
    }

}
