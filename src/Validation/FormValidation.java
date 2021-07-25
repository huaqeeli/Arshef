package Validation;

import controllers.DatabaseConniction;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;

public class FormValidation {

    public static boolean textFieldNotEmpty(TextField t) {
        boolean state = false;

        if (t.getText() != null && !t.getText().isEmpty()) {
            state = true;
        }

        return state;
    }

    public static boolean textFieldNotEmpty(TextField t, String validationmassage) {
        boolean state = true;
        if (!textFieldNotEmpty(t)) {
            state = false;
            t.setPromptText(validationmassage);
            t.setStyle("-fx-background-color:#F8DCDA");
        } else {
            t.setStyle("-fx-background-color:#FFFFFF");
        }

        return state;
    }

    public static boolean textFieldNotEmpty(TextField[] t, String validationmassage) {
        boolean state = true;
        for (int i = 0; i < t.length; i++) {
            if (!textFieldNotEmpty(t[i])) {
                state = false;
               showAlert("خطاء حقل فارغ", validationmassage);
                t[i].setStyle("-fx-background-color:#F8DCDA");
            } else {
                t[i].setStyle("#117E88;-fx-background-color:#FFFFFF");
            }
        }
        return state;
    }

    public static boolean logintextFieldNotEmpty(TextField t, String validationmassage) {
        boolean state = true;
        if (!textFieldNotEmpty(t)) {
            state = false;
            t.setPromptText(validationmassage);
            t.setStyle("-fx-font-family: \"URW DIN Arabic\";"
                    + "    -fx-font-size:15px;"
                    + "    -fx-text-fill: #E20012;"
                    + "    -fx-background-color: #EFB0AB;"
                    + "    -fx-border-color: #E20012;"
                    + "    -fx-border-insets: 0;"
                    + "    -fx-border-width: 0 0 2 0;"
                    + "    -fx-border-style: solid;");
        } else {
            t.setStyle("-fx-font-family: \"URW DIN Arabic\";"
                    + "    -fx-font-size:15px;"
                    + "    -fx-text-fill: #CDE7F0;"
                    + "    -fx-background-color: #686868;"
                    + "    -fx-border-color: #7C9D7C;"
                    + "    -fx-border-insets: 0;"
                    + "    -fx-border-width: 0 0 2 0;"
                    + "    -fx-border-style: solid;");
        }

        return state;
    }

    public static boolean comboBoxNotEmpty(ComboBox t) {
        boolean state = false;

        if (t.getValue() != null && !t.getValue().equals("")) {
            state = true;
        }

        return state;
    }

    public static boolean comboBoxNotEmpty(ComboBox t, String validationmassage) {
        boolean state = true;
        if (!comboBoxNotEmpty(t)) {
            state = false;
            t.setPromptText(validationmassage);
            t.setStyle(" -fx-background-color:#F8DCDA");
        } else {
            t.setStyle("-fx-background-color:#FFFFFF");
        }

        return state;
    }

    public static boolean textFieldTypeDate(TextField t) {
        boolean state = false;

        if (t.getText().matches("(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)")) {
            state = true;
        }

        return state;
    }

    public static boolean textFieldTypeDate(TextField t, String validationmassage) {
        boolean state = true;
        if (!textFieldTypeDate(t)) {
            state = false;
            t.setPromptText(validationmassage);
            t.setStyle("-fx-background-color:#F8DCDA");
        } else {
            t.setStyle("-fx-background-color:#FFFFFF");
        }

        return state;
    }

    public static boolean textFieldTypeNumber(TextField t) {
        boolean state = false;

        if (t.getText().matches("([0-9]+(\\.[0-9]+)?)+")) {
            state = true;
        }

        return state;
    }

    public static boolean textFieldTypeNumber(TextField t, String validationmassage) {
        boolean state = true;
        if (!textFieldTypeNumber(t)) {
            state = false;
            t.setText("");
            t.setPromptText(validationmassage);
            t.setStyle(" -fx-background-color:#F8DCDA");
        } else {
            t.setStyle("-fx-background-color:#FFFFFF");
        }

        return state;
    }

