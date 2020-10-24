
package controllers;

import Validation.FormValidation;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javax.swing.JOptionPane;


public class ShowPdf {
   
    public static void writePdf(Image img) {
        if (img == null) {
            FormValidation.showAlert("", "اختر السجل من الجدول", Alert.AlertType.ERROR);
        } else {
            try {
                Document document = new Document(img);
                File f = new File("C:\\trainingApp");
                f.mkdir();
                String path= f.getPath()+"\\showImage.pdf";
                PdfWriter.getInstance(document, new FileOutputStream(path));
                document.open();
                document.setPageSize(img);
                document.newPage();
                img.setAbsolutePosition(0.0F, 0.0F);
                document.add(img);
                document.close();
                if (Desktop.isDesktopSupported()) {
                    try {
                        File myFile = new File(path);
                        Desktop.getDesktop().open(myFile);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, ex);
                    }
                }
            } catch (FileNotFoundException | DocumentException ex) {
                Logger.getLogger(ShowPdf.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
