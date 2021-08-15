package controllers;

import Validation.FormValidation;
import static Validation.FormValidation.showAlert;
import arshef.App;
import java.io.File;
import java.io.IOException;
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
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import modeles.InternalIncomingModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class InternalIncomingPageController implements Initializable {

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
    private TableColumn<?, ?> notes_col;
    @FXML
    private TableColumn<InternalIncomingModel, String> image_col;
    @FXML
    private TableColumn<InternalIncomingModel, String> addImage_col;
    @FXML
    private TableView<InternalIncomingModel> recipientTableView;
    @FXML
    private TextField circularNumber;
    @FXML
    private TextField topic;
    @FXML
    private TextField saveFaile;
    @FXML
    private ComboBox<String> destination;
    @FXML
    private ComboBox<String> incomingDay;
    @FXML
    private ComboBox<String> incomingMonth;
    @FXML
    private ComboBox<String> incomingYear;
    @FXML
    private ComboBox<String> circularDateday;
    @FXML
    private ComboBox<String> circularDatemonth;
    @FXML
    private ComboBox<String> circularDateyear;
    @FXML
    private ComboBox<?> searchType;
    @FXML
    private ComboBox<?> year;
    @FXML
    private TextField searchText;
    @FXML
    private TextField notes;
    @FXML
    private TextField imageUrl;
    File imagefile = null;
    Stage stage = new Stage();
    byte[] pdfimage = null;
    String recordYear = null;
    private String registrationId = null;
    ObservableList<String> destinationlist = FXCollections.observableArrayList();
    ObservableList<InternalIncomingModel> recipientList = FXCollections.observableArrayList();
    Config config = new Config();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        refreshRecipienTableView();
//        refreshListCombobox(filleDirection(dirComboBoxlist));
        AppDate.setDateValue(incomingDay, incomingMonth, incomingYear);
        AppDate.setCurrentDate(incomingDay, incomingMonth, incomingYear);
        AppDate.setDateValue(circularDateday, circularDatemonth, circularDateyear);
        AppDate.setCurrentDate(circularDateday, circularDatemonth, circularDateyear);
        getTableRow(recipientTableView);
        getTableRowByInterKey(recipientTableView);
        destination.setItems(filleDestination(destinationlist));
    }

    private ObservableList filleDestination(ObservableList list) {
        try {
            ResultSet rs = DatabaseAccess.select("placenames", "UINTTYPE='داخلي'");
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

    private void refreshRecipienTableView() {
        recipientList.clear();
        recipienTableView();
    }

    private void recipienTableView() {
        try {
            ResultSet rs = DatabaseAccess.select("internalincoming", "RECORD_YEAR ='" + HijriCalendar.getSimpleYear() + "' ORDER BY REGIS_NO DESC");
            while (rs.next()) {
                recipientList.add(new InternalIncomingModel(
                        rs.getString("REGIS_NO"),
                        rs.getString("RECIPIENT_DATE"),
                        rs.getString("CIRCULAR_NO"),
                        rs.getString("CIRCULAR_DATE"),
                        rs.getString("CIRCULAR_DIR"),
                        rs.getString("TOPIC"),
                        rs.getString("SAVE_FILE"),
                        rs.getString("NOTES")
                ));
            }
            rs.close();
        } catch (SQLException | IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        regisNO_col.setCellValueFactory(new PropertyValueFactory<>("regisNo"));
        recipientDate_col.setCellValueFactory(new PropertyValueFactory<>("recipientDate"));
        circularNo_col.setCellValueFactory(new PropertyValueFactory<>("circularNo"));
        circularDate_col.setCellValueFactory(new PropertyValueFactory<>("circularDate"));
        circularDir_col.setCellValueFactory(new PropertyValueFactory<>("circularDir"));
        topic_col.setCellValueFactory(new PropertyValueFactory<>("topic"));
        notes_col.setCellValueFactory(new PropertyValueFactory<>("notes"));

        Callback<TableColumn<InternalIncomingModel, String>, TableCell<InternalIncomingModel, String>> cellFactory
                = (final TableColumn<InternalIncomingModel, String> param) -> {
                    final TableCell<InternalIncomingModel, String> cell = new TableCell<InternalIncomingModel, String>() {

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
                                if (registrationId == null) {
                                    FormValidation.showAlert(null, "اختر السجل من الجدول", Alert.AlertType.ERROR);
                                } else {
                                    pdfimage = DatabaseAccess.getInternalIncomingPdfFile(registrationId, recordYear);
                                    ShowPdf.writePdf(pdfimage);
                                    pdfimage = null;
                                    registrationId = null;
                                    recordYear = null;
                                    recipientTableView.setStyle("-fx-background-color: #FFFFFF");
                                }
                            } catch (Exception ex) {
                                FormValidation.showAlert(null, "لا توجد صورة", Alert.AlertType.ERROR);
                            }
                        });
                        btn.setStyle("-fx-font-family: 'URW DIN Arabic';"
                                + "    -fx-font-size: 10px;"
                                + "    -fx-background-color: #FFFFFF;"
                                + "    -fx-background-radius: 0;"
                                + "    -fx-text-fill: #FFFFFF;"
                                + "    -fx-effect: dropshadow(three-pass-box,#3C3B3B, 20, 0, 5, 5); ");
                        Image image = new Image("/images/newPdf.png");
                        ImageView view = new ImageView(image);
                        btn.setGraphic(view);
                        setGraphic(btn);
                        setText(null);
                    }

                }
            };
                    return cell;
                };

        Callback<TableColumn<InternalIncomingModel, String>, TableCell<InternalIncomingModel, String>> cellFactory1
                = (final TableColumn<InternalIncomingModel, String> param) -> {
                    final TableCell<InternalIncomingModel, String> cell = new TableCell<InternalIncomingModel, String>() {

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
                                if (registrationId == null) {
                                    FormValidation.showAlert(null, "اختر السجل من الجدول", Alert.AlertType.ERROR);
                                } else {
                                    DatabaseAccess.insertImage("internalincoming", " `REGIS_NO` ='" + registrationId + "' AND RECORD_YEAR ='" + recordYear + "'");
                                    registrationId = null;
                                    recordYear = null;
                                }
                            } catch (Exception ex) {
                                FormValidation.showAlert(null, "لم يتم المسح", Alert.AlertType.ERROR);
                            }
                        });
                        btn.setStyle("-fx-font-family: 'URW DIN Arabic';"
                                + "    -fx-font-size: 10px;"
                                + "    -fx-background-color: #1E3606;"
                                + "    -fx-background-radius: 0;"
                                + "    -fx-text-fill: #FFFFFF;"
                                + "    -fx-effect: dropshadow(three-pass-box,#3C3B3B, 20, 0, 5, 5); ");
                        Image image = new Image("/images/scaner.png");
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
        addImage_col.setCellFactory(cellFactory1);
        recipientTableView.setItems(recipientList);
    }

    @FXML
    private File getImageUrle(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter ext = new FileChooser.ExtensionFilter("PDF  files(*.pdf)", "*.PDF");
        fileChooser.getExtensionFilters().addAll(ext);
        imagefile = fileChooser.showOpenDialog(stage);
        setImageUrl(imagefile.getPath());
        return imagefile;
    }

    @FXML
    private void insertData(ActionEvent event) throws IOException {
        String tableName = "internalincoming";
        recordYear = Integer.toString(HijriCalendar.getSimpleYear());
        String fieldName = null;
        String[] data = {DatabaseAccess.getRegistrationNum(), getIncomingDate(), getCircularNumber(), getCircularDate(), getDestination(), getTopic(), getSaveFaile(), getNotes(), recordYear};
        String valuenumbers = null;
        if (imagefile != null) {
            fieldName = "`REGIS_NO`,`RECIPIENT_DATE`,`CIRCULAR_NO`,`CIRCULAR_DATE`,`CIRCULAR_DIR`,`TOPIC`,`SAVE_FILE`,`NOTES`,`RECORD_YEAR`,`IMAGE`";
            valuenumbers = "?,?,?,?,?,?,?,?,?,?";
        } else {
            fieldName = "`REGIS_NO`,`RECIPIENT_DATE`,`CIRCULAR_NO`,`CIRCULAR_DATE`,`CIRCULAR_DIR`,`TOPIC`,`SAVE_FILE`,`NOTES`,`RECORD_YEAR`";
            valuenumbers = "?,?,?,?,?,?,?,?,?";
        }
        boolean circularNumberState = FormValidation.textFieldNotEmpty(circularNumber, "الرجاء ادخال رقم المعاملة");
        boolean topicState = FormValidation.textFieldNotEmpty(topic, "الرجاء ادخال جهة الوارد");
        boolean circularNumberExisting = FormValidation.ifexisting("internalincoming", "CIRCULAR_NO", "CIRCULAR_NO = '" + getCircularNumber() + "'AND CIRCULAR_DIR = '" + getDestination() + "'AND RECORD_YEAR = '" + recordYear + "'", "تم حفظ المعاملة مسبقا");
        if (circularNumberState && topicState && circularNumberExisting) {
            try {
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, data, imagefile);
                registrationId = DatabaseAccess.getRegistrationNum();
                DatabaseAccess.updatRegistrationNum();
                refreshRecipienTableView();
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void updateData(ActionEvent event) throws IOException {
        String tableName = "internalincoming";
        String fieldName = null;
        String[] data = {getIncomingDate(), getCircularNumber(), getCircularDate(), getDestination(), getTopic(), getSaveFaile(), getNotes()};
        if (imagefile != null) {
            fieldName = "`RECIPIENT_DATE`=?,`CIRCULAR_NO`=?,`CIRCULAR_DATE`=?,`CIRCULAR_DIR`=?,`TOPIC`=?,`SAVE_FILE`=?,`NOTES`=?,`IMAGE`=?";
        } else {
            fieldName = "`RECIPIENT_DATE`=?,`CIRCULAR_NO`=?,`CIRCULAR_DATE`=?,`CIRCULAR_DIR`=?,`TOPIC`=?,`SAVE_FILE`=?,`NOTES`=?";
        }
        boolean circularNumberState = FormValidation.textFieldNotEmpty(circularNumber, "الرجاء ادخال رقم المعاملة");
        boolean topicState = FormValidation.textFieldNotEmpty(topic, "الرجاء ادخال جهة الوارد");

        if (circularNumberState && topicState) {
            try {
                DatabaseAccess.updat(tableName, fieldName, data, "REGIS_NO = '" + registrationId + "'AND RECORD_YEAR = '" + recordYear + "'", imagefile);
                refreshRecipienTableView();
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void printBarcode(ActionEvent event) throws JRException {
        try {
            if (registrationId != null) {
                Connection con = DatabaseConniction.dbConnector();
                JasperDesign recipientReport = JRXmlLoader.load(config.getAppURL() + "\\reports\\RecipientBarcod.jrxml");
                ResultSet rs = DatabaseAccess.select("internalincoming", "REGIS_NO = '" + registrationId + "'");
                String regisNo = null;
                String recipientDate = null;
                String circularDir = null;
                int quRegisNo = 0;
                String unitName = null;
                String saveFile = null;
                while (rs.next()) {
                    regisNo = ArabicSetting.EnglishToarabic(Integer.toString(rs.getInt("REGIS_NO")));
                    recipientDate = ArabicSetting.EnglishToarabic(rs.getString("RECIPIENT_DATE")) + "هـ";
                    circularDir = rs.getString("CIRCULAR_DIR");
                    saveFile = ArabicSetting.EnglishToarabic(rs.getString("SAVE_FILE"));
                    quRegisNo = rs.getInt("REGIS_NO");
                    unitName = DatabaseAccess.getUintName();
                }
                Map barrcod = new HashMap();
                barrcod.put("ex_id", regisNo);
                barrcod.put("ex_date", recipientDate);
                barrcod.put("dir_to", circularDir);
                barrcod.put("qex_id", quRegisNo);
                barrcod.put("unitName", unitName);
                barrcod.put("savefile", saveFile);
                JasperReport jr = JasperCompileManager.compileReport(recipientReport);
                JasperPrint jp = JasperFillManager.fillReport(jr, barrcod, con);
                JasperPrintManager.printReport(jp, false);
//                JasperViewer.viewReport(jp, false);
            } else {
                showAlert("", "اختر السجل من الجدول");
            }

        } catch (IOException | SQLException | JRException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void addNames(ActionEvent event) {
        if (registrationId != null) {
            App.lodAddNmaesPage(registrationId, AppDate.getYear(getIncomingDate()));
        } else {
            showAlert("", "اختر السجل من الجدول");
        }
    }

    public void getTableRow(TableView table) {
        table.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                ObservableList<InternalIncomingModel> list = FXCollections.observableArrayList();
                list = table.getSelectionModel().getSelectedItems();
                if (list.isEmpty()) {
                    FormValidation.showAlert("", "لاتوجد بيانات");
                } else {
                    try {
                        ResultSet rs = DatabaseAccess.select("internalincoming", "REGIS_NO = '" + list.get(0).getRegisNo() + "'");
                        setCircularDate(list.get(0).getCircularDate());
                        setIncomingDate(list.get(0).getRecipientDate());
                        setCircularNumber(list.get(0).getCircularNo());
                        setDestination(list.get(0).getCircularDir());
                        setTopic(list.get(0).getTopic());
                        if (rs.next()) {
                            setSaveFaile(rs.getString("SAVE_FILE"));
                            setNotes(rs.getString("NOTES"));
                            recordYear = rs.getString("RECORD_YEAR");
                        }
                        registrationId = list.get(0).getRegisNo();
                    } catch (IOException | SQLException ex) {
                        FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
                    }
                }
            }
        });
    }

    private void getTableRowByInterKey(TableView table) {
        table.setOnKeyPressed(new EventHandler() {
            @Override
            public void handle(Event event) {
                ObservableList<InternalIncomingModel> list = FXCollections.observableArrayList();
                list = table.getSelectionModel().getSelectedItems();
                if (list.isEmpty()) {
                    FormValidation.showAlert("", "لاتوجد بيانات");
                } else {
                    try {
                        ResultSet rs = DatabaseAccess.select("internalincoming", "REGIS_NO = '" + list.get(0).getRegisNo() + "'");
                        setCircularDate(list.get(0).getCircularDate());
                        setIncomingDate(list.get(0).getRecipientDate());
                        setCircularNumber(list.get(0).getCircularNo());
                        setDestination(list.get(0).getCircularDir());
                        setTopic(list.get(0).getTopic());
                        if (rs.next()) {
                            setSaveFaile(rs.getString("SAVE_FILE"));
                            setNotes(rs.getString("NOTES"));
                            recordYear = rs.getString("RECORD_YEAR");
                        }
                        registrationId = list.get(0).getRegisNo();
                    } catch (IOException | SQLException ex) {
                        FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
                    }
                }
            }
        });
    }

    @FXML
    private void searchData(ActionEvent event) {
    }

    @FXML
    private void clear(ActionEvent event) {
        setCircularNumber(null);
        setTopic(null);
        setDestination(null);
        setNotes(null);
        setImageUrl(null);
        setSaveFaile(null);
    }

    @FXML
    private void addtoLeaderDisplay(ActionEvent event) {
        String tableName = "displaydata";
        String[] data = {HijriCalendar.getSimpleDate(), "عرض القائد", topic.getText(), destination.getValue()};
        String fieldName = "`DISPLAYDATE`,`DISPLAYTYPE`,`TOPIC`,`DESTINATION`";
        String valuenumbers = "?,?,?,?";

        boolean idState = FormValidation.notNull(registrationId, "الرجاءاختر السجل من الجدول");

        if (idState) {
            try {
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, data);
                FormValidation.showAlert(null, "تم اضافة المعاملة الى عرض القائد", Alert.AlertType.INFORMATION);
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void addtoLeaderSignature(ActionEvent event) {
        String tableName = "displaydata";
        String[] data = {HijriCalendar.getSimpleDate(), "توقيع القائد", topic.getText(), destination.getValue()};
        String fieldName = "`DISPLAYDATE`,`DISPLAYTYPE`,`TOPIC`,`DESTINATION`";
        String valuenumbers = "?,?,?,?";

        boolean idState = FormValidation.notNull(registrationId, "الرجاءاختر السجل من الجدول");

        if (idState) {
            try {
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, data);
                FormValidation.showAlert(null, "تم اضافة المعاملة الى توقيع القائد", Alert.AlertType.INFORMATION);
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void addtoManagerSignature(ActionEvent event) {
        String tableName = "displaydata";
        String[] data = {HijriCalendar.getSimpleDate(), "توقيع الركن", topic.getText(), destination.getValue()};
        String fieldName = "`DISPLAYDATE`,`DISPLAYTYPE`,`TOPIC`,`DESTINATION`";
        String valuenumbers = "?,?,?,?";

        boolean idState = FormValidation.notNull(registrationId, "الرجاءاختر السجل من الجدول");

        if (idState) {
            try {
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, data);
                FormValidation.showAlert(null, "تم اضافة المعاملة الى توقيع الركن", Alert.AlertType.INFORMATION);
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void addtoManagerDisplay(ActionEvent event) {
        String tableName = "displaydata";
        String[] data = {HijriCalendar.getSimpleDate(), "عرض الركن", topic.getText(), destination.getValue()};
        String fieldName = "`DISPLAYDATE`,`DISPLAYTYPE`,`TOPIC`,`DESTINATION`";
        String valuenumbers = "?,?,?,?";

        boolean idState = FormValidation.notNull(registrationId, "الرجاءاختر السجل من الجدول");

        if (idState) {
            try {
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, data);
                FormValidation.showAlert(null, "تم اضافة المعاملة الى عرض الركن", Alert.AlertType.INFORMATION);
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void addtoManagerSmallSignature(ActionEvent event) {
        String tableName = "displaydata";
        String[] data = {HijriCalendar.getSimpleDate(), "تاشير الركن", topic.getText(), destination.getValue()};
        String fieldName = "`DISPLAYDATE`,`DISPLAYTYPE`,`TOPIC`,`DESTINATION`";
        String valuenumbers = "?,?,?,?";

        boolean idState = FormValidation.notNull(registrationId, "الرجاءاختر السجل من الجدول");

        if (idState) {
            try {
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, data);
                FormValidation.showAlert(null, "تم اضافة المعاملة الى تاشير الركن", Alert.AlertType.INFORMATION);
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void addtoManagerOrders(ActionEvent event) {
        String tableName = "displaydata";
        String[] data = {HijriCalendar.getSimpleDate(), "توجيه الركن", topic.getText(), destination.getValue()};
        String fieldName = "`DISPLAYDATE`,`DISPLAYTYPE`,`TOPIC`,`DESTINATION`";
        String valuenumbers = "?,?,?,?";

        boolean idState = FormValidation.notNull(registrationId, "الرجاءاختر السجل من الجدول");

        if (idState) {
            try {
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, data);
                FormValidation.showAlert(null, "تم اضافة المعاملة الى توجيه الركن", Alert.AlertType.INFORMATION);
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    public String getCircularNumber() {
        return circularNumber.getText();
    }

    public void setCircularNumber(String circularNumber) {
        this.circularNumber.setText(circularNumber);
    }

    public String getTopic() {
        return topic.getText();
    }

    public void setTopic(String topic) {
        this.topic.setText(topic);
    }

    public String getSaveFaile() {
        return saveFaile.getText();
    }

    public void setSaveFaile(String saveFaile) {
        this.saveFaile.setText(saveFaile);
    }

    public String getDestination() {
        return destination.getValue();
    }

    public void setDestination(String destination) {
        this.destination.setValue(destination);
    }

    public String getCircularDate() {
        return AppDate.getDate(circularDateday, circularDatemonth, circularDateyear);
    }

    public void setCircularDate(String date) {
        AppDate.setSeparateDate(circularDateday, circularDatemonth, circularDateyear, date);
    }

    public String getIncomingDate() {
        return AppDate.getDate(incomingDay, incomingMonth, incomingYear);
    }

    public void setIncomingDate(String date) {
        AppDate.setSeparateDate(incomingDay, incomingMonth, incomingYear, date);
    }

    public String getSearchText() {
        return searchText.getText();
    }

    public void setSearchText(String searchText) {
        this.searchText.setText(searchText);
    }

    public String getNotes() {
        return notes.getText();
    }

    public void setNotes(String notes) {
        this.notes.setText(notes);
    }

    public String getImageUrl() {
        return imageUrl.getText();
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl.setText(imageUrl);
    }

}
