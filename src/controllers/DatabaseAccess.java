package controllers;


import Validation.FormValidation;
import com.mysql.jdbc.Statement;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javax.swing.JOptionPane;

public class DatabaseAccess {

    static Config config = new Config();
    static String[] data = new String[5];

    public static int insert(String tapleName, String fildName, String valueNamber, String[] data) throws IOException {
        int lastId = 0;
        Connection con = DatabaseConniction.dbConnector();
        String guiry = "INSERT INTO " + tapleName + "(" + fildName + ")VALUES(" + valueNamber + " )";
        try {
            PreparedStatement psm = con.prepareStatement(guiry, Statement.RETURN_GENERATED_KEYS);
            int e = data.length;
            for (int i = 1; i <= e; i++) {
                psm.setString(i, data[i - 1]);
            }
            int t = psm.executeUpdate();
            if (t > 0) {
            } else {
                JOptionPane.showMessageDialog(null, "حدث خطاء في عملية الحفظ الرجاء المحاولة مرة اخرى");
            }
            ResultSet rs = psm.getGeneratedKeys();
            if (rs.next()) {
                lastId = rs.getInt(1);
            }
            con.close();
            psm.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        return lastId;
    }
public static int insert(String tapleName, String fildName, String valueNamber, String[] data, File imagefile) throws IOException {
        int lastId = 0;
        Connection con = DatabaseConniction.dbConnector();
        String guiry = "INSERT INTO " + tapleName + "(" + fildName + ")VALUES(" + valueNamber + ")";
        try {
            PreparedStatement psm = con.prepareStatement(guiry, Statement.RETURN_GENERATED_KEYS);
            int e = data.length;
            for (int i = 1; i <= e; i++) {
                psm.setString(i, data[i - 1]);
            }
            if (imagefile != null) {
                FileInputStream fin = new FileInputStream(imagefile);
                int len = (int) imagefile.length();
                psm.setBinaryStream(e+1, fin, len);
            }
            int t = psm.executeUpdate();
            if (t > 0) {
            } else {
                FormValidation.showAlert("", "حدث خطاء في عملية الحفظ الرجاء المحاولة مرة اخرى");
            }
            ResultSet rs = psm.getGeneratedKeys();
            if (rs.next()) {
                lastId = rs.getInt(1);
            }
            con.close();
            psm.close();
            rs.close();
        } catch (SQLException ex) {
             JOptionPane.showMessageDialog(null, ex);
        }
        return lastId;
    }
   

    public static ResultSet select(String tapleName) throws IOException {
        ResultSet rs = null;
        String guiry = "SELECT * FROM " + tapleName;
        Connection con = DatabaseConniction.dbConnector();
        try {
            PreparedStatement psm = con.prepareStatement(guiry);
            rs = psm.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        return rs;
    }

    public static ResultSet getItems(String tapleName) throws IOException {
        ResultSet rs = null;
        String guiry = "SELECT * FROM " + tapleName;
        Connection con = DatabaseConniction.dbConnector();
        try {
            PreparedStatement psm = con.prepareStatement(guiry);
            rs = psm.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        return rs;
    }

    public static ResultSet select(String tapleName, String condation) throws IOException {
        ResultSet rs = null;
        String guiry = "SELECT * FROM " + tapleName + " " + "WHERE" + " " + condation;
        Connection con = DatabaseConniction.dbConnector();
        try {
            PreparedStatement psm = con.prepareStatement(guiry);
            rs = psm.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        return rs;
    }
    public static ResultSet getCourses(String query) throws IOException {
        ResultSet rs = null;
        Connection con = DatabaseConniction.dbConnector();
        try {
            PreparedStatement psm = con.prepareStatement(query);
            rs = psm.executeQuery();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        return rs;
    }
    
    public static void updat(String tapleName, String fildNameAndValue, String[] data, String condition) throws IOException {
        Connection con = DatabaseConniction.dbConnector();
        String guiry = "UPDATE " + tapleName + " SET " + fildNameAndValue + "WHERE" +" "+ condition;
        try {
            PreparedStatement psm = con.prepareStatement(guiry);
            int e = data.length;
            for (int i = 1; i <= e; i++) {
                psm.setString(i, data[i - 1]);
            }
            int t = psm.executeUpdate();
            if (t > 0) {
               FormValidation.showAlert("", "تم تحديث البيانات", Alert.AlertType.INFORMATION);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public static void updat(String tapleName, String fildNameAndValue, String condition) throws IOException {
        Connection con = DatabaseConniction.dbConnector();
        String guiry = "UPDATE " + tapleName + " SET " + fildNameAndValue + " WHERE" + condition;
        try {
            PreparedStatement psm = con.prepareStatement(guiry);
            psm.executeUpdate();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    public static void updat(String tapleName, String fildNameAndValue, String[] data, String condition, File imagefile) throws IOException {
        Connection con = DatabaseConniction.dbConnector();
        String guiry = "UPDATE " + tapleName + " SET " + fildNameAndValue + " WHERE" + " " + condition;
        try {
            PreparedStatement psm = con.prepareStatement(guiry);
            int e = data.length;
            for (int i = 1; i <= e; i++) {
                psm.setString(i, data[i - 1]);
               
            }
            if (imagefile != null) {
                FileInputStream fin = new FileInputStream(imagefile);
                int len = (int) imagefile.length();
                psm.setBinaryStream(e+1, fin, len);
                
            }
            int t = psm.executeUpdate();
            if (t > 0) {
                FormValidation.showAlert("", "تم تحديث البيانات");
            }
            con.close();
            psm.close();
        } catch (SQLException ex) {
           JOptionPane.showMessageDialog(null, ex);
        }
    }

    public static void delete(String tapleName, String condition) throws IOException {
        Connection con = DatabaseConniction.dbConnector();
        String guiry = "DELETE FROM " + tapleName + " WHERE " + condition;
        try {
            PreparedStatement psm = con.prepareStatement(guiry);
            Alert alert = FormValidation.confirmationDilog("تاكيد الحذف", "سوف يتم حذف السجل هل تريد المتابعة");
            if (alert.getResult() == ButtonType.YES) {
                psm.executeUpdate();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
}
