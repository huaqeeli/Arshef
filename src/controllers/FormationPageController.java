package controllers;

import Serveces.FormationPageListener;
import Validation.FormValidation;
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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import modeles.FormationModel;

public class FormationPageController implements Initializable {

    @FXML
    private ComboBox<String> searchType;
    @FXML
    private TextField searchText;
    @FXML
    private TextField militaryID;
    @FXML
    private TextField personalID;
    @FXML
    private TextField name;
    @FXML
    private ComboBox<String> rank;
    @FXML
    private ComboBox<String> uint;
    @FXML
    private TextField note;
    @FXML
    private VBox vbox;
    ObservableList<FormationModel> FormationObject = FXCollections.observableArrayList();
    private FormationPageListener mylistener;
    ObservableList<String> uintlist = FXCollections.observableArrayList();
    ObservableList<String> rankComboBoxlist = FXCollections.observableArrayList("الفريق اول", "القريق", "الواء", "العميد", "العقيد", "المقدم", "الرائد", "النقيب", "الملازم أول", "الملازم", "رئيس رقباء", "رقيب أول", "رقيب", "وكيل رقيب", "عريف", "جندي أول", "جندي");
    ObservableList<String> searchTypelist = FXCollections.observableArrayList("البحث بالرقم العسكري", "البحث برقم السجل المدني", "البحث بالاسم", "عرض الكل", "عرض الملاحظات", "عرض اسماء المنقولين", "البحث بالرقم العسكري للمنقولين");
    ObservableList<String> typelist = FXCollections.observableArrayList("", "ضابط", "فرد");
    @FXML
    private ComboBox<String> searchUint;
    @FXML
    private TextField specializ;
    @FXML
    private Label OFCount;
    @FXML
    private Label SRCount;
    @FXML
    private Label Totel;

