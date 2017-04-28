package com.example.user.sirent.User;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.sirent.R;
import com.example.user.sirent.Utility.Backtask;

public class register_user extends AppCompatActivity {
    EditText namadep, namabel, email, username, password, repassword;
    String namad, namab, mail, user, pass, repass;
    private static final String TAG = "your activity name";
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
        namad = namadep.getText().toString();
        namab = namabel.getText().toString();
        mail = email.getText().toString();
        user = username.getText().toString();
        pass = password.getText().toString();
        repass = repassword.getText().toString();
        if (!namad.equals("") && !namab.equals("") && !mail.equals("") && !email.equals("") && !user.equals("")
                && !pass.equals("")) {
            if (pass.equals(repass)) {
                String type = "register";
                Backtask backtask = new Backtask(this);
                backtask.execute(type, namad, namab, mail, user, pass);
                Toast.makeText(this, "Register Berhasil", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
            }
            else {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Warning");
                alertDialog.setMessage("Password tidak sama!");
                alertDialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        password.requestFocus();
                    }
                });
                alertDialog.show();
            }
        }

        if (namad.isEmpty() || namab.isEmpty() || mail.isEmpty() || user.isEmpty() || pass.isEmpty() || repass.isEmpty()){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Masih ada data yang belum di isi!");
            alertDialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.show();
        }
    }
}

