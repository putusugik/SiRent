package com.example.user.sirent.User;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.sirent.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User on 4/13/2017.
 */

public class Histori_UserAdapter extends BaseAdapter implements Filterable {
    private Activity activity;
    private ArrayList<HashMap<String,String>> data;
    private ArrayList<HashMap<String,String>> dataBackup;
    private static LayoutInflater inflater = null;
    HashMap<String,String> histori = new HashMap<String, String>();

    public Histori_UserAdapter (Activity a, ArrayList<HashMap<String, String>> d){
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
        if (convertView == null)
            v = inflater.inflate(R.layout.booking_useradapter, null);
            TextView t_IDkend = (TextView)v.findViewById(R.id.id_kend);
            TextView t_kendu = (TextView)v.findViewById(R.id.t_namakend);
            TextView t_nplatu = (TextView)v.findViewById(R.id.t_plat);
            TextView t_sewau = (TextView)v.findViewById(R.id.t_tglsewa);
            TextView t_kemu = (TextView)v.findViewById(R.id.t_tglkembali);
            TextView t_stsu = (TextView)v.findViewById(R.id.t_status);
            ImageView imgUser = (ImageView) v.findViewById(R.id.img_kend);

            histori = data.get(position);

            t_IDkend.setText(histori.get(Fragment_StatusBookingUser.TAG_ID));
            t_kendu.setText(histori.get(Fragment_StatusBookingUser.TAG_KENDARAAN));
            t_nplatu.setText(histori.get(Fragment_StatusBookingUser.TAG_PLAT));
            t_sewau.setText(histori.get(Fragment_StatusBookingUser.TGL_SEWA));
            t_kemu.setText(histori.get(Fragment_StatusBookingUser.TAG_KEMBALI));
            t_stsu.setText(histori.get(Fragment_StatusBookingUser.TAG_STATUSBOOK));

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
                if (charSequence != null && charSequence.toString().length()>0){
                    ArrayList<HashMap<String, String>> x = new ArrayList<HashMap<String, String>>();
                    for (int i = 0; i <data.size();i++){
                        if (dataBackup.get(i).get("nama_kendaraan").toLowerCase().contains(charSequence));
                        x.add(dataBackup.get(i));
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                data = (ArrayList<HashMap<String, String>>)results.values;
                notifyDataSetChanged();
            }
        };
    }
}
