package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

public interface InitializClass {

    @FXML
    void close(ActionEvent event);

    @FXML
    void save(ActionEvent event);

    @FXML
    void edit(ActionEvent event);

    @FXML
    void delete(ActionEvent event);

    @FXML
    void clear(ActionEvent event);

    void tableView();

    void refreshTableView();

    void getTableRow(TableView table);

    void getTableRowByInterKey(TableView table);
}
