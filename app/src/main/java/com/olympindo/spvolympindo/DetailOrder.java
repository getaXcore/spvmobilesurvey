package com.olympindo.spvolympindo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.olympindo.spvolympindo.adapter.ListOrderRViewAdapter;
import com.olympindo.spvolympindo.modal.DatabaseManager;
import com.olympindo.spvolympindo.modal.ListModel;
import com.olympindo.spvolympindo.modal.setter;
import com.olympindo.spvolympindo.util.OtherUtil;
import com.olympindo.spvolympindo.util.WrapAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailOrder extends AppCompatActivity {
    private TextView tvName,tvOrderNo,tvTipeIdentitas,tvNomorIdentitas,tvAlamatRumah,tvJenisKelamin,
            tvNomorTeleponRumah,tvNomorHandphone,tvStatusOrder,tvJenisKredit,tvJumlahPinjaman,
            tvJaminanMultiguna,tvTenor,tvJkJenisKredit,tvJkKategoriKendaraan,tvJkMerkKendaraan,
            tvJkModelKendaraan,tvJkTypeKendaraan,tvJkPlatNomor,tvJkTahun,tvJkWarna,
            tvKategoriKendaraan,tvMerkKendaraan,tvModelKendaraan, tvTypeKendaraan,tvPlatNomor,
            tvTahun,tvWarna,tvOtr,tvDp,tvCabang,tvDealer,tvStatusCustomer,tvSurveyor;
    private Spinner spinnerUpdateStatusCustomer,spinnerSurveyor;
    private EditText etDescription;
    private Button btSubmit;
    private DatabaseManager dm;
    private String getIdOrder;
    private String id_order,name,status_data_cust,jenis_kredit,status_order,order_code,
            identity_type,identity_no,address_home,sex,telephone,handphone_1,jml_pinjaman,
            jaminan_multiguna,tenor,kategori_kendaraan,MerkName,ModelName,type,plat_kendaraan,
            thn_kendaraan,warna,otr,dp,nama_cabang,nama_dealer,namalengkap;
    private static final String TAG = DetailOrder.class.getSimpleName();
    private LinearLayout boxTvKategoriKendaraan,boxTvMerkKendaraan,boxTvModelKendaraan,
            boxTvTypeKendaraan,boxTvPlatNomor,boxTvTahun,boxTvWarna,boxTvOtr,boxTvDp,
            boxTvJumlahPinjaman,boxTvJaminanMultiguna,boxTvTenor, boxTvJkKategoriKendaraan,
            boxTvStatusCustomer,boxTvUpdateStatusCustomer,boxTvDescription,boxTvSurveyor,
            boxTvSSurveyor,boxJaminanKendaraan;
    private ArrayList<String> cekListStatusDataCustomer,cekListSurveyor;
    private String spv_id_kota,kodeStatusDataCustomer;
    private RequestQueue requestQueue;
    private String kodeIdSurveyor;
    private String hket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish();
            }
        });
        hket                    = "";
        kodeIdSurveyor          = "";
        kodeStatusDataCustomer  = "0";

        dm = new DatabaseManager(this);

        ArrayList<ArrayList<Object>> dataUser = dm.ambilSemuaBaris();
        if (dataUser.size() > 0) {
            ArrayList<Object> baris = dataUser.get(0);
            spv_id_kota      = baris.get(7).toString();
        }

        getIdOrder  = getIntent().getExtras().getString("id_order");

        tvName                      = (TextView)findViewById(R.id.name);
        tvOrderNo                   = (TextView)findViewById(R.id.order_no);
        tvTipeIdentitas             = (TextView)findViewById(R.id.tipe_identitas);
        tvNomorIdentitas            = (TextView)findViewById(R.id.nomor_identitas);
        tvAlamatRumah               = (TextView)findViewById(R.id.alamat_rumah);
        tvJenisKelamin              = (TextView)findViewById(R.id.jenis_kelamin);
        tvNomorTeleponRumah         = (TextView)findViewById(R.id.nomor_telepon_rumah);
        tvNomorHandphone            = (TextView)findViewById(R.id.nomor_handphone);
        tvStatusOrder               = (TextView)findViewById(R.id.status_order);

        tvJenisKredit               = (TextView)findViewById(R.id.jenis_kredit);
        tvKategoriKendaraan         = (TextView)findViewById(R.id.kategori_kendaraan);
        tvMerkKendaraan             = (TextView)findViewById(R.id.merk_kendaraan);
        tvModelKendaraan            = (TextView)findViewById(R.id.model_kendaraan);
        tvTypeKendaraan             = (TextView)findViewById(R.id.type_kendaraan);
        tvPlatNomor                 = (TextView)findViewById(R.id.plat_nomor);
        tvTahun                     = (TextView)findViewById(R.id.tahun);
        tvWarna                     = (TextView)findViewById(R.id.warna);
        tvOtr                       = (TextView)findViewById(R.id.otr);
        tvDp                        = (TextView)findViewById(R.id.dp);
        tvJumlahPinjaman            = (TextView)findViewById(R.id.jumlah_pinjaman);
        tvJaminanMultiguna          = (TextView)findViewById(R.id.jaminan_multiguna);
        tvTenor                     = (TextView)findViewById(R.id.tenor);

        tvJkJenisKredit             = (TextView)findViewById(R.id.jk_jenis_kredit);
        tvJkKategoriKendaraan       = (TextView)findViewById(R.id.jk_kategori_kendaraan);
        tvJkMerkKendaraan           = (TextView)findViewById(R.id.jk_merk_kendaraan);
        tvJkModelKendaraan          = (TextView)findViewById(R.id.jk_model_kendaraan);
        tvJkTypeKendaraan           = (TextView)findViewById(R.id.jk_type_kendaraan);
        tvJkPlatNomor               = (TextView)findViewById(R.id.jk_plat_nomor);
        tvJkTahun                   = (TextView)findViewById(R.id.jk_tahun);
        tvJkWarna                   = (TextView)findViewById(R.id.jk_warna);

        tvCabang                    = (TextView)findViewById(R.id.cabang);
        tvDealer                    = (TextView)findViewById(R.id.dealer);
        tvStatusCustomer            = (TextView)findViewById(R.id.status_customer);
        tvSurveyor                  = (TextView)findViewById(R.id.surveyor);
        spinnerUpdateStatusCustomer = (Spinner)findViewById(R.id.spinner_update_status_customer);
        etDescription               = (EditText)findViewById(R.id.et_description);
        spinnerSurveyor             = (Spinner)findViewById(R.id.spinner_surveyor);

        btSubmit                    = (Button)findViewById(R.id.bt_submit);
        boxTvKategoriKendaraan      = (LinearLayout)findViewById(R.id.box_tv_kategori_kendaraan);
        boxTvMerkKendaraan          = (LinearLayout)findViewById(R.id.box_tv_merk_kendaraan);
        boxTvModelKendaraan         = (LinearLayout)findViewById(R.id.box_tv_model_kendaraan);
        boxTvTypeKendaraan          = (LinearLayout)findViewById(R.id.box_tv_type_kendaraan);
        boxTvPlatNomor              = (LinearLayout)findViewById(R.id.box_tv_plat_nomor);
        boxTvTahun                  = (LinearLayout)findViewById(R.id.box_tv_tahun);
        boxTvWarna                  = (LinearLayout)findViewById(R.id.box_tv_warna);
        boxTvOtr                    = (LinearLayout)findViewById(R.id.box_tv_otr);
        boxTvDp                     = (LinearLayout)findViewById(R.id.box_tv_dp);
        boxTvJumlahPinjaman         = (LinearLayout)findViewById(R.id.box_tv_jumlah_pinjaman);
        boxTvJaminanMultiguna       = (LinearLayout)findViewById(R.id.box_tv_jaminan_multiguna);
        boxTvTenor                  = (LinearLayout)findViewById(R.id.box_tv_tenor);
        
        boxTvJkKategoriKendaraan    = (LinearLayout)findViewById(R.id.box_tv_jk_kategori_kendaraan);

        boxTvStatusCustomer         = (LinearLayout)findViewById(R.id.box_tv_status_customer);
        boxTvUpdateStatusCustomer   = (LinearLayout)findViewById(R.id.box_tv_update_status_customer);
        boxTvDescription            = (LinearLayout)findViewById(R.id.box_tv_description);
        boxTvSurveyor               = (LinearLayout)findViewById(R.id.box_tv_surveyor);
        boxTvSSurveyor              = (LinearLayout)findViewById(R.id.box_tv_s_surveyor);

        boxJaminanKendaraan         = (LinearLayout)findViewById(R.id.box_jaminan_kendaraan);



        tampilStatusDataCustomer();
        tampilSurveyor();


        spinnerUpdateStatusCustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String checkHasilStatusDataCustomer = cekListStatusDataCustomer.get(i);
                if(checkHasilStatusDataCustomer.equals("Assignment Surveyor")){
                    boxTvSSurveyor.setVisibility(View.VISIBLE);
                    boxTvDescription.setVisibility(View.GONE);
                    kodeStatusDataCustomer = "1";
                }else if(checkHasilStatusDataCustomer.equals("Pending")){
                    boxTvSSurveyor.setVisibility(View.GONE);
                    boxTvDescription.setVisibility(View.VISIBLE);
                    kodeStatusDataCustomer = "2";
                }else if(checkHasilStatusDataCustomer.equals("Reroute HO")){
                    boxTvSSurveyor.setVisibility(View.GONE);
                    boxTvDescription.setVisibility(View.VISIBLE);
                    kodeStatusDataCustomer = "3";
                }else if(checkHasilStatusDataCustomer.equals("Cancel")){
                    boxTvSSurveyor.setVisibility(View.GONE);
                    boxTvDescription.setVisibility(View.VISIBLE);
                    kodeStatusDataCustomer = "4";
                }else{
                    boxTvSSurveyor.setVisibility(View.GONE);
                    boxTvDescription.setVisibility(View.GONE);
                    kodeStatusDataCustomer = "0";
                }
            }




            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        spinnerSurveyor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String checkHasilSurveyor = cekListSurveyor.get(i);
                if(checkHasilSurveyor.equals("--")){

                }else {
                    ArrayList<ArrayList<Object>> data_surveyor = dm.ambilBarisJson("List Pilih Surveyor");
                    if(data_surveyor.size()>0){
                        try {
                            ArrayList<Object> baris_data_surveyor = data_surveyor.get(0);
                            String json_response = baris_data_surveyor.get(0).toString();

                            JSONObject json = new JSONObject(json_response);
                            String success = json.getString("code");
                            String message = json.getString("message");

                            if (success.equals("200")) {
                                String cek_data = json.getString("data");
                                JSONArray arrayData = new JSONArray(cek_data);
                                if (arrayData.length() > 0) {
                                    for (int i_surveyor=0; i_surveyor<arrayData.length(); i_surveyor++){
                                        JSONObject ar  = arrayData.getJSONObject(i_surveyor);
                                        String pilih_id_kota = ar.getString("id_kota");
                                        //if(spv_id_kota.equals(pilih_id_kota)){
                                            String pilih_namalengkap = ar.getString("namalengkap");
                                            if(checkHasilSurveyor.equals(pilih_namalengkap)){
                                                kodeIdSurveyor = ar.getString("id_admin");
                                            }
                                        //}
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }



                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hSurveyor    = String.valueOf(spinnerSurveyor.getSelectedItem());
                String hDescription = etDescription.getText().toString().trim();
                if(kodeStatusDataCustomer.equals("0")){
                    Toast.makeText(DetailOrder.this,
                            "Silahkan pilih status data customer",Toast.LENGTH_LONG).show();
                }else if(kodeStatusDataCustomer.equals("1")){
                    if(hSurveyor.equals("--")){
                        Toast.makeText(DetailOrder.this,
                                "Silahkan pilih surveyor ",Toast.LENGTH_LONG).show();
                    }else{
                        simpan();
                        /*Toast.makeText(DetailOrder.this,
                                "Surveyor anda "+hSurveyor,Toast.LENGTH_LONG).show();*/
                    }
                }else{
                    if(hDescription.equals("")){
                        Toast.makeText(DetailOrder.this,
                                "Silahkan isi description ",Toast.LENGTH_LONG).show();
                    }else{
                        simpan();
                        /*Toast.makeText(DetailOrder.this,
                                "Isi text "+hDescription,Toast.LENGTH_LONG).show();*/
                    }
                }
            }
        });
    }


    private void simpan(){
        hket = etDescription.getText().toString().trim();

        OtherUtil.hideAlertDialog();
        OtherUtil.showAlertDialogLoading(DetailOrder.this, "Please Wait ...");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, setter.URL_SERVICE_9,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            String success = json.getString("code");
                            String message = json.getString("message");
                            if (success.equals("200")) {
                                finish();
                            }else {
                                Toast.makeText(DetailOrder.this,"gagal menyimpan",
                                        Toast.LENGTH_LONG).show();
                            }
                            OtherUtil.hideAlertDialog();
                        } catch (Exception e) {
                            OtherUtil.hideAlertDialog();
                            Log.e(TAG,"error "+e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        OtherUtil.hideAlertDialog();
                        Toast.makeText(DetailOrder.this,"Tidak Terhubung",Toast.LENGTH_LONG).show();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            Log.e("VOLLEYERROR","AuthFailureError");
                        } else if (error instanceof ServerError) {
                            Log.e("VOLLEYERROR","ServerError");
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Log.e("VOLLEYERROR","NetworkError");
                            //TODO
                        } else if (error instanceof ParseError) {
                            Log.e("VOLLEYERROR","ParseError");
                            //TODO
                        }
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("tk", setter.API_KEY);
                params.put("id_order", getIdOrder);
                params.put("status_data", kodeStatusDataCustomer);
                params.put("id_surveyor", kodeIdSurveyor);
                params.put("ket", hket);

                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        RetryPolicy policy = new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    private void viewData(){
        ArrayList<ArrayList<Object>> data_list = dm.ambilBarisJson("List Order");//
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

                            String check_id_order         = ar.getString("id_order");

                            if(getIdOrder.equals(check_id_order)){
                                id_order             = ar.getString("id_order");
                                name                 = ar.getString("name");
                                status_data_cust     = ar.getString("status_data_cust");
                                jenis_kredit         = ar.getString("jenis_kredit");
                                status_order         = ar.getString("status_order");

                                order_code           = ar.getString("order_code");
                                identity_type        = ar.getString("identity_type");
                                identity_no          = ar.getString("identity_no");
                                address_home         = ar.getString("address_home");
                                sex                  = ar.getString("sex");
                                telephone            = ar.getString("telephone");
                                handphone_1          = ar.getString("handphone_1");

                                jml_pinjaman         = ar.getString("jml_pinjaman");
                                jaminan_multiguna    = ar.getString("jaminan_multiguna");
                                tenor                = ar.getString("tenor");

                                kategori_kendaraan   = ar.getString("kategori_kendaraan");
                                MerkName             = ar.getString("MerkName");
                                ModelName            = ar.getString("ModelName");
                                type                 = ar.getString("type");
                                plat_kendaraan       = ar.getString("plat_kendaraan");
                                thn_kendaraan        = ar.getString("thn_kendaraan");
                                warna                = ar.getString("warna");
                                otr                  = ar.getString("otr");
                                dp                   = ar.getString("dp");
                                nama_cabang          = ar.getString("nama_cabang");
                                nama_dealer          = ar.getString("nama_dealer");
                                namalengkap          = ar.getString("namalengkap");



                                tvName.setText(name);
                                tvOrderNo.setText(order_code);
                                tvTipeIdentitas.setText(identity_type);
                                tvNomorIdentitas.setText(identity_no);
                                tvAlamatRumah.setText(address_home);
                                tvJenisKelamin.setText(sex);
                                tvNomorTeleponRumah.setText(telephone);
                                tvNomorHandphone.setText(handphone_1);
                                tvStatusOrder.setText(status_data_cust);

                                tvJenisKredit.setText(jenis_kredit);
                                if(jenis_kredit.equals("Multiguna")){
                                    tvJumlahPinjaman.setText(jml_pinjaman);
                                    tvJaminanMultiguna.setText(jaminan_multiguna);
                                    tvTenor.setText(tenor);

                                    if(jaminan_multiguna.equals("SERTIFIKAT RUMAH")){
                                        boxJaminanKendaraan.setVisibility(View.GONE);
                                    }else{
                                        tvJkJenisKredit.setText(jenis_kredit);
                                        if(!kategori_kendaraan.equals("null")){
                                            tvJkKategoriKendaraan.setText(kategori_kendaraan);
                                            boxTvJkKategoriKendaraan.setVisibility(View.VISIBLE);
                                        }else{
                                            boxTvJkKategoriKendaraan.setVisibility(View.GONE);
                                        }
                                        tvJkMerkKendaraan.setText(MerkName);
                                        tvJkModelKendaraan.setText(ModelName);
                                        tvJkTypeKendaraan.setText(type);
                                        tvJkPlatNomor.setText(plat_kendaraan);
                                        tvJkTahun.setText(thn_kendaraan);
                                        tvJkWarna.setText(warna);

                                        boxJaminanKendaraan.setVisibility(View.VISIBLE);
                                    }


                                    boxTvMerkKendaraan.setVisibility(View.GONE);
                                    boxTvModelKendaraan.setVisibility(View.GONE);
                                    boxTvTypeKendaraan.setVisibility(View.GONE);
                                    boxTvPlatNomor.setVisibility(View.GONE);
                                    boxTvTahun.setVisibility(View.GONE);
                                    boxTvWarna.setVisibility(View.GONE);
                                    boxTvOtr.setVisibility(View.GONE);
                                    boxTvDp.setVisibility(View.GONE);
                                    boxTvJumlahPinjaman.setVisibility(View.VISIBLE);
                                    boxTvJaminanMultiguna.setVisibility(View.VISIBLE);
                                    boxTvTenor.setVisibility(View.VISIBLE);


                                    ;
                                }else{
                                    if(!kategori_kendaraan.equals("null")){
                                        tvKategoriKendaraan.setText(kategori_kendaraan);
                                        boxTvKategoriKendaraan.setVisibility(View.VISIBLE);
                                    }else{
                                        boxTvKategoriKendaraan.setVisibility(View.GONE);
                                    }
                                    tvMerkKendaraan.setText(MerkName);
                                    tvModelKendaraan.setText(ModelName);
                                    tvTypeKendaraan.setText(type);
                                    tvPlatNomor.setText(plat_kendaraan);
                                    tvTahun.setText(thn_kendaraan);
                                    tvWarna.setText(warna);
                                    tvOtr.setText(otr);
                                    tvDp.setText(dp);

                                    boxTvMerkKendaraan.setVisibility(View.VISIBLE);
                                    boxTvModelKendaraan.setVisibility(View.VISIBLE);
                                    boxTvTypeKendaraan.setVisibility(View.VISIBLE);
                                    boxTvPlatNomor.setVisibility(View.VISIBLE);
                                    boxTvTahun.setVisibility(View.VISIBLE);
                                    boxTvWarna.setVisibility(View.VISIBLE);
                                    boxTvOtr.setVisibility(View.VISIBLE);
                                    boxTvDp.setVisibility(View.VISIBLE);
                                    boxTvJumlahPinjaman.setVisibility(View.GONE);
                                    boxTvJaminanMultiguna.setVisibility(View.GONE);
                                    boxTvTenor.setVisibility(View.GONE);

                                    boxJaminanKendaraan.setVisibility(View.GONE);
                                }

                                tvCabang.setText(nama_cabang);
                                tvDealer.setText(nama_dealer);

                                if(status_order.equals("0")){
                                    boxTvStatusCustomer.setVisibility(View.GONE);
                                    boxTvUpdateStatusCustomer.setVisibility(View.VISIBLE);
                                    boxTvDescription.setVisibility(View.VISIBLE);
                                    boxTvSurveyor.setVisibility(View.GONE);
                                    boxTvSSurveyor.setVisibility(View.VISIBLE);
                                    btSubmit.setVisibility(View.VISIBLE);
                                }else if(status_order.equals("1")){
                                    tvStatusCustomer.setText("Send To Surveyor");
                                    tvSurveyor.setText(namalengkap);
                                    boxTvStatusCustomer.setVisibility(View.VISIBLE);
                                    boxTvUpdateStatusCustomer.setVisibility(View.GONE);
                                    boxTvDescription.setVisibility(View.GONE);
                                    boxTvSurveyor.setVisibility(View.VISIBLE);
                                    boxTvSSurveyor.setVisibility(View.GONE);
                                    btSubmit.setVisibility(View.GONE);
                                }else{
                                    boxTvStatusCustomer.setVisibility(View.GONE);
                                    boxTvUpdateStatusCustomer.setVisibility(View.GONE);
                                    boxTvDescription.setVisibility(View.GONE);
                                    boxTvSurveyor.setVisibility(View.GONE);
                                    boxTvSSurveyor.setVisibility(View.GONE);
                                    btSubmit.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                }
            }catch (JSONException e){
                Log.e(TAG,"errot "+e.getMessage());
            }
        }
    }



    public void tampilStatusDataCustomer(){
        ArrayList<ArrayList<Object>> data_status_data_customer = dm.ambilBarisJson("List Pilih Status Data Customer");
        if(data_status_data_customer.size()>0){


            try {
                ArrayList<Object> baris_data_status_data_customer = data_status_data_customer.get(0);
                String json_response = baris_data_status_data_customer.get(0).toString();

                cekListStatusDataCustomer = new ArrayList<String>();
                cekListStatusDataCustomer.add("--");

                JSONObject json = new JSONObject(json_response);
                String success = json.getString("code");
                String message = json.getString("message");

                if (success.equals("200")) {
                    String cek_data = json.getString("data");
                    JSONArray arrayData = new JSONArray(cek_data);
                    if (arrayData.length() > 0) {
                        for (int i=0; i<arrayData.length(); i++){
                            JSONObject ar  = arrayData.getJSONObject(i);
                            String kode_status_data_customer = ar.getString("kode_status_data_customer");
                            String nama_status_data_customer = ar.getString("nama_status_data_customer");
                            cekListStatusDataCustomer.add(nama_status_data_customer);
                        }
                    }
                }
                spinnerUpdateStatusCustomer.setAdapter(new WrapAdapter(this, cekListStatusDataCustomer));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void tampilSurveyor(){
        ArrayList<ArrayList<Object>> data_status_data_customer = dm.ambilBarisJson("List Pilih Surveyor");
        if(data_status_data_customer.size()>0){
            try {
                ArrayList<Object> baris_data_status_data_customer = data_status_data_customer.get(0);
                String json_response = baris_data_status_data_customer.get(0).toString();

                cekListSurveyor = new ArrayList<String>();
                cekListSurveyor.add("--");

                JSONObject json = new JSONObject(json_response);
                String success = json.getString("code");
                String message = json.getString("message");

                if (success.equals("200")) {
                    String cek_data = json.getString("data");
                    JSONArray arrayData = new JSONArray(cek_data);
                    if (arrayData.length() > 0) {
                        for (int i=0; i<arrayData.length(); i++){
                            JSONObject ar  = arrayData.getJSONObject(i);
                            String pilih_id_kota = ar.getString("id_kota");
                            if(spv_id_kota.equals(pilih_id_kota)){
                                String pilih_id_admin = ar.getString("id_admin");
                                String pilih_namalengkap = ar.getString("namalengkap");
                                cekListSurveyor.add(pilih_namalengkap);
                            }
                        }
                    }
                }
                spinnerSurveyor.setAdapter(new WrapAdapter(this, cekListSurveyor));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        viewData();
    }
}
