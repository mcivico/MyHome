package com.infobosccoma.projecte.myhome.Model;

/**
 * Created by Marc on 07/05/2015.
 */
public class Users {

    private String nameUsers, passwordUsers;


    public Users(String name, String password){
        this.nameUsers = name;
        this.passwordUsers = password;
    }


    public String getPassword() {
        return passwordUsers;
    }

    public void setPassword(String password) {
        this.passwordUsers = password;
    }

    public String getName() {
        return nameUsers;
    }

    public void setName(String name) {
        this.nameUsers = name;
    }


}
