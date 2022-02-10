package controllers;

import Serveces.MissionPageListener;
import Validation.FormValidation;
import arshef.App;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import modeles.MissionModel;

public class MissionItemController implements Initializable {

    @FXML
    private Label missionName;
    @FXML
    private Label startDate;
    @FXML
    private Label endDate;

    private MissionModel missionModel;
    private MissionPageListener mylistener;
    String missionID = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setData(MissionModel missionModel, MissionPageListener mylistener) {
        this.missionModel = missionModel;
        this.mylistener = mylistener;
        missionName.setText(missionModel.getMissionname());
        startDate.setText(missionModel.getStartdate());
        endDate.setText(missionModel.getEnddate());
        missionID = this.missionModel.getMissionid();
    }

    @FXML
    private void addNames(ActionEvent event) {
        App.lodAddMissionNmaesPage(missionID);
    }

    @FXML
    private void click(MouseEvent event) {
        mylistener.onClickListener(missionModel);
    }

    @FXML
    private void printExcle(ActionEvent event) {
         try {
            FileChooser fileChooser = new FileChooser();
            Window stage = null;
            fileChooser.setInitialFileName(missionModel.getMissionname());
            File file = fileChooser.showSaveDialog(stage);
            String savefile = null;
            if (file != null) {
                savefile = file.toString();
            }
            ResultSet rs = DatabaseAccess.getData("SELECT missionnames.MILITARYID,personaldata.NAME,personaldata.MILITARYID,personaldata.RANK,personaldata.UNIT FROM missionnames,personaldata "
                    + "WHERE missionnames.MILITARYID = personaldata.MILITARYID AND MISSIONID = '" + missionID + "'");
            String[] feild = {"MILITARYID", "RANK", "NAME", "UNIT"};
            String[] titel = {"الرقم العسكري", "الرتبة", "الاسم", "الوحدة"};
            String[] sheetTitel = {"اسماء المشاركين في " + missionModel.getMissionname()};
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
