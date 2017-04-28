package com.example.user.sirent.Admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.List;
import java.util.concurrent.Executor;
import java.util.jar.Attributes;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class DetailKendaraanAdmin extends AppCompatActivity {

    int id;
    String idkend;
    EditText namakend, nomesin, noplat, harga, jml, deskripsi;
    Spinner merkKend, tipeKend;
    ImageView imgKend;
    public ImageLoader imageLoader;
    String imgPath, fileName;
    String encodedString;
    Bitmap bitmap;
    Button editBut, delBut;

    private static int RESULT_LOAD_IMG = 1;
    private static String url_getdetail = "http://sirent.esy.es/select_detailkendaraan.php";
    private static String url_merk = "http://sirent.esy.es/select_merkkendaraan.php";
    private static String url_tipe = "http://sirent.esy.es/select_tipekendaraan.php";
    private static String url_updateKend = "http://sirent.esy.es/update_kendaraan.php";
    private static String url_deleteKend = "http://sirent.esy.es/delete_kendaraan.php";
    public static final String TAG_SUCCESS = "sukses";
    public static final String TAG_KEND = "kendaraan";
    public static final String TAG_ID = "id_kendaraan";
    public static final String TAG_KENDARAAN = "nama_kendaraan";
    public static final String TAG_MESIN = "no_mesin";
    public static final String TAG_PLAT = "no_plat";
    JSONParser jsonParser = new JSONParser();
    RequestParams params = new RequestParams();
    ArrayList<String> idmerk = new ArrayList<String>();
    ArrayList<String> merk_kendaraan = new ArrayList<String>();
    ArrayList<String> idtipe = new ArrayList<String>();
    ArrayList<String> tipe_kendaraan = new ArrayList<String>();

    SharedPref sharedPref;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_kendaraan_admin);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        editBut = (Button)findViewById(R.id.updateBut);
        delBut = (Button)findViewById(R.id.deleteBut);

        namakend = (EditText)findViewById(R.id.nama_kend);
        noplat = (EditText)findViewById(R.id.no_plat);
        nomesin = (EditText)findViewById(R.id.no_mesin);
        harga = (EditText)findViewById(R.id.harga_sewa);
        jml = (EditText)findViewById(R.id.jumlah);
        deskripsi = (EditText)findViewById(R.id.deskripsi);
        merkKend = (Spinner)findViewById(R.id.spinner2);
        tipeKend = (Spinner)findViewById(R.id.spinner3);
        imgKend = (ImageView)findViewById(R.id.imageKendaraan);
        imageLoader = new ImageLoader(this);

        namakend.setEnabled(false);
        noplat.setEnabled(false);
        nomesin.setEnabled(false);
        harga.setEnabled(false);
        jml.setEnabled(false);
        deskripsi.setEnabled(false);
        merkKend.setEnabled(false);
        tipeKend.setEnabled(false);
        imgKend.setEnabled(false);

        Intent i = getIntent();
        idkend = i.getStringExtra("ID");
        Toast.makeText(this, idkend, Toast.LENGTH_SHORT).show();
        sharedPref = new SharedPref(getApplicationContext());
        id = sharedPref.getUserID();

        new merkSpin().execute();
        new tipeSpin().execute();
        new getKendDetail().execute();

        editBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (namakend.isEnabled()==false){
                    namakend.setEnabled(true);
                    noplat.setEnabled(true);
                    nomesin.setEnabled(true);
                    harga.setEnabled(true);
                    jml.setEnabled(true);
                    deskripsi.setEnabled(true);
                    imgKend.setEnabled(true);
                    delBut.setEnabled(false);
                    editBut.setText("Perbaharui");
                }else if (namakend.isEnabled()==true){
                    new updateKendaraan().execute();
                    editBut.setText("Ubah");
                }
            }
        });
        delBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder aDialog = new AlertDialog.Builder(DetailKendaraanAdmin.this);
                aDialog.setMessage("Apakah anda yakin menghapus kendaraan ini ?");
                aDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new deleteKendaraan().execute();
                    }
                });
                aDialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alert = aDialog.create();
                alert.show();
            }
        });

    }

    private class merkSpin extends AsyncTask<String,String,List<String>>{
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
            ArrayAdapter<String> merkadapter = new ArrayAdapter<String>(DetailKendaraanAdmin.this, R.layout.spin_merk, merk_kendaraan);
            merkadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            merkKend.setAdapter(merkadapter);
        }
    }

    private class tipeSpin extends AsyncTask<String,String,List<String>> {
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
            ArrayAdapter<String> tipeadapter = new ArrayAdapter<String>(DetailKendaraanAdmin.this, R.layout.spin_merk, tipe_kendaraan);
            tipeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            tipeKend.setAdapter(tipeadapter);
        }
    }

    public void uploadImage (View view){
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

    private class getKendDetail extends AsyncTask<String, String, List<String>> {
        @Override
        protected List<String> doInBackground(String... arg0) {
            int sukses;
            List<String>list = new ArrayList<String>();
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("id",""+idkend));
                JSONObject object = jsonParser.makeHttpRequest(url_getdetail, "GET", params);

                Log.d("Detail: ",object.toString());
                sukses = object.getInt(TAG_SUCCESS);
                if (sukses == 1){
                    JSONArray objArray = object.getJSONArray("kendaraan");
                    JSONObject object1 = objArray.getJSONObject(0);
                    list.add(object1.getString("ID_toko"));
                    list.add(object1.getString("id_pemilik"));
                    list.add(object1.getString("id_kendaraan"));
                    list.add(object1.getString("nama_kendaraan"));
                    list.add(object1.getString("no_plat"));
                    list.add(object1.getString("no_mesin"));
                    list.add(object1.getString("harga_sewa"));
                    list.add(object1.getString("jumlah"));
                    list.add(object1.getString("deskripsi"));
                    list.add(object1.getString("gambar").replace("\\", ""));

                }else {

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            namakend.setText(result.get(3));
            noplat.setText(result.get(4));
            nomesin.setText(result.get(5));
            harga.setText(result.get(6));
            jml.setText(result.get(7));
            deskripsi.setText(result.get(8));
            imageLoader.DisplayImage(result.get(9), DetailKendaraanAdmin.this.imgKend);
        }
    }


    private class updateKendaraan extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DetailKendaraanAdmin.this);
            pDialog.setMessage("Memperbaharui");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            String IDkend = idkend;
            String IDUser = String.valueOf(id);
            String nameKend = namakend.getText().toString();
            String noPlat = noplat.getText().toString();
            String noMes = nomesin.getText().toString();
            String hrg = harga.getText().toString();
            String jumlah = jml.getText().toString();
            String desk = deskripsi.getText().toString();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("ID_kendaraan", ""+IDkend));
            params.add(new BasicNameValuePair("ID_toko",""+IDUser));
            params.add(new BasicNameValuePair("nama_kendaraan", nameKend));
            params.add(new BasicNameValuePair("no_plat", noPlat));
            params.add(new BasicNameValuePair("no_mesin", noMes));
            params.add(new BasicNameValuePair("harga_sewa", hrg));
            params.add(new BasicNameValuePair("jumlah", jumlah));
            params.add(new BasicNameValuePair("deskripsi", desk));

            JSONObject object = jsonParser.makeHttpRequest(url_updateKend,"POST", params);
            Log.d("respon", object.toString());

            try{
                int sukses = object.getInt(TAG_SUCCESS);
                if (sukses == 1){
                    Intent i = new Intent(DetailKendaraanAdmin.this, Dashboard_Admin.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
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
            uploadImage();
        }
    }

    public void uploadImage() {
        // When Image is selected from Gallery
        if (imgPath != null && !imgPath.isEmpty()) {
            pDialog.setMessage("Converting Image to Binary Data");
//            pDialog.show();
            // Convert image to String using Base64
            encodeImagetoString();
            // When Image is not selected from Gallery
        } else {
            Toast.makeText(
                    this,
                    "You must select image from gallery before you try to upload",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void encodeImagetoString() {
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

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                pDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(DetailKendaraanAdmin.this,
                            "Requested resource not found",
                            Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(DetailKendaraanAdmin.this,
                            "Something went wrong at server end",
                            Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(
                            DetailKendaraanAdmin.this,
                            "Error Occured \n Most Common Error: \n1. Device not connected to Internet\n2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : "
                                    + statusCode, Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    private class deleteKendaraan extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DetailKendaraanAdmin.this);
            pDialog.setMessage("Menghapus");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            String IDkend = idkend;
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("ID", ""+IDkend));

            JSONObject object = jsonParser.makeHttpRequest(url_deleteKend,"POST", params);
            Log.d("respon", object.toString());

            try{
                int sukses = object.getInt(TAG_SUCCESS);
                if (sukses == 1){
                    Intent i = new Intent(DetailKendaraanAdmin.this, Dashboard_Admin.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
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
        }
    }
}

