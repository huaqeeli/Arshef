package controllers;

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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import modeles.SecretModel;
import Serveces.SecretPageListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class secretPageController implements Initializable {

    @FXML
    private ComboBox<String> searchType;
    @FXML
    private ComboBox<String> year;
    @FXML
    private TextField searchText;
    @FXML
    private Button searchButton1;
    @FXML
    private TextField circularid;
    @FXML
    private ComboBox<?> circularDateDay;
    @FXML
    private ComboBox<?> circularDateMonth;
    @FXML
    private ComboBox<?> circularDateYear;
    @FXML
    private ComboBox<String> destination;
    @FXML
    private TextField topic;
    @FXML
    private TextField saveFile;
    @FXML
    private TextField imageUrl;
    @FXML
    private TextField note;
    @FXML
    public VBox vbox;
    @FXML
    private TextField receiptNumber;
    @FXML
    private ComboBox<?> receiptNumberDateDay;
    @FXML
    private ComboBox<?> receiptNumberDateMonth;
    @FXML
    private ComboBox<?> receiptNumberDateYear;
    @FXML
    private ComboBox<?> searchDateDay;
    @FXML
    private ComboBox<?> searchDateMonth;
    @FXML
    private ComboBox<?> searchDateYear;
    ObservableList<String> placeComboBoxlist = FXCollections.observableArrayList();
    public final List<SecretModel> secretObject = new ArrayList<>();
    private SecretPageListener myListener;
    ObservableList<String> searchTypelist = FXCollections.observableArrayList("البحث برقم المعاملة", "البحث برقم الوارد", "البحث بتاريخ الوارد", "البحث بالموضوع", "البحث بجهة المعاملة", "البحث برقم الملف", "البحث بالرقم العسكري", "عرض الكل");
    String recordYear = null;
    String circularID = null;
    File imagefile = null;
    Stage stage = new Stage();
    byte[] pdfimage = null;
    String receiptnumber = null;
    String circularnumber = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        refreshdata();
        AppDate.setDateValue(circularDateDay, circularDateMonth, circularDateYear);
        AppDate.setCurrentDate(circularDateDay, circularDateMonth, circularDateYear);
        AppDate.setDateValue(receiptNumberDateDay, receiptNumberDateMonth, receiptNumberDateYear);
        AppDate.setCurrentDate(receiptNumberDateDay, receiptNumberDateMonth, receiptNumberDateYear);
        AppDate.setDateValue(searchDateDay, searchDateMonth, searchDateYear);
        AppDate.setCurrentDate(searchDateDay, searchDateMonth, searchDateYear);
        destination.setItems(filleCoursPlace(placeComboBoxlist));
        AppDate.setYearValue(year);
        year.setValue(Integer.toString(HijriCalendar.getSimpleYear()));
        FillComboBox.fillComboBox(searchTypelist, searchType);
    }

    private ObservableList filleCoursPlace(ObservableList list) {
        try {
            ResultSet rs = DatabaseAccess.select("placenames");
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

    private List<SecretModel> getData(ResultSet rs) {
        List<SecretModel> secretModels = new ArrayList<>();
        SecretModel secretModel;
        try {
//            ResultSet rs = DatabaseAccess.getData("SELECT * FROM secretdata ORDER BY ID DESC");
           
            while (rs.next()) {
                secretModel = new SecretModel();
                secretModel.setId(rs.getString("ID"));
                secretModel.setCircularid(rs.getString("CIRCULARID"));
                secretModel.setCirculardate(rs.getString("CIRCULARDATE"));
                secretModel.setDestination(rs.getString("DESTINATION"));
                secretModel.setReceiptNumber(rs.getString("RECEIPTNUMBER"));
                secretModel.setReceiptNumberDate(rs.getString("RECEIPTDATE"));
                secretModel.setTopic(rs.getString("TOPIC"));
                secretModel.setSaveFile(rs.getString("SAVEFILE"));
                secretModel.setNote(rs.getString("NOTE"));
                secretModel.setRecordYear(setYear(rs.getString("CIRCULARDATE")));
                secretModels.add(secretModel);
            }
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return secretModels;
    }

    private void setChosendata(SecretModel secretModel) {
        circularid.setText(secretModel.getCircularid());
        setCircularDate(secretModel.getCirculardate());
        destination.setValue(secretModel.getDestination());
        topic.setText(secretModel.getTopic());
        receiptNumber.setText(secretModel.getReceiptNumber());
        setReceiptNumberDate(secretModel.getReceiptNumberDate());
        saveFile.setText(secretModel.getSaveFile());
        note.setText(secretModel.getNote());
        circularID = secretModel.getCircularid();
        recordYear = secretModel.getRecordYear();
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

    public void refreshdata() {
        try {
            secretObject.clear();
            vbox.getChildren().clear();
            viewdata(DatabaseAccess.getData("SELECT ID, CIRCULARID, CIRCULARDATE, RECEIPTNUMBER, RECEIPTDATE, DESTINATION, TOPIC, SAVEFILE, NOTE, RECORDYEAR FROM secretdata ORDER BY CIRCULARDATE DESC"));
        } catch (IOException ex) {
            Logger.getLogger(secretPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void viewdata(ResultSet rs) {
        secretObject.addAll(getData(rs));
        if (secretObject.size() > 0) {
//            setChosendata(secretObject.get(0));
            myListener = new SecretPageListener() {
                @Override
                public void onClickListener(SecretModel secretModel) {
                    setChosendata(secretModel);
                }

            };
        }

        try {
            for (SecretModel secretObject1 : secretObject) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/view/secretPageItem.fxml"));
                AnchorPane pane = fxmlLoader.load();
                SecretPageItemController secretPageItemController = fxmlLoader.getController();
                secretPageItemController.setData(secretObject1, myListener);
                vbox.getChildren().add(pane);
            }
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    public String getCircularDate() {
        return AppDate.getDate(circularDateDay, circularDateMonth, circularDateYear);
    }

    public void setCircularDate(String circularDate) {
        AppDate.setSeparateDate(circularDateDay, circularDateMonth, circularDateYear, circularDate);
    }

    public String getReceiptNumberDate() {
        return AppDate.getDate(receiptNumberDateDay, receiptNumberDateMonth, receiptNumberDateYear);
    }

    public void setReceiptNumberDate(String circularDate) {
        AppDate.setSeparateDate(receiptNumberDateDay, receiptNumberDateMonth, receiptNumberDateYear, circularDate);
    }

    public String getSearchDate() {
        return AppDate.getDate(searchDateDay, searchDateMonth, searchDateYear);
    }

    public void setSearchDate(String date) {
        AppDate.setSeparateDate(searchDateDay, searchDateMonth, searchDateYear, date);
    }

    public String setYear(String date) {
        return AppDate.getYear(date);
    }

    @FXML
    private void searchData(ActionEvent event) {
        try {
            String searchValue = searchType.getValue();
            switch (searchValue) {
                case "البحث برقم المعاملة":
                    secretObject.clear();
                    vbox.getChildren().clear();
                    viewdata(DatabaseAccess.getData("SELECT ID, CIRCULARID, CIRCULARDATE, RECEIPTNUMBER, RECEIPTDATE, DESTINATION, TOPIC, SAVEFILE, NOTE, RECORDYEAR"
                            + " FROM secretdata WHERE CIRCULARID = '" + searchText.getText() + "' ORDER BY RECEIPTDATE DESC"));
                    break;
                case "البحث برقم الوارد":
                    secretObject.clear();
                    vbox.getChildren().clear();
                    viewdata(DatabaseAccess.getData("SELECT ID, CIRCULARID, CIRCULARDATE, RECEIPTNUMBER, RECEIPTDATE, DESTINATION, TOPIC, SAVEFILE, NOTE, RECORDYEAR"
                            + " FROM secretdata WHERE RECEIPTNUMBER = '" + searchText.getText() + "' ORDER BY RECEIPTDATE DESC"));
                    break;
                case "البحث بتاريخ الوارد":
                    secretObject.clear();
                    vbox.getChildren().clear();
                    viewdata(DatabaseAccess.getData("SELECT ID, CIRCULARID, CIRCULARDATE, RECEIPTNUMBER, RECEIPTDATE, DESTINATION, TOPIC, SAVEFILE, NOTE, RECORDYEAR"
                            + " FROM secretdata WHERE RECEIPTDATE = '" + getSearchDate() + "' ORDER BY RECEIPTDATE DESC"));
                    break;
                case "البحث بالموضوع":
                    secretObject.clear();
                    vbox.getChildren().clear();
                    viewdata(DatabaseAccess.getData("SELECT ID, CIRCULARID, CIRCULARDATE, RECEIPTNUMBER, RECEIPTDATE, DESTINATION, TOPIC, SAVEFILE, NOTE, RECORDYEAR"
                            + " FROM secretdata WHERE TOPIC LIKE '" + "%" + searchText.getText() + "%" + "' ORDER BY RECEIPTDATE DESC"));
                    break;
                case "البحث بجهة المعاملة":
                    secretObject.clear();
                    vbox.getChildren().clear();
                    viewdata(DatabaseAccess.getData("SELECT ID, CIRCULARID, CIRCULARDATE, RECEIPTNUMBER, RECEIPTDATE, DESTINATION, TOPIC, SAVEFILE, NOTE, RECORDYEAR"
                            + " FROM secretdata WHERE DESTINATION LIKE '" + "%" + searchText.getText() + "%" + "' ORDER BY RECEIPTDATE DESC"));
                    break;
                case "البحث برقم الملف":
                    secretObject.clear();
                    vbox.getChildren().clear();
                    viewdata(DatabaseAccess.getData("SELECT ID, CIRCULARID, CIRCULARDATE, RECEIPTNUMBER, RECEIPTDATE, DESTINATION, TOPIC, SAVEFILE, NOTE, RECORDYEAR"
                            + " FROM secretdata WHERE SAVEFILE = '" + searchText.getText() + "' ORDER BY RECEIPTDATE DESC"));
                    break;
                case "البحث بالرقم العسكري":
                    secretObject.clear();
                    vbox.getChildren().clear();
                    viewdata(DatabaseAccess.getData("SELECT ID, CIRCULARID, CIRCULARDATE, RECEIPTNUMBER, RECEIPTDATE, DESTINATION, TOPIC, SAVEFILE, NOTE, RECORDYEAR"
                            + " FROM secretdata,circularnames WHERE secretdata.ID = circularnames.CIRCULARID AND circularnames.MILITARYID = '" + searchText.getText() + "' ORDER BY RECEIPTDATE DESC"));
                    break;
            }
        } catch (IOException ex) {
            Logger.getLogger(secretPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void save(ActionEvent event) throws SQLException {
        String tableName = "secretdata";
        String fieldName = null;
        recordYear = setYear(getCircularDate());
        try {
            if ((receiptNumber.getText() == null || "".equals(receiptNumber.getText())) && (circularid.getText() == null || "".equals(circularid.getText()))) {
                receiptnumber = DatabaseAccess.getRegistrationNum();
                circularnumber = DatabaseAccess.getRegistrationNum();
                DatabaseAccess.updatRegistrationNum();
            } else if ((receiptNumber.getText() != null || !"".equals(receiptNumber.getText())) && (circularid.getText() == null || "".equals(circularid.getText()))) {
                circularnumber = DatabaseAccess.getRegistrationNum();
                receiptnumber = receiptNumber.getText();
                DatabaseAccess.updatRegistrationNum();
            } else if ((receiptNumber.getText() == null || "".equals(receiptNumber.getText())) && (circularid.getText() != null || !"".equals(circularid.getText()))) {
                receiptnumber = DatabaseAccess.getRegistrationNum();
                circularnumber = circularid.getText();
                DatabaseAccess.updatRegistrationNum();
            } else {
                receiptnumber = receiptNumber.getText();
                circularnumber = circularid.getText();
            }
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }

        String[] data = {circularnumber, getCircularDate(), receiptnumber, getReceiptNumberDate(), destination.getValue(), topic.getText(), saveFile.getText(), note.getText(), recordYear};
        String valuenumbers = null;
        if (imagefile != null) {
            fieldName = "`CIRCULARID`,`CIRCULARDATE`,`RECEIPTNUMBER`,`RECEIPTDATE`,`DESTINATION`,`TOPIC`,`SAVEFILE`,`NOTE`,`RECORDYEAR`,`IMAGE`";
            valuenumbers = "?,?,?,?,?,?,?,?,?,?";
        } else {
            fieldName = "`CIRCULARID`,`CIRCULARDATE`,`RECEIPTNUMBER`,`RECEIPTDATE`,`DESTINATION`,`TOPIC`,`SAVEFILE`,`NOTE`,`RECORDYEAR`";
            valuenumbers = "?,?,?,?,?,?,?,?,?";
        }

        boolean topicState = FormValidation.textFieldNotEmpty(topic, "الرجاء ادخال الموضوع");
        boolean circularidExusting = FormValidation.ifexisting("secretdata", "CIRCULARID", "CIRCULARID = '" + circularid.getText() + "'AND RECORDYEAR = '" + year.getValue() + "'", "تم ادخال معاملة بنفس الرقم لعام " + year.getValue() + "هـ");
        boolean receiptNumberExusting = FormValidation.ifexisting("secretdata", "RECEIPTNUMBER", "RECEIPTNUMBER = '" + receiptNumber.getText() + "'AND RECORDYEAR = '" + year.getValue() + "'", "تم ادخال معاملة بنفس الرقم لعام " + year.getValue() + "هـ");
        boolean saveFileState = FormValidation.textFieldNotEmpty(saveFile, "الرجاء ادخال ملف الحفظ");

        if (topicState && saveFileState && circularidExusting && receiptNumberExusting) {
            try {
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, data, imagefile);
                refreshdata();
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void edit(ActionEvent event) throws SQLException {
        String tableName = "secretdata";
        String fieldName = null;
        recordYear = setYear(getCircularDate());
        String[] data = {circularid.getText(), getCircularDate(), receiptNumber.getText(), getReceiptNumberDate(), destination.getValue(), topic.getText(), saveFile.getText(), note.getText(), recordYear};
        if (imagefile != null) {
            fieldName = "`CIRCULARID`=?,`CIRCULARDATE`=?,`RECEIPTNUMBER`=?,`RECEIPTDATE`=?,`DESTINATION`=?,`TOPIC`=?,`SAVEFILE`=?,`NOTE`=?,`RECORDYEAR`=?,`IMAGE`=?";
        } else {
            fieldName = "`CIRCULARID`=?,`CIRCULARDATE`=?,`RECEIPTNUMBER`=?,`RECEIPTDATE`=?,`DESTINATION`=?,`TOPIC`=?,`SAVEFILE`=?,`NOTE`=?,`RECORDYEAR`=?";
        }

        boolean topicState = FormValidation.textFieldNotEmpty(topic, "الرجاء ادخال الموضوع");
        boolean saveFileState = FormValidation.textFieldNotEmpty(saveFile, "الرجاء ادخال ملف الحفظ");

        if (topicState && saveFileState) {
            try {
                DatabaseAccess.updat(tableName, fieldName, data, "CIRCULARID = '" + circularid.getText() + "'AND RECORDYEAR = '" + recordYear + "'", imagefile);
                refreshdata();
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void clear(ActionEvent event) {
        circularid.setText(null);
        AppDate.setCurrentDate(circularDateDay, circularDateMonth, circularDateYear);
        AppDate.setCurrentDate(receiptNumberDateDay, receiptNumberDateMonth, receiptNumberDateYear);
        destination.setValue(null);
        topic.setText(null);
        saveFile.setText(null);
        note.setText(null);
        receiptNumber.setText(null);
    }

    @FXML
    private void delete(ActionEvent event) throws SQLException {
        try {
            DatabaseAccess.delete("secretdata", " `CIRCULARID` ='" + circularid.getText() + "' AND RECORDYEAR ='" + recordYear + "'");
            refreshdata();
            clear(event);
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void enableSearchDate(ActionEvent event) {
        if ("البحث بتاريخ الوارد".equals(searchType.getValue())) {
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
