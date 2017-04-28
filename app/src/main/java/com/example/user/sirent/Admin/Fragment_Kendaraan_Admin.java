package com.example.user.sirent.Admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.sirent.Utility.AppURLs;
import com.example.user.sirent.R;
import com.example.user.sirent.Utility.DataKendaraan;
import com.example.user.sirent.Utility.GetKendaraan;
import com.example.user.sirent.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;




/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_Kendaraan_Admin.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class Fragment_Kendaraan_Admin extends Fragment {

    List<GetKendaraan> getKendaraan;
    public static final int CONN_TIMEOUT = 10000;
    public static final int CONN_READOUT = 15000;
    private RecyclerView recyclerView;
    private AdapterKendaraanAdmin adapterKendaraan;
    private static String url_kendaraan = "http://sirent.esy.es/selectall_kendaraan1.php";


    JsonArrayRequest jsonReq;
    RequestQueue reqQueue;

    SharedPref shared;
    int id;

    private OnFragmentInteractionListener mListener;

    public Fragment_Kendaraan_Admin() {
        // Required empty public constructor
    }

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //shared = (SharedPref) this.getActivity().getSharedPreferences("SIRent", Context.MODE_PRIVATE);
        //id = shared.getUserID();
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kendaraan_admin, container, false);


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new viewKendaraan().execute();
        dataCall();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class viewKendaraan extends AsyncTask <String, String, String>{
        ProgressDialog p = new ProgressDialog(getActivity());
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p.setMessage("Loading");
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                url = new URL(url_kendaraan);
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
                return e1.toString();
            }
            try{
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(CONN_READOUT);
                conn.setConnectTimeout(CONN_TIMEOUT);
                conn.setRequestMethod("GET");
                conn.setDoOutput(true);
            } catch (IOException e2) {
                e2.printStackTrace();
                return e2.toString();
            }
            try{
                int response_code = conn.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK){
                    InputStream inputStream = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) !=null){
                        result.append(line);
                    }
                    return (result.toString());
                }else {
                    Toast.makeText(getActivity(), "Koneksi jaringan tidak stabil", Toast.LENGTH_SHORT).show();
                    return "tidak sukses";
                }

            } catch (IOException e3) {
                e3.printStackTrace();
                e3.toString();
            }finally {
                conn.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            p.dismiss();
            List<DataKendaraan> data = new ArrayList<>();
            //data.add(new BasicNameValuePair("id", ""+id));
            p.dismiss();
            try{
             JSONObject object = new JSONObject(result);
                JSONArray jsonArray = object.getJSONArray("kendaraan");
                for (int i=0;i < jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    DataKendaraan dataKendaraan = new DataKendaraan();
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
                recyclerView = (RecyclerView)getActivity().findViewById(R.id.recyclerView1);
                adapterKendaraan = new AdapterKendaraanAdmin(getActivity(), data);
                recyclerView.setAdapter(adapterKendaraan);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            } catch (JSONException e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void dataCall(){
        jsonReq = new JsonArrayRequest(AppURLs.url_selectkendaraan,
        new Response.Listener<JSONArray>(){

            @Override
            public void onResponse(JSONArray response) {
                afterCall(response);
            }
        },
        new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        reqQueue = Volley.newRequestQueue(getContext());
        reqQueue.add(jsonReq);
    }

    public void afterCall(JSONArray array){
        for (int i = 0; i <array.length();i++){
            GetKendaraan getKendaraan1 = new GetKendaraan();
            JSONObject jsonObj = null;
            try {
                jsonObj = array.getJSONObject(i);
                getKendaraan1.setNama_kendaraan(jsonObj.getString("nama_kendaraan"));
                getKendaraan1.setNo_plat(jsonObj.getString("no_plat"));
                getKendaraan1.setTipe_kendaraan(jsonObj.getString("tipe_kendaraan"));
                getKendaraan1.setMerk_kendaraan(jsonObj.getString("merk_kendaraan"));
                getKendaraan1.setNama_toko(jsonObj.getString("nama_toko"));
                getKendaraan1.setHarga_sewa(jsonObj.getString("harga_sewa"));
                getKendaraan1.setJumlah(jsonObj.getString("jumlah"));
                getKendaraan1.setDeskripsi(jsonObj.getString("deskripsi"));
                getKendaraan1.setGambar(jsonObj.getString("gambar"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
