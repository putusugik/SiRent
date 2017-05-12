package com.example.user.sirent.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.user.sirent.Image.ImageLoader;
import com.example.user.sirent.JSON.JSONParser;
import com.example.user.sirent.R;
import com.example.user.sirent.SharedPref;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class Admin_InsertKend extends AppCompatActivity {
    private static int RESULT_LOAD_IMG = 1;
    private static String url_merk = "http://sirent.esy.es/select_merkkendaraan.php";
    private static String url_tipe = "http://sirent.esy.es/select_tipekendaraan.php";
    private static String url_insertKend = "http://sirent.esy.es/insert_kendaraan.php";
    private static String url_aksesoris = "http://sirent.esy.es/select_aksesoris.php";
    private static final String TAG_SUCCESS = "sukses";
    public static final String TAG_IDAKS = "ID_aksesoris";
    public static final String TAG_AKSESORIS = "nama_aksesoris";
    JSONParser jsonParser = new JSONParser();
    RequestParams params = new RequestParams();
    ArrayList<String> idmerk = new ArrayList<String>();
    ArrayList<String> merk_kendaraan = new ArrayList<String>();
    ArrayList<String> idtipe = new ArrayList<String>();
    ArrayList<String> tipe_kendaraan = new ArrayList<String>();
    private AdapterAksesoris adapterAksesoris;
    JSONArray aksesoris = null;
    ArrayList<HashMap<String, String>> aksesorislist = new ArrayList<HashMap<String, String>>();

    private ProgressDialog pDialog;

    SharedPref sharedPref;
    int id, idToko;

    String imgPath, fileName;
    String encodedString;
    Bitmap bitmap;
    public ImageLoader imageLoader;
    Spinner merkspin, tipespin;
    ImageView imgKend;
    EditText nama_kend, no_plat, no_mesin, jumlah, harga_sewa, deskripsi;
    String namaKend, noPlat, noMesin, jmlh, hargaSewa, deskrip, merkKend, tipeKend;
    ListView lAkse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_insert_kend);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        nama_kend = (EditText)findViewById(R.id.nama_kend);
        no_plat = (EditText)findViewById(R.id.no_plat);
        no_mesin = (EditText)findViewById(R.id.no_mesin);
        jumlah = (EditText)findViewById(R.id.jumlah);
        harga_sewa = (EditText)findViewById(R.id.harga_sewa);
        deskripsi = (EditText)findViewById(R.id.deskripsi);
        merkspin = (Spinner) findViewById(R.id.spinner2);
        tipespin = (Spinner) findViewById(R.id.spinner3);
        lAkse = (ListView)findViewById(R.id.listAksesoris);

        imgKend = (ImageView)findViewById(R.id.imageKend);
        imageLoader = new ImageLoader(this);

        sharedPref = new SharedPref(getApplicationContext());
        id = sharedPref.getUserID();
        idToko = sharedPref.getTokoID();
