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
public class FragmentKelolaKendaraan extends Fragment {
    SharedPref sharedPref;
    private KelolaKend_Adapter kelolaKend_adapter;
    JSONParser jsonParser = new JSONParser();

    private static String url_kendaraan = "http://sirent.esy.es/select_kendaraan.php";
    public static final String TAG_SUCCESS = "sukses";
    public static final String TAG_KEND = "kendaraan";
    public static final String TAG_ID = "id_kendaraan";
    public static final String TAG_KENDARAAN = "nama_kendaraan";
    public static final String TAG_MESIN = "no_mesin";
    public static final String TAG_PLAT = "no_plat";
    public static final String TAG_INDEX = "index";

    ListView listView;
    int id;
    JSONArray kendaraan = null;
    ArrayList<HashMap<String, String>> kendList = new ArrayList<HashMap<String, String>>();

    public FragmentKelolaKendaraan() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = (View) inflater.inflate(R.layout.fragment_kelola_kendaraan, container, false);
        listView = (ListView)v.findViewById(R.id.list);
        sharedPref = new SharedPref(getActivity().getApplicationContext());
        id = sharedPref.getUserID();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String pID = ((TextView)view.findViewById(R.id.ID)).getText().toString();
                Intent i = new Intent(getActivity().getApplicationContext(), DetailKendaraanAdmin.class);
                i.putExtra("ID", pID);
                startActivityForResult(i, 100);
            }
        });

        return  v;
    }

    @Override
    public void onResume() {
        super.onResume();
        new loadKendaraan().execute();

    }

    private class loadKendaraan extends AsyncTask<String,String,String> {
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

            JSONObject jobject = jsonParser.makeHttpRequest(url_kendaraan, "GET", params);

            Log.d("Kendaraan: ", jobject.toString());

            try{
                int sukses = jobject.getInt(TAG_SUCCESS);
                if (sukses == 1){
                    kendaraan = jobject.getJSONArray(TAG_KEND);
                    kendList.removeAll(kendList);
                    for (int i = 0; i < kendaraan.length();i++){
                        JSONObject obj = kendaraan.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String,String>();
                        String id = obj.getString(TAG_ID);
                        String namaKend = obj.getString(TAG_KENDARAAN);
                        String noMes = obj.getString(TAG_MESIN);
                        String noPlat = obj.getString(TAG_PLAT);

                        map.put(TAG_ID, id);
                        map.put(TAG_KENDARAAN, namaKend);
                        map.put(TAG_MESIN, noMes);
                        map.put(TAG_PLAT, noPlat);
                        map.put(TAG_INDEX, ""+i);

                        kendList.add(map);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            pDialog.dismiss();
            listView = (ListView)getView().findViewById(R.id.list);
            if (kelolaKend_adapter != null){
                kelolaKend_adapter.notifyDataSetChanged();
                return;
            }
            kelolaKend_adapter = new KelolaKend_Adapter(getActivity(), kendList);
            listView.setAdapter(kelolaKend_adapter);
        }
    }

}
