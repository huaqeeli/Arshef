package controllers;

import Serveces.InternalExportsPageListener;
import Validation.FormValidation;
import static Validation.FormValidation.showAlert;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import modeles.InternalExportsModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.progress.RingProgressIndicator;

public class InternalExportsPageController implements Initializable {

    @FXML
    private ComboBox<String> searchType;
    @FXML
    private ComboBox<String> year;
    @FXML
    private TextField searchText;
    @FXML
    private ComboBox<String> destination;
    @FXML
    private TextField topic;
    @FXML
    private TextField saveFaile;
    @FXML
    private TextField notes;
    @FXML
    private TextField imageUrl;
    @FXML
    private ComboBox<?> exportsDay;
    @FXML
    private ComboBox<?> exportsMonth;
    @FXML
    private ComboBox<?> exportsYear;
    @FXML
    private TextField incomingNum;

    File imagefile = null;
    Stage stage = new Stage();
    byte[] pdfimage = null;
    String recordYear = null;
    private String registrationId = null;
    ObservableList<String> destinationlist = FXCollections.observableArrayList();
    ObservableList<InternalExportsModel> exportsList = FXCollections.observableArrayList();
    ObservableList<String> searchTypelist = FXCollections.observableArrayList("البحث برقم الصادر", "البحث بتاريخ الصادر", "البحث بالموضوع", "البحث بجهة الصادر", "البحث برقم الملف", "البحث بالرقم العسكري", "عرض الكل");
    ObservableList<String> incomingTypelist = FXCollections.observableArrayList("صادر جديد", "صادر معاملة واردة خارجية", "صادر معاملة واردة داخلية");
    Config config = new Config();
    @FXML
    private ComboBox<?> searchDateDay;
    @FXML
    private ComboBox<?> searchDateMonth;
    @FXML
    private ComboBox<?> searchDateYear;
    public final List<InternalExportsModel> internalExportsObject = new ArrayList<>();
    private InternalExportsPageListener mylistener;
    @FXML
    private VBox vbox;
    ActionEvent event;
    @FXML
    private StackPane stackPane;
    @FXML
    private ComboBox<String> incomingType;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            refreshData();
            AppDate.setDateValue(exportsDay, exportsMonth, exportsYear);
            AppDate.setCurrentDate(exportsDay, exportsMonth, exportsYear);
            AppDate.setDateValue(searchDateDay, searchDateMonth, searchDateYear);
            AppDate.setCurrentDate(searchDateDay, searchDateMonth, searchDateYear);
            FillComboBox.fillComboBox(searchTypelist, searchType);
            FillComboBox.fillComboBox(incomingTypelist, incomingType);
            destination.setItems(filleDestination(destinationlist));
            AppDate.setYearValue(year);
            year.setValue(Integer.toString(HijriCalendar.getSimpleYear()));
            clear(event);
        } catch (SQLException ex) {
            Logger.getLogger(InternalExportsPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private ObservableList filleDestination(ObservableList list) {
        try {
            ResultSet rs = DatabaseAccess.select("placenames", "UINTTYPE='داخلي'");
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

    private void refreshData() throws SQLException {
        try {
            internalExportsObject.clear();
            vbox.getChildren().clear();
            viewdata(DatabaseAccess.getData("SELECT REGISNO,EXPORTDATE,DESTINATION,TOPIC,SAVEFILE,NOTES FROM internalexports WHERE EXPORTDATE ='" + HijriCalendar.getSimpleDate() + "' ORDER BY REGISNO DESC"));
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    private List<InternalExportsModel> getData(ResultSet rs) {
        List<InternalExportsModel> internalExportsModels = new ArrayList<>();
        InternalExportsModel internalExportsModel;
        try {
            while (rs.next()) {

                internalExportsModel = new InternalExportsModel();
                internalExportsModel.setRegisNO(rs.getString("REGISNO"));
                internalExportsModel.setExportsDate(rs.getString("EXPORTDATE"));
                internalExportsModel.setDestination(rs.getString("DESTINATION"));
                internalExportsModel.setTopic(rs.getString("TOPIC"));
                internalExportsModel.setSaveFile(rs.getString("SAVEFILE"));
                internalExportsModel.setNotes(rs.getString("NOTES"));
                internalExportsModel.setRecordYear(AppDate.getYear(rs.getString("EXPORTDATE")));
                internalExportsModels.add(internalExportsModel);
            }
            rs.close();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return internalExportsModels;

    }

    private void setChosendata(InternalExportsModel internalExportsModel) {
        setRegistrationId(internalExportsModel.getRegisNO());
        setExportsDate(internalExportsModel.getExportsDate());
        setDestination(internalExportsModel.getDestination());
        setTopic(internalExportsModel.getTopic());
        setSaveFaile(internalExportsModel.getSaveFile());
        setNotes(internalExportsModel.getNotes());
        registrationId = internalExportsModel.getRegisNO();
        recordYear = AppDate.getYear(internalExportsModel.getExportsDate());
    }

    private void viewdata(ResultSet rs) throws SQLException {
        internalExportsObject.addAll(getData(rs));
        if (internalExportsObject.size() > 0) {
            setChosendata(internalExportsObject.get(0));
            mylistener = new InternalExportsPageListener() {
                @Override
                public void onClickListener(InternalExportsModel internalExportsModel) {
                    setChosendata(internalExportsModel);
                }
            };
        }

        try {
            for (InternalExportsModel InternalExportsModel : internalExportsObject) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/view/InternalExportsItem.fxml"));
                AnchorPane pane = fxmlLoader.load();
                InternalExportsItemController internalExportsItemController = fxmlLoader.getController();
                internalExportsItemController.setData(InternalExportsModel, mylistener);
                vbox.getChildren().add(pane);
            }
            rs.close();
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private File getImageUrle(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter ext = new FileChooser.ExtensionFilter("PDF  files(*.pdf)", "*.PDF");
        fileChooser.getExtensionFilters().addAll(ext);
        imagefile = fileChooser.showOpenDialog(stage);
        setImageUrl(imagefile.getPath());
        return imagefile;
    }
//("صادر جديد", "صادر معاملة واردة خارجية", "صادر معاملة واردة داخلية")

    @FXML
    private void insertData(ActionEvent event) throws IOException, SQLException {
        String regisNo = null;
        String exportType = incomingType.getValue();
        switch (exportType) {
            case "صادر جديد":
                regisNo = DatabaseAccess.getRegistrationNum();
                break;
            case "صادر معاملة واردة خارجية":
                if (getIncomingNum() != null || !"".equals(getIncomingNum())) {
                    regisNo = getIncomingNum();
                } else {
                    FormValidation.showAlert(null, "ادخل رقم الوراد الخارجي", Alert.AlertType.ERROR);
                }
                break;
            case "صادر معاملة واردة داخلية":
                if (getIncomingNum() != null || !"".equals(getIncomingNum())) {
                    regisNo = getIncomingNum();
                } else {
                    FormValidation.showAlert(null, "ادخل رقم الوراد الداخلي", Alert.AlertType.ERROR);
                }
                break;
        }
        String tableName = "internalexports";
        recordYear = Integer.toString(HijriCalendar.getSimpleYear());
        String fieldName = null;
        String[] data = {regisNo, getExportsDate(), getDestination(), getTopic(), getSaveFaile(), getNotes(), recordYear};
        String valuenumbers = null;
        if (imagefile != null) {
            fieldName = "`REGISNO`,`EXPORTDATE`,`DESTINATION`,`TOPIC`,`SAVEFILE`,`NOTES`,`RECORDYEAR`,`IMAGE`";
            valuenumbers = "?,?,?,?,?,?,?,?";
        } else {
            fieldName = "`REGISNO`,`EXPORTDATE`,`DESTINATION`,`TOPIC`,`SAVEFILE`,`NOTES`,`RECORDYEAR`";
            valuenumbers = "?,?,?,?,?,?,?";
        }

        boolean topicState = FormValidation.textFieldNotEmpty(topic, "الرجاء ادخال الموضوع");
        boolean destinationState = FormValidation.comboBoxNotEmpty(destination, "الرجاء ادخال جهة الصادر");
        boolean incomingTypeState = FormValidation.comboBoxNotEmpty(incomingType, "اختر نوع الصادر");

        if (topicState && destinationState && incomingTypeState) {
            try {
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, data, imagefile);
                registrationId = DatabaseAccess.getRegistrationNum();
                DatabaseAccess.updatRegistrationNum();
                refreshData();
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void updateData(ActionEvent event) throws IOException, SQLException {
        String tableName = "internalexports";
        String fieldName = null;
        String[] data = {getExportsDate(), getDestination(), getTopic(), getSaveFaile(), getNotes()};
        if (imagefile != null) {
            fieldName = "`EXPORTDATE`=?,`DESTINATION`=?,`TOPIC`=?,`SAVEFILE`=?,`NOTES`=?,`IMAGE`=?";
        } else {
            fieldName = "`EXPORTDATE`=?,`DESTINATION`=?,`TOPIC`=?,`SAVEFILE`=?,`NOTES`=?";
        }

        boolean topicState = FormValidation.textFieldNotEmpty(topic, "الرجاء ادخال جهة الموضوع");

        if (topicState) {
            try {
                int t = DatabaseAccess.updat(tableName, fieldName, data, "`REGISNO` = '" + registrationId + "' AND `RECORDYEAR` = '" + recordYear + "'", imagefile);
                if (t > 0) {
                    FormValidation.showAlert("", "تم تحديث البيانات", Alert.AlertType.CONFIRMATION);
                }
                refreshData();
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    private void printBarcode(ActionEvent event) throws JRException {
        try {
            if (registrationId != null) {
                Connection con = DatabaseConniction.dbConnector();
                JasperDesign recipientReport = JRXmlLoader.load(config.getAppURL() + "\\reports\\ExportingBarcod.jrxml");
                ResultSet rs = DatabaseAccess.select("internalexports", "REGISNO = '" + registrationId + "'");
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
            } else {
                showAlert("", "اختر السجل من الجدول");
            }

        } catch (IOException | SQLException | JRException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clear(ActionEvent event) {
        setIncomingNum(null);
        setTopic(null);
        setDestination(null);
        setNotes(null);
        setImageUrl(null);
        setSaveFaile(null);
    }

    private void addtoLeaderDisplay(ActionEvent event) {
        String tableName = "displaydata";
        String[] data = {HijriCalendar.getSimpleDate(), "عرض القائد", topic.getText(), destination.getValue()};
        String fieldName = "`DISPLAYDATE`,`DISPLAYTYPE`,`TOPIC`,`DESTINATION`";
        String valuenumbers = "?,?,?,?";

        boolean idState = FormValidation.notNull(registrationId, "الرجاءاختر السجل من الجدول");

        if (idState) {
            try {
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, data);
                FormValidation.showAlert(null, "تم اضافة المعاملة الى عرض القائد", Alert.AlertType.INFORMATION);
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    private void addtoLeaderSignature(ActionEvent event) {
        String tableName = "displaydata";
        String[] data = {HijriCalendar.getSimpleDate(), "توقيع القائد", topic.getText(), destination.getValue()};
        String fieldName = "`DISPLAYDATE`,`DISPLAYTYPE`,`TOPIC`,`DESTINATION`";
        String valuenumbers = "?,?,?,?";

        boolean idState = FormValidation.notNull(registrationId, "الرجاءاختر السجل من الجدول");

        if (idState) {
            try {
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, data);
                FormValidation.showAlert(null, "تم اضافة المعاملة الى توقيع القائد", Alert.AlertType.INFORMATION);
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    private void addtoManagerSignature(ActionEvent event) {
        String tableName = "displaydata";
        String[] data = {HijriCalendar.getSimpleDate(), "توقيع الركن", topic.getText(), destination.getValue()};
        String fieldName = "`DISPLAYDATE`,`DISPLAYTYPE`,`TOPIC`,`DESTINATION`";
        String valuenumbers = "?,?,?,?";

        boolean idState = FormValidation.notNull(registrationId, "الرجاءاختر السجل من الجدول");

        if (idState) {
            try {
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, data);
                FormValidation.showAlert(null, "تم اضافة المعاملة الى توقيع الركن", Alert.AlertType.INFORMATION);
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    private void addtoManagerDisplay(ActionEvent event) {
        String tableName = "displaydata";
        String[] data = {HijriCalendar.getSimpleDate(), "عرض الركن", topic.getText(), destination.getValue()};
        String fieldName = "`DISPLAYDATE`,`DISPLAYTYPE`,`TOPIC`,`DESTINATION`";
        String valuenumbers = "?,?,?,?";

        boolean idState = FormValidation.notNull(registrationId, "الرجاءاختر السجل من الجدول");

        if (idState) {
            try {
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, data);
                FormValidation.showAlert(null, "تم اضافة المعاملة الى عرض الركن", Alert.AlertType.INFORMATION);
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    private void addtoManagerSmallSignature(ActionEvent event) {
        String tableName = "displaydata";
        String[] data = {HijriCalendar.getSimpleDate(), "تاشير الركن", topic.getText(), destination.getValue()};
        String fieldName = "`DISPLAYDATE`,`DISPLAYTYPE`,`TOPIC`,`DESTINATION`";
        String valuenumbers = "?,?,?,?";

        boolean idState = FormValidation.notNull(registrationId, "الرجاءاختر السجل من الجدول");

        if (idState) {
            try {
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, data);
                FormValidation.showAlert(null, "تم اضافة المعاملة الى تاشير الركن", Alert.AlertType.INFORMATION);
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    private void addtoManagerOrders(ActionEvent event) {
        String tableName = "displaydata";
        String[] data = {HijriCalendar.getSimpleDate(), "توجيه الركن", topic.getText(), destination.getValue()};
        String fieldName = "`DISPLAYDATE`,`DISPLAYTYPE`,`TOPIC`,`DESTINATION`";
        String valuenumbers = "?,?,?,?";

        boolean idState = FormValidation.notNull(registrationId, "الرجاءاختر السجل من الجدول");

        if (idState) {
            try {
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, data);
                FormValidation.showAlert(null, "تم اضافة المعاملة الى توجيه الركن", Alert.AlertType.INFORMATION);
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    public String getIncomingNum() {
        return incomingNum.getText();
    }

    public void setIncomingNum(String incomingNum) {
        this.incomingNum.setText(incomingNum);
    }

    public String getTopic() {
        return topic.getText();
    }

    public void setTopic(String topic) {
        this.topic.setText(topic);
    }

    public String getSaveFaile() {
        return saveFaile.getText();
    }

    public void setSaveFaile(String saveFaile) {
        this.saveFaile.setText(saveFaile);
    }

    public String getDestination() {
        return destination.getValue();
    }

    public void setDestination(String destination) {
        this.destination.setValue(destination);
    }

    public String getExportsDate() {
        return AppDate.getDate(exportsDay, exportsMonth, exportsYear);
    }

    public void setExportsDate(String date) {
        AppDate.setSeparateDate(exportsDay, exportsMonth, exportsYear, date);
    }

    public String getSearchText() {
        return searchText.getText();
    }

    public void setSearchText(String searchText) {
        this.searchText.setText(searchText);
    }

    public String getNotes() {
        return notes.getText();
    }

    public void setNotes(String notes) {
        this.notes.setText(notes);
    }

    public String getImageUrl() {
        return imageUrl.getText();
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl.setText(imageUrl);
    }

    public String getRegistrationId() throws IOException, SQLException {
        if (getIncomingNum() == null) {
            registrationId = DatabaseAccess.getRegistrationNum();
        } else {
            registrationId = getIncomingNum();
        }
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getSearchType() {
        return searchType.getValue();
    }

    public void setSearchType(String searchType) {
        this.searchType.setValue(searchType);
    }

    public String getYear() {
        return year.getValue();
    }

    public void setYear(String year) {
        this.year.setValue(year);
    }

    public String getSearchDate() {
        return AppDate.getDate(searchDateDay, searchDateMonth, searchDateYear);
    }

    public void setSearchDate(String date) {
        AppDate.setSeparateDate(searchDateDay, searchDateMonth, searchDateYear, date);
    }

    @FXML
    private void getIncomingData(ActionEvent event) {
       String exportType = incomingType.getValue();
        if (exportType == null || "".equals(exportType)) {
            FormValidation.showAlert(null, "الرجاء اختيار نوع المعاملة", Alert.AlertType.ERROR);
        } else {
            switch (exportType) {
                case "صادر معاملة واردة خارجية":
                    try {
                    ResultSet rs = DatabaseAccess.selectQuiry("SELECT TOPIC,DESTINATION,SAVEFILE FROM externalincoming WHERE RECEIPTNUMBER = '" + getIncomingNum() + "' AND ARSHEFYEAR ='" + getYear() + "'");
                    if (rs.next()) {
                        setTopic(rs.getString("TOPIC"));
                        setDestination(rs.getString("DESTINATION"));
                        setSaveFaile(rs.getString("SAVEFILE"));

                    }
//                    rs.close();
                } catch (IOException | SQLException ex) {
                    FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
                }
                break;
                case "صادر معاملة واردة داخلية":
                    try {
                    ResultSet rs = DatabaseAccess.selectQuiry("SELECT TOPIC,CIRCULAR_DIR,SAVE_FILE FROM internalincoming WHERE REGIS_NO = '" + getIncomingNum() + "' AND RECORD_YEAR ='" + getYear() + "'");
                    if (rs.next()) {
                        setTopic(rs.getString("TOPIC"));
                        setDestination(rs.getString("CIRCULAR_DIR"));
                        setSaveFaile(rs.getString("SAVE_FILE"));
                    }
//                    rs.close();
                    break;
                } catch (IOException | SQLException ex) {
                    FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
                }

            }
        }
    }

    @FXML
    private void searchData(ActionEvent event) throws SQLException {
        String searchValue = getSearchType();
        switch (searchValue) {
            case "عرض الكل":
                internalExportsObject.clear();
                vbox.getChildren().clear();
                RingProgressIndicator rpi = new RingProgressIndicator();
                rpi.setRingWidth(200);
                rpi.makeIndeterminate();
                stackPane.getChildren().add(rpi);
                new GetAllData(rpi).start();
                break;
            case "البحث بجهة الصادر":
                internalExportsObject.clear();
                vbox.getChildren().clear();
                viewdata(getDataByDestination());
                break;
            case "البحث بالموضوع":
                internalExportsObject.clear();
                vbox.getChildren().clear();
                viewdata(getDataByTopic());
                break;
            case "البحث بتاريخ الصادر":
                internalExportsObject.clear();
                vbox.getChildren().clear();
                viewdata(getDataByExportDate());
                break;
            case "البحث برقم الصادر":
                internalExportsObject.clear();
                vbox.getChildren().clear();
                viewdata(getDataByExportNumber());
                break;
            case "البحث برقم الملف":
                internalExportsObject.clear();
                vbox.getChildren().clear();
                viewdata(getDataBySaveFile());
                break;
            case "البحث بالرقم العسكري":
                internalExportsObject.clear();
                vbox.getChildren().clear();
                viewdata(getDataMitaryID());
                break;
        }
    }

    public ResultSet getAllData() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.selectQuiry("SELECT REGISNO,EXPORTDATE,DESTINATION,TOPIC,SAVEFILE,NOTES FROM internalexports WHERE RECORDYEAR = '" + getYear() + "' ORDER BY EXPORTDATE DESC");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public ResultSet getDataByDestination() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.selectQuiry("SELECT REGISNO,EXPORTDATE,DESTINATION,TOPIC,SAVEFILE,NOTES FROM internalexports WHERE DESTINATION LIKE '" + "%" + getSearchText() + "%" + "' AND RECORDYEAR = '" + getYear() + "' ORDER BY EXPORTDATE DESC ");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public ResultSet getDataByTopic() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.selectQuiry("SELECT REGISNO,EXPORTDATE,DESTINATION,TOPIC,SAVEFILE,NOTES FROM internalexports WHERE TOPIC LIKE '" + "%" + getSearchText() + "%" + "' AND RECORDYEAR = '" + getYear() + "' ORDER BY EXPORTDATE DESC");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public ResultSet getDataMitaryID() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.selectQuiry("SELECT internalexports.REGISNO,internalexports.EXPORTDATE,internalexports.DESTINATION,internalexports.TOPIC,internalexports.SAVEFILE,internalexports.NOTES FROM internalexports,circularnames WHERE internalexports.REGISNO = circularnames.CIRCULARID AND circularnames.MILITARYID =  '" + getSearchText() + "' AND RECORDYEAR = '" + getYear() + "' ORDER BY EXPORTDATE DESC");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public ResultSet getDataByExportDate() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.selectQuiry("SELECT REGISNO,EXPORTDATE,DESTINATION,TOPIC,SAVEFILE,NOTES FROM internalexports WHERE EXPORTDATE = '" + getSearchDate() + "'");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public ResultSet getDataByExportNumber() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.selectQuiry("SELECT REGISNO,EXPORTDATE,DESTINATION,TOPIC,SAVEFILE,NOTES FROM internalexports WHERE REGISNO = '" + getSearchText() + "' AND RECORDYEAR = '" + getYear() + "' ORDER BY EXPORTDATE DESC");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public ResultSet getDataBySaveFile() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.selectQuiry("SELECT REGISNO,EXPORTDATE,DESTINATION,TOPIC,SAVEFILE,NOTES FROM internalexports WHERE SAVEFILE = '" + getSearchText() + "' AND RECORDYEAR = '" + getYear() + "' ORDER BY EXPORTDATE DESC");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    @FXML
    private void enableSearchDate(ActionEvent event) {
        if ("البحث بتاريخ الصادر".equals(getSearchType())) {
            searchDateDay.setDisable(false);
            searchDateMonth.setDisable(false);
            searchDateYear.setDisable(false);
            year.setDisable(true);
        } else {
            searchDateDay.setDisable(true);
            searchDateMonth.setDisable(true);
            searchDateYear.setDisable(true);
            year.setDisable(false);
        }
    }

    public class GetAllData extends Thread {

        RingProgressIndicator rpi;
        int progrss = 0;

        public GetAllData(RingProgressIndicator rpi) {
            this.rpi = rpi;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i <= 70; i++) {
                    progrss = i;
                    Thread.sleep(i);
                    Platform.runLater(() -> {
                        rpi.setProgress(progrss);
                    });
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            viewdata(getAllData());
                        } catch (SQLException ex) {
                            Logger.getLogger(InternalIncomingPageController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });

                for (int i = 70; i <= 100; i++) {
                    progrss = i;
                    Thread.sleep(50);
                    Platform.runLater(() -> {
                        rpi.setProgress(progrss);
                    });

                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (progrss >= 100) {
                            try {
                                Thread.sleep(150);
                                rpi.setVisible(false);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(InternalIncomingPageController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                });

            } catch (InterruptedException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }

        }

    }
}
