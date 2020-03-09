package com.olympindo.spvolympindo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.olympindo.spvolympindo.modal.DatabaseManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailSurveyor extends AppCompatActivity {
    private DatabaseManager dm;
    private String getIdAdmin,id_order,namalengkap,alamat,active,jk,telp,jumlah_tugas,data_active,
            data_jk;
    private TextView tvNamaLengkap,tvAlamat,tvJenisKelamin,tvNomorTelepon,tvStatusSurveyor,
            tvJumlahTugas;
    private static final String TAG = DetailSurveyor.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_surveyor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish();
            }
        });


        dm = new DatabaseManager(this);
        getIdAdmin  = getIntent().getExtras().getString("id_admin");

        tvNamaLengkap       = (TextView)findViewById(R.id.nama_lengkap);
        tvAlamat            = (TextView)findViewById(R.id.alamat);
        tvJenisKelamin      = (TextView)findViewById(R.id.jenis_kelamin);
        tvNomorTelepon      = (TextView)findViewById(R.id.nomor_telepon);
        tvStatusSurveyor    = (TextView)findViewById(R.id.status_surveyor);
        tvJumlahTugas       = (TextView)findViewById(R.id.jumlah_tugas);
    }


    private void viewData(){
        ArrayList<ArrayList<Object>> data_list = dm.ambilBarisJson("List Surveyor");//
        if (data_list.size() > 0) {
            try {
                ArrayList<Object> baris = data_list.get(0);
                String json_response = baris.get(0).toString();

                JSONObject json = new JSONObject(json_response);
                String success = json.getString("code");
                String message = json.getString("message");

                if (success.equals("200")) {
                    String cek_data = json.getString("data");
                    if (!cek_data.equals("")) {
                        JSONArray data = json.getJSONArray("data");
                        for (int i=0; i<cek_data.length(); i++){
                            JSONObject ar  = data.getJSONObject(i);

                            String check_id_admin         = ar.getString("id_admin");

                            if(getIdAdmin.equals(check_id_admin)){
                                id_order        = ar.getString("id_admin");
                                namalengkap     = ar.getString("namalengkap");
                                alamat          = ar.getString("alamat");
                                active          = ar.getString("active");
                                jk              = ar.getString("jk");
                                telp            = ar.getString("telp");
                                jumlah_tugas    = ar.getString("jumlah_tugas");


                                if(active.equals("0")){
                                    data_active = "Non Active";
                                }else{
                                    data_active = "Active";
                                }

                                if(jk.equals("1")){
                                    data_jk = "Laki - laki";
                                }else{
                                    data_jk = "Perempuan";
                                }

                                tvNamaLengkap.setText(namalengkap);
                                tvAlamat.setText(alamat);
                                tvJenisKelamin.setText(data_jk);
                                tvNomorTelepon.setText(telp);
                                tvStatusSurveyor.setText(data_active);
                                tvJumlahTugas.setText(jumlah_tugas);
                            }
                        }
                    }
                }
            }catch (JSONException e){
                Log.e(TAG,"errot "+e.getMessage());
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        viewData();
    }
}