    public static Alert confirmationDilog(String titel, String massage) {
        Alert alert = new Alert(AlertType.CONFIRMATION, massage, ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.setTitle(titel);
        alert.setHeaderText(null);
        alert.showAndWait();
        return alert;
    }

    public static void showAlert(String titel, String massage) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titel);
        alert.setHeaderText(null);
        alert.setContentText(massage);
        alert.showAndWait();
    }

    // Alert alert = new Alert(AlertType.CONFIRMATION, "سوف يتم حذف السجل هل تريد المتابعة", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
    public static void showAlert(String titel, String massage, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titel);
        alert.setHeaderText(null);
        alert.setContentText(massage);
        alert.showAndWait();
    }

    public static boolean transporState(String tapleName, String fildName, String condition, String validationmassage) {
        boolean state = true;
        try {

            ResultSet rs = null;
            String guiry = "SELECT " + fildName + " FROM " + tapleName + " WHERE" + condition;
            Connection con = DatabaseConniction.dbConnector();
            try {
                PreparedStatement psm = con.prepareStatement(guiry);
                rs = psm.executeQuery();
                if (rs.next()) {
                    state = false;
                    showAlert("التحقق من التكرار", validationmassage);
                }
                con.close();
                psm.close();
                rs.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }

        } catch (IOException ex) {
            Logger.getLogger(FormValidation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return state;
    }

    public static boolean ifNotexisting(String tapleName, String fildName, String condition, String validationmassage) {
        boolean state = true;
        try {
            ResultSet rs = null;
            String guiry = "SELECT " + " " + fildName + " " + " FROM " + " " + tapleName + " " + " WHERE" + " " + condition;
            Connection con = DatabaseConniction.dbConnector();
            PreparedStatement psm = con.prepareStatement(guiry);
            rs = psm.executeQuery();
            if (!rs.next()) {
                state = false;
                showAlert("التحقق من التكرار", validationmassage);
            }
            con.close();
            psm.close();
            rs.close();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(FormValidation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return state;
    }

    public static boolean ifNotexisting(String tapleName, String fildName, String condition) {
        boolean state = true;
        try {
            ResultSet rs = null;
            String guiry = "SELECT " + " " + fildName + " " + " FROM " + " " + tapleName + " " + " WHERE" + " " + condition;
            Connection con = DatabaseConniction.dbConnector();
            PreparedStatement psm = con.prepareStatement(guiry);
            rs = psm.executeQuery();
            if (!rs.next()) {
                state = false;
                System.out.println("no image");
            } else {
                System.out.println("ther is image");
            }
            con.close();
            psm.close();
            rs.close();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(FormValidation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return state;
    }

    public static boolean ifexisting(String tapleName, String fildName, String condition, String validationmassage) {
        boolean state = true;
        try {
            ResultSet rs = null;
            String guiry = "SELECT " + " " + fildName + " " + " FROM " + " " + tapleName + " " + " WHERE" + " " + condition;
            Connection con = DatabaseConniction.dbConnector();
            PreparedStatement psm = con.prepareStatement(guiry);
            rs = psm.executeQuery();
            if (rs.next()) {
                state = false;
                showAlert("التحقق من التكرار", validationmassage);
            }
            con.close();
            psm.close();
            rs.close();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(FormValidation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return state;
    }

    public static boolean notNull(String string, String validationmassage) {
        boolean state = true;
        if (string == null) {
            state = false;
            showAlert("", validationmassage);
        }
        return state;
    }

    public static boolean isMatching(String tapleName, String fildName, String condition, String validationmassage) throws IOException {
        boolean state = false;
        try {
            ResultSet rs = null;
            String guiry = "SELECT " + " " + fildName + " " + " FROM " + " " + tapleName + " " + " WHERE" + " " + condition;
            Connection con = DatabaseConniction.dbConnector();
            PreparedStatement psm = con.prepareStatement(guiry);
            rs = psm.executeQuery();
            if (rs.next()) {
                state = true;
            } else {
                showAlert(null, validationmassage, AlertType.ERROR);
            }
            con.close();
            psm.close();
            rs.close();
        } catch (IOException | SQLException ex) {
            showAlert(null, ex.toString(), AlertType.ERROR);
        }
        return state;
    }

}
