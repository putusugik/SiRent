package com.example.user.sirent;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Sugik on 11/11/2016.
 */

public class SharedPref {

    private static final String PREF_NAME = "SIRent";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_IS_LOGGEDINADMIN = "isLoggedInAdmin";
    private static final String KEY_ID_USER = "idUser";
    private static final String NAME_ID_USER = "nameUser";
    private static final String KEY_TOKO = "idToko";

    // Shared Preferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    public SharedPref(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        // commit changes
        editor.commit();
    }

    public void setLoginAdmin (boolean isLoggedInAdmin){
        editor.putBoolean(KEY_IS_LOGGEDINADMIN, isLoggedInAdmin);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public boolean isLoggedInAdmin(){
        return  pref.getBoolean(KEY_IS_LOGGEDINADMIN, false);
    }

    public void setUserID(int idUser)
    {
        editor.putInt(KEY_ID_USER, idUser);
        //editor.putString(NAME_ID_USER, nameUser);
        // commit changes
        editor.commit();
    }
    public void setTokoID (int idToko){
        editor.putInt(KEY_TOKO, idToko);
        editor.commit();
    }
    public int getTokoID(){
        return pref.getInt(KEY_TOKO, -1);
    }

    public int getUserID()
    {
        return pref.getInt(KEY_ID_USER, -1);
    }
    public String getNameIdUser(){
        return pref.getString(NAME_ID_USER, "nama_user");
    }
}
