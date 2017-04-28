package com.example.user.sirent.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.user.sirent.Image.ImageLoader;
import com.example.user.sirent.JSON.JSONParser;
import com.example.user.sirent.R;
import com.example.user.sirent.SharedPref;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.logging.Handler;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class TrxKendaraan extends Activity {

    final Context context = this;
    private ProgressDialog aDialog;
    private static String url_detkendaraan = "http://sirent.esy.es/select_detailkendaraan.php";
    private static String url_book = "http://sirent.esy.es/insert_booking.php";
    public static final String TAG_SUCCESS = "sukses";
    int id, idtoko;
    ImageView image;
    public ImageLoader imageLoader;
    String imgPath, fileName, encodedString, idkend;
    String tgl_sewa, tgl_kembali, lokasi;
    Bitmap bitmap;
    String idToko;

    private static int RESULT_LOAD_IMG = 1;
    JSONParser jsonParser = new JSONParser();
    RequestParams params = new RequestParams();
    SharedPref sharedPref;
    private ProgressDialog pDialog;
    TimePickerDialog timePickerDialog;

    static Calendar calendar = Calendar.getInstance();

    Handler mHandler;
    public void updateLabel() {

    }

    EditText tx_nKen, tx_Merk, tx_nTok, tx_hrg, tx_desk, tx_idToko, tx_nKen1, tx_Merk1,
            tx_nTok1, tx_hrg1, tx_desk1, tx_Sewa, tx_Kembali;
    Button bt_book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trx_kendaraan);

        tx_idToko = (EditText)findViewById(R.id.e_idToko);
        tx_nKen = (EditText)findViewById(R.id.tx_nKend);
        tx_Merk = (EditText)findViewById(R.id.tx_merk);
        tx_nTok = (EditText)findViewById(R.id.tx_toko);
        tx_hrg = (EditText)findViewById(R.id.tx_harga);
        tx_desk = (EditText)findViewById(R.id.tx_deskripsi);
        bt_book = (Button)findViewById(R.id.but_book);

        new getKendDetail().execute();
        sharedPref = new SharedPref(getApplicationContext());
        id= sharedPref.getUserID();

        Intent i = getIntent();
        idkend = i.getStringExtra("ID_Kendaraan");



        bt_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Apakah anda ingin memesan kendaraan ini ?");
                alert.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    /*@Override
                    public void onClick(DialogInterface dialog, int which) {
                        final Dialog dialog1 = new Dialog(TrxKendaraan.this);
                        dialog1.setContentView(R.layout.trx_booking);
                        EditText tsewa = (EditText)dialog1.findViewById(R.id.tx_sewa);
                        EditText tkem = (EditText)dialog1.findViewById(R.id.tx_kembali);
                        EditText tlokasi = (EditText)dialog1.findViewById(R.id.tx_lokasi);
                        Button book = (Button)dialog1.findViewById(R.id.but_book);

                        Intent i = new Intent(TrxKendaraan.this, DetailTrxKendaraan.class);
                        i.putExtra("ID_Kendaraan", idkend);
                        i.putExtra("ID_toko", idToko);
                        //startActivityForResult(i, 100);
                        dialog1.show();
                    }*/
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final Dialog dialog1 = new Dialog(context);
                        dialog1.setContentView(R.layout.trx_booking);
                        dialog1.setTitle("Booking Kendaraan");
                        final EditText t_sewa = (EditText)dialog1.findViewById(R.id.tx_sewa);
                        final EditText t_kembali = (EditText)dialog1.findViewById(R.id.tx_kembali);
                        final EditText t_lokasi = (EditText)dialog1.findViewById(R.id.tx_lokasi);
                        t_lokasi.setFocusable(false);
                        t_lokasi.setFocusableInTouchMode(false);


                        Button bok = (Button)dialog1.findViewById(R.id.but_book);

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
                                        timePickerDialog = new TimePickerDialog(TrxKendaraan.this, new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                t_sewa.setText(sdf.format(calendar.getTime()) + " " +hourOfDay +":" + minute);
                                                tgl_sewa = t_sewa.getText().toString();

                                            }
                                        }, hs, ms, true);
                                        //t_sewa.setText(sdf.format(calendar.getTime()));


                                        timePickerDialog.show();
                                    }
                                };

                                new DatePickerDialog(TrxKendaraan.this, dateSet, calendar.get(Calendar.YEAR),  calendar.get(Calendar.MONTH),
                                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                            }
                        });
                        t_kembali.setOnClickListener(new View.OnClickListener() {
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
                                        timePickerDialog = new TimePickerDialog(TrxKendaraan.this, new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                t_kembali.setText(sdf.format(calendar.getTime()) + " " +hourOfDay +":" + minute);
                                                tgl_kembali = t_kembali.getText().toString();

                                            }
                                        }, hk, mk, true);
                                        //t_kembali.setText(sdf.format(calendar.getTime()));

                                        timePickerDialog.show();
                                    }
                                };
                                new DatePickerDialog(TrxKendaraan.this, dateSet, calendar.get(Calendar.YEAR),  calendar.get(Calendar.MONTH),
                                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                            }
                        });

                        t_lokasi.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                t_lokasi.setFocusable(true);
                                t_lokasi.setFocusableInTouchMode(true);
                                return false;
                            }
                        });


                        bok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String tsewa = t_sewa.getText().toString();
                                String tkem = t_kembali.getText().toString();
                                String tlok = t_lokasi.getText().toString();
                                if (tsewa.isEmpty() || tkem.isEmpty() || tlok.isEmpty()){
                                    Toast.makeText(TrxKendaraan.this, "Isian belum lengkap", Toast.LENGTH_SHORT).show();
                                }else {
                                    lokasi = t_lokasi.getText().toString();
                                    new bookKend().execute();
                                    dialog1.dismiss();
                                }

                            }
                        });
                        dialog1.show();
                    }
                });
                alert.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();

                //new bookKend().execute();
            }
        });
    }

    private class getKendDetail extends AsyncTask<String, String, List<String>> {
        @Override
        protected List<String> doInBackground(String... arg0) {
            int sukses;
            List<String>list = new ArrayList<String>();
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("id",""+idkend));
                JSONObject object = jsonParser.makeHttpRequest(url_detkendaraan, "GET", params);

                Log.d("Detail: ",object.toString());
                sukses = object.getInt(TAG_SUCCESS);
                if (sukses == 1){
                    JSONArray objArray = object.getJSONArray("kendaraan");
                    JSONObject object1 = objArray.getJSONObject(0);
                    list.add(object1.getString("ID_toko"));
                    list.add(object1.getString("id_pemilik"));
                    list.add(object1.getString("id_kendaraan"));
                    list.add(object1.getString("nama_kendaraan"));
                    list.add(object1.getString("merk_kendaraan"));
                    list.add(object1.getString("nama_toko"));
                    list.add(object1.getString("harga_sewa"));
                    list.add(object1.getString("deskripsi"));
                    //list.add(object1.getString("gambar").replace("\\", ""));

                }else {

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            tx_idToko.setText(result.get(0));
            tx_nKen.setText(result.get(3));
            tx_Merk.setText(result.get(4));
            tx_nTok.setText(result.get(5));
            tx_hrg.setText(result.get(6));
            tx_desk.setText(result.get(7));
            idToko = result.get(0);
            //imageLoader.DisplayImage(result.get(9), DetailKendaraanAdmin.this.imgKend);
        }
    }


    private class bookKend extends AsyncTask<String, String, String>{
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
            params.add(new BasicNameValuePair("tgl_kembali", tgl_kembali));
            params.add(new BasicNameValuePair("alamat", lokasi));
            params.add(new BasicNameValuePair("ID_kendaraan", idkend));

            JSONObject obj = jsonParser.makeHttpRequest(url_book, "POST", params);
            Log.d("Res: ", obj.toString());

            try{
                int sukses = obj.getInt(TAG_SUCCESS);
                if (sukses==1){
                    Intent i = new Intent(TrxKendaraan.this, Dashboard.class);
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
