package com.example.user.sirent.Admin;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentBookingKendaraan extends Fragment {

    SharedPref sharedPref;
    private Booking_AdminAdapter bookingAdminAdapter;
    JSONParser jsonParser = new JSONParser();
    private static String url_booking = "http://sirent.esy.es/selectall_booking.php";
    public static final String TAG_SUCCESS = "sukses";
    public static final String TAG_BOOK = "booking";
    public static final String TAG_ID = "ID_kendaraan";
    public static final String TAG_KENDARAAN = "nama_kendaraan";
    public static final String TAG_STATUSBOOK = "status_rental";
    public static final String TAG_SEWA = "tgl_sewa";
    public static final String TAG_KEMBALI = "tgl_kembali";
    public static final String TAG_ALAMAT = "alamat";
    public static final String TAG_INDEX = "index";

    int id, idToko;
    ListView lv;
    JSONArray booking = null;
    ArrayList<HashMap<String,String>> booklist = new ArrayList<HashMap<String, String>>();


    public FragmentBookingKendaraan() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragmen_booking_kendaraan, container, false);
        sharedPref = new SharedPref(getActivity().getApplicationContext());
        id = sharedPref.getUserID();
        idToko = sharedPref.getTokoID();

        lv = (ListView)view.findViewById(R.id.list);
        new loadBooking().execute();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String pID = ((TextView)view.findViewById(R.id.ID)).getText().toString();
                String status = ((TextView)view.findViewById(R.id.sttus)).getText().toString();
                Intent intent = new Intent(getActivity().getApplicationContext(), DetailBookingAdmin.class);
                intent.putExtra("ID", pID);
                intent.putExtra("status", status);
                startActivityForResult(intent, 100);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new loadBooking().execute();

    }

    private class loadBooking extends AsyncTask<String,String,String>{
        ProgressDialog pDialog = new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Loading");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id",""+idToko));
            Log.d("Toko: ", String.valueOf(idToko));

            JSONObject jObj = jsonParser.makeHttpRequest(url_booking, "GET", params);
            Log.d("Book: ", jObj.toString());

            try{
                int sukses = jObj.getInt(TAG_SUCCESS);
                if (sukses == 1){
                    booking = jObj.getJSONArray(TAG_BOOK);
                    booklist.removeAll(booklist);
                    for (int i = 0;i < booking.length();i++) {
                        JSONObject obj = booking.getJSONObject(i);
                        HashMap<String,String> map = new HashMap<String, String>();
                        String id = obj.getString(TAG_ID);
                        String nKend = obj.getString(TAG_KENDARAAN);
                        String sttus = obj.getString(TAG_STATUSBOOK);
                        String tsewa = obj.getString(TAG_SEWA);
                        String tkembali = obj.getString(TAG_KEMBALI);
                        String almt = obj.getString(TAG_ALAMAT);

                        map.put(TAG_ID, id);
                        map.put(TAG_KENDARAAN, nKend);
                        map.put(TAG_STATUSBOOK, sttus);
                        map.put(TAG_SEWA, tsewa);
                        map.put(TAG_KEMBALI, tkembali);
                        map.put(TAG_ALAMAT, almt);
                        map.put(TAG_INDEX, ""+i);

                        booklist.add(map);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String s) {
            pDialog.dismiss();
            pDialog.dismiss();

            if(bookingAdminAdapter != null)
            {
                bookingAdminAdapter.notifyDataSetChanged();
                return;
            }
            bookingAdminAdapter = new Booking_AdminAdapter(getContext(), booklist);
            lv.setAdapter(bookingAdminAdapter);

        }
    }

}
