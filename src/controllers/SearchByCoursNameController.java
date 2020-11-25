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
import java.util.logging.Level;
import java.util.logging.Logger;
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
    @FXML
    private TableColumn<?, ?> coursplace_col;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void printData(ActionEvent event) {
         try {
//            String reportSrcFile = "C:\\Users\\Administrator\\Documents\\NetBeansProjects\\TrainingData\\src\\reports\\coursByName.jrxml";
            String reportSrcFile = "C:\\Program Files\\TrainingData\\reports\\coursByName.jrxml";
            Connection con = DatabaseConniction.dbConnector();

            JasperDesign jasperReport = JRXmlLoader.load(reportSrcFile);
            Map parameters = new HashMap();
            parameters.put("coursId", coursId);
            parameters.put("coursName", coursName);

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
            ResultSet rs = DatabaseAccess.getDatabyCoursesId(coursId);
            String[] feild = {"MILITARYID", "RANK", "NAME", "UNIT", "COURSPLASE"};
            String[] titel = {"الرقم العسكري", "الرتبة", "الاسم", "الوحدة", "مكان انعقادها"};
            String[] coursname = {"اسماء الحاصلين على دورة :",coursName};
            ExporteExcelSheet exporter = new ExporteExcelSheet();
            ArrayList<Object[]> dataList = exporter.getTableData(rs,feild);
            if (dataList != null && dataList.size() > 0) {
                exporter.ceratHeader(coursname, 0, exporter.setHederStyle());
                exporter.ceratHeader(titel, 1, exporter.setHederStyle());
                exporter.ceratContent(dataList,feild, 2, exporter.setContentStyle());
                exporter.writeFile(savefile);
            } else {
                FormValidation.showAlert(null, "There is no data available in the table to export", Alert.AlertType.ERROR);
            }
            rs.close();
        } catch (IOException | SQLException ex) {
           FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
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
                        rs.getString("personaldata.UNIT"),
                        rs.getString("coursesdata.COURSPLASE")
                ));
                coursname.setText(coursName);
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
        coursplace_col.setCellValueFactory(new PropertyValueFactory<>("coursplace"));
        
        searchTable.setItems(coursList);
    }

}
