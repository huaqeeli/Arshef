package controllers;

import Validation.FormValidation;
import com.itextpdf.text.BadElementException;
import com.mysql.jdbc.Statement;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
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
                psm.setBinaryStream(e + 1, fin, len);
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
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return lastId;
    }

    public static com.itextpdf.text.Image getCircularImage(String militaryid, String coursid) {
        com.itextpdf.text.Image image = null;
        try {
            if (militaryid == null || coursid == null) {
                FormValidation.showAlert(null, "اختر السجل من الجدول", Alert.AlertType.ERROR);
            } else {
                ResultSet rs = DatabaseAccess.getData("SELECT COURSIMAGE FROM arshefdata WHERE CIRCULARID = '" + militaryid + "'AND CIRCULARDATE = '" + coursid + "'");
                if (rs.next()) {
                    ArrayList images = new ArrayList();
                    images.add(rs.getBytes("COURSIMAGE"));
                    byte[] scaledInstance = (byte[]) images.get(0);
                    image = com.itextpdf.text.Image.getInstance(scaledInstance);
                } else {
                    FormValidation.showAlert(null, "لا توجد صورة للشهادة", Alert.AlertType.ERROR);
                }
                rs.close();
            }
        } catch (IOException | SQLException | BadElementException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return image;
    }

    public static byte[] getPdfFile(String circularid, String circulardate) {
        InputStream image = null;
        byte[] pdfByte = null;
        try {
            if (circularid == null || circulardate == null) {
                FormValidation.showAlert(null, "اختر السجل من الجدول", Alert.AlertType.ERROR);
            } else {
                ResultSet rs = DatabaseAccess.getData("SELECT CIRCULARIMAGE FROM arshefdata WHERE CIRCULARID = '" + circularid + "'AND CIRCULARDATE = '" + circulardate + "'");
                if (rs.next()) {
                    image = rs.getBinaryStream("CIRCULARIMAGE");
                    pdfByte = new byte[image.available()];
                    image.read(pdfByte);
                } else {
                    FormValidation.showAlert(null, "لا توجد صورة للشهادة", Alert.AlertType.ERROR);
                }
                rs.close();
            }
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return pdfByte;
    }

    public static int insert(String tapleName, String fildName, String valueNamber, File imagefile) throws IOException {
        int lastId = 0;
        Connection con = DatabaseConniction.dbConnector();
        String guiry = "INSERT INTO " + tapleName + "(" + fildName + ")VALUES(" + valueNamber + ")";
        try {
            PreparedStatement psm = con.prepareStatement(guiry, Statement.RETURN_GENERATED_KEYS);

            if (imagefile != null) {
                FileInputStream fin = new FileInputStream(imagefile);
                int len = (int) imagefile.length();
                psm.setBinaryStream(1, fin, len);
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
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
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
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public static ResultSet getData(String quiry) throws IOException {
        ResultSet rs = null;
        Connection con = DatabaseConniction.dbConnector();
        try {
            PreparedStatement psm = con.prepareStatement(quiry);
            rs = psm.executeQuery();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
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
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
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
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public static ResultSet selectQuiry(String quiry) throws IOException {
        ResultSet rs = null;
        Connection con = DatabaseConniction.dbConnector();
        try {
            PreparedStatement psm = con.prepareStatement(quiry);
            rs = psm.executeQuery();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public static ResultSet getCourses(String miliid) throws IOException {
        ResultSet rs = null;
        Connection con = DatabaseConniction.dbConnector();
        String query = "SELECT personaldata.MILITARYID,personaldata.NAME,personaldata.RANK ,personaldata.UNIT,personaldata.PERSONALID,coursesdata.COURSID,"
                + "coursnames.CORSNAME,coursesdata.COURSNUMBER,coursesdata.COURSPLASE,coursesdata.COURSDURATION,coursesdata.STARTDATE,coursesdata.ENDDATE,coursesdata.COURSESTIMATE FROM personaldata,coursesdata,coursnames "
                + "WHERE personaldata.MILITARYID = '" + miliid + "' AND personaldata.MILITARYID = coursesdata.MILITARYID AND coursesdata.COURSID = coursnames.COURSID ";
        try {
            PreparedStatement psm = con.prepareStatement(query);
            rs = psm.executeQuery();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public static ResultSet getCourses() throws IOException {
        ResultSet rs = null;
        Connection con = DatabaseConniction.dbConnector();
        String query = "SELECT personaldata.MILITARYID,personaldata.NAME,personaldata.RANK ,personaldata.UNIT,personaldata.PERSONALID,coursesdata.COURSID,"
                + "coursnames.CORSNAME,coursesdata.COURSNUMBER,coursesdata.COURSPLASE,coursesdata.COURSDURATION,coursesdata.STARTDATE,coursesdata.ENDDATE,coursesdata.COURSESTIMATE FROM personaldata,coursesdata,coursnames "
                + "WHERE personaldata.MILITARYID = coursesdata.MILITARYID AND coursesdata.COURSID = coursnames.COURSID ";
        try {
            PreparedStatement psm = con.prepareStatement(query);
            rs = psm.executeQuery();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public static ResultSet getIdentiti(String query) throws IOException {
        ResultSet rs = null;
        PreparedStatement psm = null;
        Connection con = DatabaseConniction.dbConnector();
        try {
            psm = con.prepareStatement(query);
            rs = psm.executeQuery();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public static ResultSet getDatabyCoursesId(String coursid) throws IOException {
        ResultSet rs = null;
        Connection con = DatabaseConniction.dbConnector();
        String query = "SELECT personaldata.MILITARYID,personaldata.NAME,personaldata.RANK ,personaldata.UNIT,coursesdata.COURSPLASE FROM personaldata,coursesdata "
                + "WHERE coursesdata.COURSID = '" + coursid + "' AND personaldata.MILITARYID = coursesdata.MILITARYID  ";
        try {
            PreparedStatement psm = con.prepareStatement(query);
            rs = psm.executeQuery();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public static ResultSet getDatabyCoursesPlace(String coursplace) throws IOException {
        ResultSet rs = null;
        Connection con = DatabaseConniction.dbConnector();
        String query = "SELECT personaldata.MILITARYID,personaldata.NAME,personaldata.RANK ,personaldata.UNIT,coursnames.CORSNAME,coursesdata.COURSPLASE FROM personaldata,coursesdata,coursnames "
                + "WHERE coursesdata.COURSPLASE LIKE '" + "%" + coursplace + "%" + "' AND personaldata.MILITARYID = coursesdata.MILITARYID AND coursesdata.COURSID = coursnames.COURSID";
        try {
            PreparedStatement psm = con.prepareStatement(query);
            rs = psm.executeQuery();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public static ResultSet getDatabyCoursesPlaceAndCoursName(String coursplace, String coursid) throws IOException {
        ResultSet rs = null;
        Connection con = DatabaseConniction.dbConnector();
        String query = "SELECT personaldata.MILITARYID,personaldata.NAME,personaldata.RANK ,personaldata.UNIT,coursnames.CORSNAME,coursesdata.COURSPLASE FROM personaldata,coursesdata,coursnames "
                + "WHERE coursesdata.COURSID = '" + coursid + "' AND coursesdata.COURSPLASE LIKE '" + "%" + coursplace + "%" + "' AND personaldata.MILITARYID = coursesdata.MILITARYID AND coursesdata.COURSID = coursnames.COURSID";
        try {
            PreparedStatement psm = con.prepareStatement(query);
            rs = psm.executeQuery();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public static void updat(String tapleName, String fildNameAndValue, String[] data, String condition) throws IOException {
        Connection con = DatabaseConniction.dbConnector();
        String guiry = "UPDATE " + tapleName + " SET " + fildNameAndValue + "WHERE" + " " + condition;
        try {
            PreparedStatement psm = con.prepareStatement(guiry);
            int e = data.length;
            for (int i = 1; i <= e; i++) {
                psm.setString(i, data[i - 1]);
            }
            int t = psm.executeUpdate();
            if (t > 0) {
                FormValidation.showAlert("", "تم تحديث البيانات", Alert.AlertType.CONFIRMATION);
            }
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    public static void updat(String tapleName, String fildNameAndValue, int[] data, String condition) throws IOException {
        Connection con = DatabaseConniction.dbConnector();
        String guiry = "UPDATE " + tapleName + " SET " + fildNameAndValue + " " + "WHERE" + " " + condition;
        try {
            PreparedStatement psm = con.prepareStatement(guiry);
            int e = data.length;
            for (int i = 1; i <= e; i++) {
                psm.setInt(i, data[i - 1]);
            }
            psm.executeUpdate();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    public static void updat(String tapleName, String fildNameAndValue, String condition) throws IOException {
        Connection con = DatabaseConniction.dbConnector();
        String guiry = "UPDATE " + tapleName + " SET " + fildNameAndValue + " WHERE" + condition;
        try {
            PreparedStatement psm = con.prepareStatement(guiry);
            psm.executeUpdate();

        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
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
                psm.setBinaryStream(e + 1, fin, len);
            }
            int t = psm.executeUpdate();
            if (t > 0) {
                FormValidation.showAlert("", "تم تحديث البيانات", Alert.AlertType.CONFIRMATION);
            }
            con.close();
            psm.close();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
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
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    public static void delete(String quiry) throws IOException {
        Connection con = DatabaseConniction.dbConnector();
        try {
            PreparedStatement psm = con.prepareStatement(quiry);
            Alert alert = FormValidation.confirmationDilog("تاكيد الحذف", "سوف يتم حذف السجل هل تريد المتابعة");
            if (alert.getResult() == ButtonType.YES) {
                psm.executeUpdate();
            }
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    public static  ResultSet getSum(String tableName, String[] fieldNames, String condation) {
        ResultSet rs = null;
        PreparedStatement psm = null;
        String quiry = null;
        try {
            Connection con = DatabaseConniction.dbConnector();
            for (int i = 0; i < fieldNames.length; i++) {
                quiry = "SELECT uint,sum(" + fieldNames[i] + ") FROM " + tableName + " where " + condation;
                
            }
            psm = con.prepareStatement(quiry);
            rs = psm.executeQuery();
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }
}
