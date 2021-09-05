package controllers;

import Serveces.ExternalIncomingPageListener;
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
import modeles.ArchefModel;

public class ExternalIncomingItemController implements Initializable {

    @FXML
    private Label circularid;
    @FXML
    private Label circularDate;
    @FXML
    private Label receiptNumber;
    @FXML
    private Label receiptDate;
    @FXML
    private Label topic;
    @FXML
    private Label Destination;
    @FXML
    private Label saveFile;
    @FXML
    private Label notes;
    private ArchefModel archefModel;
    private ExternalIncomingPageListener mylistener;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setData(ArchefModel archefModel, ExternalIncomingPageListener mylistener) {
        this.archefModel = archefModel;
        this.mylistener = mylistener;
        receiptNumber.setText(archefModel.getReceiptNumber());
        receiptDate.setText(archefModel.getReceiptDate());
        circularDate.setText(archefModel.getCircularDate());
        circularid.setText(archefModel.getCircularid());
        topic.setText(archefModel.getTopic());
        Destination.setText(archefModel.getDestination());
        saveFile.setText(archefModel.getSaveFile());
        notes.setText(archefModel.getAction());
    }

    @FXML
    private void scanImage(ActionEvent event) {
        try {
            DatabaseAccess.insertImage("externalincoming", " `CIRCULARID` ='" + circularid.getText() + "' AND CIRCULARDATE ='" +  circularDate.getText() + "'");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void addNames(ActionEvent event) {
        App.lodAddNmaesPage(circularid.getText(), AppDate.getYear(circularDate.getText()),"external");
    }

    @FXML
    private void showImage(ActionEvent event) {
        byte[] pdfimage = DatabaseAccess.getPdfFile(circularid.getText(), circularDate.getText());
        ShowPdf.writePdf(pdfimage);
    }

    @FXML
    private void click(MouseEvent event) {
        mylistener.onClickListener(archefModel);
    }

}
