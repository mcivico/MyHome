package com.infobosccoma.projecte.myhome.Controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.infobosccoma.projecte.myhome.Login;

import java.util.HashMap;

/**
 * Created by Marc on 08/05/2015.
 */
public class UsuariSessio {

    private SharedPreferences preferencies;

    private SharedPreferences.Editor editor;

    private Context _context;

    private int PRIVATE_MODE = 0;

    private static final String  PREFER_NAME = "UserMyHomePref";

    private static final String IS_USER_LOGIN = "IsUserLogin";

    public static final String KEY_NAME = "name";

    public static String KEY_PASS = "pass";

    public UsuariSessio(Context context){
        this._context = context;
        preferencies = _context.getSharedPreferences(PREFER_NAME,PRIVATE_MODE);
        editor = preferencies.edit();
    }



    public void createUserLoginSession(String name, String passwd){
        editor.putBoolean(IS_USER_LOGIN,true);

        editor.putString(KEY_NAME, name);

        editor.putString(KEY_PASS,passwd);

        editor.commit();
    }


    public boolean checkLogin(){
        if(!this.isUserLoggedIn()){
            Intent i = new Intent(_context, Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            _context.startActivity(i);

            return false;
        }

        return true;
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();



        user.put(KEY_NAME, preferencies.getString(KEY_NAME, null));

        user.put(KEY_PASS, preferencies.getString(KEY_PASS,null));



        return user;
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        _context.startActivity(i);

    }

    public boolean isUserLoggedIn() {
        return preferencies.getBoolean(IS_USER_LOGIN, false);
    }





}
