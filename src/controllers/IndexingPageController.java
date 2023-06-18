package controllers;

import Validation.FormValidation;
import static controllers.DatabaseAccess.config;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import modeles.IndexingModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class IndexingPageController implements Initializable {

    @FXML
    private HBox content;
    @FXML
    private TextField fileNumber;
    @FXML
    private ComboBox<?> startDateDay;
    @FXML
    private ComboBox<?> startDateMonth;
    @FXML
    private ComboBox<?> startDateYear;
    @FXML
    private ComboBox<?> endDateDay;
    @FXML
    private ComboBox<?> endDateMonth;
    @FXML
    private ComboBox<?> endDateYear;
    String filenumber = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AppDate.setDateValue(startDateDay, startDateMonth, startDateYear);
        AppDate.setCurrentDate(startDateDay, startDateMonth, startDateYear);
        AppDate.setDateValue(endDateDay, endDateMonth, endDateYear);
        AppDate.setCurrentDate(endDateDay, endDateMonth, endDateYear);
    }

    @FXML
    private void update(ActionEvent event) {
        fileNumber.setText("");
        AppDate.setCurrentDate(startDateDay, startDateMonth, startDateYear);
        AppDate.setCurrentDate(endDateDay, endDateMonth, endDateYear);
    }

    @FXML
    private void printIndex(ActionEvent event) {
        boolean fileNumberState = FormValidation.textFieldNotEmpty(fileNumber, "الرجاء ادخال رقم الملف");
        if (fileNumberState) {
            try {
                InputStream input = new FileInputStream(new File(config.getAppURL() + "\\reports\\printIndexing.jrxml"));

                List<IndexingModel> dataItems = new ArrayList<>();
                dataItems = getData();
                List<IndexingModel> finaldataItems = new ArrayList<>();
                IndexingModel indexingMode;
                
                int squnce = 0;
                while (!dataItems.isEmpty()) {
                    squnce++;
                    indexingMode = new IndexingModel();
                    indexingMode.setCircularid(dataItems.get(0).getCircularid());
                    indexingMode.setCirculardate(dataItems.get(0).getCirculardate());
                    indexingMode.setDestination(dataItems.get(0).getDestination());
                    indexingMode.setTopic(dataItems.get(0).getTopic());
                    indexingMode.setSqunce(ArabicSetting.EnglishToarabic(Integer.toString(squnce)));
                    finaldataItems.add(indexingMode);
                }

                JRBeanCollectionDataSource itemsJarbean = new JRBeanCollectionDataSource(dataItems);

                Connection con = DatabaseConniction.dbConnector();
                Map parameters = new HashMap();

                parameters.put("repotCollation", itemsJarbean);
                parameters.put("saveFile", ArabicSetting.EnglishToarabic(fileNumber.getText()));

                JasperDesign jasperDesign = JRXmlLoader.load(input);
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, con);
                JasperViewer.viewReport(jasperPrint, false);

            } catch (IOException | JRException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void close(ActionEvent event) {
        Stage stage = (Stage) content.getScene().getWindow();
        stage.close();
    }

    public String getStartDateDate() {
        return AppDate.getDate(startDateDay, startDateMonth, startDateYear);
    }

    public String getEndDate() {
        return AppDate.getDate(endDateDay, endDateMonth, endDateYear);
    }

    private List<IndexingModel> getData() throws IOException {
        List<IndexingModel> indexingModellist = new ArrayList<>();
        IndexingModel indexingMode;
        ResultSet rs1 = DatabaseAccess.getData("SELECT EXPORTNUM, EXPORTDATE,TOPIC,DESTINATION, SAVEFILE FROM exportsdata WHERE SAVEFILE = '" + fileNumber.getText() + "'  "
                + "AND EXPORTDATE>= '" + getStartDateDate() + "' AND EXPORTDATE<= '" + getEndDate() + "' ORDER BY EXPORTDATE ASC ");
        ResultSet rs2 = DatabaseAccess.getData("SELECT RECEIPTNUMBER,RECEIPTDATE,DESTINATION,TOPIC,SAVEFILE FROM externalincoming WHERE SAVEFILE = '" + fileNumber.getText() + "'  "
                + "AND RECEIPTDATE>= '" + getStartDateDate() + "' AND RECEIPTDATE<='" + getEndDate() + "' ORDER BY RECEIPTDATE ASC ");
        ResultSet rs3 = DatabaseAccess.getData("SELECT REGISNO,EXPORTDATE,DESTINATION,TOPIC, SAVEFILE FROM internalexports  WHERE SAVEFILE = '" + fileNumber.getText() + "'  "
                + "AND EXPORTDATE>= '" + getStartDateDate() + "' AND EXPORTDATE <='" + getEndDate() + "' ORDER BY EXPORTDATE ASC ");
        ResultSet rs4 = DatabaseAccess.getData("SELECT REGIS_NO,RECIPIENT_DATE,CIRCULAR_DIR,TOPIC,SAVE_FILE FROM internalincoming  WHERE SAVE_FILE ='" + fileNumber.getText() + "'  "
                + "AND RECIPIENT_DATE>= '" + getStartDateDate() + "' AND RECIPIENT_DATE <='" + getEndDate() + "' ORDER BY RECIPIENT_DATE ASC ");
        ResultSet rs5 = DatabaseAccess.getData("SELECT RECEIPTNUMBER,RECEIPTDATE,DESTINATION,TOPIC,SAVEFILE FROM secretdata  WHERE SAVEFILE ='" + fileNumber.getText() + "'  "
                + "AND RECEIPTDATE>= '" + getStartDateDate() + "' AND RECEIPTDATE <='" + getEndDate() + "' ORDER BY RECEIPTDATE ASC ");

        try {
            int squnce = 0;
            while (rs1.next()) {
                squnce++;
                indexingMode = new IndexingModel();
                indexingMode.setCircularid(ArabicSetting.EnglishToarabic(rs1.getString("EXPORTNUM")));
                indexingMode.setCirculardate(ArabicSetting.EnglishToarabic(rs1.getString("EXPORTDATE")) + "هـ");
                indexingMode.setDestination(rs1.getString("DESTINATION"));
                indexingMode.setTopic(rs1.getString("TOPIC"));
//                indexingMode.setSqunce(ArabicSetting.EnglishToarabic(Integer.toString(squnce)));
                indexingModellist.add(indexingMode);
            }
            while (rs2.next()) {
                squnce++;
                indexingMode = new IndexingModel();
                indexingMode.setCircularid(ArabicSetting.EnglishToarabic(rs2.getString("RECEIPTNUMBER")));
                indexingMode.setCirculardate(ArabicSetting.EnglishToarabic(rs2.getString("RECEIPTDATE")) + "هـ");
                indexingMode.setDestination(rs2.getString("DESTINATION"));
                indexingMode.setTopic(rs2.getString("TOPIC"));
//                indexingMode.setSqunce(ArabicSetting.EnglishToarabic(Integer.toString(squnce)));
                indexingModellist.add(indexingMode);
            }
            while (rs3.next()) {
                squnce++;
                indexingMode = new IndexingModel();
                indexingMode.setCircularid(ArabicSetting.EnglishToarabic(rs3.getString("REGISNO")));
                indexingMode.setCirculardate(ArabicSetting.EnglishToarabic(rs3.getString("EXPORTDATE")) + "هـ");
                indexingMode.setDestination(rs3.getString("DESTINATION"));
                indexingMode.setTopic(rs3.getString("TOPIC"));
                indexingMode.setSqunce(ArabicSetting.EnglishToarabic(Integer.toString(squnce)));
                indexingModellist.add(indexingMode);
            }
            while (rs4.next()) {
                squnce++;
                indexingMode = new IndexingModel();
                indexingMode.setCircularid(ArabicSetting.EnglishToarabic(rs4.getString("REGIS_NO")));
                indexingMode.setCirculardate(ArabicSetting.EnglishToarabic(rs4.getString("RECIPIENT_DATE")) + "هـ");
                indexingMode.setDestination(rs4.getString("CIRCULAR_DIR"));
                indexingMode.setTopic(rs4.getString("TOPIC"));
//                indexingMode.setSqunce(ArabicSetting.EnglishToarabic(Integer.toString(squnce)));
                indexingModellist.add(indexingMode);
            }
            while (rs5.next()) {
                squnce++;
                indexingMode = new IndexingModel();
                indexingMode.setCircularid(ArabicSetting.EnglishToarabic(rs5.getString("RECEIPTNUMBER")));
                indexingMode.setCirculardate(ArabicSetting.EnglishToarabic(rs5.getString("RECEIPTDATE")) + "هـ");
                indexingMode.setDestination(rs5.getString("DESTINATION"));
                indexingMode.setTopic(rs5.getString("TOPIC"));
//                indexingMode.setSqunce(ArabicSetting.EnglishToarabic(Integer.toString(squnce)));
                indexingModellist.add(indexingMode);
            }
            Collections.sort(indexingModellist, new SortByDate());

            rs1.close();
            rs2.close();
            rs3.close();
            rs4.close();
            rs5.close();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return indexingModellist;
    }
}
