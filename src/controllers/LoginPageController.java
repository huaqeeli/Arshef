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
import trainingdata.App;

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
        boolean userNameStatus = FormValidation.logintextFieldNotEmpty(userName, "ادخل اسم المستخدم");
        boolean passwordStatus = FormValidation.logintextFieldNotEmpty(password, "ادخل كلمة المرور");
        if (userNameStatus && passwordStatus) {
            try { 
                ResultSet rs = DatabaseAccess.getData("SELECT personaldata.NAME,personaldata.RANK,personaldata.MILITARYID,userdata.USERTYPE,userdata.USERNAME,userdata.PASSWORD,userdata.PASSWORDSTATE FROM personaldata,userdata "
                        + "WHERE personaldata.MILITARYID = userdata.MILITARYID AND USERNAME ='" + userName.getText() + "' AND PASSWORD = '" + password.getText() + "'");
                if (rs.next()) {
                    String mypassword = rs.getString("userdata.PASSWORDSTATE");
                    if (mypassword.equals("default")) {
                         App.lodChangePassowrdPage(userName.getText());
                    } else {
                        lodMainPage(rs.getString("NAME"), rs.getString("RANK"), rs.getString("USERTYPE"),rs.getString("MILITARYID"));
                        close();
                    }
                } else {
                    FormValidation.showAlert(null, "اسم المستخدم او كلمة المرور خاطئة الرجاء التاكد من الادخال الصحيح", Alert.AlertType.ERROR);
                }
            } catch (IOException | SQLException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }
        }
    }

    public void lodMainPage(String userName, String rank, String userType,String userid)  {
        try {
            Stage stage = new Stage();
            Pane myPane = null;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/homePage.fxml"));
            myPane = loader.load();
            
            controller = (HomePageController) loader.getController();
            controller.setData(userName, rank, userType,userid);
            Scene scene = new Scene(myPane);
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());
            
            Duration delay = Duration.seconds(600);
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
                    FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
                }
            });
            scene.addEventFilter(InputEvent.ANY, evt -> transition.playFromStart());
            stage.setScene(scene);
            stage.getIcons().add(new Image("/images/appicon.png"));
            stage.setTitle("الصفحة الرئيسية");
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(LoginPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void lodLogingPage() throws IOException {
        Stage stage = new Stage();
        Pane myPane = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginPage.fxml"));
        myPane = loader.load();
        Scene scene = new Scene(myPane);
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        stage.setScene(scene);
        stage.getIcons().add(new Image("/images/appicon.png"));
        stage.setTitle("تسجيل الدخول");
        stage.show();
    }

    private void close() {
        Stage stage = (Stage) content.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void changePassword(ActionEvent event) {
        App.showFxml("/view/ChangePassowrd");
    }

}
