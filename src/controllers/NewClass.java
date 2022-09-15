package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewClass {

    public static void main(String[] args) throws IOException, SQLException {
       List<String> strList = new ArrayList<String>();
        strList.add("A");
        strList.add("C");
        strList.add("B");
        strList.add("Z");
        strList.add("E");
        //using Collections.sort() to sort ArrayList
        Collections.sort(strList);
        for(String str: strList) System.out.print(" "+str);
    }
}
