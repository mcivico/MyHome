package com.infobosccoma.projecte.myhome.Controller;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Marc on 15/05/2015.
 */
public class FlatSessio {

    private SharedPreferences preferencies;

    private SharedPreferences.Editor editor;

    private Context _context;

    private int PRIVATE_MODE = 0;

    private static final String  PREFER_NAME = "FlatMyHomePref";

    public static final String KEY_NAME = "name";

    public FlatSessio(Context context){
        this._context = context;
        preferencies = _context.getSharedPreferences(PREFER_NAME,PRIVATE_MODE);
        editor = preferencies.edit();
    }

    public void createFlatSession(String name){
        editor.putString(KEY_NAME, name);
        editor.commit();
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> flat = new HashMap<String, String>();

        flat.put(KEY_NAME, preferencies.getString(KEY_NAME,null));

        return flat;
    }

    public void flatOut(){
        editor.clear();
        editor.commit();
    }


}
