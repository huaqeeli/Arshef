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

public class InsertDataPageController implements Initializable {

    ObservableList<ArchefModel> Archeflist = FXCollections.observableArrayList();
    ObservableList<String> coursComboBoxlist = FXCollections.observableArrayList();
    ObservableList<String> placeComboBoxlist = FXCollections.observableArrayList();
    ObservableList<String> circularTypelist = FXCollections.observableArrayList(" ", "عادي", "سري", "سري للغاية", "سري /عاجل ", "سري للغاية/عاجل جدا", "عاحل", "عاجل جدا");
    ObservableList<String> searchTypelist = FXCollections.observableArrayList("البحث برقم المعاملة", "البحث برقم الوارد", "البحث بالموضوع", "البحث بجهة المعاملة");

    @FXML
    private TextField imageUrl;
    String circularID = null;
    String circularDate = null;
    File imagefile = null;
    Stage stage = new Stage();
    byte[] pdfimage = null;
    String arshefyear = null;
    boolean imageNotexisting = false;
    @FXML
    private TableView<ArchefModel> archefTable;
    @FXML
    private TableColumn<?, ?> circularid_col;
    @FXML
    private TableColumn<?, ?> circularDate_col;
    @FXML
    private TableColumn<?, ?> receiptNumber_col;
    @FXML
    private TableColumn<?, ?> receiptDate_col;
    @FXML
    private TableColumn<?, ?> topic_col;
    @FXML
    private TableColumn<?, ?> destination_col;
    @FXML
    private TableColumn<?, ?> saveFile_col;
    @FXML
    private TableColumn<?, ?> circularType_col;
    @FXML
    private TableColumn<ArchefModel, String> circularImage_col;
    @FXML
    private TableColumn<?, ?> squence_col;
    @FXML
    private TextField searchText;
    @FXML
    private TextField circularid;
    @FXML
    private ComboBox<String> circularDateDay;
    @FXML
    private ComboBox<String> circularDateMonth;
    @FXML
    private ComboBox<String> circularDateYear;
    @FXML
    private TextField receiptNumber;
    @FXML
    private ComboBox<String> receiptNumberDateDay;
    @FXML
    private ComboBox<String> receiptNumberDateMonth;
    @FXML
    private ComboBox<String> receiptNumberDateYear;
    @FXML
    private ComboBox<String> destination;
    @FXML
    private TextField topic;
    @FXML
    private TextField saveFile;
    @FXML
    private ComboBox<String> circularType;
    @FXML
    private ComboBox<String> searchType;
    @FXML
    private ComboBox<String> year;
    @FXML
    private Button searchButton1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        refreshArchefTableView();
        getTableRow(archefTable);
        getTableRowByInterKey(archefTable);
        FillComboBox.fillComboBox(circularTypelist, circularType);
        FillComboBox.fillComboBox(searchTypelist, searchType);
        AppDate.setDateValue(circularDateDay, circularDateMonth, circularDateYear);
        AppDate.setDateValue(receiptNumberDateDay, receiptNumberDateMonth, receiptNumberDateYear);
        AppDate.setCurrentDate(circularDateDay, circularDateMonth, circularDateYear);
        AppDate.setCurrentDate(receiptNumberDateDay, receiptNumberDateMonth, receiptNumberDateYear);
        AppDate.setYearValue(year);
        year.setValue(Integer.toString(HijriCalendar.getSimpleYear()));
        destination.setItems(filleCoursPlace(placeComboBoxlist));
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
    private void save(ActionEvent event) throws SQLException {
        String tableName = "arshefdata";
        String fieldName = null;
        arshefyear = AppDate.getYear(getCircularDate());
        String[] data = {getCircularid(), getCircularDate(), getReceiptNumber(), getReceiptNumberDate(), getDestination(), getTopic(), getSaveFile(), getCircularType(), arshefyear};
        String valuenumbers = null;
        if (imagefile != null) {
            fieldName = "`CIRCULARID`,`CIRCULARDATE`,`RECEIPTNUMBER`,`RECEIPTDATE`,`DESTINATION`,`TOPIC`,`SAVEFILE`,`CIRCULARTYPE`,`ARSHEFYEAR`,`CIRCULARIMAGE`";
            valuenumbers = "?,?,?,?,?,?,?,?,?,?";
        } else {
            fieldName = "`CIRCULARID`,`CIRCULARDATE`,`RECEIPTNUMBER`,`RECEIPTDATE`,`DESTINATION`,`TOPIC`,`SAVEFILE`,`CIRCULARTYPE`,`ARSHEFYEAR`";
            valuenumbers = "?,?,?,?,?,?,?,?,?";
        }

        boolean circularidState = FormValidation.textFieldNotEmpty(circularid, "الرجاء ادخال رقم المعاملة");
        boolean circularidTypeNumber = FormValidation.textFieldTypeNumber(circularid, "يقبل ارقام فقط");
        boolean circularidSNotexisting = FormValidation.ifexisting("arshefdata", "`CIRCULARID`,`ARSHEFYEAR`", "CIRCULARID ='" + getCircularid() + "' AND ARSHEFYEAR='" + arshefyear + "'", "تم ادخال رقم المعاملة مسبقا");
        boolean receiptNumberNotexisting = FormValidation.ifexisting("arshefdata", "`RECEIPTNUMBER`,`ARSHEFYEAR`", "RECEIPTNUMBER ='" + getReceiptNumber() + "' AND ARSHEFYEAR='" + arshefyear + "'", "تم ادخال رقم الوارد  مسبقا");
        boolean receiptNumberState = FormValidation.textFieldNotEmpty(receiptNumber, "الرجاء ادخال رقم الوارد");
        boolean receiptNumberTypeNumber = FormValidation.textFieldTypeNumber(receiptNumber, "يقبل ارقام فقط");
        boolean destinationState = FormValidation.comboBoxNotEmpty(destination, "الرجاء ادخال جهة المعاملة");
        boolean topicState = FormValidation.textFieldNotEmpty(topic, "الرجاء ادخال الموضوع");
        boolean saveFileState = FormValidation.textFieldNotEmpty(saveFile, "الرجاء ادخال ملف الحفظ");

        if (circularidState && circularidSNotexisting && receiptNumberNotexisting && receiptNumberState && destinationState && topicState && saveFileState && circularidTypeNumber && receiptNumberTypeNumber) {
            try {
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, data, imagefile);
                refreshArchefTableView();
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void edit(ActionEvent event) throws SQLException {
        String tableName = "arshefdata";
        String fieldName = null;
        arshefyear = AppDate.getYear(getCircularDate());
        String[] data = {getCircularid(), getCircularDate(), getReceiptNumber(), getReceiptNumberDate(), getDestination(), getTopic(), getSaveFile(), getCircularType()};
        if (imagefile != null) {
            fieldName = "`CIRCULARID`=?,`CIRCULARDATE`=?,`RECEIPTNUMBER`=?,`RECEIPTDATE`=?,`DESTINATION`=?,`TOPIC`=?,`SAVEFILE`=?,`CIRCULARTYPE`=?,`CIRCULARIMAGE`=?";
        } else {
            fieldName = "`CIRCULARID`=?,`CIRCULARDATE`=?,`RECEIPTNUMBER`=?,`RECEIPTDATE`=?,`DESTINATION`=?,`TOPIC`=?,`SAVEFILE`=?,`CIRCULARTYPE`=?";
        }

        boolean circularidState = FormValidation.textFieldNotEmpty(circularid, "الرجاء ادخال رقم المعاملة");
        boolean circularidTypeNumber = FormValidation.textFieldTypeNumber(circularid, "الرجاء ادخال رقم المعاملة");
        boolean receiptNumberState = FormValidation.textFieldNotEmpty(receiptNumber, "الرجاء ادخال رقم الوارد");
        boolean receiptNumberTypeNumber = FormValidation.textFieldTypeNumber(receiptNumber, "الرجاء ادخال رقم الوارد");
        boolean destinationState = FormValidation.comboBoxNotEmpty(destination, "الرجاء ادخال جهة المعاملة");
        boolean topicState = FormValidation.textFieldNotEmpty(topic, "الرجاء ادخال الموضوع");
        boolean saveFileState = FormValidation.textFieldNotEmpty(saveFile, "الرجاء ادخال ملف الحفظ");

        if (circularidState && receiptNumberState && destinationState && topicState && saveFileState && circularidTypeNumber && receiptNumberTypeNumber) {
            try {
                DatabaseAccess.updat(tableName, fieldName, data, "CIRCULARID = '" + circularID + "' AND ARSHEFYEAR = '" + arshefyear + "' ", imagefile);
                refreshArchefTableView();
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void delete(ActionEvent event) {
        try {
            arshefyear = AppDate.getYear(getCircularDate());
            DatabaseAccess.delete("arshefdata", "CIRCULARID = '" + circularID + "' AND ARSHEFYEAR = '" + arshefyear + "' ");
            refreshArchefTableView();
            clear(event);
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    public String getImageUrl() {
        return imageUrl.getText();
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl.setText(imageUrl);
    }

    public String getSearchText() {
        return searchText.getText();
    }

    public void setSearchText(String searchText) {
        this.searchText.setText(searchText);
    }

    public String getSearchType() {
        return searchType.getValue();
    }

    public void setSearchType(String searchType) {
        this.searchType.setValue(searchType);
    }

    public String getYear() {
        return year.getValue();
    }

    public void setYear(String year) {
        this.year.setValue(year);
    }

    public String getCircularType() {
        return circularType.getValue();
    }

    public void setCircularType(String circularType) {
        this.circularType.setValue(circularType);
    }

    public String getCircularid() {
        return circularid.getText();
    }

    public void setCircularid(String circularid) {
        this.circularid.setText(circularid);
    }

    public String getReceiptNumber() {
        return receiptNumber.getText();
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber.setText(receiptNumber);
    }

    public String getDestination() {
        return destination.getValue();
    }

    public void setDestination(String destination) {
        this.destination.setValue(destination);
    }

    public String getTopic() {
        return topic.getText();
    }

    public void setTopic(String topic) {
        this.topic.setText(topic);
    }

    public String getSaveFile() {
        return saveFile.getText();
    }

    public void setSaveFile(String saveFile) {
        this.saveFile.setText(saveFile);
    }

    public String getCircularDate() {
        return AppDate.getDate(circularDateDay, circularDateMonth, circularDateYear);
    }

    public void setCircularDate() {
        AppDate.setCurrentDate(circularDateDay, circularDateMonth, circularDateYear);
    }

    public void setCircularDate(String date) {
        AppDate.setSeparateDate(circularDateDay, circularDateMonth, circularDateYear, date);
    }

    public String getReceiptNumberDate() {
        return AppDate.getDate(receiptNumberDateDay, receiptNumberDateMonth, receiptNumberDateYear);
    }

    public void setReceiptNumberDate() {
        AppDate.setCurrentDate(receiptNumberDateDay, receiptNumberDateMonth, receiptNumberDateYear);
    }

    public void setReceiptNumberDate(String date) {
        AppDate.setSeparateDate(receiptNumberDateDay, receiptNumberDateMonth, receiptNumberDateYear, date);
    }

    public String getCircularDateYear() {
        return circularDateYear.getValue().toString();
    }

    public void setCircularDateYear(String circularDateYear) {
        this.circularDateYear.setValue(circularDateYear);
    }

    @FXML
    private void clear(ActionEvent event) {
        imageUrl.setText(null);
        imagefile = null;
        setCircularid(null);
        setReceiptNumber(null);
        setSaveFile(null);
        setTopic(null);
        setDestination(null);
        setCircularType(null);
        refreshArchefTableView();
        AppDate.setCurrentDate(circularDateDay, circularDateMonth, circularDateYear);
        AppDate.setCurrentDate(receiptNumberDateDay, receiptNumberDateMonth, receiptNumberDateYear);
    }

    private boolean chakimage() {
        boolean stat = false;
        try {
            ResultSet rs = DatabaseAccess.getData("SELECT CIRCULARIMAGE FROM arshefdata WHERE CIRCULARIMAGE IS NULL");
            stat = rs.next();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(InsertDataPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stat;
    }

    private void archefTableView(ResultSet rs) {
        try {
            int squence = 0;
            while (rs.next()) {
                squence++;
                Archeflist.add(new ArchefModel(
                        rs.getString("CIRCULARID"),
                        rs.getString("CIRCULARDATE"),
                        rs.getString("RECEIPTNUMBER"),
                        rs.getString("RECEIPTDATE"),
                        rs.getString("DESTINATION"),
                        rs.getString("TOPIC"),
                        rs.getString("SAVEFILE"),
                        rs.getString("CIRCULARTYPE"),
                        squence
                ));
            }
            rs.close();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        circularid_col.setCellValueFactory(new PropertyValueFactory<>("circularid"));
        circularDate_col.setCellValueFactory(new PropertyValueFactory<>("circularDate"));
        receiptNumber_col.setCellValueFactory(new PropertyValueFactory<>("receiptNumber"));
        receiptDate_col.setCellValueFactory(new PropertyValueFactory<>("receiptDate"));
        topic_col.setCellValueFactory(new PropertyValueFactory<>("topic"));
        destination_col.setCellValueFactory(new PropertyValueFactory<>("destination"));
        saveFile_col.setCellValueFactory(new PropertyValueFactory<>("saveFile"));
        circularType_col.setCellValueFactory(new PropertyValueFactory<>("circularType"));
        squence_col.setCellValueFactory(new PropertyValueFactory<>("squnce"));

        Callback<TableColumn<ArchefModel, String>, TableCell<ArchefModel, String>> cellFactory
                = (final TableColumn<ArchefModel, String> param) -> {
                    final TableCell<ArchefModel, String> cell = new TableCell<ArchefModel, String>() {

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
                                if (circularID == null) {
                                    FormValidation.showAlert(null, "اختر السجل من الجدول", Alert.AlertType.ERROR);
                                } else {
                                    pdfimage = DatabaseAccess.getPdfFile(circularID, circularDate);
                                    ShowPdf.writePdf(pdfimage);
                                    pdfimage = null;
                                    circularID = null;
                                    circularDate = null;
                                }
                            } catch (Exception ex) {
                                FormValidation.showAlert(null, "لا توجد صورة", Alert.AlertType.ERROR);
                            }
                        });
                        btn.setStyle("-fx-font-family: 'URW DIN Arabic';"
                                + "    -fx-font-size: 10px;"
                                + "    -fx-background-color: #E00012;"
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

        circularImage_col.setCellFactory(cellFactory);

        archefTable.setItems(Archeflist);
    }

    public void getTableRow(TableView table) {
        table.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                ObservableList<ArchefModel> list = FXCollections.observableArrayList();
                list = table.getSelectionModel().getSelectedItems();
                if (!list.isEmpty()) {
                    circularID = list.get(0).getCircularid();
                    circularDate = list.get(0).getCircularDate();
                    setCircularid(list.get(0).getCircularid());
                    setCircularDate(list.get(0).getCircularDate());
                    setReceiptNumber(list.get(0).getReceiptNumber());
                    setReceiptNumberDate(list.get(0).getReceiptDate());
                    setDestination(list.get(0).getDestination());
                    setTopic(list.get(0).getTopic());
                    setSaveFile(list.get(0).getSaveFile());
                    setCircularType(list.get(0).getCircularType());
                }
            }
        });
    }

    private void getTableRowByInterKey(TableView table) {
        table.setOnKeyPressed(new EventHandler() {
            @Override
            public void handle(Event event) {
                ObservableList<ArchefModel> list = FXCollections.observableArrayList();
                list = table.getSelectionModel().getSelectedItems();
                if (!list.isEmpty()) {
                    circularID = list.get(0).getCircularid();
                    circularDate = list.get(0).getCircularDate();
                    setCircularid(list.get(0).getCircularid());
                    setCircularDate(list.get(0).getCircularDate());
                    setReceiptNumber(list.get(0).getReceiptNumber());
                    setReceiptNumberDate(list.get(0).getReceiptDate());
                    setTopic(list.get(0).getTopic());
                    setSaveFile(list.get(0).getSaveFile());
                    setCircularType(list.get(0).getCircularType());
                }
            }
        });
    }

    private void refreshArchefTableView() {
        try {
            Archeflist.clear();
            archefTableView(DatabaseAccess.getData("SELECT * FROM arshefdata ORDER BY ID DESC"));
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
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
        imageUrl.setText(imagefile.getPath());
        return imagefile;
    }

    @FXML
    private void searchData(ActionEvent event) {
        String searchValue = getSearchType();
        switch (searchValue) {
            case "البحث برقم المعاملة":
                Archeflist.clear();
                archefTableView(getDataBycircularid());
                break;
            case "البحث برقم الوارد":
                Archeflist.clear();
                archefTableView(getDataByReceiptNumber());
                break;
            case "البحث بالموضوع":
                Archeflist.clear();
                archefTableView(getDataByTopic());
                break;
            case "البحث بجهة المعاملة":
                Archeflist.clear();
                archefTableView(getDataByDestination());
                break;
        }
    }

    public ResultSet getDataBycircularid() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.select("arshefdata", "CIRCULARID = '" + getSearchText() + "' AND ARSHEFYEAR = '" + getYear() + "' ");
        } catch (IOException ex) {
           FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public ResultSet getDataByReceiptNumber() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.select("arshefdata", "RECEIPTNUMBER = '" + getSearchText() + "' AND ARSHEFYEAR = '" + getYear() + "' ");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public ResultSet getDataByTopic() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.selectQuiry("SELECT * FROM arshefdata WHERE TOPIC LIKE '" + "%" + getSearchText() + "%" + "' AND ARSHEFYEAR = '" + getYear() + "' ");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public ResultSet getDataByDestination() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.selectQuiry("SELECT * FROM arshefdata WHERE DESTINATION LIKE '" + "%" + getSearchText() + "%" + "' AND ARSHEFYEAR = '" + getYear() + "' ");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

}
