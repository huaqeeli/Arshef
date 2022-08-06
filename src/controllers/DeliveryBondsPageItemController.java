package controllers;

import Serveces.DeliveryBondsListener;
import Validation.FormValidation;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import modeles.DeliveryBondsModel;

public class DeliveryBondsPageItemController implements Initializable {
    
    @FXML
    private HBox content;
    @FXML
    private HBox state;
    @FXML
    private Label bondsNumber;
    @FXML
    private Label date;
    @FXML
    private TextField circularNumber;
    @FXML
    private ComboBox<String> destination;
    
    DeliveryBondsModel deliveryBondsModel;
    DeliveryBondsListener myListener;
    public final List<DeliveryBondsModel> bondObject = new ArrayList<>();
    ObservableList<String> destinationlist = FXCollections.observableArrayList();
    @FXML
    private VBox vBox;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        destination.setItems(filleDestination(destinationlist));
    }
    
    private ObservableList filleDestination(ObservableList list) {
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
    
    public void setData(DeliveryBondsModel deliveryBondsModel, DeliveryBondsListener myListener) {
        this.deliveryBondsModel = deliveryBondsModel;
        this.myListener = myListener;
        bondsNumber.setText(deliveryBondsModel.getBondId());
        date.setText(deliveryBondsModel.getBondDate());
    }
    
    @FXML
    private void showImage(ActionEvent event) {
        byte[] pdfimage = DatabaseAccess.getBondPdfFile(deliveryBondsModel.getBondId());
        ShowPdf.writePdf(pdfimage);
    }
    
    @FXML
    private void click(MouseEvent event) {
    }
    
    @FXML
    private void addUinte(ActionEvent event) {
        String tableName = "bonduint";
        String fieldName = "`BONDID`,`CIRCULARNUMBER`,`UINT`";
        String[] data = {deliveryBondsModel.getBondId(), circularNumber.getText(), destination.getValue()};
        String valuenumbers = "?,?,?";
        boolean circularidSNotexisting = FormValidation.ifexisting("bonduint", "`BONDID`,`CIRCULARNUMBER`,`UINT`", "BONDID = '" + deliveryBondsModel.getBondId() + "' AND CIRCULARNUMBER = '" +circularNumber.getText() + "' AND UINT ='" + destination.getValue() + "'", "تم ادخال المعاملة مسبقا");
        if (circularidSNotexisting) {
            try {
                DatabaseAccess.insert(tableName, fieldName, valuenumbers, data);
                refreshdata();
            } catch (IOException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
        
    }
    
    @FXML
    private void update(ActionEvent event) {
        refreshdata();
    }
    
    @FXML
    private void printeBond(ActionEvent event) {
    }
    
    @FXML
    private void scanImage(ActionEvent event) {
        try {
            DatabaseAccess.insertImage("deliverybonds", " `BONDID` ='" + deliveryBondsModel.getBondId() + "'");
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }
    
    public void refreshdata() {
        try {
            bondObject.clear();
            vBox.getChildren().clear();
            viewdata(DatabaseAccess.getData("SELECT ID, BONDID, CIRCULARNUMBER, UINT FROM bonduint where BONDID ='" + deliveryBondsModel.getBondId() + "'"));
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }
    
    private void viewdata(ResultSet rs) {
        bondObject.addAll(getData(rs));
        try {
            for (DeliveryBondsModel deliveryBondsModel : bondObject) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/view/deliveryBondsUinteItem.fxml"));
                AnchorPane pane = fxmlLoader.load();
                DeliveryBondsUinteItemController deliveryBondsUinteItemController = fxmlLoader.getController();
                deliveryBondsUinteItemController.setData(deliveryBondsModel, myListener);
                vBox.getChildren().add(pane);
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
            int squnce = 0;
            while (rs.next()) {
                squnce++;
                deliveryBondsModel = new DeliveryBondsModel();
                deliveryBondsModel.setBondId(rs.getString("BONDID"));
                deliveryBondsModel.setId(rs.getString("ID"));
                deliveryBondsModel.setCircularNumber(rs.getString("CIRCULARNUMBER"));
                deliveryBondsModel.setUintt(rs.getString("UINT"));
                deliveryBondsModel.setSqunce(squnce);
                deliveryBondslist.add(deliveryBondsModel);
            }
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return deliveryBondslist;
    }
    
    @FXML
    private void showUinte(ActionEvent event) {
        refreshdata();
    }
    
}
