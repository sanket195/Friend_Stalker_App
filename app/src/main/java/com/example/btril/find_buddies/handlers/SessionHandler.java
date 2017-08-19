package com.example.btril.find_buddies.handlers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by btril on 07/27/17.
 */


/*Session Handler manages the log in and log out activity of any user of the application */
public class SessionHandler {

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    Context con;

    int MODE = 0;
    private static final String PREFERENCE_NAME = "LoginApiTest";
    private static final String LOGGED_IN = "isLoggedIn";

    public SessionHandler(Context con) {
        this.con = con;
        sp = con.getSharedPreferences(PREFERENCE_NAME, MODE);
        editor = sp.edit();
    }

    public void setLogin(boolean isLoggedin) {
        editor.putBoolean(LOGGED_IN, isLoggedin);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return sp.getBoolean(LOGGED_IN, false);
    }

    public void setPreferenceName(String mailID) {
        editor.putString(PREFERENCE_NAME, mailID);
        editor.commit();
    }

    public String getPreferenceName() {
        return sp.getString(PREFERENCE_NAME, "LoginApiTest");
    }
}
