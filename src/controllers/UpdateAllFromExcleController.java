package controllers;

import Validation.FormValidation;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;

public class UpdateAllFromExcleController implements Initializable {

    @FXML
    private VBox vbox;
    @FXML
    private ListView<String> showArea;
    @FXML
    private TextField excleFileUrl;
    File execlfile = null;
    @FXML
    private ProgressIndicator progress;
    @FXML
    private ColorPicker color;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private File getExcleFile(ActionEvent event) {

        Window stage = null;
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter ext1 = new FileChooser.ExtensionFilter("Excel files(*.xls)", "*.XLS");
        fileChooser.getExtensionFilters().addAll(ext1);
        execlfile = fileChooser.showOpenDialog(stage);
        excleFileUrl.setText(execlfile.getPath());
        return execlfile;
    }

    @FXML
    private void updateFromjExcle(ActionEvent event) throws IOException {
        Alert alert = FormValidation.confirmationDilog("تنبيه", "يجب ان يكون ترتيب ملف الاكسل كتالي :" + "\n" + "الرقم العسكري - الرتبة - الاسم - رقم الهوية - الوحدة" + "\n" + "هل تريد المتابعة ؟");
        if (alert.getResult() == ButtonType.YES) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(execlfile);
                HSSFWorkbook workbook = new HSSFWorkbook(fis);
                HSSFSheet sheet = workbook.getSheetAt(0);
                Iterator rows = sheet.rowIterator();
                int t = 0;
                while (rows.hasNext()) {
                    HSSFRow row = (HSSFRow) rows.next();
                    Iterator cells = row.cellIterator();
                    List data = new ArrayList();
                    while (cells.hasNext()) {
                        HSSFCell cell = (HSSFCell) cells.next();
                        cell.setCellType(CellType.STRING);
                        data.add(cell);
                    }
                    String militryid = data.get(0).toString();
                    String rank = data.get(1).toString();
                    String name = data.get(2).toString();
                    String personalid = data.get(3).toString();
                    String unit = data.get(4).toString();
                    String specialty = data.get(5).toString();
                    String[] updatdata = {name, rank, personalid,unit, specialty};
                    String[] insertdata = {militryid, personalid, name, rank,unit, specialty};
                    boolean milataryidExisting = FormValidation.ifNotexisting("personaldata", "MILITARYID", "MILITARYID='" + militryid + "'");
                    double pr = 0;
                    if (milataryidExisting) {
                        showArea.getItems().add(updatdata[1] + "         " + updatdata[0]);
                        t = DatabaseAccess.updat("personaldata", "`NAME`=?,`RANK`=?,`PERSONALID`=?,`UNIT`=?,`SPECIALTY`=?", updatdata, "MILITARYID='" + militryid + "'");
                    } else {
                        showArea.getItems().add(updatdata[1] + " | " + updatdata[0]);
                        t = DatabaseAccess.insert("personaldata", "`MILITARYID`,`PERSONALID`,`NAME`,`RANK`,`UNIT`,`SPECIALTY`", "?,?,?,?,?,?", insertdata);
                    }
                }
                if (t > 0) {
                    FormValidation.showAlert(null, "تم تحديث البيانات", Alert.AlertType.INFORMATION);
                }
            } catch (IOException e) {
                FormValidation.showAlert(null, e.toString(), Alert.AlertType.ERROR);
            } finally {
                if (fis != null) {
                    fis.close();
                }
            }
        }
    }

}
