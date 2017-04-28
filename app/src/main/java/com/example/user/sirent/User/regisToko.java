package com.example.user.sirent.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.sirent.JSON.JSONParser;
import com.example.user.sirent.R;
import com.example.user.sirent.SharedPref;
import com.example.user.sirent.login.login_dashboard;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class regisToko extends AppCompatActivity {

    private static String url_regisToko = "http://sirent.esy.es/insert_toko.php";
    private static String url_updatestatus = "http://sirent.esy.es/update_statususer.php";
    private static final String TAG_SUCCESS = "sukses";
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    RequestParams params = new RequestParams();
    private SharedPref sharedPref;
    int id;

    EditText nama_toko, slogan, alamat_toko, notelp, email,
            jam_buka, jam_tutup;
    Button save, cancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis_toko);

        sharedPref = new SharedPref(getApplicationContext());
        id = sharedPref.getUserID();

        nama_toko = (EditText)findViewById(R.id.edit_namatok);
        slogan = (EditText)findViewById(R.id.edit_slogan);
        alamat_toko = (EditText)findViewById(R.id.edit_alamat);
        notelp = (EditText)findViewById(R.id.edit_nohp);
        email = (EditText)findViewById(R.id.edit_email);
        jam_buka = (EditText)findViewById(R.id.edit_buka);
        jam_tutup = (EditText)findViewById(R.id.edit_tutup);

        save = (Button)findViewById(R.id.button5);
        cancel = (Button)findViewById(R.id.button6);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new registerToko().execute();
                new insertToko().execute();
                Intent i = new Intent(regisToko.this, login_dashboard.class);
                pDialog.dismiss();
                finish();
                startActivity(i);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    class registerToko extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(regisToko.this);
            pDialog.setMessage("Memproses");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("ID",""+id));


            JSONObject jsonObject = jsonParser.makeHttpRequest(url_updatestatus, "POST", params);
            Log.d("Response: ", jsonObject.toString());

            try{
                int sukses = jsonObject.getInt(TAG_SUCCESS);
                if (sukses == 1){
                    regisToko.this.params.put("ID",""+id);
                    /*Intent i = new Intent(regisToko.this, Login.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();*/
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    class insertToko extends AsyncTask<String, String ,String >{

        @Override
        protected String doInBackground(String... args) {
            String ID_user = String.valueOf(id);
            String namatoko = nama_toko.getText().toString();
            String alamat = alamat_toko.getText().toString();
            String noTelp = notelp.getText().toString();
            String eMail = email.getText().toString();
            String jBuka = jam_buka.getText().toString();
            String jTutup = jam_tutup.getText().toString();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("nama_toko", namatoko));
            params.add(new BasicNameValuePair("ID_user", ID_user));
            params.add(new BasicNameValuePair("alamat", alamat));
            params.add(new BasicNameValuePair("no_hp", noTelp));
            params.add(new BasicNameValuePair("email", eMail));
            params.add(new BasicNameValuePair("jam_buka", jBuka));
            params.add(new BasicNameValuePair("jam_tutup", jTutup));

            JSONObject jObject = jsonParser.makeHttpRequest(url_regisToko, "POST", params);
            Log.d("Respon: ", jObject.toString());

            try{
                int sukses = jObject.getInt(TAG_SUCCESS);
                if (sukses == 1){
                    pDialog.dismiss();
                } else {
                    Toast.makeText(regisToko.this, "Gagal", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog = new ProgressDialog(regisToko.this);
            pDialog.setMessage("Silahkan login kembali");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
    }
}
