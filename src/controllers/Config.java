package controllers;

import Validation.FormValidation;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import javafx.scene.control.Alert;

public class Config {

    String userName, password, dbName, hostName, AppURL,imagePath;
    Properties properties = new Properties();
    OutputStream output;

    public String getUserName() throws IOException {
        return userName = readProperties().getProperty("userName");
    }

    public void setUserName(String userName) {
       writeToProperties("userName", userName);
    }

    public String getPassword() throws IOException {
        return password = readProperties().getProperty("password");
    }

    public void setPassword(String password) {
       writeToProperties("password", password);
    }

    public String getDbName() throws IOException {
        return dbName = readProperties().getProperty("dbName");
    }

    public void setDbName(String dbName) {
        writeToProperties("dbName", dbName);
    }

    public String getHostName() throws IOException {
        return hostName = readProperties().getProperty("hostName");
    }

    public void setHostName(String hostName) {
        writeToProperties("hostName", hostName);
    }

    public String getImagePath() throws IOException {
        return imagePath = readProperties().getProperty("imagePath");
    }

    public void setImagePath(String imagePath) {
        writeToProperties("imagePath", imagePath);
    }

    public String getAppURL() throws IOException {
        return AppURL= readProperties().getProperty("AppURL");
    }

    public void setAppURL(String AppURL) {
       writeToProperties("AppURL", AppURL);
    }

    

    public void writeToProperties(String name, String value) {
        try {
            String propertiesFileName = "config.properties";
            File f = new File(propertiesFileName);
            InputStream input = new FileInputStream(f);
            if (input != null) {
                properties.load(input);
                properties.setProperty(name, value);
                output = new FileOutputStream(f);
                properties.store(output, null);
            }
        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    public Properties readProperties() throws IOException {
        String propertiesFileName = "config.properties";
        File f = new File(propertiesFileName);
        InputStream input = new FileInputStream(f);
        if (input != null) {
            properties.load(input);
        } else {
            FormValidation.showAlert(null, "property file not found in the classpath", Alert.AlertType.ERROR);
        }
        return properties;
    }

}
