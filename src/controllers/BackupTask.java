package controllers;

import Validation.FormValidation;
import static controllers.DatabaseAccess.config;
import java.io.IOException;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;

public class BackupTask extends Task {

    private String savefile;

    public BackupTask(String savefile) {
        this.savefile = savefile;
    }
    int processComplete;

    @Override
    protected Object call() throws Exception {

//        updateProgress(0, 500);
//        Thread.sleep(1505);
//        updateProgress(30, 500);
//        Thread.sleep(1505);
//        updateProgress(100, 500);
//        Thread.sleep(1505);
        for (int i = 0; i < 100; i++) {
            updateProgress(i, 500);
            Thread.sleep(1505);
        }

        try {
            String userName = config.getUserName();
            String password = config.getPassword();
            String dbName = "hutechnicaldb";
            String hostName = config.getHostName();
            Runtime run = Runtime.getRuntime();
//            Process pr = run.exec("C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump --no-defaults --user=" + userName + " "
//                    + "--password=" + password + " --host=" + hostName + " --skip-opt --add-locks --create-options --disable-keys "
//                    + "--extended-insert --single-transaction --skip-master-data --quick --set-charset --flush-privileges "
//                    + "--quote-names --triggers --routines --comments --databases --default-character-set=utf8 "
//                    + "--max_allowed_packet=50M " + dbName + " --result-file=" + savefile);
            Process pr = run.exec(config.getAppURL() + "\\backup\\mysqldump --user=" + userName + " --password=" + password + " --host=" + hostName + " --max_allowed_packet=50M " + dbName + " --result-file=" + savefile);
            processComplete = pr.waitFor();
//           

        } catch (IOException | InterruptedException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
//        updateProgress(150, 500);
//        Thread.sleep(1505);
//        updateProgress(200, 500);
//        Thread.sleep(1505);
//        updateProgress(300, 500);
//        Thread.sleep(1505);
//        updateProgress(350, 500);
//        Thread.sleep(1505);
//        updateProgress(500, 500);
//        Thread.sleep(1505);
         for (int i = 1000; i < 500; i++) {
            updateProgress(i, 500);
            Thread.sleep(1505);
        }
        Thread.sleep(1505);
        if (processComplete == 0) {
            FormValidation.showAlert(null, "تم اخذ نسخةاحتياطية بنجاح", Alert.AlertType.CONFIRMATION);
        } else {
            FormValidation.showAlert(null, "حدثت مشكلة لم يتم اخذ النسخة", Alert.AlertType.ERROR);
        }
        return processComplete;
    }

}
