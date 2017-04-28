package com.example.user.sirent.Admin;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.user.sirent.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User on 1/17/2017.
 */
public class KelolaKend_Adapter extends BaseAdapter implements Filterable{

    private Activity activity;
    private ArrayList<HashMap<String, String >> data;
    private ArrayList<HashMap<String, String >> dataBackup;
    private static LayoutInflater inflater = null;
    HashMap<String, String>kendaraan = new HashMap<String, String>();
    public KelolaKend_Adapter(Activity a, ArrayList<HashMap<String, String >> d){
        activity = a;
        data = d;
        dataBackup = (ArrayList<HashMap<String, String>>)data.clone();
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

            TextView id = (TextView)v.findViewById(R.id.ID);
            TextView nKend = (TextView)v.findViewById(R.id.namaken);
            TextView noMes = (TextView)v.findViewById(R.id.no_mesin);
            TextView noPlat = (TextView)v.findViewById(R.id.no_plat);

            kendaraan = data.get(position);

            id.setText(kendaraan.get(Kelola_KendaraanAdmin.TAG_ID));
            nKend.setText(kendaraan.get(Kelola_KendaraanAdmin.TAG_KENDARAAN));
            noMes.setText(kendaraan.get(Kelola_KendaraanAdmin.TAG_MESIN));
            noPlat.setText(kendaraan.get(Kelola_KendaraanAdmin.TAG_PLAT));

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

}
