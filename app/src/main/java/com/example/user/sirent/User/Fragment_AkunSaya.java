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
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_AkunSaya extends Fragment {
    private static int RESULT_LOAD_IMG = 1;
    SharedPref sharedPref;
    private static String url_updateuser = "http://sirent.esy.es/update_profiluser-old.php";
    private static String url_selectuser = "http://sirent.esy.es/select_profiluser.php";
    private static final String TAG_SUCCESS = "sukses";
    public ProgressDialog pD;
    JSONParser jsonParser = new JSONParser();
    RequestParams params = new RequestParams();

    int id, ID_user;
    String imgPath, fileName;
    String encodedString;
    Bitmap bitmap;
    public ImageLoader imageLoader;

    EditText tjudul, tnamadep, tnamabel, ttempat, ttgl, talamat, tntlp, temail, tid;
    Button edit, foto;
    ImageView imgAkun;
    String ID, nDep, nBel, tLahir, tglLahir, nohp, almt, email;

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
        ttgl.setText(sdf.format(calendar.getTime()));
    }

    public Fragment_AkunSaya() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        View v =  inflater.inflate(R.layout.fragment_akun_saya, container, false);
        imgAkun = (ImageView)v.findViewById(R.id.imgakun);
        tjudul = (EditText)v.findViewById(R.id.editText);
        tid = (EditText)v.findViewById(R.id.iduser);
        tnamadep = (EditText)v.findViewById(R.id.edit_namadep);
        tnamabel = (EditText)v.findViewById(R.id.edit_namabel);
        ttempat = (EditText)v.findViewById(R.id.edit_tempatlahir);
        ttgl = (EditText)v.findViewById(R.id.edit_tgllahir);
        talamat = (EditText)v.findViewById(R.id.edit_alamat);
        tntlp = (EditText)v.findViewById(R.id.edit_nohp);
        temail = (EditText)v.findViewById(R.id.edit_email);
        edit = (Button)v.findViewById(R.id.bt_edit);
        foto = (Button)v.findViewById(R.id.bt_foto);
        imageLoader = new ImageLoader(getContext());

        sharedPref = new SharedPref(this.getContext());
        id = sharedPref.getUserID();
        new getProfil().execute();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tnamadep.isEnabled()==false){
                    tnamadep.setEnabled(true);
                    tnamabel.setEnabled(true);
                    ttempat.setEnabled(true);
                    ttgl.setEnabled(true);
                    talamat.setEnabled(true);
                    tntlp.setEnabled(true);
                    temail.setEnabled(true);
                    foto.setEnabled(true);
                    edit.setText("Perbaharui");
                }else if (tnamadep.isEnabled()){
                    new updateUser().execute();
                }
            }
        });

        ttgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), dateSet, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
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

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        new getProfil().execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgPath = cursor.getString(columnIndex);
                cursor.close();

                // Set the Image in ImageView
                imgAkun.setImageBitmap(BitmapFactory
                        .decodeFile(imgPath));
                // Get the Image's file name
                String fileNameSegments[] = imgPath.split("/");
                fileName = fileNameSegments[fileNameSegments.length - 1];
                // Put file name in Async Http Post Param which will used in Php web app
                params.put("filename", fileName);
            } else {
                Toast.makeText(getContext(), "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private class getProfil extends AsyncTask<String, String, List<String>> {
        ProgressDialog pDialog = new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Memuat");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected List<String> doInBackground(String... args) {
            int sukses;
            List<String> list = new ArrayList<String>();
            try{
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("id", ""+id));
                JSONObject obj = jsonParser.makeHttpRequest(url_selectuser, "GET", params);
                Log.d("Res: ", obj.toString());
                sukses = obj.getInt(TAG_SUCCESS);
                if (sukses == 1){
                    JSONArray jarray = obj.getJSONArray("profil");
                    JSONObject jobj = jarray.getJSONObject(0);
                    list.add(jobj.getString("ID"));
                    list.add(jobj.getString("nama_depan"));
                    list.add(jobj.getString("nama_belakang"));
                    list.add(jobj.getString("tempat_lahir"));
                    list.add(jobj.getString("tgl_lahir"));
                    list.add(jobj.getString("no_hp"));
                    list.add(jobj.getString("alamat"));
                    list.add(jobj.getString("email"));
                    list.add(jobj.getString("foto_user").replace("\\", ""));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return list;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            tid.setText(strings.get(0));
            tnamadep.setText(strings.get(1));
            tnamabel.setText(strings.get(2));
            ttempat.setText(strings.get(3));
            ttgl.setText(strings.get(4));
            tntlp.setText(strings.get(5));
            talamat.setText(strings.get(6));
            temail.setText(strings.get(7));
            imageLoader.DisplayImage(strings.get(8), Fragment_AkunSaya.this.imgAkun);
            pDialog.dismiss();
        }
    }

    class updateUser extends AsyncTask<String,String,String>{
        ProgressDialog pd = new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Menyimpan");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... args) {
            ID = tid.getText().toString();
            nDep = tnamadep.getText().toString();
            nBel = tnamabel.getText().toString();
            tLahir = ttempat.getText().toString();
            tglLahir = ttgl.getText().toString();
            nohp = tntlp.getText().toString();
            almt = talamat.getText().toString();
            email = temail.getText().toString();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("ID_user", ""+ID));
            params.add(new BasicNameValuePair("nama_depan", nDep));
            params.add(new BasicNameValuePair("nama_belakang", nBel));
            params.add(new BasicNameValuePair("tempat_lahir", tLahir));
            params.add(new BasicNameValuePair("tgl_lahir", tglLahir));
            params.add(new BasicNameValuePair("no_hp", nohp));
            params.add(new BasicNameValuePair("alamat", almt));
            params.add(new BasicNameValuePair("email", email));

            JSONObject jsonObject = jsonParser.makeHttpRequest(url_updateuser, "POST", params);
            Log.d("Create Response", jsonObject.toString());

            try{
                int sukses = jsonObject.getInt(TAG_SUCCESS);
                if (sukses==1){
                    Fragment_AkunSaya.this.params.put("id",""+id);
                    Intent i = new Intent(getActivity(), Dashboard.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    getActivity().finish();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            uploadImage();
            pd.dismiss();
        }
    }

    public void uploadImage() {
        // When Image is selected from Gallery
        if (imgPath != null && !imgPath.isEmpty()) {
            // Convert image to String using Base64
            encodeImagetoString();
            // When Image is not selected from Gallery
        } else {
            Toast.makeText(
                    getContext(),
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
                // Put converted Image string into Async Http Post param
                params.put("image", encodedString);


                // Trigger Image upload
                //triggerImageUpload();
                makeHTTPCall();
            }
        }.execute(null, null, null);
    }

    private void makeHTTPCall() {

        AsyncHttpClient client = new AsyncHttpClient();
        // Don't forget to change the IP address to your LAN address. Port no as well.
        client.post("http://sirent.esy.es/foto_upload.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(getContext(), "Upload successfull",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (statusCode == 404) {
                    Toast.makeText(getContext(),
                            "Requested resource not found",
                            Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getContext(),
                            "Something went wrong at server end",
                            Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(
                            getContext(),
                            "Error Occured \n Most Common Error: \n1. Device not connected to Internet\n2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : "
                                    + statusCode, Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }
}
