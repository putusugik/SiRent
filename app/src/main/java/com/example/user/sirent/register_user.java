package com.example.user.sirent;

import android.app.AlertDialog;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
        namad = namadep.getText().toString();
        namab = namabel.getText().toString();
        mail = email.getText().toString();
        user = username.getText().toString();
        pass = password.getText().toString();
        repass = repassword.getText().toString();


    }

    public void onregister(View view) {
        if (pass != null || !pass.equals("") && repass !=  null || !repass.equals("")){

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Warning");
                alertDialog.setMessage("Password sama!");
                alertDialog.show();

        } else  {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Password harus diisi!");
            alertDialog.show();
        }
        }



        /*if (password.getText().toString().equals(null) && repassword.getText().toString().equals(null)) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Password harus diisi!");
            alertDialog.show();
        } else if (namadep.getText().toString() != null && namabel.getText().toString() != null && email.getText().toString() != null
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
        else if (namadep.getText().toString().equals(null)){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Nama Depan masih kosong");
            alertDialog.show();
        }
        else if (namabel.getText().toString().equals(null)){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Nama Belakang masih kosong");
            alertDialog.show();
        }
        else if (email.getText().toString().equals(null)){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Email masih kosong");
            alertDialog.show();
        }
        else if (username.getText().toString().equals(null)){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Username masih kosong");
            alertDialog.show();
        }
        else if (password.getText().toString().equals(null)){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Password masih kosong");
            alertDialog.show();
        }
        else if (repassword.getText().toString().equals(null)){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Ulangi Password masih kosong");
            alertDialog.show();
        }
        else if (namadep.getText().toString().equals(null) && namabel.getText().toString().equals(null)){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Nama Depan dan Nama Belakang masih kosong");
            alertDialog.show();
        }
        else if (namadep.getText().toString().equals(null) && email.getText().toString().equals(null)){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Nama Depan dan Email masih kosong");
            alertDialog.show();
        }
        else if (namadep.getText().toString().equals(null) && username.getText().toString().equals(null)){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Nama Depan dan Username masih kosong");
            alertDialog.show();
        }
        else if (namadep.getText().toString().equals(null) && password.getText().toString().equals(null)){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Nama Depan dan Password masih kosong");
            alertDialog.show();
        }
        else if (namadep.getText().toString().equals(null) && repassword.getText().toString().equals(null)){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Nama Depan dan Password masih kosong");
            alertDialog.show();
        }
        else if (namabel.getText().toString().equals(null) && email.getText().toString().equals(null)){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Nama Belakang dan Email masih kosong");
            alertDialog.show();
        }
        else if (namabel.getText().toString().equals(null) && username.getText().toString().equals(null)){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Nama Belakang dan Username masih kosong");
            alertDialog.show();
        }
        else if (namabel.getText().toString().equals(null) && password.getText().toString().equals(null)){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Nama Belakang dan Password masih kosong");
            alertDialog.show();
        }
        else if (namabel.getText().toString().equals(null) && repassword.getText().toString().equals(null)){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Nama Belakang dan Password masih kosong");
            alertDialog.show();
        }
        else if (email.getText().toString().equals(null) && username.getText().toString().equals(null)){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Email dan Username masih kosong");
            alertDialog.show();
        }
        else if (email.getText().toString().equals(null) && password.getText().toString().equals(null)){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Email dan Password masih kosong");
            alertDialog.show();
        }
        else if (email.getText().toString().equals(null) && repassword.getText().toString().equals(null)){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Email dan Password masih kosong");
            alertDialog.show();
        }
        else if (username.getText().toString().equals(null) && password.getText().toString().equals(null)){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Username dan Password masih kosong");
            alertDialog.show();
        }
        else if (username.getText().toString().equals(null) && repassword.getText().toString().equals(null)){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Username dan Password masih kosong");
            alertDialog.show();
        }
        else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Semua form wajib diisi!");
            alertDialog.show();
            namadep.requestFocus();
        }*/
    }

