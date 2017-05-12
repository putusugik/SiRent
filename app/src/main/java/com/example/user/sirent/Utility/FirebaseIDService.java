package com.example.user.sirent.Utility;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Sugik on 5/3/2017.
 */

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";
    SharedPreferences preferences;
    SharedPreferences.Editor perfEditor;
    @Override
    public void onTokenRefresh() {
        Log.d(TAG, "Test");
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.d(TAG, "Refreshed token: " + refreshedToken);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        perfEditor = preferences.edit();
        perfEditor.putString("token", refreshedToken);
        perfEditor.apply();
    }

}
