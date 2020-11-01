package controllers;

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

public class SearchByCoursPlaceController implements Initializable {

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
    ObservableList<CoursesModel> coursList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void printData(ActionEvent event) {
        try {
//            String reportSrcFile = "C:\\Users\\Administrator\\Documents\\NetBeansProjects\\TrainingData\\src\\reports\\coursByPlace.jrxml";
            String reportSrcFile = "C:\\Users\\ابو ريان\\Documents\\NetBeansProjects\\TrainingData\\src\\reports\\coursByPlace.jrxml";
            Connection con = DatabaseConniction.dbConnector();

            JasperDesign jasperReport = JRXmlLoader.load(reportSrcFile);
            Map parameters = new HashMap();
            parameters.put("coursPlace", coursPlace);
            JasperReport jrr = JasperCompileManager.compileReport(jasperReport);
            JasperPrint print = JasperFillManager.fillReport(jrr, parameters, con);

//        JasperPrintManager.printReport(print, false);
            JasperViewer.viewReport(print, false);
        } catch (JRException | IOException ex) {
            Logger.getLogger(SearchByMiltaryIdController.class.getName()).log(Level.SEVERE, null, ex);
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
            ResultSet rs = DatabaseAccess.getDatabyCoursesPlace(coursPlace);
            String[] feild = {"MILITARYID", "RANK", "NAME", "UNIT", "CORSNAME"};
            String[] titel = {"الرقم العسكري", "الرتبة", "الاسم", "الوحدة", "اسم الدورة"};
            String[] coursname = {"اسماء الحاصلين على دورة في :", coursPlace};
            ExporteExcelSheet exporter = new ExporteExcelSheet();
            ArrayList<Object[]> dataList = exporter.getTableData(rs, feild);
            if (dataList != null && dataList.size() > 0) {
                exporter.ceratHeader(coursname, 0, exporter.setHederStyle());
                exporter.ceratHeader(titel, 1, exporter.setHederStyle());
                exporter.ceratContent(dataList, feild, 2, exporter.setContentStyle());
                exporter.writeFile(savefile);
            } else {
                System.out.println("There is no data available in the table to export");
            }
            rs.close();
        } catch (IOException ex) {
            Logger.getLogger(SearchByCoursPlaceController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SearchByCoursPlaceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void setCuoursId(String coursplace) {
        coursPlace = coursplace;
        refreshcoursesTableView();
    }

    private void refreshcoursesTableView() {
        coursList.clear();
        coursesTableView();
    }

    private void coursesTableView() {
        try {
            ResultSet rs = DatabaseAccess.getDatabyCoursesPlace(coursPlace);
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
                coursplace.setText(rs.getString("COURSPLASE"));
                coursPlace = rs.getString("COURSPLASE");
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
        coursname_col.setCellValueFactory(new PropertyValueFactory<>("coursname"));

        searchTable.setItems(coursList);
    }

}
