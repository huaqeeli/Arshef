package controllers;

import Validation.FormValidation;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import modeles.UserModel;

public class UsersPageController implements Initializable {

    @FXML
    private ComboBox<String> userType;
    @FXML
    private TableView<UserModel> userTable;
    @FXML
    private TableColumn<?, ?> militaryid_col;
    @FXML
    private TableColumn<?, ?> rank_col;
    @FXML
    private TableColumn<?, ?> name_col;
    @FXML
    private TableColumn<?, ?> userType_col;
    @FXML
    private TableColumn<?, ?> squnce_col;

    ObservableList<UserModel> userlist = FXCollections.observableArrayList();
    ObservableList<String> usertypeItem = FXCollections.observableArrayList("مدير", "مستخدم");
    ObservableList<String> rankComboBoxlist = FXCollections.observableArrayList("فريق اول", "فريق", "لواء", "عميد", "عقيد", "مقدم", "رائد", "نقيب", "ملازم أول", "ملازم", "رئيس رقباء", "رقيب أول", "رقيب", "وكيل رقيب", "عريف", "جندي أول", "جندي");
    String selectedmilitaryid = null;

    @FXML
    private TextField miliataryid;
    @FXML
    private ComboBox<String> rank;
    @FXML
    private TextField name;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FillComboBox.fillComboBox(usertypeItem, userType);
        FillComboBox.fillComboBox(rankComboBoxlist, rank);
        refreshUsertable();
        getTableRow(userTable);
        getTableRowByInterKey(userTable);
    }

    public void clearField() {
        miliataryid.setText(null);
        userType.setValue(null);
    }

    @FXML
    private void saveData(ActionEvent event) {
        String tabelNme = "userdata";
        String fieldName = "`MILITARYID`,`RANK`,`NAME`,`USERTYPE`,`USERNAME`,`PASSWORD`,`PASSWORDSTATE`";
        String[] data = {miliataryid.getText(),rank.getValue(),name.getText(), userType.getValue(), miliataryid.getText(), "123456", "default"};
        String valuenumbers = "?,?,?,?,?,?,?";

        boolean miliataryidstatus = FormValidation.textFieldNotEmpty(miliataryid, "ادخل الرقم العسكري");
        boolean namestatus = FormValidation.textFieldNotEmpty(name, "ادخل الاسم رباعي");
        boolean miliataryidOnly = FormValidation.textFieldTypeNumber(miliataryid, "ادخل ارقام فقط");
        boolean userTypestatus = FormValidation.comboBoxNotEmpty(userType, "اختر نوع المستخدم");
        boolean rankstatus = FormValidation.comboBoxNotEmpty(rank, "اختر الرتبة");
        

        if (miliataryidstatus && userTypestatus && miliataryidOnly && rankstatus && namestatus) {
            try {
                DatabaseAccess.insert(tabelNme, fieldName, valuenumbers, data);
                refreshUsertable();
                clearField();
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void editData(ActionEvent event) throws SQLException {
        String tabelNme = "userdata";
        String fieldName = "`MILITARYID`=?,`RANK`=?,`NAME`=?,`USERTYPE`=?";
        String[] data = {miliataryid.getText(),rank.getValue(),name.getText(), userType.getValue()};

        boolean miliataryidstatus = FormValidation.textFieldNotEmpty(miliataryid, "ادخل الرقم العسكري");
        boolean miliataryidOnly = FormValidation.textFieldTypeNumber(miliataryid, "ادخل ارقام فقط");
        boolean userTypestatus = FormValidation.comboBoxNotEmpty(userType, "اختر نوع المستخدم");

        if (miliataryidstatus && userTypestatus && miliataryidOnly) {
            try {
                DatabaseAccess.updat(tabelNme, fieldName, data, "MILITARYID = '" + selectedmilitaryid + "'");
                refreshUsertable();
                clearField();
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void deleteData(ActionEvent event) throws SQLException {
        String tabelNme = "userdata";
        try {
            DatabaseAccess.delete(tabelNme, "MILITARYID = '" + selectedmilitaryid + "'");
            refreshUsertable();
            clearField();
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    private void userTableView() {
        try {
            ResultSet rs = DatabaseAccess.select("userdata");
            int squnce = 0;
            while (rs.next()) {
                squnce++;
                userlist.add(new UserModel(
                        rs.getString("MILITARYID"),
                        rs.getString("RANK"),
                        rs.getString("NAME"),
                        rs.getString("USERTYPE"),
                        squnce
                ));
            }
            rs.close();
        } catch (SQLException | IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        militaryid_col.setCellValueFactory(new PropertyValueFactory<>("militaryid"));
        name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        rank_col.setCellValueFactory(new PropertyValueFactory<>("rank"));
        userType_col.setCellValueFactory(new PropertyValueFactory<>("usertype"));
        squnce_col.setCellValueFactory(new PropertyValueFactory<>("squnce"));

        userTable.setItems(userlist);
    }

    public void getTableRow(TableView table) {
        table.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                ObservableList<UserModel> list = FXCollections.observableArrayList();
                list = table.getSelectionModel().getSelectedItems();
                if (!list.isEmpty()) {
                    selectedmilitaryid = list.get(0).getMilitaryid();
                    miliataryid.setText(list.get(0).getMilitaryid());
                    userType.setValue(list.get(0).getUsertype());
                    rank.setValue(list.get(0).getRank());
                    name.setText(list.get(0).getName());
                }
            }
        });
    }

    private void getTableRowByInterKey(TableView table) {
        table.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                ObservableList<UserModel> list = FXCollections.observableArrayList();
                list = table.getSelectionModel().getSelectedItems();
                if (!list.isEmpty()) {
                    selectedmilitaryid = list.get(0).getMilitaryid();
                    miliataryid.setText(list.get(0).getMilitaryid());
                    userType.setValue(list.get(0).getUsertype());
                    rank.setValue(list.get(0).getRank());
                    name.setText(list.get(0).getName());
                }
            }
        });
    }

    private void refreshUsertable() {
        userlist.clear();
        userTableView();
    }

}
