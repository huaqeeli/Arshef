package controllers;

import Validation.FormValidation;
import java.io.File;
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
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import modeles.PersonalModel;

public class PersonalDataPageController implements Initializable {

    @FXML
    private TextField milataryid;
    @FXML
    private ComboBox<String> rank;
    @FXML
    private TextField name;
    @FXML
    private TextField personalid;
    @FXML
    private TextField unit;
    @FXML
    private TableView<PersonalModel> personaltable;
    @FXML
    private TableColumn<?, ?> milataryid_col;
    @FXML
    private TableColumn<?, ?> rank_col;
    @FXML
    private TableColumn<?, ?> name_col;
    @FXML
    private TableColumn<?, ?> personalid_col;
    @FXML
    private TableColumn<?, ?> unit_col;

    ObservableList<String> rankComboBoxlist = FXCollections.observableArrayList("فريق اول", "فريق", "لواء", "عميد", "عقيد", "مقدم", "رائد", "نقيب", "ملازم أول", "ملازم", "رئيس رقباء", "رقيب أول", "رقيب", "وكيل رقيب", "عريف", "جندي أول", "جندي");
    ObservableList<PersonalModel> personalList = FXCollections.observableArrayList();
    String selectedMilatryid = null;
    Window stage = null;
    File imagefile = null;
    @FXML
    private TextField imageUrl;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FillComboBox.fillComboBox(rankComboBoxlist, rank);
        refreshpersonaltableTableView();
        getTableRow(personaltable);
        getTableRowByInterKey(personaltable);
    }

    @FXML
    private void save(ActionEvent event) {
        String tableName = "personaldata";
        String fieldName = null;
        String[] data = {getMilataryid(), getName(), getRank(), getUnit(), getPersonalid()};
        String valuenumbers =null;
         if (imagefile != null) {
            fieldName ="`MILITARYID`,`NAME`,`RANK`,`UNIT`,`PERSONALID`,`PERSONALIMAGE`";
            valuenumbers =  "?,?,?,?,?,?";
        } else {
            fieldName = "`MILITARYID`,`NAME`,`RANK`,`UNIT`,`PERSONALID`";
            valuenumbers =  "?,?,?,?,?";
        }
        boolean milataryidState = FormValidation.textFieldNotEmpty(milataryid, "الرجاء ادخال الرقم العسكري");
        boolean milataryidExisting = FormValidation.ifexisting("personaldata", "MILITARYID", "MILITARYID='" + getMilataryid() + "'", "لا يمكن تكرار الرقم العسكري");
        boolean personalidExisting = FormValidation.ifexisting("personaldata", "PERSONALID", "PERSONALID='" + getPersonalid() + "'", "لا يمكن تكرار رقم الهوية ");
        boolean nameState = FormValidation.textFieldNotEmpty(name, "الرجاء ادخال الاسم");
        boolean unitState = FormValidation.textFieldNotEmpty(unit, "الرجاء ادخال الوحده");
        boolean rankState = FormValidation.comboBoxNotEmpty(rank, "الرجاء اختيار الرتبه");
        if (rankState && nameState && unitState && milataryidState && milataryidExisting && personalidExisting) {
            try {
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, data,imagefile);
                refreshpersonaltableTableView();
            } catch (IOException ex) {
                Logger.getLogger(PersonalDataPageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void edit(ActionEvent event) {
        String tableName = "personaldata";
        String fieldName = null;
        String[] data = {getMilataryid(), getName(), getRank(), getUnit(), getPersonalid()};
         if (imagefile != null) {
            fieldName ="`MILITARYID`=?,`NAME`=?,`RANK`=?,`UNIT`=?,`PERSONALID`=? ,`PERSONALIMAGE`=?";
        } else {
            fieldName = "`MILITARYID`=?,`NAME`=?,`RANK`=?,`UNIT`=?,`PERSONALID`=?";
        }
        boolean milataryidState = FormValidation.textFieldNotEmpty(milataryid, "الرجاء ادخال الرقم العسكري");
        boolean nameState = FormValidation.textFieldNotEmpty(name, "الرجاء ادخال الاسم");
        boolean unitState = FormValidation.textFieldNotEmpty(unit, "الرجاء ادخال الوحده");
        boolean rankState = FormValidation.comboBoxNotEmpty(rank, "الرجاء اختيار الرتبه");
        if (rankState && nameState && unitState && milataryidState) {
            try {
                DatabaseAccess.updat(tableName, fieldName, data, "MILITARYID = '" + selectedMilatryid + "'",imagefile);
                refreshpersonaltableTableView();
            } catch (IOException ex) {
                Logger.getLogger(PersonalDataPageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void delete(ActionEvent event) {
        try {
            DatabaseAccess.delete("personaldata", "MILITARYID = '" + selectedMilatryid + "'");
            refreshpersonaltableTableView();
        } catch (IOException ex) {
            Logger.getLogger(PersonalDataPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getMilataryid() {
        return milataryid.getText();
    }

    public String getRank() {
        return rank.getValue();
    }

    public String getName() {
        return name.getText();
    }

    public String getPersonalid() {
        return personalid.getText();
    }

    public String getUnit() {
        return unit.getText();
    }

    private void personaltableView() {
        try {
            ResultSet rs = DatabaseAccess.select("personaldata");
            while (rs.next()) {
                personalList.add(new PersonalModel(
                        rs.getString("MILITARYID"),
                        rs.getString("RANK"),
                        rs.getString("NAME"),
                        rs.getString("PERSONALID"),
                        rs.getString("UNIT")
                ));
            }
            rs.close();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(PersonalDataPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        milataryid_col.setCellValueFactory(new PropertyValueFactory<>("militaryId"));
        rank_col.setCellValueFactory(new PropertyValueFactory<>("rank"));
        name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        personalid_col.setCellValueFactory(new PropertyValueFactory<>("personalid"));
        unit_col.setCellValueFactory(new PropertyValueFactory<>("unit"));

        personaltable.setItems(personalList);
    }

    public void getTableRow(TableView table) {
        table.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                ObservableList<PersonalModel> list = FXCollections.observableArrayList();
                list = table.getSelectionModel().getSelectedItems();
                if (!list.isEmpty()) {
                    milataryid.setText(list.get(0).getMilitaryId());
                    rank.setValue(list.get(0).getRank());
                    name.setText(list.get(0).getName());
                    personalid.setText(list.get(0).getPersonalid());
                    unit.setText(list.get(0).getUnit());
                    selectedMilatryid = (list.get(0).getMilitaryId());
                }
            }
        });
    }

    private void getTableRowByInterKey(TableView table) {
        table.setOnKeyPressed(new EventHandler() {
            @Override
            public void handle(Event event) {
                ObservableList<PersonalModel> list = FXCollections.observableArrayList();
                list = table.getSelectionModel().getSelectedItems();
                if (!list.isEmpty()) {
                    milataryid.setText(list.get(0).getMilitaryId());
                    rank.setValue(list.get(0).getRank());
                    name.setText(list.get(0).getName());
                    personalid.setText(list.get(0).getPersonalid());
                    unit.setText(list.get(0).getUnit());
                    selectedMilatryid = (list.get(0).getMilitaryId());
                }
            }
        });
    }

    private void refreshpersonaltableTableView() {
        personalList.clear();
        personaltableView();
    }

    @FXML
    private void clearField(ActionEvent event) {
        milataryid.setText(null);
        rank.setValue(null);
        name.setText(null);
        personalid.setText(null);
        unit.setText(null);

    }

    @FXML
    private File getImageUrl(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter ext1 = new FileChooser.ExtensionFilter("JPG files(*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter ext2 = new FileChooser.ExtensionFilter("PNG files(*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(ext1, ext2);
        imagefile = fileChooser.showOpenDialog(stage);
        imageUrl.setText(imagefile.getPath());
        return imagefile;
    }

    @FXML
    private void getPersonalData(ActionEvent event) {
        try {
            ResultSet rs = DatabaseAccess.select("personaldata","MILITARYID='"+milataryid.getText()+"'");
            while (rs.next()) {
                    rank.setValue( rs.getString("RANK"));
                    name.setText( rs.getString("NAME"));
                    personalid.setText(rs.getString("PERSONALID"));
                    unit.setText(rs.getString("UNIT"));
            }
            rs.close();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(PersonalDataPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
