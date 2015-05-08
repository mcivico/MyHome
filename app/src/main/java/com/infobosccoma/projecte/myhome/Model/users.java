package com.infobosccoma.projecte.myhome.Model;

/**
 * Created by Marc on 07/05/2015.
 */
public class users {

    private String nameUsers, passwordUser;

    public users(String nameUsers, String passwordUser) {
        this.nameUsers = nameUsers;
        this.passwordUser = passwordUser;
    }

    public String getNameUsers() {
        return nameUsers;
    }

    public void setNameUsers(String nameUsers) {
        this.nameUsers = nameUsers;
    }

    public String getPasswordUsers() {
        return passwordUser;
    }

    public void setPasswordUsers(String passwordUsers) {
        this.passwordUser = passwordUsers;
    }
}
