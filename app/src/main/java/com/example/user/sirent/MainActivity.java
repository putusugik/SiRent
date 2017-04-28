package com.example.user.sirent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.cengalabs.flatui.FlatUI;

public class MainActivity extends AppCompatActivity {

    private SharedPref sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sharedPref = new SharedPref(getApplicationContext());
    }

}
