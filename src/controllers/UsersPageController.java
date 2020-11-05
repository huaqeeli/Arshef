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
    private TableColumn<?, ?> name_col;
     @FXML
    private TableColumn<?, ?> nationalID_col;
    @FXML
    private TableColumn<?, ?> userType_col;
    @FXML
    private TableColumn<?, ?> squnce_col;

    ObservableList<UserModel> userlist = FXCollections.observableArrayList();
    ObservableList<String> usertypeItem = FXCollections.observableArrayList("مسئول النضام", "مستخدم عادي");
    String militaryid = null;
    
    @FXML
    private TextField miliataryid;
   

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FillComboBox.fillComboBox(usertypeItem,userType );
        refreshUsertable();

        
    }
  

    public void clearField() {
        miliataryid.setText(null);
        userType.setValue(null);
    }

    @FXML
    private void saveData(ActionEvent event) {
        String tabelNme = "userdata";
        String fieldName = "`MILITARYID`,`USERTYPE`,`USERNAME`,`PASSWORD`,`PASSWORDSTATE`";
        String[] data = {miliataryid.getText(), userType.getValue(), miliataryid.getText(), "123456","default"};
        String valuenumbers = "?,?,?,?,?";
      
        boolean miliataryidstatus = FormValidation.textFieldNotEmpty(miliataryid, "ادخل الرقم العسكري");
        boolean miliataryidOnly = FormValidation.textFieldTypeNumber(miliataryid, "ادخل ارقام فقط");
        boolean userTypestatus = FormValidation.comboBoxNotEmpty(userType, "اختر نوع المستخدم");
        boolean miliataryidExisting = FormValidation.ifNotexisting("personaldata", "MILITARYID","MILITARYID = '"+miliataryid.getText()+"'","لا يوجد له بيانات الرجاء اضافة البيانات اولا");
      

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
                DatabaseAccess.updat(tabelNme, fieldName, data, "MILITARYID = '" + miliataryid + "'");
                refreshUsertable();
                clearField();
            } catch (IOException ex) {
                 FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void deleteData(ActionEvent event) {
        String tabelNme = "users";
        try {
            DatabaseAccess.delete(tabelNme, "MILITARYID = '" + miliataryid + "'");
            refreshUsertable();
            clearField();
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    private void userTableView() {
        try {
            ResultSet rs = DatabaseAccess.getData("SELECT personaldata.MILITARYID,personaldata.NAME,personaldata.PERSONALID,userdata.USERTYPE FROM personaldata,userdata WHERE personaldata.MILITARYID = userdata.MILITARYID" );
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

    private void refreshUsertable() {
        userlist.clear();
        userTableView();
    }
}
