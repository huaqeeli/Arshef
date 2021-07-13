package controllers;

import Validation.FormValidation;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import modeles.CensusModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

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
    @FXML
    private TableColumn<?, ?> Prison_col;
    @FXML
    private TableColumn<?, ?> OutKingdomTraining_col;

    ObservableList<String> uintComboBoxlist = FXCollections.observableArrayList();
    ObservableList<CensusModel> censusList = FXCollections.observableArrayList();
    String tabeluint = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AppDate.setDateValue(DateDay, DateMonth, DateYear);
        AppDate.setCurrentDate(DateDay, DateMonth, DateYear);
        clearListCombobox();
        refreshListCombobox();
        refreshcensusTableView();
        getTableRow(censusTable);
        getTableRowByInterKey(censusTable);
    }

    @FXML
    private void searchData(ActionEvent event) {
        refreshcensusTableView();
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
                + "`InareaTraining`,`OutareaTraining`,`OutKingdomTraining`,`OfficialMission`,`JoblMission`,`hospital`,`outKingdomJob`,`outOftheForce`,`alternates`,"
                + "`administrativeleave`,`late`,`Absence`,`Prison`,`type`";

        String valuenumbers = "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?";
        /*جدول التمام المختصر*/
        String origalOF = ArabicSetting.EnglishToarabic(originalCensusOF.getText());
        String origalSR = ArabicSetting.EnglishToarabic(originalCensusSR.getText());
        String inFieldOF = ArabicSetting.EnglishToarabic(currentCensusOF.getText());
        String inFieldSR = ArabicSetting.EnglishToarabic(currentCensusSR.getText());
        int intoriginalCensusOf = Integer.parseInt(originalCensusOF.getText());
        int intcurrentCensusOF = Integer.parseInt(currentCensusOF.getText());
        int totalOF = intoriginalCensusOf - intcurrentCensusOF;
        String outfieldOF = Integer.toString(totalOF);
        int intoriginalCensusSR = Integer.parseInt(originalCensusSR.getText());
        int intcurrentCensusSR = Integer.parseInt(currentCensusSR.getText());
        int totalSR = intoriginalCensusSR - intcurrentCensusSR;
        String outfieldSR = Integer.toString(totalSR);

        String outFieldOF = ArabicSetting.EnglishToarabic(outfieldOF);
        String outFieldSR = ArabicSetting.EnglishToarabic(outfieldSR);

        String[] mdata = {uint.getValue(), daydate, origalOF, origalSR, inFieldOF, inFieldSR, outFieldOF, outFieldSR};

        String mfieldName = "`uint`,`daydate`,`originalCensusOF`,`originalCensusSR`,`infieldOF`,`infieldSR`,`outfiedOF`,`outfieldSR`";
        String mvaluenumbers = "?,?,?,?,?,?,?,?";

        TextField[] textfield = {originalCensusOF, originalCensusSR, currentCensusOF, currentCensusSR, OrdinaryVacationOF, OrdinaryVacationSR, OccasionalVacationOF,
            OccasionalVacationSR, SickleaveOF, SickleaveSR, QuarantineOF, QuarantineSR, InareaTrainingOF, InareaTrainingSR, OutareaTrainingOF, OutareaTrainingSR, OutKingdomTrainingOF,
            OutKingdomTrainingSR, OfficialMissionOF, OfficialMissionSR, JoblMissionOF, JobMissionSR, hospitalOF, hospitalSR, outKingdomJobOF, outKingdomJobSR, outOftheForceOF, outOftheForceSR,
            alternatesOF, alternatesSR, administrativeleaveOF, administrativeleaveSR, lateOF, lateSR, AbsenceOF, AbsenceSR, PrisonOF, PrisonSR};

        boolean textfieldState = FormValidation.textFieldNotEmpty(textfield, "الرجاء تعبئة الحقل الفارغ");

        boolean censusNotexisting = FormValidation.ifexisting("census", "`uint`,`dayDate`", "uint ='" + uint.getValue() + "' AND dayDate='" + daydate + "'", "تم ادخال تمام " + uint.getValue() + " " + "لهذا اليوم");
        if (textfieldState && censusNotexisting) {
            try {
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, OFdata);
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, SRdata);
                DatabaseAccess.insert("manualtable", mfieldName, mvaluenumbers, mdata);
                refreshcensusTableView();
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void edit(ActionEvent event) {
        String tableName = "census";
        String daydate = AppDate.getDate(DateDay, DateMonth, DateYear);
        String[] OFdata = {originalCensusOF.getText(), currentCensusOF.getText(), OrdinaryVacationOF.getText(), OccasionalVacationOF.getText(),
            SickleaveOF.getText(), QuarantineOF.getText(), InareaTrainingOF.getText(), OutareaTrainingOF.getText(), OutKingdomTrainingOF.getText(), OfficialMissionOF.getText(), JoblMissionOF.getText(),
            hospitalOF.getText(), outKingdomJobOF.getText(), outOftheForceOF.getText(), alternatesOF.getText(), administrativeleaveOF.getText(), lateOF.getText(),
            AbsenceOF.getText(), PrisonOF.getText()};

        String[] SRdata = {originalCensusSR.getText(), currentCensusSR.getText(), OrdinaryVacationSR.getText(), OccasionalVacationSR.getText(),
            SickleaveSR.getText(), QuarantineSR.getText(), InareaTrainingSR.getText(), OutareaTrainingSR.getText(), OutKingdomTrainingSR.getText(), OfficialMissionSR.getText(), JobMissionSR.getText(),
            hospitalSR.getText(), outKingdomJobSR.getText(), outOftheForceSR.getText(), alternatesSR.getText(), administrativeleaveSR.getText(), lateSR.getText(),
            AbsenceSR.getText(), PrisonSR.getText()};
        String fieldName = "`originalCensus`=?,`currentCensus`=?,`OrdinaryVacation`=?,`OccasionalVacation`=?,`Sickleave`=?,`Quarantine`=?,"
                + "`InareaTraining`=?,`OutareaTraining`=?,`OutKingdomTraining`=?,`OfficialMission`=?,`JoblMission`=?,`hospital`=?,`outKingdomJob`=?,`outOftheForce`=?,`alternates`=?,"
                + "`administrativeleave`=?,`late`=?,`Absence`=?,`Prison`=?";
        /*جدول التمام المختصر*/
        String origalOF = ArabicSetting.EnglishToarabic(originalCensusOF.getText());
        String origalSR = ArabicSetting.EnglishToarabic(originalCensusSR.getText());
        String inFieldOF = ArabicSetting.EnglishToarabic(currentCensusOF.getText());
        String inFieldSR = ArabicSetting.EnglishToarabic(originalCensusSR.getText());
        int intoriginalCensusOf = Integer.parseInt(originalCensusOF.getText());
        int intcurrentCensusOF = Integer.parseInt(currentCensusOF.getText());
        int totalOF = intoriginalCensusOf - intcurrentCensusOF;
        String outfieldOF = Integer.toString(totalOF);
        int intoriginalCensusSR = Integer.parseInt(originalCensusSR.getText());
        int intcurrentCensusSR = Integer.parseInt(currentCensusSR.getText());
        int totalSR = intoriginalCensusSR - intcurrentCensusSR;
        String outfieldSR = Integer.toString(totalSR);

        String outFieldOF = ArabicSetting.EnglishToarabic(outfieldOF);
        String outFieldSR = ArabicSetting.EnglishToarabic(outfieldSR);

        String[] mdata = {origalOF, origalSR, inFieldOF, inFieldSR, outFieldOF, outFieldSR};

        String mfieldName = "`originalCensusOF`=?,`originalCensusSR`=?,`infieldOF`=?,`infieldSR`=?,`outfiedOF`=?,`outfieldSR`=?";

        TextField[] textfield = {originalCensusOF, originalCensusSR, currentCensusOF, currentCensusSR, OrdinaryVacationOF, OrdinaryVacationSR, OccasionalVacationOF,
            OccasionalVacationSR, SickleaveOF, SickleaveSR, QuarantineOF, QuarantineSR, InareaTrainingOF, InareaTrainingSR, OutareaTrainingOF, OutareaTrainingSR, OutKingdomTrainingOF,
            OutKingdomTrainingSR, OfficialMissionOF, OfficialMissionSR, JoblMissionOF, JobMissionSR, hospitalOF, hospitalSR, outKingdomJobOF, outKingdomJobSR, outOftheForceOF, outOftheForceSR,
            alternatesOF, alternatesSR, administrativeleaveOF, administrativeleaveSR, lateOF, lateSR, AbsenceOF, AbsenceSR, PrisonOF, PrisonSR};

        boolean textfieldState = FormValidation.textFieldNotEmpty(textfield, "الرجاء تعبئة الحقل الفارغ");
        boolean tabeluintState = FormValidation.notNull(tabeluint, "الرجاء اختيار السجل من الجدول");

        if (textfieldState && tabeluintState) {
            try {
                DatabaseAccess.updat(tableName, fieldName, OFdata, "uint = '" + tabeluint + "' AND dayDate = '" + daydate + "'AND type = 'OF'");
                DatabaseAccess.updat(tableName, fieldName, SRdata, "uint = '" + tabeluint + "' AND dayDate = '" + daydate + "'AND type = 'SR'");
                DatabaseAccess.updat("manualtable", mfieldName, mdata, "uint = '" + tabeluint + "' AND dayDate = '" + daydate + "'");
                refreshcensusTableView();
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void delete(ActionEvent event) {
        boolean tabeluintState = FormValidation.notNull(tabeluint, "الرجاء اختيار السجل من الجدول");
        if (tabeluintState) {
            try {
                DatabaseAccess.delete("census", "uint = '" + tabeluint + "' AND dayDate = '" + AppDate.getDate(DateDay, DateMonth, DateYear) + "'");
                DatabaseAccess.delete("manualtable", "uint = '" + tabeluint + "' AND dayDate = '" + AppDate.getDate(DateDay, DateMonth, DateYear) + "'");
                refreshcensusTableView();
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }

    }

    @FXML
    private void clear(ActionEvent event) {
        originalCensusOF.setText(null);
        currentCensusOF.setText(null);
        OrdinaryVacationOF.setText(null);
        OccasionalVacationOF.setText(null);
        SickleaveOF.setText(null);
        QuarantineOF.setText(null);
        InareaTrainingOF.setText(null);
        OutareaTrainingOF.setText(null);
        OutKingdomTrainingOF.setText(null);
        OfficialMissionOF.setText(null);
        JoblMissionOF.setText(null);
        hospitalOF.setText(null);
        outKingdomJobOF.setText(null);
        outOftheForceOF.setText(null);
        alternatesOF.setText(null);
        administrativeleaveOF.setText(null);
        lateOF.setText(null);
        AbsenceOF.setText(null);
        PrisonOF.setText(null);
        originalCensusSR.setText(null);
        currentCensusSR.setText(null);
        OrdinaryVacationSR.setText(null);
        OccasionalVacationSR.setText(null);
        SickleaveSR.setText(null);
        QuarantineSR.setText(null);
        InareaTrainingSR.setText(null);
        OutareaTrainingSR.setText(null);
        OutKingdomTrainingSR.setText(null);
        OfficialMissionSR.setText(null);
        JobMissionSR.setText(null);
        hospitalSR.setText(null);
        outKingdomJobSR.setText(null);
        outOftheForceSR.setText(null);
        alternatesSR.setText(null);
        administrativeleaveSR.setText(null);
        lateSR.setText(null);
        AbsenceSR.setText(null);
        PrisonSR.setText(null);
        tabeluint = null;
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

    private void censusTableView() {
        try {
            ResultSet rs1 = DatabaseAccess.select("census", "dayDate = '" + AppDate.getDate(DateDay, DateMonth, DateYear) + "'AND type = 'OF'");

            while (rs1.next()) {
                ResultSet rs = DatabaseAccess.getSum("census", "uint = '" + rs1.getString("uint") + "' AND dayDate = '" + AppDate.getDate(DateDay, DateMonth, DateYear) + "'");
                while (rs.next()) {
                    censusList.add(new CensusModel(
                            rs1.getString("uint"),
                            rs.getString("sum(originalCensus)"),
                            rs.getString("sum(currentCensus)"),
                            rs.getString("sum(OrdinaryVacation)"),
                            rs.getString("sum(OccasionalVacation)"),
                            rs.getString("sum(Sickleave)"),
                            rs.getString("sum(Quarantine)"),
                            rs.getString("sum(InareaTraining)"),
                            rs.getString("sum(OutareaTraining)"),
                            rs.getString("sum(OutKingdomTraining)"),
                            rs.getString("sum(OfficialMission)"),
                            rs.getString("sum(JoblMission)"),
                            rs.getString("sum(hospital)"),
                            rs.getString("sum(outKingdomJob)"),
                            rs.getString("sum(outOftheForce)"),
                            rs.getString("sum(alternates)"),
                            rs.getString("sum(administrativeleave)"),
                            rs.getString("sum(late)"),
                            rs.getString("sum(Absence)"),
                            rs.getString("sum(Prison)")
                    ));
                }
                rs.close();
            }
            rs1.close();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        } catch (IOException ex) {
            Logger.getLogger(CensusPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        uint_col.setCellValueFactory(new PropertyValueFactory<>("uint"));
        originalCensus_col.setCellValueFactory(new PropertyValueFactory<>("originalCensus"));
        currentCensus_col.setCellValueFactory(new PropertyValueFactory<>("currentCensus"));
        OrdinaryVacation_col.setCellValueFactory(new PropertyValueFactory<>("OrdinaryVacation"));
        OccasionalVacation_col.setCellValueFactory(new PropertyValueFactory<>("OccasionalVacation"));
        Sickleave_col.setCellValueFactory(new PropertyValueFactory<>("Sickleave"));
        Quarantine_col.setCellValueFactory(new PropertyValueFactory<>("Quarantine"));
        InareaTraining_col.setCellValueFactory(new PropertyValueFactory<>("InareaTraining"));
        OutareaTraining_col.setCellValueFactory(new PropertyValueFactory<>("OutareaTraining"));
        OutKingdomTraining_col.setCellValueFactory(new PropertyValueFactory<>("OutKingdomTraining"));
        OfficialMission_col.setCellValueFactory(new PropertyValueFactory<>("OfficialMission"));
        JoblMission_col1.setCellValueFactory(new PropertyValueFactory<>("JoblMission"));
        hospital_col.setCellValueFactory(new PropertyValueFactory<>("hospital"));
        outKingdomJob_col.setCellValueFactory(new PropertyValueFactory<>("outKingdomJob"));
        outOftheForce_col.setCellValueFactory(new PropertyValueFactory<>("outOftheForce"));
        alternates_col.setCellValueFactory(new PropertyValueFactory<>("alternates"));
        administrativeleave_col.setCellValueFactory(new PropertyValueFactory<>("administrativeleave"));
        late_col.setCellValueFactory(new PropertyValueFactory<>("late"));
        Absence_col.setCellValueFactory(new PropertyValueFactory<>("Absence"));
        Prison_col.setCellValueFactory(new PropertyValueFactory<>("Prison"));

        censusTable.setItems(censusList);
    }

    private void refreshcensusTableView() {
        censusList.clear();
        censusTableView();
    }

    public void getTableRow(TableView table) {
        table.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                ObservableList<CensusModel> list = FXCollections.observableArrayList();
                list = table.getSelectionModel().getSelectedItems();
                if (!list.isEmpty()) {
                    try {
                        ResultSet rs = DatabaseAccess.select("census", "dayDate = '" + AppDate.getDate(DateDay, DateMonth, DateYear) + "'AND uint = '" + list.get(0).getUint() + "'AND type = 'OF'");
                        if (rs.next()) {
                            originalCensusOF.setText(rs.getString("originalCensus"));
                            currentCensusOF.setText(rs.getString("currentCensus"));
                            OrdinaryVacationOF.setText(rs.getString("OrdinaryVacation"));
                            OccasionalVacationOF.setText(rs.getString("OccasionalVacation"));
                            SickleaveOF.setText(rs.getString("Sickleave"));
                            QuarantineOF.setText(rs.getString("Quarantine"));
                            InareaTrainingOF.setText(rs.getString("InareaTraining"));
                            OutareaTrainingOF.setText(rs.getString("OutareaTraining"));
                            OutKingdomTrainingOF.setText(rs.getString("OutKingdomTraining"));
                            OfficialMissionOF.setText(rs.getString("OfficialMission"));
                            JoblMissionOF.setText(rs.getString("JoblMission"));
                            hospitalOF.setText(rs.getString("hospital"));
                            outKingdomJobOF.setText(rs.getString("outKingdomJob"));
                            outOftheForceOF.setText(rs.getString("outOftheForce"));
                            alternatesOF.setText(rs.getString("alternates"));
                            administrativeleaveOF.setText(rs.getString("administrativeleave"));
                            lateOF.setText(rs.getString("late"));
                            AbsenceOF.setText(rs.getString("Absence"));
                            PrisonOF.setText(rs.getString("Prison"));
                            tabeluint = list.get(0).getUint();
                        }
                        ResultSet rs1 = DatabaseAccess.select("census", "dayDate = '" + AppDate.getDate(DateDay, DateMonth, DateYear) + "'AND uint = '" + list.get(0).getUint() + "'AND type = 'SR'");
                        if (rs1.next()) {
                            originalCensusSR.setText(rs1.getString("originalCensus"));
                            currentCensusSR.setText(rs1.getString("currentCensus"));
                            OrdinaryVacationSR.setText(rs1.getString("OrdinaryVacation"));
                            OccasionalVacationSR.setText(rs1.getString("OccasionalVacation"));
                            SickleaveSR.setText(rs1.getString("Sickleave"));
                            QuarantineSR.setText(rs1.getString("Quarantine"));
                            InareaTrainingSR.setText(rs1.getString("InareaTraining"));
                            OutareaTrainingSR.setText(rs1.getString("OutareaTraining"));
                            OutKingdomTrainingSR.setText(rs1.getString("OutKingdomTraining"));
                            OfficialMissionSR.setText(rs1.getString("OfficialMission"));
                            JobMissionSR.setText(rs1.getString("JoblMission"));
                            hospitalSR.setText(rs1.getString("hospital"));
                            outKingdomJobSR.setText(rs1.getString("outKingdomJob"));
                            outOftheForceSR.setText(rs1.getString("outOftheForce"));
                            alternatesSR.setText(rs1.getString("alternates"));
                            administrativeleaveSR.setText(rs1.getString("administrativeleave"));
                            lateSR.setText(rs1.getString("late"));
                            AbsenceSR.setText(rs1.getString("Absence"));
                            PrisonSR.setText(rs1.getString("Prison"));
                            tabeluint = list.get(0).getUint();
                        }
                        rs.close();
                        rs1.close();
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
                ObservableList<CensusModel> list = FXCollections.observableArrayList();
                list = table.getSelectionModel().getSelectedItems();
                if (!list.isEmpty()) {
                    try {
                        ResultSet rs = DatabaseAccess.select("census", "dayDate = '" + AppDate.getDate(DateDay, DateMonth, DateYear) + "'AND uint = '" + list.get(0).getUint() + "'AND type = 'OF'");
                        if (rs.next()) {
                            originalCensusOF.setText(rs.getString("originalCensus"));
                            currentCensusOF.setText(rs.getString("currentCensus"));
                            OrdinaryVacationOF.setText(rs.getString("OrdinaryVacation"));
                            OccasionalVacationOF.setText(rs.getString("OccasionalVacation"));
                            SickleaveOF.setText(rs.getString("Sickleave"));
                            QuarantineOF.setText(rs.getString("Quarantine"));
                            InareaTrainingOF.setText(rs.getString("InareaTraining"));
                            OutareaTrainingOF.setText(rs.getString("OutareaTraining"));
                            OutKingdomTrainingOF.setText(rs.getString("OutKingdomTraining"));
                            OfficialMissionOF.setText(rs.getString("OfficialMission"));
                            JoblMissionOF.setText(rs.getString("JoblMission"));
                            hospitalOF.setText(rs.getString("hospital"));
                            outKingdomJobOF.setText(rs.getString("outKingdomJobOF"));
                            outOftheForceOF.setText(rs.getString("outOftheForce"));
                            alternatesOF.setText(rs.getString("alternatesOF"));
                            administrativeleaveOF.setText(rs.getString("administrativeleave"));
                            lateOF.setText(rs.getString("late"));
                            AbsenceOF.setText(rs.getString("Absence"));
                            PrisonOF.setText(rs.getString("Prison"));
                        }
                        ResultSet rs1 = DatabaseAccess.select("census", "dayDate = '" + AppDate.getDate(DateDay, DateMonth, DateYear) + "'AND uint = '" + list.get(0).getUint() + "'AND type = 'SR'");
                        if (rs.next()) {
                            originalCensusSR.setText(rs1.getString("originalCensus"));
                            currentCensusSR.setText(rs1.getString("currentCensus"));
                            OrdinaryVacationSR.setText(rs1.getString("OrdinaryVacation"));
                            OccasionalVacationSR.setText(rs1.getString("OccasionalVacation"));
                            SickleaveSR.setText(rs1.getString("Sickleave"));
                            QuarantineOF.setText(rs1.getString("Quarantine"));
                            InareaTrainingSR.setText(rs1.getString("InareaTraining"));
                            OutareaTrainingSR.setText(rs1.getString("OutareaTraining"));
                            OutKingdomTrainingSR.setText(rs1.getString("OutKingdomTraining"));
                            OfficialMissionSR.setText(rs1.getString("OfficialMission"));
                            JobMissionSR.setText(rs1.getString("JoblMission"));
                            hospitalSR.setText(rs1.getString("hospital"));
                            outKingdomJobSR.setText(rs1.getString("outKingdomJobOF"));
                            outOftheForceSR.setText(rs1.getString("outOftheForce"));
                            alternatesSR.setText(rs.getString("alternatesOF"));
                            administrativeleaveSR.setText(rs1.getString("administrativeleave"));
                            lateSR.setText(rs1.getString("late"));
                            AbsenceSR.setText(rs1.getString("Absence"));
                            PrisonSR.setText(rs1.getString("Prison"));
                        }
                        rs.close();
                        rs1.close();
                    } catch (SQLException | IOException ex) {
                        FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
                    }
                }
            }
        });
    }

    @FXML
    private void printOprationReport(ActionEvent event) {
        try {
            String reportSrcFile = "C:\\Users\\ابو ريان\\Documents\\NetBeansProjects\\Arshef\\src\\reports\\reportofopration.jrxml";
//            String reportSrcFile = "C:\\Users\\y50\\Documents\\NetBeansProjects\\Arshef\\src\\reports\\reportofopration.jrxml";
            Connection con = DatabaseConniction.dbConnector();
            ResultSet rs = DatabaseAccess.getSum("census", "dayDate = '" + AppDate.getDate(DateDay, DateMonth, DateYear) + "'AND type = 'OF'");
            ResultSet rs1 = DatabaseAccess.getSum("census", "dayDate = '" + AppDate.getDate(DateDay, DateMonth, DateYear) + "'AND type = 'SR'");
            ResultSet rs2 = DatabaseAccess.getSum("census", "dayDate = '" + AppDate.getDate(DateDay, DateMonth, DateYear) + "'");
            JasperDesign jasperReport = JRXmlLoader.load(reportSrcFile);
            Map parameters = new HashMap();
            while (rs.next() && rs1.next() && rs2.next()) {

                parameters.put("originalCensusOF", ArabicSetting.EnglishToarabic(rs.getString("sum(originalCensus)")));
                parameters.put("currentCensusOF", ArabicSetting.EnglishToarabic(rs.getString("sum(currentCensus)")));
                parameters.put("OrdinaryVacationOF", ArabicSetting.EnglishToarabic(rs.getString("sum(OrdinaryVacation)")));
                parameters.put("OccasionalVacationOF", ArabicSetting.EnglishToarabic(rs.getString("sum(OccasionalVacation)")));
                parameters.put("SickleaveOF", ArabicSetting.EnglishToarabic(rs.getString("sum(Sickleave)")));
                parameters.put("QuarantineOF", ArabicSetting.EnglishToarabic(rs.getString("sum(Quarantine)")));
                parameters.put("InareaTrainingOF", ArabicSetting.EnglishToarabic(rs.getString("sum(InareaTraining)")));
                parameters.put("OutareaTrainingOF", ArabicSetting.EnglishToarabic(rs.getString("sum(OutareaTraining)")));
                parameters.put("OutKingdomTrainingOF", ArabicSetting.EnglishToarabic(rs.getString("sum(OutKingdomTraining)")));
                parameters.put("OfficialMissionOF", ArabicSetting.EnglishToarabic(rs.getString("sum(OfficialMission)")));
                parameters.put("JoblMissionOF", ArabicSetting.EnglishToarabic(rs.getString("sum(JoblMission)")));
                parameters.put("hospitalOF", ArabicSetting.EnglishToarabic(rs.getString("sum(hospital)")));
                parameters.put("outKingdomJobOF", ArabicSetting.EnglishToarabic(rs.getString("sum(outKingdomJob)")));
                parameters.put("outOftheForceOF", ArabicSetting.EnglishToarabic(rs.getString("sum(outOftheForce)")));
                parameters.put("alternatesOF", ArabicSetting.EnglishToarabic(rs.getString("sum(alternates)")));
                parameters.put("administrativeleaveOF", ArabicSetting.EnglishToarabic(rs.getString("sum(administrativeleave)")));
                parameters.put("lateOF", ArabicSetting.EnglishToarabic(rs.getString("sum(late)")));
                parameters.put("AbsenceOF", ArabicSetting.EnglishToarabic(rs.getString("sum(Absence)")));
                parameters.put("PrisonOF", ArabicSetting.EnglishToarabic(rs.getString("sum(Prison)")));

                parameters.put("originalCensusSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(originalCensus)")));
                parameters.put("currentCensusSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(currentCensus)")));
                parameters.put("OrdinaryVacationSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(OrdinaryVacation)")));
                parameters.put("OccasionalVacationSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(OccasionalVacation)")));
                parameters.put("SickleaveSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(Sickleave)")));
                parameters.put("QuarantineSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(Quarantine)")));
                parameters.put("InareaTrainingSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(InareaTraining)")));
                parameters.put("OutareaTrainingSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(OutareaTraining)")));
                parameters.put("OutKingdomTrainingSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(OutKingdomTraining)")));
                parameters.put("OfficialMissionSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(OfficialMission)")));
                parameters.put("JoblMissionSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(JoblMission)")));
                parameters.put("hospitalSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(hospital)")));
                parameters.put("outKingdomJobSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(outKingdomJob)")));
                parameters.put("outOftheForceSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(outOftheForce)")));
                parameters.put("alternatesSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(alternates)")));
                parameters.put("administrativeleaveSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(administrativeleave)")));
                parameters.put("lateSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(late)")));
                parameters.put("AbsenceSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(Absence)")));
                parameters.put("PrisonSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(Prison)")));

                parameters.put("originalCensus", ArabicSetting.EnglishToarabic(rs2.getString("sum(originalCensus)")));
                parameters.put("currentCensus", ArabicSetting.EnglishToarabic(rs2.getString("sum(currentCensus)")));
                parameters.put("OrdinaryVacation", ArabicSetting.EnglishToarabic(rs2.getString("sum(OrdinaryVacation)")));
                parameters.put("OccasionalVacation", ArabicSetting.EnglishToarabic(rs2.getString("sum(OccasionalVacation)")));
                parameters.put("Sickleave", ArabicSetting.EnglishToarabic(rs2.getString("sum(Sickleave)")));
                parameters.put("Quarantine", ArabicSetting.EnglishToarabic(rs2.getString("sum(Quarantine)")));
                parameters.put("InareaTraining", ArabicSetting.EnglishToarabic(rs2.getString("sum(InareaTraining)")));
                parameters.put("OutareaTraining", ArabicSetting.EnglishToarabic(rs2.getString("sum(OutareaTraining)")));
                parameters.put("OutKingdomTraining", ArabicSetting.EnglishToarabic(rs2.getString("sum(OutKingdomTraining)")));
                parameters.put("OfficialMission", ArabicSetting.EnglishToarabic(rs2.getString("sum(OfficialMission)")));
                parameters.put("JoblMission", ArabicSetting.EnglishToarabic(rs2.getString("sum(JoblMission)")));
                parameters.put("hospital", ArabicSetting.EnglishToarabic(rs2.getString("sum(hospital)")));
                parameters.put("outKingdomJob", ArabicSetting.EnglishToarabic(rs2.getString("sum(outKingdomJob)")));
                parameters.put("outOftheForce", ArabicSetting.EnglishToarabic(rs2.getString("sum(outOftheForce)")));
                parameters.put("alternates", ArabicSetting.EnglishToarabic(rs2.getString("sum(alternates)")));
                parameters.put("administrativeleave", ArabicSetting.EnglishToarabic(rs2.getString("sum(administrativeleave)")));
                parameters.put("late", ArabicSetting.EnglishToarabic(rs2.getString("sum(late)")));
                parameters.put("Absence", ArabicSetting.EnglishToarabic(rs2.getString("sum(Absence)")));
                parameters.put("Prison", ArabicSetting.EnglishToarabic(rs2.getString("sum(Prison)")));
                parameters.put("day", HijriCalendar.getSimpleWeekday());
                parameters.put("date", ArabicSetting.EnglishToarabic(HijriCalendar.getSimpleDate()) + "هـ");
                parameters.put("uintNum", ArabicSetting.EnglishToarabic("067"));

            }
            JasperReport jrr = JasperCompileManager.compileReport(jasperReport);
            JasperPrint print = JasperFillManager.fillReport(jrr, parameters, con);

