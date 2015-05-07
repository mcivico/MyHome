package com.infobosccoma.projecte.myhome.Model;

/**
 * Created by Marc on 07/05/2015.
 */
public class users {

    private String nameUsers, passwordUsers;

    public users(String nameUsers, String passwordUsers) {
        this.nameUsers = nameUsers;
        this.passwordUsers = passwordUsers;
    }

    public String getNameUsers() {
        return nameUsers;
    }

    public void setNameUsers(String nameUsers) {
        this.nameUsers = nameUsers;
    }

    public String getPasswordUsers() {
        return passwordUsers;
    }

    public void setPasswordUsers(String passwordUsers) {
        this.passwordUsers = passwordUsers;
    }
}
