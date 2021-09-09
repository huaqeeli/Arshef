package controllers;

import Serveces.InternalIncomingPageListener;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import modeles.InternalIncomingModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class InternalIncomingItemController implements Initializable {

    @FXML
    private Label regisNO;
    @FXML
    private Label recipientDate;
    @FXML
    private Label circularNo;
    @FXML
    private Label circularDate;
    @FXML
    private Label destination;
    @FXML
    private Label topic;
    @FXML
    private Label saveFaile;
    @FXML
    private Label notes;

    private InternalIncomingModel internalIncomingModel;
    private InternalIncomingPageListener mylistener;
    private String recordYear = null;
    Config config = new Config();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setData(InternalIncomingModel internalIncomingModel, InternalIncomingPageListener mylistener) {
        this.internalIncomingModel = internalIncomingModel;
        this.mylistener = mylistener;
        regisNO.setText(internalIncomingModel.getRegisNo());
        recipientDate.setText(internalIncomingModel.getRecipientDate());
        circularNo.setText(internalIncomingModel.getCircularNo());
        circularDate.setText(internalIncomingModel.getCircularDate());
        destination.setText(internalIncomingModel.getCircularDir());
        topic.setText(internalIncomingModel.getTopic());
        saveFaile.setText(internalIncomingModel.getSaveFile());
        notes.setText(internalIncomingModel.getNotes());
        recordYear = internalIncomingModel.getRecordYear();
    }

    @FXML
    private void scanImage(ActionEvent event) {
        try {
            DatabaseAccess.insertImage("internalincoming", " `REGIS_NO` ='" + regisNO.getText() + "' AND RECORD_YEAR ='" + recordYear + "'");
        } catch (IOException ex) {
            Logger.getLogger(InternalIncomingItemController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void addNames(ActionEvent event) {
        App.lodAddNmaesPage(regisNO.getText(), recordYear, "internal");
    }

    @FXML
    private void showImage(ActionEvent event) {
        byte[] pdfimage = DatabaseAccess.getInternalIncomingPdfFile(regisNO.getText(), recordYear);
        ShowPdf.writePdf(pdfimage);
    }

    @FXML
    private void click(MouseEvent event) {
        mylistener.onClickListener(internalIncomingModel);
    }

    @FXML
    private void printBarcod(ActionEvent event) {
        try {
            Connection con = DatabaseConniction.dbConnector();
            JasperDesign recipientReport = JRXmlLoader.load(config.getAppURL() + "\\reports\\RecipientBarcod.jrxml");
            ResultSet rs = DatabaseAccess.select("internalincoming", "REGIS_NO = '" + regisNO.getText() + "'");
            String regisNo = null;
            String recipientDate = null;
            String circularDir = null;
            int quRegisNo = 0;
            String unitName = null;
            String saveFile = null;
            while (rs.next()) {
                regisNo = ArabicSetting.EnglishToarabic(Integer.toString(rs.getInt("REGIS_NO")));
                recipientDate = ArabicSetting.EnglishToarabic(rs.getString("RECIPIENT_DATE")) + "هـ";
                circularDir = rs.getString("CIRCULAR_DIR");
                saveFile = ArabicSetting.EnglishToarabic(rs.getString("SAVE_FILE"));
                quRegisNo = rs.getInt("REGIS_NO");
                unitName = DatabaseAccess.getUintName();
            }
            Map barrcod = new HashMap();
            barrcod.put("ex_id", regisNo);
            barrcod.put("ex_date", recipientDate);
            barrcod.put("dir_to", circularDir);
            barrcod.put("qex_id", quRegisNo);
            barrcod.put("unitName", unitName);
            barrcod.put("savefile", saveFile);
            JasperReport jr = JasperCompileManager.compileReport(recipientReport);
            JasperPrint jp = JasperFillManager.fillReport(jr, barrcod, con);
            JasperPrintManager.printReport(jp, false);
//                JasperViewer.viewReport(jp, false);

        } catch (IOException | SQLException | JRException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

}
