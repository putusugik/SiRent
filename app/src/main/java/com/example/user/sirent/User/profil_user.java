package com.example.user.sirent.User;

import android.app.DatePickerDialog;
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
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class profil_user extends AppCompatActivity {
    private static int RESULT_LOAD_IMG = 1;
    SharedPref sharedPref;
    private static String url_updateuser = "http://sirent.esy.es/update_profiluser-old.php";
    private static String url_selectuser = "http://sirent.esy.es/select_profiluser.php";
    private static final String TAG_SUCCESS = "sukses";
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    RequestParams params = new RequestParams();

    int id, ID_user;
    String imgPath, fileName;
    String encodedString;
    Bitmap bitmap;
    public ImageLoader imageLoader;

    EditText  edit_namadep, edit_namabel, edit_tempatlahir, edit_tgllahir,
            edit_nohp, edit_alamat, edit_email;
    ImageView image;
    TextView userview, emailview, iduser;
    Button edit, foto;
    Calendar calendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dateSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updatelabel();
        }
    };

    private void updatelabel() {
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        edit_tgllahir.setText(sdf.format(calendar.getTime()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_user);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        edit_namadep = (EditText)findViewById(R.id.edit_namadep);
        edit_namabel = (EditText)findViewById(R.id.edit_namabel);
        edit_tempatlahir = (EditText)findViewById(R.id.edit_tempatlahir);
        edit_tgllahir = (EditText)findViewById(R.id.edit_tgllahir);
        edit_nohp = (EditText)findViewById(R.id.edit_nohp);
        edit_alamat = (EditText)findViewById(R.id.edit_alamat);
        edit_email = (EditText)findViewById(R.id.edit_email);
        image = (ImageView)findViewById(R.id.image);
        userview = (TextView)findViewById(R.id.userview);
        emailview = (TextView)findViewById(R.id.emailview);
        iduser = (TextView)findViewById(R.id.iduser);
        edit = (Button)findViewById(R.id.buttedit);
        foto = (Button)findViewById(R.id.butt_foto);
        imageLoader = new ImageLoader(this);

        sharedPref = new SharedPref(getApplicationContext());
        id = sharedPref.getUserID();
        new getProfilDetail().execute();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_namadep.isEnabled()==false){
                    edit_namadep.setEnabled(true);
                    edit_namabel.setEnabled(true);
                    edit_tempatlahir.setEnabled(true);
                    edit_tgllahir.setEnabled(true);
                    edit_nohp.setEnabled(true);
                    edit_alamat.setEnabled(true);
                    edit_email.setEnabled(true);
                    foto.setEnabled(true);
                    edit.setText("Perbaharui");
                } else if (edit_namadep.isEnabled()==true){
                    new updateUser().execute();
                }
            }
        });

        edit_tgllahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(profil_user.this, dateSet, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        new getProfilDetail().execute();
    }

    /*public void saveprofil(View v){
        new updateUser().execute();
    }*/

    /*public void fotoprofil (View view){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }*/

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
                image.setImageBitmap(BitmapFactory
                        .decodeFile(imgPath));
                // Get the Image's file name
                String fileNameSegments[] = imgPath.split("/");
                fileName = fileNameSegments[fileNameSegments.length - 1];
                // Put file name in Async Http Post Param which will used in Php web app
                params.put("filename", fileName);
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }

    class getProfilDetail extends AsyncTask <String, String, List<String>>{
        @Override
        protected void onPreExecute() {

        }


        protected List<String> doInBackground(String... arg0) {
            int sukses;
            List<String> list = new ArrayList<String>();
            try{
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("id",""+id));

                JSONObject json = jsonParser.makeHttpRequest(url_selectuser, "GET", params);

                Log.d("Background: ", json.toString());
                sukses = json.getInt(TAG_SUCCESS);
                if (sukses == 1){
                    JSONArray productObj = json.getJSONArray("profil");

                    JSONObject profil = productObj.getJSONObject(0);
                    list.add(profil.getString("ID"));
                    list.add(profil.getString("nama_depan"));
                    list.add(profil.getString("nama_belakang"));
                    list.add(profil.getString("tempat_lahir"));
                    list.add(profil.getString("tgl_lahir"));
                    list.add(profil.getString("no_hp"));
                    list.add(profil.getString("alamat"));
                    list.add(profil.getString("email"));
                    list.add(profil.getString("foto_user").replace("\\", ""));
                }else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return list;
        }


        protected void onPostExecute(List<String> result) {
            iduser.setText(result.get(0));
            edit_namadep.setText(result.get(1));
            edit_namabel.setText(result.get(2));
            edit_tempatlahir.setText(result.get(3));
            edit_tgllahir.setText(result.get(4));
            edit_nohp.setText(result.get(5));
            edit_alamat.setText(result.get(6));
            edit_email.setText(result.get(7));
            userview.setText(result.get(1)+ " "+result.get(2));
            emailview.setText(result.get(7));
            imageLoader.DisplayImage(result.get(8), profil_user.this.image);

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
        client.post("http://sirent.esy.es/foto_upload.php", params, new AsyncHttpResponseHandler() {
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

    class updateUser extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(profil_user.this);
            pDialog.setMessage("Menyimpan");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            String IDuser = iduser.getText().toString();
            String namadep = edit_namadep.getText().toString();
            String namabel = edit_namabel.getText().toString();
            String tempatlahit = edit_tempatlahir.getText().toString();
            String tgllahir = edit_tgllahir.getText().toString();
            String nohp = edit_nohp.getText().toString();
            String alamat = edit_alamat.getText().toString();
            String email = edit_email.getText().toString();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("ID_user",""+IDuser));
            params.add(new BasicNameValuePair("nama_depan", namadep));
            params.add(new BasicNameValuePair("nama_belakang", namabel));
            params.add(new BasicNameValuePair("tempat_lahir", tempatlahit));
            params.add(new BasicNameValuePair("tgl_lahir", tgllahir));
            params.add(new BasicNameValuePair("no_hp", nohp));
            params.add(new BasicNameValuePair("alamat", alamat));
            params.add(new BasicNameValuePair("email", email));

            JSONObject jsonObject = jsonParser.makeHttpRequest(url_updateuser, "POST", params);
            Log.d("Create Response", jsonObject.toString());

            try{
                int sukses = jsonObject.getInt(TAG_SUCCESS);
                if (sukses == 1){
                    profil_user.this.params.put("id",""+id);
                    Intent i = new Intent(profil_user.this, Dashboard.class);
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
            uploadImage();
            pDialog.dismiss();
        }
    }


}
