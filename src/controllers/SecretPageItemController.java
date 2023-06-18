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
import static controllers.DatabaseAccess.config;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class SecretPageItemController implements Initializable {

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
    private String recordYear = null;
    @FXML
    private Label receiptNumber;
    @FXML
    private Label receiptNumberDate;
    String id = null;
    @FXML
    private Label regisNO11;
    @FXML
    private Label recipientDate11;
    @FXML
    private Label saveFaile11;
    @FXML
    private Label regisNO1;
    @FXML
    private Label recipientDate1;
    @FXML
    private Label saveFaile1;
    @FXML
    private Label regisNO111;
    @FXML
    private Label recipientDate111;

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
        id = secretModel.getId();
        circularid.setText(secretModel.getCircularid());
        circulardete.setText(secretModel.getCirculardate());
        receiptNumber.setText(secretModel.getReceiptNumber());
        receiptNumberDate.setText(secretModel.getReceiptNumberDate());
        destination.setText(secretModel.getDestination());
        topic.setText(secretModel.getTopic());
        saveFile.setText(secretModel.getSaveFile());
        note.setText(secretModel.getNote());
        recordYear = secretModel.getRecordYear();
    }

    @FXML
    private void scanImage(ActionEvent event) {
        try {
            DatabaseAccess.insertImage("secretdata", " `ID` ='" + id + "' AND RECORDYEAR ='" + recordYear + "'");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void addNames(ActionEvent event) {
        App.lodAddNmaesPage(id, recordYear, "secret");
    }

    @FXML
    private void showImage(ActionEvent event) {
        byte[] pdfimage = DatabaseAccess.getSecretPdfFile(id, recordYear);
        ShowPdf.writePdf(pdfimage);
    }

    private void delete(ActionEvent event) throws SQLException {
        try {
            DatabaseAccess.delete("secretdata", " `CIRCULARID` ='" + id + "' AND RECORDYEAR ='" + recordYear + "'");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void printBarcod(ActionEvent event) {
         try {
            Connection con = DatabaseConniction.dbConnector();
            JasperDesign recipientReport = JRXmlLoader.load(config.getAppURL() + "\\reports\\Secret‏‏Barcod.jrxml");
            ResultSet rs = DatabaseAccess.getData("SELECT  RECEIPTNUMBER, RECEIPTDATE, DESTINATION, SAVEFILE ,RECORDYEAR FROM secretdata "
                    + "WHERE `RECEIPTNUMBER` = '" +secretModel.getReceiptNumber() + "' AND `RECORDYEAR` = '" + recordYear + "'");

            String regisNo = null;
            String recipientDate = null;
            String circularDir = null;
            int quRegisNo = 0;
            String unitName = null;
            String saveFile = null;
            String recordYear = null;
            while (rs.next()) {
                regisNo = ArabicSetting.EnglishToarabic(Integer.toString(rs.getInt("RECEIPTNUMBER")));
                recipientDate = ArabicSetting.EnglishToarabic(rs.getString("RECEIPTDATE")) + "هـ";
                circularDir = rs.getString("DESTINATION");
                saveFile = ArabicSetting.EnglishToarabic(rs.getString("SAVEFILE"));
                quRegisNo = rs.getInt("RECEIPTNUMBER");
                recordYear = rs.getString("RECORDYEAR");
                unitName = DatabaseAccess.getUintName();
            }
            Map barrcod = new HashMap();
            barrcod.put("ex_id", regisNo);
            barrcod.put("ex_date", recipientDate);
            barrcod.put("dir_to", circularDir);
            barrcod.put("qex_id", quRegisNo);
            barrcod.put("unitName", unitName);
            barrcod.put("savefile", saveFile);
            barrcod.put("recordYear", recordYear);
            JasperReport jr = JasperCompileManager.compileReport(recipientReport);
            JasperPrint jp = JasperFillManager.fillReport(jr, barrcod, con);
//            JasperPrintManager.printReport(jp, false);
          JasperViewer.viewReport(jp, false);

        } catch (IOException | JRException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

}
