package controllers;

import Validation.FormValidation;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class VacationPageController implements Initializable {

    @FXML
    private TextField militaryId;
    @FXML
    private VBox missionView;
    @FXML
    private VBox namesView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void save(ActionEvent event) {
        String tableName = "vacationnames";
        String fieldName = "`MILITARYID`";
        String[] data = {militaryId.getText()};
        String valuenumbers = "?";

        boolean militaryIdState = FormValidation.textFieldNotEmpty(militaryId, "الرجاء ادخال الموضوع");

        if (militaryIdState) {
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
    }

    @FXML
    private void delete(ActionEvent event) {
    }

    @FXML
    private void clear(ActionEvent event) {
    }

//    private void refreshdata() {
//        try {
//            missionObject.clear();
//            missionView.getChildren().clear();
//            viewdata(DatabaseAccess.getData("SELECT MISSIONID,MISSIONNAME,STARTDATE,ENDDATAE FROM missiondata"));
//        } catch (IOException ex) {
//            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
//        }
//    }
//
//    private void refreshNameData() {
//        try {
//            namesObject.clear();
//            namesView.getChildren().clear();
//            viewNameData(DatabaseAccess.getData("SELECT missionnames.MILITARYID,personaldata.NAME,personaldata.MILITARYID,personaldata.RANK,personaldata.UNIT FROM missionnames,personaldata "
//                    + "WHERE missionnames.MILITARYID = personaldata.MILITARYID AND MISSIONID = '" + missionid + "'"));
//        } catch (IOException ex) {
//            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
//        }
//    }
//
//    private List<MissionModel> getData(ResultSet rs) {
//        List<MissionModel> missionlist = new ArrayList<>();
//        MissionModel missionModel;
//        try {
//            while (rs.next()) {
//                missionModel = new MissionModel();
//                missionModel.setMissionid(rs.getString("MISSIONID"));
//                missionModel.setMissionname(rs.getString("MISSIONNAME"));
//                missionModel.setStartdate(rs.getString("STARTDATE"));
//                missionModel.setEnddate(rs.getString("ENDDATAE"));
//                missionlist.add(missionModel);
//            }
//        } catch (SQLException ex) {
//            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
//        }
//        return missionlist;
//    }
//
//    private List<UserModel> getNameData(ResultSet rs) {
//        List<UserModel> namelist = new ArrayList<>();
//        UserModel userModel;
//        try {
//            int squnce = 1;
//            while (rs.next()) {
//                userModel = new UserModel();
//                userModel.setSqunce(squnce++);
//                userModel.setMilitaryid(rs.getString("MILITARYID"));
//                userModel.setRank(rs.getString("RANK"));
//                userModel.setName(rs.getString("NAME"));
//                userModel.setUint(rs.getString("UNIT"));
//                namelist.add(userModel);
//            }
//        } catch (SQLException ex) {
//            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
//        }
//        return namelist;
//    }
//
//    private void setChosendata(MissionModel missionModel) {
//        missionname.setText(missionModel.getMissionname());
//        setStartDate(missionModel.getStartdate());
//        setEndDate(missionModel.getEnddate());
//        missionid = missionModel.getMissionid();
//    }
//
//    private void viewdata(ResultSet rs) {
//        missionObject.addAll(getData(rs));
//        if (missionObject.size() > 0) {
//            // setChosendata(missionObject.get(0));
//            mylistener = new MissionPageListener() {
//                @Override
//                public void onClickListener(MissionModel missionModel) {
//                    setChosendata(missionModel);
//                    refreshNameData();
//                }
//            };
//        }
//
//        try {
//            for (MissionModel missionModel : missionObject) {
//                FXMLLoader fxmlLoader = new FXMLLoader();
//                fxmlLoader.setLocation(getClass().getResource("/view/MissionItem.fxml"));
//                AnchorPane pane = fxmlLoader.load();
//                missionItemController = fxmlLoader.getController();
//                missionItemController.setData(missionModel, mylistener);
//                missionView.getChildren().add(pane);
//            }
//        } catch (IOException ex) {
//            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
//        }
//    }
//
//    private void viewNameData(ResultSet rs) {
//        namesObject.addAll(getNameData(rs));
//        if (!namesObject.isEmpty()) {
//            try {
//                for (UserModel userModel : namesObject) {
//                    FXMLLoader fxmlLoader = new FXMLLoader();
//                    fxmlLoader.setLocation(getClass().getResource("/view/MissionNamesItem.fxml"));
//                    AnchorPane pane = fxmlLoader.load();
//                    missionNamesItemController = fxmlLoader.getController();
//                    missionNamesItemController.setData(userModel, myNamelistener);
//                    namesView.getChildren().add(pane);
//                }
//            } catch (IOException ex) {
//                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
//            }
//        }
//    }
}
