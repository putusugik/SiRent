package com.example.user.sirent.User;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
public class Fragment_StatusBookingUser extends Fragment {

    SharedPref sharedPref;
    private Booking_UserAdapter booking_userAdapter;
    JSONParser jsonParser = new JSONParser();
    private static String url_booking = "http://sirent.esy.es/select_stsbookinguser.php";
    public static final String TAG_SUCCESS = "sukses";
    public static final String TAG_BOOK = "booking";
    public static final String TAG_ID = "ID_kendaraan";
    public static final String TAG_KENDARAAN = "nama_kendaraan";
    public static final String TAG_PLAT = "no_plat";
    public static final String TGL_SEWA = "tgl_sewa";
    public static final String TAG_KEMBALI = "tgl_kembali";
    public static final String TAG_STATUSBOOK = "status_rental";
    public static final String TAG_INDEX = "index";

    int id;
    ListView lv;
    JSONArray booking = null;
    ArrayList<HashMap<String, String>> booklist = new ArrayList<HashMap<String, String>>();


    public Fragment_StatusBookingUser() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_status_booking_user, container, false);
        sharedPref = new SharedPref(getActivity().getApplicationContext());
        id = sharedPref.getUserID();
        lv = (ListView)v.findViewById(R.id.listuser);
        new loadBooking().execute();

        return v;
    }

    private class loadBooking extends AsyncTask<String, String, String>{
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
            params.add(new BasicNameValuePair("id",""+id));

            JSONObject obj = jsonParser.makeHttpRequest(url_booking, "GET", params);
            Log.d("Book: ", obj.toString());

            try {
                int sukses = obj.getInt(TAG_SUCCESS);
                if (sukses==1){
                    booking = obj.getJSONArray(TAG_BOOK);
                    booklist.removeAll(booklist);
                    for (int i = 0;i<booking.length();i++){
                        JSONObject jobj = booking.getJSONObject(i);
                        HashMap<String,String> map = new HashMap<String, String>();
                        String id = jobj.getString(TAG_ID);
                        String nKend = jobj.getString(TAG_KENDARAAN);
                        String nPlat = jobj.getString(TAG_PLAT);
                        String tSewa = jobj.getString(TGL_SEWA);
                        String tKem  = jobj.getString(TAG_KEMBALI);
                        String sts = jobj.getString(TAG_STATUSBOOK);

                        map.put(TAG_ID, id);
                        map.put(TAG_KENDARAAN, nKend);
                        map.put(TAG_PLAT, nPlat);
                        map.put(TGL_SEWA, tSewa);
                        map.put(TAG_KEMBALI, tKem);
                        map.put(TAG_STATUSBOOK, sts);
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
            super.onPostExecute(s);
            pDialog.dismiss();
            if (booking_userAdapter !=null){
                booking_userAdapter.notifyDataSetChanged();
                return;
            }
            booking_userAdapter = new Booking_UserAdapter(getActivity(), booklist);
            lv.setAdapter(booking_userAdapter);
        }
    }
}
