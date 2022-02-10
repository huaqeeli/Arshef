package controllers;

import Serveces.MissionNameListener;
import Serveces.MissionPageListener;
import Validation.FormValidation;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import modeles.MissionModel;
import modeles.UserModel;

public class MissionPageController implements Initializable {

    @FXML
    private ComboBox<?> DateDay;
    @FXML
    private ComboBox<?> DateMonth;
    @FXML
    private ComboBox<?> DateYear;
    @FXML
    private VBox missionView;
    @FXML
    private VBox namesView;

    public final List<MissionModel> missionObject = new ArrayList<>();
    public final List<UserModel> namesObject = new ArrayList<>();
    private MissionPageListener mylistener;
    private MissionNameListener myNamelistener;
    @FXML
    private TextField missionname;
    @FXML
    private ComboBox<String> statrDateDay;
    @FXML
    private ComboBox<String> statrDateMonth;
    @FXML
    private ComboBox<String> statrDateYear;
    @FXML
    private ComboBox<String> endDateDay;
    @FXML
    private ComboBox<String> endDateMonth;
    @FXML
    private ComboBox<String> endDateYear;
    String missionid = null;
    private MissionItemController missionItemController;
    private MissionNamesItemController missionNamesItemController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        setEndDate(HijriCalendar.getSimpleDate());
        setStartDate(HijriCalendar.getSimpleDate());
        AppDate.setDateValue(statrDateDay, statrDateMonth, statrDateYear);
        AppDate.setDateValue(endDateDay, endDateMonth, endDateYear);
        refreshdata();
    }

    @FXML
    private void searchData(ActionEvent event) {
    }

    @FXML
    private void save(ActionEvent event) {
        String tableName = "missiondata";
        String[] data = {missionname.getText(), getStartDate(), getEndDate()};
        String fieldName = "`MISSIONNAME`,`STARTDATE`,`ENDDATAE`";
        String valuenumbers = "?,?,?";

        boolean missionnameState = FormValidation.textFieldNotEmpty(missionname, "الرجاء ادخل اسم المهمة");

        if (missionnameState) {
            try {
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, data);
                refreshdata();
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void edit(ActionEvent event) {
        String tableName = "missiondata";
        String[] data = {missionname.getText(), getStartDate(), getEndDate()};
        String fieldName = "`MISSIONNAME`=?,`STARTDATE`=?,`ENDDATAE`=?";

        boolean missionnameState = FormValidation.textFieldNotEmpty(missionname, "الرجاء ادخل اسم المهمة");

        if (missionnameState) {
            try {
                DatabaseAccess.updat(tableName, fieldName, data,"MISSIONID = '"+missionid+"'");
                refreshdata();
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void delete(ActionEvent event) {
        try {
            boolean state = DatabaseAccess.deleteAll("missiondata", "MISSIONID = '"+missionid+"'");
            if (state) {
                DatabaseAccess.delete("missionnames", "MISSIONID = '"+missionid+"'");
                FormValidation.showAlert(null, "تم حذف بيانات "+ missionname.getText(), Alert.AlertType.ERROR);
                refreshdata();
            }
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clear(ActionEvent event) {
    }

    private void refreshdata() {
        try {
            missionObject.clear();
            missionView.getChildren().clear();
            viewdata(DatabaseAccess.getData("SELECT MISSIONID,MISSIONNAME,STARTDATE,ENDDATAE FROM missiondata"));
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    private void refreshNameData() {
        try {
            namesObject.clear();
            namesView.getChildren().clear();
            viewNameData(DatabaseAccess.getData("SELECT missionnames.MILITARYID,personaldata.NAME,personaldata.MILITARYID,personaldata.RANK,personaldata.UNIT FROM missionnames,personaldata "
                    + "WHERE missionnames.MILITARYID = personaldata.MILITARYID AND MISSIONID = '" + missionid + "'"));
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    private List<MissionModel> getData(ResultSet rs) {
        List<MissionModel> missionlist = new ArrayList<>();
        MissionModel missionModel;
        try {
            while (rs.next()) {
                missionModel = new MissionModel();
                missionModel.setMissionid(rs.getString("MISSIONID"));
                missionModel.setMissionname(rs.getString("MISSIONNAME"));
                missionModel.setStartdate(rs.getString("STARTDATE"));
                missionModel.setEnddate(rs.getString("ENDDATAE"));
                missionlist.add(missionModel);
            }
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return missionlist;
    }

    private List<UserModel> getNameData(ResultSet rs) {
        List<UserModel> namelist = new ArrayList<>();
        UserModel userModel;
        try {
            int squnce = 1;
            while (rs.next()) {
                userModel = new UserModel();
                userModel.setSqunce(squnce++);
                userModel.setMilitaryid(rs.getString("MILITARYID"));
                userModel.setRank(rs.getString("RANK"));
                userModel.setName(rs.getString("NAME"));
                userModel.setUint(rs.getString("UNIT"));
                namelist.add(userModel);
            }
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return namelist;
    }

    private void setChosendata(MissionModel missionModel) {
        missionname.setText(missionModel.getMissionname());
        setStartDate(missionModel.getStartdate());
        setEndDate(missionModel.getEnddate());
        missionid = missionModel.getMissionid();
    }

    private void viewdata(ResultSet rs) {
        missionObject.addAll(getData(rs));
        if (missionObject.size() > 0) {
            setChosendata(missionObject.get(0));
            mylistener = new MissionPageListener() {
                @Override
                public void onClickListener(MissionModel missionModel) {
                    setChosendata(missionModel);
                    refreshNameData();
                }
            };
        }

        try {
            for (MissionModel missionModel : missionObject) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/view/MissionItem.fxml"));
                AnchorPane pane = fxmlLoader.load();
                missionItemController = fxmlLoader.getController();
                missionItemController.setData(missionModel, mylistener);
                missionView.getChildren().add(pane);
            }
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    private void viewNameData(ResultSet rs) {
        namesObject.addAll(getNameData(rs));
        if (!namesObject.isEmpty()) {
            try {
                for (UserModel userModel : namesObject) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/view/MissionNamesItem.fxml"));
                    AnchorPane pane = fxmlLoader.load();
                    missionNamesItemController = fxmlLoader.getController();
                    missionNamesItemController.setData(userModel, myNamelistener);
                    namesView.getChildren().add(pane);
                }
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        } 
    }

    public String getStartDate() {
        return AppDate.getDate(statrDateDay, statrDateMonth, statrDateYear);
    }

    public void setStartDate(String date) {
        AppDate.setSeparateDate(statrDateDay, statrDateMonth, statrDateYear, date);
    }

    public String getEndDate() {
        return AppDate.getDate(endDateDay, endDateMonth, endDateYear);
    }

    public void setEndDate(String date) {
        AppDate.setSeparateDate(endDateDay, endDateMonth, endDateYear, date);
    }
}
