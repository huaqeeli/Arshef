
package controllers;

import Serveces.DisplayPageListener;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import modeles.DisplayModele;


public class DisplayItemController implements Initializable {

    @FXML
    private Label topic;
    @FXML
    private Label notes;
    @FXML
    private Label squance;
    @FXML
    private Label saveNum;
    @FXML
    private Label displayDate;
    @FXML
    private Label Destination;
    @FXML
    private Label displayTape;

    private DisplayModele displayModele;
    private DisplayPageListener mylistener;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setData(DisplayModele displayModele, DisplayPageListener mylistener) {
        this.displayModele = displayModele;
        this.mylistener = mylistener;
        squance.setText(Integer.toString(displayModele.getSquence()));
        saveNum.setText(displayModele.getDisplayid());
        displayDate.setText(displayModele.getDisplayDate());
        topic.setText(displayModele.getTopic());
        Destination.setText(displayModele.getDestination());
        displayTape.setText(displayModele.getDisplayType());
        notes.setText(displayModele.getNotes());
    }  

    @FXML
    private void click(MouseEvent event) {
        mylistener.onClickListener(displayModele);
    }

    
}
