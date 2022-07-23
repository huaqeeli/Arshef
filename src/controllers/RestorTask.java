package controllers;

import Validation.FormValidation;
import static controllers.DatabaseAccess.config;
import java.io.File;
import java.io.IOException;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;

public class RestorTask extends Task {

    private File backupfile;
    static Config config = new Config();

    public RestorTask(File backupfile) {
        this.backupfile = backupfile;
    }

    @Override
    protected Object call() throws Exception {
        updateProgress(0, 500);
        Thread.sleep(1505);
        updateProgress(30, 500);
        Thread.sleep(1505);
        updateProgress(100, 500);
        Thread.sleep(1505);

        try {
            String userName = config.getUserName();
            String password = config.getPassword();
            String dbName = "hutechnicaldb";
            Runtime run = Runtime.getRuntime();
            String[] restorCmd = new String[]{config.getAppURL()+"\\backup\\mysql.exe ", dbName, " -u" + userName, " -p" + password, " -e", " source " + backupfile};
            Process pr = run.exec(restorCmd);
            int processComplete = pr.waitFor();
            System.out.println(processComplete);
            if (processComplete == 0) {
                FormValidation.showAlert(null, "تم استعادة البيانات بنجاح", Alert.AlertType.CONFIRMATION);
            } else {
                FormValidation.showAlert(null, "حدثت مشكلة لم يتم استعادة البيانات", Alert.AlertType.ERROR);
            }
        } catch (IOException | InterruptedException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }

        updateProgress(150, 500);
        Thread.sleep(1505);
        updateProgress(200, 500);
        Thread.sleep(1505);
        updateProgress(300, 500);
        Thread.sleep(1505);
        updateProgress(350, 500);
        Thread.sleep(1505);
        updateProgress(500, 500);
        Thread.sleep(1505);
        Thread.sleep(1505);
        return null;

    }

}
