package controllers;

import Serveces.FormationPageListener;
import Validation.FormValidation;
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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import modeles.FormationModel;

public class FormationPageController implements Initializable {

    @FXML
    private ComboBox<?> searchType;
    @FXML
    private ComboBox<?> searchDateDay;
    @FXML
    private ComboBox<?> searchDateMonth;
    @FXML
    private ComboBox<?> searchDateYear;
    @FXML
    private ComboBox<?> year;
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
    ObservableList<String> rankComboBoxlist = FXCollections.observableArrayList("فريق اول", "فريق", "لواء", "عميد", "عقيد", "مقدم", "رائد", "نقيب", "ملازم أول", "ملازم", "رئيس رقباء", "رقيب أول", "رقيب", "وكيل رقيب", "عريف", "جندي أول", "جندي");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        refreshData();
        uint.setItems(filleUint(uintlist));
        rank.setItems(rankComboBoxlist);
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

    private ObservableList filleRank(ObservableList list) {
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
    private void enableSearchDate(ActionEvent event) {
    }

    @FXML
    private void searchData(ActionEvent event) {
    }

    @FXML
    private void save(ActionEvent event) {
        String tableName = "personaldata";
        String fieldName = "`MILITARYID`,`PERSONALID`,`NAME`,`RANK`,`UNIT`,`NOTE`";
        String[] data = {militaryID.getText(), personalID.getText(), name.getText(), rank.getValue(), uint.getValue(), note.getText()};
        String valuenumbers = "?,?,?,?,?,?";

        boolean rankState = FormValidation.comboBoxNotEmpty(rank, "الرجاء اختيار الرتبة");
        boolean uintState = FormValidation.comboBoxNotEmpty(uint, "الرجاء اختيار الوحدة");
        boolean nameState = FormValidation.textFieldNotEmpty(name, "الرجاء ادخال الاسم");
        boolean personalIDState = FormValidation.textFieldNotEmpty(personalID, "الرجاء ادخال رقم السجل المدني");
        boolean personalIDNumber = FormValidation.textFieldTypeNumber(personalID, "الرجاء ادخال رقم السجل المدني");
        boolean militaryIDState = FormValidation.textFieldNotEmpty(militaryID, "الرجاء ادخال الرقم العسكري");
        boolean militaryIDNumber = FormValidation.textFieldTypeNumber(militaryID, "الرجاء ادخال الرقم العسكري");
        boolean militaryIDExisting = FormValidation.ifexisting("personaldata", "MILITARYID", "MILITARYID = '" + militaryID.getText() + "'", "الرقم العسكري موجود مسبقا");
        boolean personalIDExisting = FormValidation.ifexisting("personaldata", "PERSONALID", "PERSONALID = '" + personalID.getText() + "'", "رقم السجل موجود مسبقا");
        if (militaryIDExisting && personalIDExisting && rankState && uintState && nameState && personalIDState && personalIDNumber && militaryIDState && militaryIDNumber) {
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
        String tableName = "personaldata";
        String fieldName = "`MILITARYID`=?,`PERSONALID`=?,`NAME`=?,`RANK`=?,`UNIT`=?,`NOTE`=?";
        String[] data = {militaryID.getText(), personalID.getText(), name.getText(), rank.getValue(), uint.getValue(), note.getText()};

        boolean rankState = FormValidation.comboBoxNotEmpty(rank, "الرجاء اختيار الرتبة");
        boolean uintState = FormValidation.comboBoxNotEmpty(uint, "الرجاء اختيار الوحدة");
        boolean nameState = FormValidation.textFieldNotEmpty(name, "الرجاء ادخال الاسم");
        boolean personalIDState = FormValidation.textFieldNotEmpty(personalID, "الرجاء ادخال رقم السجل المدني");
        boolean personalIDNumber = FormValidation.textFieldTypeNumber(personalID, "الرجاء ادخال رقم السجل المدني");
        boolean militaryIDState = FormValidation.textFieldNotEmpty(militaryID, "الرجاء ادخال الرقم العسكري");
        boolean militaryIDNumber = FormValidation.textFieldTypeNumber(militaryID, "الرجاء ادخال الرقم العسكري");

        if (rankState && uintState && nameState && personalIDState && personalIDNumber && militaryIDState && militaryIDNumber) {
            try {
                DatabaseAccess.updat(tableName, fieldName, data, "MILITARYID = '" + militaryID.getText() + "'");
                refreshData();
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void delete(ActionEvent event) {
        try {
            DatabaseAccess.delete("personaldata", "MILITARYID = '" + militaryID.getText() + "'");
        } catch (IOException ex) {
            Logger.getLogger(FormationPageController.class.getName()).log(Level.SEVERE, null, ex);
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
    }

    private void refreshData() {
        try {
            FormationObject.clear();
            vbox.getChildren().clear();
            viewdata(DatabaseAccess.getData("SELECT MILITARYID , PERSONALID , NAME , RANK , UNIT , NOTE FROM personaldata "));//ORDER BY MILITARYID ASC
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    private void viewdata(ResultSet data) {
        FormationObject.addAll(getData(data));
        if (FormationObject.size() > 0) {
            setChosendata(FormationObject.get(0));
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
    }

}
