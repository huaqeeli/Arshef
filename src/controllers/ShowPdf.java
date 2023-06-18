package controllers;

import Validation.FormValidation;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javafx.scene.control.Alert;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

public class ShowPdf {

    static Config config = new Config();

    public static void writePdf(byte[] pdfByte) {
        try {
            File f = new File(config.getAppURL() + "\\pdfFiles");
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

    public static void getClashahImage(byte[] pdfByte) {
        try {
            File f = new File(config.getAppURL() + "\\pdfFiles");
            f.mkdir();
            String path = f.getPath() + "\\showPdf.pdf";
            FileOutputStream fout = new FileOutputStream(path);
            DataOutputStream dout = new DataOutputStream(fout);
            dout.write(pdfByte, 0, pdfByte.length);
            fout.close();
            File newFile = new File(path);
            PDDocument pdfDocument = PDDocument.load(newFile);
            PDFRenderer pdfRenderer = new PDFRenderer(pdfDocument);
            BufferedImage img = pdfRenderer.renderImage(0);
            ImageIO.write(img, "PNG", new File(f.getPath() + "\\showPdf.png"));
            pdfDocument.close();
        } catch (IOException ex) {
            FormValidation.showAlert("", ex.toString(), Alert.AlertType.ERROR);
        }
    }

    public static void writePdf(Image img) {
        try {
            if (img != null) {
                Document document = new Document(img);
                File f = new File(config.getAppURL() + "\\pdfFiles");
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
        } catch (IOException ex) {
            FormValidation.showAlert("", ex.toString(), Alert.AlertType.ERROR);
        }
    }
}
