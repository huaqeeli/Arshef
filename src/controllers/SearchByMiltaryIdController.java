package controllers;

import com.itextpdf.text.BadElementException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
            ResultSet rs = DatabaseAccess.getCourses("SELECT personaldata.MILITARYID,personaldata.NAME,personaldata.RANK ,personaldata.UNIT,"
                    + "coursnames.CORSNAME,coursesdata.COURSNUMBER,coursesdata.COURSPLASE,coursesdata.COURSDURATION,coursesdata.STARTDATE,coursesdata.ENDDATE,coursesdata.COURSESTIMATE FROM personaldata,coursesdata,coursnames "
                    + "WHERE personaldata.MILITARYID = '" + miliid + "' AND personaldata.MILITARYID = coursesdata.MILITARYID AND coursesdata.COURSID = coursnames.COURSID ");
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
            ResultSet rs = DatabaseAccess.getCourses("SELECT personaldata.MILITARYID,personaldata.NAME,personaldata.RANK ,personaldata.UNIT,"
                    + "coursnames.CORSNAME,coursesdata.COURSNUMBER,coursesdata.COURSPLASE,coursesdata.COURSDURATION,coursesdata.STARTDATE,coursesdata.ENDDATE,coursesdata.COURSESTIMATE FROM personaldata,coursesdata,coursnames "
                    + "WHERE personaldata.MILITARYID = '" + milatryId + "' AND personaldata.MILITARYID = coursesdata.MILITARYID AND coursesdata.COURSID = coursnames.COURSID ");
            String[] feild = {"MILITARYID", "RANK", "NAME", "ENFROM", "ENTO", "ENDATEFROM", "ENDATETO"};
            String[] titel = {"الرقم العسكري", "الرتبة", "الاسم", "الانتداب من", "الانتداب الى", "تاريخ بداية الانتداب", "تاريخ نهاية الانتداب"};
            ExporteExcelSheet exporter = new ExporteExcelSheet(rs, feild, titel);
            ArrayList<Object[]> dataList = exporter.getTableData();
            if (dataList != null && dataList.size() > 0) {
                exporter.doExport(dataList, savefile);
            } else {
                System.out.println("There is no data available in the table to export");
            }
        } catch (IOException ex) {
            Logger.getLogger(SearchByMiltaryIdController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
