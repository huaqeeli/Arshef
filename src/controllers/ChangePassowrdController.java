package controllers;

import Validation.FormValidation;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ChangePassowrdController implements Initializable {

    @FXML
    private PasswordField oldPasowrd;
    @FXML
    private PasswordField newPassword;
    @FXML
    private PasswordField reNewPassowrd;
    @FXML
    private AnchorPane content;
    @FXML
    private TextField militaryid;
    String militaryID = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        militaryid.setText(militaryID);
    }

    @FXML
    private void close(ActionEvent event) {
        Stage stage = (Stage) content.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void save(ActionEvent event) throws IOException {
        String tabelNme = "userdata";
        String fieldName = "`PASSWORD`=?,`PASSWORDSTATE`=?";
        String[] data = {newPassword.getText(), "new"};
        boolean militaryidstatus = FormValidation.textFieldNotEmpty(militaryid, "ادخل الرقم العسكري");
        boolean newPasswordstatus = FormValidation.textFieldNotEmpty(newPassword, "ادخل كلمة مرور جديدة");
        boolean reNewPassowrdstatus = FormValidation.textFieldNotEmpty(reNewPassowrd, "اعد ادخال كلمة المرور الجديدة");

        if (militaryidstatus && newPasswordstatus && reNewPassowrdstatus) {
            if (newPassword.getText().equals(reNewPassowrd.getText())) {
                try {
                    int t = DatabaseAccess.updat(tabelNme, fieldName, data, "MILITARYID ='" + militaryid.getText() + "'");
                    if (t > 0) {
                        FormValidation.showAlert(null, "تم تحديث كلمة المرور ويمكنك الان تسجيل الدخول بالبيانات الجديدة", Alert.AlertType.INFORMATION);
                        close(event);
                    }
                } catch (IOException ex) {
                    FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
                }
            } else {
                FormValidation.showAlert(null, "كلمة المرور غير مطابقة", Alert.AlertType.ERROR);
            }
        }
    }

    public void setMilitaryId(String militariid) {
        militaryID = militariid;
        militaryid.setText(militaryID);
        militaryid.setEditable(false);
    }

}
