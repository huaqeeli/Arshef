package controllers;

import Serveces.InternalExportsPageListener;
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
import modeles.InternalExportsModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class InternalExportsItemController implements Initializable {

    @FXML
    private Label regisNO;
    @FXML
    private Label exportsDate;
    @FXML
    private Label Destination;
    @FXML
    private Label topic;
    @FXML
    private Label saveFile;
    @FXML
    private Label notes;
    private InternalExportsModel internalExportsModel;
    InternalExportsPageListener mylistener;
    private String recordYear = null;
    Config config = new Config();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setData(InternalExportsModel internalExportsModel, InternalExportsPageListener mylistener) {
        this.internalExportsModel = internalExportsModel;
        this.mylistener = mylistener;
        regisNO.setText(internalExportsModel.getRegisNO());
        exportsDate.setText(internalExportsModel.getExportsDate());
        Destination.setText(internalExportsModel.getDestination());
        topic.setText(internalExportsModel.getTopic());
        saveFile.setText(internalExportsModel.getSaveFile());
        notes.setText(internalExportsModel.getNotes());
        recordYear = internalExportsModel.getRecordYear();
    }

    @FXML
    private void scanImage(ActionEvent event) {
        try {
            DatabaseAccess.insertImage("internalexports", " REGISNO ='" + regisNO.getText() + "' AND RECORDYEAR ='" + recordYear + "'");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void addNames(ActionEvent event) {
         App.lodAddNmaesPage(regisNO.getText(), recordYear, "internal");
    }

    @FXML
    private void showImage(ActionEvent event) {
        byte[] pdfimage = DatabaseAccess.getInternalExportPdfFile( regisNO.getText(), recordYear);
        ShowPdf.writePdf(pdfimage);
    }

    @FXML
    private void click(MouseEvent event) {
        mylistener.onClickListener(internalExportsModel);
    }

    @FXML
    private void printBarcod(ActionEvent event) {
        try {
            Connection con = DatabaseConniction.dbConnector();
            JasperDesign recipientReport = JRXmlLoader.load(config.getAppURL() + "\\reports\\ExportingBarcod.jrxml");
            ResultSet rs = DatabaseAccess.select("internalexports", "REGISNO = '" + regisNO.getText() + "'");
            String regisNo = null;
            String recipientDate = null;
            String circularDir = null;
            int quRegisNo = 0;
            String unitName = null;
            String saveFile = null;
            while (rs.next()) {
                regisNo = ArabicSetting.EnglishToarabic(Integer.toString(rs.getInt("REGISNO")));
                recipientDate = ArabicSetting.EnglishToarabic(rs.getString("EXPORTDATE")) + "هـ";
                circularDir = rs.getString("DESTINATION");
                quRegisNo = rs.getInt("REGISNO");
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
