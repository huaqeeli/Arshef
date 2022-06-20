
package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;


public class addVacationController implements Initializable {

    @FXML
    private VBox content;
    @FXML
    private ComboBox<?> nameOfDay;
    @FXML
    private ComboBox<?> Day;
    @FXML
    private ComboBox<?> Month;
    @FXML
    private ComboBox<?> Year;
    @FXML
    private TextField vacationPeriod;
    @FXML
    private TextField vacationReason;
    @FXML
    private TextField approvalOfficer;
    @FXML
    private TableView<?> viewTable;
    @FXML
    private TableColumn<?, ?> day_col;
    @FXML
    private TableColumn<?, ?> date_col;
    @FXML
    private TableColumn<?, ?> vacationPeriod_col;
    @FXML
    private TableColumn<?, ?> vacationReason_col;
    @FXML
    private TableColumn<?, ?> approvalOfficer_col;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void close(ActionEvent event) {
    }

    @FXML
    private void save(ActionEvent event) {
    }

    @FXML
    private void update(ActionEvent event) {
    }

    @FXML
    private void delete(ActionEvent event) {
    }
    
}
