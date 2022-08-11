package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class IndexingPageController implements Initializable {

    @FXML
    private HBox content;
    @FXML
    private TextField fileNumber;
    @FXML
    private ComboBox<?> startDateDay;
    @FXML
    private ComboBox<?> startDateMonth;
    @FXML
    private ComboBox<?> startDateYear;
    @FXML
    private ComboBox<?> endDateDay;
    @FXML
    private ComboBox<?> endDateMonth;
    @FXML
    private ComboBox<?> endDateYear;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AppDate.setDateValue(startDateDay, startDateMonth, startDateYear);
        AppDate.setCurrentDate(startDateDay, startDateMonth, startDateYear);
        AppDate.setDateValue(endDateDay, endDateMonth, endDateYear);
        AppDate.setCurrentDate(endDateDay, endDateMonth, endDateYear);
    }

    @FXML
    private void update(ActionEvent event) {
    }

    @FXML
    private void printIndex(ActionEvent event) {
    }

    @FXML
    private void close(ActionEvent event) {
        Stage stage = (Stage) content.getScene().getWindow();
        stage.close();
    }

    public String getStartDateDate() {
        return AppDate.getDate(startDateDay, startDateMonth, startDateYear);
    }

    public String getEndDate() {
        return AppDate.getDate(endDateDay, endDateMonth, endDateYear);
    }
}
