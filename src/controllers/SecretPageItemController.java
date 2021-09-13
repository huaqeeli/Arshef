package controllers;

import Validation.FormValidation;
import arshef.App;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import modeles.SecretModel;
import Serveces.SecretPageListener;

public class SecretPageItemController implements Initializable {

    @FXML
    private Label squnse;
    @FXML
    private Label circularid;
    @FXML
    private Label circulardete;
    @FXML
    private Label destination;
    @FXML
    private Label topic;
    @FXML
    private Label saveFile;
    @FXML
    private Label note;

    private SecretModel secretModel;
    private SecretPageListener mylistener;
    @FXML
    private HBox content;
    @FXML
    private Label circulardete1;
    @FXML
    private Label destination1;
    @FXML
    private Label topic1;
    @FXML
    private Label topic11;
    @FXML
    private Label saveFile1;
    private String recordYear = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void click(MouseEvent mouseEvent) {
        mylistener.onClickListener(secretModel);
    }

    public void setData(SecretModel secretModel, SecretPageListener mylistener) {
        this.secretModel = secretModel;
        this.mylistener = mylistener;
        squnse.setText(Integer.toString(secretModel.getSqunse()));
        circularid.setText(secretModel.getCircularid());
        circulardete.setText(secretModel.getCirculardate());
        destination.setText(secretModel.getDestination());
        topic.setText(secretModel.getTopic());
        saveFile.setText(secretModel.getSaveFile());
        note.setText(secretModel.getNote());
        recordYear = secretModel.getRecordYear();
    }

    @FXML
    private void scanImage(ActionEvent event) {
        try {
            DatabaseAccess.insertImage("secretdata", " `CIRCULARID` ='" + circularid.getText() + "' AND RECORDYEAR ='" + recordYear + "'");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void addNames(ActionEvent event) {
        App.lodAddNmaesPage(circularid.getText(), recordYear, "secret");
    }

    @FXML
    private void showImage(ActionEvent event) {
        byte[] pdfimage = DatabaseAccess.getSecretPdfFile(circularid.getText(), recordYear);
        ShowPdf.writePdf(pdfimage);
    }

    @FXML
    private void delete(ActionEvent event) {
        try {
            DatabaseAccess.delete("secretdata", " `CIRCULARID` ='" + circularid.getText() + "' AND RECORDYEAR ='" + recordYear + "'");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

}
