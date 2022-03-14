package controllers;

import Serveces.ExternalIncomingPageListener;
import Validation.FormValidation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import modeles.ArchefModel;

public class ExternalIncomingPageController implements Initializable {

    ObservableList<ArchefModel> Archeflist = FXCollections.observableArrayList();
    ObservableList<String> coursComboBoxlist = FXCollections.observableArrayList();
    ObservableList<String> placeComboBoxlist = FXCollections.observableArrayList();
    ObservableList<String> searchTypelist = FXCollections.observableArrayList("البحث برقم المعاملة", "البحث برقم الوارد", "البحث بتاريخ الوارد", "البحث بالموضوع", "البحث بجهة المعاملة", "البحث برقم الملف", "البحث بالرقم العسكري", "عرض الكل");

    @FXML
    private TextField imageUrl;
    String circularID = null;
    String circularDate = null;
    File imagefile = null;
    Stage stage = new Stage();
    byte[] pdfimage = null;
    String arshefyear = null;

    @FXML
    private TextField searchText;
    @FXML
    private TextField circularid;
    @FXML
    private ComboBox<String> circularDateDay;
    @FXML
    private ComboBox<String> circularDateMonth;
    @FXML
    private ComboBox<String> circularDateYear;
    @FXML
    private TextField receiptNumber;
    @FXML
    private ComboBox<String> receiptNumberDateDay;
    @FXML
    private ComboBox<String> receiptNumberDateMonth;
    @FXML
    private ComboBox<String> receiptNumberDateYear;
    @FXML
    private ComboBox<String> destination;
    @FXML
    private TextField topic;
    @FXML
    private TextField saveFile;
    @FXML
    private ComboBox<String> searchType;
    @FXML
    private ComboBox<String> year;
    @FXML
    private Button searchButton1;
    @FXML
    private TextField action;
    private TableColumn<ArchefModel, String> addImage_col;
    @FXML
    private VBox vbox;
    List<ArchefModel> archefModelObject = new ArrayList<>();
    private ExternalIncomingPageListener mylistener;
    ActionEvent event;
    @FXML
    private ComboBox<String> searchDateDay;
    @FXML
    private ComboBox<String> searchDateMonth;
    @FXML
    private ComboBox<String> searchDateYear;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        refreshData();
        FillComboBox.fillComboBox(searchTypelist, searchType);
        AppDate.setDateValue(circularDateDay, circularDateMonth, circularDateYear);
        AppDate.setDateValue(receiptNumberDateDay, receiptNumberDateMonth, receiptNumberDateYear);
        AppDate.setCurrentDate(circularDateDay, circularDateMonth, circularDateYear);
        AppDate.setCurrentDate(receiptNumberDateDay, receiptNumberDateMonth, receiptNumberDateYear);
        AppDate.setDateValue(searchDateDay, searchDateMonth, searchDateYear);
        AppDate.setCurrentDate(searchDateDay, searchDateMonth, searchDateYear);
        AppDate.setYearValue(year);
        year.setValue(Integer.toString(HijriCalendar.getSimpleYear()));
        destination.setItems(filleCoursPlace(placeComboBoxlist));
        clear(event);
    }

    private ObservableList filleCoursPlace(ObservableList list) {
        try {
            ResultSet rs = DatabaseAccess.select("placenames", "UINTTYPE='خارجي'");
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

    @FXML
    private void save(ActionEvent event) throws SQLException {
        String tableName = "externalincoming";
        String fieldName = null;
        arshefyear = AppDate.getYear(getCircularDate());
        String[] data = {getCircularid(), getCircularDate(), getReceiptNumber(), getReceiptNumberDate(), getDestination(), getTopic(), getSaveFile(), getAction(), arshefyear};
        String valuenumbers = null;
        if (imagefile != null) {
            fieldName = "`CIRCULARID`,`CIRCULARDATE`,`RECEIPTNUMBER`,`RECEIPTDATE`,`DESTINATION`,`TOPIC`,`SAVEFILE`,`ACTION`,`ARSHEFYEAR`,`IMAGE`";
            valuenumbers = "?,?,?,?,?,?,?,?,?,?";
        } else {
            fieldName = "`CIRCULARID`,`CIRCULARDATE`,`RECEIPTNUMBER`,`RECEIPTDATE`,`DESTINATION`,`TOPIC`,`SAVEFILE`,`ACTION`,`ARSHEFYEAR`";
            valuenumbers = "?,?,?,?,?,?,?,?,?";
        }

        boolean circularidState = FormValidation.textFieldNotEmpty(circularid, "الرجاء ادخال رقم المعاملة");
        boolean circularidTypeNumber = FormValidation.textFieldTypeNumber(circularid, "يقبل ارقام فقط");
        boolean circularidSNotexisting = FormValidation.ifexisting("externalincoming", "`CIRCULARID`,`ARSHEFYEAR`", "CIRCULARID ='" + getCircularid() + "' AND ARSHEFYEAR='" + arshefyear + "'", "تم ادخال رقم المعاملة مسبقا");
        boolean receiptNumberNotexisting = FormValidation.ifexisting("externalincoming", "`RECEIPTNUMBER`,`ARSHEFYEAR`", "RECEIPTNUMBER ='" + getReceiptNumber() + "' AND ARSHEFYEAR='" + arshefyear + "'", "تم ادخال رقم الوارد  مسبقا");
        boolean receiptNumberState = FormValidation.textFieldNotEmpty(receiptNumber, "الرجاء ادخال رقم الوارد");
        boolean receiptNumberTypeNumber = FormValidation.textFieldTypeNumber(receiptNumber, "يقبل ارقام فقط");
        boolean destinationState = FormValidation.comboBoxNotEmpty(destination, "الرجاء ادخال جهة المعاملة");
        boolean topicState = FormValidation.textFieldNotEmpty(topic, "الرجاء ادخال الموضوع");
        boolean saveFileState = FormValidation.textFieldNotEmpty(saveFile, "الرجاء ادخال ملف الحفظ");

        if (circularidState && circularidSNotexisting && receiptNumberNotexisting && receiptNumberState && destinationState && topicState && saveFileState && circularidTypeNumber && receiptNumberTypeNumber) {
            try {
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, data, imagefile);
                refreshData();
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void edit(ActionEvent event) throws SQLException {
        String tableName = "externalincoming";
        String fieldName = null;
        arshefyear = AppDate.getYear(getCircularDate());
        String[] data = {getCircularid(), getCircularDate(), getReceiptNumber(), getReceiptNumberDate(), getDestination(), getTopic(), getSaveFile(), getAction()};
        if (imagefile != null) {
            fieldName = "`CIRCULARID`=?,`CIRCULARDATE`=?,`RECEIPTNUMBER`=?,`RECEIPTDATE`=?,`DESTINATION`=?,`TOPIC`=?,`SAVEFILE`=?,`ACTION`=?,`IMAGE`=?";
        } else {
            fieldName = "`CIRCULARID`=?,`CIRCULARDATE`=?,`RECEIPTNUMBER`=?,`RECEIPTDATE`=?,`DESTINATION`=?,`TOPIC`=?,`SAVEFILE`=?,`ACTION`=?";
        }

        boolean circularidState = FormValidation.textFieldNotEmpty(circularid, "الرجاء ادخال رقم المعاملة");
        boolean circularidTypeNumber = FormValidation.textFieldTypeNumber(circularid, "الرجاء ادخال رقم المعاملة");
        boolean receiptNumberState = FormValidation.textFieldNotEmpty(receiptNumber, "الرجاء ادخال رقم الوارد");
        boolean receiptNumberTypeNumber = FormValidation.textFieldTypeNumber(receiptNumber, "الرجاء ادخال رقم الوارد");
        boolean destinationState = FormValidation.comboBoxNotEmpty(destination, "الرجاء ادخال جهة المعاملة");
        boolean topicState = FormValidation.textFieldNotEmpty(topic, "الرجاء ادخال الموضوع");
        boolean saveFileState = FormValidation.textFieldNotEmpty(saveFile, "الرجاء ادخال ملف الحفظ");

        if (circularidState && receiptNumberState && destinationState && topicState && saveFileState && circularidTypeNumber && receiptNumberTypeNumber) {
            try {
                int t = DatabaseAccess.updat(tableName, fieldName, data, "CIRCULARID = '" + circularID + "' AND ARSHEFYEAR = '" + arshefyear + "' ", imagefile);
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

    @FXML
    private void delete(ActionEvent event) {
        try {
            arshefyear = AppDate.getYear(getCircularDate());
            DatabaseAccess.delete("externalincoming", "CIRCULARID = '" + circularID + "' AND ARSHEFYEAR = '" + arshefyear + "' ");
            refreshData();
            clear(event);
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    public String getImageUrl() {
        return imageUrl.getText();
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl.setText(imageUrl);
    }

    public String getSearchText() {
        return searchText.getText();
    }

    public void setSearchText(String searchText) {
        this.searchText.setText(searchText);
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

    public String getAction() {
        return action.getText();
    }

    public void setAction(String action) {
        this.action.setText(action);
    }

    public String getCircularid() {
        return circularid.getText();
    }

    public void setCircularid(String circularid) {
        this.circularid.setText(circularid);
    }

    public String getReceiptNumber() {
        return receiptNumber.getText();
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber.setText(receiptNumber);
    }

    public String getDestination() {
        return destination.getValue();
    }

    public void setDestination(String destination) {
        this.destination.setValue(destination);
    }

    public String getTopic() {
        return topic.getText();
    }

    public void setTopic(String topic) {
        this.topic.setText(topic);
    }

    public String getSaveFile() {
        return saveFile.getText();
    }

    public void setSaveFile(String saveFile) {
        this.saveFile.setText(saveFile);
    }

    public String getCircularDate() {
        return AppDate.getDate(circularDateDay, circularDateMonth, circularDateYear);
    }

    public void setCircularDate() {
        AppDate.setCurrentDate(circularDateDay, circularDateMonth, circularDateYear);
    }

    public void setCircularDate(String date) {
        AppDate.setSeparateDate(circularDateDay, circularDateMonth, circularDateYear, date);
    }

    public String getReceiptNumberDate() {
        return AppDate.getDate(receiptNumberDateDay, receiptNumberDateMonth, receiptNumberDateYear);
    }

    public void setReceiptNumberDate() {
        AppDate.setCurrentDate(receiptNumberDateDay, receiptNumberDateMonth, receiptNumberDateYear);
    }

    public void setReceiptNumberDate(String date) {
        AppDate.setSeparateDate(receiptNumberDateDay, receiptNumberDateMonth, receiptNumberDateYear, date);
    }

    public String getCircularDateYear() {
        return circularDateYear.getValue();
    }

    public void setCircularDateYear(String circularDateYear) {
        this.circularDateYear.setValue(circularDateYear);
    }

    @FXML
    private void clear(ActionEvent event) {
        imageUrl.setText(null);
        imagefile = null;
        setCircularid(null);
        setReceiptNumber(null);
        setSaveFile(null);
        setTopic(null);
        setDestination(null);
        setAction(null);
        AppDate.setCurrentDate(circularDateDay, circularDateMonth, circularDateYear);
        AppDate.setCurrentDate(receiptNumberDateDay, receiptNumberDateMonth, receiptNumberDateYear);
    }

    private void refreshData() {
        try {
            archefModelObject.clear();
            vbox.getChildren().clear();
            viewdata(DatabaseAccess.getData("SELECT CIRCULARID,CIRCULARDATE,TOPIC,DESTINATION,SAVEFILE,RECEIPTNUMBER,RECEIPTDATE,ACTION FROM externalincoming WHERE RECEIPTDATE = '" + HijriCalendar.getSimpleDate() + "'  ORDER BY ID DESC"));
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    private List<ArchefModel> getData(ResultSet rs) {
        List<ArchefModel> archefModels = new ArrayList<>();
        ArchefModel archefModel;
        try {
            while (rs.next()) {
                archefModel = new ArchefModel();
                archefModel.setCircularid(rs.getString("CIRCULARID"));
                archefModel.setCircularDate(rs.getString("CIRCULARDATE"));
                archefModel.setTopic(rs.getString("TOPIC"));
                archefModel.setDestination(rs.getString("DESTINATION"));
                archefModel.setSaveFile(rs.getString("SAVEFILE"));
                archefModel.setReceiptNumber(rs.getString("RECEIPTNUMBER"));
                archefModel.setReceiptDate(rs.getString("RECEIPTDATE"));
                archefModel.setAction(rs.getString("ACTION"));
                archefModel.setRecordYear(AppDate.getYear(rs.getString("RECEIPTNUMBER")));
                archefModels.add(archefModel);
            }
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return archefModels;
    }

    private void setChosendata(ArchefModel archefModel) {
        setCircularid(archefModel.getCircularid());
        setCircularDate(archefModel.getCircularDate());
        setReceiptNumber(archefModel.getReceiptNumber());
        setReceiptNumberDate(archefModel.getReceiptDate());
        setTopic(archefModel.getTopic());
        setDestination(archefModel.getDestination());
        setSaveFile(archefModel.getSaveFile());
        setAction(archefModel.getAction());
        arshefyear = AppDate.getYear(archefModel.getReceiptDate());
        circularID = archefModel.getCircularid();
    }

    private void viewdata(ResultSet rs) {
        archefModelObject.addAll(getData(rs));
        if (archefModelObject.size() > 0) {
            setChosendata(archefModelObject.get(0));
            mylistener = new ExternalIncomingPageListener() {
                @Override
                public void onClickListener(ArchefModel archefModel) {
                    setChosendata(archefModel);
                }
            };
        }

        try {
            for (ArchefModel archefModel : archefModelObject) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/view/ExternalIncomingItem.fxml"));
                AnchorPane pane = fxmlLoader.load();
                ExternalIncomingItemController externalIncomingItemController = fxmlLoader.getController();
                externalIncomingItemController.setData(archefModel, mylistener);
                vbox.getChildren().add(pane);
            }
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private File getImageUrle(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter ext4 = new FileChooser.ExtensionFilter("PDF  files(*.pdf)", "*.PDF");
        fileChooser.getExtensionFilters().addAll(ext4);
        imagefile = fileChooser.showOpenDialog(stage);
        imageUrl.setText(imagefile.getPath());
        return imagefile;
    }

    public String getSearchDate() {
        return AppDate.getDate(searchDateDay, searchDateMonth, searchDateYear);
    }

    @FXML
    private void searchData(ActionEvent event) {
        String searchValue = getSearchType();
        switch (searchValue) {
            case "البحث برقم المعاملة":
                archefModelObject.clear();
                vbox.getChildren().clear();
                viewdata(getDataBycircularid());
                break;
            case "البحث برقم الوارد":
                archefModelObject.clear();
                vbox.getChildren().clear();
                viewdata(getDataByReceiptNumber());
                break;
            case "البحث بالموضوع":
                archefModelObject.clear();
                vbox.getChildren().clear();
                viewdata(getDataByTopic());
                break;
            case "البحث بجهة المعاملة":
                archefModelObject.clear();
                vbox.getChildren().clear();
                viewdata(getDataByDestination());
                break;
            case "البحث برقم الملف":
                archefModelObject.clear();
                vbox.getChildren().clear();
                viewdata(getDataSaveFile());
                break;
            case "البحث بالرقم العسكري":
                archefModelObject.clear();
                vbox.getChildren().clear();
                viewdata(getDataMitaryID());
                break;
            case "البحث بتاريخ الوارد":
                archefModelObject.clear();
                vbox.getChildren().clear();
                viewdata(getDataByReceiptDate());
                break;
            case "عرض الكل":
                archefModelObject.clear();
                vbox.getChildren().clear();
                viewdata(getAllData());
                break;
        }
    }

    public ResultSet getAllData() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.select("externalincoming", " ARSHEFYEAR = '" + getYear() + "' ORDER BY ID DESC ");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public ResultSet getDataBycircularid() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.select("externalincoming", "CIRCULARID = '" + getSearchText() + "' AND ARSHEFYEAR = '" + getYear() + "' ");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public ResultSet getDataByReceiptDate() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.select("externalincoming", "RECEIPTDATE = '" + getSearchDate() + "' ");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public ResultSet getDataByReceiptNumber() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.select("externalincoming", "RECEIPTNUMBER = '" + getSearchText() + "' AND ARSHEFYEAR = '" + getYear() + "' ");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public ResultSet getDataSaveFile() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.select("externalincoming", "SAVEFILE = '" + getSearchText() + "' AND ARSHEFYEAR = '" + getYear() + "' ");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public ResultSet getDataByTopic() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.selectQuiry("SELECT CIRCULARID,CIRCULARDATE,TOPIC,DESTINATION,SAVEFILE,RECEIPTNUMBER,RECEIPTDATE,ACTION FROM externalincoming WHERE TOPIC LIKE '" + "%" + getSearchText() + "%" + "' AND ARSHEFYEAR = '" + getYear() + "' ");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public ResultSet getDataMitaryID() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.selectQuiry("SELECT externalincoming.CIRCULARID,externalincoming.CIRCULARDATE,externalincoming.TOPIC,externalincoming.DESTINATION,externalincoming.SAVEFILE,externalincoming.RECEIPTNUMBER,externalincoming.RECEIPTDATE,externalincoming.ACTION FROM externalincoming,circularnames WHERE externalincoming.CIRCULARID = circularnames.CIRCULARID AND circularnames.MILITARYID = '" + getSearchText() + "' AND ARSHEFYEAR = '" + getYear() + "'");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public ResultSet getDataByDestination() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.selectQuiry("SELECT * FROM externalincoming WHERE DESTINATION LIKE '" + "%" + getSearchText() + "%" + "' AND ARSHEFYEAR = '" + getYear() + "' ");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    private void addtoLeaderDisplay(ActionEvent event) {
        String tableName = "displaydata";
        String[] data = {HijriCalendar.getSimpleDate(), "عرض القائد", topic.getText(), destination.getValue()};
        String fieldName = "`DISPLAYDATE`,`DISPLAYTYPE`,`TOPIC`,`DESTINATION`";
        String valuenumbers = "?,?,?,?";

        boolean idState = FormValidation.notNull(circularID, "الرجاءاختر السجل من الجدول");

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

        boolean idState = FormValidation.notNull(circularID, "الرجاءاختر السجل من الجدول");

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

        boolean idState = FormValidation.notNull(circularID, "الرجاءاختر السجل من الجدول");

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

        boolean idState = FormValidation.notNull(circularID, "الرجاءاختر السجل من الجدول");

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

        boolean idState = FormValidation.notNull(circularID, "الرجاءاختر السجل من الجدول");

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

        boolean idState = FormValidation.notNull(circularID, "الرجاءاختر السجل من الجدول");

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

    @FXML
    private void sendTosecretData(ActionEvent event) {
        InputStream image = null;
        byte[] byteimage = null;
        try {
            String tableName = "secretdata";
            ResultSet rs = DatabaseAccess.select("externalincoming", "CIRCULARID='" + circularID + "'AND ARSHEFYEAR = '" + arshefyear + "'");
            if (rs.next()) {
                setCircularid(rs.getString("CIRCULARID"));
                setCircularDate(rs.getString("CIRCULARDATE"));
                setReceiptNumber(rs.getString("RECEIPTNUMBER"));
                setReceiptNumberDate(rs.getString("RECEIPTDATE"));
                setDestination(rs.getString("DESTINATION"));
                setTopic(rs.getString("TOPIC"));
                setSaveFile(rs.getString("SAVEFILE"));
                setAction(rs.getString("ACTION"));
                arshefyear = rs.getString("ARSHEFYEAR");
                image = rs.getBinaryStream("IMAGE");
                byteimage = new byte[image.available()];
                image.read(byteimage);
            }
            String[] data = {getCircularid(), getCircularDate(), getReceiptNumber(), getReceiptNumberDate(), getDestination(), getTopic(), getSaveFile(), getAction(), arshefyear};
            String valuenumbers = null;
            String fieldName = "`CIRCULARID`,`CIRCULARDATE`,`RECEIPTNUMBER`,`RECEIPTDATE`,`DESTINATION`,`TOPIC`,`SAVEFILE`,`NOTE`,`RECORDYEAR`,`IMAGE`";
            valuenumbers = "?,?,?,?,?,?,?,?,?,?";
            DatabaseAccess.insert(tableName, fieldName, valuenumbers, data, byteimage);
            DatabaseAccess.secretDelete("externalincoming", "CIRCULARID='" + circularID + "'AND ARSHEFYEAR = '" + arshefyear + "'");
            refreshData();
            clear(event);
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }

    }

}
