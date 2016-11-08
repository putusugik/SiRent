package com.example.user.sirent;

import android.app.AlertDialog;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class register_user extends AppCompatActivity {
    EditText namadep, namabel, email, username, password, repassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        namadep = (EditText) findViewById(R.id.nama_depan);
        namabel = (EditText) findViewById(R.id.nama_blkg);
        email = (EditText) findViewById(R.id.email);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.repassword);

    }

    public void onregister(View view) {
        if (namadep.getText().toString() != null && namabel.getText().toString() != null && email.getText().toString() != null
                && username.getText().toString() != null && password.getText().toString() != null && repassword.getText().toString() != null) {
            if (password.getText().toString().equals(repassword.getText().toString())) {
                String nama_dep = namadep.getText().toString();
                String nama_bel = namabel.getText().toString();
                String e_mail = email.getText().toString();
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String type = "register";
                Backtask backtask = new Backtask(this);
                backtask.execute(type, nama_dep, nama_bel, e_mail, user, pass);


            } else {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Warning");
                alertDialog.setMessage("Password tidak sama!");
                alertDialog.show();
            }
        }
    }
}
