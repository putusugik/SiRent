package com.example.user.sirent.Admin;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.sirent.R;
import com.example.user.sirent.Utility.DataKendaraan;

import java.util.Collections;
import java.util.List;

/**
 * Created by Sugik on 12/27/2016.
 */

public class AdapterKendaraanAdmin extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    List<DataKendaraan> data = Collections.emptyList();
    DataKendaraan current;
    int currentPos = 0;

    public AdapterKendaraanAdmin(Context context, List<DataKendaraan> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data=data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.container_kendaraan_admin, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        DataKendaraan current = data.get(position);
        myHolder.tx_namakend.setText(current.nama_kendaraan);
        myHolder.tx_noplat.setText(current.no_plat);
        myHolder.tx_tipekend.setText(current.tipe_kendaraan);
        myHolder.tx_merkkend.setText(current.merk_kendaraan);
        myHolder.tx_namatoko.setText(current.nama_toko);
        myHolder.tx_hargasewa.setText(current.harga_sewa);
        myHolder.tx_jumlah.setText(current.jumlah);
        myHolder.tx_desk.setText(current.deskripsi);
        myHolder.tx_gambar.setText(current.gambar);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private class MyHolder extends RecyclerView.ViewHolder {

        TextView tx_namakend, tx_noplat,tx_tipekend, tx_merkkend, tx_namatoko,
                tx_hargasewa, tx_jumlah, tx_desk, tx_gambar;

        public MyHolder(View view) {
            super(view);
            tx_namakend=(TextView)itemView.findViewById(R.id.text_kendaraan);
            tx_noplat=(TextView)itemView.findViewById(R.id.text_noplat);
            tx_tipekend=(TextView)itemView.findViewById(R.id.text_tipekend);
            tx_merkkend=(TextView)itemView.findViewById(R.id.text_merkkend);
            tx_namatoko=(TextView)itemView.findViewById(R.id.text_namatoko);
            tx_hargasewa=(TextView)itemView.findViewById(R.id.text_hargasewa);
            tx_jumlah=(TextView)itemView.findViewById(R.id.text_jumlah);
            tx_desk=(TextView)itemView.findViewById(R.id.text_desk);
            tx_gambar=(TextView)itemView.findViewById(R.id.text_gambar);

        }
    }
}
