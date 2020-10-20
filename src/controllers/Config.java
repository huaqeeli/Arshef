package controllers;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

public class Config {

    public void getPropValues(String[] data) throws IOException {
        
        try {
            Properties prop = new Properties();
            String propFileName = "config.properties";

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            Date time = new Date(System.currentTimeMillis());

            // get the property value and print it out
            String userName = prop.getProperty("userName");
            String password = prop.getProperty("password");
            String dbName = prop.getProperty("dbName");
            String hostName = prop.getProperty("hostName");

            data[0] = userName;
            data[1] = password;
            data[2] = dbName;
            data[3] = hostName;
        } catch (IOException e) {
            System.out.println("Exception: " + e);
        }
    }
}
