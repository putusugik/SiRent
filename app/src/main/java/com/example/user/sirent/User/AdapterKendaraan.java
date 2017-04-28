package com.example.user.sirent.User;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
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

public class AdapterKendaraan extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private LayoutInflater inflater;
    public RecyclerView recyclerView;
    OnCardClickListner onCardClickListner;



    List<DataKendaraan> data = Collections.emptyList();
    DataKendaraan current;
    int currentPos = 0;


    public AdapterKendaraan(Context context, List<DataKendaraan> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data=data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(parent.getContext()).inflate(R.layout.container_kendaraan, parent, false);
        MyHolder holder = new MyHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyHolder myHolder = (MyHolder) holder;
        final DataKendaraan current = data.get(position);
        myHolder.tx_IDkend.setText(current.ID_kendaraan);
        myHolder.tx_namakend.setText(current.nama_kendaraan);
        myHolder.tx_noplat.setText(current.no_plat);
        myHolder.tx_tipekend.setText(current.tipe_kendaraan);
        myHolder.tx_merkkend.setText(current.merk_kendaraan);
        myHolder.tx_namatoko.setText(current.nama_toko);
        myHolder.tx_hargasewa.setText(current.harga_sewa);
        myHolder.tx_jumlah.setText(current.jumlah);
        myHolder.tx_desk.setText(current.deskripsi);
        myHolder.tx_gambar.setText(current.gambar);
        myHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCardClickListner.onCardClicked(v, position);
                Intent i = new Intent(v.getContext(), TrxKendaraan.class);
                i.putExtra("ID_Kendaraan", current.ID_kendaraan);
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public DataKendaraan getCurrent (int currentPos){
        return data.get(currentPos);
    }

    public void clear(){
        data.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<DataKendaraan> list){
        data.addAll(list);
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    private class MyHolder extends RecyclerView.ViewHolder {
        CardView cardView;

        TextView tx_IDkend, tx_namakend, tx_noplat,tx_tipekend, tx_merkkend, tx_namatoko,
                tx_hargasewa, tx_jumlah, tx_desk, tx_gambar;

        public MyHolder(View view) {
            super(view);
            cardView = (CardView)view.findViewById(R.id.cardID);
            tx_IDkend = (TextView)view.findViewById(R.id.text_IDkendaraan);
            tx_namakend=(TextView)view.findViewById(R.id.text_kendaraan);
            tx_noplat=(TextView)view.findViewById(R.id.text_noplat);
            tx_tipekend=(TextView)view.findViewById(R.id.text_tipekend);
            tx_merkkend=(TextView)view.findViewById(R.id.text_merkkend);
            tx_namatoko=(TextView)view.findViewById(R.id.text_namatoko);
            tx_hargasewa=(TextView)view.findViewById(R.id.text_hargasewa);
            tx_jumlah=(TextView)view.findViewById(R.id.text_jumlah);
            tx_desk=(TextView)view.findViewById(R.id.text_desk);
            tx_gambar=(TextView)view.findViewById(R.id.text_gambar);
        }
    }

    public interface OnCardClickListner {
        void onCardClicked(View view, int position);
    }

    public void setOnCardClickListner(OnCardClickListner onCardClickList) {
        this.onCardClickListner = onCardClickList;
    }
}
