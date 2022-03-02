package controllers;

import Serveces.ExternalIncomingPageListener;
import Validation.FormValidation;
import arshef.App;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import modeles.ArchefModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

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
    Config config = new Config();

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
            DatabaseAccess.insertImage("externalincoming", " `CIRCULARID` ='" + circularid.getText() + "' AND CIRCULARDATE ='" + circularDate.getText() + "'");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void addNames(ActionEvent event) {
        App.lodAddNmaesPage(circularid.getText(), AppDate.getYear(circularDate.getText()), "external");
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

    @FXML
    private void printBarcod(ActionEvent event) {
        try {
            Connection con = DatabaseConniction.dbConnector();
            JasperDesign recipientReport = JRXmlLoader.load(config.getAppURL() + "\\reports\\Externl‏‏RecipientBarcod.jrxml");
            ResultSet rs = DatabaseAccess.select("externalincoming", "RECEIPTNUMBER = '" + archefModel.getReceiptNumber() + "'AND ARSHEFYEAR = '"+AppDate.getYear(archefModel.getReceiptDate())+"'");
            String regisNo = null;
            String recipientDate = null;
            String circularDir = null;
            int quRegisNo = 0;
            String unitName = null;
            String saveFile = null;
            while (rs.next()) {
                regisNo = ArabicSetting.EnglishToarabic(Integer.toString(rs.getInt("RECEIPTNUMBER")));
                recipientDate = ArabicSetting.EnglishToarabic(rs.getString("RECEIPTDATE")) + "هـ";
                circularDir = rs.getString("DESTINATION");
                quRegisNo = rs.getInt("RECEIPTNUMBER");
                saveFile = ArabicSetting.EnglishToarabic(rs.getString("SAVEFILE"));
                unitName = DatabaseAccess.getUintName();
            }
            Map barrcod = new HashMap();
            barrcod.put("ex_id", regisNo);
            barrcod.put("ex_date", recipientDate);
            barrcod.put("dir_to", circularDir);
            barrcod.put("qex_id", quRegisNo);
            barrcod.put("savefile", saveFile);
            barrcod.put("unitName", unitName);
            JasperReport jr = JasperCompileManager.compileReport(recipientReport);
            JasperPrint jp = JasperFillManager.fillReport(jr, barrcod, con);
            JasperPrintManager.printReport(jp, false);
//                JasperViewer.viewReport(jp, false);

        } catch (IOException | SQLException | JRException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }

    }

}
