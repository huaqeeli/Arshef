package controllers;

import Validation.FormValidation;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import modeles.CoursesModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class SearchByCoursplaceAndCoursNamePageController implements Initializable {

    @FXML
    private Label coursname;
    @FXML
    private Label coursplace;
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
    @FXML
    private TableColumn<?, ?> coursname_col;

    String coursPlace = null;
    String coursId = null;
    String coursName = null;
    ObservableList<CoursesModel> coursList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void printData(ActionEvent event) {
         try {
//            String reportSrcFile = "C:\\Users\\Administrator\\Documents\\NetBeansProjects\\TrainingData\\src\\reports\\‏‏coursByPlaceAndName.jrxml";
            String reportSrcFile = "C:\\Program Files\\TrainingData\\reports\\‏‏coursByPlaceAndName.jrxml";
            Connection con = DatabaseConniction.dbConnector();

            JasperDesign jasperReport = JRXmlLoader.load(reportSrcFile);
            Map parameters = new HashMap();
            parameters.put("coursPlace", coursPlace);
            parameters.put("coursId", coursId);
            JasperReport jrr = JasperCompileManager.compileReport(jasperReport);
            JasperPrint print = JasperFillManager.fillReport(jrr, parameters, con);

//        JasperPrintManager.printReport(print, false);
            JasperViewer.viewReport(print, false);
        } catch (JRException | IOException ex) {
             FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void getExcelSheet(ActionEvent event) {
         try {
            FileChooser fileChooser = new FileChooser();
            Window stage = null;
            File file = fileChooser.showSaveDialog(stage);
            String savefile = null;
            if (file != null) {
                savefile = file.toString();
            }
            ResultSet rs = DatabaseAccess.getDatabyCoursesPlaceAndCoursName(coursPlace,coursId);
            String[] feild = {"MILITARYID", "RANK", "NAME", "UNIT", "CORSNAME"};
            String[] titel = {"الرقم العسكري", "الرتبة", "الاسم", "الوحدة", "اسم الدورة"};
            String[] coursnameandplace = {"اسماء الحاصلين على دورة :",coursName ,"في :",coursPlace};
            ExporteExcelSheet exporter = new ExporteExcelSheet();
            ArrayList<Object[]> dataList = exporter.getTableData(rs, feild);
            if (dataList != null && dataList.size() > 0) {
                exporter.ceratHeader(coursnameandplace, 0, exporter.setHederStyle());
                exporter.ceratHeader(titel, 1, exporter.setHederStyle());
                exporter.ceratContent(dataList, feild, 2, exporter.setContentStyle());
                exporter.writeFile(savefile);
            } else {
                 FormValidation.showAlert(null,"There is no data available in the table to export" , Alert.AlertType.ERROR);
            }
            rs.close();
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    void setCuoursId(String coursid,String coursplace) {
        coursPlace = coursplace;
        coursId = coursid ;
        refreshcoursesTableView();
    }

    private void refreshcoursesTableView() {
        coursList.clear();
        coursesTableView();
    }

    private void coursesTableView() {
        try {
            ResultSet rs = DatabaseAccess.getDatabyCoursesPlaceAndCoursName(coursPlace,coursId);
            int squance = 0;
            while (rs.next()) {
                squance++;
                coursList.add(new CoursesModel(
                        squance,
                        rs.getString("MILITARYID"),
                        rs.getString("RANK"),
                        rs.getString("NAME"),
                        rs.getString("UNIT"),
                        rs.getString("CORSNAME"),
                        rs.getString("COURSPLASE")
                ));
                coursname.setText(rs.getString("CORSNAME"));
                coursplace.setText(rs.getString("COURSPLASE"));
                coursPlace = rs.getString("COURSPLASE");
                coursName = rs.getString("CORSNAME");
            }
            rs.close();
        } catch (SQLException | IOException ex) {
             FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        squnce_col.setCellValueFactory(new PropertyValueFactory<>("sequence"));
        militaryid_col.setCellValueFactory(new PropertyValueFactory<>("militaryId"));
        rank_col.setCellValueFactory(new PropertyValueFactory<>("rank"));
        name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        unit_col.setCellValueFactory(new PropertyValueFactory<>("unit"));
        coursname_col.setCellValueFactory(new PropertyValueFactory<>("coursname"));

        searchTable.setItems(coursList);
    }

}
