package controllers;

import Validation.FormValidation;
import static controllers.DatabaseAccess.config;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class ClashahPageController implements Initializable {

    @FXML
    private HBox content;
    @FXML
    private TextField clashahName;
    @FXML
    private TextField clashahText;
    @FXML
    private ComboBox<?> savedClashah;
    @FXML
    private ComboBox<String> destination;
    @FXML
    private ComboBox<String> clashahList;
    @FXML
    private TextField newText;
    @FXML
    private TextField capy1;
    @FXML
    private TextField capy2;
    @FXML
    private TextField capy3;
    ObservableList<String> newclashah = FXCollections.observableArrayList();
    ObservableList<String> clashahlist = FXCollections.observableArrayList();
    ObservableList<String> placenameslist = FXCollections.observableArrayList();
    String circularID = null;
    String year = null;
    String tablename = null;
    String clashahtext = null;
    String saveFile = null;
    boolean capy1state = true;
    boolean capy2state = true;
    boolean capy3state = true;
    boolean scretstate = true;
    @FXML
    private TextField scretText;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    private ObservableList filleClashah(ObservableList list) {
        try {
            ResultSet rs = DatabaseAccess.select("clashah");
            try {
                while (rs.next()) {
                    list.add(rs.getString("CLASHAHNAME"));
                }
                rs.close();
            } catch (SQLException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        list.add("آخرى");
        return list;
    }

    private ObservableList fillePlaceNames(ObservableList list) {
        try {
            ResultSet rs = DatabaseAccess.select("placenames", "UINTTYPE = 'كليشة'");
            try {
                while (rs.next()) {
                    list.add(rs.getString("PLACENAME"));

                }
                rs.close();
            } catch (SQLException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return list;
    }

    private String getClashahId() {
        String id = null;
        try {
            ResultSet rs = DatabaseAccess.select("clashah", "CLASHAHNAME = '" + savedClashah.getValue() + "'");
            try {
                while (rs.next()) {
                    id = rs.getString("ID");
                }
                rs.close();
            } catch (SQLException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return id;
    }

    @FXML
    private void save(ActionEvent event) {
        String tableName = "clashah";
        String[] data = {clashahName.getText(), clashahText.getText()};
        String fieldName = "`CLASHAHNAME`, `CLASHAHTEXT`";
        String valuenumbers = "?,?";

        boolean clashahNameState = FormValidation.textFieldNotEmpty(clashahName, "الرجاء ادخال مسمى الكليشة");
        boolean clashahTextState = FormValidation.textFieldNotEmpty(clashahText, "الرجاء ادخال نص الكليشة");
        boolean clashahNameExisting = FormValidation.ifexisting("clashah", "CLASHAHNAME", "CLASHAHNAME = '" + clashahName.getText() + "'", "يوجد كليشة بنفس الاسم");

        if (clashahTextState && clashahNameState && clashahNameExisting) {
            try {
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, data);
                newclashah.clear();
                clashahlist.clear();
                savedClashah.setItems(filleClashah(newclashah));
                clashahList.setItems(filleClashah(clashahlist));
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void update(ActionEvent event) {
        String tableName = "clashah";
        String[] data = {clashahName.getText(), clashahText.getText()};
        String fieldName = "`CLASHAHNAME`=?, `CLASHAHTEXT`=?";

        boolean clashahListState = FormValidation.comboBoxNotEmpty(savedClashah, "الرجاء ادخال الموضوع");

        if (clashahListState) {
            try {
                DatabaseAccess.updat(tableName, fieldName, data, "ID = '" + getClashahId() + "'");
                newclashah.clear();
                clashahlist.clear();
                savedClashah.setItems(filleClashah(newclashah));
                clashahList.setItems(filleClashah(clashahlist));
            } catch (IOException | SQLException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void delete(ActionEvent event) {
        try {
            DatabaseAccess.delete("clashah", "ID = '" + getClashahId() + "'");
            newclashah.clear();
            clashahlist.clear();
            savedClashah.setItems(filleClashah(newclashah));
            clashahList.setItems(filleClashah(clashahlist));
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clear(ActionEvent event) {
    }

    @FXML
    private void capy1activation(ActionEvent event) {
        if (capy1state) {
            capy1.setDisable(false);
            capy2.setDisable(true);
            capy3.setDisable(true);
            capy1state = false;
        } else {
            capy1.setDisable(true);
            capy1state = true;
        }
    }

    @FXML
    private void capy2activation(ActionEvent event) {
        if (capy2state) {
            capy1.setDisable(false);
            capy2.setDisable(false);
            capy3.setDisable(true);
            capy1state = false;
            capy2state = false;
        } else {
            capy1.setDisable(true);
            capy2.setDisable(true);
            capy1state = true;
            capy2state = true;
        }
    }

    @FXML
    private void capy3activation(ActionEvent event) {
        if (capy3state) {
            capy1.setDisable(false);
            capy2.setDisable(false);
            capy3.setDisable(false);
            capy1state = false;
            capy2state = false;
            capy3state = false;
        } else {
            capy1.setDisable(true);
            capy2.setDisable(true);
            capy3.setDisable(true);
            capy1state = true;
            capy2state = true;
            capy3state = true;
        }
    }

    @FXML
    private void printClashah(ActionEvent event) {
        if (capy1state && capy2state && capy3state) {
            try {
                Connection con = DatabaseConniction.dbConnector();
                String reportSrcFile = config.getAppURL() + "\\reports\\clashahReport.jrxml";
                JasperDesign jasperReport = JRXmlLoader.load(reportSrcFile);

                Map<String, Object> parameters = new HashMap<String, Object>();
                if (tablename == "InternalIncoming") {
                    byte[] pdfimage = DatabaseAccess.getInternalIncomingPdfFile(circularID, year);
                    ShowPdf.getClashahImage(pdfimage);
                } else {
                    byte[] pdfimage = DatabaseAccess.getExternalincomingPdfFile(circularID, year);
                    ShowPdf.getClashahImage(pdfimage);
                }

                String imagePath = config.getAppURL() + "\\pdfFiles\\showPdf.png";

                parameters.put("imagePath", imagePath);
                parameters.put("destination", destination.getValue());
                parameters.put("clashahtext", getCopyText(getClashahText()));
                 parameters.put("scretText", getScretText());
                parameters.put("saveFile", ArabicSetting.EnglishToarabic(saveFile));
                parameters.put("year", ArabicSetting.EnglishToarabic(year));

                JasperReport jr = JasperCompileManager.compileReport(jasperReport);
                JasperPrint jp = JasperFillManager.fillReport(jr, parameters, con);
                JasperViewer.viewReport(jp, false);

            } catch (IOException | JRException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        } else if (!capy1state && capy2state && capy3state) {
            try {
                Connection con = DatabaseConniction.dbConnector();
                String reportSrcFile = config.getAppURL() + "\\reports\\‏‏clashahReportCopy1.jrxml";
                JasperDesign jasperReport = JRXmlLoader.load(reportSrcFile);

                Map<String, Object> parameters = new HashMap<String, Object>();
                if (tablename == "InternalIncoming") {
                    byte[] pdfimage = DatabaseAccess.getInternalIncomingPdfFile(circularID, year);
                    ShowPdf.getClashahImage(pdfimage);
                } else if (tablename == "ExternalIncoming") {
                    byte[] pdfimage = DatabaseAccess.getExternalincomingPdfFile(circularID, year);
                    ShowPdf.getClashahImage(pdfimage);
                }

                String imagePath = config.getAppURL() + "\\pdfFiles\\showPdf.png";
                System.out.println(getCopyText(getClashahText()));
                parameters.put("imagePath", imagePath);
                parameters.put("destination", destination.getValue());
                parameters.put("clashahtext", getCopyText(getClashahText()));
                parameters.put("copyText1", getCopyText(capy1.getText()));
                 parameters.put("scretText", getScretText());
                parameters.put("saveFile", ArabicSetting.EnglishToarabic(saveFile));
                parameters.put("year", ArabicSetting.EnglishToarabic(year));

                JasperReport jr = JasperCompileManager.compileReport(jasperReport);
                JasperPrint jp = JasperFillManager.fillReport(jr, parameters, con);
                JasperViewer.viewReport(jp, false);

            } catch (IOException | JRException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        } else if (!capy1state && !capy2state && capy3state) {
            try {
                Connection con = DatabaseConniction.dbConnector();
                String reportSrcFile = config.getAppURL() + "\\reports\\‏‏clashahReportCopy2.jrxml";
                JasperDesign jasperReport = JRXmlLoader.load(reportSrcFile);

                Map<String, Object> parameters = new HashMap<String, Object>();
                if (tablename == "InternalIncoming") {
                    byte[] pdfimage = DatabaseAccess.getInternalIncomingPdfFile(circularID, year);
                    ShowPdf.getClashahImage(pdfimage);
                } else {
                    byte[] pdfimage = DatabaseAccess.getExternalincomingPdfFile(circularID, year);
                    ShowPdf.getClashahImage(pdfimage);
                }

                String imagePath = config.getAppURL() + "\\pdfFiles\\showPdf.png";

                parameters.put("imagePath", imagePath);
                parameters.put("destination", destination.getValue());
                parameters.put("clashahtext", getCopyText(getClashahText()));
                parameters.put("copyText1", getCopyText(capy1.getText()));
                parameters.put("copyText2", getCopyText(capy2.getText()));
                parameters.put("scretText", getScretText());
                parameters.put("saveFile", ArabicSetting.EnglishToarabic(saveFile));
                parameters.put("year", ArabicSetting.EnglishToarabic(year));

                JasperReport jr = JasperCompileManager.compileReport(jasperReport);
                JasperPrint jp = JasperFillManager.fillReport(jr, parameters, con);
                JasperViewer.viewReport(jp, false);

            } catch (IOException | JRException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        } else if (!capy1state && !capy2state && !capy3state) {
            try {
                Connection con = DatabaseConniction.dbConnector();
                String reportSrcFile = config.getAppURL() + "\\reports\\‏‏clashahReportCopy3.jrxml";
                JasperDesign jasperReport = JRXmlLoader.load(reportSrcFile);

                Map<String, Object> parameters = new HashMap<String, Object>();
                if (tablename == "InternalIncoming") {
                    byte[] pdfimage = DatabaseAccess.getInternalIncomingPdfFile(circularID, year);
                    ShowPdf.getClashahImage(pdfimage);
                } else {
                    byte[] pdfimage = DatabaseAccess.getExternalincomingPdfFile(circularID, year);
                    ShowPdf.getClashahImage(pdfimage);
                }

                String imagePath = config.getAppURL() + "\\pdfFiles\\showPdf.png";

                parameters.put("imagePath", imagePath);
                parameters.put("destination", destination.getValue());
                parameters.put("clashahtext", getCopyText(getClashahText()));
                parameters.put("copyText1", getCopyText(capy1.getText()));
                parameters.put("copyText2", getCopyText(capy2.getText()));
                parameters.put("copyText3", getCopyText(capy3.getText()));
                 parameters.put("scretText", getScretText());
                parameters.put("saveFile", ArabicSetting.EnglishToarabic(saveFile));
                parameters.put("year", ArabicSetting.EnglishToarabic(year));

                JasperReport jr = JasperCompileManager.compileReport(jasperReport);
                JasperPrint jp = JasperFillManager.fillReport(jr, parameters, con);
                JasperViewer.viewReport(jp, false);

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

    @FXML
    private void getInstructions(ActionEvent event) {
        try {
            Connection con = DatabaseConniction.dbConnector();
            String reportSrcFile = config.getAppURL() + "\\reports\\insturctionsReport.jrxml";
            JasperDesign jasperReport = JRXmlLoader.load(reportSrcFile);

            Map<String, Object> parameters = new HashMap<String, Object>();
            if (tablename == "InternalIncoming") {
                byte[] pdfimage = DatabaseAccess.getInternalIncomingPdfFile(circularID, year);
                ShowPdf.getClashahImage(pdfimage);
            } else {
                byte[] pdfimage = DatabaseAccess.getExternalincomingPdfFile(circularID, year);
                ShowPdf.getClashahImage(pdfimage);
            }

            String imagePath = config.getAppURL() + "\\pdfFiles\\showPdf.png";

            parameters.put("imagePath", imagePath);

            JasperReport jr = JasperCompileManager.compileReport(jasperReport);
            JasperPrint jp = JasperFillManager.fillReport(jr, parameters, con);
            JasperViewer.viewReport(jp, false);

        } catch (IOException | JRException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clearNewClashah(ActionEvent event) {
        clashahName.setText(null);
        clashahText.setText(null);
        savedClashah.setValue(null);
    }

    public void setPassingData(String circularID, String year, String tablename, String saveFile) {
        this.circularID = circularID;
        this.year = year;
        this.tablename = tablename;
        this.saveFile = saveFile;
        savedClashah.setItems(filleClashah(newclashah));
        clashahList.setItems(filleClashah(clashahlist));
        destination.setItems(fillePlaceNames(placenameslist));
    }

    @FXML
    private void setChosenData(ActionEvent event) {
        try {
            ResultSet rs = DatabaseAccess.select("clashah", "CLASHAHNAME = '" + savedClashah.getValue() + "'");

            if (rs.next()) {
                clashahName.setText(rs.getString("CLASHAHNAME"));
                clashahText.setText(rs.getString("CLASHAHTEXT"));
            }
            rs.close();
        } catch (SQLException | IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void getNewText(ActionEvent event) {
        if (clashahList.getValue() == "آخرى") {
            newText.setDisable(false);
        } else {
            newText.setDisable(true);
            clashahList.getValue();
        }
    }

    private String getClashahText() {
        if (clashahList.getValue() == "آخرى") {
            clashahtext = newText.getText();
        } else {
            try {
                newText.setDisable(true);
                ResultSet rs = DatabaseAccess.select("clashah", "CLASHAHNAME = '" + clashahList.getValue() + "'");
                if (rs.next()) {
                    clashahtext = rs.getString("CLASHAHTEXT");
                }
            } catch (IOException | SQLException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
        return clashahtext;
    }

    private String getCopyText(String text) {
        return ArabicSetting.EnglishToarabic(text);
    }
    private String getScretText() {
        String text = null;
        if (scretstate) {
             text = "";
        }else{
        text = scretText.getText();
        }
        return text;
    }

    @FXML
    private void scertactivation(ActionEvent event) {
        if (scretstate) {
            scretText.setDisable(false);
            scretstate= false;
        } else {
            scretText.setDisable(true);
            scretstate= true;
        }
    }

}
