package controllers;

import Validation.FormValidation;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modeles.CourrsNamesModel;
import modeles.CoursesModel;

public class AddNewCoursNameController implements Initializable {

    @FXML
    private TextField newcoursname;
    String coursID = null;
    @FXML
    private TableView<CourrsNamesModel> coursnamesTable;
    @FXML
    private TableColumn<?, ?> coursId_col;
    @FXML
    private TableColumn<?, ?> coursname_col;
    ObservableList<CourrsNamesModel> coursList = FXCollections.observableArrayList();
    @FXML
    private VBox content;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        refreshcoursnamesTableView();
        getTableRow(coursnamesTable);
        getTableRowByInterKey(coursnamesTable);
    }

    @FXML
    private void coursnameSave(ActionEvent event) {
        String tableName = "coursnames";
        String fieldName = "`CORSNAME`";
        String[] data = {getNewcoursname()};
        String valuenumbers = "?";
        boolean newcoursnameState = FormValidation.textFieldNotEmpty(newcoursname, "الرجاء ادخال مسمى الدورة");
        if (newcoursnameState) {
            try {
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, data);
                refreshcoursnamesTableView();
                setNewcoursname(null);
            } catch (IOException ex) {
                Logger.getLogger(AddNewCoursNameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void coursnameEdit(ActionEvent event) {
        String tableName = "coursnames";
        String fieldName = "`CORSNAME`=?";
        String[] data = {getNewcoursname()};
        boolean newcoursnameState = FormValidation.textFieldNotEmpty(newcoursname, "الرجاء ادخال مسمى الدورة");
        boolean newcoursidState = FormValidation.notNull(coursID, "الرجاء اختر مسمى الدورة من القائمة اعلاه");
        if (newcoursnameState && newcoursidState) {
            try {
                DatabaseAccess.updat(tableName, fieldName, data, "COURSID = '" + coursID + "'");
                refreshcoursnamesTableView();
            } catch (IOException ex) {
                Logger.getLogger(PersonalDataPageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void coursnameDelete(ActionEvent event) {
        boolean newcoursidState = FormValidation.notNull(coursID, "الرجاء اختر مسمى الدورة من القائمة اعلاه");
        if (newcoursidState) {
            try {
                DatabaseAccess.delete("coursnames", "COURSID = '" + coursID + "' ");
                refreshcoursnamesTableView();
            } catch (IOException ex) {
                Logger.getLogger(PersonalDataPageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setCoursId(String coursID) {
        this.coursID = coursID;
    }

    public String getNewcoursname() {
        return newcoursname.getText();
    }

    public void setNewcoursname(String newcoursname) {
        this.newcoursname.setText(newcoursname);
    }

    private void coursnamesTableView() {
        try {
            ResultSet rs = DatabaseAccess.select("coursnames");
            while (rs.next()) {
                coursList.add(new CourrsNamesModel(
                        rs.getString("COURSID"),
                        rs.getString("CORSNAME")
                ));
            }
            rs.close();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(PersonalDataPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        coursId_col.setCellValueFactory(new PropertyValueFactory<>("coursid"));
        coursname_col.setCellValueFactory(new PropertyValueFactory<>("coursname"));

        coursnamesTable.setItems(coursList);
    }

    public void getTableRow(TableView table) {
        table.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                ObservableList<CourrsNamesModel> list = FXCollections.observableArrayList();
                list = table.getSelectionModel().getSelectedItems();
                if (list.isEmpty()) {
                    FormValidation.showAlert("", "لاتوجد بيانات");
                } else {
                    newcoursname.setText(list.get(0).getCoursname());
                    coursID = list.get(0).getCoursid();
                }
            }
        });
    }

    private void getTableRowByInterKey(TableView table) {
        table.setOnKeyPressed(new EventHandler() {
            @Override
            public void handle(Event event) {
                ObservableList<CourrsNamesModel> list = FXCollections.observableArrayList();
                list = table.getSelectionModel().getSelectedItems();
                if (list.isEmpty()) {
                    FormValidation.showAlert("", "لاتوجد بيانات");
                } else {
                    newcoursname.setText(list.get(0).getCoursname());
                    coursID = list.get(0).getCoursid();
                }
            }
        });
    }

    private void refreshcoursnamesTableView() {
        coursList.clear();
        coursnamesTableView();
    }

    @FXML
    private void close(ActionEvent event) {
        Stage stage = (Stage) content.getScene().getWindow();
        stage.close();
    }
}
