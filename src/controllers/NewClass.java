package controllers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NewClass {

    public static void main(String[] args) throws IOException, SQLException {
        ResultSet rs = DatabaseAccess.selectQuiry("SELECT  internalexports.REGISNO, circularnames.CIRCULARID, circularnames.MILITARYID,circularnames.ID, circularnames.YEAR, circularnames.type "
                + "FROM circularnames,internalexports"
                + " WHERE circularnames.CIRCULARID =  internalexports.REGISNO");
        if (rs.next()) {
            DatabaseAccess.updat("circularnames", "type = 'internalexports'", "CIRCULARID = '"+rs.getString("CIRCULARID")+"'");
        }
    }
   

}
