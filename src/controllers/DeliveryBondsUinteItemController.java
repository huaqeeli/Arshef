package controllers;

import Serveces.DeliveryBondsListener;
import Validation.FormValidation;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import modeles.DeliveryBondsModel;

public class DeliveryBondsUinteItemController implements Initializable {

    
    @FXML
    private Label squnce;
    @FXML
    private Label circularNumber;
    @FXML
    private Label uint;
    
    DeliveryBondsModel deliveryBondsModel;
    DeliveryBondsListener myListener;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setData(DeliveryBondsModel deliveryBondsModel, DeliveryBondsListener myListener) {
        this.deliveryBondsModel = deliveryBondsModel;
        this.myListener = myListener;
        circularNumber.setText(deliveryBondsModel.getCircularNumber());
        uint.setText(deliveryBondsModel.getUintt());
        squnce.setText(Integer.toString(deliveryBondsModel.getSqunce()));
    }

    @FXML
    private void delete(ActionEvent event) {
        try {
            DatabaseAccess.delete("bonduint", "BONDID = '"+deliveryBondsModel.getBondId()+"' AND CIRCULARNUMBER = '"+deliveryBondsModel.getCircularNumber()+"'AND ID ='"+deliveryBondsModel.getId()+"'");
        } catch (IOException | SQLException ex) {
           FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

}