//        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();

        new merkSpin().execute();
        new tipeSpin().execute();
        new loadAksesoris().execute();

    }

    class insertKend extends AsyncTask <String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Admin_InsertKend.this);
            pDialog.setMessage("Menyimpan");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            namaKend = nama_kend.getText().toString();
            noPlat = no_plat.getText().toString();
            noMesin = no_mesin.getText().toString();
            jmlh = jumlah.getText().toString();
            hargaSewa = harga_sewa.getText().toString();
            deskrip = deskripsi.getText().toString();
            String IDmerk = idmerk.get(merkspin.getSelectedItemPosition());
            String IDtipe = idtipe.get(tipespin.getSelectedItemPosition());
            String idtoko = String.valueOf(idToko);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("nama_kendaraan", namaKend));
            params.add(new BasicNameValuePair("no_mesin", noMesin));
            params.add(new BasicNameValuePair("no_plat", noPlat));
            params.add(new BasicNameValuePair("ID_merk", IDmerk));
            params.add(new BasicNameValuePair("ID_tipe", IDtipe));
            params.add(new BasicNameValuePair("ID_toko", idtoko));
            params.add(new BasicNameValuePair("harga_sewa", hargaSewa));
            params.add(new BasicNameValuePair("jumlah", jmlh));
            params.add(new BasicNameValuePair("deskripsi", deskrip));

            JSONObject object = jsonParser.makeHttpRequest(url_insertKend, "POST", params);
            Log.d("Response ", object.toString());

            try{
                int sukses = object.getInt(TAG_SUCCESS);
                if (sukses == 1){

                } else {
                    Toast.makeText(Admin_InsertKend.this, "Gagal", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            uploadImage();
            pDialog.dismiss();
        }
    }

    public void simpanKend(View view) {
        namaKend = nama_kend.getText().toString();
        noPlat = no_plat.getText().toString();
        noMesin = no_mesin.getText().toString();
        jmlh = jumlah.getText().toString();
        hargaSewa = harga_sewa.getText().toString();
        deskrip = deskripsi.getText().toString();
        /*merkKend = merkspin.getSelectedItem().toString();
        tipeKend = tipespin.getSelectedItem().toString();
        String type = "insertKend";
        Backtask backtask = new Backtask(this);
        backtask.execute(type, namaKend, noMesin, noPlat, merkKend, tipeKend, jmlh, hargaSewa, deskrip);
        Intent i = new Intent(this, Dashboard_Admin.class);
        startActivity(i);*/
        if (namaKend.isEmpty() || noPlat.isEmpty() ||
                noMesin.isEmpty() || hargaSewa.isEmpty() || deskrip.isEmpty()) {
            Toast.makeText(this, "Isian belum lengkap", Toast.LENGTH_SHORT).show();
        } else {
            new insertKend().execute();
            Intent i = new Intent(Admin_InsertKend.this, Dashboard_Admin.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
    }

    private class merkSpin extends AsyncTask<String, String, List<String>>{
        @Override
        protected List<String> doInBackground(String... args) {
            int sukses;
            List<String> list = new ArrayList<String>();
            try{
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject object = jsonParser.makeHttpRequest(url_merk, "GET", params);

                sukses = object.getInt(TAG_SUCCESS);
                if (sukses == 1){
                    JSONArray array = object.getJSONArray("merk");
                    int numRows = array.length();
                    Log.d("List:", "" +numRows);
                    for (int i = 0; i < numRows; i++){
                        JSONObject merk = array.getJSONObject(i);
                        idmerk.add(merk.getString("ID"));
                        merk_kendaraan.add(merk.getString("merk_kendaraan"));
                    }
                    Log.d("List2:", "" + idmerk.size() + "," + merk_kendaraan.size());

                }else {

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            ArrayAdapter<String> merkadapter = new ArrayAdapter<String>(Admin_InsertKend.this, R.layout.spin_merk, merk_kendaraan);
            merkadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            merkspin.setAdapter(merkadapter);
        }
    }

    private class tipeSpin extends AsyncTask<String, String, List<String>>{
        @Override
        protected List<String> doInBackground(String... args) {
            int sukses;
            List<String> list = new ArrayList<String>();
            try{
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject object = jsonParser.makeHttpRequest(url_tipe, "GET", params);

                sukses = object.getInt(TAG_SUCCESS);
                if (sukses == 1){
                    JSONArray array = object.getJSONArray("tipe");
                    int numRows = array.length();
                    Log.d("List:", "" +numRows);
                    for (int i = 0; i < numRows; i++){
                        JSONObject tipe = array.getJSONObject(i);
                        idtipe.add(tipe.getString("ID"));
                        tipe_kendaraan.add(tipe.getString("tipe_kendaraan"));
                    }
                    Log.d("List2:", "" + idtipe.size() + "," + tipe_kendaraan.size());

                }else {

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            ArrayAdapter<String> tipeadapter = new ArrayAdapter<String>(Admin_InsertKend.this, R.layout.spin_merk, tipe_kendaraan);
            tipeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            tipespin.setAdapter(tipeadapter);
        }
    }

    public void imageKend (View view){
        Intent gallery = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgPath = cursor.getString(columnIndex);
                cursor.close();

                // Set the Image in ImageView
                imgKend.setImageBitmap(BitmapFactory
                        .decodeFile(imgPath));
                // Get the Image's file name
                String fileNameSegments[] = imgPath.split("/");
                fileName = fileNameSegments[fileNameSegments.length - 1];
                // Put file name in Async Http Post Param which will used in Php web app
                params.put("filename", fileName);
                Toast.makeText(this, "Image Selected", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void uploadImage() {
        // When Image is selected from Gallery
        if (imgPath != null && !imgPath.isEmpty()) {
            pDialog.setMessage("Converting Image to Binary Data");
            pDialog.show();
            // Convert image to String using Base64
            encodeImagetoString();
            // When Image is not selected from Gallery
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "You must select image from gallery before you try to upload",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void encodeImagetoString() {
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {

            };

            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                bitmap = BitmapFactory.decodeFile(imgPath,
                        options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                encodedString = Base64.encodeToString(byte_arr, 0);
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                pDialog.setMessage("Calling Upload");
                // Put converted Image string into Async Http Post param
                params.put("image", encodedString);


                // Trigger Image upload
                //triggerImageUpload();
                makeHTTPCall();
            }
        }.execute(null, null, null);
    }

    private void makeHTTPCall() {
        pDialog.setMessage("Please Wait");
        AsyncHttpClient client = new AsyncHttpClient();
        // Don't forget to change the IP address to your LAN address. Port no as well.
        client.post("http://sirent.esy.es/fotokend_upload.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                pDialog.hide();
                Toast.makeText(getApplicationContext(), "Upload successfull",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                pDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(),
                            "Requested resource not found",
                            Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(),
                            "Something went wrong at server end",
                            Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(
                            getApplicationContext(),
                            "Error Occured \n Most Common Error: \n1. Device not connected to Internet\n2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : "
                                    + statusCode, Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    private class loadAksesoris extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            JSONObject obj = jsonParser.makeHttpRequest(url_aksesoris, "GET", params);
            Log.d("Aksesoris: ", obj.toString());

            try {
                int sukses = obj.getInt(TAG_SUCCESS);
                if (sukses==1){
                    aksesoris = obj.getJSONArray("aksesoris");
                    aksesorislist.removeAll(aksesorislist);
                    for (int i = 0; i < aksesoris.length(); i ++){
                        JSONObject object = aksesoris.getJSONObject(i);
                        HashMap<String,String> map = new HashMap<String, String>();
                        String id = object.getString(TAG_IDAKS);
                        String namaAks = object.getString(TAG_AKSESORIS);

                        map.put(TAG_IDAKS, id);
                        map.put(TAG_AKSESORIS, namaAks);

                        aksesorislist.add(map);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (adapterAksesoris!=null){
                adapterAksesoris.notifyDataSetChanged();
                return;
            }
            adapterAksesoris = new AdapterAksesoris(Admin_InsertKend.this, aksesorislist);
            lAkse.setAdapter(adapterAksesoris);
        }
    }
}
