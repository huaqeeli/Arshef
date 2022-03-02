package controllers;

import Serveces.ExternalExportsPageListener;
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
import modeles.ExportsModel;

public class ExternalExportsItemController implements Initializable {

    @FXML
    private Label saveNum;
    @FXML
    private Label entryDate;
    @FXML
    private Label topic;
    @FXML
    private Label Destination;
    @FXML
    private Label saveFile;
    @FXML
    private Label exportNum;
    @FXML
    private Label exportDate;
    @FXML
    private Label notes;
    private ExportsModel exportsModel;
    private ExternalExportsPageListener mylistener;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setData(ExportsModel exportsModel, ExternalExportsPageListener mylistener) {
        this.exportsModel = exportsModel;
        this.mylistener = mylistener;
        saveNum.setText(exportsModel.getId());
        entryDate.setText(exportsModel.getEntryDate());
        topic.setText(exportsModel.getTopic());
        Destination.setText(exportsModel.getDestination());
        saveFile.setText(exportsModel.getSaveFile());
        exportNum.setText(exportsModel.getExportNum());
        exportDate.setText(exportsModel.getExportDate());
        notes.setText(exportsModel.getNotes());
    }

    @FXML
    private void scanImage(ActionEvent event) {
        try {
            DatabaseAccess.insertImage("exportsdata", " `ID` ='" + saveNum.getText() + "' AND ENTRYDATE ='" + entryDate.getText() + "'");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void addNames(ActionEvent event) {
        App.lodAddNmaesPage(saveNum.getText(), AppDate.getYear(entryDate.getText()), "external");
    }

    @FXML
    private void showImage(ActionEvent event) {
        byte[] pdfimage = DatabaseAccess.getExportPdfFile(saveNum.getText(), entryDate.getText());
        ShowPdf.writePdf(pdfimage);
    }

    @FXML
    private void click(MouseEvent event) {
        mylistener.onClickListener(exportsModel);
    }

}
