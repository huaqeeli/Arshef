package modeles;

import controllers.DatabaseAccess;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserModel {

    String militaryid, name, nationalid, usertype;
    int squnce;
    static int saveCount, editCount, deleteCount;

    public UserModel(String militaryid, String name, String nationalid, String usertype, int squnce) {
        this.militaryid = militaryid;
        this.name = name;
        this.nationalid = nationalid;
        this.usertype = usertype;
        this.squnce = squnce;
    }

    public String getMilitaryid() {
        return militaryid;
    }

    public void setMilitaryid(String militaryid) {
        this.militaryid = militaryid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationalid() {
        return nationalid;
    }

    public void setNationalid(String nationalid) {
        this.nationalid = nationalid;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public int getSqunce() {
        return squnce;
    }

    public void setSqunce(int squnce) {
        this.squnce = squnce;
    }

    public static int getSaveCount(String militaryId) {
        try {
            ResultSet rs = DatabaseAccess.select("userdata", "MILITARYID = '" + militaryId + "'");
            if (rs.next()) {
                saveCount = rs.getInt("SAVECOUNT");
            }
        } catch (IOException | SQLException ex) {
            Logger.getLogger(UserModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return saveCount;
    }

    public static void setSaveCount(int saveCount) {
        UserModel.saveCount = saveCount;
    }

    public static int getEditCount(String militaryId) {
        try {
            ResultSet rs = DatabaseAccess.select("userdata", "MILITARYID = '" + militaryId + "'");
            if (rs.next()) {
                editCount = rs.getInt("EDITCOUNT");
            }
        } catch (IOException | SQLException ex) {
            Logger.getLogger(UserModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return editCount;
    }

    public static void setEditCount(int editCount) {
        UserModel.editCount = editCount;
    }

    public static int getDeleteCount(String militaryId) {
        try {
            ResultSet rs = DatabaseAccess.select("userdata", "MILITARYID = '" + militaryId + "'");
            if (rs.next()) {
                deleteCount = rs.getInt("DELETECOUNT");
            }
        } catch (IOException | SQLException ex) {
            Logger.getLogger(UserModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return deleteCount;
    }

    public static void setDeleteCount(int deleteCount) {
        UserModel.deleteCount = deleteCount;
    }

    public static void incrementSavCount(String militaryId) {
        try {
            setSaveCount(getSaveCount(militaryId));
            saveCount = saveCount + 1;
            int[] data = {saveCount};
            DatabaseAccess.updat("userdata", "`SAVECOUNT`=?", data, "MILITARYID = '" + militaryId + "'");
        } catch (IOException ex) {
            Logger.getLogger(UserModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void incrementEditCount(String militaryId) {
        try {
            setEditCount(getEditCount(militaryId));
            editCount = editCount + 1;
            int[] data = {editCount};
            DatabaseAccess.updat("userdata", "`EDITCOUNT`=?", data, "MILITARYID = '" + militaryId + "'");
        } catch (IOException ex) {
            Logger.getLogger(UserModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void incrementDeleteCount(String militaryId) {
        try {
            setDeleteCount(getDeleteCount(militaryId));
            deleteCount = deleteCount + 1;
            int[] data = {deleteCount};
            DatabaseAccess.updat("userdata", "`DELETECOUNT`=?", data, "MILITARYID = '" + militaryId + "'");
        } catch (IOException ex) {
            Logger.getLogger(UserModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String toString() {
        return "UserModel{" + "militaryid=" + militaryid + ", name=" + name + ", nationalid=" + nationalid + ", usertype=" + usertype + ", squnce=" + squnce + '}';
    }

}
