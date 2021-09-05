package controllers;

import Serveces.DisplayPageListener;
import Validation.FormValidation;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
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

    ObservableList<DisplayModele> Displaylist = FXCollections.observableArrayList();
    ObservableList<String> displayTypelist = FXCollections.observableArrayList("عرض القائد", "توقيع القائد", "عرض الركن", "توقيع الركن", "توجيه الركن", "تاشير الركن");
    ObservableList<String> placeComboBoxlist = FXCollections.observableArrayList();
    Label displayDate = null;
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
    Config config = new Config();
    List<DisplayModele> displayObject = new ArrayList<>();
    private DisplayPageListener mylistener;
    @FXML
    private VBox vbox;
    ActionEvent event;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AppDate.setDateValue(DateDay, DateMonth, DateYear);
        AppDate.setCurrentDate(DateDay, DateMonth, DateYear);
        AppDate.setDateValue(displayDateDay, displayDateMonth, displayDateYear);
        AppDate.setCurrentDate(displayDateDay, displayDateMonth, displayDateYear);
        refreshData();
        FillComboBox.fillComboBox(displayTypelist, displayType);
        destination.setItems(filleCoursPlace(placeComboBoxlist));
        clear(event);
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
            displayObject.clear();
            vbox.getChildren().clear();
            viewdata(DatabaseAccess.getData("SELECT * FROM displaydata WHERE DISPLAYDATE = '" + getSearchDate() + "' "));
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void printBagStatement(ActionEvent event) throws SQLException {
        try {
            InputStream input = new FileInputStream(new File(config.getAppURL() + "\\reports\\‏ComanderDisplay.jrxml"));

            List<DisplayModele> dataItems = new ArrayList<>();

//            ResultSet rs = DatabaseAccess.select("displaydata", "DISPLAYDATE = '" + getSearchDate() + "' and (DISPLAYTYPE = 'توقيع القائد')");
            ResultSet rs = DatabaseAccess.getData("SELECT displaydata.TOPIC,displaydata.NOTES,displaydata.DESTINATION ,circularnames.MILITARYID,personaldata.NAME,personaldata.MILITARYID,personaldata.RANK "
                    + "FROM circularnames,personaldata,displaydata "
                    + "WHERE displaydata.ID = circularnames.CIRCULARID AND circularnames.MILITARYID =personaldata.MILITARYID AND DISPLAYTYPE = 'توقيع القائد'");
            int squnce = 1;
            while (rs.next()) {
                DisplayModele display = new DisplayModele();
                String squnceText = ArabicSetting.EnglishToarabic(Integer.toString(squnce));
                 display.setSqunces(squnceText);
                display.setTopic(rs.getString("TOPIC"));
                display.setDestination(rs.getString("DESTINATION"));
                display.setName(rs.getString("RANK") + "/" + rs.getString("NAME"));
                display.setNotes(rs.getString("NOTES"));
                dataItems.add(display);
                squnce++;
            }
            rs.close();
            List<DisplayModele> dataItems1 = new ArrayList<>();

            ResultSet rs1 = DatabaseAccess.select("displaydata", "DISPLAYDATE = '" + getSearchDate() + "' and DISPLAYTYPE = 'عرض القائد'");
            int squnce1 = 1;

            while (rs1.next()) {
                DisplayModele display1 = new DisplayModele();
                String squnceText = ArabicSetting.EnglishToarabic(Integer.toString(squnce1));
               display1.setSqunces(squnceText);
                display1.setTopic(rs1.getString("TOPIC"));
                display1.setNotes(rs1.getString("NOTES"));
                dataItems1.add(display1);
                squnce1++;
            }
            rs1.close();
            JRBeanCollectionDataSource itemsJarbean = new JRBeanCollectionDataSource(dataItems);
            JRBeanCollectionDataSource itemsJarbean1 = new JRBeanCollectionDataSource(dataItems1);
            Connection con = DatabaseConniction.dbConnector();
            Map parameters = new HashMap();

            parameters.put("day", HijriCalendar.getSimpleWeekday());
            parameters.put("date", ArabicSetting.EnglishToarabic(HijriCalendar.getSimpleDate()) + "هـ");
            parameters.put("displayDateprameter", AppDate.getDate(DateDay, DateMonth, DateYear));
            parameters.put("uintNum", ArabicSetting.EnglishToarabic("067"));
            parameters.put("repotCollation", itemsJarbean1);
            parameters.put("SignatureCollaiction", itemsJarbean);

            JasperDesign jasperDesign = JRXmlLoader.load(input);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, con);
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException | IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void printManagerDisplay(ActionEvent event) throws SQLException {
        try {
            InputStream input = new FileInputStream(new File(config.getAppURL() + "\\reports\\mangerDisplay.jrxml"));

            List<DisplayModele> dataItems = new ArrayList<>();

            ResultSet rs = DatabaseAccess.select("displaydata", "DISPLAYDATE = '" + getSearchDate() + "' and (DISPLAYTYPE = 'تاشير الركن' or DISPLAYTYPE = 'توقيع الركن')");
            int squnce = 1;
            while (rs.next()) {
                DisplayModele display = new DisplayModele();
                String squnceText = ArabicSetting.EnglishToarabic(Integer.toString(squnce));
                display.setSqunces(squnceText);
                display.setTopic(rs.getString("TOPIC"));
                display.setNotes(rs.getString("NOTES"));
                dataItems.add(display);
                squnce++;
            }
            rs.close();
            List<DisplayModele> dataItems1 = new ArrayList<>();

            ResultSet rs1 = DatabaseAccess.select("displaydata", "DISPLAYDATE = '" + getSearchDate() + "' and (DISPLAYTYPE = 'عرض الركن' or DISPLAYTYPE = 'توجيه الركن')");
            int squnce1 = 1;

            while (rs1.next()) {
                DisplayModele display1 = new DisplayModele();
                String squnceText = ArabicSetting.EnglishToarabic(Integer.toString(squnce1));
                display1.setSqunces(squnceText);
                display1.setTopic(rs1.getString("TOPIC"));
                display1.setNotes(rs1.getString("NOTES"));
                dataItems1.add(display1);
                squnce1++;
            }
            rs1.close();
            JRBeanCollectionDataSource itemsJarbean = new JRBeanCollectionDataSource(dataItems);
            JRBeanCollectionDataSource itemsJarbean1 = new JRBeanCollectionDataSource(dataItems1);
            Connection con = DatabaseConniction.dbConnector();
            Map parameters = new HashMap();

            parameters.put("day", HijriCalendar.getSimpleWeekday());
            parameters.put("date", ArabicSetting.EnglishToarabic(HijriCalendar.getSimpleDate()) + "هـ");
            parameters.put("displayDateprameter", AppDate.getDate(DateDay, DateMonth, DateYear));
            parameters.put("uintNum", ArabicSetting.EnglishToarabic("067"));
            parameters.put("repotCollation", itemsJarbean);
            parameters.put("SignatureCollaiction", itemsJarbean1);

            JasperDesign jasperDesign = JRXmlLoader.load(input);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, con);
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException | IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void delete(ActionEvent event) {
        try {
            DatabaseAccess.delete("displaydata", "ID = '" + id + "'");
            refreshData();
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

    private void refreshData() {
        try {
            displayObject.clear();
            vbox.getChildren().clear();
            viewdata(DatabaseAccess.getData("SELECT * FROM displaydata WHERE DISPLAYDATE = '" + getCurrentDate() + "' "));
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    private List<DisplayModele> getData(ResultSet rs) {
        List<DisplayModele> displayModeles = new ArrayList<>();
        DisplayModele displayModele;
        try {
            int squence = 0;
            while (rs.next()) {
                squence++;
                displayModele = new DisplayModele();
                displayModele.setSquence(squence);
                displayModele.setDisplayid(rs.getString("ID"));
                displayModele.setDisplayDate(rs.getString("DISPLAYDATE"));
                displayModele.setDestination(rs.getString("DESTINATION"));
                displayModele.setTopic(rs.getString("TOPIC"));
                displayModele.setDisplayType(rs.getString("DISPLAYTYPE"));
                displayModele.setNotes(rs.getString("NOTES"));
                displayModeles.add(displayModele);
            }
            rs.close();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return displayModeles;
    }

    private void setChosendata(DisplayModele displayModele) {
        AppDate.setSeparateDate(displayDateDay, displayDateMonth, displayDateYear, displayModele.getDisplayDate());
        displayType.setValue(displayModele.getDisplayType());
        topic.setText(displayModele.getTopic());
        destination.setValue(displayModele.getDestination());
        notes.setText(displayModele.getNotes());
        id = displayModele.getDisplayid();
    }

    private void viewdata(ResultSet rs) {
        displayObject.addAll(getData(rs));
        if (displayObject.size() > 0) {
            setChosendata(displayObject.get(0));
            mylistener = new DisplayPageListener() {
                @Override
                public void onClickListener(DisplayModele displayModele) {
                    setChosendata(displayModele);
                }
            };
        }

        try {
            for (DisplayModele displayModele : displayObject) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/view/DisplayItem.fxml"));
                AnchorPane pane = fxmlLoader.load();
                DisplayItemController displayItemController = fxmlLoader.getController();
                displayItemController.setData(displayModele, mylistener);
                vbox.getChildren().add(pane);
            }
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
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
                refreshData();
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
                refreshData();
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

    @FXML
    private void click(MouseEvent event) {
    }
}
