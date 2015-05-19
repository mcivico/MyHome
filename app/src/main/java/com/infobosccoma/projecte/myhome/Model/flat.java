package com.infobosccoma.projecte.myhome.Model;

import java.io.Serializable;

/**
 * Created by Marc on 07/05/2015.
 */
public class flat implements Serializable {

    //private int idFlat;
    private String nameFlat/*, passwordFlat*/;


    /*public flat(int idFlat, String nameFlat, String passwordFlat) {

        this.idFlat = idFlat;
        this.nameFlat = nameFlat;
        this.passwordFlat = passwordFlat;
    }*/

    public flat(String nameFlat){
        this.nameFlat= nameFlat;
    }

/*
    public int getIdFlat() {
        return idFlat;
    }

    public void setIdFlat(int idFlat) {
        this.idFlat = idFlat;
    }*/

    public String getNameFlat() {
        return nameFlat;
    }

    public void setNameFlat(String nameFlat) {
        this.nameFlat = nameFlat;
    }
/*
    public String getPasswordFlat() {
        return passwordFlat;
    }

    public void setPasswordFlat(String passwordFlat) {
        this.passwordFlat = passwordFlat;
    }
*/

    @Override
    public String toString() {
        return nameFlat;
    }
}
