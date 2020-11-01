package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            String reportSrcFile = "C:\\Users\\Administrator\\Documents\\NetBeansProjects\\TrainingData\\src\\reports\\coursByName.jrxml";
//            String reportSrcFile = "C:\\Users\\ابو ريان\\Documents\\NetBeansProjects\\TrainingData\\src\\reports\\courseByid.jrxml";
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
            Logger.getLogger(SearchByMiltaryIdController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
                        rs.getString("personaldata.UNIT"),
                        rs.getString("coursesdata.COURSPLASE")
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
        coursplace_col.setCellValueFactory(new PropertyValueFactory<>("coursplace"));
        
        searchTable.setItems(coursList);
    }

}
