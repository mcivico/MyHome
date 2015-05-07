package com.infobosccoma.projecte.myhome.Model;

/**
 * Created by Marc on 07/05/2015.
 */
public class FlatUser {

    private int idFlat;
    private String nameUser;

    public FlatUser(int idFlat, String nameUser) {
        this.idFlat = idFlat;
        this.nameUser = nameUser;
    }

    public int getIdFlat() {
        return idFlat;
    }

    public void setIdFlat(int idFlat) {
        this.idFlat = idFlat;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }
}
