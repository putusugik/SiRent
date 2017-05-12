package com.example.user.sirent.Admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.sirent.JSON.JSONParser;
import com.example.user.sirent.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by User on 1/17/2017.
 */
public class KelolaKend_Adapter extends BaseAdapter implements Filterable{

    private static String url_updateKend = "http://sirent.esy.es/update_stsdtkendaraan.php";
    JSONParser jsonParser = new JSONParser();
    public static final String TAG_SUCCESS = "sukses";
    private ProgressDialog pDialog ;
    String ID_kend, kend;
    List<NameValuePair>params;

    private Activity activity;
    private Context mcontext = null;
    private ArrayList<HashMap<String, String >> data;
    private ArrayList<HashMap<String, String >> dataBackup;
    private static LayoutInflater inflater = null;
    HashMap<String, String>kendaraan = new HashMap<String, String>();

    public KelolaKend_Adapter(Activity a, ArrayList<HashMap<String, String >> d, Context ctx){
        this.mcontext = ctx;
        activity = a;
        data = d;
        dataBackup = (ArrayList<HashMap<String, String>>)data.clone();
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public KelolaKend_Adapter(Context context, ArrayList<HashMap<String, String>> kendList) {
        this.mcontext = context;
        data = kendList;
        dataBackup = (ArrayList<HashMap<String, String>>)data.clone();
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(convertView == null)
            v = inflater.inflate(R.layout.list_kendaraan, null);

            final TextView id = (TextView)v.findViewById(R.id.ID);
            final TextView nKend = (TextView)v.findViewById(R.id.namaken);
            TextView noMes = (TextView)v.findViewById(R.id.no_mesin);
            TextView noPlat = (TextView)v.findViewById(R.id.no_plat);
            final CheckBox cbStatus = (CheckBox)v.findViewById(R.id.cb_status);

            kendaraan = data.get(position);

        final String[] status = {kendaraan.get(FragmentKelolaKendaraan.TAG_STATUS)};
            id.setText(kendaraan.get(FragmentKelolaKendaraan.TAG_ID));
            nKend.setText(kendaraan.get(FragmentKelolaKendaraan.TAG_KENDARAAN));
            noMes.setText(kendaraan.get(FragmentKelolaKendaraan.TAG_MESIN));
            noPlat.setText(kendaraan.get(FragmentKelolaKendaraan.TAG_PLAT));

            if (!status[0].equals("0")){
                cbStatus.setChecked(false);
            } else {
                cbStatus.setChecked(true);
            }

            final AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());

        final View finalV = v;
        cbStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbStatus.isChecked()==false){
                    new AlertDialog.Builder(finalV.getContext())
                            .setMessage("Anda yakin tidak menampilkan kendaraan ini pada aplikasi ?")
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cbStatus.setChecked(false);
                                    ID_kend = id.getText().toString();
                                    kend = nKend.getText().toString();
                                    status[0] = "Tidak siap";
                                    new updateKend().execute("Tidak siap");
                                    //new Kelola_KendaraanAdmin.updatedtKend().execute("IDkend", id.getText().toString());
                                }
                            })
                            .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cbStatus.setChecked(true);
                                }
                            })
                            .show();
                } else if (cbStatus.isChecked()==true){
                    new AlertDialog.Builder(finalV.getContext())
                            .setMessage("Anda akan menampilkan kendaraan ini pada aplikasi ?")
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cbStatus.setChecked(true);
                                    ID_kend = id.getText().toString();
                                    kend = nKend.getText().toString();
                                    status[0] = "Siap";
                                    new updateKend().execute("Siap");
                                    //new updateKendaraan().execute();

                                }
                            })
                            .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cbStatus.setChecked(false);
                                }
                            })
                            .show();
                }
            }
        });

            /*else {
                alert.setMessage("Kendaraan tersedia dan akan ditampilkan pada aplikasi ?");
                alert.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(activity, "Checked Bro", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(activity, "Still Uncheckedd!!!!", Toast.LENGTH_SHORT).show();
                    }
                });
            }*/

            return v;

    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                charSequence = charSequence.toString().toLowerCase();
                if (charSequence == null || charSequence.length()==0){
                    results.values = dataBackup;
                    results.count = dataBackup.size();
                }
                if (charSequence!=null && charSequence.toString().length()>0){
                    ArrayList<HashMap<String,String>> x = new ArrayList<HashMap<String, String>>();
                    for(int i = 0; i < data.size();i++){
                        if (dataBackup.get(i).get("nama_kendaraan").toLowerCase().contains(charSequence)){
                            x.add(dataBackup.get(i));
                        }
                    }
                    results.values = x;
                    results.count = x.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults filterResults) {
                data =  (ArrayList<HashMap<String,String>>)filterResults.values;
                notifyDataSetChanged();

            }
        };
    }

    private class updateKend extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(mcontext);
            pDialog.setMessage("Memproses");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            if (args[0].equals("Tidak siap")) {
                params.add(new BasicNameValuePair("ID_kendaraan", ""+ID_kend));
                params.add(new BasicNameValuePair("status", args[0]));
            }else if (args[0].equals("Siap")){
                params.add(new BasicNameValuePair("ID_kendaraan", ""+ID_kend));
                params.add(new BasicNameValuePair("status", args[0]));
            }



            JSONObject obj = jsonParser.makeHttpRequest(url_updateKend, "POST", params);
            Log.d("Res: ", obj.toString());

            try {
                int sukses = obj.getInt(TAG_SUCCESS);
                if (sukses==1){
                    Log.d("Sukses= ", String.valueOf(sukses));
                    return args[0];
                } else if (sukses==0){
                    Log.d("Sukses= ", String.valueOf(sukses));
                    return args[0];
                }

            } catch (JSONException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equals("Tidak siap")){
                Toast.makeText(mcontext, "Kendaraan " +kend +" tidak ditampilkan ", Toast.LENGTH_SHORT).show();
            } else if (s.equals("Siap")){
                Toast.makeText(mcontext, "Kendaraan "+ kend +" ditampilkan", Toast.LENGTH_SHORT).show();
            }

            pDialog.dismiss();
        }
    }

    /*private class updateKendaraan extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... params) {
            return null;
        }
    }*/
}