    String tableName = "personaldata";
    @FXML
    private ComboBox<String> type;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        uint.setItems(filleUint(uintlist));
        searchUint.setItems(filleUint(uintlist));
        rank.setItems(rankComboBoxlist);
        searchType.setItems(searchTypelist);
        type.setItems(typelist);
        OFCount.setText(getOfCount());
        SRCount.setText(getSrCount());
        Totel.setText(getAllCount());

    }

    private ObservableList filleUint(ObservableList list) {
        try {
            ResultSet rs = DatabaseAccess.select("placenames", "UINTTYPE='داخلي'");
            list.clear();
            list.add("");
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

    private String getType() {
        String value = null;
        switch (type.getValue()) {
            case "ضابط":
                value = "OF";
                break;
            case "فرد":
                value = "SR";
                break;
        }
        return value;
    }

    @FXML
    private void save(ActionEvent event) {
        String tableName = "personaldata";
        String fieldName = "`MILITARYID`,`PERSONALID`,`NAME`,`RANK`,`UNIT`,`NOTE`,`SPECIALTY`,`MEMBERTYPE`";
        String[] data = {militaryID.getText(), personalID.getText(), name.getText(), rank.getValue(), uint.getValue(), note.getText(), specializ.getText(), getType()};
        String valuenumbers = "?,?,?,?,?,?,?,?";

        boolean rankState = FormValidation.comboBoxNotEmpty(rank, "الرجاء اختيار الرتبة");
        boolean uintState = FormValidation.comboBoxNotEmpty(uint, "الرجاء اختيار الوحدة");
        boolean typeState = FormValidation.comboBoxNotEmpty(type, "الرجاء اختيار ضابط او فرد");
        boolean nameState = FormValidation.textFieldNotEmpty(name, "الرجاء ادخال الاسم");
        boolean personalIDState = FormValidation.textFieldNotEmpty(personalID, "الرجاء ادخال رقم السجل المدني");
        boolean personalIDNumber = FormValidation.textFieldTypeNumber(personalID, "الرجاء ادخال رقم السجل المدني");
        boolean militaryIDState = FormValidation.textFieldNotEmpty(militaryID, "الرجاء ادخال الرقم العسكري");
        boolean militaryIDNumber = FormValidation.textFieldTypeNumber(militaryID, "الرجاء ادخال الرقم العسكري");
        boolean militaryIDExisting = FormValidation.ifexisting("personaldata", "MILITARYID", "MILITARYID = '" + militaryID.getText() + "'", "الرقم العسكري موجود مسبقا");
        boolean personalIDExisting = FormValidation.ifexisting("personaldata", "PERSONALID", "PERSONALID = '" + personalID.getText() + "'", "رقم السجل موجود مسبقا");
        if (militaryIDExisting && personalIDExisting && rankState && uintState && nameState && personalIDState && personalIDNumber && militaryIDState && militaryIDNumber && typeState) {
            try {
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, data);
                refreshData(uint.getValue());
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void edit(ActionEvent event) {
        //String tableName = "personaldata";
        String fieldName = "`MILITARYID`=?,`PERSONALID`=?,`NAME`=?,`RANK`=?,`UNIT`=?,`NOTE`=?,`SPECIALTY`=?";
        String[] data = {militaryID.getText(), personalID.getText(), name.getText(), rank.getValue(), uint.getValue(), note.getText(), specializ.getText()};

        boolean rankState = FormValidation.comboBoxNotEmpty(rank, "الرجاء اختيار الرتبة");
        boolean uintState = FormValidation.comboBoxNotEmpty(uint, "الرجاء اختيار الوحدة");
        boolean nameState = FormValidation.textFieldNotEmpty(name, "الرجاء ادخال الاسم");
        boolean personalIDState = FormValidation.textFieldNotEmpty(personalID, "الرجاء ادخال رقم السجل المدني");
        boolean personalIDNumber = FormValidation.textFieldTypeNumber(personalID, "الرجاء ادخال رقم السجل المدني");
        boolean militaryIDState = FormValidation.textFieldNotEmpty(militaryID, "الرجاء ادخال الرقم العسكري");
        boolean militaryIDNumber = FormValidation.textFieldTypeNumber(militaryID, "الرجاء ادخال الرقم العسكري");

        if (rankState && uintState && nameState && personalIDState && personalIDNumber && militaryIDState && militaryIDNumber) {
            try {
                int t = 0;
                switch (tableName) {
                    case "personaldata":
                        t = DatabaseAccess.updat("personaldata", fieldName, data, "MILITARYID = '" + militaryID.getText() + "'");
                        if (t > 0) {
                            FormValidation.showAlert("", "تم تحديث البيانات", Alert.AlertType.CONFIRMATION);
                        }
                        refreshData(uint.getValue());
                        clear(event);
                        break;
                    case "livingdata":
                        t = DatabaseAccess.updat("livingdata", fieldName, data, "MILITARYID = '" + militaryID.getText() + "'");
                        if (t > 0) {
                            FormValidation.showAlert("", "تم تحديث البيانات", Alert.AlertType.CONFIRMATION);
                        }
                        refreshlivingeData();
                        clear(event);
                        break;
                }
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void delete(ActionEvent event) {

        String fieldName = "`MILITARYID`,`PERSONALID`,`NAME`,`RANK`,`UNIT`,`NOTE`,`SPECIALTY`";
        String[] data = {militaryID.getText(), personalID.getText(), name.getText(), rank.getValue(), uint.getValue(), note.getText(), specializ.getText()};
        String valuenumbers = "?,?,?,?,?,?,?";
        try {
            int t = 0;
            switch (tableName) {
                case "personaldata":
                    t = DatabaseAccess.insert("livingdata", fieldName, valuenumbers, data);
                    if (t > 0) {
                        DatabaseAccess.delete("personaldata", "MILITARYID = '" + militaryID.getText() + "'");
                    } else {
                        FormValidation.showAlert(null, "حدثت مشكلة اثناء الحذف", Alert.AlertType.ERROR);
                    }
                    refreshData(uint.getValue());
                    clear(event);
                    break;
                case "livingdata":
                    DatabaseAccess.delete("livingdata", "MILITARYID = '" + militaryID.getText() + "'");
                    refreshlivingeData();
                    clear(event);
                    break;
            }

        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clear(ActionEvent event) {
        militaryID.setText(null);
        personalID.setText(null);
        name.setText(null);
        rank.setValue(null);
        uint.setValue(null);
        note.setText(null);
        searchText.setText(null);
        searchType.setValue(null);
        searchUint.setValue(null);
        OFCount.setText(null);
        SRCount.setText(null);
        Totel.setText(null);
        refreshData(null);
        tableName = "personaldata";
    }

    private void refreshData(String uint) {
        try {
            FormationObject.clear();
            vbox.getChildren().clear();
            viewdata(DatabaseAccess.getData("SELECT * FROM personaldata WHERE UNIT ='" + uint + "' ORDER BY MILITARYID ASC"));
            OFCount.setText(getOfCount());
            SRCount.setText(getSrCount());
            Totel.setText(getAllCount());
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    private void refreshlivingeData() {
        try {
            FormationObject.clear();
            vbox.getChildren().clear();
            viewdata(DatabaseAccess.getData("SELECT * FROM livingdata "));
            OFCount.setText(getOfCount());
            SRCount.setText(getSrCount());
            Totel.setText(getAllCount());
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    private void viewdata(ResultSet data) {
        FormationObject.addAll(getData(data));
        if (FormationObject.size() > 0) {
//            setChosendata(FormationObject.get(0));
            mylistener = new FormationPageListener() {
                @Override
                public void onClickListener(FormationModel formationModel) {
                    setChosendata(formationModel);
                }
            };
        }
        try {
            for (FormationModel formationModel : FormationObject) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/view/FormationItem.fxml"));
                AnchorPane pane = fxmlLoader.load();
                FormationItemController formationItemController = fxmlLoader.getController();
                formationItemController.setData(formationModel, mylistener);
                vbox.getChildren().add(pane);
            }
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    private ObservableList<FormationModel> getData(ResultSet rs) {
        ObservableList<FormationModel> formationModels = FXCollections.observableArrayList();
        FormationModel formationModel;
        try {
            int squnce = 0;
            while (rs.next()) {
                squnce++;
                formationModel = new FormationModel();
                formationModel.setSqunce(squnce);
                formationModel.setMilitaryID(rs.getString("MILITARYID"));
                formationModel.setPersonalID(rs.getString("PERSONALID"));
                formationModel.setName(rs.getString("NAME"));
                formationModel.setRank(rs.getString("RANK"));
                formationModel.setUint(rs.getString("UNIT"));
                formationModel.setNote(rs.getString("NOTE"));
                formationModel.setMarkState(rs.getInt("MARK"));
                formationModel.setMarkColor(rs.getString("MARKCOLOR"));
                formationModel.setSpecializ(rs.getString("SPECIALTY"));
                formationModels.add(formationModel);
            }
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return formationModels;
    }

    private void setChosendata(FormationModel formationModel) {
        militaryID.setText(formationModel.getMilitaryID());
        personalID.setText(formationModel.getPersonalID());
        name.setText(formationModel.getName());
        rank.setValue(formationModel.getRank());
        uint.setValue(formationModel.getUint());
        note.setText(formationModel.getNote());
        specializ.setText(formationModel.getSpecializ());
    }

    @FXML
    private void getDataBYUint(ActionEvent event) {
        refreshData(searchUint.getValue());
        OFCount.setText(getOfCount(searchUint.getValue()));
        SRCount.setText(getSrCount(searchUint.getValue()));
        Totel.setText(getAllCount(searchUint.getValue()));
    }

    @FXML
    private void searchData(ActionEvent event) {
        String searchValue = searchType.getValue();
        switch (searchValue) {
            case "عرض الكل":
                FormationObject.clear();
                vbox.getChildren().clear();
                viewdata(getAllData());
                tableName = "personaldata";
                break;
            case "عرض اسماء المنقولين":
                FormationObject.clear();
                vbox.getChildren().clear();
                viewdata(getLivingData());
                tableName = "livingdata";
                break;
            case "البحث بالاسم":
                FormationObject.clear();
                vbox.getChildren().clear();
                viewdata(getDataByName());
                tableName = "personaldata";
                break;
            case "البحث برقم السجل المدني":
                FormationObject.clear();
                vbox.getChildren().clear();
                viewdata(getDataByPersonalID());
                tableName = "personaldata";
                break;
            case "عرض الملاحظات":
                FormationObject.clear();
                vbox.getChildren().clear();
                viewdata(getAllNotes());
                tableName = "personaldata";
                break;
            case "البحث بالرقم العسكري":
                FormationObject.clear();
                vbox.getChildren().clear();
                viewdata(getDataMitaryID());
                tableName = "personaldata";
                break;
            case "البحث بالرقم العسكري للمنقولين":
                FormationObject.clear();
                vbox.getChildren().clear();
                viewdata(getLivingDataMitaryID());
                tableName = "livingdata";
                break;
        }
    }

    private ResultSet getAllData() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.select("personaldata");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    private ResultSet getLivingData() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.select("livingdata");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    private ResultSet getDataByName() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.select("personaldata", "NAME LIKE '" + "%" + searchText.getText() + "%" + "'");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    private ResultSet getDataByPersonalID() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.select("personaldata", "PERSONALID = '" + searchText.getText() + "'");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    private ResultSet getAllNotes() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.select("personaldata", "MARK = 1 ORDER BY MARKCOLOR");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    private ResultSet getDataMitaryID() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.select("personaldata", "MILITARYID = '" + searchText.getText() + "'");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    private ResultSet getLivingDataMitaryID() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.select("livingdata", "MILITARYID = '" + searchText.getText() + "'");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    private String getOfCount(String unit) {
        String count = null;
        try {
            ResultSet rs = DatabaseAccess.getData("SELECT count(MILITARYID) AS COUNTRESOLT FROM arshefdb.personaldata WHERE MEMBERTYPE = 'OF' AND UNIT = '" + unit + "'");
            if (rs.next()) {
                count = rs.getString("COUNTRESOLT");
            }
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return count;
    }

    private String getSrCount(String unit) {
        String count = null;
        try {
            ResultSet rs = DatabaseAccess.getData("SELECT count(MILITARYID) AS COUNTRESOLT FROM arshefdb.personaldata WHERE MEMBERTYPE = 'SR' AND UNIT = '" + unit + "'");
            if (rs.next()) {
                count = rs.getString("COUNTRESOLT");
            }
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return count;
    }

    private String getAllCount(String unit) {
        String count = null;
        try {
            ResultSet rs = DatabaseAccess.getData("SELECT count(MILITARYID) AS COUNTRESOLT FROM arshefdb.personaldata WHERE UNIT = '" + unit + "'");
            if (rs.next()) {
                count = rs.getString("COUNTRESOLT");
            }
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return count;
    }

    private String getOfCount() {
        String count = null;
        try {
            ResultSet rs = DatabaseAccess.getData("SELECT count(MILITARYID) AS COUNTRESOLT FROM arshefdb.personaldata WHERE MEMBERTYPE = 'OF'");
            if (rs.next()) {
                count = rs.getString("COUNTRESOLT");
            }
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return count;
    }

    private String getSrCount() {
        String count = null;
        try {
            ResultSet rs = DatabaseAccess.getData("SELECT count(MILITARYID) AS COUNTRESOLT FROM arshefdb.personaldata WHERE MEMBERTYPE = 'SR'");
            if (rs.next()) {
                count = rs.getString("COUNTRESOLT");
            }
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return count;
    }

    private String getAllCount() {
        String count = null;
        try {
            ResultSet rs = DatabaseAccess.getData("SELECT count(MILITARYID) AS COUNTRESOLT FROM arshefdb.personaldata ");
            if (rs.next()) {
                count = rs.getString("COUNTRESOLT");
            }
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return count;
    }

    @FXML
    private void getExclForUint(ActionEvent event) {
        if (uint.getValue() == null) {
            FormValidation.showAlert(null, "الرجاء اختار الوحدة", Alert.AlertType.ERROR);
        } else {
            try {
                FileChooser fileChooser = new FileChooser();
                Window stage = null;
                fileChooser.setInitialFileName("تشكيل " + " " + uint.getValue());
                File file = fileChooser.showSaveDialog(stage);
                String savefile = null;
                if (file != null) {
                    savefile = file.toString();
                }
                ResultSet rs = DatabaseAccess.getData("SELECT * FROM personaldata WHERE UNIT ='" + uint.getValue() + "' ORDER BY MILITARYID ASC");
                String[] feild = {"MILITARYID", "PERSONALID", "NAME", "RANK", "UNIT", "SPECIALTY"};
                String[] titel = {"الرقم العسكري", "رقم الهوية", "الاسم", "الرتبة", "الوحدة", "التخصص"};
                String[] sheetTitel = {"تشكيل " + " " + uint.getValue()};
                ExporteExcelSheet exporter = new ExporteExcelSheet();
                ArrayList<Object[]> dataList = exporter.getTableData(rs, feild);
                if (dataList != null && dataList.size() > 0) {
                    exporter.ceratHeader(sheetTitel, 0, exporter.setTitelStyle());
                    exporter.ceratHeader(titel, 1, exporter.setHederStyle());
                    exporter.ceratContent(dataList, feild, 2, exporter.setContentStyle());
                    exporter.writeFile(savefile);
                } else {
                    FormValidation.showAlert(null, "There is no data available in the table to export", Alert.AlertType.ERROR);
                }
                rs.close();

            } catch (IOException | SQLException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void getExcleForFores(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            Window stage = null;
            fileChooser.setInitialFileName("تشكيل قوة السيف الجرب ");
            File file = fileChooser.showSaveDialog(stage);
            String savefile = null;
            if (file != null) {
                savefile = file.toString();
            }
            ResultSet rs = DatabaseAccess.getData("SELECT * FROM personaldata ORDER BY MILITARYID ASC");
            String[] feild = {"MILITARYID",  "RANK","NAME", "PERSONALID", "UNIT", "SPECIALTY"};
            String[] titel = {"الرقم العسكري",  "الرتبة", "الاسم","رقم الهوية", "الوحدة", "التخصص"};
            String[] sheetTitel = {"تشكيل قوة السيف الجرب "};
            ExporteExcelSheet exporter = new ExporteExcelSheet();
            ArrayList<Object[]> dataList = exporter.getTableData(rs, feild);
            if (dataList != null && dataList.size() > 0) {
                exporter.ceratHeader(sheetTitel, 0, exporter.setTitelStyle());
                exporter.ceratHeader(titel, 1, exporter.setHederStyle());
                exporter.ceratContent(dataList, feild, 2, exporter.setContentStyle());
                exporter.writeFile(savefile);
            } else {
                FormValidation.showAlert(null, "There is no data available in the table to export", Alert.AlertType.ERROR);
            }
            rs.close();
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void updateAllFromExcle(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/view/UpdateAllFromExcle.fxml"));
        AnchorPane pane = fxmlLoader.load();
        vbox.getChildren().add(pane);
    }

    @FXML
    private void getExcleForLivingName(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            Window stage = null;
            fileChooser.setInitialFileName("بيان اسماء المنقولين خارج القوة ");
            File file = fileChooser.showSaveDialog(stage);
            String savefile = null;
            if (file != null) {
                savefile = file.toString();
            }
            ResultSet rs = DatabaseAccess.getData("SELECT * FROM livingdata ");
            String[] feild = {"MILITARYID", "PERSONALID", "NAME", "RANK", "UNIT", "SPECIALTY"};
            String[] titel = {"الرقم العسكري", "رقم الهوية", "الاسم", "الرتبة", "الوحدة", "التخصص"};
            String[] sheetTitel = {"بيان اسماء المنقولين خارج القوة "};
            ExporteExcelSheet exporter = new ExporteExcelSheet();
            ArrayList<Object[]> dataList = exporter.getTableData(rs, feild);
            if (dataList != null && dataList.size() > 0) {
                exporter.ceratHeader(sheetTitel, 0, exporter.setTitelStyle());
                exporter.ceratHeader(titel, 1, exporter.setHederStyle());
                exporter.ceratContent(dataList, feild, 2, exporter.setContentStyle());
                exporter.writeFile(savefile);
            } else {
                FormValidation.showAlert(null, "There is no data available in the table to export", Alert.AlertType.ERROR);
            }
            rs.close();
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void getAllNotesExcle(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            Window stage = null;
            fileChooser.setInitialFileName("بيان جميع الملاحظات");
            File file = fileChooser.showSaveDialog(stage);
            String savefile = null;
            if (file != null) {
                savefile = file.toString();
            }
            ResultSet rs = DatabaseAccess.select("personaldata", "MARK = 1 ORDER BY MARKCOLOR");
            String[] feild = {"MILITARYID", "PERSONALID", "NAME", "RANK", "UNIT", "SPECIALTY", "NOTE"};
            String[] titel = {"الرقم العسكري", "رقم الهوية", "الاسم", "الرتبة", "الوحدة", "التخصص", "ملاحظات"};
            String[] sheetTitel = {"بيان جميع الملاحظات"};
            ExporteExcelSheet exporter = new ExporteExcelSheet();
            ArrayList<Object[]> dataList = exporter.getTableData(rs, feild);
            if (dataList != null && dataList.size() > 0) {
                exporter.ceratHeader(sheetTitel, 0, exporter.setTitelStyle());
                exporter.ceratHeader(titel, 1, exporter.setHederStyle());
                exporter.ceratContent(dataList, feild, 2, exporter.setContentStyle());
                exporter.writeFile(savefile);
            } else {
                FormValidation.showAlert(null, "There is no data available in the table to export", Alert.AlertType.ERROR);
            }
            rs.close();
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

}
