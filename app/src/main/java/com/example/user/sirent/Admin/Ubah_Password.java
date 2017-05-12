package com.example.user.sirent.Admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.sirent.AkunProfil;
import com.example.user.sirent.JSON.JSONParser;
import com.example.user.sirent.R;
import com.example.user.sirent.SharedPref;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by Sugik on 5/5/2017.
 */

public class Ubah_Password extends AppCompatActivity {

    private static String url_updatepass = "http://sirent.esy.es/update_password.php";
    private static final String TAG_SUCCESS = "sukses";

    SharedPref sharedPref;
    JSONParser jsonParser = new JSONParser();
    RequestParams params = new RequestParams();
    private ProgressDialog pDialog;

    boolean lama;
    String baru;


    EditText t_lama, t_baru, t_baru1;
    Button bt_ubah;
    int id, idtoko;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubahpass);

        t_lama = (EditText)findViewById(R.id.edit_passlama);
        t_baru = (EditText)findViewById(R.id.edit_passbaru);
        t_baru1 = (EditText)findViewById(R.id.edit_passbaru1);
        bt_ubah = (Button)findViewById(R.id.but_simpan);


        sharedPref = new SharedPref(getApplicationContext());
        if (sharedPref.isLoggedInAdmin()==true){
            id = sharedPref.getUserID();
            idtoko = sharedPref.getTokoID();
        } else if (sharedPref.isLoggedIn()==true){
            id = sharedPref.getUserID();
        }
        Toast.makeText(this, "ID_User: "+id, Toast.LENGTH_SHORT).show();


        t_baru1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                bt_ubah.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>5){
                    bt_ubah.setEnabled(true);
                } else {
                    bt_ubah.setEnabled(false);
                }
            }
        });




        bt_ubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(Ubah_Password.this, "Test: "+f+" "+s, Toast.LENGTH_SHORT).show();
                if (t_lama.getText().toString().isEmpty() || t_baru.getText().toString().isEmpty() ||
                        t_baru1.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Field tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                } else {
                    if (t_baru.getText().toString().equals(t_baru1.getText().toString())){
                        new updatePass().execute();
                    } else if (t_baru.getText().toString() != t_baru1.getText().toString()){
                        Toast.makeText(Ubah_Password.this, "Password baru tidak cocok!", Toast.LENGTH_SHORT).show();
                        t_baru.requestFocus();

                    }
                }
            }
        });

    }

    private class updatePass extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Ubah_Password.this);
            pDialog.setMessage("Mengubah Password");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            String tLama = t_lama.getText().toString();
            String tBaru = t_baru.getText().toString();
            String tBaru1 = t_baru1.getText().toString();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("ID_user", ""+id));
            params.add(new BasicNameValuePair("pass_lama", tLama));
            params.add(new BasicNameValuePair("pass_baru", tBaru));
            params.add(new BasicNameValuePair("pass_baru1", tBaru1));

            JSONObject jobj = jsonParser.makeHttpRequest(url_updatepass, "POST", params);
            Log.d("Res: ", jobj.toString());

            try {
                int sukses = jobj.getInt(TAG_SUCCESS);
                if (sukses==1){
                    Intent in = new Intent(Ubah_Password.this, AkunProfil.class);
                    in.putExtra("sukses", "Yes");
                    startActivity(in);
                    finish();
                } else if (sukses==0){
                    //pDialog.dismiss();
                    return null;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            if (s == null){
                showpassDialog();
            }

        }
    }

    public void showpassDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(Ubah_Password.this);
        alert.setMessage("Password lama salah!");
        alert.setNeutralButton("Oke", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog buil = alert.create();
        buil.show();
    }

    public void showpassDialogOke(){
        AlertDialog.Builder alert = new AlertDialog.Builder(Ubah_Password.this);
        alert.setMessage("Password berhasil diubah");
        alert.setNeutralButton("Oke", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog buil = alert.create();
        buil.show();
    }
}
