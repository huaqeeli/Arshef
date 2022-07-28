
package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;


public class vacationDetailsController implements Initializable {

    @FXML
    private HBox content;
    @FXML
    private Label day;
    @FXML
    private Label vacationPeriod;
    @FXML
    private Label vacationReason;
    @FXML
    private Label approvalOfficer;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }    

    @FXML
    private void cilck(MouseEvent event) {
    }
    
}