//        JasperPrintManager.printReport(print, false);
            JasperViewer.viewReport(print, false);
            rs.close();
        } catch (JRException | IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML//interyReport
    private void printForceReport(ActionEvent event) {
        try {
            String reportSrcFile = "C:\\Users\\ابو ريان\\Documents\\NetBeansProjects\\Arshef\\src\\reports\\ForceReport.jrxml";
//            String reportSrcFile = "C:\\Users\\y50\\Documents\\NetBeansProjects\\Arshef\\src\\reports\\ForceReport.jrxml";
            Connection con = DatabaseConniction.dbConnector();
            ResultSet rs = DatabaseAccess.getSum("census", "dayDate = '" + AppDate.getDate(DateDay, DateMonth, DateYear) + "'AND type = 'OF'");
            ResultSet rs1 = DatabaseAccess.getSum("census", "dayDate = '" + AppDate.getDate(DateDay, DateMonth, DateYear) + "'AND type = 'SR'");
            ResultSet rs2 = DatabaseAccess.getSum("census", "dayDate = '" + AppDate.getDate(DateDay, DateMonth, DateYear) + "'");
            JasperDesign jasperReport = JRXmlLoader.load(reportSrcFile);
            Map parameters = new HashMap();
            while (rs.next() && rs1.next() && rs2.next()) {

                parameters.put("originalCensusOF", ArabicSetting.EnglishToarabic(rs.getString("sum(originalCensus)")));
                parameters.put("currentCensusOF", ArabicSetting.EnglishToarabic(rs.getString("sum(currentCensus)")));
                parameters.put("OrdinaryVacationOF", ArabicSetting.EnglishToarabic(rs.getString("sum(OrdinaryVacation)")));
                parameters.put("OccasionalVacationOF", ArabicSetting.EnglishToarabic(rs.getString("sum(OccasionalVacation)")));
                parameters.put("SickleaveOF", ArabicSetting.EnglishToarabic(rs.getString("sum(Sickleave)")));
                parameters.put("QuarantineOF", ArabicSetting.EnglishToarabic(rs.getString("sum(Quarantine)")));
                parameters.put("InareaTrainingOF", ArabicSetting.EnglishToarabic(rs.getString("sum(InareaTraining)")));
                parameters.put("OutareaTrainingOF", ArabicSetting.EnglishToarabic(rs.getString("sum(OutareaTraining)")));
                parameters.put("OutKingdomTrainingOF", ArabicSetting.EnglishToarabic(rs.getString("sum(OutKingdomTraining)")));
                parameters.put("OfficialMissionOF", ArabicSetting.EnglishToarabic(rs.getString("sum(OfficialMission)")));
                parameters.put("JoblMissionOF", ArabicSetting.EnglishToarabic(rs.getString("sum(JoblMission)")));
                parameters.put("hospitalOF", ArabicSetting.EnglishToarabic(rs.getString("sum(hospital)")));
                parameters.put("outKingdomJobOF", ArabicSetting.EnglishToarabic(rs.getString("sum(outKingdomJob)")));
                parameters.put("outOftheForceOF", ArabicSetting.EnglishToarabic(rs.getString("sum(outOftheForce)")));
                parameters.put("alternatesOF", ArabicSetting.EnglishToarabic(rs.getString("sum(alternates)")));
                parameters.put("administrativeleaveOF", ArabicSetting.EnglishToarabic(rs.getString("sum(administrativeleave)")));
                parameters.put("lateOF", ArabicSetting.EnglishToarabic(rs.getString("sum(late)")));
                parameters.put("AbsenceOF", ArabicSetting.EnglishToarabic(rs.getString("sum(Absence)")));
                parameters.put("PrisonOF", ArabicSetting.EnglishToarabic(rs.getString("sum(Prison)")));

                parameters.put("originalCensusSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(originalCensus)")));
                parameters.put("currentCensusSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(currentCensus)")));
                parameters.put("OrdinaryVacationSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(OrdinaryVacation)")));
                parameters.put("OccasionalVacationSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(OccasionalVacation)")));
                parameters.put("SickleaveSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(Sickleave)")));
                parameters.put("QuarantineSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(Quarantine)")));
                parameters.put("InareaTrainingSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(InareaTraining)")));
                parameters.put("OutareaTrainingSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(OutareaTraining)")));
                parameters.put("OutKingdomTrainingSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(OutKingdomTraining)")));
                parameters.put("OfficialMissionSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(OfficialMission)")));
                parameters.put("JoblMissionSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(JoblMission)")));
                parameters.put("hospitalSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(hospital)")));
                parameters.put("outKingdomJobSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(outKingdomJob)")));
                parameters.put("outOftheForceSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(outOftheForce)")));
                parameters.put("alternatesSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(alternates)")));
                parameters.put("administrativeleaveSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(administrativeleave)")));
                parameters.put("lateSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(late)")));
                parameters.put("AbsenceSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(Absence)")));
                parameters.put("PrisonSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(Prison)")));

                parameters.put("originalCensus", ArabicSetting.EnglishToarabic(rs2.getString("sum(originalCensus)")));
                parameters.put("currentCensus", ArabicSetting.EnglishToarabic(rs2.getString("sum(currentCensus)")));
                parameters.put("OrdinaryVacation", ArabicSetting.EnglishToarabic(rs2.getString("sum(OrdinaryVacation)")));
                parameters.put("OccasionalVacation", ArabicSetting.EnglishToarabic(rs2.getString("sum(OccasionalVacation)")));
                parameters.put("Sickleave", ArabicSetting.EnglishToarabic(rs2.getString("sum(Sickleave)")));
                parameters.put("Quarantine", ArabicSetting.EnglishToarabic(rs2.getString("sum(Quarantine)")));
                parameters.put("InareaTraining", ArabicSetting.EnglishToarabic(rs2.getString("sum(InareaTraining)")));
                parameters.put("OutareaTraining", ArabicSetting.EnglishToarabic(rs2.getString("sum(OutareaTraining)")));
                parameters.put("OutKingdomTraining", ArabicSetting.EnglishToarabic(rs2.getString("sum(OutKingdomTraining)")));
                parameters.put("OfficialMission", ArabicSetting.EnglishToarabic(rs2.getString("sum(OfficialMission)")));
                parameters.put("JoblMission", ArabicSetting.EnglishToarabic(rs2.getString("sum(JoblMission)")));
                parameters.put("hospital", ArabicSetting.EnglishToarabic(rs2.getString("sum(hospital)")));
                parameters.put("outKingdomJob", ArabicSetting.EnglishToarabic(rs2.getString("sum(outKingdomJob)")));
                parameters.put("outOftheForce", ArabicSetting.EnglishToarabic(rs2.getString("sum(outOftheForce)")));
                parameters.put("alternates", ArabicSetting.EnglishToarabic(rs2.getString("sum(alternates)")));
                parameters.put("administrativeleave", ArabicSetting.EnglishToarabic(rs2.getString("sum(administrativeleave)")));
                parameters.put("late", ArabicSetting.EnglishToarabic(rs2.getString("sum(late)")));
                parameters.put("Absence", ArabicSetting.EnglishToarabic(rs2.getString("sum(Absence)")));
                parameters.put("Prison", ArabicSetting.EnglishToarabic(rs2.getString("sum(Prison)")));
                parameters.put("day", HijriCalendar.getSimpleWeekday());
                parameters.put("date", ArabicSetting.EnglishToarabic(HijriCalendar.getSimpleDate()) + "هـ");
                parameters.put("uintNum", ArabicSetting.EnglishToarabic("067"));
            }
            JasperReport jrr = JasperCompileManager.compileReport(jasperReport);
            JasperPrint print = JasperFillManager.fillReport(jrr, parameters, con);

