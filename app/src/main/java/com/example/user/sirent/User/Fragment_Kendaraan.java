package com.example.user.sirent.User;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.user.sirent.R;
import com.example.user.sirent.Utility.DataKendaraan;
import com.example.user.sirent.Utility.DividerItemDecoration;

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

import static com.example.user.sirent.User.AdapterKendaraan.OnCardClickListner;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_Kendaraan.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class Fragment_Kendaraan extends Fragment implements OnCardClickListner {

    private SwipeRefreshLayout swipeRefreshLayout;
    public static final int CONN_TIMEOUT = 10000;
    public static final int CONN_READOUT = 15000;
    private RecyclerView recyclerView;
    private AdapterKendaraan adapterKendaraan;
    List<DataKendaraan> data = new ArrayList<>();
    private static String url_kendaraan = "http://sirent.esy.es/selectall_kendaraan1.php";
    ConnectivityManager conMgr;

    private OnFragmentInteractionListener mListener;

    public Fragment_Kendaraan() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_kendaraan, container, false);
        final View[] v = {inflater.inflate(R.layout.fragment_kendaraan, container, false)};
        conMgr = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        swipeRefreshLayout = (SwipeRefreshLayout) v[0].findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                        || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {
                    Toast.makeText(getContext(), "Online", Toast.LENGTH_SHORT).show();
                    new viewKendaraan().execute();
                    swipeRefreshLayout.setRefreshing(false);
                    if (adapterKendaraan.equals(null)){
                        adapterKendaraan.addAll(data);
                    } else {
                        adapterKendaraan.clear();
                        adapterKendaraan.addAll(data);
                    }

                }
                else if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                        || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {
                    Toast.makeText(getContext(), "Offline hahaha", Toast.LENGTH_SHORT).show();
                    v[0] = inflater.inflate(R.layout.fragment_kendaraan, container, false);

                }

            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.sea_light, R.color.sea_dark,
        R.color.orange_light, R.color.blood_dark);
        return v[0];
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


    @Override
    public void onCardClicked(View view, int position) {
        Log.d("Onclick", "Card Posisi: " +position);

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

    public class viewKendaraan extends AsyncTask<String,String,String> {
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
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return e.toString();
            }
            try{
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(CONN_READOUT);
                conn.setConnectTimeout(CONN_TIMEOUT);
                conn.setRequestMethod("GET");
                conn.setDoOutput(true);
            } catch (IOException e1) {
                e1.printStackTrace();
                return e1.toString();
            }
            try{
                int response_code = conn.getResponseCode();
                if(response_code == HttpURLConnection.HTTP_OK){
                    InputStream inputStream = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine())!=null){
                        result.append(line);
                    }
                    return (result.toString());
                }else {
                    return ("tidak sukses");
                }

            } catch (IOException e2) {
                e2.printStackTrace();
                return e2.toString();
            }finally {
                conn.disconnect();
            }

        }

        @Override
        protected void onPostExecute(String result) {
            p.dismiss();

            //Toast.makeText(KendaraanActivity.this, result, Toast.LENGTH_SHORT).show();
            p.dismiss();
            try{
                JSONObject object = new JSONObject(result);
                JSONArray jsonArray = object.getJSONArray("kendaraan");
                for (int i=0; i < jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
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
                recyclerView = (RecyclerView)getActivity().findViewById(R.id.recyclerView1);
                adapterKendaraan = new AdapterKendaraan(getActivity(), data);
                recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
                recyclerView.setAdapter(adapterKendaraan);
                adapterKendaraan.setOnCardClickListner((OnCardClickListner) getActivity());
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            } catch (JSONException e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

}
