package controllers;

import Serveces.FollowupPageListener;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import modeles.FollowupModel;

public class FollowupItemController implements Initializable {

    @FXML
    private Label squnce;
    @FXML
    private Label circularid;
    @FXML
    private Label circularDate;
    @FXML
    private Label topic;
    @FXML
    private Label Required;
    @FXML
    private Label Status;
    @FXML
    private Label CompletionDate;
    
    private FollowupModel followupModel;
    private FollowupPageListener followupPageListener;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setData(FollowupModel followupModel, FollowupPageListener followupPageListener) {
        this.followupModel = followupModel;
        this.followupPageListener = followupPageListener;
        squnce.setText(Integer.toString(followupModel.getSqunce()));
        circularid.setText(followupModel.getCircularid());
        circularDate.setText(followupModel.getCirculardate());
        topic.setText(followupModel.getTopic());
        Required.setText(followupModel.getRequired());
        Status.setText(followupModel.getStatus());
        CompletionDate.setText(followupModel.getCompletiondate());

    }

    @FXML
    private void deleteMark(ActionEvent event) {
    }

    @FXML
    private void click(MouseEvent event) {
        followupPageListener.onClickListener(followupModel);
    }

}
