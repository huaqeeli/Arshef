package controllers;

import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;

public class AddImagePageController implements Initializable {

    @FXML
    private ProgressBar progressbar;
    double x = 0.0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    private static boolean isDirEmpty(String directory) throws IOException {
        boolean folderStates = false;
        java.nio.file.Path checkIfEmpty = Paths.get(directory);
        DirectoryStream<java.nio.file.Path> ds = Files.newDirectoryStream(checkIfEmpty);
        Iterator files = ds.iterator();
        if (!files.hasNext()) {
            folderStates = true;
        } else {
            folderStates = false;
        }
        return folderStates;
    }

    @FXML
    private void save(ActionEvent event) {
        incresProgrss();
    }

    private void incresProgrss() {
       
        
        for (int i = 0; i < 100; i++) {
             x = x + .1;
             progressbar.setProgress(x);
        }
//        try {
////            while (isDirEmpty("C:\\Users\\ابو ريان\\Pictures\\محل الخدمات\\New folder (3)")) {
//                
//               
//            }
//            System.out.println("It is not empty");
//        } catch (IOException ex) {
//            Logger.getLogger(AddImagePageController.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
}
