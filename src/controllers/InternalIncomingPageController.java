package controllers;

import Serveces.InternalIncomingPageListener;
import Validation.FormValidation;
import static Validation.FormValidation.showAlert;
import arshef.App;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import modeles.InternalIncomingModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class InternalIncomingPageController implements Initializable {

    @FXML
    private TextField circularNumber;
    @FXML
    private TextField topic;
    @FXML
    private TextField saveFaile;
    @FXML
    private ComboBox<String> destination;
    @FXML
    private ComboBox<String> incomingDay;
    @FXML
    private ComboBox<String> incomingMonth;
    @FXML
    private ComboBox<String> incomingYear;
    @FXML
    private ComboBox<String> circularDateday;
    @FXML
    private ComboBox<String> circularDatemonth;
    @FXML
    private ComboBox<String> circularDateyear;
    @FXML
    private ComboBox<String> searchType;
    @FXML
    private ComboBox<String> year;
    @FXML
    private TextField searchText;
    @FXML
    private TextField notes;
    @FXML
    private TextField imageUrl;
    File imagefile = null;
    Stage stage = new Stage();
    byte[] pdfimage = null;
    String recordYear = null;
    private String registrationId = null;
    ObservableList<String> destinationlist = FXCollections.observableArrayList();
    ObservableList<InternalIncomingModel> recipientList = FXCollections.observableArrayList();
    ObservableList<String> searchTypelist = FXCollections.observableArrayList("البحث برقم الوارد", "البحث بتاريخ الوارد", "البحث بالموضوع", "البحث بجهة الوارد", "عرض الكل");
    
    public final List<InternalIncomingModel> internalIncomingObject = new ArrayList<>();
    private InternalIncomingPageListener mylistener;
    
    Config config = new Config();
    @FXML
    private ComboBox<?> searchDateDay;
    @FXML
    private ComboBox<?> searchDateMonth;
    @FXML
    private ComboBox<?> searchDateYear;
    @FXML
    private VBox vbox;
    ActionEvent event;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        refreshdata();
        AppDate.setDateValue(incomingDay, incomingMonth, incomingYear);
        AppDate.setCurrentDate(incomingDay, incomingMonth, incomingYear);
        AppDate.setDateValue(circularDateday, circularDatemonth, circularDateyear);
        AppDate.setCurrentDate(circularDateday, circularDatemonth, circularDateyear);
        AppDate.setDateValue(searchDateDay, searchDateMonth, searchDateYear);
        AppDate.setCurrentDate(searchDateDay, searchDateMonth, searchDateYear);
        FillComboBox.fillComboBox(searchTypelist, searchType);
        destination.setItems(filleDestination(destinationlist));
        AppDate.setYearValue(year);
        AppDate.setCurrentYear(year);
        clear(event);
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

    private void refreshdata() {
        try {
            internalIncomingObject.clear();
            vbox.getChildren().clear();
            viewdata(DatabaseAccess.getData("SELECT REGIS_NO,RECIPIENT_DATE,CIRCULAR_NO,CIRCULAR_DATE,CIRCULAR_DIR,TOPIC,SAVE_FILE,NOTES FROM internalincoming where RECORD_YEAR ='" + Integer.toString(HijriCalendar.getSimpleYear()) + "' ORDER BY REGIS_NO DESC"));
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    private List<InternalIncomingModel> getData(ResultSet rs) {
        List<InternalIncomingModel> internalIncomingModels = new ArrayList<>();
        InternalIncomingModel internalIncomingModel;
        try {
            while (rs.next()) {
                internalIncomingModel = new InternalIncomingModel();
                internalIncomingModel.setRegisNo(rs.getString("REGIS_NO"));
                internalIncomingModel.setRecipientDate(rs.getString("RECIPIENT_DATE"));
                internalIncomingModel.setCircularNo(rs.getString("CIRCULAR_NO"));
                internalIncomingModel.setCircularDate(rs.getString("CIRCULAR_DATE"));
                internalIncomingModel.setCircularDir(rs.getString("CIRCULAR_DIR"));
                internalIncomingModel.setTopic(rs.getString("TOPIC"));
                internalIncomingModel.setSaveFile(rs.getString("SAVE_FILE"));
                internalIncomingModel.setNotes(rs.getString("NOTES"));
                internalIncomingModel.setRecordYear(AppDate.getYear(rs.getString("RECIPIENT_DATE")));
                internalIncomingModels.add(internalIncomingModel);
            }
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return internalIncomingModels;
    }

    private void setChosendata(InternalIncomingModel internalIncomingModel) {
        setCircularDate(internalIncomingModel.getCircularDate());
        setIncomingDate(internalIncomingModel.getRecipientDate());
        setCircularNumber(internalIncomingModel.getCircularNo());
        setDestination(internalIncomingModel.getCircularDir());
        setTopic(internalIncomingModel.getTopic());
        setSaveFaile(internalIncomingModel.getSaveFile());
        setNotes(internalIncomingModel.getNotes());
        registrationId = internalIncomingModel.getRegisNo();
        recordYear = AppDate.getYear(internalIncomingModel.getCircularDate());
    }

    private void viewdata(ResultSet rs) {
        internalIncomingObject.addAll(getData(rs));
        if (internalIncomingObject.size() > 0) {
            setChosendata(internalIncomingObject.get(0));
            mylistener = new InternalIncomingPageListener() {
                @Override
                public void onClickListener(InternalIncomingModel internalIncomingModel) {
                    setChosendata(internalIncomingModel);
                }
            };
        }

        try {
            for (InternalIncomingModel internalIncomingModel : internalIncomingObject) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/view/InternalIncomingItem.fxml"));
                AnchorPane pane = fxmlLoader.load();
                InternalIncomingItemController internalIncomingItemController = fxmlLoader.getController();
                internalIncomingItemController.setData(internalIncomingModel, mylistener);
                vbox.getChildren().add(pane);
            }
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

    @FXML
    private void insertData(ActionEvent event) throws IOException {
        String tableName = "internalincoming";
        recordYear = Integer.toString(HijriCalendar.getSimpleYear());
        String fieldName = null;
        String[] data = {DatabaseAccess.getRegistrationNum(), getIncomingDate(), getCircularNumber(), getCircularDate(), getDestination(), getTopic(), getSaveFaile(), getNotes(), recordYear};
        String valuenumbers = null;
        if (imagefile != null) {
            fieldName = "`REGIS_NO`,`RECIPIENT_DATE`,`CIRCULAR_NO`,`CIRCULAR_DATE`,`CIRCULAR_DIR`,`TOPIC`,`SAVE_FILE`,`NOTES`,`RECORD_YEAR`,`IMAGE`";
            valuenumbers = "?,?,?,?,?,?,?,?,?,?";
        } else {
            fieldName = "`REGIS_NO`,`RECIPIENT_DATE`,`CIRCULAR_NO`,`CIRCULAR_DATE`,`CIRCULAR_DIR`,`TOPIC`,`SAVE_FILE`,`NOTES`,`RECORD_YEAR`";
            valuenumbers = "?,?,?,?,?,?,?,?,?";
        }
        boolean circularNumberState = FormValidation.textFieldNotEmpty(circularNumber, "الرجاء ادخال رقم المعاملة");
        boolean topicState = FormValidation.textFieldNotEmpty(topic, "الرجاء ادخال جهة الوارد");
        boolean circularNumberExisting = FormValidation.ifexisting("internalincoming", "CIRCULAR_NO", "CIRCULAR_NO = '" + getCircularNumber() + "'AND CIRCULAR_DIR = '" + getDestination() + "'AND RECORD_YEAR = '" + recordYear + "'", "تم حفظ المعاملة مسبقا");
        if (circularNumberState && topicState && circularNumberExisting) {
            try {
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, data, imagefile);
                registrationId = DatabaseAccess.getRegistrationNum();
                DatabaseAccess.updatRegistrationNum();
                refreshdata();
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void updateData(ActionEvent event) throws IOException {
        String tableName = "internalincoming";
        String fieldName = null;
        String[] data = {getIncomingDate(), getCircularNumber(), getCircularDate(), getDestination(), getTopic(), getSaveFaile(), getNotes()};
        if (imagefile != null) {
            fieldName = "`RECIPIENT_DATE`=?,`CIRCULAR_NO`=?,`CIRCULAR_DATE`=?,`CIRCULAR_DIR`=?,`TOPIC`=?,`SAVE_FILE`=?,`NOTES`=?,`IMAGE`=?";
        } else {
            fieldName = "`RECIPIENT_DATE`=?,`CIRCULAR_NO`=?,`CIRCULAR_DATE`=?,`CIRCULAR_DIR`=?,`TOPIC`=?,`SAVE_FILE`=?,`NOTES`=?";
        }
        boolean circularNumberState = FormValidation.textFieldNotEmpty(circularNumber, "الرجاء ادخال رقم المعاملة");
        boolean topicState = FormValidation.textFieldNotEmpty(topic, "الرجاء ادخال جهة الوارد");

        if (circularNumberState && topicState) {
            try {
                DatabaseAccess.updat(tableName, fieldName, data, "REGIS_NO = '" + registrationId + "'AND RECORD_YEAR = '" + recordYear + "'", imagefile);
                refreshdata();
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

   
    @FXML
    private void clear(ActionEvent event) {
        setCircularNumber(null);
        circularNumber.setText(null);
        setTopic(null);
        setDestination(null);
        setNotes(null);
        setImageUrl(null);
        setSaveFaile(null);
    }

    @FXML
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

    @FXML
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

    @FXML
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

    @FXML
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

    @FXML
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

    @FXML
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

    public String getCircularNumber() {
        return circularNumber.getText();
    }

    public void setCircularNumber(String circularNumber) {
        this.circularNumber.setText(circularNumber);
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

    public String getCircularDate() {
        return AppDate.getDate(circularDateday, circularDatemonth, circularDateyear);
    }

    public void setCircularDate(String date) {
        AppDate.setSeparateDate(circularDateday, circularDatemonth, circularDateyear, date);
    }

    public String getIncomingDate() {
        return AppDate.getDate(incomingDay, incomingMonth, incomingYear);
    }

    public void setIncomingDate(String date) {
        AppDate.setSeparateDate(incomingDay, incomingMonth, incomingYear, date);
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
    private void searchData(ActionEvent event) {
        String searchValue = getSearchType();
        switch (searchValue) {
            case "عرض الكل":
                internalIncomingObject.clear();
                vbox.getChildren().clear();
                viewdata(getAllData());
                break;
            case "البحث بجهة الوارد":
                internalIncomingObject.clear();
                vbox.getChildren().clear();
                viewdata(getDataByDestination());
                break;
            case "البحث بالموضوع":
                internalIncomingObject.clear();
                vbox.getChildren().clear();
                viewdata(getDataByTopic());
                break;
            case "البحث بتاريخ الوارد":
                internalIncomingObject.clear();
                vbox.getChildren().clear();
                viewdata(getDataByIncomingDate());
                break;
            case "البحث برقم الوارد":
                internalIncomingObject.clear();
                vbox.getChildren().clear();
                viewdata(getDataByRegistrationNum());
                break;
        }
    }

    public ResultSet getAllData() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.select("internalincoming", "RECORD_YEAR = '" + getYear() + "' ");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public ResultSet getDataByDestination() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.selectQuiry("SELECT * FROM internalincoming WHERE CIRCULAR_DIR LIKE '" + "%" + getSearchText() + "%" + "' AND RECORD_YEAR = '" + getYear() + "' ");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public ResultSet getDataByTopic() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.selectQuiry("SELECT * FROM internalincoming WHERE TOPIC LIKE '" + "%" + getSearchText() + "%" + "' AND RECORD_YEAR = '" + getYear() + "' ");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public ResultSet getDataByIncomingDate() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.select("internalincoming", "RECIPIENT_DATE = '" + getSearchDate() + "'");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public ResultSet getDataByRegistrationNum() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.select("internalincoming", "REGIS_NO = '" + getSearchText() + "'");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    @FXML
    private void enableSearchDate(ActionEvent event) {
        if ("البحث بتاريخ الوارد".equals(getSearchType())) {
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

}
