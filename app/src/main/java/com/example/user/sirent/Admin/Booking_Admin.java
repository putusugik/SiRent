package com.example.user.sirent.Admin;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.sirent.JSON.JSONParser;
import com.example.user.sirent.R;
import com.example.user.sirent.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class Booking_Admin extends AppCompatActivity {

    SharedPref sharedPref;
    private Booking_AdminAdapter bookingAdminAdapter;

    JSONParser jsonParser = new JSONParser();

    private static String url_booking = "http://sirent.esy.es/selectall_booking.php";
    public static final String TAG_SUCCESS = "sukses";
    public static final String TAG_BOOK = "booking";
    public static final String TAG_ID = "ID_kendaraan";
    public static final String TAG_KENDARAAN = "nama_kendaraan";
    public static final String TAG_STATUSBOOK = "status_rental";
    public static final String TAG_TGLSEWA = "tgl_sewa";
    public static final String TAG_TGLKEM = "tgl_kembali";
    public static final String TAG_ALAMAT = "alamat";
    public static final String TAG_INDEX = "index";

    int id, idToko;
    ListView lv;
    JSONArray booking = null;
    ArrayList<HashMap<String,String>> booklist = new ArrayList<HashMap<String, String>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_admin);

        sharedPref = new SharedPref(getApplicationContext());
        id = sharedPref.getUserID();
        idToko = sharedPref.getTokoID();
        //Toast.makeText(this, "idtoko: "+idToko, Toast.LENGTH_SHORT).show();
        Intent i = getIntent();
        String ts = i.getStringExtra("message");
        Toast.makeText(this, "Res: "+ts, Toast.LENGTH_SHORT).show();

        lv = (ListView)findViewById(R.id.list);
        new loadBooking().execute();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String pID = ((TextView)view.findViewById(R.id.ID)).getText().toString();
                Intent intent = new Intent(Booking_Admin.this, DetailBookingAdmin.class);
                intent.putExtra("ID", pID);
                startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new loadBooking().execute();
    }

    private class loadBooking extends AsyncTask<String, String,String> {
        @Override
        protected String doInBackground(String... args) {
            Log.d("ID:", String.valueOf(idToko));
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id",""+idToko));

            JSONObject jObject = jsonParser.makeHttpRequest(url_booking, "GET", params);
            Log.d("book: ", jObject.toString());

            try{
                int sukses = jObject.getInt(TAG_SUCCESS);
                if (sukses == 1){
                    booking = jObject.getJSONArray(TAG_BOOK);
                    booklist.removeAll(booklist);
                    for (int i = 0;i < booking.length();i++) {
                        JSONObject obj = booking.getJSONObject(i);
                        HashMap<String,String> map = new HashMap<String, String>();
                        String id = obj.getString(TAG_ID);
                        String nKend = obj.getString(TAG_KENDARAAN);
                        String sttus = obj.getString(TAG_STATUSBOOK);
                        String tGlse = obj.getString(TAG_TGLSEWA);
                        String tGlkem = obj.getString(TAG_TGLKEM);
                        String alamat = obj.getString(TAG_ALAMAT);

                        map.put(TAG_ID, id);
                        map.put(TAG_KENDARAAN, nKend);
                        map.put(TAG_STATUSBOOK, sttus);
                        map.put(TAG_TGLSEWA, tGlse);
                        map.put(TAG_TGLKEM, tGlkem);
                        map.put(TAG_ALAMAT, alamat);
                        map.put(TAG_INDEX, ""+i);

                        booklist.add(map);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(bookingAdminAdapter != null)
            {
                bookingAdminAdapter.notifyDataSetChanged();
                return;
            }
            bookingAdminAdapter = new Booking_AdminAdapter(Booking_Admin.this, booklist);
            lv.setAdapter(bookingAdminAdapter);
        }
    }
}
