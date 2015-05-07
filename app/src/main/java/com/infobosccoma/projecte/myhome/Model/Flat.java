package com.infobosccoma.projecte.myhome.Model;

/**
 * Created by Marc on 07/05/2015.
 */
public class Flat {
    private int idFlat;
    private String name, password;

    public Flat(int idFlat, String name, String password) {
        this.idFlat = idFlat;
        this.name = name;
        this.password = password;
    }

    public int getIdFlat() {
        return idFlat;
    }

    public void setIdFlat(int idFlat) {
        this.idFlat = idFlat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
