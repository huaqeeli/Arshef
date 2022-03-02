package controllers;

import Validation.FormValidation;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modeles.OriginalCensusModel;

public class addOriginalCensusController implements Initializable, InitializClass {

    @FXML
    private VBox content;
    @FXML
    private ComboBox<String> uint;
    @FXML
    private TextField OFcensus;
    @FXML
    private TextField SRcensus;
    @FXML
    private TableView<OriginalCensusModel> originalCensusTable;
    @FXML
    private TableColumn<?, ?> uint_col;
    @FXML
    private TableColumn<?, ?> OFcensus_col;
    @FXML
    private TableColumn<?, ?> SRcensus_col;
    ObservableList<String> uintComboBoxlist = FXCollections.observableArrayList();
    ObservableList<OriginalCensusModel> originalcensusList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        uint.setItems(filleCoursPlace(uintComboBoxlist));
        refreshTableView();
        getTableRow(originalCensusTable);
        getTableRowByInterKey(originalCensusTable);
    }

    private ObservableList filleCoursPlace(ObservableList list) {
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

    @Override
    public void close(ActionEvent event) {
        Stage stage = (Stage) content.getScene().getWindow();
        stage.close();
    }

    @Override
    public void save(ActionEvent event) {
        String tableName = "originalcensus";
        String[] data = {uint.getValue(), OFcensus.getText(), SRcensus.getText()};
        String fieldName = "`UINT`,`OFCENSUS`,`SRCEMSUS`";
        String valuenumbers = "?,?,?";

        boolean uintState = FormValidation.comboBoxNotEmpty(uint, "الرجاء اختر الوحدةة");
        boolean uintExusting = FormValidation.ifexisting("originalcensus", "UINT", "UINT = '" + uint.getValue() + "'", "تم حفظ تعداد "+uint.getValue()+" "+" يمكن تحديثه من الجدول ادناه");
        boolean OFcensusState = FormValidation.textFieldNotEmpty(OFcensus, "الرجاء ادخال تعداد الضباط");
        boolean SRcensusState = FormValidation.textFieldNotEmpty(SRcensus, "الرجاء ادخال تعداد الافراد");

        if (uintState && uintExusting && OFcensusState && SRcensusState) {
            try {
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, data);
                refreshTableView();
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @Override
    public void edit(ActionEvent event) {
        String tableName = "originalcensus";
        String[] data = {uint.getValue(), OFcensus.getText(), SRcensus.getText()};
        String fieldName = "`UINT`=?,`OFCENSUS`=?,`SRCEMSUS`=?";

        boolean uintState = FormValidation.comboBoxNotEmpty(uint, "الرجاء اختر الوحدةة");
        boolean OFcensusState = FormValidation.textFieldNotEmpty(OFcensus, "الرجاء ادخال تعداد الضباط");
        boolean SRcensusState = FormValidation.textFieldNotEmpty(SRcensus, "الرجاء ادخال تعداد الافراد");

        if (uintState && OFcensusState && SRcensusState) {
            try {
                int t = DatabaseAccess.updat(tableName, fieldName, data, "UINT = '" + uint.getValue() + "'");
                if (t > 0) {
                    FormValidation.showAlert("", "تم تحديث البيانات", Alert.AlertType.CONFIRMATION);
                }
                refreshTableView();
                clear(event);
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    @Override
    public void delete(ActionEvent event) {
        try {
            DatabaseAccess.delete("originalcensus", "UINT = '" + uint.getValue() + "'");
            refreshTableView();
            clear(event);
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @Override
    public void getTableRow(TableView table) {
        table.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                ObservableList<OriginalCensusModel> list = FXCollections.observableArrayList();
                list = table.getSelectionModel().getSelectedItems();
                if (!list.isEmpty()) {
                    uint.setValue(list.get(0).getUint());
                    OFcensus.setText(list.get(0).getOFcensus());
                    SRcensus.setText(list.get(0).getSRcensus());
                }
            }
        });
    }

    @Override
    public void getTableRowByInterKey(TableView table) {
        table.setOnKeyPressed(new EventHandler() {
            @Override
            public void handle(Event event) {
                ObservableList<OriginalCensusModel> list = FXCollections.observableArrayList();
                list = table.getSelectionModel().getSelectedItems();
                if (!list.isEmpty()) {
                    uint.setValue(list.get(0).getUint());
                    OFcensus.setText(list.get(0).getOFcensus());
                    SRcensus.setText(list.get(0).getSRcensus());
                }
            }

        });
    }

    @Override
    public void tableView() {
        try {
            ResultSet rs = DatabaseAccess.select("originalcensus");
            while (rs.next()) {
                originalcensusList.add(new OriginalCensusModel(
                        rs.getString("UINT"),
                        rs.getString("OFCENSUS"),
                        rs.getString("SRCEMSUS")
                ));
            }
            rs.close();
        } catch (SQLException | IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        uint_col.setCellValueFactory(new PropertyValueFactory<>("uint"));
        OFcensus_col.setCellValueFactory(new PropertyValueFactory<>("OFcensus"));
        SRcensus_col.setCellValueFactory(new PropertyValueFactory<>("SRcensus"));

        originalCensusTable.setItems(originalcensusList);
    }

    @Override
    public void refreshTableView() {
        originalcensusList.clear();
        tableView();
    }

    @Override
    public void clear(ActionEvent event) {
       uint.setValue(null);
       OFcensus.setText(null);
       SRcensus.setText(null);
    }

}
