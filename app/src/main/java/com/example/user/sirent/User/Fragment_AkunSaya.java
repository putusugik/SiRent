package com.example.user.sirent.User;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.user.sirent.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_AkunSaya extends Fragment {

    EditText tjudul;

    public Fragment_AkunSaya() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_akun_saya, container, false);
        tjudul = (EditText)v.findViewById(R.id.editText);
        

        return v;
    }

}
