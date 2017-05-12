package com.example.user.sirent.Admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.sirent.JSON.JSONParser;
import com.example.user.sirent.R;
import com.example.user.sirent.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class DetailBookingAdmin extends AppCompatActivity {

    int id, idToko;
    String idKend, update, decl, st_sewa;
    EditText namakend, noplat, tglsewa, tglkembali, penyewa, status, alamat;
    Button konfirm, decline;
    TextView idRental;

    private static String url_getbook = "http://sirent.esy.es/select_booking.php";
    private static String url_confirmbook = "http://sirent.esy.es/update_statusbook.php";
    public static final String TAG_SUCCESS = "sukses";
    public static final String TAG_BOOK = "booking";
    public static final String TAG_ID = "ID_kendaraan";
    public static final String TAG_KENDARAAN = "nama_kendaraan";
    public static final String TAG_TSEWA= "tgl_sewa";
    public static final String TAG_PLAT = "no_plat";
    public static final String TAG_TKEMBALI= "tgl_kembali";
    public static final String TAG_NAMABEL = "nama_belakang";
    public static final String TAG_STATUS= "status_rental";
    JSONParser jsonParser = new JSONParser();

    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_bookingadmin);

        Intent i = getIntent();
        idKend = i.getStringExtra("ID");
        st_sewa = i.getStringExtra("status");


        idRental = (TextView)findViewById(R.id.idRental);
        konfirm = (Button)findViewById(R.id.confirm);
        //decline = (Button)findViewById(R.id.decline);

        namakend = (EditText)findViewById(R.id.kend);
        noplat = (EditText)findViewById(R.id.plat);
        tglsewa = (EditText)findViewById(R.id.tgl_sewa);
        tglkembali = (EditText)findViewById(R.id.tgl_kembali);
        penyewa = (EditText)findViewById(R.id.penyewa);
        status = (EditText)findViewById(R.id.sttus);
        alamat = (EditText)findViewById(R.id.alamat_pengiriman);

        sharedPref = new SharedPref(getApplicationContext());
        idToko = sharedPref.getTokoID();
        new getBookDetail().execute();

        if (st_sewa.equals("booking")){
            konfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder aDialog = new AlertDialog.Builder(DetailBookingAdmin.this);
                    aDialog.setMessage("Terima pesanan ini ?");
                    aDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            update = "confirm";
                            new updateConfirm().execute();
                        }
                    });
                    aDialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            decl = "decline";
                            new updateDecline().execute();
                        }
                    });
                    AlertDialog alert = aDialog.create();
                    alert.show();
                }
            });
        } else {
            konfirm.setEnabled(false);
        }




    }

    private class getBookDetail extends AsyncTask<String, String, List<String>> {
        @Override
        protected List<String> doInBackground(String... args) {
            int sukses;
            List<String> list = new ArrayList<String>();
            try{
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("id",""+idKend));
                JSONObject object = jsonParser.makeHttpRequest(url_getbook, "GET", params);
                Log.d("Detail: ", object.toString());
                sukses = object.getInt(TAG_SUCCESS);
                if (sukses == 1){
                    JSONArray jsonArray = object.getJSONArray(TAG_BOOK);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    list.add(jsonObject.getString("ID_kendaraan"));
                    list.add(jsonObject.getString("ID_rental"));
                    list.add(jsonObject.getString("nama_kendaraan"));
                    list.add(jsonObject.getString("no_plat"));
                    list.add(jsonObject.getString("tgl_sewa"));
                    list.add(jsonObject.getString("tgl_kembali"));
                    list.add(jsonObject.getString("nama_belakang"));
                    list.add(jsonObject.getString("status_rental"));
                    list.add(jsonObject.getString("alamat"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return list;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            idRental.setText(result.get(1));
            namakend.setText(result.get(2));
            noplat.setText(result.get(3));
            tglsewa.setText(result.get(4));
            tglkembali.setText(result.get(5));
            penyewa.setText(result.get(6));
            status.setText(result.get(7));
            alamat.setText(result.get(8));
        }
    }

    private class updateConfirm extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... args) {
            String IDkend = idKend;
            String confirm = update;
            String iDrent = idRental.getText().toString();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id",""+iDrent));
            params.add(new BasicNameValuePair("confirm",""+confirm));
            params.add(new BasicNameValuePair("ID_kendaraan",""+IDkend));


            JSONObject object = jsonParser.makeHttpRequest(url_confirmbook, "POST", params);
            Log.d("Update: ", object.toString());

            try{
                int sukses = object.getInt(TAG_SUCCESS);
                if (sukses == 1){
                    Intent i = new Intent(DetailBookingAdmin.this, Booking_Admin.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private class updateDecline extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... args) {
            String IDkend = idKend;
            String iDrent = idRental.getText().toString();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id",""+iDrent));
            params.add(new BasicNameValuePair("ID_kendaraan",""+IDkend));


            JSONObject object = jsonParser.makeHttpRequest(url_confirmbook, "POST", params);
            Log.d("Update: ", object.toString());

            try{
                int sukses = object.getInt(TAG_SUCCESS);
                if (sukses == 1){
                    Intent i = new Intent(DetailBookingAdmin.this, Booking_Admin.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
