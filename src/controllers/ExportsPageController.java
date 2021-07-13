
package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;


public class ExportsPageController implements Initializable {
    @FXML
    private ComboBox<?> searchType;
    @FXML
    private ComboBox<?> year;
    @FXML
    private TextField searchText;
    @FXML
    private Button searchButton1;
    @FXML
    private ComboBox<?> receiptNumberDateDay;
    @FXML
    private ComboBox<?> receiptNumberDateMonth;
    @FXML
    private ComboBox<?> receiptNumberDateYear;
    @FXML
    private TextField topic;
    @FXML
    private ComboBox<?> destination;
    @FXML
    private TextField saveFile;
    @FXML
    private TextField circularid;
    @FXML
    private ComboBox<?> circularDateDay;
    @FXML
    private ComboBox<?> circularDateMonth;
    @FXML
    private ComboBox<?> circularDateYear;
    @FXML
    private TextField receiptNumber;
    @FXML
    private TextField imageUrl;
    @FXML
    private TableView<?> archefTable;
    @FXML
    private TableColumn<?, ?> squence_col;
    @FXML
    private TableColumn<?, ?> circularid_col;
    @FXML
    private TableColumn<?, ?> circularDate_col;
    @FXML
    private TableColumn<?, ?> receiptNumber_col;
    @FXML
    private TableColumn<?, ?> receiptDate_col;
    @FXML
    private TableColumn<?, ?> destination_col;
    @FXML
    private TableColumn<?, ?> topic_col;
    @FXML
    private TableColumn<?, ?> saveFile_col;
    @FXML
    private TableColumn<?, ?> circularType_col;
    @FXML
    private TableColumn<?, ?> circularImage_col;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void searchData(ActionEvent event) {
    }

    @FXML
    private void getImageUrle(ActionEvent event) {
    }

    @FXML
    private void save(ActionEvent event) {
    }

    @FXML
    private void edit(ActionEvent event) {
    }

    @FXML
    private void delete(ActionEvent event) {
    }

    @FXML
    private void clear(ActionEvent event) {
    }
    
}
