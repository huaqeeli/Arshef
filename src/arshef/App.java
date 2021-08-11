package arshef;

import Validation.FormValidation;
import controllers.AddNamesController;
import controllers.CensusPageController;
import controllers.ChangePassowrdController;
import controllers.Config;
import controllers.DatabaseAccess;
import controllers.HijriCalendar;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.chrono.HijrahDate;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {

    private static Scene scene;
    private static double x, y;
    Config config = new Config();

    @Override
    public void start(Stage stage) throws IOException {
        if (config.getAppURL() == null) {
            App.showFxml("/view/ConfigSetting");
        } else {
            Font.loadFont(new FileInputStream(new File(config.getAppURL() + "\\fonts\\URW-DIN-Arabic.ttf")), 12);
        }
        newYearSetting();
        scene = new Scene(loadFXML("/view/LoginPage"));

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        stage.setScene(scene);
        stage.getIcons().add(new Image("/images/appicon.png"));
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Parent root = fxmlLoader.load();
        return root;
    }

    public static Parent loadCensusPage(String userType) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/view/censusPage.fxml"));
        Parent root = fxmlLoader.load();
        CensusPageController con = new CensusPageController();
        con = fxmlLoader.getController();
        con.setUsertype(userType);
        return root;
    }

    public static FXMLLoader loadFX(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader;
    }

    public static Parent lodChangePassowrdPage(String militariid) {
        Parent root = null;
        try {
            FXMLLoader fxmlLoader = loadFX("/view/ChangePassowrd");
            root = fxmlLoader.load();
            ChangePassowrdController controller = new ChangePassowrdController();
            controller = (ChangePassowrdController) fxmlLoader.getController();
            controller.setMilitaryId(militariid);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            scene.setFill(Color.TRANSPARENT);
            root.setOnMousePressed((MouseEvent event) -> {
                x = event.getSceneX();
                y = event.getSceneY();
            });
            root.setOnMouseDragged((MouseEvent event) -> {
                stage.setX(event.getScreenX() - x);
                stage.setY(event.getScreenY() - y);
            });
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.showAndWait();
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return root;
    }

    public static Parent lodAddNmaesPage(String circularID, String year) {
        Parent root = null;
        try {
            FXMLLoader fxmlLoader = loadFX("/view/AddNames");
            root = fxmlLoader.load();
            AddNamesController controller = new AddNamesController();
            controller = (AddNamesController) fxmlLoader.getController();
            controller.setPassingData(circularID, year);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            scene.setFill(Color.TRANSPARENT);
            root.setOnMousePressed((MouseEvent event) -> {
                x = event.getSceneX();
                y = event.getSceneY();
            });
            root.setOnMouseDragged((MouseEvent event) -> {
                stage.setX(event.getScreenX() - x);
                stage.setY(event.getScreenY() - y);
            });
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.showAndWait();
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return root;
    }

    public static void showFxml(String fxml) {
        Parent root = null;
        Stage stage = new Stage();
        try {
            root = loadFXML(fxml);
            stage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            root.setOnMousePressed((MouseEvent event) -> {
                x = event.getSceneX();
                y = event.getSceneY();
            });
            root.setOnMouseDragged((MouseEvent event) -> {
                stage.setX(event.getScreenX() - x);
                stage.setY(event.getScreenY() - y);
            });
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.showAndWait();
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }

    }

    public static void newYearSetting() {
        String newYear = Integer.toString(HijriCalendar.getSimpleYear());
        try {
            ResultSet rs = DatabaseAccess.select("recordesquins", "SQUINS_YEAR = '" + newYear + "'");
            if (!rs.next()) {
                DatabaseAccess.updatRegistrationNum(1, newYear);
            }
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    public static void main(String[] args) {
        launch();
    }

}
