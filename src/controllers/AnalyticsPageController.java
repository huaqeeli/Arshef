package controllers;

import Serveces.AnalyticesListener;
import Validation.FormValidation;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import modeles.AnalyticsModel;

public class AnalyticsPageController implements Initializable {

    @FXML
    private VBox namesView;

    public final List<AnalyticsModel> analyticsObject = new ArrayList<>();

    String analyticsid = null;
    private AnalyticesListener mylistener;
    private AnalyticsItemController analyticsItemController;
    ObservableList<String> destinationlist = FXCollections.observableArrayList();
    ObservableList<String> searchTypelist = FXCollections.observableArrayList("اسماء المحليلين", "اسماء غير المحليلين", "البحث بالرقم العسكري", "البحث بالاسم", "البحث بالعام");
    ObservableList<String> stagelist = FXCollections.observableArrayList("الربع الاول", "الربع الثاني", "الربع الثالث", "الربع الاخير");
    @FXML
    private ComboBox<String> searchType;
    @FXML
    private ComboBox<?> year;
    @FXML
    private TextField searchText;
    @FXML
    private TextField militaryId;
    @FXML
    private ComboBox<?> analyticsDateDay;
    @FXML
    private ComboBox<?> analyticsDateMonth;
    @FXML
    private ComboBox<?> analyticsDateYear;
    @FXML
    private ComboBox<String> stage;
    @FXML
    private ComboBox<String> interDatastage;
    @FXML
    private ComboBox<String> uinte;
    @FXML
    private CheckBox selecteUint;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        setEndDate(HijriCalendar.getSimpleDate());
        setAnalyticDate(AppDate.getGregorianCurrentDate());
        AppDate.setGregorianDateValue(analyticsDateDay, analyticsDateMonth, analyticsDateYear);
        AppDate.setGregorianCurrentDate(analyticsDateDay, analyticsDateMonth, analyticsDateYear);
        FillComboBox.fillComboBox(searchTypelist, searchType);
        FillComboBox.fillComboBox(stagelist, stage);
        FillComboBox.fillComboBox(stagelist, interDatastage);
        uinte.setItems(filleDestination(destinationlist));
        AppDate.setGregorianYearValue(year);
        AppDate.setCurrentGregorianYear(year);
        refreshdata();
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

