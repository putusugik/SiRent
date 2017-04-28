package com.example.user.sirent.Utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.user.sirent.Admin.Dashboard_Admin;
import com.example.user.sirent.User.Dashboard;
import com.example.user.sirent.JSON.JSONParser;
import com.example.user.sirent.User.Login;
import com.example.user.sirent.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

//import android.support.annotation.StringRes;

/**
 * Created by User on 11/5/2016.
 */

public class Backtask extends AsyncTask<String, Void, JSONObject> {
    Context context;
    AlertDialog alertDialog;
    public Backtask(Context ctx){
        context = ctx;
        jsonParser = new JSONParser();
        sharedPref = new SharedPref(ctx);
    }
    String methode;
    JSONParser jsonParser;
    SharedPref sharedPref;
    private static final String TAG_SUCCESS = "sukses";
    private static final String TAG_MESSAGE = "pesan";
    private static final String TAG_ID_USER = "ID";
    private static final String TAG_NAME_USER = "nama_user";

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        methode = params[0];
        String type =  params[0];
        String login_url = "http://sirent.esy.es/login-coba.php";
        String regis_url = "http://sirent.esy.es/register_user.php";
        String insertkend_url = "http://sirent.esy.es/insert_kendaraan.php";
        if (type.equals("login")){
            List<NameValuePair> paket = new ArrayList<NameValuePair>();
            paket.add(new BasicNameValuePair("user_name", params[1]));
            paket.add(new BasicNameValuePair("user_pass", params[2]));

            return jsonParser.makeHttpRequest(login_url, "POST", paket);
        }
        if (type.equals("register")){
            List<NameValuePair> paket = new ArrayList<NameValuePair>();
            paket.add(new BasicNameValuePair("nama_depan", params[1]));
            paket.add(new BasicNameValuePair("nama_belakang", params[2]));
            paket.add(new BasicNameValuePair("email", params[3]));
            paket.add(new BasicNameValuePair("user_name", params[4]));
            paket.add(new BasicNameValuePair("user_pass", params[5]));

            return jsonParser.makeHttpRequest(regis_url, "POST", paket);
        }
        if (type.equals("insertKend")){
            List<NameValuePair> paket = new ArrayList<NameValuePair>();
            paket.add(new BasicNameValuePair("nama_kendaraan", params[1]));
            paket.add(new BasicNameValuePair("no_mesin", params[2]));
            paket.add(new BasicNameValuePair("no_plat", params[3]));

            return jsonParser.makeHttpRequest(insertkend_url, "POST", paket);
        }
        return null;
    }



    @Override
    protected void onPostExecute(JSONObject result) {
        try
        {
            if (methode.equals("login"))
            {
                if(result.getInt("sukses") == 1)
                {
                    if (result.getInt("status") == 1) {
                        sharedPref.setLogin(true);
                        sharedPref.setUserID(result.getInt("ID"));
                        Intent intent = new Intent(context, Dashboard.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(intent);
                    } else if (result.getInt("status") == 2){
                        sharedPref.setLoginAdmin(true);
                        sharedPref.setUserID(result.getInt("ID"));
                        sharedPref.setTokoID(result.getInt("id_toko"));
                        Intent intent = new Intent(context, Dashboard_Admin.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(intent);
                    }

                } else if (result.getInt("sukses")==0){
                    Toast.makeText(context, "Username/Password anda salah", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, Login.class);
                    context.startActivity(i);
                }
            } else if (methode.equals("register")){
                if(result.getInt("sukses") == 1)
                {
                    Intent intent = new Intent(context, Login.class);
                    context.startActivity(intent);
                }
            }
        }
        catch (JSONException e)
        {
            Log.d("jsonobjecterror", "onPostExecute: " + e.getMessage());
        }

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}
