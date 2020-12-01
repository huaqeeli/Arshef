
package controllers;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;


public class DatabaseConniction {
    private static Connection con;
    InputStream inputStream;
    static Config config = new Config();
    static String[] data = new String[5];

    public static Connection dbConnector() throws IOException {
        config.getPropValues(data);
        String userName = data[0];
        String password = data[1];
        String dbName = data[2];
        String hostName = data[3];
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://"+hostName+":3306/"+dbName+"?useUnicode=true&characterEncoding=utf-8", userName, password);
//            con = DriverManager.getConnection("jdbc:mysql://maylocalehost:3307/"+dbName+"?useUnicode=true&characterEncoding=utf-8", userName, password);
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        return con;
    }
}
