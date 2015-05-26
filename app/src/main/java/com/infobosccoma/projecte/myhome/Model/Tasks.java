package com.infobosccoma.projecte.myhome.Model;

/**
 * Created by Marc on 07/05/2015.
 */
public class Tasks {

    private int idTasks, idFlatTask;
    private String nameTask, nomUserTask;

    public Tasks(int idTask, int idFlatTask, String nameTask, String nomUserTask) {
        this.idTasks = idTask;
        this.idFlatTask = idFlatTask;
        this.nameTask = nameTask;
        this.nomUserTask = nomUserTask;
    }

    public int getIdTask() {
        return idTasks;
    }

    public void setIdTask(int idTask) {
        this.idTasks = idTask;
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

    public String getNomUserTask() {
        return nomUserTask;
    }

    public void setNomUserTask(String nomUserTask) {
        this.nomUserTask = nomUserTask;
    }
}
