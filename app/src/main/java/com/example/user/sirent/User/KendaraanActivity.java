package com.example.user.sirent.User;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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

public class KendaraanActivity extends AppCompatActivity implements OnCardClickListner {

    public static final int CONN_TIMEOUT = 10000;
    public static final int CONN_READOUT = 15000;
    private RecyclerView recyclerView;
    private AdapterKendaraan adapterKendaraan;
    private static String url_kendaraan = "http://sirent.esy.es/selectall_kendaraan1.php";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kendaraan);
        new viewKendaraan().execute();

    }

    @Override
    public void onCardClicked(View view, int position) {
        Log.d("OnClick", "Card Position" + position);
    }

    public class viewKendaraan extends AsyncTask<String,String,String> {
        ProgressDialog p = new ProgressDialog(KendaraanActivity.this);
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
            List<DataKendaraan> data = new ArrayList<>();
            //Toast.makeText(KendaraanActivity.this, result, Toast.LENGTH_SHORT).show();
            p.dismiss();
            try{
                JSONObject object = new JSONObject(result);
                JSONArray jsonArray = object.getJSONArray("kendaraan");
                Log.d("Kend: ", result.toString());
                for (int i=0; i < jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    DataKendaraan dataKendaraan = new DataKendaraan();
                    dataKendaraan.ID_kendaraan = jsonObject.getString("ID_kendaraan");
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
                recyclerView = (RecyclerView)findViewById(R.id.recyclerView1);
                adapterKendaraan = new AdapterKendaraan(KendaraanActivity.this, data);
                recyclerView.addItemDecoration(new DividerItemDecoration(KendaraanActivity.this, LinearLayoutManager.VERTICAL));
                recyclerView.setAdapter(adapterKendaraan);
                adapterKendaraan.setOnCardClickListner((OnCardClickListner) KendaraanActivity.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(KendaraanActivity.this));

            } catch (JSONException e) {
                Toast.makeText(KendaraanActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
