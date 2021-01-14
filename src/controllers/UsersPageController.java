package controllers;

import Validation.FormValidation;
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
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class UsersPageController implements Initializable {

    @FXML
    private ComboBox<String> userType;
    @FXML
    private TableView<UserModel> userTable;
    @FXML
    private TableColumn<?, ?> militaryid_col;
    @FXML
    private TableColumn<?, ?> name_col;
    @FXML
    private TableColumn<?, ?> nationalID_col;
    @FXML
    private TableColumn<?, ?> userType_col;
    @FXML
    private TableColumn<?, ?> squnce_col;

    ObservableList<UserModel> userlist = FXCollections.observableArrayList();
    ObservableList<String> usertypeItem = FXCollections.observableArrayList("مدير", "مستخدم");
    String selectedmilitaryid = null;

    @FXML
    private TextField miliataryid;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FillComboBox.fillComboBox(usertypeItem, userType);
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
        String fieldName = "`MILITARYID`,`USERTYPE`,`USERNAME`,`PASSWORD`,`PASSWORDSTATE`";
        String[] data = {miliataryid.getText(), userType.getValue(), miliataryid.getText(), "123456", "default"};
        String valuenumbers = "?,?,?,?,?";

        boolean miliataryidstatus = FormValidation.textFieldNotEmpty(miliataryid, "ادخل الرقم العسكري");
        boolean miliataryidOnly = FormValidation.textFieldTypeNumber(miliataryid, "ادخل ارقام فقط");
        boolean userTypestatus = FormValidation.comboBoxNotEmpty(userType, "اختر نوع المستخدم");
        boolean miliataryidExisting = FormValidation.ifNotexisting("personaldata", "MILITARYID", "MILITARYID = '" + miliataryid.getText() + "'", "لا يوجد له بيانات الرجاء اضافة البيانات اولا");

        if (miliataryidstatus && userTypestatus && miliataryidOnly && miliataryidExisting) {
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
    private void editData(ActionEvent event) {
        String tabelNme = "userdata";
        String fieldName = "`MILITARYID`=?,`USERTYPE`=?";
        String[] data = {miliataryid.getText(), userType.getValue()};

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
    private void deleteData(ActionEvent event) {
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
            ResultSet rs = DatabaseAccess.getData("SELECT personaldata.MILITARYID,personaldata.NAME,personaldata.PERSONALID,userdata.USERTYPE FROM personaldata,userdata WHERE personaldata.MILITARYID = userdata.MILITARYID");
            int squnce = 0;
            while (rs.next()) {
                squnce++;
                userlist.add(new UserModel(
                        rs.getString("MILITARYID"),
                        rs.getString("NAME"),
                        rs.getString("PERSONALID"),
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
        nationalID_col.setCellValueFactory(new PropertyValueFactory<>("nationalid"));
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
                }
            }
        });
    }

    private void refreshUsertable() {

        userlist.clear();
        userTableView();
    }

    @FXML
    private void countingReport(ActionEvent event) {
         try {
//            String reportSrcFile = "C:\\Users\\ابو ريان\\Documents\\NetBeansProjects\\TrainingData\\src\\reports\\countingReport.jrxml";
            String reportSrcFile = "C:\\Program Files\\TrainingData\\reports\\countingReport.jrxml";
            Connection con = DatabaseConniction.dbConnector();

            JasperDesign jasperReport = JRXmlLoader.load(reportSrcFile);
            Map parameters = new HashMap();
//            parameters.put("milataryId", "");

            JasperReport jrr = JasperCompileManager.compileReport(jasperReport);
            JasperPrint print = JasperFillManager.fillReport(jrr, null, con);

//        JasperPrintManager.printReport(print, false);
            JasperViewer.viewReport(print, false);
        } catch (JRException | IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void oprationReport(ActionEvent event) {
         try {
//            String reportSrcFile = "C:\\Users\\ابو ريان\\Documents\\NetBeansProjects\\TrainingData\\src\\reports\\oprationReport.jrxml";
            String reportSrcFile = "C:\\Program Files\\TrainingData\\reports\\oprationReport.jrxml";
            Connection con = DatabaseConniction.dbConnector();

            JasperDesign jasperReport = JRXmlLoader.load(reportSrcFile);
            Map parameters = new HashMap();
//            parameters.put("milataryId", "");

            JasperReport jrr = JasperCompileManager.compileReport(jasperReport);
            JasperPrint print = JasperFillManager.fillReport(jrr, null, con);

//        JasperPrintManager.printReport(print, false);
            JasperViewer.viewReport(print, false);
        } catch (JRException | IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }
}
