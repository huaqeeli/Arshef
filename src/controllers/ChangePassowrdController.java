package controllers;

import Messages.Controllers.ShowMessage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ChangePassowrdController implements Initializable {

    @FXML
    private TextField nationalID;
    @FXML
    private PasswordField oldPasowrd;
    @FXML
    private PasswordField newPassword;
    @FXML
    private PasswordField reNewPassowrd;
    @FXML
    private AnchorPane content;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void close(ActionEvent event) {
        Stage stage = (Stage) content.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void save(ActionEvent event) throws IOException {
        String tabelNme = "users";
        String fieldName = "`PASSWORD`=?";
        String[] data = {newPassword.getText()};
        boolean nationalIDstatus = Validation.textFieldNotEmpty(nationalID, "ادخل رقم الهوية");
        boolean oldPasowrdstatus = Validation.textFieldNotEmpty(oldPasowrd, "ادخل كلمة المرور الحالية");
        boolean oldPasowrdexs = Validation.isMatching(tabelNme, "PASSWORD", "NATIONALID ='" + nationalID.getText() + "' And PASSWORD ='" + oldPasowrd.getText() + "'", "كلمة المرور الحالية غير صحيحة");
        boolean newPasswordstatus = Validation.textFieldNotEmpty(newPassword, "ادخل كلمة مرور جديدة");
        boolean reNewPassowrdstatus = Validation.textFieldNotEmpty(reNewPassowrd, "اعد ادخال كلمة المرور الجديدة");

        if (nationalIDstatus && oldPasowrdstatus && oldPasowrdexs && newPasswordstatus && reNewPassowrdstatus) {
            if (newPassword.getText().equals(reNewPassowrd.getText())) {
                try {
                    DatabaseAccess.updat(tabelNme, fieldName, data, "NATIONALID ='" + nationalID.getText() + "'");
                } catch (IOException ex) {
                    Logger.getLogger(ChangePassowrdController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                ShowMessage showMessage = new ShowMessage();
                showMessage.error("كلمة المرور غير مطابقة");
            }
        }
    }

}
