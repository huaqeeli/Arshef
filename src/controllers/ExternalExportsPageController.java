package controllers;

import Serveces.ExternalExportsPageListener;
import Validation.FormValidation;
import java.io.File;
import java.io.IOException;
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
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import modeles.ExportsModel;

public class ExternalExportsPageController implements Initializable {

    @FXML
    private ComboBox<String> searchType;
    @FXML
    private ComboBox<String> year;
    @FXML
    private TextField searchText;
    @FXML
    private Button searchButton1;
    @FXML
    private TextField topic;
    @FXML
    private ComboBox<String> destination;
    @FXML
    private TextField saveFile;
    @FXML
    private TextField imageUrl;
    @FXML
    private ComboBox<?> entryDateDay;
    @FXML
    private ComboBox<?> entryDateMonth;
    @FXML
    private ComboBox<?> entryDateYear;
    @FXML
    private TextField exportNum;
    @FXML
    private ComboBox<?> exportDateDay;
    @FXML
    private ComboBox<?> exportDateMonth;
    @FXML
    private ComboBox<?> exportDateYear;
    @FXML
    private TextField notes;
    private TableView<ExportsModel> exportsTable;
    @FXML
    private TextField internalincomingnum;

    ObservableList<ExportsModel> Exportslist = FXCollections.observableArrayList();
    ObservableList<String> placeComboBoxlist = FXCollections.observableArrayList();
    ObservableList<String> searchTypelist = FXCollections.observableArrayList("البحث برقم الصادر", "البحث بتاريخ الصادر", "البحث بالموضوع", "البحث بجهة الصادر", "البحث الرقم العسكري", "البحث برقم الملف", "عرض الكل");
    ObservableList<String> uintComboBoxlist = FXCollections.observableArrayList();

