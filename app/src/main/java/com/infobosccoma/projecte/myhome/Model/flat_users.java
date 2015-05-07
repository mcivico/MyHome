package com.infobosccoma.projecte.myhome.Model;

/**
 * Created by Marc on 07/05/2015.
 */
public class flat_users {

    private int idFlat_Users;
    private String nameUserFlat;

    public flat_users(int idFlat_Users, String nameUserFlat) {
        this.idFlat_Users = idFlat_Users;
        this.nameUserFlat = nameUserFlat;
    }

    public int getIdFlat_Users() {
        return idFlat_Users;
    }

    public void setIdFlat_Users(int idFlat_Users) {
        this.idFlat_Users = idFlat_Users;
    }

    public String getNameUserFlat() {
        return nameUserFlat;
    }

    public void setNameUserFlat(String nameUserFlat) {
        this.nameUserFlat = nameUserFlat;
    }
}
