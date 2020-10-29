package controllers;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

public class ExporteExcelSheet {

    ResultSet rs;
    String[] feild;
    String[] titel;
    String[] personaltitel;
    String[] personalData;

    public ExporteExcelSheet(ResultSet rs, String[] feild, String[] titel) {
        this.rs = rs;
        this.feild = feild;
        this.titel = titel;
    }

    public ExporteExcelSheet(ResultSet rs, String[] feild, String[] titel, String[] personaltitel, String[] personalData) {
        this.rs = rs;
        this.feild = feild;
        this.titel = titel;
        this.personaltitel = personaltitel;
        this.personalData = personalData;
    }

    public ArrayList<Object[]> getTableData() throws IOException {
        ArrayList<Object[]> tableDataList = null;
        tableDataList = new ArrayList<>();
        try {
            while (rs.next()) {
                Object[] objArray = new Object[feild.length];
                for (int i = 0; i < feild.length; i++) {
                    objArray[i] = rs.getString(feild[i]);
                }
                tableDataList.add(objArray);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ExporteExcelSheet.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tableDataList;
    }

    public void setHeserStyle(CellStyle headerstyle) {
        headerstyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        headerstyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerstyle.setAlignment(HorizontalAlignment.CENTER);
        headerstyle.setBorderBottom((short) 2);
        headerstyle.setBorderTop((short) 2);
        headerstyle.setBorderRight((short) 2);
        headerstyle.setBorderLeft((short) 2);
    }
    public void setContentStyle(CellStyle headerstyle) {
        headerstyle.setAlignment(HorizontalAlignment.CENTER);
        headerstyle.setBorderBottom((short) 2);
        headerstyle.setBorderTop((short) 2);
        headerstyle.setBorderRight((short) 2);
        headerstyle.setBorderLeft((short) 2);
    }
    
    public void ceratHeader(HSSFRow row,int rownum){
    
    }
    public void ceratContent(){}

    public void doExport(ArrayList<Object[]> dataList, String fileName) {
        if (dataList != null && !dataList.isEmpty()) {
            HSSFWorkbook workBook = new HSSFWorkbook();
            HSSFSheet sheet = workBook.createSheet();
            sheet.setRightToLeft(true);
            sheet.setDefaultColumnWidth(20);
            

            CellStyle headerstyle = workBook.createCellStyle();
            headerstyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            headerstyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            headerstyle.setAlignment(HorizontalAlignment.CENTER);
            headerstyle.setBorderBottom((short) 2);
            headerstyle.setBorderTop((short) 2);
            headerstyle.setBorderRight((short) 2);
            headerstyle.setBorderLeft((short) 2);

            CellStyle contentstyle = workBook.createCellStyle();
            contentstyle.setAlignment(HorizontalAlignment.CENTER);
            contentstyle.setBorderBottom((short) 2);
            contentstyle.setBorderTop((short) 2);
            contentstyle.setBorderRight((short) 2);
            contentstyle.setBorderLeft((short) 2);
            
            HSSFRow personaltitelRow = sheet.createRow(0);
            HSSFRow personaldataRow = sheet.createRow(1);
            for (int i = 0; i < personaltitel.length; i++) {
                HSSFCell cell = personaltitelRow.createCell((short) i);
                cell.setCellValue(personaltitel[i]);
                cell.setCellStyle(headerstyle);
            }
            for (int i = 0; i < personalData.length; i++) {
                HSSFCell cell = personaldataRow.createCell((short) i);
                cell.setCellValue(personalData[i]);
                cell.setCellStyle(contentstyle);
            }
            HSSFRow headingRow = sheet.createRow(2);
            for (int i = 0; i < titel.length; i++) {
                HSSFCell cell = headingRow.createCell((short) i);
                cell.setCellValue(titel[i]);
                cell.setCellStyle(headerstyle);
            }
            short rowNo = 3;
            for (Object[] objects : dataList) {
                HSSFRow row = sheet.createRow(rowNo);
                for (int i = 0; i < feild.length; i++) {
                    HSSFCell cell = row.createCell((short) i);
                    cell.setCellValue(objects[i].toString());
                    cell.setCellStyle(contentstyle);
                }
                rowNo++;
            }

            String file = fileName + ".xls";
            try {
                FileOutputStream fos = new FileOutputStream(file);
                workBook.write(fos);
                fos.flush();
            } catch (FileNotFoundException e) {
                System.out.println("Invalid directory or file not found");
            } catch (IOException e) {
                System.out.println("Error occurred while writting excel file to directory");
            }
        }
    }
}
