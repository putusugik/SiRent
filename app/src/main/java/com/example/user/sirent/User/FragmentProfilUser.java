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

import com.example.user.sirent.Admin.Dashboard_Admin;
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
public class FragmentProfilUser extends Fragment {

    private static int RESULT_LOAD_IMG = 1;
    private static String url_updateuser = "http://sirent.esy.es/update_profiluser-old.php";
    private static String url_selectuser = "http://sirent.esy.es/select_profiluser.php";
    private static final String TAG_SUCCESS = "sukses";
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    RequestParams params = new RequestParams();

    EditText namaDep, namaBel, tempatLahir, tglLahir, nohp, alamat, email, IDuser;
    Button foto, edit;
    ImageView image;
    int id, ID_user;
    String imgPath, fileName, encodedString;
    Bitmap bitmap;
    SharedPref sharedPref;
    public ImageLoader imageLoader;
    Calendar calendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dateSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    private void updateLabel() {
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        tglLahir.setText(sdf.format(calendar.getTime()));
    }

    public FragmentProfilUser() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_fragment_profil_user, container, false);
        View view = (View)inflater.inflate(R.layout.fragment_fragment_profil_user, container, false);
        IDuser = (EditText)view.findViewById(R.id.iduser);
        namaDep = (EditText)view.findViewById(R.id.edit_namadep);
        namaBel = (EditText)view.findViewById(R.id.edit_namabel);
        tempatLahir = (EditText)view.findViewById(R.id.edit_tempatlahir);
        tglLahir = (EditText)view.findViewById(R.id.edit_tgllahir);
        nohp = (EditText)view.findViewById(R.id.edit_nohp);
        alamat = (EditText)view.findViewById(R.id.edit_alamat);
        email = (EditText)view.findViewById(R.id.edit_email);
        image = (ImageView)view.findViewById(R.id.image);
        edit = (Button)view.findViewById(R.id.butt_edit);
        foto = (Button)view.findViewById(R.id.butt_foto);

        namaDep.setEnabled(false);
        namaBel.setEnabled(false);
        tempatLahir.setEnabled(false);
        tglLahir.setEnabled(false);
        nohp.setEnabled(false);
        alamat.setEnabled(false);
        email.setEnabled(false);
        foto.setEnabled(false);

        imageLoader = new ImageLoader(this.getActivity());

        sharedPref = new SharedPref(getActivity().getApplicationContext());
        id = sharedPref.getUserID();
        new getProfilDetail().execute();

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "TEST", Toast.LENGTH_SHORT).show();
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, RESULT_LOAD_IMG);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (namaDep.isEnabled()==false){
                    namaDep.setEnabled(true);
                    namaBel.setEnabled(true);
                    tempatLahir.setEnabled(true);
                    tglLahir.setEnabled(true);
                    nohp.setEnabled(true);
                    alamat.setEnabled(true);
                    email.setEnabled(true);
                    foto.setEnabled(true);
                    edit.setText("Update");
                } else if (namaDep.isEnabled()==true){
                    new updateUser().execute();
                    edit.setText("Edit");
                }
            }
        });

        tglLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), dateSet, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        return view;
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
                image.setImageBitmap(BitmapFactory
                        .decodeFile(imgPath));
                // Get the Image's file name
                String fileNameSegments[] = imgPath.split("/");
                fileName = fileNameSegments[fileNameSegments.length - 1];
                // Put file name in Async Http Post Param which will used in Php web app
                params.put("filename", fileName);
            } else {
                Toast.makeText(getActivity(), "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private class getProfilDetail extends AsyncTask<String, String, List<String>> {

        @Override
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

        @Override
        protected void onPostExecute(List<String> result) {
            IDuser.setText(result.get(0));
            namaDep.setText(result.get(1));
            namaBel.setText(result.get(2));
            tempatLahir.setText(result.get(3));
            tglLahir.setText(result.get(4));
            nohp.setText(result.get(5));
            alamat.setText(result.get(6));
            email.setText(result.get(7));
            imageLoader.DisplayImage(result.get(8), FragmentProfilUser.this.image);
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
                    getActivity(),
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

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                pDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(getActivity(),
                            "Requested resource not found",
                            Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getActivity(),
                            "Something went wrong at server end",
                            Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(
                            getActivity(),
                            "Error Occured \n Most Common Error: \n1. Device not connected to Internet\n2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : "
                                    + statusCode, Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    private class updateUser extends AsyncTask<String, String , String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FragmentProfilUser.this.getActivity());
            pDialog.setMessage("Menyimpan");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            String IdUser = IDuser.getText().toString();
            String nDep = namaDep.getText().toString();
            String nBel = namaBel.getText().toString();
            String tLahir = tempatLahir.getText().toString();
            String tglL = tglLahir.getText().toString();
            String NOhp = nohp.getText().toString();
            String aLamat = alamat.getText().toString();
            String Email = email.getText().toString();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("ID_user",""+IdUser));
            params.add(new BasicNameValuePair("nama_depan",""+nDep));
            params.add(new BasicNameValuePair("nama_belakang", nBel));
            params.add(new BasicNameValuePair("tempat_lahir", tLahir));
            params.add(new BasicNameValuePair("tgl_lahir", tglL));
            params.add(new BasicNameValuePair("no_hp", NOhp));
            params.add(new BasicNameValuePair("alamat", aLamat));
            params.add(new BasicNameValuePair("email", Email));

            JSONObject jsonObject = jsonParser.makeHttpRequest(url_updateuser, "POST", params);
            Log.d("Respon: ", jsonObject.toString());

            try{
                int sukses = jsonObject.getInt(TAG_SUCCESS);
                if (sukses==1){
                    FragmentProfilUser.this.params.put("id", ""+id);
                    Intent i = new Intent(getActivity(), Dashboard_Admin.class);
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
            uploadImage();
            pDialog.dismiss();

        }
    }
}
