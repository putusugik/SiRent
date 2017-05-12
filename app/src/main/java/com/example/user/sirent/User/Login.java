package com.example.user.sirent.User;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
//import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cengalabs.flatui.FlatUI;
import com.example.user.sirent.Admin.Dashboard_Admin;
import com.example.user.sirent.JSON.JSONParser;
import com.example.user.sirent.R;
import com.example.user.sirent.SharedPref;
import com.example.user.sirent.Utility.Backtask;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * A login screen that offers login via email/password.
 */
public class Login extends AppCompatActivity {


    private static String url_lupapass = "http://sirent.esy.es/lupapass.php";
    private static final String TAG_SUCCESS = "sukses";
    JSONParser jsonParser = new JSONParser();
    RequestParams params = new RequestParams();
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private SharedPref sharedPref;
    EditText eLupa;
    Button login;
    String em, book;
    SharedPreferences preferences;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //MultiDex.install(this);
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
        login = (Button)findViewById(R.id.email_sign_in_button);
        login.setEnabled(false);



        sharedPref = new SharedPref(getApplicationContext());
        preferences = PreferenceManager.getDefaultSharedPreferences(this);




        /*String iddd = in.getStringExtra("FragmentBooking");
        Toast.makeText(this, "Hello Fragment "+iddd, Toast.LENGTH_SHORT).show();*/

        if (sharedPref.isLoggedIn()) {
            Intent intent = new Intent(Login.this, Dashboard.class);
            startActivity(intent);
            finish();
        } else if (sharedPref.isLoggedInAdmin()){
            if (getIntent().getStringExtra("FragmentBooking")!= null){
                if (getIntent().getStringExtra("FragmentBooking").equals("book")){
                    book = getIntent().getStringExtra("FragmentBooking");
                    Toast.makeText(this, book, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Login.this, Dashboard_Admin.class);
                    i.putExtra("Booking", book);
                    startActivity(i);
                    finish();

                }
            } else {
                Intent i = new Intent(Login.this, Dashboard_Admin.class);
                startActivity(i);
                finish();
            }

        }

        mPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                login.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>6){
                    login.setEnabled(true);
                }else {
                    login.setEnabled(false);
                }
            }
        });

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

    public void lupapass (View view){
        AlertDialog.Builder alert = new AlertDialog.Builder(Login.this);
        LayoutInflater infalter = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = infalter.inflate(R.layout.lupa_pass, null);
        eLupa = (EditText)v.findViewById(R.id.edit_lupa);
        alert.setView(v);
        alert.setTitle("Email lupa password");
        alert.setPositiveButton("Kirim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               em  = eLupa.getText().toString();


                if (em.isEmpty()==true){
                    eLupa.requestFocus();
                    Toast.makeText(Login.this, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    new lupaPass().execute();
                    Toast.makeText(Login.this, em, Toast.LENGTH_SHORT).show();
                }
            }
        });
        alert.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog builder = alert.create();
        builder.show();
    }


    private class lupaPass extends AsyncTask<String,String,String >{
        ProgressDialog pd = new ProgressDialog(Login.this);
        AlertDialog.Builder alrt = new AlertDialog.Builder(Login.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Memproses");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email",""+em));
            JSONObject jobj = jsonParser.makeHttpRequest(url_lupapass, "POST", params);
            Log.d("Resp: ", jobj.toString());

            try {
                int sukses = jobj.getInt(TAG_SUCCESS);
                alrt.setTitle("Lupa password");
                if (sukses == 1) {
                    pd.dismiss();
                    alrt.setMessage("Password baru sudah dikirim ke email " + em + ".");
                    alrt.setNeutralButton("Selesai", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                }
                if (sukses == 0) {
                    pd.dismiss();
                    alrt.setMessage("Email tidak terdaftar");
                    alrt.setNeutralButton("Selesai", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            AlertDialog bld = alrt.create();
            bld.show();
            pd.dismiss();
        }
    }
}

