package controllers;

import Validation.FormValidation;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import modeles.CensusModel;

public class CensusPageController implements Initializable {

    @FXML
    private ComboBox<?> DateDay;
    @FXML
    private ComboBox<?> DateMonth;
    @FXML
    private ComboBox<?> DateYear;
    @FXML
    private ComboBox<String> uint;
    @FXML
    private TextField originalCensusOF;
    @FXML
    private TextField originalCensusSR;
    @FXML
    private TextField currentCensusOF;
    @FXML
    private TextField currentCensusSR;
    @FXML
    private TextField OrdinaryVacationOF;
    @FXML
    private TextField OrdinaryVacationSR;
    @FXML
    private TextField OccasionalVacationOF;
    @FXML
    private TextField OccasionalVacationSR;
    @FXML
    private TextField SickleaveOF;
    @FXML
    private TextField SickleaveSR;
    @FXML
    private TextField QuarantineOF;
    @FXML
    private TextField QuarantineSR;
    @FXML
    private TextField InareaTrainingOF;
    @FXML
    private TextField InareaTrainingSR;
    @FXML
    private TextField OutareaTrainingOF;
    @FXML
    private TextField OutareaTrainingSR;
    @FXML
    private TextField OutKingdomTrainingOF;
    @FXML
    private TextField OutKingdomTrainingSR;
    @FXML
    private TextField OfficialMissionOF;
    @FXML
    private TextField OfficialMissionSR;
    @FXML
    private TextField JoblMissionOF;
    @FXML
    private TextField JobMissionSR;
    @FXML
    private TextField hospitalOF;
    @FXML
    private TextField hospitalSR;
    @FXML
    private TextField outKingdomJobOF;
    @FXML
    private TextField outKingdomJobSR;
    @FXML
    private TextField outOftheForceOF;
    @FXML
    private TextField outOftheForceSR;
    @FXML
    private TextField alternatesOF;
    @FXML
    private TextField alternatesSR;
    @FXML
    private TextField administrativeleaveOF;
    @FXML
    private TextField administrativeleaveSR;
    @FXML
    private TextField lateOF;
    @FXML
    private TextField lateSR;
    @FXML
    private TextField AbsenceOF;
    @FXML
    private TextField AbsenceSR;
    @FXML
    private TextField PrisonOF;
    @FXML
    private TextField PrisonSR;
    @FXML
    private TableView<CensusModel> censusTable;
    @FXML
    private TableColumn<?, ?> uint_col;
    @FXML
    private TableColumn<?, ?> originalCensus_col;
    @FXML
    private TableColumn<?, ?> currentCensus_col;
    @FXML
    private TableColumn<?, ?> OrdinaryVacation_col;
    @FXML
    private TableColumn<?, ?> OccasionalVacation_col;
    @FXML
    private TableColumn<?, ?> Sickleave_col;
    @FXML
    private TableColumn<?, ?> Quarantine_col;
    @FXML
    private TableColumn<?, ?> InareaTraining_col;
    @FXML
    private TableColumn<?, ?> OutareaTraining_col;
    @FXML
    private TableColumn<?, ?> OfficialMission_col;
    @FXML
    private TableColumn<?, ?> JoblMission_col1;
    @FXML
    private TableColumn<?, ?> hospital_col;
    @FXML
    private TableColumn<?, ?> outKingdomJob_col;
    @FXML
    private TableColumn<?, ?> outOftheForce_col;
    @FXML
    private TableColumn<?, ?> alternates_col;
    @FXML
    private TableColumn<?, ?> administrativeleave_col;
    @FXML
    private TableColumn<?, ?> late_col;
    @FXML
    private TableColumn<?, ?> Absence_col;
    ObservableList<String> uintComboBoxlist = FXCollections.observableArrayList();
    ObservableList<CensusModel> placeList = FXCollections.observableArrayList();
    @FXML
    private TableColumn<?, ?> Prison_col;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AppDate.setDateValue(DateDay, DateMonth, DateYear);
        AppDate.setCurrentDate(DateDay, DateMonth, DateYear);
        clearListCombobox();
        refreshListCombobox();
    }

    @FXML
    private void searchData(ActionEvent event) {
    }

    @FXML
    private void save(ActionEvent event) {
        String tableName = "census";
        String daydate = AppDate.getDate(DateDay, DateMonth, DateYear);
        String[] OFdata = {uint.getValue(), daydate, originalCensusOF.getText(), currentCensusOF.getText(), OrdinaryVacationOF.getText(), OccasionalVacationOF.getText(),
            SickleaveOF.getText(), QuarantineOF.getText(), InareaTrainingOF.getText(), OutareaTrainingOF.getText(), OutKingdomTrainingOF.getText(), OfficialMissionOF.getText(), JoblMissionOF.getText(),
            hospitalOF.getText(), outKingdomJobOF.getText(), outOftheForceOF.getText(), alternatesOF.getText(), administrativeleaveOF.getText(), lateOF.getText(),
            AbsenceOF.getText(), PrisonOF.getText(), "OF"};

        String[] SRdata = {uint.getValue(), daydate, originalCensusSR.getText(), currentCensusSR.getText(), OrdinaryVacationSR.getText(), OccasionalVacationSR.getText(),
            SickleaveSR.getText(), QuarantineSR.getText(), InareaTrainingSR.getText(), OutareaTrainingSR.getText(), OutKingdomTrainingSR.getText(), OfficialMissionSR.getText(), JobMissionSR.getText(),
            hospitalSR.getText(), outKingdomJobSR.getText(), outOftheForceSR.getText(), alternatesSR.getText(), administrativeleaveSR.getText(), lateSR.getText(),
            AbsenceSR.getText(), PrisonSR.getText(), "SR"};
        String fieldName = "`uint`,`dayDate`,`originalCensus`,`currentCensus`,`OrdinaryVacation`,`OccasionalVacation`,`Sickleave`,`Quarantine`,"
                + "`InareaTraining`,`OutareaTraining`,` OutKingdomTraining`,`OfficialMission`,`JoblMission`,`hospital`,`outKingdomJob`,`outOftheForce`,`alternates`,"
                + "`administrativeleave`,`late`,`Absence`,`Prison`,`type`";
        String valuenumbers = "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?";
        TextField[] textfield = {originalCensusOF, originalCensusSR, currentCensusOF, currentCensusSR, OrdinaryVacationOF, OrdinaryVacationSR, OccasionalVacationOF,
            OccasionalVacationSR, SickleaveOF, SickleaveSR, QuarantineOF, QuarantineSR, InareaTrainingOF, InareaTrainingSR, OutareaTrainingOF, OutareaTrainingSR, OutKingdomTrainingOF,
            OutKingdomTrainingSR, OfficialMissionOF, OfficialMissionSR, JoblMissionOF, JobMissionSR, hospitalOF, hospitalSR, outKingdomJobOF, outKingdomJobSR, outOftheForceOF, outOftheForceSR,
            alternatesOF, alternatesSR, administrativeleaveOF, administrativeleaveSR, lateOF, lateSR, AbsenceOF, AbsenceSR, PrisonOF, PrisonSR};

        boolean textfieldState = FormValidation.textFieldNotEmpty(textfield, "الرجاء تعبئة الحقل الفارغ");

        boolean censusNotexisting = FormValidation.ifexisting("census", "`uint`,`dayDate`", "uint ='" + uint.getValue() + "' AND dayDate='" + daydate + "'", "تم ادخال تمام " + " " + uint.getValue() + "لهذا اليوم");
        if (textfieldState && censusNotexisting) {
            try {
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, OFdata);
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, SRdata);
//                refreshArchefTableView();
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void edit(ActionEvent event) {
        try {
            String[] fi = {"originalCensus", "currentCensus", "OrdinaryVacation", "OccasionalVacation", "Sickleave", "Quarantine", "InareaTraining",
                "OutareaTraining", "OutKingdomTraining", "OfficialMission", "JoblMission", "hospital", "outKingdomJob", "outOftheForce", "alternates",
                "administrativeleave", "late", "Absence", "Prison"};
            ResultSet rs = DatabaseAccess.getSum("census", fi, "uint = '" + uint.getValue() + "' AND dayDate = '" + AppDate.getDate(DateDay, DateMonth, DateYear) + "'");

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String name = rsmd.getColumnName(i);
                System.out.println(name);
            }
//            if (rs.next()) {
//                System.out.println(rs.getCursorName());
//            }

        } catch (SQLException ex) {
            Logger.getLogger(CensusPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void delete(ActionEvent event) {
    }

    @FXML
    private void clear(ActionEvent event) {
    }

    private ObservableList filleUint(ObservableList list) {
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

    private void refreshListCombobox() {
        uint.setItems(filleUint(uintComboBoxlist));
    }

    public void clearListCombobox() {
        uint.getItems().clear();
    }

//    private void censusTableView() {
//        try {
//            ResultSet rs = DatabaseAccess.select("census");
//            while (rs.next()) {
//                placeList.add(new CensusModel(
//                        rs.getString("PLACEID"),
//                        rs.getString("PLACENAME")
//                ));
//            }
//            rs.close();
//        } catch (SQLException | IOException ex) {
//            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
//        }
//        placeid_col.setCellValueFactory(new PropertyValueFactory<>("placeid"));
//        coursplace_col.setCellValueFactory(new PropertyValueFactory<>("placeName"));
//
//        destinationTable.setItems(placeList);
//    }
}
