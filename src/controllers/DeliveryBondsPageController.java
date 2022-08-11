package controllers;

import Serveces.DeliveryBondsListener;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import modeles.DeliveryBondsModel;

public class DeliveryBondsPageController implements Initializable {

    @FXML
    private ComboBox<String> searchType;
    @FXML
    private ComboBox<?> searchDateDay;
    @FXML
    private ComboBox<?> searchDateMonth;
    @FXML
    private ComboBox<?> searchDateYear;
    @FXML
    private ComboBox<String> year;
    @FXML
    private TextField searchText;
    @FXML
    private StackPane stackPane;
    @FXML
    private VBox vbox;
    @FXML
    private ComboBox<?> dateDay;
    @FXML
    private ComboBox<?> dateMonth;
    @FXML
    private ComboBox<?> dateYear;
    ObservableList<String> searchTypelist = FXCollections.observableArrayList("البحث برقم المعاملة", "البحث بتاريخ السند");
    ObservableList<DeliveryBondsModel> bondList = FXCollections.observableArrayList();

    public final List<DeliveryBondsModel> bondObject = new ArrayList<>();
    private DeliveryBondsListener myListener;
    String bondId = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AppDate.setDateValue(dateDay, dateMonth, dateYear);
        AppDate.setCurrentDate(dateDay, dateMonth, dateYear);
        AppDate.setDateValue(searchDateDay, searchDateMonth, searchDateYear);
        AppDate.setCurrentDate(searchDateDay, searchDateMonth, searchDateYear);
        FillComboBox.fillComboBox(searchTypelist, searchType);
        AppDate.setYearValue(year);
        year.setValue(Integer.toString(HijriCalendar.getSimpleYear()));
        refreshdata();
    }

    @FXML
    private void enableSearchDate(ActionEvent event) {
        if ("البحث بتاريخ السند".equals(searchType.getValue())) {
            searchDateDay.setDisable(false);
            searchDateMonth.setDisable(false);
            searchDateYear.setDisable(false);
            year.setDisable(true);
        } else {
            searchDateDay.setDisable(true);
            searchDateMonth.setDisable(true);
            searchDateYear.setDisable(true);
            year.setDisable(false);
        }
    }

   
    @FXML
    private void searchData(ActionEvent event) throws IOException {
        String searchValue = searchType.getValue();
        switch (searchValue) {
            case "البحث برقم المعاملة":
                bondObject.clear();
                vbox.getChildren().clear();
                viewdata(DatabaseAccess.getData("SELECT DISTINCT bonduint.BONDID,deliverybonds.BONDDATE FROM deliverybonds,bonduint where CIRCULARNUMBER ='" + searchText.getText() + "' AND deliverybonds.BONDID = bonduint.BONDID ORDER BY BONDID DESC"));
                break;
            case "البحث بتاريخ السند":
                bondObject.clear();
                vbox.getChildren().clear();
                viewdata(DatabaseAccess.getData("SELECT BONDID,BONDDATE FROM deliverybonds where BONDDATE ='" + getSearchDate() + "' ORDER BY BONDID DESC"));
        }
    }

    @FXML
    private void creatBond(ActionEvent event) {
        String tableName = "deliverybonds";
        String recordYear = Integer.toString(HijriCalendar.getSimpleYear());
        String fieldName = "`BONDDATE`,`YEAR`";
        String[] data = {getBondDate(), recordYear};
        String valuenumbers = "?,?";
        try {
            DatabaseAccess.insert(tableName, fieldName, valuenumbers, data);
            refreshdata();
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }

    }

    @FXML
    private void delete(ActionEvent event) {
         try {
           int t = DatabaseAccess.delete("deliverybonds", "BONDID = '"+bondId+"'");
             if (t > 0) {
                  DatabaseAccess.delete("bonduint", "BONDID = '"+bondId+"'");
             }
             refreshdata();
        } catch (IOException | SQLException ex) {
           FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    public String getBondDate() {
        return AppDate.getDate(dateDay, dateMonth, dateYear);
    }

    public String getSearchDate() {
        return AppDate.getDate(searchDateDay, searchDateMonth, searchDateYear);
    }

    private void refreshdata() {
        try {
            bondObject.clear();
            vbox.getChildren().clear();
            viewdata(DatabaseAccess.getData("SELECT BONDID,BONDDATE FROM deliverybonds where BONDDATE ='" + HijriCalendar.getSimpleDate() + "' ORDER BY BONDID DESC"));
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }
    private void setChosendata(DeliveryBondsModel deliveryBondsModel) {
        bondId = deliveryBondsModel.getBondId();
    }
    private void viewdata(ResultSet rs) {
        bondObject.addAll(getData(rs));
        if (bondObject.size() > 0) {
            myListener = new DeliveryBondsListener() {
                @Override
                public void onClickListener(DeliveryBondsModel deliveryBondsModel) {
                    setChosendata(deliveryBondsModel);
                }
            };
        }

        try {
            for (DeliveryBondsModel deliveryBondsModel : bondObject) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/view/deliveryBondsPageItem.fxml"));
                AnchorPane pane = fxmlLoader.load();
                DeliveryBondsPageItemController deliveryBondsPageItemController = fxmlLoader.getController();
                deliveryBondsPageItemController.setData(deliveryBondsModel, myListener);
                vbox.getChildren().add(pane);
            }
            rs.close();
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    private List<DeliveryBondsModel> getData(ResultSet rs) {
        List<DeliveryBondsModel> deliveryBondslist = new ArrayList<>();
        DeliveryBondsModel deliveryBondsModel;
        try {
            while (rs.next()) {
                deliveryBondsModel = new DeliveryBondsModel();
                deliveryBondsModel.setBondId(rs.getString("BONDID"));
                deliveryBondsModel.setBondDate(rs.getString("BONDDATE"));
                deliveryBondslist.add(deliveryBondsModel);
            }
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return deliveryBondslist;
    }

}
