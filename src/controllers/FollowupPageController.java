package controllers;

import Serveces.FollowupPageListener;
import Validation.FormValidation;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import modeles.FollowupModel;

public class FollowupPageController implements Initializable {

    @FXML
    private ComboBox<String> searchType;
    @FXML
    private ComboBox<String> circularType;
    @FXML
    private TextField circularid;
    @FXML
    private ComboBox<String> circularDateDay;
    @FXML
    private ComboBox<String> circularDateMonth;
    @FXML
    private ComboBox<String> circularDateYear;
    @FXML
    private TextField topic;
    @FXML
    private TextField Required;
    @FXML
    private TextField Status;
    @FXML
    private ComboBox<String> CompletionDateDay;
    @FXML
    private ComboBox<String> CompletionDateMonth;
    @FXML
    private ComboBox<String> CompletionDateYear;
    @FXML
    private VBox vbox;
    List<FollowupModel> followupObject = new ArrayList<>();
    private FollowupPageListener mylistener;
    String circularID, cirularDate, tablename,tableYearName;
    String tableId;
    ObservableList<String> circularTypelist = FXCollections.observableArrayList("الوارد الخارجي", "الصادرالخارجي", "الوارد الداخلي", "الصادر الداخلي","السري");
    ObservableList<String> searchTypelist = FXCollections.observableArrayList("المعاملات تحت الاجراء", "المعاملات المنتهية");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AppDate.setDateValue(circularDateDay, circularDateMonth, circularDateYear);
        AppDate.setCurrentDate(circularDateDay, circularDateMonth, circularDateYear);
        AppDate.setCompletionDateValue(CompletionDateDay, CompletionDateMonth, CompletionDateYear);
        AppDate.setCurrentDate(CompletionDateDay, CompletionDateMonth, CompletionDateYear);
        circularType.setItems(circularTypelist);
        searchType.setItems(searchTypelist);
        refreshData();
        clear(null);
    }

    @FXML
    private void searchData(ActionEvent event) {
        String searchValue = searchType.getValue();
        switch (searchValue) {
            case "المعاملات تحت الاجراء":
                followupObject.clear();
                vbox.getChildren().clear();
                viewdata(getUnderProcedur());
                break;
            case "المعاملات المنتهية":
                followupObject.clear();
                vbox.getChildren().clear();
                viewdata(getFinshed());
                break;
        }
    }

    private ResultSet getUnderProcedur() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.select("followup", "OPENSTAT = '0'");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    private ResultSet getFinshed() {
        ResultSet rs = null;
        try {
            rs = DatabaseAccess.select("followup", "OPENSTAT = '1'");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    @FXML
    private void save(ActionEvent event) {
        String tableName = "followup";
        String fieldName = "`CIRCULARID`,`CIRCULARDATE`,`TOPIC`,`REQUIRED`,`STATUS`,`COMPLETIONDATE`,`TABLENAME`,`TABLEYEARNAME`,`TABLEID`";
        String[] data = {circularid.getText(), getCircularDate(), topic.getText(), Required.getText(), Status.getText(), getCompletionDate(), tablename ,tableYearName, tableId};
        String valuenumbers = "?,?,?,?,?,?,?,?,?";

//        boolean circularidState = FormValidation.textFieldNotEmpty(circularid, "الرجاء ادخال رقم المعاملة");
//        boolean circularTypeState = FormValidation.comboBoxNotEmpty(circularType, "الرجاء ادخال رقم المعاملة");
        boolean topicState = FormValidation.textFieldNotEmpty(topic, "الرجاء ادخال الموضوع");
        boolean RequiredState = FormValidation.textFieldNotEmpty(Required, "الرجاء ادخال الاجراء المطلوب");
        boolean circularidExisting = FormValidation.ifexisting("followup", "CIRCULARID", "CIRCULARID = '" + circularid.getText() + "' AND CIRCULARDATE = '" + getCircularDate() + "'", "تم اداخال المعاملة مسبقا");

        if (RequiredState  && topicState && circularidExisting ) {
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
        String tableName = "followup";
        String fieldName = "`CIRCULARID`=?,`CIRCULARDATE`=?,`TOPIC`=?,`REQUIRED`=?,`STATUS`=?,`COMPLETIONDATE`=?,`TABLENAME`=?,`TABLEYEARNAME`=?,`TABLEID`=?";
        String[] data = {circularid.getText(), getCircularDate(), topic.getText(), Required.getText(), Status.getText(), getCompletionDate(), tablename,tableYearName, tableId};

        boolean circularidState = FormValidation.textFieldNotEmpty(circularid, "الرجاء ادخال رقم المعاملة");
        boolean topicState = FormValidation.textFieldNotEmpty(topic, "الرجاء ادخال الموضوع");
        boolean RequiredState = FormValidation.textFieldNotEmpty(Required, "الرجاء ادخال الاجراء المطلوب");

        if (RequiredState && circularidState && topicState) {
            try {
                int t = DatabaseAccess.updat(tableName, fieldName, data, "CIRCULARID = '" + circularID + "' AND CIRCULARDATE = '" + cirularDate + "'");
                if (t > 0) {
                    FormValidation.showAlert("", "تم تحديث البيانات", Alert.AlertType.CONFIRMATION);
                }
                refreshData();
                clear(event);
            } catch (IOException | SQLException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void delete(ActionEvent event) {
        try {
            DatabaseAccess.delete("followup", "CIRCULARID = '" + circularID + "' AND CIRCULARDATE = '" + cirularDate + "'");
            refreshData();
            clear(event);
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clear(ActionEvent event) {
        circularid.setText(null);
        AppDate.setCurrentDate(circularDateDay, circularDateMonth, circularDateYear);
        topic.setText(null);
        Required.setText(null);
        Status.setText(null);
        AppDate.setCurrentDate(CompletionDateDay, CompletionDateMonth, CompletionDateYear);
        refreshData();
    }

    private void refreshData() {
        try {
            followupObject.clear();
            vbox.getChildren().clear();
            viewdata(DatabaseAccess.select("followup", "OPENSTAT = '0' ORDER BY CIRCULARDATE DESC"));
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    private void viewdata(ResultSet rs) {
        followupObject.addAll(getData(rs));
        if (followupObject.size() > 0) {
            mylistener = new FollowupPageListener() {
                @Override
                public void onClickListener(FollowupModel followupModel) {
                    setChosendata(followupModel);
                }
            };
        }
        try {
            for (FollowupModel followupModel : followupObject) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/view/FollowupItem.fxml"));
                AnchorPane pane = fxmlLoader.load();
                FollowupItemController followupItemController = fxmlLoader.getController();
                followupItemController.setData(followupModel, mylistener);
                vbox.getChildren().add(pane);
            }
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    private List<FollowupModel> getData(ResultSet rs) {
        List<FollowupModel> followupObjects = new ArrayList<>();
        FollowupModel followupModel;
        try {
            while (rs.next()) {
                followupModel = new FollowupModel();
                followupModel.setCirculardate(rs.getString("CIRCULARDATE"));
                followupModel.setCircularid(rs.getString("CIRCULARID"));
                followupModel.setTopic(rs.getString("TOPIC"));
                followupModel.setRequired(rs.getString("REQUIRED"));
                followupModel.setStatus(rs.getString("STATUS"));
                followupModel.setCompletiondate(rs.getString("COMPLETIONDATE"));
                followupModel.setOpenStat(rs.getInt("OPENSTAT"));
                followupModel.setTableName(rs.getString("TABLENAME"));
                followupModel.setTableId(rs.getString("TABLEID"));
                followupModel.setTableYearName(rs.getString("TABLEYEARNAME"));
                followupObjects.add(followupModel);
            }
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return followupObjects;
    }

    private void setChosendata(FollowupModel followupModel) {
        circularid.setText(followupModel.getCircularid());
        setCircularDate(followupModel.getCirculardate());
        topic.setText(followupModel.getTopic());
        Required.setText(followupModel.getRequired());
        Status.setText(followupModel.getStatus());
        setCompletionDate(followupModel.getCompletiondate());
        circularID = followupModel.getCircularid();
        cirularDate = followupModel.getCirculardate();
    }

    public void setCircularDate(String date) {
        AppDate.setSeparateDate(circularDateDay, circularDateMonth, circularDateYear, date);
    }

    public String getCircularDate() {
        return AppDate.getDate(circularDateDay, circularDateMonth, circularDateYear);
    }

    public void setCompletionDate(String date) {
        AppDate.setSeparateDate(CompletionDateDay, CompletionDateMonth, CompletionDateYear, date);
    }

    public String getCompletionDate() {
        return AppDate.getDate(CompletionDateDay, CompletionDateMonth, CompletionDateYear);
    }

    @FXML
    private void getCircularData(ActionEvent event) {
        String typeValue = circularType.getValue();
        if (typeValue == null || "".equals(typeValue)) {
            FormValidation.showAlert(null, "الرجاء اختيار نوع المعاملة", Alert.AlertType.ERROR);
        } else {
            switch (typeValue) {
                case "الوارد الخارجي":
                    try {
//                        ResultSet rs = DatabaseAccess.select("externalincoming", "RECEIPTNUMBER = '" + circularid.getText() + "' AND ARSHEFYEAR ='" + HijriCalendar.getSimpleYear() + "'");
                    ResultSet rs = DatabaseAccess.selectQuiry("SELECT CIRCULARDATE,TOPIC FROM externalincoming WHERE RECEIPTNUMBER = '" + circularid.getText() + "' AND ARSHEFYEAR ='" + AppDate.getYear(getCircularDate()) + "'");
                    if (rs.next()) {
                        setCircularDate(rs.getString("CIRCULARDATE"));
                        topic.setText(rs.getString("TOPIC"));
                        tablename = "externalincoming";
                        tableId = "RECEIPTNUMBER";
                        tableYearName = "ARSHEFYEAR";
                    }
                } catch (IOException | SQLException ex) {
                    FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
                }
                break;
                case "الصادرالخارجي":
                    try {
//                        ResultSet rs = DatabaseAccess.select("exportsdata", "EXPORTNUM = '" + circularid.getText() + "' AND RECORDYEAR ='" + HijriCalendar.getSimpleYear() + "'");
                    ResultSet rs = DatabaseAccess.selectQuiry("SELECT TOPIC,EXPORTDATE FROM exportsdata WHERE EXPORTNUM = '" + circularid.getText() + "'AND RECORDYEAR = '" + AppDate.getYear(getCircularDate()) + "'");
                    if (rs.next()) {
                        setCircularDate(rs.getString("EXPORTDATE"));
                        topic.setText(rs.getString("TOPIC"));
                        tablename = "exportsdata";
                        tableId = "ID";
                        tableYearName = "RECORDYEAR";
                    }
                } catch (IOException | SQLException ex) {
                    FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
                }
                break;
                case "الوارد الداخلي":
                    try {
//                        ResultSet rs = DatabaseAccess.select("internalincoming", "REGIS_NO = '" + circularid.getText() + "' AND RECORD_YEAR ='" + HijriCalendar.getSimpleYear() + "'");
                    ResultSet rs = DatabaseAccess.selectQuiry("SELECT  RECIPIENT_DATE,TOPIC FROM internalincoming WHERE REGIS_NO = '" + circularid.getText() + "' AND RECORD_YEAR ='" +AppDate.getYear(getCircularDate()) + "'");
                    if (rs.next()) {
                        setCircularDate(rs.getString("RECIPIENT_DATE"));
                        topic.setText(rs.getString("TOPIC"));
                        tablename = "internalincoming";
                        tableId = "REGIS_NO";
                        tableYearName = "RECORD_YEAR";
                    }
                } catch (IOException | SQLException ex) {
                    FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
                }
                break;
                case "الصادر الداخلي":
                    try {
//                        ResultSet rs = DatabaseAccess.select("internalexports", "REGISNO = '" + circularid.getText() + "' AND RECORDYEAR ='" + HijriCalendar.getSimpleYear() + "'");
                    ResultSet rs = DatabaseAccess.selectQuiry("SELECT EXPORTDATE,TOPIC FROM internalexports WHERE REGISNO = '" + circularid.getText() + "' AND RECORDYEAR ='" + AppDate.getYear(getCircularDate()) + "'");
                    if (rs.next()) {
                        setCircularDate(rs.getString("EXPORTDATE"));
                        topic.setText(rs.getString("TOPIC"));
                        tablename = "internalexports";
                        tableId = "REGISNO";
                        tableYearName = "RECORDYEAR";
                    }
                } catch (IOException | SQLException ex) {
                    FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
                }
                break;
                case "السري":
                    try {
//                        ResultSet rs = DatabaseAccess.select("internalexports", "REGISNO = '" + circularid.getText() + "' AND RECORDYEAR ='" + HijriCalendar.getSimpleYear() + "'");
                    ResultSet rs = DatabaseAccess.selectQuiry("SELECT RECEIPTDATE,TOPIC FROM secretdata WHERE RECEIPTNUMBER = '" + circularid.getText() + "' AND RECORDYEAR ='" + AppDate.getYear(getCircularDate()) + "'");
                    if (rs.next()) {
                        setCircularDate(rs.getString("RECEIPTDATE"));
                        topic.setText(rs.getString("TOPIC"));
                        tablename = "secretdata";
                        tableId = "RECEIPTNUMBER";
                        tableYearName = "RECORDYEAR";
                    }
                } catch (IOException | SQLException ex) {
                    FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
                }
                break;
            }
        }
    }

    @FXML
    private void getTableNameAndTableId(ActionEvent event) {
        String typeValue = circularType.getValue();
        if (typeValue == null || "".equals(typeValue)) {
            FormValidation.showAlert(null, "الرجاء اختيار نوع المعاملة", Alert.AlertType.ERROR);
        } else {
            switch (typeValue) {
                case "الوارد الخارجي":
                    tablename = "externalincoming";
                    tableId = "CIRCULARID";
                    System.out.println(tablename + "" + tableId);
                    break;
                case "الصادرالخارجي":
                    tablename = "exportsdata";
                    tableId = "ID";
                    System.out.println(tablename + "" + tableId);
                    break;
                case "الوارد الداخلي":
                    tablename = "internalincoming";
                    tableId = "REGIS_NO";
                    System.out.println(tablename + "" + tableId);
                    break;
                case "الصادر الداخلي":
                    tablename = "internalexports";
                    tableId = "REGISNO";
                    System.out.println(tablename + "" + tableId);
                    break;
            }
        }
    }

}