    @FXML
    private void save(ActionEvent event) {
        String tableName = "analytics";
        String[] data = {militaryId.getText(), getAnalyticDate(), interDatastage.getValue(), getYear(getAnalyticDate())};
        String fieldName = "`MILITARYID`,`ANALYTICSDATE`,`STAGE`,`ANALYTICSYEAR`";
        String valuenumbers = "?,?,?,?";

        boolean missionnameState = FormValidation.textFieldNotEmpty(militaryId, "الرجاء ادخال الرقم العسكري");

        if (missionnameState) {
            try {
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, data);
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void edit(ActionEvent event) {
        String tableName = "analytics";
        String[] data = {militaryId.getText(), getAnalyticDate(), interDatastage.getValue(), getYear(getAnalyticDate())};
        String fieldName = "`MILITARYID`=?,`ANALYTICSDATE`=?,`STAGE`=?,`ANALYTICSYEAR`=?";

        boolean missionnameState = FormValidation.textFieldNotEmpty(militaryId, "الرجاء ادخال الرقم العسكري");

        if (missionnameState) {
            try {
                int t = DatabaseAccess.updat(tableName, fieldName, data, "ID = '" + analyticsid + "'");
                if (t > 0) {
                    FormValidation.showAlert("", "تم تحديث البيانات", Alert.AlertType.CONFIRMATION);
                }
                clear(event);
            } catch (IOException | SQLException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void clear(ActionEvent event) {
        refreshdata();
        militaryId.setText(null);
        stage.setValue(null);
        interDatastage.setValue(null);
        uinte.setValue(null);
        searchText.setText(null);
        searchType.setValue(null);
    }

    private void refreshdata() {
        try {
            analyticsObject.clear();
            namesView.getChildren().clear();
            viewdata(DatabaseAccess.getData("SELECT analytics.ID,analytics.MILITARYID,analytics.STAGE,analytics.ANALYTICSDATE,personaldata.NAME,personaldata.MILITARYID,personaldata.RANK,personaldata.UNIT FROM analytics,personaldata "
                    + "WHERE analytics.MILITARYID = personaldata.MILITARYID "));
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    private List<AnalyticsModel> getData(ResultSet rs) {
        List<AnalyticsModel> datalist = new ArrayList<>();
        AnalyticsModel analyticsModel;
        try {
            int squnce = 1;
            while (rs.next()) {
                analyticsModel = new AnalyticsModel();
                analyticsModel.setSqunce(squnce++);
                analyticsModel.setId(rs.getString("ID"));
                analyticsModel.setMilitaryid(rs.getString("MILITARYID"));
                analyticsModel.setRank(rs.getString("RANK"));
                analyticsModel.setName(rs.getString("NAME"));
                analyticsModel.setUint(rs.getString("UNIT"));
                analyticsModel.setStage(rs.getString("STAGE"));
                analyticsModel.setAnalyticesDate(rs.getString("ANALYTICSDATE"));
                datalist.add(analyticsModel);
            }
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return datalist;
    }

    private List<AnalyticsModel> getUnanalyticsData(ResultSet rs) {
        List<AnalyticsModel> datalist = new ArrayList<>();
        AnalyticsModel analyticsModel;
        try {
            int squnce = 1;
            while (rs.next()) {
                analyticsModel = new AnalyticsModel();
                analyticsModel.setSqunce(squnce++);
                analyticsModel.setMilitaryid(rs.getString("MILITARYID"));
                analyticsModel.setRank(rs.getString("RANK"));
                analyticsModel.setName(rs.getString("NAME"));
                analyticsModel.setUint(rs.getString("UNIT"));
                datalist.add(analyticsModel);
            }
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return datalist;
    }

    private void setChosendata(AnalyticsModel analyticsModel) {
        interDatastage.setValue(analyticsModel.getStage());
        militaryId.setText(analyticsModel.getMilitaryid());
        setAnalyticDate(analyticsModel.getAnalyticesDate());
        analyticsid = analyticsModel.getId();
    }

    private void viewdata(ResultSet rs) {
        analyticsObject.addAll(getData(rs));
        if (analyticsObject.size() > 0) {
            // setChosendata(missionObject.get(0));
            mylistener = new AnalyticesListener() {
                @Override
                public void onClickListener(AnalyticsModel analyticsModel) {
                    setChosendata(analyticsModel);
                }
            };
        }

        try {
            for (AnalyticsModel analyticsModel : analyticsObject) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/view/AnalyticsItem.fxml"));
                AnchorPane pane = fxmlLoader.load();
                analyticsItemController = fxmlLoader.getController();
                analyticsItemController.setData(analyticsModel, mylistener);
                namesView.getChildren().add(pane);
            }
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    private void viewUnanalyticsdata(ResultSet rs) {
        analyticsObject.addAll(getUnanalyticsData(rs));
        if (analyticsObject.size() > 0) {
            // setChosendata(missionObject.get(0));
            mylistener = new AnalyticesListener() {
                @Override
                public void onClickListener(AnalyticsModel analyticsModel) {
                    setChosendata(analyticsModel);
                }
            };
        }

        try {
            for (AnalyticsModel analyticsModel : analyticsObject) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/view/AnalyticsItem.fxml"));
                AnchorPane pane = fxmlLoader.load();
                analyticsItemController = fxmlLoader.getController();
                analyticsItemController.setData(analyticsModel, mylistener);
                namesView.getChildren().add(pane);
            }
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    public String getAnalyticDate() {
        return AppDate.getDate(analyticsDateDay, analyticsDateMonth, analyticsDateYear);
    }

    public void setAnalyticDate(String date) {
        AppDate.setSeparateDate(analyticsDateDay, analyticsDateMonth, analyticsDateYear, date);
    }

    public String getYear(String date) {
        return AppDate.getYear(date);
    }

    @FXML
    private void search(ActionEvent event) {
        boolean stageState = FormValidation.comboBoxNotEmpty(stage, "الرجاء اختر المرحلة");
        boolean uinteState = FormValidation.comboBoxNotEmpty(uinte, "الرجاء اختر الوحدة");
        String searchValue = searchType.getValue();
        switch (searchValue) {
            case "اسماء المحليلين":
                if (disableUint() == true && stageState && uinteState) {
                    analyticsObject.clear();
                    namesView.getChildren().clear();
                    viewdata(getAnalyticsNameByUint());
                    break;
                } else if (stageState) {
                    analyticsObject.clear();
                    namesView.getChildren().clear();
                    viewdata(getAllAnalyticsName());
                    break;
                }
            case "اسماء غير المحليلين":
                if (disableUint() == true && stageState && uinteState) {
                    analyticsObject.clear();
                    namesView.getChildren().clear();
                    viewUnanalyticsdata(getUnAnalyticsNameByUint());
                    break;
                } else if (stageState) {
                    analyticsObject.clear();
                    namesView.getChildren().clear();
                    viewUnanalyticsdata(getAllUnAnalyticsName());
                    break;
                }
            case "البحث بالرقم العسكري":
                analyticsObject.clear();
                namesView.getChildren().clear();
                viewUnanalyticsdata(getAnalyticsNameByMilitaryid());
                break;
            case "البحث بالعام":
                analyticsObject.clear();
                namesView.getChildren().clear();
                viewUnanalyticsdata(getAnalyticsNameByYear());
                break;
            case "البحث بالاسم":
                analyticsObject.clear();
                namesView.getChildren().clear();
                viewUnanalyticsdata(getAnalyticsNameByName());
                break;
        }
    }

    @FXML
    private void excle(ActionEvent event) {
        ResultSet rs = null;
        boolean stageState = FormValidation.comboBoxNotEmpty(stage, "الرجاء اختر المرحلة");
        boolean uinteState = FormValidation.comboBoxNotEmpty(uinte, "الرجاء اختر الوحدة");
        String searchValue = searchType.getValue();
        String[] feild = {"MILITARYID", "RANK", "NAME", "UNIT", "STAGE", "ANALYTICSDATE"};
        String[] titel = {"الرقم العسكري", "الرتبة", "الاسم", "الوحدة", "المرحلة", "تاريخ التحليل"};
        String[] feild2 = {"MILITARYID", "RANK", "NAME", "UNIT"};
        String[] titel2 = {"الرقم العسكري", "الرتبة", "الاسم", "الوحدة"};
        String titleText = null;
        switch (searchValue) {
            case "اسماء المحليلين":
                if (disableUint() == true && stageState && uinteState) {
                    rs = getAnalyticsNameByUint();
                    titleText = searchType.getValue() + " " + "في" + " " + uinte.getValue();
                    getExcle(rs, feild, titel, titleText);
                    break;
                } else if (stageState) {
                    rs = getAllAnalyticsName();
                    titleText = searchType.getValue();
                    getExcle(rs, feild, titel, titleText);
                    break;
                }
            case "اسماء غير المحليلين":
                if (disableUint() == true && stageState && uinteState) {
                    rs = getUnAnalyticsNameByUint();
                    titleText = searchType.getValue() + " " + "في" + " " + uinte.getValue();
                    getExcle(rs, feild2, titel2, titleText);
                    break;
                } else if (stageState) {
                    rs = getAllUnAnalyticsName();
                    titleText = searchType.getValue();
                    getExcle(rs, feild2, titel2, titleText);
                    break;
                }
            case "البحث بالرقم العسكري":
                rs = getAnalyticsNameByMilitaryid();
                titleText = "بيان التحاليل";
                getExcle(rs, feild, titel, titleText);
                break;
            case "البحث بالعام":
                rs = getAnalyticsNameByYear();
                titleText = "بيان التحاليل";
                getExcle(rs, feild, titel, titleText);
                break;
            case "البحث بالاسم":
                rs = getAnalyticsNameByName();
                titleText = "بيان التحاليل";
                getExcle(rs, feild, titel, titleText);
                break;
        }

    }

    private void getExcle(ResultSet rs, String[] feild, String[] titel, String filetitel) {
        try {
            FileChooser fileChooser = new FileChooser();
            Window stage = null;
            fileChooser.setInitialFileName(filetitel);
            File file = fileChooser.showSaveDialog(stage);
            String savefile = null;
            if (file != null) {
                savefile = file.toString();
            }
            String[] sheetTitel = {filetitel};
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

    private ResultSet getAnalyticsNameByUint() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.getData("SELECT analytics.ID,analytics.MILITARYID,analytics.STAGE,analytics.ANALYTICSDATE,personaldata.NAME,personaldata.MILITARYID,personaldata.RANK,personaldata.UNIT FROM analytics,personaldata "
                    + "WHERE analytics.MILITARYID = personaldata.MILITARYID AND analytics.STAGE = '" + stage.getValue() + "' AND personaldata.UNIT = '" + uinte.getValue() + "'AND analytics.ANALYTICSYEAR = '" + year.getValue() + "' ");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    private ResultSet getAllAnalyticsName() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.getData("SELECT analytics.ID,analytics.MILITARYID,analytics.STAGE,analytics.ANALYTICSDATE,personaldata.NAME,personaldata.MILITARYID,personaldata.RANK,personaldata.UNIT FROM analytics,personaldata "
                    + "WHERE analytics.MILITARYID = personaldata.MILITARYID AND analytics.STAGE = '" + stage.getValue() + "' AND analytics.ANALYTICSYEAR = '" + year.getValue() + "' ");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    private ResultSet getUnAnalyticsNameByUint() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.getData("SELECT distinct personaldata.MILITARYID ,personaldata.NAME,personaldata.MILITARYID,personaldata.RANK,personaldata.UNIT FROM analytics,personaldata "
                    + "WHERE personaldata.MILITARYID NOT IN(select analytics.MILITARYID from analytics) AND analytics.STAGE = '" + stage.getValue() + "' AND personaldata.UNIT = '" + uinte.getValue() + "'AND analytics.ANALYTICSYEAR = '" + year.getValue() + "' ");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    private ResultSet getAllUnAnalyticsName() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.getData("SELECT distinct personaldata.MILITARYID ,personaldata.NAME,personaldata.MILITARYID,personaldata.RANK,personaldata.UNIT FROM analytics,personaldata "
                    + "WHERE personaldata.MILITARYID NOT IN(select analytics.MILITARYID from analytics) AND analytics.STAGE = '" + stage.getValue() + "' AND analytics.ANALYTICSYEAR = '" + year.getValue() + "' ");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    private ResultSet getAnalyticsNameByMilitaryid() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.getData("SELECT analytics.ID,analytics.MILITARYID,analytics.STAGE,analytics.ANALYTICSDATE,personaldata.NAME,personaldata.MILITARYID,personaldata.RANK,personaldata.UNIT FROM analytics,personaldata "
                    + "WHERE analytics.MILITARYID = personaldata.MILITARYID AND analytics.MILITARYID = '" + searchText.getText() + "' AND analytics.ANALYTICSYEAR = '" + year.getValue() + "' ");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    private ResultSet getAnalyticsNameByYear() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.getData("SELECT analytics.ID,analytics.MILITARYID,analytics.STAGE,analytics.ANALYTICSDATE,personaldata.NAME,personaldata.MILITARYID,personaldata.RANK,personaldata.UNIT FROM analytics,personaldata "
                    + "WHERE analytics.ANALYTICSYEAR = '" + year.getValue() + "' ");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    private ResultSet getAnalyticsNameByName() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.getData("SELECT analytics.ID,analytics.MILITARYID,analytics.STAGE,analytics.ANALYTICSDATE,personaldata.NAME,personaldata.MILITARYID,personaldata.RANK,personaldata.UNIT FROM analytics,personaldata "
                    + "WHERE analytics.MILITARYID = personaldata.MILITARYID AND personaldata.NAME LIKE '" + "%" + searchText.getText() + "%" + "' AND analytics.ANALYTICSYEAR = '" + year.getValue() + "' ");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    @FXML
    private boolean disableUint() {
        if (selecteUint.isSelected()) {
            uinte.setDisable(false);
            return true;
        } else {
            uinte.setDisable(true);
        }
        return false;
    }

    @FXML
    private void enableSearchDate(ActionEvent event) {
    }

}
