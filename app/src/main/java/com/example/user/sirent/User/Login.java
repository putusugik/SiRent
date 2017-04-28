package com.example.user.sirent.User;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.cengalabs.flatui.FlatUI;
import com.example.user.sirent.Admin.Dashboard_Admin;
import com.example.user.sirent.R;
import com.example.user.sirent.SharedPref;
import com.example.user.sirent.Utility.Backtask;

/**
 * A login screen that offers login via email/password.
 */
public class Login extends AppCompatActivity {


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private SharedPref sharedPref;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        FlatUI.initDefaultValues(this);
        FlatUI.setDefaultTheme(FlatUI.GRASS);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mLoginFormView = findViewById(R.id.login_form);


        sharedPref = new SharedPref(getApplicationContext());

        if (sharedPref.isLoggedIn()) {
            Intent intent = new Intent(Login.this, Dashboard.class);
            startActivity(intent);
            finish();
        } else if (sharedPref.isLoggedInAdmin()){
            Intent i = new Intent(Login.this, Dashboard_Admin.class);
            startActivity(i);
            finish();
        }

    }

    public void  onlogin (View view){
        if ((mEmailView.getText().toString()).isEmpty() || (mPasswordView.getText().toString()).isEmpty()){
            Toast.makeText(this, "Kolom belum terisi dengan lengkap", Toast.LENGTH_SHORT).show();
        } else {
            String user = mEmailView.getText().toString();
            String pass = mPasswordView.getText().toString();
            String type = "login";
            Backtask backtask = new Backtask(this);
            backtask.execute(type, user, pass);
            finish();

        }


    }

    public void register (View view){
        Intent intent = new Intent(this, register_user.class);
        startActivity(intent);
        finish();
    }


}

