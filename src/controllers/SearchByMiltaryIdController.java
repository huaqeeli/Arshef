package controllers;

import com.itextpdf.text.BadElementException;
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
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Callback;
import modeles.CoursesModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class SearchByMiltaryIdController implements Initializable {

    String milatryId = null;
    @FXML
    private Label milataryid;
    @FXML
    private Label name;
    @FXML
    private Label rank;
    @FXML
    private Label unit;
    @FXML
    private TableView<CoursesModel> searchTable;
    @FXML
    private TableColumn<?, ?> squnce_col;
    @FXML
    private TableColumn<?, ?> corsname_col;
    @FXML
    private TableColumn<?, ?> coursnum_col;
    @FXML
    private TableColumn<?, ?> coursplace_col;
    @FXML
    private TableColumn<?, ?> coursDuration_col;
    @FXML
    private TableColumn<?, ?> startdate_col;
    @FXML
    private TableColumn<?, ?> enddate_col;
    @FXML
    private TableColumn<?, ?> estimate_col;
    @FXML
    private TableColumn<CoursesModel, String> image_col;
    ObservableList<CoursesModel> coursList = FXCollections.observableArrayList();
    com.itextpdf.text.Image pdfimage = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setMiltaryId(String milataryid) {
        milatryId = milataryid;
        getTableRow(searchTable);
        refreshcoursesTableView(milatryId);
    }

    public String getMilataryid() {
        return milataryid.getText();
    }

    public void setMilataryid(String milataryid) {
        this.milataryid.setText(milatryId);
    }

    public String getName() {
        return name.getText();
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public String getRank() {
        return rank.getText();
    }

    public void setRank(String rank) {
        this.rank.setText(rank);
    }

    public String getUnit() {
        return unit.getText();
    }

    public void setUnit(String unit) {
        this.unit.setText(unit);
    }

    private void refreshcoursesTableView(String miliid) {
        coursList.clear();
        coursesTableView(miliid);
    }

    private void coursesTableView(String miliid) {
        try {
            ResultSet rs = DatabaseAccess.getCourses(miliid);
            int squance = 0;
            while (rs.next()) {
                squance++;
                coursList.add(new CoursesModel(
                        squance,
                        rs.getString("coursnames.CORSNAME"),
                        rs.getString("coursesdata.COURSNUMBER"),
                        rs.getString("coursesdata.COURSPLASE"),
                        rs.getString("coursesdata.COURSDURATION"),
                        rs.getString("coursesdata.STARTDATE"),
                        rs.getString("coursesdata.ENDDATE"),
                        rs.getString("coursesdata.COURSESTIMATE")
                ));
                milataryid.setText(rs.getString("personaldata.MILITARYID"));
                name.setText(rs.getString("personaldata.NAME"));
                rank.setText(rs.getString("personaldata.RANK"));
                unit.setText(rs.getString("personaldata.UNIT"));
            }
            rs.close();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(PersonalDataPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        squnce_col.setCellValueFactory(new PropertyValueFactory<>("sequence"));
        corsname_col.setCellValueFactory(new PropertyValueFactory<>("coursname"));
        coursnum_col.setCellValueFactory(new PropertyValueFactory<>("coursNumber"));
        coursplace_col.setCellValueFactory(new PropertyValueFactory<>("coursplace"));
        coursDuration_col.setCellValueFactory(new PropertyValueFactory<>("coursDuration"));
        startdate_col.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        enddate_col.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        estimate_col.setCellValueFactory(new PropertyValueFactory<>("estimate"));
        Callback<TableColumn<CoursesModel, String>, TableCell<CoursesModel, String>> cellFactory
                = (final TableColumn<CoursesModel, String> param) -> {
                    final TableCell<CoursesModel, String> cell = new TableCell<CoursesModel, String>() {

                final Button btn = new Button();

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        btn.setOnAction(event -> {
                            try {
                                ShowPdf.writePdf(pdfimage);
                                pdfimage = null;
                            } catch (Exception ex) {
                                Logger.getLogger(TrainingDataPageController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                        btn.setStyle("-fx-font-family: 'URW DIN Arabic';"
                                + "    -fx-font-size: 10px;"
                                + "    -fx-background-color: #769676;"
                                + "    -fx-background-radius: 10;"
                                + "    -fx-text-fill: #FFFFFF;"
                                + "    -fx-effect: dropshadow(three-pass-box,#3C3B3B, 20, 0, 5, 5); ");
                        Image image = new Image("/images/pdf.png");
                        ImageView view = new ImageView(image);
                        btn.setGraphic(view);
                        setGraphic(btn);
                        setText(null);
                    }
                }
            };
                    return cell;
                };
        image_col.setCellFactory(cellFactory);

        searchTable.setItems(coursList);
    }

    public void getTableRow(TableView table) {
        table.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                ObservableList<CoursesModel> list = FXCollections.observableArrayList();
                list = table.getSelectionModel().getSelectedItems();
                if (!list.isEmpty()) {
                    try {
                        String coursID = list.get(0).getCoursId(list.get(0).getCoursname());
                        ResultSet rs = DatabaseAccess.select("coursesdata", "MILITARYID = '" + milatryId + "'AND COURSID = '" + coursID + "'");
                        if (rs.next()) {
                            ArrayList images = new ArrayList();
                            images.add(rs.getBytes("COURSIMAGE"));
                            byte[] scaledInstance = (byte[]) images.get(0);
                            pdfimage = com.itextpdf.text.Image.getInstance(scaledInstance);
                        }
                    } catch (SQLException | IOException | BadElementException ex) {
                        Logger.getLogger(TrainingDataPageController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }

    @FXML
    private void printData(ActionEvent event) {
        try {
            String reportSrcFile = "C:\\Users\\Administrator\\Documents\\NetBeansProjects\\TrainingData\\src\\reports\\courseByid.jrxml";
            Connection con = DatabaseConniction.dbConnector();

            JasperDesign jasperReport = JRXmlLoader.load(reportSrcFile);
            Map parameters = new HashMap();
            parameters.put("milataryId", milatryId);

            JasperReport jrr = JasperCompileManager.compileReport(jasperReport);
            JasperPrint print = JasperFillManager.fillReport(jrr, parameters, con);

//        JasperPrintManager.printReport(print, false);
            JasperViewer.viewReport(print, false);
        } catch (JRException | IOException ex) {
            Logger.getLogger(SearchByMiltaryIdController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void getExcelSheet(ActionEvent event) throws SQLException {
        try {
            FileChooser fileChooser = new FileChooser();
            Window stage = null;
            File file = fileChooser.showSaveDialog(stage);
            String savefile = null;
            if (file != null) {
                savefile = file.toString();
            }
            ResultSet rs = DatabaseAccess.getCourses(milatryId);
            String militaryid = null;
            String name = null;
            String rank = null;
            String unit = null;
            if (rs.next()) {
                militaryid = rs.getString("MILITARYID");
                name = rs.getString("NAME");
                rank = rs.getString("RANK");
                unit = rs.getString("UNIT");
            }
            while (rs.next()) {
                System.out.println(rs.getString("coursnames.CORSNAME"));
            }
//            String[] feild = {"CORSNAME", "COURSNUMBER", "COURSPLASE", "COURSDURATION", "STARTDATE", "ENDDATE","COURSESTIMATE"};
//            String[] titel = {"اسم الدورة", "رقم الدورة", "مكان انعقادها", "مدتها", "تاريخ بداية الدورة", "تاريخ نهاية الدورة", "التقدير"};
//            String[] personaltitel = {"الرقم العسكري",  "الاسم",  "الرتبة",  "الوحدة"};
//            String[] personaldata = { militaryid,  name, rank, unit};
//            ExporteExcelSheet exporter = new ExporteExcelSheet(rs, feild, titel,personaltitel,personaldata);
//            ArrayList<Object[]> dataList = exporter.getTableData();
//            if (dataList != null && dataList.size() > 0) {
//                exporter.doExport(dataList, savefile);
//                for (int i = 0; i < dataList.size(); i++) {
//                    System.out.println(dataList.get(i));
//                }
//            } else {
//                System.out.println("There is no data available in the table to export");
//            }
            rs.close();
        } catch (IOException ex) {
            Logger.getLogger(SearchByMiltaryIdController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
