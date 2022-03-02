
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modeles.AddNamesModel;


public class AddMissionNamesController implements Initializable {
 @FXML
    private VBox content;
    @FXML
    private TextField milataryId;
    @FXML
    private TableView<AddNamesModel> namesTable;
    @FXML
    private TableColumn<?, ?> squinse_col;
    @FXML
    private TableColumn<?, ?> milataryId_col;
    @FXML
    private TableColumn<?, ?> rank_col;
    @FXML
    private TableColumn<?, ?> name_col;
    String missionID = null;
    String year = null;
    String type = null;
    ObservableList<AddNamesModel> addnamesList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    public void close(ActionEvent event) {
        Stage stage = (Stage) content.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void save(ActionEvent event) {
        String tableName = "missionnames";
        String[] data = {getMissionID(), getMilataryId()};
        String fieldName = "`MISSIONID`,`MILITARYID`";
        String valuenumbers = "?,?";

        boolean missionidExusting = FormValidation.ifexisting("missionnames", "MILITARYID", "MISSIONID = '" + missionID + "'AND MILITARYID = '" + getMilataryId() + "'", "تم حفظ الاسم لنقس المهمة  " + "" );
        boolean militrayidExusting = FormValidation.ifNotexisting("personaldata", "MILITARYID", " MILITARYID = '" + getMilataryId() + "'", "لا توجد بيانات بالرقم العسكري (" + "" + getMilataryId() + ") الرجاء اضافة بياناته في التشكيل");
        boolean milataryIdState = FormValidation.textFieldNotEmpty(milataryId, "الرجاء ادخل الرقم العسكري");

        if (missionidExusting && milataryIdState && militrayidExusting) {
            try {
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, data);
                refreshTableView();
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    public void delete(ActionEvent event) {
        try {
            DatabaseAccess.delete("missionnames", "MISSIONID = '" + missionID + "'AND MILITARYID = '" + getMilataryId() + "'");
            refreshTableView();
            clear(event);
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

   @FXML
    public void edit(ActionEvent event) {
    }

    @FXML
    public void clear(ActionEvent event) {
        setMilataryId(null);
    }

   @FXML
    public void tableView() {
        try {
            ResultSet rs = DatabaseAccess.getData("SELECT missionnames.MILITARYID,personaldata.NAME,personaldata.MILITARYID,personaldata.RANK FROM missionnames,personaldata "
                    + "WHERE missionnames.MILITARYID = personaldata.MILITARYID AND MISSIONID = '" + missionID + "'");
            int squence = 0;
            while (rs.next()) {
                squence++;
                addnamesList.add(new AddNamesModel(
                        rs.getString("MILITARYID"),
                        rs.getString("RANK"),
                        rs.getString("NAME"),
                        squence
                ));
            }
            rs.close();
        } catch (SQLException | IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        milataryId_col.setCellValueFactory(new PropertyValueFactory<>("milataryId"));
        rank_col.setCellValueFactory(new PropertyValueFactory<>("rank"));
        name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        squinse_col.setCellValueFactory(new PropertyValueFactory<>("squinse"));

        namesTable.setItems(addnamesList);
    }

    @FXML
    public void refreshTableView() {
        addnamesList.clear();
        tableView();
    }

   @FXML
    public void getTableRow(TableView table) {
        table.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                ObservableList<AddNamesModel> list = FXCollections.observableArrayList();
                list = table.getSelectionModel().getSelectedItems();
                if (!list.isEmpty()) {
                    setMilataryId(list.get(0).getMilataryId());
                }
            }
        });
    }

   @FXML
    public void getTableRowByInterKey(TableView table) {
        table.setOnKeyPressed(new EventHandler() {
            @Override
            public void handle(Event event) {
                ObservableList<AddNamesModel> list = FXCollections.observableArrayList();
                list = table.getSelectionModel().getSelectedItems();
                if (!list.isEmpty()) {
                    setMilataryId(list.get(0).getMilataryId());
                }
            }
        });
    }

    public String getMilataryId() {
        return milataryId.getText();
    }

    public void setMilataryId(String milataryId) {
        this.milataryId.setText(milataryId);
    }

    public String getMissionID() {
        return missionID;
    }

    public void setMissionID(String missionID) {
        this.missionID = missionID;
    }

    public void setPassingData(String missionID) {
        this.missionID = missionID;
        refreshTableView();
        getTableRow(namesTable);
        getTableRowByInterKey(namesTable);
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    
}
