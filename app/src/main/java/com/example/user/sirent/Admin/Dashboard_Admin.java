package com.example.user.sirent.Admin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.sirent.AkunProfil;
import com.example.user.sirent.Image.ImageLoader;
import com.example.user.sirent.JSON.JSONParser;
import com.example.user.sirent.R;
import com.example.user.sirent.SharedPref;
import com.example.user.sirent.login.login_dashboard;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by User on 12/24/2016.
 */

public class Dashboard_Admin extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Fragment_Kendaraan_Admin.OnFragmentInteractionListener {

    private static int RESULT_LOAD_IMG = 1;
    private static String url_products_details = "http://sirent.esy.es/select_profiluser.php";
    //private static String url_updatestatus = "http://sirent.esy.es/update_statususer.php";
    private static final String TAG_SUCCESS = "sukses";
    JSONParser jsonParser = new JSONParser();
    RequestParams params = new RequestParams();
    private SharedPref sharedPref;

    //private ProgressDialog progressDialog;
    TextView userview, emailview;
    String imgPath, fileName;
    String encodedString;
    Bitmap bitmap;
    public ImageLoader imageLoader;
    int id, idToko;
    ImageView image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout_admin);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,  R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view_admin);
        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState == null) {
            Fragment fragment = null;
            Class fragmentClass = null;
            fragmentClass = FragmentKelolaKendaraan.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContentAdmin, fragment).commit();
        }

        imageLoader = new ImageLoader(this);
        View header = navigationView.getHeaderView(0);
        userview = (TextView) header.findViewById(R.id.userview);
        emailview = (TextView) header.findViewById(R.id.emailview);
        image = (ImageView)header.findViewById(R.id.imageadmin);


        sharedPref = new SharedPref(getApplicationContext());
        id = sharedPref.getUserID();
        idToko = sharedPref.getTokoID();
        Toast.makeText(this, "ID Toko: "+idToko, Toast.LENGTH_SHORT).show();

        new getProfilDetail().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.tambahKend) {
            Intent i = new Intent(Dashboard_Admin.this, Admin_InsertKend.class);
            startActivity(i);
            //Toast.makeText(this, "Ini nambah", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout_admin);
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        /*int id = item.getItemId();
        if (id == R.id.nav_kendaraan) {

        } else if (id == R.id.nav_booking) {

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_logout) {
            logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_admin);
        drawer.closeDrawer(GravityCompat.START);*/
        displaySelected(item.getItemId());
        return true;
    }

    private void displaySelected (int itemID){
        Fragment fragment = null;

        switch (itemID){
            case R.id.nav_kendaraan:
                //fragment = new Fragment_Kendaraan_Admin();
                fragment = new FragmentKelolaKendaraan();
                /*Intent i = new Intent(Dashboard_Admin.this, Kelola_KendaraanAdmin.class);
                startActivity(i);*/
                break;
            case R.id.nav_booking:
                fragment = new FragmentBookingKendaraan();
                /*Intent in = new Intent(Dashboard_Admin.this, Booking_Admin.class);
                startActivity(in);*/
                break;
            case R.id.nav_about:
                Toast.makeText(this, "Ini About", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                logoutUser();
                break;

        }

        if (fragment!=null){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.flContentAdmin, fragment);
            fragmentTransaction.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_admin);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void logoutUser() {
        sharedPref.setLoginAdmin(false);
        Intent intent = new Intent(Dashboard_Admin.this, login_dashboard.class);
        finish();
        startActivity(intent);
    }

    public void profil(View view){
        Intent intent = new Intent(Dashboard_Admin.this, AkunProfil.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new getProfilDetail().execute();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    class getProfilDetail extends AsyncTask<String, String, List<String>> {
        @Override
        protected void onPreExecute() {

        }


        protected List<String> doInBackground(String... arg0) {
            int sukses;
            List<String> list = new ArrayList<String>();
            try{
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("id",""+id));

                JSONObject json = jsonParser.makeHttpRequest(url_products_details, "GET", params);

                Log.d("Background: ", json.toString() + " " +id);
                sukses = json.getInt(TAG_SUCCESS);
                if (sukses == 1){
                    JSONArray productObj = json.getJSONArray("profil");

                    JSONObject profil = productObj.getJSONObject(0);
                    list.add(profil.getString("nama_depan"));
                    list.add(profil.getString("nama_belakang"));
                    list.add(profil.getString("email"));
                    list.add(profil.getString("foto_user").replace("\\", ""));

                    //list.add(profil.getString("foto_user").replace("\\", ""));
                }else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return list;
        }


        protected void onPostExecute(List<String> result) {
            userview.setText(result.get(0)+ " " +result.get(1));
            emailview.setText(result.get(2));
            imageLoader.DisplayImage(result.get(3), Dashboard_Admin.this.image);
            //foto belum
        }
    }
}