    File imagefile = null;
    Stage stage = new Stage();
    byte[] pdfimage = null;
    String recordYear = null;
    String id = null;
    String enteryDate = null;
    @FXML
    private ComboBox<?> searchDateDay;
    @FXML
    private ComboBox<?> searchDateMonth;
    @FXML
    private ComboBox<?> searchDateYear;
    List<ExportsModel> exportObject = new ArrayList<>();
    private ExternalExportsPageListener mylistener;
    @FXML
    private VBox vbox;
    ActionEvent event;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AppDate.setDateValue(exportDateDay, exportDateMonth, exportDateYear);
        AppDate.setDateValue(entryDateDay, entryDateMonth, entryDateYear);
        AppDate.setCurrentDate(entryDateDay, entryDateMonth, entryDateYear);
        FillComboBox.fillComboBox(searchTypelist, searchType);
        AppDate.setDateValue(searchDateDay, searchDateMonth, searchDateYear);
        AppDate.setCurrentDate(searchDateDay, searchDateMonth, searchDateYear);
        AppDate.setYearValue(year);
//        AppDate.setCurrentYear(year);
        year.setValue(Integer.toString(HijriCalendar.getSimpleYear()));
        clearListCombobox();
        refreshListCombobox();
        refreshExportTableView();
        clear(event);
    }

    private ObservableList filleUint(ObservableList list) {
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

    private void refreshListCombobox() {
        destination.setItems(filleUint(uintComboBoxlist));
    }

    public void clearListCombobox() {
        destination.getItems().clear();
    }

    @FXML
    private File getImageUrle(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter ext4 = new FileChooser.ExtensionFilter("PDF  files(*.pdf)", "*.PDF");
        fileChooser.getExtensionFilters().addAll(ext4);
        imagefile = fileChooser.showOpenDialog(stage);
        setImageUrl(imagefile.getPath());
        return imagefile;
    }

    @FXML
    private void save(ActionEvent event) {
        String tableName = "exportsdata";
        String fieldName = null;
        recordYear = Integer.toString(HijriCalendar.getSimpleYear());
        String[] data = {getTopic(), getDestination(), getExportNum(), getExportDate(), getNotes(), getSaveFile(), getInternalincomingnum(), getEntryDate(), recordYear};
        String valuenumbers = null;
        if (imagefile != null) {
            fieldName = "`TOPIC`,`DESTINATION`,`EXPORTNUM`,`EXPORTDATE`,`NOTES`,`SAVEFILE`,`INTERNALINCOMINGNUM`,`ENTRYDATE`,`RECORDYEAR`,`IMAGE`";
            valuenumbers = "?,?,?,?,?,?,?,?,?,?";
        } else {
            fieldName = "`TOPIC`,`DESTINATION`,`EXPORTNUM`,`EXPORTDATE`,`NOTES`,`SAVEFILE`,`INTERNALINCOMINGNUM`,`ENTRYDATE`,`RECORDYEAR`";
            valuenumbers = "?,?,?,?,?,?,?,?,?";
        }

        boolean destinationState = FormValidation.comboBoxNotEmpty(destination, "الرجاء ادخال جهة المعاملة");
        boolean topicState = FormValidation.textFieldNotEmpty(topic, "الرجاء ادخال الموضوع");
        boolean saveFileState = FormValidation.textFieldNotEmpty(saveFile, "الرجاء ادخال ملف الحفظ");

        if (destinationState && topicState && saveFileState) {
            try {
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, data, imagefile);
                refreshExportTableView();
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void edit(ActionEvent event) {
        String tableName = "exportsdata";
        String fieldName = null;
        String[] data = {getTopic(), getDestination(), getExportNum(), getExportDate(), getNotes(), getSaveFile(), getInternalincomingnum()};
        if (imagefile != null) {
            fieldName = "`TOPIC`=?,`DESTINATION`=?,`EXPORTNUM`=?,`EXPORTDATE`=?,`NOTES`=?,`SAVEFILE`=?,`INTERNALINCOMINGNUM`=?,`IMAGE`=?";
        } else {
            fieldName = "`TOPIC`=?,`DESTINATION`=?,`EXPORTNUM`=?,`EXPORTDATE`=?,`NOTES`=?,`SAVEFILE`=?,`INTERNALINCOMINGNUM`=?";
        }

        boolean destinationState = FormValidation.comboBoxNotEmpty(destination, "الرجاء ادخال جهة المعاملة");
        boolean topicState = FormValidation.textFieldNotEmpty(topic, "الرجاء ادخال الموضوع");
        boolean saveFileState = FormValidation.textFieldNotEmpty(saveFile, "الرجاء ادخال ملف الحفظ");

        if (destinationState && topicState && saveFileState) {
            try {
                int t = DatabaseAccess.updat(tableName, fieldName, data, "ID = '" + id + "'AND ENTRYDATE = '" + enteryDate + "'", imagefile);
                if (t > 0) {
                    FormValidation.showAlert("", "تم تحديث البيانات", Alert.AlertType.CONFIRMATION);
                }
                refreshExportTableView();
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void delete(ActionEvent event) {
        try {
            DatabaseAccess.delete("exportsdata", "ID = '" + id + "'AND ENTRYDATE = '" + enteryDate + "'");
            refreshExportTableView();
            clear(event);
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clear(ActionEvent event) {
        setEntryDate(HijriCalendar.getSimpleDate());
        setInternalincomingnum(null);
        setTopic(null);
        setDestination(null);
        setSaveFile(null);
        setExportNum(null);
        setExportDate(null);
        setImageUrl(null);
    }

    @FXML
    private void addtoLeaderDisplay(ActionEvent event) {
        String tableName = "displaydata";
        String[] data = {HijriCalendar.getSimpleDate(), "عرض القائد", topic.getText(), destination.getValue()};
        String fieldName = "`DISPLAYDATE`,`DISPLAYTYPE`,`TOPIC`,`DESTINATION`";
        String valuenumbers = "?,?,?,?";

        boolean idState = FormValidation.notNull(id, "الرجاءاختر السجل من الجدول");

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

        boolean idState = FormValidation.notNull(id, "الرجاءاختر السجل من الجدول");

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

        boolean idState = FormValidation.notNull(id, "الرجاءاختر السجل من الجدول");

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

        boolean idState = FormValidation.notNull(id, "الرجاءاختر السجل من الجدول");

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

        boolean idState = FormValidation.notNull(id, "الرجاءاختر السجل من الجدول");

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

        boolean idState = FormValidation.notNull(id, "الرجاءاختر السجل من الجدول");

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

    public String getYear() {
        return year.getValue();
    }

    public void setYear(String year) {
        this.year.setValue(year);
    }

    public String getSearchText() {
        return searchText.getText();
    }

    public void setSearchText(String searchText) {
        this.searchText.setText(searchText);
    }

    public String getTopic() {
        return topic.getText();
    }

    public void setTopic(String topic) {
        this.topic.setText(topic);
    }

    public String getDestination() {
        return destination.getValue();
    }

    public void setDestination(String destination) {
        this.destination.setValue(destination);
    }

    public String getSaveFile() {
        return saveFile.getText();
    }

    public void setSaveFile(String saveFile) {
        this.saveFile.setText(saveFile);
    }

    public String getImageUrl() {
        return imageUrl.getText();
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl.setText(imageUrl);
    }

    public String getExportNum() {
        return exportNum.getText();
    }

    public void setExportNum(String exportNum) {
        this.exportNum.setText(exportNum);
    }

    public String getNotes() {
        return notes.getText();
    }

    public void setNotes(String notes) {
        this.notes.setText(notes);
    }

    public String getInternalincomingnum() {
        return internalincomingnum.getText();
    }

    public void setInternalincomingnum(String internalincomingnum) {
        this.internalincomingnum.setText(internalincomingnum);
    }

    public String getEntryDate() {
        return AppDate.getDate(entryDateDay, entryDateMonth, entryDateYear);
    }

    public void setEntryDate(String date) {
        AppDate.setSeparateDate(entryDateDay, entryDateMonth, entryDateYear, date);
    }

    public String getExportDate() {
        if (exportDateDay.getValue() != null && exportDateMonth.getValue() != null && exportDateYear.getValue() != null) {
            return AppDate.getDate(exportDateDay, exportDateMonth, exportDateYear);
        }
        return null;
    }

    public void setExportDate(String date) {
        AppDate.setSeparateDate(exportDateDay, exportDateMonth, exportDateYear, date);
    }

    public String getSearchType() {
        return searchType.getValue();
    }

    public void setSearchType(String searchType) {
        this.searchType.setValue(searchType);
    }

    public String getSearchDate() {
        return AppDate.getDate(searchDateDay, searchDateMonth, searchDateYear);
    }

    public void setSearchDate(String date) {
        AppDate.setSeparateDate(searchDateDay, searchDateMonth, searchDateYear, date);
    }

    private void refreshExportTableView() {
        try {
            exportObject.clear();
            vbox.getChildren().clear();
            viewdata(DatabaseAccess.getData("SELECT ID,ENTRYDATE,TOPIC,DESTINATION,SAVEFILE,EXPORTNUM,EXPORTDATE,NOTES FROM exportsdata WHERE ENTRYDATE = '" + HijriCalendar.getSimpleDate() + "' ORDER BY ENTRYDATE DESC"));
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    private List<ExportsModel> getData(ResultSet rs) {
        List<ExportsModel> exportsModels = new ArrayList<>();
        ExportsModel exportsModel;
        try {
            while (rs.next()) {
                exportsModel = new ExportsModel();
                exportsModel.setId(rs.getString("ID"));
                exportsModel.setEntryDate(rs.getString("ENTRYDATE"));
                exportsModel.setTopic(rs.getString("TOPIC"));
                exportsModel.setDestination(rs.getString("DESTINATION"));
                exportsModel.setSaveFile(rs.getString("SAVEFILE"));
                exportsModel.setExportNum(rs.getString("EXPORTNUM"));
                exportsModel.setExportDate(rs.getString("EXPORTDATE"));
                exportsModel.setNotes(rs.getString("NOTES"));
                exportsModel.setRecordYear(AppDate.getYear(rs.getString("ENTRYDATE")));
                exportsModels.add(exportsModel);
            }
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return exportsModels;
    }

    private void setChosendata(ExportsModel exportsModel) {
        setEntryDate(exportsModel.getEntryDate());
        setInternalincomingnum(exportsModel.getInternalincomingnum());
        setTopic(exportsModel.getTopic());
        setDestination(exportsModel.getDestination());
        setSaveFile(exportsModel.getSaveFile());
        setExportNum(exportsModel.getExportNum());
        setExportDate(exportsModel.getExportDate());
        setNotes(exportsModel.getNotes());
        recordYear = AppDate.getYear(exportsModel.getEntryDate());
        id = exportsModel.getId();
        enteryDate = exportsModel.getEntryDate();
    }

    private void viewdata(ResultSet rs) {
        exportObject.addAll(getData(rs));
        if (exportObject.size() > 0) {
            setChosendata(exportObject.get(0));
            mylistener = new ExternalExportsPageListener() {
                @Override
                public void onClickListener(ExportsModel exportsModel) {
                    setChosendata(exportsModel);
                }
            };
        }

        try {
            for (ExportsModel exportsModel : exportObject) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/view/ExternalExportsItem.fxml"));
                AnchorPane pane = fxmlLoader.load();
                ExternalExportsItemController externalExportsItemController = fxmlLoader.getController();
                externalExportsItemController.setData(exportsModel, mylistener);
                vbox.getChildren().add(pane);
            }
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    /*"البحث برقم الصادر", "البحث بتاريخ الصادر", "البحث بالموضوع", "البحث بجهة الصادر", "عرض الكل"*/
    @FXML
    private void searchData(ActionEvent event) {
        String searchValue = getSearchType();
        switch (searchValue) {
            case "عرض الكل":
                exportObject.clear();
                vbox.getChildren().clear();
                viewdata(getAllData());
                break;
            case "البحث بجهة الصادر":
                exportObject.clear();
                vbox.getChildren().clear();
                viewdata(getDataByDestination());
                break;
            case "البحث بالموضوع":
                exportObject.clear();
                vbox.getChildren().clear();
                viewdata(getDataByTopic());
                break;
            case "البحث بتاريخ الصادر":
                exportObject.clear();
                vbox.getChildren().clear();
                viewdata(getDataByExportDate());
                break;
            case "البحث برقم الصادر":
                exportObject.clear();
                vbox.getChildren().clear();
                viewdata(getDataByExportNumber());
                break;
            case "البحث برقم الملف":
                exportObject.clear();
                vbox.getChildren().clear();
                viewdata(getDataBySaveFile());
                break;
            case "البحث الرقم العسكري":
                exportObject.clear();
                vbox.getChildren().clear();
                viewdata(getDataMitaryID());
                break;
        }
    }

    public ResultSet getAllData() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.select("exportsdata", "RECORDYEAR = '" + getYear() + "' ORDER BY ENTRYDATE DESC ");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public ResultSet getDataByDestination() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.selectQuiry("SELECT ID,ENTRYDATE,TOPIC,DESTINATION,SAVEFILE,EXPORTNUM,EXPORTDATE,NOTES FROM exportsdata WHERE DESTINATION LIKE '" + "%" + getSearchText() + "%" + "' AND RECORDYEAR = '" + getYear() + "' ");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public ResultSet getDataByTopic() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.selectQuiry("SELECT ID,ENTRYDATE,TOPIC,DESTINATION,SAVEFILE,EXPORTNUM,EXPORTDATE,NOTES FROM exportsdata WHERE TOPIC LIKE '" + "%" + getSearchText() + "%" + "' AND RECORDYEAR = '" + getYear() + "' ");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public ResultSet getDataByExportDate() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.select("exportsdata", "EXPORTDATE = '" + getSearchDate() + "'");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public ResultSet getDataByExportNumber() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.select("exportsdata", "EXPORTNUM = '" + getSearchText() + "'AND RECORDYEAR = '" + getYear() + "' ");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public ResultSet getDataBySaveFile() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.select("exportsdata", "SAVEFILE = '" + getSearchText() + "' AND RECORDYEAR = '" + getYear() + "'");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public ResultSet getDataMitaryID() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.getData("SELECT exportsdata.ID,exportsdata.ENTRYDATE,exportsdata.TOPIC,exportsdata.DESTINATION,exportsdata.SAVEFILE,exportsdata.EXPORTNUM,exportsdata.EXPORTDATE,exportsdata.NOTES FROM exportsdata,circularnames WHERE exportsdata.ID = circularnames.CIRCULARID AND circularnames.MILITARYID = '" + getSearchText() + "' AND RECORDYEAR = '" + getYear() + "'");
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

    @FXML
    private void getInernalIncomingData(KeyEvent event) {
        try {
            ResultSet rs = DatabaseAccess.select("internalincoming", "REGIS_NO = '" + getInternalincomingnum() + "'");
            if (rs.next()) {
                setTopic(rs.getString("TOPIC"));
                setSaveFile(rs.getString("SAVE_FILE"));
            }
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

}
