package controllers;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

public class FillComboBox {

    public static void fillComboBox(ObservableList list, ComboBox com) {
        com.setItems(list);
    }
}
