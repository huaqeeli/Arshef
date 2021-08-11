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
import modeles.InternalExportsModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class InternalExportsPageController implements Initializable {

    @FXML
    private ComboBox<?> searchType;
    @FXML
    private ComboBox<?> year;
    @FXML
    private TextField searchText;
    @FXML
    private ComboBox<String> destination;
    @FXML
    private TextField topic;
    @FXML
    private TextField saveFaile;
    @FXML
    private TextField notes;
    @FXML
    private TextField imageUrl;
    @FXML
    private ComboBox<?> exportsDay;
    @FXML
    private ComboBox<?> exportsMonth;
    @FXML
    private ComboBox<?> exportsYear;
    @FXML
    private TextField incomingNum;
    @FXML
    private TableView<InternalExportsModel> exportsTable;
    @FXML
    private TableColumn<?, ?> exportsDate_col;
    @FXML
    private TableColumn<?, ?> exportsDir_col;
    @FXML
    private TableColumn<?, ?> regisNO_col;
    @FXML
    private TableColumn<?, ?> topic_col;
    @FXML
    private TableColumn<?, ?> notes_col;
    @FXML
    private TableColumn<InternalExportsModel, String> image_col;
    @FXML
    private TableColumn<InternalExportsModel, String> addImage_col;
    File imagefile = null;
    Stage stage = new Stage();
    byte[] pdfimage = null;
    String recordYear = null;
    private String registrationId = null;
    ObservableList<String> destinationlist = FXCollections.observableArrayList();
    ObservableList<InternalExportsModel> exportsList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        refreshRecipienTableView();
        AppDate.setDateValue(exportsDay, exportsMonth, exportsYear);
        AppDate.setCurrentDate(exportsDay, exportsMonth, exportsYear);
        getTableRow(exportsTable);
        getTableRowByInterKey(exportsTable);
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
        exportsList.clear();
        recipienTableView();
    }

    private void recipienTableView() {
        try {
            ResultSet rs = DatabaseAccess.select("internalexports", "RECORDYEAR ='" + HijriCalendar.getSimpleYear() + "' ORDER BY REGISNO DESC");
            while (rs.next()) {
                exportsList.add(new InternalExportsModel(
                        rs.getString("REGISNO"),
                        rs.getString("EXPORTDATE"),
                        rs.getString("DESTINATION"),
                        rs.getString("TOPIC"),
                        rs.getString("SAVEFILE"),
                        rs.getString("NOTES")
                ));
            }
            rs.close();
        } catch (SQLException | IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        regisNO_col.setCellValueFactory(new PropertyValueFactory<>("regisNO"));
        exportsDate_col.setCellValueFactory(new PropertyValueFactory<>("exportsDate"));
        exportsDir_col.setCellValueFactory(new PropertyValueFactory<>("destination"));
        topic_col.setCellValueFactory(new PropertyValueFactory<>("topic"));
        notes_col.setCellValueFactory(new PropertyValueFactory<>("notes"));

        Callback<TableColumn<InternalExportsModel, String>, TableCell<InternalExportsModel, String>> cellFactory
                = (final TableColumn<InternalExportsModel, String> param) -> {
                    final TableCell<InternalExportsModel, String> cell = new TableCell<InternalExportsModel, String>() {

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
        Callback<TableColumn<InternalExportsModel, String>, TableCell<InternalExportsModel, String>> cellFactory1
                = (final TableColumn<InternalExportsModel, String> param) -> {
                    final TableCell<InternalExportsModel, String> cell = new TableCell<InternalExportsModel, String>() {

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
                                            DatabaseAccess.insertImage("internalexports", " REGISNO ='" + registrationId + "' AND RECORDYEAR ='" + recordYear + "'");
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

        addImage_col.setCellFactory(cellFactory1);
        image_col.setCellFactory(cellFactory);
        exportsTable.setItems(exportsList);
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
        String tableName = "internalexports";
        recordYear = Integer.toString(HijriCalendar.getSimpleYear());
        String fieldName = null;
        String[] data = {getRegistrationId(), getExportsDate(), getDestination(), getTopic(), getSaveFaile(), getNotes(), recordYear};
        String valuenumbers = null;
        if (imagefile != null) {
            fieldName = "`REGISNO`,`EXPORTDATE`,`DESTINATION`,`TOPIC`,`SAVEFILE`,`NOTES`,`RECORDYEAR`,`IMAGE`";
            valuenumbers = "?,?,?,?,?,?,?,?";
        } else {
            fieldName = "`REGISNO`,`EXPORTDATE`,`DESTINATION`,`TOPIC`,`SAVEFILE`,`NOTES`,`RECORDYEAR`";
            valuenumbers = "?,?,?,?,?,?,?";
        }

        boolean topicState = FormValidation.textFieldNotEmpty(topic, "الرجاء ادخال الموضوع");
        boolean destinationState = FormValidation.comboBoxNotEmpty(destination, "الرجاء ادخال جهة الصادر");

        if (topicState && destinationState) {
            try {
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, data, imagefile);
                registrationId = getRegistrationId();
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
        String tableName = "internalexports";
        String fieldName = null;
        String[] data = {getExportsDate(), getDestination(), getTopic(), getSaveFaile(), getNotes()};
        if (imagefile != null) {
            fieldName = "`EXPORTDATE`=?,`DESTINATION`=?,`TOPIC`=?,`SAVEFILE`=?,`NOTES`=?,`IMAGE`=?";
        } else {
            fieldName = "`EXPORTDATE`=?,`DESTINATION`=?,`TOPIC`=?,`SAVEFILE`=?,`NOTES`=?,";
        }

        boolean topicState = FormValidation.textFieldNotEmpty(topic, "الرجاء ادخال جهة الموضوع");

        if (topicState) {
            try {
                DatabaseAccess.updat(tableName, fieldName, data, "REGISNO = '" + registrationId + "'AND RECORDYEAR = '" + recordYear + "'", imagefile);
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
                JasperDesign recipientReport = JRXmlLoader.load("C:\\Users\\ابو ريان\\Documents\\NetBeansProjects\\Arshef\\src\\reports\\ExportingBarcod.jrxml");
                ResultSet rs = DatabaseAccess.select("internalexports", "REGISNO = '" + registrationId + "'");
                String regisNo = null;
                String recipientDate = null;
                String circularDir = null;
                int quRegisNo = 0;
                String unitName = null;
                String saveFile = null;
                while (rs.next()) {
                    regisNo = ArabicSetting.EnglishToarabic(Integer.toString(rs.getInt("REGISNO")));
                    recipientDate = ArabicSetting.EnglishToarabic(rs.getString("EXPORTDATE")) + "هـ";
                    circularDir = rs.getString("DESTINATION");
                    quRegisNo = rs.getInt("REGISNO");
                    saveFile = rs.getString("SAVEFILE");
                    unitName = DatabaseAccess.getUintName();
                }
                Map barrcod = new HashMap();
                barrcod.put("ex_id", regisNo);
                barrcod.put("ex_date", recipientDate);
                barrcod.put("dir_to", circularDir);
                barrcod.put("qex_id", quRegisNo);
                barrcod.put("savefile", saveFile);
                barrcod.put("unitName", unitName);
                JasperReport jr = JasperCompileManager.compileReport(recipientReport);
                JasperPrint jp = JasperFillManager.fillReport(jr, barrcod, con);
//            JasperPrintManager.printReport(jp, false);
                JasperViewer.viewReport(jp, false);
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
            App.lodAddNmaesPage(registrationId, AppDate.getYear(getExportsDate()));
        } else {
            showAlert("", "اختر السجل من الجدول");
        }
    }

    public void getTableRow(TableView table) {
        table.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                ObservableList<InternalExportsModel> list = FXCollections.observableArrayList();
                list = table.getSelectionModel().getSelectedItems();
                if (list.isEmpty()) {
                    FormValidation.showAlert("", "لاتوجد بيانات");
                } else {
                    try {
                        ResultSet rs = DatabaseAccess.select("internalexports", "REGISNO = '" + list.get(0).getRegisNO() + "'");
                        setExportsDate(list.get(0).getExportsDate());
                        setDestination(list.get(0).getDestination());
                        setTopic(list.get(0).getTopic());
                        if (rs.next()) {
                            setSaveFaile(rs.getString("SAVEFILE"));
                            setNotes(rs.getString("NOTES"));
                            recordYear = rs.getString("RECORDYEAR");
                        }
                        registrationId = list.get(0).getRegisNO();
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
                ObservableList<InternalExportsModel> list = FXCollections.observableArrayList();
                list = table.getSelectionModel().getSelectedItems();
                if (list.isEmpty()) {
                    FormValidation.showAlert("", "لاتوجد بيانات");
                } else {
                    try {
                        ResultSet rs = DatabaseAccess.select("internalexports", "REGISNO = '" + list.get(0).getRegisNO() + "'");
                        setExportsDate(list.get(0).getExportsDate());
                        setDestination(list.get(0).getDestination());
                        setTopic(list.get(0).getTopic());
                        if (rs.next()) {
                            setSaveFaile(rs.getString("SAVEFILE"));
                            setNotes(rs.getString("NOTES"));
                            recordYear = rs.getString("RECORDYEAR");
                        }
                        registrationId = list.get(0).getRegisNO();
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
        setIncomingNum(null);
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

    public String getIncomingNum() {
        return incomingNum.getText();
    }

    public void setIncomingNum(String incomingNum) {
        this.incomingNum.setText(incomingNum);
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

    public String getExportsDate() {
        return AppDate.getDate(exportsDay, exportsMonth, exportsYear);
    }

    public void setExportsDate(String date) {
        AppDate.setSeparateDate(exportsDay, exportsMonth, exportsYear, date);
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

    public String getRegistrationId() throws IOException {
        if (getIncomingNum() == null) {
            registrationId = DatabaseAccess.getRegistrationNum();
        } else {
            registrationId = getIncomingNum();
        }
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    @FXML
    private void getIncomingData(ActionEvent event) {
        try {
            ResultSet rs = DatabaseAccess.select("internalincoming", "REGIS_NO = '" + getIncomingNum() + "'");
            if (rs.next()) {
                setTopic(rs.getString("TOPIC"));
                setSaveFaile(rs.getString("SAVE_FILE"));
                setNotes(rs.getString("NOTES"));
            }
            rs.close();
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

}
