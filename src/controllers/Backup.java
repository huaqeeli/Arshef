package controllers;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class Backup extends Service {

    private String savefile;

    public Backup(String savefile) {
        this.savefile = savefile;
    }

    @Override
    protected Task createTask() {
        return new BackupTask(savefile);
    }

}
