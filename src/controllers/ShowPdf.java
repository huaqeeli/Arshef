package controllers;

import Validation.FormValidation;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javafx.scene.control.Alert;

public class ShowPdf {

    public static void writePdf(byte[] pdfByte) {
        try {
            File f = new File("C:\\Program Files\\Arshef\\pdf");
            f.mkdir();
            String path = f.getPath() + "\\showPdf.pdf";
            FileOutputStream fout = new FileOutputStream(path);
            DataOutputStream dout = new DataOutputStream(fout);
            dout.write(pdfByte, 0, pdfByte.length);
            fout.close();
            if (Desktop.isDesktopSupported()) {
                try {
                    File myFile = new File(path);
                    Desktop.getDesktop().open(myFile);
                } catch (IOException ex) {
                    FormValidation.showAlert("", ex.toString(), Alert.AlertType.ERROR);
                }
            }
        } catch (IOException ex) {
            FormValidation.showAlert("", ex.toString(), Alert.AlertType.ERROR);
        }
    }

    public static void writePdf(Image img) {
        try {
            if (img != null) {
                Document document = new Document(img);
                File f = new File("C:\\Program Files\\Arshef\\pdf");
                f.mkdir();
                String path = f.getPath() + "\\showImage.pdf";
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
                        FormValidation.showAlert("", ex.toString(), Alert.AlertType.ERROR);
                    }
                }
            }
        } catch (FileNotFoundException | DocumentException ex) {
            FormValidation.showAlert("", ex.toString(), Alert.AlertType.ERROR);
        }
    }
}
