package controllers;

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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import modeles.CoursesModel;

public class SearchByCoursNameController implements Initializable {

    @FXML
    private Label coursname;
    @FXML
    private TableView<CoursesModel> searchTable;
    @FXML
    private TableColumn<?, ?> squnce_col;
    @FXML
    private TableColumn<?, ?> militaryid_col;
    @FXML
    private TableColumn<?, ?> rank_col;
    @FXML
    private TableColumn<?, ?> name_col;
    @FXML
    private TableColumn<?, ?> unit_col;
    String coursId = null;
    String coursName = null;
    ObservableList<CoursesModel> coursList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void printData(ActionEvent event) {
    }

    @FXML
    private void getExcelSheet(ActionEvent event) {
    }

    void setCuoursId(String coursid,String coursname) {
        coursId = coursid;
        coursName=coursname;
        refreshcoursesTableView();
    }

    private void refreshcoursesTableView() {
        coursList.clear();
        coursesTableView();
    }

    private void coursesTableView() {
        try {
            ResultSet rs = DatabaseAccess.getDatabyCoursesId(coursId);
            int squance = 0;
            while (rs.next()) {
                squance++;
                coursList.add(new CoursesModel(
                        squance,
                        rs.getString("personaldata.MILITARYID"),
                         rs.getString("personaldata.RANK"),
                        rs.getString("personaldata.NAME"),
                        rs.getString("personaldata.UNIT")
                ));
                coursname.setText(coursName);
            }
            rs.close();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(PersonalDataPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        squnce_col.setCellValueFactory(new PropertyValueFactory<>("sequence"));
        militaryid_col.setCellValueFactory(new PropertyValueFactory<>("militaryId"));
        rank_col.setCellValueFactory(new PropertyValueFactory<>("rank"));
        name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        unit_col.setCellValueFactory(new PropertyValueFactory<>("unit"));
        searchTable.setItems(coursList);
    }

}
