package com.infobosccoma.projecte.myhome.Model;

/**
 * Created by Marc on 08/05/2015.
 */
public class tasksFlat {

    private int idTasks, idFlatTask;
    private String nameTask;

    public tasksFlat(int idTasks, int idFlatTask, String nameTask) {
        this.idTasks = idTasks;
        this.idFlatTask = idFlatTask;
        this.nameTask = nameTask;
    }

    public int getIdTasks() {
        return idTasks;
    }

    public void setIdTasks(int idTasks) {
        this.idTasks = idTasks;
    }

    public int getIdFlatTask() {
        return idFlatTask;
    }

    public void setIdFlatTask(int idFlatTask) {
        this.idFlatTask = idFlatTask;
    }

    public String getNameTask() {
        return nameTask;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }
}