//        JasperPrintManager.printReport(print, false);
            JasperViewer.viewReport(print, false);
            rs.close();
        } catch (JRException | IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void printManualReport(ActionEvent event) {
        try {
            String reportSrcFile = "C:\\Users\\ابو ريان\\Documents\\NetBeansProjects\\Arshef\\src\\reports\\ManualReport.jrxml";
//            String reportSrcFile = "C:\\Users\\y50\\Documents\\NetBeansProjects\\Arshef\\src\\reports\\ManualReport.jrxml";
            Connection con = DatabaseConniction.dbConnector();
            ResultSet rs = DatabaseAccess.getManualSum("census", "dayDate = '" + AppDate.getDate(DateDay, DateMonth, DateYear) + "'AND type = 'OF'");
            ResultSet rs1 = DatabaseAccess.getManualSum("census", "dayDate = '" + AppDate.getDate(DateDay, DateMonth, DateYear) + "'AND type = 'SR'");
            JasperDesign jasperReport = JRXmlLoader.load(reportSrcFile);
            Map parameters = new HashMap();
            while (rs.next() && rs1.next()) {
                int intoriginalCensusOf = rs.getInt("sum(originalCensus)");
                int intcurrentCensusOF = rs.getInt("sum(currentCensus)");
                int totalOF = intoriginalCensusOf - intcurrentCensusOF;
                String outfieldOF = Integer.toString(totalOF);
                int intoriginalCensusSR = rs1.getInt("sum(originalCensus)");
                int intcurrentCensusSR = rs1.getInt("sum(currentCensus)");
                int totalSR = intoriginalCensusSR - intcurrentCensusSR;
                String outfieldSR = Integer.toString(totalSR);
                parameters.put("originalCensusOF", ArabicSetting.EnglishToarabic(rs.getString("sum(originalCensus)")));
                parameters.put("originalCensusSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(originalCensus)")));
                parameters.put("infieldOF", ArabicSetting.EnglishToarabic(rs.getString("sum(currentCensus)")));
                parameters.put("infieldSR", ArabicSetting.EnglishToarabic(rs1.getString("sum(currentCensus)")));
                parameters.put("outfieldOF", ArabicSetting.EnglishToarabic(outfieldOF));
                parameters.put("outfieldSR", ArabicSetting.EnglishToarabic(outfieldSR));
                parameters.put("day", HijriCalendar.getSimpleWeekday());
                parameters.put("date", ArabicSetting.EnglishToarabic(HijriCalendar.getSimpleDate()) + "هـ");
                parameters.put("quaridate", AppDate.getDate(DateDay, DateMonth, DateYear));
                parameters.put("uintNum", ArabicSetting.EnglishToarabic("067"));
            }
            JasperReport jrr = JasperCompileManager.compileReport(jasperReport);
            JasperPrint print = JasperFillManager.fillReport(jrr, parameters, con);

//        JasperPrintManager.printReport(print, false);
            JasperViewer.viewReport(print, false);
            rs.close();
            rs1.close();
        } catch (JRException | IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }
}
