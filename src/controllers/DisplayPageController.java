package controllers;

import Validation.FormValidation;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import modeles.DisplayModele;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class DisplayPageController implements Initializable {

    @FXML
    private ComboBox<?> DateDay;
    @FXML
    private ComboBox<?> DateMonth;
    @FXML
    private ComboBox<?> DateYear;
    @FXML
    private Button searchButton1;
    @FXML
    private TableView<DisplayModele> displayTable;
    @FXML
    private TableColumn<?, ?> squence_col;
    @FXML
    private TableColumn<?, ?> displayid_col;
    @FXML
    private TableColumn<?, ?> displayDate_col;
    @FXML
    private TableColumn<?, ?> destination_col;
    @FXML
    private TableColumn<?, ?> topic_col;
    @FXML
    private TableColumn<?, ?> displayType_col;
    @FXML
    private TableColumn<?, ?> notes_col;

    ObservableList<DisplayModele> Displaylist = FXCollections.observableArrayList();
    ObservableList<String> displayTypelist = FXCollections.observableArrayList("عرض القائد", "توقيع القائد", "عرض الركن", "توقيع الركن", "توجيه الركن", "تاشير الركن");
    ObservableList<String> placeComboBoxlist = FXCollections.observableArrayList();
    String displayDate = null;
    String id = null;
    @FXML
    private ComboBox<?> displayDateDay;
    @FXML
    private ComboBox<?> displayDateMonth;
    @FXML
    private ComboBox<?> displayDateYear;
    @FXML
    private ComboBox<String> displayType;
    @FXML
    private TextField topic;
    @FXML
    private ComboBox<String> destination;
    @FXML
    private TextField notes;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AppDate.setDateValue(DateDay, DateMonth, DateYear);
        AppDate.setCurrentDate(DateDay, DateMonth, DateYear);
        AppDate.setDateValue(displayDateDay, displayDateMonth, displayDateYear);
        AppDate.setCurrentDate(displayDateDay, displayDateMonth, displayDateYear);
        refreshDisplayTableView();
        FillComboBox.fillComboBox(displayTypelist, displayType);
        destination.setItems(filleCoursPlace(placeComboBoxlist));
        getTableRow(displayTable);
        getTableRowByInterKey(displayTable);
    }

    private ObservableList filleCoursPlace(ObservableList list) {
        try {
            ResultSet rs = DatabaseAccess.select("placenames");
            try {
                while (rs.next()) {
                    list.add(rs.getString("PLACENAME"));
                }
                rs.close();
            } catch (SQLException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return list;
    }

    @FXML
    private void searchData(ActionEvent event) {
        try {
            Displaylist.clear();
            displayTableView(DatabaseAccess.getData("SELECT * FROM displaydata WHERE DISPLAYDATE = '" + getSearchDate() + "' "));
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void printBagStatement(ActionEvent event) {
    }

    @FXML
    private void printManagerDisplay(ActionEvent event) throws SQLException {
        try {
//            String reportSrcFile = "C:\\Program Files\\Arshef\\reports\\ManualReport.jrxml";
            String reportSrcFile = "C:\\Users\\ابو ريان\\Documents\\NetBeansProjects\\Arshef\\src\\reports\\mangerDisplay.jrxml";

            Connection con = DatabaseConniction.dbConnector();
//            List<DisplayModele> dataItems = new ArrayList<DisplayModele>();
//            DisplayModele display = new DisplayModele();
            
//            ResultSet rs = DatabaseAccess.select("displaydata","DISPLAYDATE = '"+getSearchDate()+"' and (DISPLAYTYPE = 'عرض الركن' or DISPLAYTYPE = 'توجيه الركن')");
//            int squnce = 1;
//            while (rs.next()) {
//               display.setSqu(ArabicSetting.EnglishToarabic(Integer.toString(squnce)));
//               display.setTopic(rs.getString("TOPIC"));
//               display.setNotes(rs.getString("NOTES"));
//               dataItems.add(display);
//               squnce++;
//            }
//            JRBeanCollectionDataSource itemsJarbean = new JRBeanCollectionDataSource(dataItems);
            JasperDesign jasperReport = JRXmlLoader.load(reportSrcFile);
            Map parameters = new HashMap();
            parameters.put("day", HijriCalendar.getSimpleWeekday());
            parameters.put("date", ArabicSetting.EnglishToarabic(HijriCalendar.getSimpleDate()) + "هـ");
            parameters.put("displayDateprameter", AppDate.getDate(DateDay, DateMonth, DateYear));
            parameters.put("uintNum", ArabicSetting.EnglishToarabic("067"));
            parameters.put("subReport1", "C:\\Users\\ابو ريان\\Documents\\NetBeansProjects\\Arshef\\src\\reports\\subReport2.jasper");
            parameters.put("subReport2", "C:\\Users\\ابو ريان\\Documents\\NetBeansProjects\\Arshef\\src\\reports\\subReport2.jasper");
//            parameters.put("CollextionBeanPram",itemsJarbean);

            JasperReport jrr = JasperCompileManager.compileReport(jasperReport);
            JasperPrint print = JasperFillManager.fillReport(jrr, parameters, con);

//        JasperPrintManager.printReport(print, false);
            JasperViewer.viewReport(print, false);
        } catch (JRException | IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void delete(ActionEvent event) {
        try {
            DatabaseAccess.delete("displaydata", "ID = '" + id + "'");
            refreshDisplayTableView();
            clear(event);
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    public String getDisplayDate() {
        return AppDate.getDate(displayDateDay, displayDateMonth, displayDateYear);
    }

    public String getSearchDate() {
        return AppDate.getDate(DateDay, DateMonth, DateYear);
    }

    public String getCurrentDate() {
        return HijriCalendar.getSimpleDate();
    }

    private void refreshDisplayTableView() {
        try {
            Displaylist.clear();
            displayTableView(DatabaseAccess.getData("SELECT * FROM displaydata WHERE DISPLAYDATE = '" + getCurrentDate() + "' "));
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    private void displayTableView(ResultSet rs) {
        try {
            int squence = 0;
            while (rs.next()) {
                squence++;
                Displaylist.add(new DisplayModele(
                        rs.getString("ID"),
                        rs.getString("DISPLAYDATE"),
                        rs.getString("DESTINATION"),
                        rs.getString("TOPIC"),
                        rs.getString("DISPLAYTYPE"),
                        rs.getString("NOTES"),
                        squence
                ));
            }
            rs.close();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        displayid_col.setCellValueFactory(new PropertyValueFactory<>("displayid"));
        displayDate_col.setCellValueFactory(new PropertyValueFactory<>("displayDate"));
        topic_col.setCellValueFactory(new PropertyValueFactory<>("topic"));
        destination_col.setCellValueFactory(new PropertyValueFactory<>("destination"));
        displayType_col.setCellValueFactory(new PropertyValueFactory<>("displayType"));
        squence_col.setCellValueFactory(new PropertyValueFactory<>("squence"));
        notes_col.setCellValueFactory(new PropertyValueFactory<>("notes"));

        displayTable.setItems(Displaylist);
    }

    @FXML
    private void save(ActionEvent event) {
        String tableName = "displaydata";
        String[] data = {getDisplayDate(), displayType.getValue(), topic.getText(), destination.getValue(), notes.getText()};
        String fieldName = "`DISPLAYDATE`,`DISPLAYTYPE`,`TOPIC`,`DESTINATION`,`NOTES`";
        String valuenumbers = "?,?,?,?,?";

        boolean destinationState = FormValidation.comboBoxNotEmpty(destination, "الرجاء ادخال جهة المعاملة");
        boolean displayTypeState = FormValidation.comboBoxNotEmpty(displayType, "الرجاء اختر نوع العرض");
        boolean topicState = FormValidation.textFieldNotEmpty(topic, "الرجاء ادخال الموضوع");

        if (displayTypeState && destinationState && topicState) {
            try {
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, data);
                refreshDisplayTableView();
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void edit(ActionEvent event) {
        String tableName = "displaydata";
        String[] data = {getDisplayDate(), displayType.getValue(), topic.getText(), destination.getValue(), notes.getText()};
        String fieldName = "`DISPLAYDATE`=?,`DISPLAYTYPE`=?,`TOPIC`=?,`DESTINATION`=?,`NOTES`=?";

        boolean destinationState = FormValidation.comboBoxNotEmpty(destination, "الرجاء ادخال جهة المعاملة");
        boolean displayTypeState = FormValidation.comboBoxNotEmpty(displayType, "الرجاء اختر نوع العرض");
        boolean topicState = FormValidation.textFieldNotEmpty(topic, "الرجاء ادخال الموضوع");

        if (displayTypeState && destinationState && topicState) {
            try {
                DatabaseAccess.updat(tableName, fieldName, data, "ID = '" + id + "'");
                refreshDisplayTableView();
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void clear(ActionEvent event) {
        AppDate.setCurrentDate(displayDateDay, displayDateMonth, displayDateYear);
        displayType.setValue(null);
        topic.setText(null);
        notes.setText(null);
        destination.setValue(null);
    }

    public void getTableRow(TableView table) {
        table.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                ObservableList<DisplayModele> list = FXCollections.observableArrayList();
                list = table.getSelectionModel().getSelectedItems();
                if (!list.isEmpty()) {
                    id = list.get(0).getDisplayid();
                    displayType.setValue(list.get(0).getDisplayType());
                    topic.setText(list.get(0).getTopic());
                    destination.setValue(list.get(0).getDestination());
                    notes.setText(list.get(0).getNotes());
                    AppDate.setSeparateDate(displayDateDay, displayDateMonth, displayDateYear, list.get(0).getDisplayDate());
                }
            }
        });
    }

    private void getTableRowByInterKey(TableView table) {
        table.setOnKeyPressed(new EventHandler() {
            @Override
            public void handle(Event event) {
                ObservableList<DisplayModele> list = FXCollections.observableArrayList();
                list = table.getSelectionModel().getSelectedItems();
                if (!list.isEmpty()) {
                    id = list.get(0).getDisplayid();
                    displayType.setValue(list.get(0).getDisplayType());
                    topic.setText(list.get(0).getTopic());
                    destination.setValue(list.get(0).getDestination());
                    destination.setValue(null);
                    notes.setText(list.get(0).getNotes());
                    AppDate.setSeparateDate(displayDateDay, displayDateMonth, displayDateYear, list.get(0).getDisplayDate());
                }
            }
        });
    }
}
