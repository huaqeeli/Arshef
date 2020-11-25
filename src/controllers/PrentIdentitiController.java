package controllers;

import Validation.FormValidation;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import modeles.CoursesModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class PrentIdentitiController implements Initializable {

    @FXML
    private ImageView personalImage;
    @FXML
    private Label name;
    @FXML
    private Label militaryid;
    @FXML
    private Label rank;
    @FXML
    private Label unit;
    @FXML
    private Label idnumber;
    @FXML
    private TableView<CoursesModel> tableView;
    @FXML
    private TableColumn<?, ?> squance_col;
    @FXML
    private TableColumn<?, ?> coursname_col;
    String milatryId = null;
    ObservableList<CoursesModel> coursList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        personalImage.setStyle("-fx-border-style: null;"
                + "    -fx-border-color:#000;"
                + "    -fx-border-width: 1;");
    }

    public void setMiltaryId(String milataryid) {
        milatryId = milataryid;
        refreshcoursesTableView(milatryId);
    }

    private void refreshcoursesTableView(String miliid) {
        coursList.clear();
        coursesTableView();
    }

    private void coursesTableView() {
        try {
            String query = "SELECT personaldata.MILITARYID,coursnames.CORSNAME FROM personaldata,coursesdata,coursnames"
                    + " WHERE personaldata.MILITARYID =  '" + milatryId + "' And personaldata.MILITARYID = coursesdata.MILITARYID AND coursesdata.COURSID = coursnames.COURSID";
            String query1 = "SELECT MILITARYID,NAME,RANK,UNIT,PERSONALID,PERSONALIMAGE FROM personaldata"
                    + " WHERE MILITARYID =  '" + milatryId + "' ";
            ResultSet rs = DatabaseAccess.getIdentiti(query);
            ResultSet rs1 = DatabaseAccess.getIdentiti(query1);
            if (rs1.next()) {
                militaryid.setText(rs1.getString("MILITARYID"));
                name.setText(rs1.getString("NAME"));
                rank.setText(rs1.getString("RANK"));
                unit.setText(rs1.getString("UNIT"));
                idnumber.setText(rs1.getString("PERSONALID"));
                InputStream is = rs1.getBinaryStream("PERSONALIMAGE");
                if (is != null) {
                    OutputStream os = new FileOutputStream(new File("photo.jpg"));
                    byte[] content = new byte[1024];
                    int size = 0;
                    while ((size = is.read(content)) != -1) {
                        os.write(content, 0, size);
                    }
                    os.close();
                    is.close();
                    Image imagex = new Image("file:photo.jpg", 130, 160, true, true);
                    personalImage.setImage(imagex);
                } else {
                    personalImage.setImage(null);
                }
            }
            int squance = 0;
            while (rs.next()) {
                squance++;
                coursList.add(new CoursesModel(
                        squance,
                        rs.getString("coursnames.CORSNAME")
                ));

            }
            rs.close();
            rs1.close();
        } catch (SQLException | IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        squance_col.setCellValueFactory(new PropertyValueFactory<>("sequence"));
        coursname_col.setCellValueFactory(new PropertyValueFactory<>("coursname"));
        tableView.setItems(coursList);
    }

    @FXML
    private void printData(ActionEvent event) {
        try {
            String reportSrcFile = "C:\\Program Files\\TrainingData\\reports\\Identity.jrxml";
//            String reportSrcFile = "C:\\Users\\ابو ريان\\Documents\\NetBeansProjects\\TrainingData\\src\\reports\\Identity.jrxml";
            Connection con = DatabaseConniction.dbConnector();

            JasperDesign jasperReport = JRXmlLoader.load(reportSrcFile);
            Map parameters = new HashMap();
            parameters.put("militaryid", milatryId);

            JasperReport jrr = JasperCompileManager.compileReport(jasperReport);
            JasperPrint print = JasperFillManager.fillReport(jrr, parameters, con);

//        JasperPrintManager.printReport(print, false);
            JasperViewer.viewReport(print, false);
        } catch (JRException | IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }
}
