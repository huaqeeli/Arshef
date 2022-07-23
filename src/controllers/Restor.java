package controllers;

import java.io.File;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class Restor extends Service {

    private File backupfile;

    public Restor(File backupfile) {
        this.backupfile = backupfile;
    }

    @Override
    protected Task createTask() {
        return new RestorTask(backupfile);
    }

}
