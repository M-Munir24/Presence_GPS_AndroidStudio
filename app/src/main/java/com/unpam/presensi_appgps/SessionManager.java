package com.unpam.presensi_appgps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


import java.util.HashMap;

public class SessionManager {

    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static  final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String NAME = "NAME";
    public static final String USERNAME = "USERNAME";
    public static final String NEWPASS = "NEWPASS";
    public static final String REGU = "REGU";
    public static final String GRUP = "GRUP";
   // public static final String NEWPASS = "NEWPASS";
    public static final String LEVEL = "LEVEL";
    public static final String GAMBAR = "GAMBAR";
    public static final String ID = "ID";


    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String name, String username, String id_users, String level, String regu, String grup, String gambar){
        editor.putBoolean(LOGIN,true);
        editor.putString(NAME,name);
        editor.putString(USERNAME,username);
        editor.putString(LEVEL,level);
        editor.putString(REGU,regu);
        editor.putString(GRUP,grup);
        editor.putString(GAMBAR,gambar);
        editor.putString(ID, id_users);
        editor.apply();
    }
    public boolean isLogin(){
        return sharedPreferences.getBoolean(LOGIN,false);
    }
    public void checkLogin(){

        if (!this.isLogin()){
            Intent i = new Intent(context, LoginActivity.class);
            context.sendBroadcast(i);
            ((EditActivity)context).finish();
        }
    }

    public HashMap<String, String> getUserDetail() {
        HashMap<String, String> user = new HashMap<>();
        user.put(GAMBAR, sharedPreferences.getString(GAMBAR, null));
        user.put(LEVEL, sharedPreferences.getString(LEVEL, null));
        user.put(NAME, sharedPreferences.getString(NAME, null));
        user.put(REGU, sharedPreferences.getString(REGU, null));
        user.put(GRUP, sharedPreferences.getString(GRUP, null));
        user.put(NEWPASS, sharedPreferences.getString(NEWPASS, null));
        user.put(USERNAME, sharedPreferences.getString(USERNAME,null));
        user.put(ID, sharedPreferences.getString(ID,null));
        return user;
    }

    public void logout() {
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        context.sendBroadcast(i);
        ((EditActivity)context).finish();
    }



}
