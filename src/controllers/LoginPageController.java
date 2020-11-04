
package controllers;


import Validation.FormValidation;
import com.huaqeeli.training.HomePageController;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginPageController implements Initializable {

    @FXML
    private TextField userName;
    @FXML
    private PasswordField password;
    @FXML
    private AnchorPane content;
    HomePageController controller = new HomePageController();
   

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void login(ActionEvent event) {
        boolean userNameStatus = FormValidation.textFieldNotEmpty(userName, "ادخل اسم المستخدم");
        boolean passwordStatus = FormValidation.textFieldNotEmpty(password, "ادخل كلمة المرور");
        if (userNameStatus && passwordStatus) {
            try {
                ResultSet rs = DatabaseAccess.select("users", "USERNAME ='" + userName.getText() + "' AND PASSWORD = '" + password.getText() + "'");
                if (rs.next()) {
                    lodMainPage(rs.getString("NAME"), rs.getString("USERID"));
                    close();
                } else {
                    FormValidation.showAlert(null, "اسم المستخدم او كلمة المرور خاطئة الرجاء التاكد من الادخال الصحيح", Alert.AlertType.ERROR);
                }
            } catch (IOException | SQLException ex) {
                Logger.getLogger(LoginPageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void lodMainPage(String userName, String userid) throws IOException {
        Stage stage = new Stage();
        Pane myPane = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/MainPage.fxml"));
        myPane = loader.load();

        controller = (HomePageController) loader.getController();
        controller.setUserName(userName, userid);
        Scene scene = new Scene(myPane);
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());

        Duration delay = Duration.seconds(200);
        PauseTransition transition = new PauseTransition(delay);
        transition.setOnFinished(evt -> {
            try {
                if (controller.logOut) {
                    transition.stop();
                } else {
                    controller.close();
                    lodLogingPage();
                    transition.stop();
                }
            } catch (IOException ex) {
                Logger.getLogger(LoginPageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        scene.addEventFilter(InputEvent.ANY, evt -> transition.playFromStart());
        stage.setScene(scene);
        stage.getIcons().add(new Image("/Images/iconImage.png"));
        stage.setTitle("الصفحة الرئيسية");
        stage.show();
    }
    
    public void lodLogingPage() throws IOException {
        Stage stage = new Stage();
        Pane myPane = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/LoginPage.fxml"));
        myPane = loader.load();
        Scene scene = new Scene(myPane);
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        stage.setScene(scene);
        stage.getIcons().add(new Image("/Images/iconImage.png"));
        stage.setTitle("تسجيل الدخول");
        stage.show();
    }

    private void close() {
        Stage stage = (Stage) content.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void changePassword(ActionEvent event) {
        try {
            loding.lodNewFXML("/Views/Users/ChangePassowrd");
        } catch (IOException ex) {
            Logger.getLogger(LoginPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
