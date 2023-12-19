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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
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

    ObservableList<DisplayModele> Displaylist = FXCollections.observableArrayList();
    ObservableList<String> displayTypelist = FXCollections.observableArrayList("عرض القائد", "توقيع القائد", "عرض الركن", "توقيع الركن", "توجيه الركن", "تاشير الركن");
    ObservableList<String> placeComboBoxlist = FXCollections.observableArrayList();
    ObservableList<String> circularTypelist = FXCollections.observableArrayList("الوارد الخارجي", "الصادرالخارجي", "الوارد الداخلي", "الصادر الداخلي");
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
    @FXML
    private ComboBox<String> circularType;
    @FXML
    private TextField circularid;
    @FXML
    private TextField action;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AppDate.setDateValue(DateDay, DateMonth, DateYear);
        AppDate.setCurrentDate(DateDay, DateMonth, DateYear);
        AppDate.setDateValue(displayDateDay, displayDateMonth, displayDateYear);
        AppDate.setCurrentDate(displayDateDay, displayDateMonth, displayDateYear);
        refreshData();
        FillComboBox.fillComboBox(displayTypelist, displayType);
        destination.setItems(filleCoursPlace(placeComboBoxlist));
        circularType.setItems(circularTypelist);
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
            viewdata(DatabaseAccess.getData("SELECT * FROM displaydata WHERE DISPLAYDATE = '" + getSearchDate() + "' ORDER BY ID DESC"));
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void printBagStatement(ActionEvent event) throws SQLException {
        try {
            InputStream input = new FileInputStream(new File(config.getAppURL() + "\\reports\\‏ComanderDisplay.jrxml"));

            List<DisplayModele> dataItems = new ArrayList<>();

            ResultSet rs = DatabaseAccess.select("displaydata", "DISPLAYDATE = '" + getSearchDate() + "' AND DISPLAYTYPE = 'توقيع القائد'");
            int squnce = 1;
            while (rs.next()) {
                DisplayModele display = new DisplayModele();
                String squnceText = ArabicSetting.EnglishToarabic(Integer.toString(squnce));
                display.setSqunces(squnceText);
                display.setTopic(rs.getString("TOPIC"));
                display.setDistnation(rs.getString("DESTINATION"));
                display.setNotes(rs.getString("NOTES"));
                dataItems.add(display);
                squnce++;
            }
            rs.close();
            List<DisplayModele> dataItems1 = new ArrayList<>();

            ResultSet rs1 = DatabaseAccess.select("displaydata", "DISPLAYDATE = '" + getSearchDate() + "' AND DISPLAYTYPE = 'عرض القائد'");
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
                if (rs.getString("CIRCULARID") == null || "".equals(rs.getString("CIRCULARID"))) {
                    display.setCircularid("===");
                } else {
                    display.setCircularid(ArabicSetting.EnglishToarabic(rs.getString("CIRCULARID")));
                }
                display.setCirculardate(ArabicSetting.EnglishToarabic(rs.getString("CIRCULARDATE") + "هـ"));
                display.setTopic(ArabicSetting.EnglishToarabic(rs.getString("TOPIC")));
                display.setAction(rs.getString("ACTION"));
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
                if (rs1.getString("CIRCULARID") == null || "".equals(rs1.getString("CIRCULARID"))) {
                    display1.setCircularid("===");
                } else {
                    display1.setCircularid(ArabicSetting.EnglishToarabic(rs1.getString("CIRCULARID")));
                }
                display1.setCirculardate(ArabicSetting.EnglishToarabic(rs1.getString("CIRCULARDATE") + "هـ"));
                display1.setTopic(ArabicSetting.EnglishToarabic(rs1.getString("TOPIC")));
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
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    public String getDisplayDate() {
        return HijriCalendar.getSimpleDate();
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
            viewdata(DatabaseAccess.getData("SELECT * FROM displaydata WHERE DISPLAYDATE = '" + getCurrentDate() + "'  ORDER BY ID DESC "));
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
                displayModele.setDistnation(rs.getString("DESTINATION"));
                displayModele.setTopic(rs.getString("TOPIC"));
                displayModele.setDisplayType(rs.getString("DISPLAYTYPE"));
                displayModele.setNotes(rs.getString("NOTES"));
                displayModele.setCircularid(rs.getString("CIRCULARID"));
                displayModele.setCirculardate(rs.getString("CIRCULARDATE"));
                displayModele.setAction(rs.getString("ACTION"));
                displayModeles.add(displayModele);
            }
            rs.close();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return displayModeles;
    }

    private void setChosendata(DisplayModele displayModele) {
        AppDate.setSeparateDate(displayDateDay, displayDateMonth, displayDateYear, displayModele.getCirculardate());
        displayType.setValue(displayModele.getDisplayType());
        topic.setText(displayModele.getTopic());
        destination.setValue(displayModele.getDistnation());
        notes.setText(displayModele.getNotes());
        circularid.setText(displayModele.getCircularid());
        notes.setText(displayModele.getNotes());
        action.setText(displayModele.getAction());
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
        String[] data = {getDisplayDate(), displayType.getValue(), topic.getText(), destination.getValue(), notes.getText(), circularid.getText(), getCircularDate(), action.getText()};
        String fieldName = "`DISPLAYDATE`,`DISPLAYTYPE`,`TOPIC`,`DESTINATION`,`NOTES`,`CIRCULARID`,`CIRCULARDATE`,`ACTION`";
        String valuenumbers = "?,?,?,?,?,?,?,?";

        boolean destinationState = FormValidation.comboBoxNotEmpty(destination, "الرجاء ادخال جهة المعاملة");
        boolean displayTypeState = FormValidation.comboBoxNotEmpty(displayType, "الرجاء اختر نوع العرض");
        boolean topicState = FormValidation.textFieldNotEmpty(topic, "الرجاء ادخال الموضوع");
        boolean circularidExisting = FormValidation.ifexisting("displaydata", "CIRCULARID", "CIRCULARID = '" + circularid.getText() + "' AND DISPLAYDATE ='" + getDisplayDate() + "'AND DISPLAYTYPE ='" + displayType.getValue() + "'", "تم اضافة المعاملة في عرض اليوم مسبقا");

        if (displayTypeState && destinationState && topicState && circularidExisting) {
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
        String[] data = {getDisplayDate(), displayType.getValue(), topic.getText(), destination.getValue(), notes.getText(), circularid.getText(), getCircularDate(), action.getText()};
        String fieldName = "`DISPLAYDATE`=?,`DISPLAYTYPE`=?,`TOPIC`=?,`DESTINATION`=?,`NOTES`=?,`CIRCULARID`=?,`CIRCULARDATE`=?,`ACTION`=?";

        boolean destinationState = FormValidation.comboBoxNotEmpty(destination, "الرجاء ادخال جهة المعاملة");
        boolean displayTypeState = FormValidation.comboBoxNotEmpty(displayType, "الرجاء اختر نوع العرض");
        boolean topicState = FormValidation.textFieldNotEmpty(topic, "الرجاء ادخال الموضوع");

        if (displayTypeState && destinationState && topicState) {
            try {
                DatabaseAccess.updat(tableName, fieldName, data, "ID = '" + id + "'");
                refreshData();
                clear(event);
            } catch (IOException | SQLException ex) {
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
        circularid.setText(null);
        circularType.setValue(null);
        action.setText(null);
    }

    @FXML
    private void getCircularData(KeyEvent event) {
        String typeValue = circularType.getValue();
        if (typeValue != null) {

            switch (typeValue) {
                case "الوارد الخارجي":
                    try {
                    ResultSet rs = DatabaseAccess.selectQuiry("SELECT CIRCULARDATE,TOPIC,DESTINATION FROM externalincoming WHERE RECEIPTNUMBER = '" + circularid.getText() + "' AND ARSHEFYEAR ='" + HijriCalendar.getSimpleYear() + "'");
                    if (rs.next()) {
                        setCircularDate(rs.getString("CIRCULARDATE"));
                        topic.setText("وارد من" + " " + rs.getString("DESTINATION") + " " + rs.getString("TOPIC") + " " + getName(circularid.getText(), "external"));
                        destination.setValue(rs.getString("DESTINATION"));
                    }
                } catch (IOException | SQLException ex) {
                    FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
                }
                break;
                case "الصادرالخارجي":
                    try {
                    ResultSet rs = DatabaseAccess.selectQuiry("SELECT TOPIC,EXPORTDATE,DESTINATION FROM exportsdata WHERE EXPORTNUM = '" + circularid.getText() + "' AND RECORDYEAR ='" + HijriCalendar.getSimpleYear() + "'");
                    if (rs.next()) {
                        setCircularDate(rs.getString("EXPORTDATE"));
                        topic.setText("صادر الى" + " " + rs.getString("DESTINATION") + " " + rs.getString("TOPIC") + " " + getName(circularid.getText(), "external"));
                        destination.setValue(rs.getString("DESTINATION"));
                    }
                } catch (IOException | SQLException ex) {
                    FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
                }
                break;
                case "الوارد الداخلي":
                    try {
                    ResultSet rs = DatabaseAccess.selectQuiry("SELECT  RECIPIENT_DATE,TOPIC,CIRCULAR_DIR FROM internalincoming WHERE REGIS_NO = '" + circularid.getText() + "' AND RECORD_YEAR ='" + HijriCalendar.getSimpleYear() + "'");
                    if (rs.next()) {
                        setCircularDate(rs.getString("RECIPIENT_DATE"));
                        topic.setText("وارد من" + " " + rs.getString("CIRCULAR_DIR") + " " + rs.getString("TOPIC") + " " + getName(circularid.getText(), "internal"));
                        destination.setValue(rs.getString("CIRCULAR_DIR"));
                    }
                } catch (IOException | SQLException ex) {
                    FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
                }
                break;
                case "الصادر الداخلي":
                    try {
                    ResultSet rs = DatabaseAccess.selectQuiry("SELECT EXPORTDATE,TOPIC,DESTINATION FROM internalexports WHERE REGISNO = '" + circularid.getText() + "' AND RECORDYEAR ='" + HijriCalendar.getSimpleYear() + "'");
                    if (rs.next()) {
                        setCircularDate(rs.getString("EXPORTDATE"));
                        topic.setText("صادر الى" + " " + rs.getString("DESTINATION") + " " + rs.getString("TOPIC") + " " + getName(circularid.getText(), "internal"));
                        destination.setValue(rs.getString("DESTINATION"));
                    }
                } catch (IOException | SQLException ex) {
                    FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
                }
                break;
            }
        } else {
            FormValidation.showAlert(null, "اختر نوع المعاملة", Alert.AlertType.ERROR);
        }

    }

    private String getName(String id, String type) {
        String name = null;
        try {
            ResultSet rs = DatabaseAccess.getData("SELECT circularnames.YEAR,circularnames.MILITARYID,personaldata.NAME,personaldata.MILITARYID,personaldata.RANK FROM circularnames,personaldata "
                    + "WHERE circularnames.MILITARYID = personaldata.MILITARYID AND CIRCULARID = '" + circularid.getText() + "'AND YEAR = '" + HijriCalendar.getSimpleYear() + "'AND type = '" + type + "' ");
            if (rs.next()) {
                name = rs.getString("personaldata.RANK") + "/" + rs.getString("personaldata.NAME");
            } else {
                name = "";
            }
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return name;
    }

    private void setCircularDate(String date) {
        AppDate.setSeparateDate(displayDateDay, displayDateMonth, displayDateYear, date);
    }

    private String getCircularDate() {
        return AppDate.getDate(displayDateDay, displayDateMonth, displayDateYear);
    }

    @FXML
    private void addToDisplay(ActionEvent event) {
    }
}
