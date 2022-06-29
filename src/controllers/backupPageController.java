package controllers;

import Validation.FormValidation;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

public class backupPageController implements Initializable {

    @FXML
    private VBox content;
    @FXML
    private TextField backupUrl;
    @FXML
    private VBox continar;

    static Config config = new Config();
    File backupfile = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void close(ActionEvent event) {
        Stage stage = (Stage) content.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void getBackup(ActionEvent event) {
        try {
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            FileChooser fileChooser = new FileChooser();
            Window stage = null;
            fileChooser.setInitialFileName("Backup_"+ date + ".sql");
            File file = fileChooser.showSaveDialog(stage);
            String savefile = null;
            if (file != null) {
                savefile = file.toString();
            }
            String userName = config.getUserName();
            String password = config.getPassword();
            String dbName = config.getDbName();
            String hostName = config.getHostName();
            Runtime run = Runtime.getRuntime();
            /*"C:\\Program Files\\MySQL\\MySQL Server 5.6\\bin\\mysqldump --no-defaults --user="+ userName +" --password=" + password +" --host="+hostName+" --skip-opt --add-locks --create-options --disable-keys --extended-insert --single-transaction --skip-master-data --quick --set-charset --flush-privileges --quote-names --triggers --routines --comments --databases --default-character-set="$DB_CHARSET" --max_allowed_packet=16M "+dbName+" --result-file="+savefile+" */
//            Process pr = run.exec("C:\\Program Files\\MySQL\\MySQL Server 5.6\\bin\\mysqldump.exe -u" + userName + " -p" + password + " --add-drop-database -B " + dbName + " -r " + savefile);
            Process pr = run.exec(config.getAppURL() +"\\backupfiles\\mysqldump --no-defaults --user=" + userName + " "
                    + "--password=" + password + " --host=" + hostName + " --skip-opt --add-locks --create-options --disable-keys "
                    + "--extended-insert --single-transaction --skip-master-data --quick --set-charset --flush-privileges "
                    + "--quote-names --triggers --routines --comments --databases --default-character-set=utf8 "
                    + "--max_allowed_packet=100M " + dbName + " --result-file=" + savefile);
            int processComplete = pr.waitFor();
            if (processComplete == 0) {
                FormValidation.showAlert(null, "تم اخذ نسخةاحتياطية بنجاح", Alert.AlertType.CONFIRMATION);
            } else {
                FormValidation.showAlert(null, "حدثت مشكلة لم يتم اخذ النسخة", Alert.AlertType.ERROR);
            }

        } catch (IOException | InterruptedException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void getBackupUrl(ActionEvent event) {
        Window stage = null;
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter ext1 = new FileChooser.ExtensionFilter("Sql files(*.sql)", "*.SQL");
        fileChooser.getExtensionFilters().addAll(ext1);
        backupfile = fileChooser.showOpenDialog(stage);
        backupUrl.setText(backupfile.getPath());
    }

    @FXML
    private void restorData(ActionEvent event) {
        try {
            String userName = config.getUserName();
            String password = config.getPassword();
            Runtime run = Runtime.getRuntime();
            if (backupfile == null) {
                FormValidation.showAlert(null, "حدد ملف النسخة الاحتياطية", Alert.AlertType.ERROR);
            } else {
                String[] restorCmd = new String[]{config.getAppURL() +"\\mysql.exe", " --user=" + userName, " --password=" + password, " -e", " source " + backupfile};
                Process pr = run.exec(restorCmd);

                int processComplete = pr.waitFor();
                if (processComplete == 0) {
                    FormValidation.showAlert(null,"تم استرجاع البيانات بنجاح", Alert.AlertType.CONFIRMATION);
                } else {
                    FormValidation.showAlert(null,"حدث خطاء لم يتم استرجاع البيانات", Alert.AlertType.ERROR);
                }
            }
        } catch (IOException | InterruptedException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

}
