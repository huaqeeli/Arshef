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
import modeles.InternalIncomingModel;
import modeles.SecretModel;
import Serveces.SecretPageListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class secretPageController implements Initializable {

    @FXML
    private ComboBox<?> searchType;
    @FXML
    private ComboBox<?> year;
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

    ObservableList<String> placeComboBoxlist = FXCollections.observableArrayList();
    public final List<SecretModel> secretObject = new ArrayList<>();
    private SecretPageListener myListener;

    String recordYear = null;
    File imagefile = null;
    Stage stage = new Stage();
    byte[] pdfimage = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        refreshdata();
        AppDate.setDateValue(circularDateDay, circularDateMonth, circularDateYear);
        AppDate.setCurrentDate(circularDateDay, circularDateMonth, circularDateYear);
        destination.setItems(filleCoursPlace(placeComboBoxlist));
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

    private List<SecretModel> getData(ResultSet rs) {
        List<SecretModel> secretModels = new ArrayList<>();
        SecretModel secretModel;
        try {
//            ResultSet rs = DatabaseAccess.getData("SELECT * FROM secretdata ORDER BY ID DESC");
            int squence = 0;
            while (rs.next()) {
                squence++;
                secretModel = new SecretModel();
                secretModel.setSqunse(squence);
                secretModel.setCircularid(rs.getString("CIRCULARID"));
                secretModel.setCirculardate(rs.getString("CIRCULARDATE"));
                secretModel.setDestination(rs.getString("DESTINATION"));
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
        AppDate.setSeparateDate(circularDateDay, circularDateMonth, circularDateYear, secretModel.getCirculardate());
        destination.setValue(secretModel.getDestination());
        topic.setText(secretModel.getTopic());
        saveFile.setText(secretModel.getSaveFile());
        note.setText(secretModel.getNote());
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
            viewdata(DatabaseAccess.getData("SELECT * FROM secretdata ORDER BY ID DESC"));
        } catch (IOException ex) {
            Logger.getLogger(secretPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void viewdata(ResultSet rs) {
        secretObject.addAll(getData(rs));
        if (secretObject.size() > 0) {
            setChosendata(secretObject.get(0));
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

    public String setYear(String date) {
        return AppDate.getYear(date);
    }

    @FXML
    private void searchData(ActionEvent event) {

    }

    @FXML
    private void save(ActionEvent event) {
        String tableName = "secretdata";
        String fieldName = null;
        recordYear = Integer.toString(HijriCalendar.getSimpleYear());
        String[] data = {circularid.getText(), getCircularDate(), destination.getValue(), topic.getText(), saveFile.getText(), note.getText(), recordYear};
        String valuenumbers = null;
        if (imagefile != null) {
            fieldName = "`CIRCULARID`,`CIRCULARDATE`,`DESTINATION`,`TOPIC`,`SAVEFILE`,`NOTE`,`RECORDYEAR`,`IMAGE`";
            valuenumbers = "?,?,?,?,?,?,?,?";
        } else {
            fieldName = "`CIRCULARID`,`CIRCULARDATE`,`DESTINATION`,`TOPIC`,`SAVEFILE`,`NOTE`,`RECORDYEAR`";
            valuenumbers = "?,?,?,?,?,?,?";
        }

        boolean destinationState = FormValidation.comboBoxNotEmpty(destination, "الرجاء ادخال جهة المعاملة");
        boolean topicState = FormValidation.textFieldNotEmpty(topic, "الرجاء ادخال الموضوع");
        boolean circularidState = FormValidation.textFieldNotEmpty(circularid, "الرجاء ادخال رقم المعاملة");
        boolean circularidEexisting = FormValidation.ifexisting("secretdata", "CIRCULARID", "CIRCULARID = '" + circularid.getText() + "'AND RECORDYEAR = '" + recordYear + "'", "تم حفظ المعاملة مسبقا");
        boolean saveFileState = FormValidation.textFieldNotEmpty(saveFile, "الرجاء ادخال ملف الحفظ");

        if (destinationState && topicState && saveFileState && circularidState && circularidEexisting) {
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
    private void edit(ActionEvent event) {
        String tableName = "secretdata";
        String fieldName = null;
        recordYear = Integer.toString(HijriCalendar.getSimpleYear());
        String[] data = {circularid.getText(), getCircularDate(), destination.getValue(), topic.getText(), saveFile.getText(), note.getText(), recordYear};
        if (imagefile != null) {
            fieldName = "`CIRCULARID`=?,`CIRCULARDATE`=?,`DESTINATION`=?,`TOPIC`=?,`SAVEFILE`=?,`NOTE`=?,`RECORDYEAR`=?,`IMAGE`=?";
        } else {
            fieldName = "`CIRCULARID`=?,`CIRCULARDATE`=?,`DESTINATION`=?,`TOPIC`=?,`SAVEFILE`=?,`NOTE`=?,`RECORDYEAR`=?";
        }

        boolean destinationState = FormValidation.comboBoxNotEmpty(destination, "الرجاء ادخال جهة المعاملة");
        boolean topicState = FormValidation.textFieldNotEmpty(topic, "الرجاء ادخال الموضوع");
        boolean circularidState = FormValidation.textFieldNotEmpty(circularid, "الرجاء ادخال رقم المعاملة"); 
        boolean saveFileState = FormValidation.textFieldNotEmpty(saveFile, "الرجاء ادخال ملف الحفظ");

        if (destinationState && topicState && saveFileState && circularidState ) {
            try {
                DatabaseAccess.updat(tableName, fieldName, data,"CIRCULARID = '" + circularid.getText() + "'AND RECORDYEAR = '" + recordYear + "'", imagefile);
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
        destination.setValue(null);
        topic.setText(null);
        saveFile.setText(null);
        note.setText(null);
        refreshdata();
    }

}
