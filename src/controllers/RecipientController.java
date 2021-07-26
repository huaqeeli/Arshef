package controllers;


import Validation.FormValidation;
import static Validation.FormValidation.showAlert;


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
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import modeles.RecipientModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class RecipientController implements Initializable {

    ObservableList<RecipientModel> recipientList = FXCollections.observableArrayList();

    @FXML
    private TableColumn<?, ?> regisNO_col;
    @FXML
    private TableColumn<?, ?> recipientDate_col;
    @FXML
    private TableColumn<?, ?> circularNo_col;
    @FXML
    private TableColumn<?, ?> circularDate_col;
    @FXML
    private TableColumn<?, ?> circularDir_col;
    @FXML
    private TableColumn<?, ?> topic_col;
    @FXML
    private TableView<RecipientModel> recipientTableView;
    @FXML
    private TextField circularNumber;
    @FXML
    private TextField topic;
    @FXML
    private TextField saveFaile;
    @FXML
    private Button clearData;
    @FXML
    private ComboBox<String> dirList;

    ObservableList<String> dirComboBoxlist = FXCollections.observableArrayList();
    @FXML
    private ComboBox<String> reday;
    @FXML
    private ComboBox<String> remonth;
    @FXML
    private ComboBox<String> circularDateday;
    @FXML
    private ComboBox<String> circularDatemonth;
    @FXML
    private ComboBox<String> reyear;
    @FXML
    private ComboBox<String> circularDateyear;
//    LodPages lodPages = new LodPages();
    private int lastRegisId = 0;
    private int regisNumber = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        refreshRecipienTableView();
        refreshListCombobox(filleDirection(dirComboBoxlist));
        AppDate.setDateValue(reday, remonth, reyear);
        AppDate.setCurrentDate(reday, remonth, reyear);
        AppDate.setDateValue(circularDateday, circularDatemonth, circularDateyear);
        AppDate.setCurrentDate(circularDateday, circularDatemonth, circularDateyear);
        getTableRow(recipientTableView);
        getTableRowByInterKey(recipientTableView);
    }

    private void refreshRecipienTableView() {
        recipientList.clear();
        recipienTableView();
    }

    private void recipienTableView() {
        try {
            ResultSet rs = DatabaseAccess.select("ex_and_re_recordes", "RECORD_TYPE = 'RE' ORDER BY REGIS_NO DESC");
            while (rs.next()) {
                recipientList.add(new RecipientModel(
                        ArabicSetting.EnglishToarabic(rs.getString("REGIS_NO")),
                        rs.getString("RECIPIENT_DATE"),
                        rs.getString("CIRCULAR_NO"),
                        rs.getString("CIRCULAR_DATE"),
                        rs.getString("CIRCULAR_DIR"),
                        rs.getString("TOPIC"),
                        rs.getString("SAVE_FILE")
                ));
            }
            rs.close();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(RecipientController.class.getName()).log(Level.SEVERE, null, ex);
        }
        regisNO_col.setCellValueFactory(new PropertyValueFactory<>("regisNo"));
        recipientDate_col.setCellValueFactory(new PropertyValueFactory<>("recipientDate"));
        circularNo_col.setCellValueFactory(new PropertyValueFactory<>("circularNo"));
        circularDate_col.setCellValueFactory(new PropertyValueFactory<>("circularDate"));
        circularDir_col.setCellValueFactory(new PropertyValueFactory<>("circularDir"));
        topic_col.setCellValueFactory(new PropertyValueFactory<>("topic"));

        recipientTableView.setItems(recipientList);
    }

    private Callback addButtonToTable() {
        TableColumn<RecipientModel, Void> colBtn = new TableColumn("Button Column");

        Callback<TableColumn<RecipientModel, Void>, TableCell<RecipientModel, Void>> cellFactory = (final TableColumn<RecipientModel, Void> param) -> {
            final TableCell<RecipientModel, Void> cell = new TableCell<RecipientModel, Void>() {

                private final Button btn = new Button("Action");

                {
                    btn.setOnAction((ActionEvent event) -> {
                        RecipientModel data = getTableView().getItems().get(getIndex());
                        System.out.println("selectedData: " + data.getTopic());
                    });
                }

                @Override
                public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(btn);
                    }
                }
            };
            return cell;
        };
        return cellFactory;
    }

    @FXML
    private void insertData(ActionEvent event) {
        String reDate = AppDate.getDate(reday, remonth, reyear);
        String circularDate = AppDate.getDate(circularDateday, circularDatemonth, circularDateyear);
        String tableName = "ex_and_re_recordes";
        String fieldName = "`RECIPIENT_DATE`,`CIRCULAR_NO`,`CIRCULAR_DATE`,`CIRCULAR_DIR`,`TOPIC`,`SAVE_FILE`,`RECORD_TYPE`";
        String[] data = {reDate, ArabicSetting.EnglishToarabic(circularNumber.getText()), circularDate, dirList.getValue(), topic.getText(), ArabicSetting.EnglishToarabic(saveFaile.getText()), "RE"};
        String valuenumbers = "?,?,?,?,?,?,?";
        boolean circularNumberState = FormValidation.textFieldNotEmpty(circularNumber, "الرجاء ادخال رقم المعاملة");
        boolean topicState = FormValidation.textFieldNotEmpty(topic, "الرجاء ادخال جهة الوارد");

        if (circularNumberState && topicState) {
            try {
                lastRegisId = DatabaseAccess.insert(tableName, fieldName, valuenumbers, data);
                refreshRecipienTableView();
            } catch (IOException ex) {
                Logger.getLogger(RecipientController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @FXML
    private void printBarcode(ActionEvent event) throws JRException {
        try {
            if (lastRegisId != 0) {
                Connection con = DatabaseConniction.dbConnector();
                JasperDesign recipientReport = JRXmlLoader.load("C:\\Users\\Administrator\\Documents\\NetBeansProjects\\EDMS\\src\\Reportes\\RecipientBarcod.jrxml");
                ResultSet rs = DatabaseAccess.select("ex_and_re_recordes", "REGIS_NO = '" + lastRegisId + "'");
                String regisNo = null;
                String recipientDate = null;
                String circularDir = null;
                int quRegisNo = 0;
                String unitName = null;
                while (rs.next()) {
                    regisNo = ArabicSetting.EnglishToarabic(Integer.toString(rs.getInt("REGIS_NO")));
                    recipientDate = new StringBuilder().append(rs.getString("RECIPIENT_DATE")).append("هـ").toString();
                    circularDir = rs.getString("CIRCULAR_DIR");
                    quRegisNo = rs.getInt("REGIS_NO");
                    unitName = DatabaseAccess.getUintName();
                }
                Map barrcod = new HashMap();
                barrcod.put("ex_id", regisNo);
                barrcod.put("ex_date", recipientDate);
                barrcod.put("dir_to", circularDir);
                barrcod.put("qex_id", quRegisNo);
                barrcod.put("unitName", unitName);
                JasperReport jr = JasperCompileManager.compileReport(recipientReport);
                JasperPrint jp = JasperFillManager.fillReport(jr, barrcod, con);
//            JasperPrintManager.printReport(jp, false);
                JasperViewer.viewReport(jp, false);
            } else {
                showAlert("", "اختر السجل من الجدول");
            }

        } catch (IOException | SQLException | JRException ex) {
            Logger.getLogger(RecipientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void addNames(ActionEvent event) {
        if (lastRegisId != 0) {
            lodPages.lodAddNamesPage(Integer.toString(lastRegisId));
        } else {
            showAlert("", "اختر السجل من الجدول");
        }
    }

    @FXML
    private void addImages(ActionEvent event) {
        try {
            if (lastRegisId != 0) {
                DatabaseAccess.insertImage(Integer.toString(lastRegisId));
            } else {
                showAlert("", "اختر السجل من الجدول");
            }
        } catch (IOException ex) {
            Logger.getLogger(RecipientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void updateData(ActionEvent event) {
    }

    @FXML
    private void printImage(ActionEvent event) {
        lodPages.lodShowImage(lastRegisId);
    }

    @FXML
    private void addDirection(MouseEvent event) {
        String tableName = "items";
        String fieldName = "`item_text`";
        String[] data = {dirList.getPromptText()};
        String valuenumbers = "?";

        try {
            DatabaseAccess.insert(tableName, fieldName, valuenumbers, data);
        } catch (IOException ex) {
            Logger.getLogger(RecipientController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void updaeDirection(MouseEvent event) {

    }

    @FXML
    private void deleteDirection(MouseEvent event) {
    }

    @FXML
    private void clear(MouseEvent event) {
    }

    private ObservableList filleDirection(ObservableList list) {
        try {
            ResultSet rs = DatabaseAccess.getItems("items");
            try {
                while (rs.next()) {
                    list.add(rs.getString("item_text"));
                }
                rs.close();

            } catch (SQLException ex) {
                Logger.getLogger(RecipientController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (IOException ex) {
            Logger.getLogger(RecipientController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    private void refreshListCombobox(ObservableList list) {
        dirList.setItems(list);
    }

    public void getTableRow(TableView table) {
        table.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                ObservableList<RecipientModel> list = FXCollections.observableArrayList();
                list = table.getSelectionModel().getSelectedItems();
                if (list.isEmpty()) {
                    FormValidation.showAlert("", "لاتوجد بيانات");
                } else {
                    try {
                        ResultSet rs = DatabaseAccess.select("ex_and_re_recordes", "REGIS_NO = '" + list.get(0).getRegisNo() + "'");
                        reday.setValue(DateBoxController.getDay(list.get(0).getRecipientDate()));
                        remonth.setValue(DateBoxController.getMonth(list.get(0).getRecipientDate()));
                        reyear.setValue(DateBoxController.getYear(list.get(0).getRecipientDate()));
                        circularDateday.setValue(DateBoxController.getDay(list.get(0).getCircularDate()));
                        circularDatemonth.setValue(DateBoxController.getMonth(list.get(0).getCircularDate()));
                        circularDateyear.setValue(DateBoxController.getYear(list.get(0).getCircularDate()));
                        circularNumber.setText(list.get(0).getCircularNo());
                        dirList.setValue(list.get(0).getCircularDir());
                        topic.setText(list.get(0).getTopic());
                        if (rs.next()) {
                            saveFaile.setText(rs.getString("SAVE_FILE"));
                        }
                        lastRegisId = Integer.parseInt(list.get(0).getRegisNo());
                    } catch (IOException | SQLException ex) {
                        Logger.getLogger(RecipientController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }

    private void getTableRowByInterKey(TableView table) {
        table.setOnKeyPressed(new EventHandler() {
            @Override
            public void handle(Event event) {
                ObservableList<RecipientModel> list = FXCollections.observableArrayList();
                list = table.getSelectionModel().getSelectedItems();
                if (list.isEmpty()) {
                    FormValidation.showAlert("", "لاتوجد بيانات");
                } else {
                    try {
                        ResultSet rs = DatabaseAccess.select("ex_and_re_recordes", "REGIS_NO = '" + list.get(0).getRegisNo() + "'");
                        reday.setValue(DateBoxController.getDay(list.get(0).getRecipientDate()));
                        remonth.setValue(DateBoxController.getMonth(list.get(0).getRecipientDate()));
                        reyear.setValue(DateBoxController.getYear(list.get(0).getRecipientDate()));
                        circularDateday.setValue(DateBoxController.getDay(list.get(0).getCircularDate()));
                        circularDatemonth.setValue(DateBoxController.getMonth(list.get(0).getCircularDate()));
                        circularDateyear.setValue(DateBoxController.getYear(list.get(0).getCircularDate()));
                        circularNumber.setText(list.get(0).getCircularNo());
                        dirList.setValue(list.get(0).getCircularDir());
                        topic.setText(list.get(0).getTopic());
                        if (rs.next()) {
                            saveFaile.setText(rs.getString("SAVE_FILE"));
                        }
                        lastRegisId = Integer.parseInt(list.get(0).getRegisNo());
                    } catch (IOException | SQLException ex) {
                        Logger.getLogger(RecipientController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }

}
