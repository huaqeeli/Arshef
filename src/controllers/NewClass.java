package controllers;

import java.io.IOException;

public class NewClass {

    public static void main(String[] args) throws IOException {
        String unet = "سرية الحماية الثالثة";
        DatabaseAccess.delete(" DELETE FROM personaldata WHERE UNIT !='"+unet+"' ");
    }/*
    DELETE coursesdata, personaldata FROM coursesdata inner join personaldata
`coursesdata`.MILITARYID = `personaldata`.MILITARYID WHERE `personaldata`.UNIT = 'سرية الحماية الثالثة'
     */
}
