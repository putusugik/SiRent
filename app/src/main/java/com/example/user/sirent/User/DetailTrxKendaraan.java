package com.example.user.sirent.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.user.sirent.JSON.JSONParser;
import com.example.user.sirent.R;
import com.example.user.sirent.SharedPref;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class DetailTrxKendaraan extends Activity {

    final Context context = this;
    private ProgressDialog aDialog;
    EditText t_sewa, t_kem, t_lokasi, t_detlokasi, tidtoko;
    Button bookbt;
    private static String url_book = "http://sirent.esy.es/insert_booking.php";
    public static final String TAG_SUCCESS = "sukses";
    int id, idtoko;
    String tgl_sewa, tgl_kem, idkend;
    public String idToko;
    JSONParser jsonParser = new JSONParser();
    RequestParams params = new RequestParams();
    SharedPref sharedPref;
    TimePickerDialog timePickerDialog;
    static Calendar calendar = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_trx_kendaraan);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        tidtoko = (EditText)findViewById(R.id.e_IDtoko);
        t_sewa = (EditText)findViewById(R.id.trx_sewa);
        t_kem = (EditText)findViewById(R.id.trx_kem);
        t_lokasi = (EditText)findViewById(R.id.trx_lokasi);
        t_detlokasi = (EditText)findViewById(R.id.tx_detlokasi);
        bookbt = (Button)findViewById(R.id.but_confirm);

        Intent it = getIntent();
        idkend = it.getStringExtra("ID_Kendaraan");
        idToko = it.getStringExtra("ID_toko");
        Toast.makeText(context, "IDTOKO: "+idToko, Toast.LENGTH_SHORT).show();
        tidtoko.setText(idToko);

        t_detlokasi.clearFocus();
        sharedPref = new SharedPref(getApplicationContext());
        id = sharedPref.getUserID();

        t_sewa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener dateSet = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String format = "yyyy-MM-dd";
                        final SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
                        int hs = calendar.get(Calendar.HOUR_OF_DAY);
                        int ms = calendar.get(Calendar.MINUTE);
                        timePickerDialog = new TimePickerDialog(DetailTrxKendaraan.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                t_sewa.setText(sdf.format(calendar.getTime()) + " " +hourOfDay +":" + minute);
                            }
                        }, hs, ms, true);
                        t_sewa.setText(sdf.format(calendar.getTime()));
                        tgl_sewa = t_sewa.getText().toString();
                        timePickerDialog.show();
                    }
                };

                new DatePickerDialog(DetailTrxKendaraan.this, dateSet, calendar.get(Calendar.YEAR),  calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        t_kem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener dateSet = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String format = "yyyy-MM-dd";
                        final SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
                        int hk = calendar.get(Calendar.HOUR_OF_DAY);
                        int mk = calendar.get(Calendar.MINUTE);
                        timePickerDialog = new TimePickerDialog(DetailTrxKendaraan.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                t_kem.setText(sdf.format(calendar.getTime()) + " " +hourOfDay +":"+minute);
                            }
                        }, hk, mk, true);
                        t_kem.setText(sdf.format(calendar.getTime()));
                        tgl_kem = t_kem.getText().toString();
                        timePickerDialog.show();
                    }
                };
                new DatePickerDialog(DetailTrxKendaraan.this, dateSet, calendar.get(Calendar.YEAR),  calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        t_detlokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        bookbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Apakah anda ingin memesan kendaraan ini ?");
                alert.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new bookKend().execute();
                    }
                });
                alert.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertD = alert.create();
                alertD.show();
            }
        });

    }

    private class bookKend extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            aDialog = new ProgressDialog(context);
            aDialog.setMessage("Memproses");
            aDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            String idU = String.valueOf(id);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("ID_toko", idToko));
            params.add(new BasicNameValuePair("ID_user", idU));
            params.add(new BasicNameValuePair("tgl_sewa", tgl_sewa));
            params.add(new BasicNameValuePair("tgl_kembali", tgl_kem));
            params.add(new BasicNameValuePair("ID_kendaraan", idkend));

            JSONObject obj = jsonParser.makeHttpRequest(url_book, "POST", params);
            Log.d("Res: ", obj.toString());

            try{
                int sukses = obj.getInt(TAG_SUCCESS);
                if (sukses==1){
                    Intent i = new Intent(DetailTrxKendaraan.this, Dashboard.class);
                    startActivity(i);
                }else {
                    Toast.makeText(context, "Pesanan Gagal", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            aDialog.dismiss();
            Toast.makeText(context, "Pesanan selesai", Toast.LENGTH_SHORT).show();
        }
    }
}
