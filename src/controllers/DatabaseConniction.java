package controllers;

import Validation.FormValidation;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.scene.control.Alert;

public class DatabaseConniction {

    private static Connection con;
    static Config config = new Config();

    public static Connection dbConnector() throws IOException {
        String userName = config.getUserName();
        String password = config.getPassword();
        String dbName = config.getDbName();
        String hostName = config.getHostName();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://" + hostName + ":3306/" + dbName + "?useUnicode=true&characterEncoding=utf-8", userName, password);
        } catch (ClassNotFoundException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return con;
    }
}
