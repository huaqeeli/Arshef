package controllers;

import com.huaqeeli.arshef.MyListener;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import modeles.SecretModel;

public class SecretPageItemController implements Initializable {

    @FXML
    private Label squnse;
    @FXML
    private Label circularid;
    @FXML
    private Label circulardete;
    @FXML
    private Label destination;
    @FXML
    private Label topic;
    @FXML
    private Label saveFile;
    @FXML
    private Label note;

    private SecretModel secretModel;
    private MyListener mylistener;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void click(MouseEvent mouseEvent) {
        mylistener.onClickListener(secretModel);
    }

    public void setData(SecretModel secretModel, MyListener mylistener) {
        this.secretModel = secretModel;
        this.mylistener = mylistener;
        squnse.setText(Integer.toString(secretModel.getSqunse()));
        circularid.setText(secretModel.getCircularid());
        circulardete.setText(secretModel.getCirculardete());
        destination.setText(secretModel.getDestination());
        topic.setText(secretModel.getTopic());
        saveFile.setText(secretModel.getSaveFile());
        note.setText(secretModel.getNote());
    }

    @FXML
    private void scanImage(ActionEvent event) {
        System.out.println(circularid.getText());
    }

    @FXML
    private void addNames(ActionEvent event) {
    }

    @FXML
    private void showImage(ActionEvent event) {
    }

    @FXML
    private void delete(ActionEvent event) {
    }

}
