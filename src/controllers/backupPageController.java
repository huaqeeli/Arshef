package controllers;

import Validation.FormValidation;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.progress.RingProgressIndicator;

public class backupPageController implements Initializable {

    @FXML
    private VBox content;
    @FXML
    private TextField backupUrl;
    @FXML
    private VBox continar;
    @FXML
    private StackPane stackPane;

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
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        FileChooser fileChooser = new FileChooser();
        Window stage = null;
        fileChooser.setInitialFileName("Backup" + "_" + date + ".sql");
        File file = fileChooser.showSaveDialog(stage);
        String savefile = null;
        if (file != null) {
            savefile = file.toString();
            RingProgressIndicator rpi = new RingProgressIndicator();
            rpi.setRingWidth(200);
            rpi.makeIndeterminate();
            stackPane.getChildren().addAll(rpi);
            new GetBackup(rpi, savefile).start();
        }
//        final Backup serves = new Backup(savefile);
//        Region vile = new Region();
//        vile.setStyle("-fx-background-color:rgba(0,0,0,0.4)");
//        vile.setPrefSize(400, 440);
//        ProgressIndicator p = new ProgressIndicator();
//        p.setMaxSize(80, 80);
//        p.setStyle("-fx-background-color:#696969;"
//                + "-fx-foreground-color: #FFFFFF;"
//                + "-fx-fill: #ff0000 ;");
//
//        p.progressProperty().bind(serves.progressProperty());
//        vile.visibleProperty().bind(serves.runningProperty());
//        p.visibleProperty().bind(serves.runningProperty());

    }

    @FXML
    private void getBackupUrl(ActionEvent event) {
        Window stage = null;
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter ext1 = new FileChooser.ExtensionFilter("Sql files(*.sql)", "*.SQL");
        fileChooser.getExtensionFilters().addAll(ext1);
        backupfile = fileChooser.showOpenDialog(stage);
        if (backupfile != null) {
            backupUrl.setText(backupfile.getPath());
        }

    }

    @FXML
    private void restorData(ActionEvent event) {
        if (backupfile == null) {
            FormValidation.showAlert(null, "الرجاء تحديد ملف النسخة الاحتياطية", Alert.AlertType.ERROR);
        } else {
//            final Restor serves = new Restor(backupfile);
//            Region vile = new Region();
//            vile.setStyle("-fx-background-color:rgba(0,0,0,0.4)");
//            vile.setPrefSize(400, 440);
//            ProgressIndicator p = new ProgressIndicator();
//            p.setMaxSize(80, 80);
//            p.setStyle("-fx-text-fill: #FFFFFF;");
//
//            p.progressProperty().bind(serves.progressProperty());
//            vile.visibleProperty().bind(serves.runningProperty());
//            p.visibleProperty().bind(serves.runningProperty());

            RingProgressIndicator rpi = new RingProgressIndicator();
            rpi.setRingWidth(200);
            rpi.makeIndeterminate();
            stackPane.getChildren().addAll(rpi);
            new GetRestor(rpi, backupfile).start();
        }
    }

    public class GetBackup extends Thread {

        RingProgressIndicator rpi;
        private String savefile;
        int progrss = 0;

        public GetBackup(RingProgressIndicator rpi, String savefile) {
            this.rpi = rpi;
            this.savefile = savefile;
        }

        @Override
        public void run() {

            try {
                for (int i = 0; i <= 50; i++) {
                    progrss = i;
                    Thread.sleep(150);
                    Platform.runLater(() -> {
                        rpi.setProgress(progrss);
                    });
                }
                String userName = config.getUserName();
                String password = config.getPassword();
                String dbName = config.getDbName();
                String hostName = config.getHostName();
                Runtime run = Runtime.getRuntime();
                String path = "C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump --user=" + userName + " --password=" + password + " --host=" + hostName + " --max_allowed_packet=50M " + dbName + " --result-file=" + savefile;
                Process pr = run.exec(path);

                int processComplete = pr.waitFor();
                for (int i = 50; i <= 100; i++) {
                    progrss = i;
                    Thread.sleep(150);
                    Platform.runLater(() -> {
                        rpi.setProgress(progrss);
                    });
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (processComplete == 0) {
                            rpi.setVisible(false);
                            FormValidation.showAlert(null, "تم اخذ نسخة احتياطية بنجاح", Alert.AlertType.INFORMATION);
                        } else {
                            rpi.setVisible(false);
                            FormValidation.showAlert(null, "لم يتم اخذ نسخة احتياطية بنجاح", Alert.AlertType.ERROR);
                        }
                    }

                });

            } catch (IOException | InterruptedException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }

        }

    }

    public class GetRestor extends Thread {

        RingProgressIndicator rpi;
        private File backupfile;
        int progrss = 0;

        public GetRestor(RingProgressIndicator rpi, File backupfile) {
            this.rpi = rpi;
            this.backupfile = backupfile;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i <= 50; i++) {
                    progrss = i;
                    Thread.sleep(150);
                    Platform.runLater(() -> {
                        rpi.setProgress(progrss);
                    });
                }
                String userName = config.getUserName();
                String password = config.getPassword();
                String dbName = config.getDbName();
                Runtime run = Runtime.getRuntime();
                String[] restorCmd = new String[]{"C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysql ", dbName, " -u" + userName, " -p" + password, " -e", " source " + backupfile};
                Process pr = run.exec(restorCmd);

                int processComplete = pr.waitFor();
                for (int i = 50; i <= 100; i++) {
                    progrss = i;
                    Thread.sleep(150);
                    Platform.runLater(() -> {
                        rpi.setProgress(progrss);
                    });
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (processComplete == 0) {
                            rpi.setVisible(false);
                            FormValidation.showAlert(null, "تم استعادة البيانات بنجاح", Alert.AlertType.INFORMATION);
                        } else {
                            rpi.setVisible(false);
                            FormValidation.showAlert(null, "لم يتم استعادة البيانات بنجاح", Alert.AlertType.ERROR);
                        }
                    }
                });

            } catch (IOException | InterruptedException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }

        }

    }
}
