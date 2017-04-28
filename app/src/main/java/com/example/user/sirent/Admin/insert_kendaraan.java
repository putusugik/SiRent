package com.example.user.sirent.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.user.sirent.Image.ImageLoader;
import com.example.user.sirent.JSON.JSONParser;
import com.example.user.sirent.R;
import com.example.user.sirent.SharedPref;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class insert_kendaraan extends AppCompatActivity {
    private static int RESULT_LOAD_IMG = 1;
    SharedPref sharedPref;
    private static String url_insertkendaraan = "http://sirent.esy.es/insert_kendaraan-old.php";
    private static final String TAG_SUCCESS = "sukses";
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    RequestParams params = new RequestParams();

    String imgPath, fileName, encodedString;
    Bitmap bitmap;
    public ImageLoader imageLoader;

    EditText namakend, no_plat, no_mesin, harga_sewa;
    ImageView image;

    InputStream is=null;
    String result = null;
    String line = null;
    String[] name;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_kendaraan);


        spinner = (Spinner)findViewById(R.id.spinner);


        namakend = (EditText)findViewById(R.id.nama_kendaraan);
        no_plat = (EditText)findViewById(R.id.no_plat);
        no_mesin = (EditText)findViewById(R.id.no_mesin);
        harga_sewa = (EditText)findViewById(R.id.sewa);
        image = (ImageView)findViewById(R.id.imagekend);

        imageLoader = new ImageLoader(this);
        new spinerView().execute();

    }

    class spinerView extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {
            final List<String> list1 = new ArrayList<String>();

            try{
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://sirent.esy.es/select_merkkendaraan.php");
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity entity = httpResponse.getEntity();
                is = entity.getContent();


            } catch (Exception e) {
                Log.e("Fail 3", e.toString());
                Toast.makeText(insert_kendaraan.this, "Invalid Address", Toast.LENGTH_SHORT).show();
                finish();
            }

            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"), 8);
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = reader.readLine())!= null){
                    stringBuilder.append(line + "\n");
                }
                is.close();
                result = stringBuilder.toString();
            } catch (Exception e){
                Log.e("Fail 2", e.toString());
            }

            try{
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject = null;
                name = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length();i++){
                    jsonObject=jsonArray.getJSONObject(i);
                    name[i]=jsonObject.getString("merk_kendaraan");
                }
                for (int i = 0; i<name.length;i++){
                    list1.add(name[i]);
                }
                spinner_func();
            } catch (Exception e) {
                Log.e("Fail 1", e.toString());
            }
            return null;
        }
    }

    private void spinner_func() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(insert_kendaraan.this, R.layout.support_simple_spinner_dropdown_item, name);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void savekendaraan (View v){
        new insertKendaraan().execute();
        //fungsi insert
    }

    public void showKendaraan(View view){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    class insertKendaraan extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(insert_kendaraan.this);
            pDialog.setMessage("Menyimpan");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            String namaKendaraan = namakend.getText().toString();
            String noplat = no_plat.getText().toString();
            String nomesin = no_mesin.getText().toString();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("nama_kendaraan", namaKendaraan));
            params.add(new BasicNameValuePair("no_mesin", nomesin));
            params.add(new BasicNameValuePair("no_plat", noplat));

            JSONObject jsonObject = jsonParser.makeHttpRequest(url_insertkendaraan, "POST", params);
            Log.d("Response: ", jsonObject.toString());

            try{
                int sukses = jsonObject.getInt(TAG_SUCCESS);
                if (sukses == 1){

                    //Toast.makeText(insert_kendaraan.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                    //Intent i = new Intent(insert_kendaraan.this, )
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();
            Toast.makeText(insert_kendaraan.this, "Data disimpan", Toast.LENGTH_SHORT).show();
        }
    }
}
