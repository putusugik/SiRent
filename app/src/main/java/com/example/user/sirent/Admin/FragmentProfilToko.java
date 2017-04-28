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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.sirent.Image.ImageLoader;
import com.example.user.sirent.JSON.JSONParser;
import com.example.user.sirent.R;
import com.example.user.sirent.SharedPref;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProfilToko extends Fragment {

    SharedPref sharedPref;
    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;
    private static int RESULT_LOAD_IMG = 1;
    private static String url_profiltoko = "http://sirent.esy.es/select_profiltoko.php";
    private static String url_updateprofiltoko = "http://sirent.esy.es/update_profiltoko.php";
    public static final String TAG_SUCCESS = "sukses";
    public static final String TAG_NAMATOKO = "nama_toko";
    public static final String TAG_SLOGAN= "slogan";
    public static final String TAG_ALAMAT = "alamat";
    public static final String TAG_NOTELP = "no_telp";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_JAMBUKA = "jam_buka";
    public static final String TAG_JAMTTUP = "jam_tutup";
    public static Double Mlat, Mlng;

    RequestParams params = new RequestParams();
    EditText id_pemilik, namaToko, alamatToko, noHP_toko, emailToko,
        jam_Buka, jam_Tutup, lat, lng;
    ImageView image;
    int id, id_Pemilik, id_toko;
    String imgPath, fileName, encodedString;
    Bitmap bitmap;
    public ImageLoader imageLoader;
    MapView mapView;
    GoogleMap map;
    Button edit, lokasi, simpan;

    public FragmentProfilToko() {
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
        View v = inflater.inflate(R.layout.fragment_profiltoko, container, false);
        lokasi = (Button)v.findViewById(R.id.lokasi);
        simpan = (Button)v.findViewById(R.id.simpanTok);

        id_pemilik = (EditText)v.findViewById(R.id.idpemilik);
        namaToko = (EditText)v.findViewById(R.id.edit_namatoko);
        alamatToko = (EditText)v.findViewById(R.id.edit_alamat);
        noHP_toko = (EditText)v.findViewById(R.id.edit_notel);
        emailToko = (EditText)v.findViewById(R.id.edit_eMail);
        jam_Buka = (EditText)v.findViewById(R.id.edit_jamBuka);
        jam_Tutup = (EditText)v.findViewById(R.id.edit_jamTutup);
        lat = (EditText)v.findViewById(R.id.lat);
        lng = (EditText)v.findViewById(R.id.lng);
        image = (ImageView)v.findViewById(R.id.imageToko);

        imageLoader = new ImageLoader(this.getActivity());

        sharedPref= new SharedPref(getActivity().getApplicationContext());
        id = sharedPref.getUserID();
        id_toko = sharedPref.getTokoID();

        new getProfilToko().execute();

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, RESULT_LOAD_IMG);
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (namaToko.isEnabled()==false){
                    namaToko.setEnabled(true);
                    alamatToko.setEnabled(true);
                    noHP_toko.setEnabled(true);
                    emailToko.setEnabled(true);
                    jam_Buka.setEnabled(true);
                    jam_Tutup.setEnabled(true);
                    lokasi.setEnabled(true);
                    simpan.setText("Perbaharui");
                }else if (namaToko.isEnabled()==true){
                    new updateProfilToko().execute();
                    simpan.setText("Edit");
                }
            }
        });
        lokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String pID = ((TextView)v.findViewById(R.id.idpemilik)).getText().toString();
                Intent i = new Intent(getActivity(), MapsAdmin.class);
                i.putExtra("ID",id);
                i.putExtra("lat", Mlat);
                i.putExtra("lng", Mlng);
                startActivityForResult(i, 100);
                getActivity().finish();
            }
        });


        /*lokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapsAdmin mapsAdmin = new MapsAdmin();
                FragmentManager fManage = getFragmentManager();
                FragmentTransaction fTrans = fManage.beginTransaction();
                fTrans.replace(R.id.frame1, mapsAdmin);
                fTrans.addToBackStack(null);
                fTrans.commit();
            }
        });*/
        /*mapView = (MapView) v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(true);*/

        return v;
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

    @Override
    public void onResume() {
        super.onResume();
        new getProfilToko().execute();
    }

    private class getProfilToko extends AsyncTask<String, String, List<String>> {
        @Override
        protected List<String> doInBackground(String... args) {
            int sukses;
            List<String> list = new ArrayList<String>();
            try{
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("id",""+id));

                JSONObject jObj = jsonParser.makeHttpRequest(url_profiltoko, "GET", params);
                Log.d("Cek: ", jObj.toString());

                sukses = jObj.getInt(TAG_SUCCESS);
                if (sukses == 1){
                    JSONArray jArray = jObj.getJSONArray("profil");
                    JSONObject object = jArray.getJSONObject(0);
                    list.add(object.getString("ID"));
                    list.add(object.getString("nama_toko"));
                    list.add(object.getString("alamat"));
                    list.add(object.getString("no_telp"));
                    list.add(object.getString("email"));
                    list.add(object.getString("jam_buka"));
                    list.add(object.getString("jam_tutup"));
                    list.add(object.getString("lat"));
                    list.add(object.getString("lng"));
                    list.add(object.getString("id_pemilik"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            id_pemilik.setText(strings.get(0));
            namaToko.setText(strings.get(1));
            alamatToko.setText(strings.get(2));
            noHP_toko.setText(strings.get(3));
            emailToko.setText(strings.get(4));
            jam_Buka.setText(strings.get(5));
            jam_Tutup.setText(strings.get(6));
            lat.setText(strings.get(7));
            lng.setText(strings.get(8));
            Mlat = Double.valueOf(strings.get(7));
            Mlng= Double.valueOf(strings.get(8));
        }
    }

    private class updateProfilToko extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FragmentProfilToko.this.getActivity());
            pDialog.setMessage("Menyimpan");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            String ID_user = id_pemilik.getText().toString();
            String nToko = namaToko.getText().toString();
            String Alamat = alamatToko.getText().toString();
            String noHP = noHP_toko.getText().toString();
            String eMail = emailToko.getText().toString();
            String jBuka = jam_Buka.getText().toString();
            String jTutup = jam_Tutup.getText().toString();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_pemilik",""+ID_user));
            params.add(new BasicNameValuePair("nama_toko", nToko));
            params.add(new BasicNameValuePair("alamat", Alamat));
            params.add(new BasicNameValuePair("no_telp", noHP));
            params.add(new BasicNameValuePair("email", eMail));
            params.add(new BasicNameValuePair("jam_buka", jBuka));
            params.add(new BasicNameValuePair("jam_tutup", jTutup));

            JSONObject jsonObject = jsonParser.makeHttpRequest(url_updateprofiltoko, "POST", params);
            Log.d("Res: ", jsonObject.toString());

            try{
                int sukses = jsonObject.getInt(TAG_SUCCESS);
                if (sukses==1){
                    FragmentProfilToko.this.params.put("id",""+id);
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
            super.onPostExecute(s);
            pDialog.dismiss();
        }
    }

   /* public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng sydney = new LatLng(-34, 151);
        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }*/
}