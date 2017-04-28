package com.example.user.sirent.Admin;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.sirent.AkunProfil;
import com.example.user.sirent.JSON.JSONParser;
import com.example.user.sirent.R;
import com.example.user.sirent.SharedPref;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapsAdmin extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    int id, idToko;
    TextView idUser, tLat, tLng;
    String idPemilik;
    Double mLat, mLng;
    SharedPref sharedPref;
    JSONParser jsonParser = new JSONParser();
    private static String url_updateAlamat = "http://sirent.esy.es/update_alamatToko.php";
    private static String url_selectAlamat = "http://sirent.esy.es/select_profiltoko.php";
    public static final String TAG_SUCCESS = "sukses";

    JSONArray alamat = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_maps_admin);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        idUser = (TextView) findViewById(R.id.IDpemilik);
        tLat = (TextView) findViewById(R.id.lat);
        tLng = (TextView) findViewById(R.id.lng);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent i = getIntent();
        idPemilik = i.getStringExtra("ID");
        mLat = i.getDoubleExtra("lat", -8.721404);
        mLng = i.getDoubleExtra("lng", 115.182714);
        sharedPref = new SharedPref(getApplicationContext());
        idToko = sharedPref.getTokoID();
        id = sharedPref.getUserID();

        idUser.setText(String.valueOf(id));



        //new getAlamat().execute();


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng myLoc = new LatLng(mLat, mLng);
        mMap.addMarker(new MarkerOptions().position(myLoc).title("Lokasi Toko Anda"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 10));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(point));
                double lat = point.latitude;
                double lng = point.longitude;
                String lAt = String.valueOf(lat);
                String lNg = String.valueOf(lng);
                tLat.setText(lAt);
                tLng.setText(lNg);
                AlertDialog.Builder builder = new AlertDialog.Builder(MapsAdmin.this);
                builder.setMessage("Apakah lokasi toko ini benar ?")
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //update tabel
                                new updateAlamat().execute();
                                //MapsAdmin.this.finish();
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //pilih lagi lokasi
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });
    }


    private class getAlamat extends AsyncTask<String, String, Location> {

        @Override
        protected Location doInBackground(String... args) {
            List<String> list = new ArrayList<String>();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id", "" + id));

            JSONObject jObj = jsonParser.makeHttpRequest(url_selectAlamat, "GET", params);
            Log.d("Lokasi: ", jObj.toString());

            try {
                int sukses = jObj.getInt(TAG_SUCCESS);
                if (sukses == 1) {
                    alamat = jObj.getJSONArray("profil");
                    JSONObject object = alamat.getJSONObject(0);
                    list.add(object.getString("ID"));
                    list.add(object.getString("lat"));
                    list.add(object.getString("lng"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Location loc) {
            /*double lat = Double.parseDouble(s.get(1));
            double lng = Double.parseDouble(s.get(2));
            onMapReady(mMap.addMarker(new MarkerOptions().position(lat+","+lng)));*/
            //mMap.addMarker(new MarkerOptions().position(loc));
        }
    }

    private class updateAlamat extends AsyncTask<String, String, String> {
        ProgressDialog prg = new ProgressDialog(MapsAdmin.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prg.setMessage("Memperbaharui");
            prg.setCancelable(false);
            prg.show();
        }

        @Override
        protected String doInBackground(String... args) {
            String ID_user = idUser.getText().toString();
            String lLat = tLat.getText().toString();
            String lLng = tLng.getText().toString();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_pemilik", "" + ID_user));
            params.add(new BasicNameValuePair("lat", lLat));
            params.add(new BasicNameValuePair("lng", lLng));

            JSONObject jObj = jsonParser.makeHttpRequest(url_updateAlamat, "POST", params);
            Log.d("Alamat: ", jObj.toString());

            try {
                int sukses = jObj.getInt(TAG_SUCCESS);
                if (sukses == 1) {
                    Intent i = new Intent(MapsAdmin.this, AkunProfil.class);
                    i.putExtra("id", ID_user);
                    startActivityForResult(i, 100);
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
            prg.dismiss();
            Toast.makeText(MapsAdmin.this, "Alamat berhasil diperbaharui", Toast.LENGTH_SHORT).show();
        }
    }
}
