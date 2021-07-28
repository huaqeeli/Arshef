package controllers;

import Validation.FormValidation;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import modeles.ArchefModel;
import modeles.ExportsModel;

public class ExportsPageController implements Initializable {

    @FXML
    private ComboBox<String> searchType;
    @FXML
    private ComboBox<String> year;
    @FXML
    private TextField searchText;
    @FXML
    private Button searchButton1;
    @FXML
    private TextField topic;
    @FXML
    private ComboBox<String> destination;
    @FXML
    private TextField saveFile;
    @FXML
    private TextField imageUrl;
    @FXML
    private ComboBox<?> entryDateDay;
    @FXML
    private ComboBox<?> entryDateMonth;
    @FXML
    private ComboBox<?> entryDateYear;
    @FXML
    private TextField exportNum;
    @FXML
    private ComboBox<?> exportDateDay;
    @FXML
    private ComboBox<?> exportDateMonth;
    @FXML
    private ComboBox<?> exportDateYear;
    @FXML
    private TextField notes;
    @FXML
    private TableView<ExportsModel> exportsTable;
    @FXML
    private TableColumn<?, ?> topic_col;
    @FXML
    private TableColumn<?, ?> destination_col;
    @FXML
    private TableColumn<?, ?> exportNum_col;
    @FXML
    private TableColumn<?, ?> exportDate_col;
    @FXML
    private TableColumn<?, ?> notes_col;
    @FXML
    private TableColumn<?, ?> saveFile_col;
    @FXML
    private TableColumn<ExportsModel, String> exportsImage_col;
    @FXML
    private TableColumn<?, ?> squence_col;
    @FXML
    private TextField internalincomingnum;
    @FXML
    private TableColumn<?, ?> entrydat_col;

    ObservableList<ExportsModel> Exportslist = FXCollections.observableArrayList();
    ObservableList<String> placeComboBoxlist = FXCollections.observableArrayList();
    ObservableList<String> searchTypelist = FXCollections.observableArrayList("البحث برقم الصادر", "البحث بتاريخ البيان", "البحث بالموضوع", "البحث بجهة المعاملة");
    ObservableList<String> uintComboBoxlist = FXCollections.observableArrayList();

