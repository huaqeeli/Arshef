package controllers;

import Validation.FormValidation;
import com.itextpdf.text.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import modeles.CoursesModel;
import modeles.UserModel;
import trainingdata.App;

public class TrainingDataPageController implements Initializable {

    @FXML
    private TableView<CoursesModel> coursesTable;
    @FXML
    private TableColumn<?, ?> milataryid_col;
    @FXML
    private TableColumn<?, ?> rank_col;
    @FXML
    private TableColumn<?, ?> name_col;
    @FXML
    private TableColumn<?, ?> coursname_col;
    @FXML
    private TableColumn<CoursesModel, String> coursImage_col;
    @FXML
    private TextField milataryid;
    @FXML
    public ComboBox<String> coursname;

    ObservableList<CoursesModel> coursList = FXCollections.observableArrayList();
    ObservableList<String> coursComboBoxlist = FXCollections.observableArrayList();
    ObservableList<String> placeComboBoxlist = FXCollections.observableArrayList();
    ObservableList<String> estimatelist = FXCollections.observableArrayList("ممتاز", "جيد جدا", "جيد", "مقبول", "ضعيف");

    @FXML
    private TextField coursNumber;
    @FXML
    private ComboBox<String> coursplace;
    @FXML
    private TextField coursDuration;
    @FXML
    private ComboBox<?> startDateDay;
    @FXML
    private ComboBox<?> startDateMonth;
    @FXML
    private ComboBox<?> startDateYear;
    @FXML
    private ComboBox<?> endDateDay;
    @FXML
    private ComboBox<?> endDateMonth;
    @FXML
    private ComboBox<?> endDateYear;
    @FXML
    private ComboBox<String> estimate;
    @FXML
    private TextField imageUrl;
    String coursID = null;
    String miltaryID = null;
    File imagefile = null;
    Stage stage = new Stage();
    com.itextpdf.text.Image pdfimage = null;
    String userId = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        refreshcoursesTableView();
        refreshListCombobox();
        getTableRow(coursesTable);
        getTableRowByInterKey(coursesTable);
        FillComboBox.fillComboBox(estimatelist, estimate);
        AppDate.setDateValue(startDateDay, startDateMonth, startDateYear);
        AppDate.setDateValue(endDateDay, endDateMonth, endDateYear);
        AppDate.setCurrentDate(startDateDay, startDateMonth, startDateYear);
        AppDate.setCurrentDate(endDateDay, endDateMonth, endDateYear);
    }

    @FXML
    private void save(ActionEvent event) throws SQLException {
        String tableName = "coursesdata";
        String fieldName = null;
        String[] data = {getMilataryid(), getCoursid(), getCoursNumber(), getCoursplace(), getCoursDuration(), getStartDate(), getEndDate(), getEstimate()};
        String valuenumbers = null;
        if (imagefile != null) {
            fieldName = "`MILITARYID`,`COURSID`,`COURSNUMBER`,`COURSPLASE`,`COURSDURATION`,`STARTDATE`,`ENDDATE`,`COURSESTIMATE`,`COURSIMAGE`";
            valuenumbers = "?,?,?,?,?,?,?,?,?";
        } else {
            fieldName = "`MILITARYID`,`COURSID`,`COURSNUMBER`,`COURSPLASE`,`COURSDURATION`,`STARTDATE`,`ENDDATE`,`COURSESTIMATE`";
            valuenumbers = "?,?,?,?,?,?,?,?";
        }

        String poriationTableName = "opration";
        String poriationFielsName = "`USERID`,`OPRATIONTYPE`,`DESCRIPTION`,`OPRATIONDATE`";
        String opriationvaluenumbers = "?,?,?,?";
        String dexcription = "حفظ دورة " + getCoursname() + " لـ " + getMilataryid();
        String[] poraitionData = {userId, "save", dexcription, HijriCalendar.getSimpleDate()};

        boolean milataryidState = FormValidation.textFieldNotEmpty(milataryid, "الرجاء ادخال الرقم العسكري");
        boolean milataryidExisting = FormValidation.ifNotexisting("personaldata", "MILITARYID", "MILITARYID='" + getMilataryid() + "'", "لا توجد بيانات بالرقم العسكري الحالي");
        boolean coursExisting = FormValidation.ifexisting("coursesdata", "MILITARYID", "MILITARYID ='" + getMilataryid() + "' AND COURSID='" + getCoursid() + "'", "يوجد لديه دورة بنفس المسى");
        boolean milataryidNumber = FormValidation.textFieldTypeNumber(milataryid, "ادخال ارقام فقط");
        boolean coursnameState = FormValidation.comboBoxNotEmpty(coursname, "الرجاء اختيار اسم الدورة");
        boolean coursDurationState = FormValidation.textFieldNotEmpty(coursDuration, "اضغط على (احسب)لحساب مدة الدورة");
        if (coursnameState && milataryidState && milataryidNumber && milataryidExisting && coursExisting && coursDurationState) {
            try {
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, data, imagefile);
                DatabaseAccess.insert(poriationTableName, poriationFielsName, opriationvaluenumbers, poraitionData);
                UserModel.incrementSavCount(userId);
                refreshcoursesTableView();
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void edit(ActionEvent event) throws SQLException {
        String tableName = "coursesdata";
        String fieldName = null;
        String[] data = {getMilataryid(), getCoursid(), getCoursNumber(), getCoursplace(), getCoursDuration(), getStartDate(), getEndDate(), getEstimate()};
        if (imagefile != null) {
            fieldName = "`MILITARYID`=?,`COURSID`=?,`COURSNUMBER`=?,`COURSPLASE`=?,`COURSDURATION`=?,`STARTDATE`=?,`ENDDATE`=?,`COURSESTIMATE`=?,`COURSIMAGE`=?";
        } else {
            fieldName = "`MILITARYID`=?,`COURSID`=?,`COURSNUMBER`=?,`COURSPLASE`=?,`COURSDURATION`=?,`STARTDATE`=?,`ENDDATE`=?,`COURSESTIMATE`=?";
        }
        String poriationTableName = "opration";
        String poriationFielsName = "`USERID`,`OPRATIONTYPE`,`DESCRIPTION`,`"
                + ""
                + "`";
        String opriationvaluenumbers = "?,?,?,?";
        String dexcription = "تعديل دورة " + getCoursname() + " لـ " + getMilataryid();
        String[] poraitionData = {userId, "edit", dexcription, HijriCalendar.getSimpleDate()};

        boolean milataryidState = FormValidation.textFieldNotEmpty(milataryid, "الرجاء ادخال الرقم العسكري");
        boolean milataryidExisting = FormValidation.ifNotexisting("personaldata", "MILITARYID", "MILITARYID='" + getMilataryid() + "'", "لا توجد بيانات بالرقم العسكري الحالي");
        boolean milataryidNumber = FormValidation.textFieldTypeNumber(milataryid, "ادخال ارقام فقط");
        boolean coursnameState = FormValidation.comboBoxNotEmpty(coursname, "الرجاء اختيار اسم الدورة");
        if (coursnameState && milataryidState && milataryidNumber && milataryidExisting) {
            try {
                DatabaseAccess.updat(tableName, fieldName, data, "MILITARYID = '" + miltaryID + "' AND COURSID = '" + coursID + "' ", imagefile);
                DatabaseAccess.insert(poriationTableName, poriationFielsName, opriationvaluenumbers, poraitionData);
                UserModel.incrementEditCount(userId);
                refreshcoursesTableView();
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void delete(ActionEvent event) {
        try {
            String poriationTableName = "opration";
            String poriationFielsName = "`USERID`,`OPRATIONTYPE`,`DESCRIPTION`,`OPRATIONDATE`";
            String opriationvaluenumbers = "?,?,?,?";
            String dexcription = "حذف دورة " + getCoursname() + " لـ " + getMilataryid();
            String[] poraitionData = {userId, "delete", dexcription, HijriCalendar.getSimpleDate()};

            DatabaseAccess.delete("coursesdata", "MILITARYID = '" + miltaryID + "' AND COURSID = '" + coursID + "' ");
            DatabaseAccess.insert(poriationTableName, poriationFielsName, opriationvaluenumbers, poraitionData);
            UserModel.incrementDeleteCount(userId);
            refreshcoursesTableView();
            clear(event);
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    public String getMilataryid() {
        return milataryid.getText();
    }

    public void setMilataryid(String milataryid) {
        this.milataryid.setText(milataryid);
    }

    public String getCoursname() {
        return coursname.getValue();
    }

    public String getCoursid() throws SQLException {
        if (getCoursname() != null) {
            coursID = CoursesModel.getCoursId(getCoursname());
        }
        return coursID;
    }

    public void setCoursname(String coursname) {
        this.coursname.setValue(coursname);
    }

    public String getCoursNumber() {
        return coursNumber.getText();
    }

    public void setCoursNumber(String coursNumber) {
        this.coursNumber.setText(coursNumber);
    }

    public String getCoursDuration() {
        return coursDuration.getText();
    }

    public void setCoursDuration(String coursDuration) {
        this.coursDuration.setText(coursDuration);
    }

    public String getCoursplace() {
        return coursplace.getValue();
    }

    public void setCoursplace(String coursplace) {
        this.coursplace.setValue(coursplace);
    }

    public String getEstimate() {
        return estimate.getValue();
    }

    public void setEstimate(String estimate) {
        this.estimate.setValue(estimate);
    }

    public String getStartDate() {
        return AppDate.getDate(startDateDay, startDateMonth, startDateYear);
    }

    public void setStartDate(String date) {
        AppDate.setSeparateDate(startDateDay, startDateMonth, startDateYear, date);
    }

    public String getEndDate() {
        return AppDate.getDate(endDateDay, endDateMonth, endDateYear);
    }

    public void setEndDate(String date) {
        AppDate.setSeparateDate(endDateDay, endDateMonth, endDateYear, date);
    }

    @FXML
    private void clear(ActionEvent event) {
        setCoursname(null);
        setMilataryid(null);
        clearListCombobox();
        refreshListCombobox();
        setCoursNumber(null);
        setCoursplace(null);
        setCoursDuration(null);
        imageUrl.setText(null);
        imagefile = null;
        setEstimate(null);
        AppDate.setCurrentDate(startDateDay, startDateMonth, startDateYear);
        AppDate.setCurrentDate(endDateDay, endDateMonth, endDateYear);
    }

    private void coursesTableView() {
        try {
            ResultSet rs = DatabaseAccess.getData("SELECT personaldata.MILITARYID ,personaldata.RANK,personaldata.NAME,coursnames.CORSNAME,coursesdata.COURSID,coursesdata.COURSIMAGE "
                    + "FROM personaldata,coursesdata,coursnames "
                    + "WHERE personaldata.MILITARYID = coursesdata.MILITARYID AND coursesdata.COURSID = coursnames.COURSID ORDER BY MILITARYID");
            while (rs.next()) {
                coursList.add(new CoursesModel(
                        rs.getString("personaldata.MILITARYID"),
                        rs.getString("personaldata.NAME"),
                        rs.getString("personaldata.RANK"),
                        rs.getString("coursnames.CORSNAME")
                ));
                miltaryID = rs.getString("personaldata.MILITARYID");
                coursID = rs.getString("coursesdata.COURSID");
            }
            rs.close();
        } catch (SQLException | IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        milataryid_col.setCellValueFactory(new PropertyValueFactory<>("militaryId"));
        rank_col.setCellValueFactory(new PropertyValueFactory<>("rank"));
        name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        coursname_col.setCellValueFactory(new PropertyValueFactory<>("coursname"));

        boolean coursImageExisting = FormValidation.ifNotexisting("coursesdata", "COURSIMAGE", "MILITARYID = '" + miltaryID + "'AND COURSID = '" + coursID + "'");

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
                                        if (miltaryID == null || coursID == null) {
                                            FormValidation.showAlert(null, "اختر السجل من الجدول", Alert.AlertType.ERROR);
                                        } else {
                                            pdfimage = DatabaseAccess.getCoursImage(miltaryID, coursID);
                                            ShowPdf.writePdf(pdfimage);
                                            pdfimage = null;
                                            miltaryID = null;
                                            coursID = null;
                                        }
                                    } catch (Exception ex) {
                                        FormValidation.showAlert(null, "لا توجد صورة", Alert.AlertType.ERROR);
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
        coursImage_col.setCellFactory(cellFactory);

        coursesTable.setItems(coursList);
    }

    public void getTableRow(TableView table) {
        table.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                ObservableList<CoursesModel> list = FXCollections.observableArrayList();
                list = table.getSelectionModel().getSelectedItems();
                if (!list.isEmpty()) {
                    try {
                        coursID = list.get(0).getCoursId(list.get(0).getCoursname());
                        miltaryID = list.get(0).getMilitaryId();
                        ResultSet rs = DatabaseAccess.select("coursesdata", "MILITARYID = '" + miltaryID + "'AND COURSID = '" + coursID + "'");
                        setMilataryid(list.get(0).getMilitaryId());
                        setCoursname(list.get(0).getCoursname());
                        if (rs.next()) {
                            setCoursNumber(rs.getString("COURSNUMBER"));
                            setCoursplace(rs.getString("COURSPLASE"));
                            setCoursDuration(rs.getString("COURSDURATION"));
                            setEstimate(rs.getString("COURSESTIMATE"));
                            setStartDate(rs.getString("STARTDATE"));
                            setEndDate(rs.getString("ENDDATE"));
                        }
                        rs.close();
                    } catch (SQLException | IOException ex) {
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
                ObservableList<CoursesModel> list = FXCollections.observableArrayList();
                list = table.getSelectionModel().getSelectedItems();
                if (!list.isEmpty()) {
                    try {
                        coursID = list.get(0).getCoursId(list.get(0).getCoursname());
                        miltaryID = list.get(0).getMilitaryId();
                        ResultSet rs = DatabaseAccess.select("coursesdata", "MILITARYID = '" + miltaryID + "'AND COURSID = '" + coursID + "'");
                        setMilataryid(list.get(0).getMilitaryId());
                        setCoursname(list.get(0).getCoursname());
                        if (rs.next()) {
                            setCoursNumber(rs.getString("COURSNUMBER"));
                            setCoursplace(rs.getString("COURSPLASE"));
                            setCoursDuration(rs.getString("COURSDURATION"));
                            setEstimate(rs.getString("COURSESTIMATE"));
                            setStartDate(rs.getString("STARTDATE"));
                            setEndDate(rs.getString("ENDDATE"));
                            ArrayList images = new ArrayList();
                            images.add(rs.getBytes("COURSIMAGE"));
                            byte[] scaledInstance = (byte[]) images.get(0);
                            pdfimage = com.itextpdf.text.Image.getInstance(scaledInstance);
                        }
                    } catch (SQLException | IOException | BadElementException ex) {
                        FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
                    }
                }
            }
        });
    }

    private void refreshcoursesTableView() {
        coursList.clear();
        coursesTableView();
    }

    private ObservableList filleCoursNames(ObservableList list) {
        try {
            ResultSet rs = DatabaseAccess.select("coursnames");
            try {
                while (rs.next()) {
                    list.add(rs.getString("CORSNAME"));
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

    public void refreshListCombobox() {
        coursname.setItems(filleCoursNames(coursComboBoxlist));
        coursplace.setItems(filleCoursPlace(placeComboBoxlist));
    }

    public void clearListCombobox() {
        coursname.getItems().clear();
        coursplace.getItems().clear();
    }

    @FXML
    private void addNewCoursName(ActionEvent event) {
        App.showFxml("/view/AddNewCoursName");
    }

    @FXML
    private void selectedCours(ActionEvent event) {
        try {
            getCoursid();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private File getImageUrle(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter ext1 = new FileChooser.ExtensionFilter("JPG files(*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter ext2 = new FileChooser.ExtensionFilter("PNG files(*.png)", "*.PNG");
        FileChooser.ExtensionFilter ext3 = new FileChooser.ExtensionFilter("JPEG‬‬  files(*.jpeg)", "*.JPEG‬‬ ");
        fileChooser.getExtensionFilters().addAll(ext1, ext2, ext3);
        imagefile = fileChooser.showOpenDialog(stage);
        imageUrl.setText(imagefile.getPath());
        return imagefile;
    }

    @FXML
    private void addNewCoursPlace(ActionEvent event) {
        App.showFxml("/view/AddNewCoursPlace");
    }

    public void setUserId(String userid) {
        this.userId = userid;
    }

    @FXML
    private void calculateDuration(ActionEvent event) {
       
        setCoursDuration(AppDate.getDifferenceDate(
                Integer.parseInt(startDateDay.getValue().toString()),
                Integer.parseInt(startDateMonth.getValue().toString()),
                Integer.parseInt(startDateYear.getValue().toString()),
                Integer.parseInt(endDateDay.getValue().toString()),
                Integer.parseInt(endDateMonth.getValue().toString()),
                Integer.parseInt(endDateYear.getValue().toString())
        ));
    }

}
