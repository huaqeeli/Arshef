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

public class AddNamesController implements Initializable, InitializClass {

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
    @FXML
    private TableColumn<?, ?> unit_col;
    String circularID = null;
    String year = null;
    String type = null;
    ObservableList<AddNamesModel> addnamesList = FXCollections.observableArrayList();
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @Override
    @FXML
    public void close(ActionEvent event) {
        Stage stage = (Stage) content.getScene().getWindow();
        stage.close();
    }

    @Override
    @FXML
    public void save(ActionEvent event) {
        String tableName = "circularnames";
        String[] data = {getCircularID(), getMilataryId(), year,type};
        String fieldName = "`CIRCULARID`,`MILITARYID`,`YEAR`,`type`";
        String valuenumbers = "?,?,?,?";

        boolean uintExusting = FormValidation.ifexisting("circularnames", "MILITARYID", "CIRCULARID = '" + circularID + "'AND MILITARYID = '" + getMilataryId() + "'AND YEAR = '" + year + "'AND type = '"+type+"'", "تم حفظ الاسم لنقس المعاملةرقم " + "" + getCircularID());
        boolean militrayidExusting = FormValidation.ifNotexisting("personaldata", "MILITARYID", " MILITARYID = '" + getMilataryId() + "'", "لا توجد بيانات بالرقم العسكري (" + "" + getMilataryId() + ") الرجاء اضافة بياناته في التشكيل");
        boolean OFcensusState = FormValidation.textFieldNotEmpty(milataryId, "الرجاء ادخل الرقم العسكري");

        if (uintExusting && OFcensusState && militrayidExusting) {
            try {
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, data);
                refreshTableView();
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @Override
    @FXML
    public void delete(ActionEvent event) {
        try {
            DatabaseAccess.delete("circularnames", "CIRCULARID = '" + circularID + "'AND MILITARYID = '" + getMilataryId() + "'AND YEAR = '" + year + "'");
            refreshTableView();
            clear(event);
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @Override
    public void edit(ActionEvent event) {
    }

    @Override
    public void clear(ActionEvent event) {
        setMilataryId(null);
    }

    @Override
    public void tableView() {
        try {
            ResultSet rs = DatabaseAccess.getData("SELECT circularnames.YEAR,circularnames.MILITARYID,personaldata.NAME,personaldata.MILITARYID,personaldata.RANK,personaldata.UNIT FROM circularnames,personaldata "
                    + "WHERE circularnames.MILITARYID = personaldata.MILITARYID AND CIRCULARID = '" + circularID + "'AND YEAR = '" + year + "'AND type = '"+type+"' ");
            int squence = 0;
            while (rs.next()) {
                squence++;
                addnamesList.add(new AddNamesModel(
                        rs.getString("MILITARYID"),
                        rs.getString("RANK"),
                        rs.getString("NAME"),
                        rs.getString("UNIT"),
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
        unit_col.setCellValueFactory(new PropertyValueFactory<>("unit"));

        namesTable.setItems(addnamesList);
    }

    @Override
    public void refreshTableView() {
        addnamesList.clear();
        tableView();
    }

    @Override
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

    @Override
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

    public String getCircularID() {
        return circularID;
    }

    public void setCircularID(String circularID) {
        this.circularID = circularID;
    }

    public void setPassingData(String circularID, String year,String type) {
        this.circularID = circularID;
        this.year = year;
        this.type = type;
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
