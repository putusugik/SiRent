package com.example.user.sirent.User;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.sirent.Image.ImageLoader;
import com.example.user.sirent.JSON.JSONParser;
import com.example.user.sirent.R;
import com.example.user.sirent.SharedPref;
import com.example.user.sirent.Utility.DataKendaraan;
import com.example.user.sirent.Utility.DividerItemDecoration;
import com.example.user.sirent.login.login_dashboard;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        Fragment_Kendaraan.OnFragmentInteractionListener, AdapterKendaraan.OnCardClickListner {

    private static String url_products_details = "http://sirent.esy.es/select_profiluser.php";
    private static String url_updatestatus = "http://sirent.esy.es/update_statususer.php";
    private static String url_carikend = "http://sirent.esy.es/select_carikendaraan.php";

    private static final String TAG_SUCCESS = "sukses";
    JSONParser jsonParser = new JSONParser();
    RequestParams params = new RequestParams();
    private SharedPref sharedPref;
    public ImageLoader imageLoader;
    private MenuItem mSearchAction;
    RecyclerView rView;
    private AdapterKendaraan aKendaraan;
    List<DataKendaraan> data  = new ArrayList<>();
    private boolean isSearchOpened = false;
    private EditText edtSearch;
    String nama;

    TextView userview, emailview;
    ImageView image;
    int id;

    private ProgressDialog progressDialog;
    String[] colors = new String[]{
            "Sewa Termurah-Sewa Termahal",
            "Sewa Termahal-Sewa Termurah",
            "Mobil",
            "Motor",

    };
    final boolean[] checkedColors = new boolean[]{
            false,
            false,
            false,
            false,


    };
    final List<String> colorsList = Arrays.asList(colors);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState == null) {
            Fragment fragment = null;
            Class fragmentClass = null;
            fragmentClass = Fragment_Kendaraan.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        }

        imageLoader = new ImageLoader(this);
        View header = navigationView.getHeaderView(0);
        userview = (TextView) header.findViewById(R.id.userview);
        emailview = (TextView) header.findViewById(R.id.emailview);
        image = (ImageView)header.findViewById(R.id.imageadmin);

        sharedPref = new SharedPref(getApplicationContext());
        id = sharedPref.getUserID();

        ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            Toast.makeText(this, "Online", Toast.LENGTH_SHORT).show();
            new getProfilDetail().execute();

        }
        else if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTING) {
            Toast.makeText(this, "Offline hahaha", Toast.LENGTH_SHORT).show();
            // notify user you are not online
        }


    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSearchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_search:
                handleSearch();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleSearch() {
        Toast.makeText(this, "Tesss", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder alert = new AlertDialog.Builder(Dashboard.this);
        LayoutInflater infalter = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = infalter.inflate(R.layout.cari_kendaraan, null);
        final EditText ecari = (EditText)v.findViewById(R.id.t_cari);
        final RadioButton rNama = (RadioButton)v.findViewById(R.id.rNamakend);
        final RadioButton rJenis = (RadioButton)v.findViewById(R.id.rJenisKend);
        final RadioButton rMotor= (RadioButton)v.findViewById(R.id.rmotor);
        final RadioButton rMobil = (RadioButton)v.findViewById(R.id.rmobil);
        final RadioButton rMurah = (RadioButton)v.findViewById(R.id.rmurah);
        final RadioButton rMahal = (RadioButton)v.findViewById(R.id.rmahal);
        final RadioGroup rgCari = (RadioGroup)v.findViewById(R.id.RGcari);
        final RadioGroup rgKend = (RadioGroup)v.findViewById(R.id.RGkend);
        final RadioGroup rgpilih = (RadioGroup)v.findViewById(R.id.RGpilih);
        rMotor.setEnabled(false);
        rMobil.setEnabled(false);
        rMurah.setEnabled(false);
        rMahal.setEnabled(false);
        ecari.setEnabled(false);
        rgCari.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.rNamakend:
                        Toast.makeText(Dashboard.this, "Pilih Nama Kendaraan", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rJenisKend:
                        Toast.makeText(Dashboard.this, "Pilih Jenis Kendaraan", Toast.LENGTH_SHORT).show();
                        break;
                }

                if (rNama.isChecked()){
                    if (rMotor.isEnabled()){
                        rMotor.setEnabled(false);
                        rMobil.setEnabled(false);
                        rMurah.setEnabled(false);
                        rMahal.setEnabled(false);
                        ecari.setEnabled(true);
                    }
                    ecari.setEnabled(true);
                } else if (rJenis.isChecked()){
                    rMotor.setEnabled(true);
                    rMobil.setEnabled(true);
                    rMurah.setEnabled(true);
                    rMahal.setEnabled(true);
                    ecari.setEnabled(false);
                }
            }
        });
        alert.setView(v);
        alert.setTitle("Cari Kendaraan");
        alert.setNeutralButton("Cari", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean isByName = false;
                boolean ismobil = false;
                boolean isasc = false;

                int jenis = 1;
                int order = 0;

                if (rNama.isChecked()){
                    isByName = true;
                    nama = ecari.getText().toString();
                    Toast.makeText(Dashboard.this, ""+ecari.getText().toString(), Toast.LENGTH_SHORT).show();
                    new searchKend().execute("byName", ecari.getText().toString());
                } else {
                    if (rMobil.isChecked()){
                        ismobil = true; //harga naik dan harga turun
                        jenis = 2;
                    }
                    if (rMahal.isChecked()){
                        isasc = false;
                        order = 1;
                    }
                    new searchKend().execute("byJenis", ""+jenis, ""+order);
                }


            }
        });
        AlertDialog builder = alert.create();
        builder.show();
        /*final ActionBar action = getSupportActionBar();
        if (isSearchOpened){
            action.setDisplayShowCustomEnabled(false);
            action.setDisplayShowTitleEnabled(true);

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edtSearch.getWindowToken(), 0);
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.places_ic_search));
            isSearchOpened = false;
        } else {
            action.setDisplayShowCustomEnabled(true);
            action.setCustomView(R.layout.cari_kendaraan);
            action.setDisplayShowTitleEnabled(false);
            edtSearch = (EditText)action.getCustomView().findViewById(R.id.t_cari);
            edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH){
            //            doseacrh();

                        return  true;
                }
                    return false;
                }
            });
            edtSearch.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edtSearch, InputMethodManager.SHOW_IMPLICIT);
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.places_ic_search));
            isSearchOpened = true;

        }*/
    }

    private void displaySelected (int itemID){
        Fragment fragment = null;

        switch (itemID){
            case R.id.nav_kend:
                fragment = new Fragment_Kendaraan();
                break;
            case R.id.nav_statusbook:
                fragment = new Fragment_StatusBookingUser();
                mSearchAction.setVisible(false);
                break;
            case R.id.nav_histori:
                fragment = new Fragment_HistoriUser();
                mSearchAction.setVisible(false);

                break;
            case R.id.nav_myakun:
                mSearchAction.setVisible(false);
                fragment = new Fragment_AkunSaya();
                break;
            case R.id.nav_toko:
                mSearchAction.setVisible(false);
                daftarToko();
                break;
            case R.id.nav_about:
                mSearchAction.setVisible(false);
                Toast.makeText(this, "Ini About", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                mSearchAction.setVisible(false);
                logoutUser();
                break;
        }

        if (fragment!=null){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.flContent, fragment);
            fragmentTransaction.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }





    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        /*int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass =null;


        if (id == R.id.nav_camera) {
            fragmentClass = Fragment_Kendaraan.class;
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage){

        } else if (id == R.id.nav_toko) {

        } else if (id == R.id.nav_about) {
            Toast.makeText(this, "Ini About", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_logout) {
            logoutUser();
        }

        try{
            fragment = (Fragment)fragmentClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);*/
        displaySelected(item.getItemId());
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void logoutUser() {
        sharedPref.setLogin(false);
        Intent intent = new Intent(Dashboard.this, login_dashboard.class);
        finish();
        startActivity(intent);

    }

    public void profil(View view){
        Intent intent = new Intent(Dashboard.this, profil_user.class);
        startActivity(intent);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onCardClicked(View view, int position) {
        Log.d("OnClick", "Card Position" + position);
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
            imageLoader.DisplayImage(result.get(3), Dashboard.this.image);

        }
    }

    private void daftarToko() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Apakah anda ingin membuka toko ?");
        alert.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //new registerToko().execute();
                Intent i = new Intent(Dashboard.this, regisToko.class);
                startActivity(i);
                //buatin activity untuk daftar
            }
        });
        alert.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();


        //Intent intent = new Intent(Dashboard.this, regisToko.class);
        //startActivity(intent);
    }

    class registerToko extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Dashboard.this);
            progressDialog.setMessage("Memproses");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
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
                    Dashboard.this.params.put("ID",""+id);
                    Intent i = new Intent(Dashboard.this, Login.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    private class searchKend extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            if(args[0].equals("byName"))
            {
                //cari sesuai nama ambil di args[1]
                params.add(new BasicNameValuePair("nama", args[1]));

            }
            else
            {
                //cari sesuai jenis dan harga
                // jenis ambil di args[1]
                // harga asc = 0 atau desc = 1 ambil di args[2]
                params.add(new BasicNameValuePair("jenis", args[1]));
                params.add(new BasicNameValuePair("order", args[2]));
            }
            JSONObject jsonObject = jsonParser.makeHttpRequest(url_carikend, "GET", params);
            Log.d("Response: ", jsonObject.toString());

            return jsonObject.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject obj = new JSONObject(result);
                JSONArray jArray = obj.getJSONArray("kendaraan");
                if (data != null){
                    data.clear();
                }
                for (int i = 0; i < jArray.length(); i++){
                    JSONObject jsonObject = jArray.getJSONObject(i);
                    DataKendaraan dataKendaraan = new DataKendaraan();
                    dataKendaraan.ID_kendaraan=jsonObject.getString("ID_kendaraan");
                    dataKendaraan.nama_kendaraan=jsonObject.getString("nama_kendaraan");
                    dataKendaraan.no_plat=jsonObject.getString("no_plat");
                    dataKendaraan.tipe_kendaraan=jsonObject.getString("tipe_kendaraan");
                    dataKendaraan.merk_kendaraan=jsonObject.getString("merk_kendaraan");
                    dataKendaraan.nama_toko=jsonObject.getString("nama_toko");
                    dataKendaraan.harga_sewa=jsonObject.getString("harga_sewa");
                    dataKendaraan.jumlah=jsonObject.getString("jumlah");
                    dataKendaraan.deskripsi=jsonObject.getString("deskripsi");
                    dataKendaraan.gambar=jsonObject.getString("gambar");
                    data.add(dataKendaraan);
                }
                rView = (RecyclerView)findViewById(R.id.recyclerView1);
                aKendaraan = new AdapterKendaraan(Dashboard.this, data);
                aKendaraan.notifyDataSetChanged();
                rView.addItemDecoration(new DividerItemDecoration(Dashboard.this, LinearLayoutManager.VERTICAL));
                rView.setAdapter(aKendaraan);

                aKendaraan.setOnCardClickListner((AdapterKendaraan.OnCardClickListner) Dashboard.this);
                rView.setLayoutManager(new LinearLayoutManager(Dashboard.this));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
