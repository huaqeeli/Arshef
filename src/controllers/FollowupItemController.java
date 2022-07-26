package controllers;

import Serveces.FollowupPageListener;
import Validation.FormValidation;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import modeles.FollowupModel;

public class FollowupItemController implements Initializable {

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
    @FXML
    private HBox content;
    @FXML
    private Label remingdayes;
    @FXML
    private HBox state;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setData(FollowupModel followupModel, FollowupPageListener followupPageListener) {
        this.followupModel = followupModel;
        this.followupPageListener = followupPageListener;
        circularid.setText(followupModel.getCircularid());
        circularDate.setText(followupModel.getCirculardate());
        topic.setText(followupModel.getTopic());
        Required.setText(followupModel.getRequired());
        Status.setText(followupModel.getStatus());
        CompletionDate.setText(followupModel.getCompletiondate());
        int remingday = AppDate.getRemainingDays(followupModel.getCompletiondate());
        if (remingday < 0) {
            remingdayes.setText("0");
        } else {
            remingdayes.setText(Integer.toString(remingday) + " " + "يوما");
        }
        if (remingday <= 7 && remingday > 5 && followupModel.getOpenStat() == 0) {
            state.setStyle(" -fx-background-color: #F68A1F;");
        } else if (remingday <= 5 && followupModel.getOpenStat() == 0) {
            state.setStyle(" -fx-background-color: #FE0000;");
//            circularid.setStyle(" -fx-text-fill: #FFFFFF;");
//            circularDate.setStyle(" -fx-text-fill: #FFFFFF;");
//            topic.setStyle(" -fx-text-fill: #FFFFFF;");
//            Required.setStyle(" -fx-text-fill: #FFFFFF;");
//            Status.setStyle(" -fx-text-fill: #FFFFFF;");
//            CompletionDate.setStyle(" -fx-text-fill: #FFFFFF;");
//            remingdayes.setStyle(" -fx-text-fill: #FFFFFF;");
        }
    }

    @FXML
    private void deleteMark(ActionEvent event) {
        try {
            int t = DatabaseAccess.updat("followup", " OPENSTAT = 1", "CIRCULARID = '" + circularid.getText() + "' AND CIRCULARDATE = '" + circularDate.getText() + "'");
            if (t > 0) {
                FormValidation.showAlert("", "تم حذف المعاملة حدث الصفحة لمشاهدة التغيير" , Alert.AlertType.CONFIRMATION);
            }
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void click(MouseEvent event) {
        followupPageListener.onClickListener(followupModel);
    }

    @FXML
    private void showImage(ActionEvent event) {
//        byte[] pdfimage = DatabaseAccess.getInternalIncomingPdfFile(regisNO.getText(), recordYear);
//        ShowPdf.writePdf(pdfimage);
    }

}
