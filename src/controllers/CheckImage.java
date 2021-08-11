package controllers;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CheckImage {

    public static void main(String args[]) {
        try {
            //        File d = new File("C:\\Users\\ابو ريان\\Pictures\\محل الخدمات\\New folder (3)");
//        String path = d.getAbsolutePath();
//        File dir = new File(path);
//        File[] files = dir.listFiles();
//        if (!dir.exists()) {
//            System.out.println("Directory is Empty");
//        } else {
//            for (File file : files) {
//                System.out.println(file);
//            }
//        }

            Path checkIfEmpty = Paths.get("C:\\Users\\ابو ريان\\Pictures\\محل الخدمات\\New folder (3)");
            DirectoryStream<Path> ds = Files.newDirectoryStream(checkIfEmpty);
            Iterator files = ds.iterator();
            if (!files.hasNext()) {
                System.out.println("Directory is Empty");
//                Files.deleteIfExists(Paths.get(checkIfEmpty.toString()));
            } else {
                    System.out.println(files);
            }
        } catch (IOException ex) {
            Logger.getLogger(CheckImage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
