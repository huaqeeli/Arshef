package controllers;

import Validation.FormValidation;
import com.asprise.imaging.core.Imaging;
import com.asprise.imaging.core.Request;
import com.asprise.imaging.core.Result;
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

    public static void insert(String tapleName, String fildName, String valueNamber, int data) throws IOException {
        Connection con = DatabaseConniction.dbConnector();
        String guiry = "INSERT INTO " + tapleName + "(" + fildName + ")VALUES(" + valueNamber + " )";
        try {
            PreparedStatement psm = con.prepareStatement(guiry);
            psm.setInt(1, data);
            int t = psm.executeUpdate();
            if (t > 0) {
            } else {
                JOptionPane.showMessageDialog(null, "حدث خطاء في عملية الحفظ الرجاء المحاولة مرة اخرى");
            }
            con.close();
            psm.close();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
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
                ResultSet rs = DatabaseAccess.getData("SELECT COURSIMAGE FROM externalincoming WHERE CIRCULARID = '" + militaryid + "'AND CIRCULARDATE = '" + coursid + "'");
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
                ResultSet rs = DatabaseAccess.getData("SELECT IMAGE FROM externalincoming WHERE CIRCULARID = '" + circularid + "'AND CIRCULARDATE = '" + circulardate + "'");
                if (rs.next()) {
                    image = rs.getBinaryStream("IMAGE");
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

    public static byte[] getExportPdfFile(String id, String entrydate) {
        InputStream image = null;
        byte[] pdfByte = null;
        try {
            if (id == null || entrydate == null) {
                FormValidation.showAlert(null, "اختر السجل من الجدول", Alert.AlertType.ERROR);
            } else {
                ResultSet rs = DatabaseAccess.getData("SELECT IMAGE FROM exportsdata WHERE ID = '" + id + "'AND ENTRYDATE = '" + entrydate + "'");
                if (rs.next()) {
                    image = rs.getBinaryStream("IMAGE");
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

    public static byte[] getInternalIncomingPdfFile(String regisid, String year) {
        InputStream image = null;
        byte[] pdfByte = null;
        try {
            if (regisid == null || year == null) {
                FormValidation.showAlert(null, "اختر السجل من الجدول", Alert.AlertType.ERROR);
            } else {
                ResultSet rs = DatabaseAccess.getData("SELECT IMAGE FROM internalincoming WHERE REGIS_NO = '" + regisid + "'AND RECORD_YEAR = '" + year + "'");
                if (rs.next()) {
                    image = rs.getBinaryStream("IMAGE");
                    pdfByte = new byte[image.available()];
                    image.read(pdfByte);
                } else {
                    FormValidation.showAlert(null, "لا توجد صورة ", Alert.AlertType.ERROR);
                }
                rs.close();
            }
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return pdfByte;
    }

    public static byte[] getSecretPdfFile(String regisid, String year) {
        InputStream image = null;
        byte[] pdfByte = null;
        try {
            if (regisid == null || year == null) {
                FormValidation.showAlert(null, "اختر السجل من الجدول", Alert.AlertType.ERROR);
            } else {
                ResultSet rs = DatabaseAccess.getData("SELECT IMAGE FROM secretdata WHERE CIRCULARID = '" + regisid + "'AND RECORDYEAR = '" + year + "'");
                if (rs.next()) {
                    image = rs.getBinaryStream("IMAGE");
                    pdfByte = new byte[image.available()];
                    image.read(pdfByte);
                } else {
                    FormValidation.showAlert(null, "لا توجد صورة ", Alert.AlertType.ERROR);
                }
                rs.close();
            }
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return pdfByte;
    }

    public static byte[] getInternalExportPdfFile(String regisid, String year) {
        InputStream image = null;
        byte[] pdfByte = null;
        try {
            if (regisid == null || year == null) {
                FormValidation.showAlert(null, "اختر السجل من الجدول", Alert.AlertType.ERROR);
            } else {
                ResultSet rs = DatabaseAccess.getData("SELECT IMAGE FROM internalexports WHERE REGISNO = '" + regisid + "'AND RECORDYEAR = '" + year + "'");
                if (rs.next()) {
                    image = rs.getBinaryStream("IMAGE");
                    pdfByte = new byte[image.available()];
                    image.read(pdfByte);
                } else {
                    FormValidation.showAlert(null, "لا توجد صورة ", Alert.AlertType.ERROR);
                }
                rs.close();
            }
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, "لا توجد صورة ", Alert.AlertType.ERROR);
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

    public static String getUintName() throws IOException {
        ResultSet rs = null;
        String unitName = null;
        String guiry = "SELECT * FROM uintname";
        Connection con = DatabaseConniction.dbConnector();
        try {
            PreparedStatement psm = con.prepareStatement(guiry);
            rs = psm.executeQuery();
            if (rs.next()) {
                unitName = rs.getString("uintname");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        return unitName;
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

    public static String getRegistrationNum() throws IOException {
        ResultSet rs = null;
        String recordSquinc = null;
        Connection con = DatabaseConniction.dbConnector();
        String quiry = "SELECT REGISTRATION_NUM FROM recordesquins ";
        try {
            PreparedStatement psm = con.prepareStatement(quiry);
            rs = psm.executeQuery();
            if (rs.next()) {
                recordSquinc = rs.getString("REGISTRATION_NUM");
            }
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return recordSquinc;
    }

    public static void updatRegistrationNum() throws IOException {
        Connection con = DatabaseConniction.dbConnector();
        String guiry = "UPDATE recordesquins SET REGISTRATION_NUM = ? WHERE ID = 1";
        try {
            PreparedStatement psm = con.prepareStatement(guiry);
            int newNum = Integer.parseInt(getRegistrationNum()) + 1;
            psm.setInt(1, newNum);
            psm.executeUpdate();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }

    public static void updatRegistrationNum(int newid, String newYear) throws IOException {
        Connection con = DatabaseConniction.dbConnector();
        String guiry = "UPDATE recordesquins SET REGISTRATION_NUM = ?,SQUINS_YEAR = ? WHERE ID = 1";
        try {
            PreparedStatement psm = con.prepareStatement(guiry);
            psm.setInt(1, newid);
            psm.setString(2, newYear);
            psm.executeUpdate();
        } catch (SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
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

    public static void updat(String tapleName, String fildNameAndValue, String[] data, String condition) throws IOException {
        Connection con = DatabaseConniction.dbConnector();
        String guiry = "UPDATE " + tapleName + " SET " + fildNameAndValue + " " + "WHERE" + " " + condition;
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
        String guiry = "UPDATE " + tapleName + " SET " + fildNameAndValue + " WHERE" + " " + condition;
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

    public static ResultSet getSum(String tableName, String condation) {
        ResultSet rs = null;
        PreparedStatement psm = null;
        String quiry = null;
        quiry = "SELECT sum(originalCensus), sum(currentCensus), sum(OrdinaryVacation), sum(OccasionalVacation), sum(Sickleave), sum(Quarantine), sum(InareaTraining),"
                + " sum(OutareaTraining), sum(OutKingdomTraining), sum(OfficialMission), sum(JoblMission), sum(hospital), sum(outKingdomJob), sum(outOftheForce), sum(alternates),"
                + "sum(administrativeleave), sum(late), sum(Absence), sum(Prison) FROM " + tableName + " where " + condation;
        try {
            Connection con = DatabaseConniction.dbConnector();
            psm = con.prepareStatement(quiry);
            rs = psm.executeQuery();
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public static ResultSet getManualSum(String tableName, String condation) {
        ResultSet rs = null;
        PreparedStatement psm = null;
        String quiry = null;//`originalCensusOF`,`originalCensusSR`,`infieldOF`,`infieldSR`,`outfiedOF`,`outfieldSR`
        quiry = "SELECT sum(originalCensus), sum(currentCensus) FROM " + tableName + " where " + condation;
        try {
            Connection con = DatabaseConniction.dbConnector();
            psm = con.prepareStatement(quiry);
            rs = psm.executeQuery();
        } catch (IOException | SQLException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
        return rs;
    }

    public static void insertImage(String tapleName, String condition) throws IOException {
        try {
            Imaging imaging = new Imaging("arshef", 0);
            String path = config.getImagePath();
            Result result = imaging.scan(Request.fromJson(
                    "{"
                    + "\"output_settings\" : [ {"
                    + "  \"type\" : \"save\","
                    + "  \"format\" : \"pdf\","
                    + "  \"save_path\" : \"" + path + "\\\\${TMS}${EXT}\""
                    + "} ]"
                    + "}"), "select", false, false);

            File pdfFile = result.getPdfFile();
            FileInputStream fis = new FileInputStream(pdfFile);
            Connection con = DatabaseConniction.dbConnector();
            String quiry = "UPDATE " + tapleName + " SET `IMAGE` =? WHERE " + " " + condition;
            try {
                PreparedStatement psm = con.prepareStatement(quiry);
                psm.setBinaryStream(1, fis, (int) (pdfFile.length()));
                int t = psm.executeUpdate();
                if (t > 0) {
                } else {
                    FormValidation.showAlert(null, "حدث خطاء في عملية الحفظ الرجاء المحاولة مرة اخرى", Alert.AlertType.ERROR);
                }
                con.close();
                psm.close();
                fis.close();
            } catch (SQLException ex) {
                FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
            }

        } catch (IOException ex) {
            FormValidation.showAlert(null, ex.toString(), Alert.AlertType.ERROR);
        }
    }
}
