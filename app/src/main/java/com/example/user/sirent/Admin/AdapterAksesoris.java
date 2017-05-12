package com.example.user.sirent.Admin;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.sirent.R;

import java.util.ArrayList;
import java.util.HashMap;



/**
 * Created by User on 5/9/2017.
 */

public class AdapterAksesoris extends BaseAdapter implements Filterable {
    private Activity activity;
    private ArrayList<HashMap<String,String>> data;
    private ArrayList<HashMap<String,String>> dataBackup;
    private static LayoutInflater inflater = null;
    HashMap<String, String> aks = new HashMap<String, String>();

    public AdapterAksesoris (Activity a, ArrayList<HashMap<String, String>> d){
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
        if (convertView==null)
            v = inflater.inflate(R.layout.aksesoris_adapter, null);
            final CheckBox ckAks = (CheckBox)v.findViewById(R.id.chk_aksesori);
            final TextView tAkss = (TextView)v.findViewById(R.id.tv_aksesoris);
            final TextView idAks = (TextView)v.findViewById(R.id.id_aks);

            tAkss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ckAks.isChecked()){
                        ckAks.setChecked(false);
                    }else {
                        ckAks.setChecked(true);
                        Toast.makeText(v.getContext(), tAkss.getText(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            aks = data.get(position);

            idAks.setText(aks.get(Admin_InsertKend.TAG_IDAKS));
            tAkss.setText(aks.get(Admin_InsertKend.TAG_AKSESORIS));


            return v;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                charSequence = charSequence.toString().toLowerCase();
                if (charSequence==null || charSequence.length()>0){
                    results.values= dataBackup;
                    results.count = dataBackup.size();
                }
                if (charSequence!=null && charSequence.toString().length()==0){
                    ArrayList<HashMap<String, String>> x = new ArrayList<HashMap<String, String>>();
                    for (int i = 0; i<data.size();i++){
                        if (dataBackup.get(i).get("nama_aksesoris").toLowerCase().contains(charSequence)){
                            x.add(dataBackup.get(i));
                        }
                    }
                    results.values = x;
                    results.count = x.size();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                data = (ArrayList<HashMap<String, String >>)results.values;
                notifyDataSetChanged();
            }
        };
    }
}