    File imagefile = null;
    Stage stage = new Stage();
    byte[] pdfimage = null;
    String arshefyear = null;
    String id = null;
    String enteryDate = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AppDate.setDateValue(exportDateDay, exportDateMonth, exportDateYear);
        AppDate.setDateValue(entryDateDay, entryDateMonth, entryDateYear);
        AppDate.setCurrentDate(entryDateDay, entryDateMonth, entryDateYear);
        FillComboBox.fillComboBox(searchTypelist, searchType);
        AppDate.setYearValue(year);
        clearListCombobox();
        refreshListCombobox();
        refreshExportTableView();
        getTableRow(exportsTable);
        getTableRowByInterKey(exportsTable);
    }

    private ObservableList filleUint(ObservableList list) {
        try {
            ResultSet rs = DatabaseAccess.select("placenames", "UINTTYPE='خارجي'");
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

    private void refreshListCombobox() {
        destination.setItems(filleUint(uintComboBoxlist));
    }

    public void clearListCombobox() {
        destination.getItems().clear();
    }

    @FXML
    private void searchData(ActionEvent event) {
    }

    @FXML
    private File getImageUrle(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
//        FileChooser.ExtensionFilter ext1 = new FileChooser.ExtensionFilter("JPG files(*.jpg)", "*.JPG");
//        FileChooser.ExtensionFilter ext2 = new FileChooser.ExtensionFilter("PNG files(*.png)", "*.PNG");
//        FileChooser.ExtensionFilter ext3 = new FileChooser.ExtensionFilter("JPEG‬‬  files(*.jpeg)", "*.JPEG‬‬");
        FileChooser.ExtensionFilter ext4 = new FileChooser.ExtensionFilter("PDF  files(*.pdf)", "*.PDF");
        fileChooser.getExtensionFilters().addAll(ext4);
        imagefile = fileChooser.showOpenDialog(stage);
        setImageUrl(imagefile.getPath());
        return imagefile;
    }

    @FXML
    private void save(ActionEvent event) {
        String tableName = "exportsdata";
        String fieldName = null;
        String[] data = {getTopic(), getDestination(), getExportNum(), getExportDate(), getNotes(), getSaveFile(), getInternalincomingnum(), getEntryDate()};
        String valuenumbers = null;
        if (imagefile != null) {
            fieldName = "`TOPIC`,`DESTINATION`,`EXPORTNUM`,`EXPORTDATE`,`NOTES`,`SAVEFILE`,`INTERNALINCOMINGNUM`,`ENTRYDATE`,`EXPORTSIMAGE`";
            valuenumbers = "?,?,?,?,?,?,?,?,?";
        } else {
            fieldName = "`TOPIC`,`DESTINATION`,`EXPORTNUM`,`EXPORTDATE`,`NOTES`,`SAVEFILE`,`INTERNALINCOMINGNUM`,`ENTRYDATE`";
            valuenumbers = "?,?,?,?,?,?,?,?";
        }

        boolean destinationState = FormValidation.comboBoxNotEmpty(destination, "الرجاء ادخال جهة المعاملة");
        boolean topicState = FormValidation.textFieldNotEmpty(topic, "الرجاء ادخال الموضوع");
        boolean saveFileState = FormValidation.textFieldNotEmpty(saveFile, "الرجاء ادخال ملف الحفظ");

        if (destinationState && topicState && saveFileState) {
            try {
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, data, imagefile);
                refreshExportTableView();
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void edit(ActionEvent event) {
        String tableName = "exportsdata";
        String fieldName = null;
        String[] data = {getTopic(), getDestination(), getExportNum(), getExportDate(), getNotes(), getSaveFile(), getInternalincomingnum()};
        if (imagefile != null) {
            fieldName = "`TOPIC`=?,`DESTINATION`=?,`EXPORTNUM`=?,`EXPORTDATE`=?,`NOTES`=?,`SAVEFILE`=?,`INTERNALINCOMINGNUM`=?,`EXPORTSIMAGE`=?";
        } else {
            fieldName = "`TOPIC`=?,`DESTINATION`=?,`EXPORTNUM`=?,`EXPORTDATE`=?,`NOTES`=?,`SAVEFILE`=?,`INTERNALINCOMINGNUM`=?";
        }

        boolean destinationState = FormValidation.comboBoxNotEmpty(destination, "الرجاء ادخال جهة المعاملة");
        boolean topicState = FormValidation.textFieldNotEmpty(topic, "الرجاء ادخال الموضوع");
        boolean saveFileState = FormValidation.textFieldNotEmpty(saveFile, "الرجاء ادخال ملف الحفظ");

        if (destinationState && topicState && saveFileState) {
            try {
                DatabaseAccess.updat(tableName, fieldName, data, "ID = '" + id + "'", imagefile);
                refreshExportTableView();
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void delete(ActionEvent event) {
        try {
            DatabaseAccess.delete("exportsdata", "ID = '" + id + "'");
            refreshExportTableView();
            clear(event);
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clear(ActionEvent event) {
        setEntryDate(HijriCalendar.getSimpleDate());
        setInternalincomingnum(null);
        setTopic(null);
        setDestination(null);
        setSaveFile(null);
        setExportNum(null);
        setExportDate(null);
        setImageUrl(null);
    }

    @FXML
    private void addtoLeaderDisplay(ActionEvent event) {
        String tableName = "displaydata";
        String[] data = {HijriCalendar.getSimpleDate(), "عرض القائد", topic.getText(), destination.getValue()};
        String fieldName = "`DISPLAYDATE`,`DISPLAYTYPE`,`TOPIC`,`DESTINATION`";
        String valuenumbers = "?,?,?,?";

        boolean idState = FormValidation.notNull(id, "الرجاءاختر السجل من الجدول");

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

        boolean idState = FormValidation.notNull(id, "الرجاءاختر السجل من الجدول");

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

        boolean idState = FormValidation.notNull(id, "الرجاءاختر السجل من الجدول");

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

        boolean idState = FormValidation.notNull(id, "الرجاءاختر السجل من الجدول");

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

        boolean idState = FormValidation.notNull(id, "الرجاءاختر السجل من الجدول");

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

        boolean idState = FormValidation.notNull(id, "الرجاءاختر السجل من الجدول");

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

    public String getYear() {
        return year.getValue();
    }

    public void setYear(String year) {
        this.year.setValue(year);
    }

    public String getSearchText() {
        return searchText.getText();
    }

    public void setSearchText(String searchText) {
        this.searchText.setText(searchText);
    }

    public String getTopic() {
        return topic.getText();
    }

    public void setTopic(String topic) {
        this.topic.setText(topic);
    }

    public String getDestination() {
        return destination.getValue();
    }

    public void setDestination(String destination) {
        this.destination.setValue(destination);
    }

    public String getSaveFile() {
        return saveFile.getText();
    }

    public void setSaveFile(String saveFile) {
        this.saveFile.setText(saveFile);
    }

    public String getImageUrl() {
        return imageUrl.getText();
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl.setText(imageUrl);
    }

    public String getExportNum() {
        return exportNum.getText();
    }

    public void setExportNum(String exportNum) {
        this.exportNum.setText(exportNum);
    }

    public String getNotes() {
        return notes.getText();
    }

    public void setNotes(String notes) {
        this.notes.setText(notes);
    }

    public String getInternalincomingnum() {
        return internalincomingnum.getText();
    }

    public void setInternalincomingnum(String internalincomingnum) {
        this.internalincomingnum.setText(internalincomingnum);
    }

    public String getEntryDate() {
        return AppDate.getDate(entryDateDay, entryDateMonth, entryDateYear);
    }

    public void setEntryDate(String date) {
        AppDate.setSeparateDate(entryDateDay, entryDateMonth, entryDateYear, date);
    }

    public String getExportDate() {
        if (exportDateDay.getValue() != null && exportDateMonth.getValue() != null && exportDateYear.getValue() != null) {
            return AppDate.getDate(exportDateDay, exportDateMonth, exportDateYear);
        }
        return null;
    }

    public void setExportDate(String date) {
        AppDate.setSeparateDate(exportDateDay, exportDateMonth, exportDateYear, date);

    }

    private void refreshExportTableView() {
        try {
            Exportslist.clear();
            exportTableView(DatabaseAccess.getData("SELECT * FROM exportsdata ORDER BY ENTRYDATE DESC"));
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    private void exportTableView(ResultSet rs) {
        try {
            int squence = 0;
            while (rs.next()) {
                squence++;
                Exportslist.add(new ExportsModel(
                        rs.getString("ID"),
                        rs.getString("TOPIC"),
                        rs.getString("DESTINATION"),
                        rs.getString("EXPORTNUM"),
                        rs.getString("EXPORTDATE"),
                        rs.getString("NOTES"),
                        rs.getString("SAVEFILE"),
                        rs.getString("ENTRYDATE")
                ));
            }
            rs.close();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        topic_col.setCellValueFactory(new PropertyValueFactory<>("topic"));
        destination_col.setCellValueFactory(new PropertyValueFactory<>("destination"));
        exportNum_col.setCellValueFactory(new PropertyValueFactory<>("exportNum"));
        exportDate_col.setCellValueFactory(new PropertyValueFactory<>("exportDate"));
        notes_col.setCellValueFactory(new PropertyValueFactory<>("notes"));
        saveFile_col.setCellValueFactory(new PropertyValueFactory<>("saveFile"));
        squence_col.setCellValueFactory(new PropertyValueFactory<>("id"));
        entrydat_col.setCellValueFactory(new PropertyValueFactory<>("entryDate"));

        Callback<TableColumn<ExportsModel, String>, TableCell<ExportsModel, String>> cellFactory
                = (final TableColumn<ExportsModel, String> param) -> {
                    final TableCell<ExportsModel, String> cell = new TableCell<ExportsModel, String>() {

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
                                        if (id == null) {
                                            FormValidation.showAlert(null, "اختر السجل من الجدول", Alert.AlertType.ERROR);
                                        } else {
                                            pdfimage = DatabaseAccess.getExportPdfFile(id, enteryDate);
                                            ShowPdf.writePdf(pdfimage);
                                            pdfimage = null;
                                            id = null;
                                            enteryDate = null;
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

        exportsImage_col.setCellFactory(cellFactory);

        exportsTable.setItems(Exportslist);
    }

    public void getTableRow(TableView table) {
        table.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                ObservableList<ExportsModel> list = FXCollections.observableArrayList();
                list = table.getSelectionModel().getSelectedItems();
                if (!list.isEmpty()) {
                    id = list.get(0).getId();
                    enteryDate = list.get(0).getEntryDate();
                    setInternalincomingnum(list.get(0).getInternalincomingnum());
                    setEntryDate(list.get(0).getEntryDate());
                    setDestination(list.get(0).getDestination());
                    setTopic(list.get(0).getTopic());
                    setSaveFile(list.get(0).getSaveFile());
                    setExportNum(list.get(0).getExportNum());
                    setExportDate(list.get(0).getExportDate());
                    try {
                        ResultSet rs = DatabaseAccess.getData("SELECT INTERNALINCOMINGNUM FROM exportsdata WHERE ID = '" + id + "'");
                        if (rs.next()) {
                            setInternalincomingnum(rs.getString("INTERNALINCOMINGNUM"));
                        }
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
                ObservableList<ExportsModel> list = FXCollections.observableArrayList();
                list = table.getSelectionModel().getSelectedItems();
                if (!list.isEmpty()) {
                    id = list.get(0).getId();
                    enteryDate = list.get(0).getEntryDate();
                    setInternalincomingnum(list.get(0).getInternalincomingnum());
                    setEntryDate(list.get(0).getEntryDate());
                    setDestination(list.get(0).getDestination());
                    setTopic(list.get(0).getTopic());
                    setSaveFile(list.get(0).getSaveFile());
                    setExportDate(list.get(0).getExportDate());
                    try {
                        ResultSet rs = DatabaseAccess.getData("SELECT INTERNALINCOMINGNUM FROM exportsdata WHERE ID = '" + id + "'");
                        if (rs.next()) {
                            setInternalincomingnum(rs.getString("INTERNALINCOMINGNUM"));
                        }
                    } catch (IOException | SQLException ex) {
                        FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
                    }
                }
            }
        });
    }
}
