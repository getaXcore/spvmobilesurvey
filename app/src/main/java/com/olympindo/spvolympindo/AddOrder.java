package com.olympindo.spvolympindo;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.olympindo.spvolympindo.modal.DatabaseManager;
import com.olympindo.spvolympindo.modal.setter;
import com.olympindo.spvolympindo.util.OtherUtil;
import com.olympindo.spvolympindo.util.WrapAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddOrder extends AppCompatActivity {
    private DatabaseManager dm;
    private RequestQueue requestQueue;
    private String getIdKota;
    private ArrayList<String> cekListDealer,cekListJenisPinjaman,cekListPilihKendaraan,
            cekListJaminanMultiguna,cekListTenor,cekListMerk,cekListModel,cekListType,cekListWarna,
            cekListTahun,cekListAsuransi,cekListSurveyor;
    private Spinner spinnerJenisPinjaman,spinnerPilihKendaraan,
            spinnerJaminanMultiguna,spinnerJumlahTenorDana,spinnerTenor,spinnerAsuransi,
            spinnerPilihWarnaKendaraan,spinnerPilihTahunKendaraan;
    private String hasilJenisPinjaman,hasilJaminanMultiguna;
    private LinearLayout boxPilihKendaraan,boxJaminanMultiguna,boxJumlahPinjamanDanaTunai,
            boxJumlahTenorDana,boxGarisJaminanKendaraanMultiguna,boxPilihMerkKendaraan,
            boxPilihModelKendaraan,boxPilihTypeKendaraan,boxPilihWarnaKendaraan,
            boxPilihTahunKendaraan,boxPlatKendaraan,boxGarisDataPinjaman,boxNilaiOtr,
            boxTotalDownPayment,boxTenor,boxAsuransi;
    private String id_cabang;
    private ImageView photoId;
    private Uri imageUri;
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmapAll, cameraBmpAll, cameraBmpKecilAll;
    private String sphoto_link,sphoto_bitmap;
    private Button btSubmit;
    private EditText etNomorKtp,etNamaLengkap,etKodeNomorTelepon,etNomorTelepon,
            etKodeNomorTeleponRumah,etNomorTeleponRumah,etCabang,etDealer,etDescription,
            etJumlahPinjamanDanaTunai,etPlatKendaraan1,etPlatKendaraan2,etPlatKendaraan3,
            etNilaiOtr,etTotalDownPayment;
    private String idnumber,namalengkap,gender,kode_hp,telp,kode_area,telp_rmh,kode_cabang,
            id_dealer,alamat,image_ktp,jenisPinjaman,jaminan_multiguna,jml_dana,tenor_dana,
            merk_kendaraan,model_kendaraan,type_kendaraan,warna_kendaraan,tahun_kendaraan,
            plat_mobil_1,plat_mobil_2,plat_mobil_3,kategori_kendaraan,otr,dp,tenor,asuransi,
            id_surveyor;
    private RadioGroup radioJenisKelamin;
    private String hs_id_admin;
    private String hsMerkId,hsModelId;
    private EditText etSurveyor;
    private int reqDealer = 2;
    private int reqMerkKendaraan = 3;
    private int reqModelKendaraan = 4;
    private int reqTypeKendaraan = 5;
    private int reqSurveyor = 6;
    private EditText etMerkKendaraan,etModelKendaraan,etTypeKendaraan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish();
            }
        });
        id_cabang               = "";
        hasilJenisPinjaman      = "";
        hasilJaminanMultiguna   = "";
        sphoto_bitmap           = "";
        hs_id_admin             = "";

        hsMerkId                = "";
        hsModelId               = "";


        dm = new DatabaseManager(this);

        ArrayList<ArrayList<Object>> dataUser = dm.ambilSemuaBaris();
        if (dataUser.size() > 0) {
            ArrayList<Object> baris = dataUser.get(0);
            getIdKota      = baris.get(7).toString();
        }


        etNomorKtp                          = (EditText)findViewById(R.id.et_nomor_ktp);
        etNamaLengkap                       = (EditText)findViewById(R.id.et_nama_lengkap);
        radioJenisKelamin                   = (RadioGroup)findViewById(R.id.radio_jenis_kelamin);
        etKodeNomorTelepon                  = (EditText)findViewById(R.id.et_kode_nomor_telepon);
        etNomorTelepon                      = (EditText)findViewById(R.id.et_nomor_telepon);
        etKodeNomorTeleponRumah             = (EditText)findViewById(R.id.et_kode_nomor_telepon_rumah);
        etNomorTeleponRumah                 = (EditText)findViewById(R.id.et_nomor_telepon_rumah);
        etCabang                            = (EditText)findViewById(R.id.et_cabang);
        etDealer                            = (EditText)findViewById(R.id.et_dealer);
        //spinnerDealer                       = (Spinner)findViewById(R.id.spinner_dealer);
        etDescription                       = (EditText)findViewById(R.id.et_description);
        photoId                             = (ImageView)findViewById(R.id.photo_id);

        spinnerJenisPinjaman                = (Spinner)findViewById(R.id.spinner_jenis_pinjaman);
        spinnerPilihKendaraan               = (Spinner)findViewById(R.id.spinner_pilih_kendaraan);
        spinnerJaminanMultiguna             = (Spinner)findViewById(R.id.spinner_jaminan_multiguna);
        etJumlahPinjamanDanaTunai           = (EditText)findViewById(R.id.et_jumlah_pinjaman_dana_tunai);
        spinnerJumlahTenorDana              = (Spinner)findViewById(R.id.spinner_jumlah_tenor_dana);

        //spinnerPilihMerkKendaraan         = (Spinner)findViewById(R.id.spinner_pilih_merk_kendaraan);
        etMerkKendaraan                     = (EditText)findViewById(R.id.et_merk_kendaraan);
        //spinnerPilihModelKendaraan        = (Spinner)findViewById(R.id.spinner_pilih_model_kendaraan);
        etModelKendaraan                    = (EditText)findViewById(R.id.et_model_kendaraan);
        //spinnerPilihTypeKendaraan         = (Spinner)findViewById(R.id.spinner_pilih_type_kendaraan);
        etTypeKendaraan                     = (EditText) findViewById(R.id.et_type_kendaraan);
        spinnerPilihWarnaKendaraan          = (Spinner)findViewById(R.id.spinner_pilih_warna_kendaraan);
        spinnerPilihTahunKendaraan          = (Spinner)findViewById(R.id.spinner_pilih_tahun_kendaraan);
        etPlatKendaraan1                    = (EditText)findViewById(R.id.et_plat_kendaraan1);
        etPlatKendaraan2                    = (EditText)findViewById(R.id.et_plat_kendaraan2);
        etPlatKendaraan3                    = (EditText)findViewById(R.id.et_plat_kendaraan3);

        etNilaiOtr                          = (EditText)findViewById(R.id.et_nilai_otr);
        etTotalDownPayment                  = (EditText)findViewById(R.id.et_total_down_payment);
        spinnerTenor                        = (Spinner)findViewById(R.id.spinner_tenor);
        spinnerAsuransi                     = (Spinner)findViewById(R.id.spinner_asuransi);

        btSubmit                            = (Button)findViewById(R.id.bt_submit);

        boxPilihKendaraan                   = (LinearLayout)findViewById(R.id.box_pilih_kendaraan);
        boxJaminanMultiguna                 = (LinearLayout)findViewById(R.id.box_jaminan_multiguna);
        boxJumlahPinjamanDanaTunai          = (LinearLayout)findViewById(R.id.box_jumlah_pinjaman_dana_tunai);
        boxJumlahTenorDana                  = (LinearLayout)findViewById(R.id.box_jumlah_tenor_dana);
        boxGarisJaminanKendaraanMultiguna   = (LinearLayout)findViewById(R.id.box_garis_jaminan_kendaraan_multiguna);
        boxPilihMerkKendaraan               = (LinearLayout)findViewById(R.id.box_pilih_merk_kendaraan);
        boxPilihModelKendaraan              = (LinearLayout)findViewById(R.id.box_pilih_model_kendaraan);
        boxPilihTypeKendaraan               = (LinearLayout)findViewById(R.id.box_pilih_type_kendaraan);
        boxPilihWarnaKendaraan              = (LinearLayout)findViewById(R.id.box_pilih_warna_kendaraan);
        boxPilihTahunKendaraan              = (LinearLayout)findViewById(R.id.box_pilih_tahun_kendaraan);
        boxPlatKendaraan                    = (LinearLayout)findViewById(R.id.box_plat_kendaraan);

        boxGarisDataPinjaman                = (LinearLayout)findViewById(R.id.box_garis_data_pinjaman);
        boxNilaiOtr                         = (LinearLayout)findViewById(R.id.box_nilai_otr);
        boxTotalDownPayment                 = (LinearLayout)findViewById(R.id.box_total_down_payment);
        boxTenor                            = (LinearLayout)findViewById(R.id.box_tenor);
        boxAsuransi                         = (LinearLayout)findViewById(R.id.box_asuransi);


        etSurveyor                          = (EditText)findViewById(R.id.et_surveyor);
        //spinnerSurveyor                     = (Spinner)findViewById(R.id.spinner_surveyor);

        tampilCabang();
        //tampilDealer();
        tampilJenisPinjaman();
        tampilPilihKendaraan();
        tampilJaminanMultiguna();
        tampilTenor();
        //tampilMerk();
        //tampilModelNone();
        //tampilTypeNone();
        tampilWarna();
        tampilTahun();
        tampilAsuransi();
        //tampilSurveyor();


        setCurrencyCalculate(etJumlahPinjamanDanaTunai);
        setCurrencyCalculate(etNilaiOtr);
        setCurrencyCalculate(etTotalDownPayment);

        /*spinnerDealer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String checkHasilDealer = cekListDealer.get(i);
                if(!checkHasilDealer.equals("--")){
                    ArrayList<ArrayList<Object>> data_Dealer = dm.ambilBarisJson("List Pilih Dealer");
                    if(data_Dealer.size()>0){
                        try {
                            ArrayList<Object> baris_data_Dealer = data_Dealer.get(0);
                            String json_response = baris_data_Dealer.get(0).toString();

                            JSONObject json = new JSONObject(json_response);
                            String success = json.getString("code");
                            String message = json.getString("message");

                            if (success.equals("200")) {
                                String cek_data = json.getString("data");
                                JSONArray arrayData = new JSONArray(cek_data);
                                if (arrayData.length() > 0) {
                                    for (int ji=0; ji<arrayData.length(); ji++){
                                        JSONObject ar  = arrayData.getJSONObject(ji);
                                        String nama_dealer = ar.getString("nama_dealer");
                                        if(checkHasilDealer.equals(nama_dealer)){
                                            id_dealer = ar.getString("id_dealer");
                                        }
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    id_dealer = "";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

        spinnerJenisPinjaman.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String checkHasilJenisPinjaman = cekListJenisPinjaman.get(i);
                if(checkHasilJenisPinjaman.equals("Pembiayaan Multiguna")){
                    boxPilihKendaraan.setVisibility(View.GONE);
                    boxJaminanMultiguna.setVisibility(View.VISIBLE);
                    boxJumlahPinjamanDanaTunai.setVisibility(View.VISIBLE);
                    boxJumlahTenorDana.setVisibility(View.VISIBLE);

                    boxGarisDataPinjaman.setVisibility(View.GONE);
                    boxNilaiOtr.setVisibility(View.GONE);
                    boxTotalDownPayment.setVisibility(View.GONE);
                    boxTenor.setVisibility(View.GONE);
                    boxAsuransi.setVisibility(View.GONE);
                    hasilJenisPinjaman = "Multiguna";

                    spinnerJaminanMultiguna.setSelection(0);
                }else if(checkHasilJenisPinjaman.equals("Pembiayaan Mobil")){
                    boxPilihKendaraan.setVisibility(View.VISIBLE);
                    boxJaminanMultiguna.setVisibility(View.GONE);
                    boxJumlahPinjamanDanaTunai.setVisibility(View.GONE);
                    boxJumlahTenorDana.setVisibility(View.GONE);

                    boxGarisDataPinjaman.setVisibility(View.VISIBLE);
                    boxNilaiOtr.setVisibility(View.VISIBLE);
                    boxTotalDownPayment.setVisibility(View.VISIBLE);
                    boxTenor.setVisibility(View.VISIBLE);
                    boxAsuransi.setVisibility(View.VISIBLE);
                    hasilJenisPinjaman = "Kendaraan";



                    boxGarisJaminanKendaraanMultiguna.setVisibility(View.GONE);
                    boxPilihMerkKendaraan.setVisibility(View.GONE);
                    boxPilihModelKendaraan.setVisibility(View.GONE);
                    boxPilihTypeKendaraan.setVisibility(View.GONE);
                    boxPilihWarnaKendaraan.setVisibility(View.GONE);
                    boxPilihTahunKendaraan.setVisibility(View.GONE);
                    boxPlatKendaraan.setVisibility(View.GONE);
                }else{
                    boxJaminanMultiguna.setVisibility(View.GONE);
                    boxPilihKendaraan.setVisibility(View.GONE);
                    boxJumlahPinjamanDanaTunai.setVisibility(View.GONE);
                    boxJumlahTenorDana.setVisibility(View.GONE);

                    boxGarisDataPinjaman.setVisibility(View.GONE);
                    boxNilaiOtr.setVisibility(View.GONE);
                    boxTotalDownPayment.setVisibility(View.GONE);
                    boxTenor.setVisibility(View.GONE);
                    boxAsuransi.setVisibility(View.GONE);
                    hasilJenisPinjaman = "";


                    boxGarisJaminanKendaraanMultiguna.setVisibility(View.GONE);
                    boxPilihMerkKendaraan.setVisibility(View.GONE);
                    boxPilihModelKendaraan.setVisibility(View.GONE);
                    boxPilihTypeKendaraan.setVisibility(View.GONE);
                    boxPilihWarnaKendaraan.setVisibility(View.GONE);
                    boxPilihTahunKendaraan.setVisibility(View.GONE);
                    boxPlatKendaraan.setVisibility(View.GONE);


                    spinnerJaminanMultiguna.setSelection(0);
                }

                spinnerJumlahTenorDana.setSelection(0);
                spinnerTenor.setSelection(0);
                spinnerPilihKendaraan.setSelection(0);


                etMerkKendaraan.setText("");
                etModelKendaraan.setText("");
                etTypeKendaraan.setText("");
                hsMerkId = "";
                hsModelId = "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinnerPilihKendaraan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String checkHasilPilihKendaraan = cekListPilihKendaraan.get(i);
                if(checkHasilPilihKendaraan.equals("--")){
                    boxPilihMerkKendaraan.setVisibility(View.GONE);
                    boxPilihModelKendaraan.setVisibility(View.GONE);
                    boxPilihTypeKendaraan.setVisibility(View.GONE);
                    boxPilihWarnaKendaraan.setVisibility(View.GONE);
                    boxPilihTahunKendaraan.setVisibility(View.GONE);
                    boxPlatKendaraan.setVisibility(View.GONE);
                }else {
                    boxPilihMerkKendaraan.setVisibility(View.VISIBLE);
                    boxPilihModelKendaraan.setVisibility(View.VISIBLE);
                    boxPilihTypeKendaraan.setVisibility(View.VISIBLE);
                    boxPilihWarnaKendaraan.setVisibility(View.VISIBLE);
                    boxPilihTahunKendaraan.setVisibility(View.VISIBLE);
                    boxPlatKendaraan.setVisibility(View.VISIBLE);
                }
                //spinnerPilihMerkKendaraan.setSelection(0);
                etMerkKendaraan.setText("");
                etModelKendaraan.setText("");
                etTypeKendaraan.setText("");
                hsMerkId = "";
                hsModelId = "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        spinnerJaminanMultiguna.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String checkHasilJaminanMultiguna = cekListJaminanMultiguna.get(i);
                if(checkHasilJaminanMultiguna.equals("BPKB MOBIL")){
                    boxGarisJaminanKendaraanMultiguna.setVisibility(View.VISIBLE);
                    boxPilihMerkKendaraan.setVisibility(View.VISIBLE);
                    boxPilihModelKendaraan.setVisibility(View.VISIBLE);
                    boxPilihTypeKendaraan.setVisibility(View.VISIBLE);
                    boxPilihWarnaKendaraan.setVisibility(View.VISIBLE);
                    boxPilihTahunKendaraan.setVisibility(View.VISIBLE);
                    boxPlatKendaraan.setVisibility(View.VISIBLE);
                    hasilJaminanMultiguna = "BPKB MOBIL";
                }else if(checkHasilJaminanMultiguna.equals("SERTIFIKAT RUMAH")){
                    boxGarisJaminanKendaraanMultiguna.setVisibility(View.GONE);
                    boxPilihMerkKendaraan.setVisibility(View.GONE);
                    boxPilihModelKendaraan.setVisibility(View.GONE);
                    boxPilihTypeKendaraan.setVisibility(View.GONE);
                    boxPilihWarnaKendaraan.setVisibility(View.GONE);
                    boxPilihTahunKendaraan.setVisibility(View.GONE);
                    boxPlatKendaraan.setVisibility(View.GONE);
                    hasilJaminanMultiguna = "SERTIFIKAT RUMAH";
                    //spinnerPilihMerkKendaraan.setSelection(0);
                    etMerkKendaraan.setText("");
                    etModelKendaraan.setText("");
                    etTypeKendaraan.setText("");
                    hsMerkId = "";
                    hsModelId = "";
                }else{
                    boxGarisJaminanKendaraanMultiguna.setVisibility(View.GONE);
                    boxPilihMerkKendaraan.setVisibility(View.GONE);
                    boxPilihModelKendaraan.setVisibility(View.GONE);
                    boxPilihTypeKendaraan.setVisibility(View.GONE);
                    boxPilihWarnaKendaraan.setVisibility(View.GONE);
                    boxPilihTahunKendaraan.setVisibility(View.GONE);
                    boxPlatKendaraan.setVisibility(View.GONE);
                    hasilJaminanMultiguna = "";
                    //spinnerPilihMerkKendaraan.setSelection(0);
                    etMerkKendaraan.setText("");
                    etModelKendaraan.setText("");
                    etTypeKendaraan.setText("");
                    hsMerkId = "";
                    hsModelId = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });





        /*spinnerPilihMerkKendaraan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String checkHasilMerkKendaraan = cekListMerk.get(i);
                if(!checkHasilMerkKendaraan.equals("--")){
                    ArrayList<ArrayList<Object>> data_merk = dm.ambilBarisJson("List Pilih Merk");
                    if(data_merk.size()>0){
                        try {
                            ArrayList<Object> baris_data_merk = data_merk.get(0);
                            String json_response = baris_data_merk.get(0).toString();

                            JSONObject json = new JSONObject(json_response);
                            String success = json.getString("code");
                            String message = json.getString("message");

                            if (success.equals("200")) {
                                String cek_data = json.getString("data");
                                JSONArray arrayData = new JSONArray(cek_data);
                                if (arrayData.length() > 0) {
                                    for (int ji=0; ji<arrayData.length(); ji++){
                                        JSONObject ar  = arrayData.getJSONObject(ji);
                                        String MerkName = ar.getString("MerkName");
                                        if(checkHasilMerkKendaraan.equals(MerkName)){
                                            hsMerkId = ar.getString("MerkId");
                                            tampilModel(hsMerkId);
                                            spinnerPilihModelKendaraan.setSelection(0);
                                            spinnerPilihWarnaKendaraan.setSelection(0);
                                            spinnerPilihTahunKendaraan.setSelection(0);
                                        }
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }else{
                    hsMerkId = "";
                    tampilModelNone();
                    spinnerPilihModelKendaraan.setSelection(0);
                    spinnerPilihWarnaKendaraan.setSelection(0);
                    spinnerPilihTahunKendaraan.setSelection(0);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/



        /*spinnerPilihModelKendaraan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String checkHasilModelKendaraan = cekListModel.get(i);
                if(!checkHasilModelKendaraan.equals("--")){
                    ArrayList<ArrayList<Object>> data_merk = dm.ambilBarisJson("List Pilih Model");
                    if(data_merk.size()>0){
                        try {
                            ArrayList<Object> baris_data_merk = data_merk.get(0);
                            String json_response = baris_data_merk.get(0).toString();

                            JSONObject json = new JSONObject(json_response);
                            String success = json.getString("code");
                            String message = json.getString("message");

                            if (success.equals("200")) {
                                String cek_data = json.getString("data");
                                JSONArray arrayData = new JSONArray(cek_data);
                                if (arrayData.length() > 0) {
                                    for (int ji=0; ji<arrayData.length(); ji++){
                                        JSONObject ar  = arrayData.getJSONObject(ji);
                                        String ModelName = ar.getString("ModelName");
                                        if(checkHasilModelKendaraan.equals(ModelName)){
                                            hsModelId = ar.getString("ModelId");
                                            String MerkId = ar.getString("MerkId");
                                            tampilType(hsModelId,MerkId);
                                            spinnerPilihTypeKendaraan.setSelection(0);
                                        }
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }else{
                    hsModelId = "";
                    tampilTypeNone();
                    spinnerPilihTypeKendaraan.setSelection(0);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

        etDealer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data_param = new Intent(AddOrder.this,
                        ListSearchDealer.class);
                Bundle detail = new Bundle();
                detail.putString("data", "");
                data_param.putExtras(detail);
                startActivityForResult(data_param, reqDealer);
            }
        });


        etMerkKendaraan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data_param = new Intent(AddOrder.this,
                        ListSearchMerkKendaraan.class);
                Bundle detail = new Bundle();
                detail.putString("data", "");
                data_param.putExtras(detail);
                startActivityForResult(data_param, reqMerkKendaraan);
            }
        });

        etModelKendaraan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hsMerkId.equals("")){
                    Intent data_param = new Intent(AddOrder.this,
                            ListSearchModelKendaraan.class);
                    Bundle detail = new Bundle();
                    detail.putString("MerkId", ""+hsMerkId);
                    data_param.putExtras(detail);
                    startActivityForResult(data_param, reqModelKendaraan);
                }
            }
        });

        etTypeKendaraan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hsMerkId.equals("") && !hsModelId.equals("")){
                    Intent data_param = new Intent(AddOrder.this,
                            ListSearchTypeKendaraan.class);
                    Bundle detail = new Bundle();
                    detail.putString("MerkId", ""+hsMerkId);
                    detail.putString("ModelId", ""+hsModelId);
                    data_param.putExtras(detail);
                    startActivityForResult(data_param, reqTypeKendaraan);
                }
            }
        });

        etSurveyor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data_param = new Intent(AddOrder.this,
                        ListSearchSurveyor.class);
                Bundle detail = new Bundle();
                detail.putString("data", "");
                data_param.putExtras(detail);
                startActivityForResult(data_param, reqSurveyor);
            }
        });



        photoId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues value = new ContentValues();
                value.put(MediaStore.Images.Media.TITLE, "New Picture");
                value.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, value);
                Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                photoCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(photoCaptureIntent, PICK_IMAGE_REQUEST);
            }
        });


        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idnumber            = etNomorKtp.getText().toString().trim();
                namalengkap         = etNamaLengkap.getText().toString().trim();

                int selectedId = radioJenisKelamin.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(selectedId);
                gender              = radioButton.getText().toString().trim();

                kode_hp             = etKodeNomorTelepon.getText().toString().trim();
                telp                = etNomorTelepon.getText().toString().trim();
                kode_area           = etKodeNomorTeleponRumah.getText().toString().trim();
                telp_rmh            = etNomorTeleponRumah.getText().toString().trim();
                kode_cabang         = ""+id_cabang;
                id_dealer           = ""+id_dealer;
                alamat              = etDescription.getText().toString().trim();
                image_ktp           = ""+sphoto_bitmap;
                jenisPinjaman       = ""+hasilJenisPinjaman;
                jaminan_multiguna   = ""+hasilJaminanMultiguna;
                jml_dana            = ""+getRawVal(etJumlahPinjamanDanaTunai.getText().toString().trim());
                tenor_dana          = spinnerJumlahTenorDana.getSelectedItem().toString();
                merk_kendaraan      = ""+hsMerkId;
                model_kendaraan     = ""+hsModelId;
                type_kendaraan      = etTypeKendaraan.getText().toString().trim();
                warna_kendaraan     = spinnerPilihWarnaKendaraan.getSelectedItem().toString();
                tahun_kendaraan     = spinnerPilihTahunKendaraan.getSelectedItem().toString();
                plat_mobil_1        = etPlatKendaraan1.getText().toString().trim();
                plat_mobil_2        = etPlatKendaraan2.getText().toString().trim();
                plat_mobil_3        = etPlatKendaraan3.getText().toString().trim();

                kategori_kendaraan  = spinnerPilihKendaraan.getSelectedItem().toString();
                otr                 = ""+getRawVal(etNilaiOtr.getText().toString().trim());
                dp                  = ""+getRawVal(etTotalDownPayment.getText().toString().trim());
                tenor               = spinnerTenor.getSelectedItem().toString();
                asuransi            = spinnerAsuransi.getSelectedItem().toString();
                id_surveyor         = ""+hs_id_admin;


                if(idnumber.equals("")){
                    Toast.makeText(AddOrder.this,"Mohon isi nomor KTP",
                            Toast.LENGTH_LONG).show();
                }else if(idnumber.length() != 16){
                    Toast.makeText(AddOrder.this,"Nomor KTP Kurang",
                            Toast.LENGTH_LONG).show();
                }else if(namalengkap.equals("")){
                    Toast.makeText(AddOrder.this,"Mohon isi Nama Lengkap",
                            Toast.LENGTH_LONG).show();
                }else if(kode_hp.equals("")){
                    Toast.makeText(AddOrder.this,"Mohon isi Kode HP",
                            Toast.LENGTH_LONG).show();
                }else if(telp.equals("")){
                    Toast.makeText(AddOrder.this,"Mohon isi Telp",
                            Toast.LENGTH_LONG).show();
                }else if(kode_area.equals("")){
                    Toast.makeText(AddOrder.this,"Mohon isi Kode Area",
                            Toast.LENGTH_LONG).show();
                }else if(telp_rmh.equals("")){
                    Toast.makeText(AddOrder.this,"Mohon isi Telp Rumah",
                            Toast.LENGTH_LONG).show();
                }else if(telp_rmh.equals("")){
                    Toast.makeText(AddOrder.this,"Mohon isi Telp Rumah",
                            Toast.LENGTH_LONG).show();
                }else if(id_dealer.equals("")){
                    Toast.makeText(AddOrder.this,"Mohon pilih Dealer",
                            Toast.LENGTH_LONG).show();
                }else if(alamat.equals("")){
                    Toast.makeText(AddOrder.this,"Mohon isi Alamat",
                            Toast.LENGTH_LONG).show();
                }else if(image_ktp.equals("")){
                    Toast.makeText(AddOrder.this,"Mohon masukkan Foto",
                            Toast.LENGTH_LONG).show();
                }else if(jenisPinjaman.equals("")){
                    Toast.makeText(AddOrder.this,"Mohon pilih Jenis Pinjaman",
                            Toast.LENGTH_LONG).show();
                }else if(jenisPinjaman.equals("Multiguna")){
                    if(jaminan_multiguna.equals("")){
                        Toast.makeText(AddOrder.this,"Mohon pilih Jaminan Multiguna",
                                Toast.LENGTH_LONG).show();
                    }else if(jaminan_multiguna.equals("BPKB MOBIL")){
                        if(jml_dana.equals("")){
                            Toast.makeText(AddOrder.this,"Mohon isi Jumlah Dana",
                                    Toast.LENGTH_LONG).show();
                        }else if(tenor_dana.equals("--")){
                            Toast.makeText(AddOrder.this,"Mohon pilih Tenor",
                                    Toast.LENGTH_LONG).show();
                        }else if(merk_kendaraan.equals("")){
                            Toast.makeText(AddOrder.this,"Mohon pilih merk kendaraan",
                                    Toast.LENGTH_LONG).show();
                        }else if(model_kendaraan.equals("")){
                            Toast.makeText(AddOrder.this,"Mohon pilih model kendaraan",
                                    Toast.LENGTH_LONG).show();
                        }else if(type_kendaraan.equals("--")){
                            Toast.makeText(AddOrder.this,"Mohon pilih type kendaraan",
                                    Toast.LENGTH_LONG).show();
                        }else if(warna_kendaraan.equals("--")){
                            Toast.makeText(AddOrder.this,"Mohon pilih warna kendaraan",
                                    Toast.LENGTH_LONG).show();
                        }else if(tahun_kendaraan.equals("--")){
                            Toast.makeText(AddOrder.this,"Mohon pilih tahun kendaraan",
                                    Toast.LENGTH_LONG).show();
                        }else if(plat_mobil_1.equals("")){
                            Toast.makeText(AddOrder.this,"Mohon isi Plat Kendaraan",
                                    Toast.LENGTH_LONG).show();
                        }else if(plat_mobil_2.equals("")){
                            Toast.makeText(AddOrder.this,"Mohon isi Plat Kendaraan",
                                    Toast.LENGTH_LONG).show();
                        }else if(plat_mobil_3.equals("")){
                            Toast.makeText(AddOrder.this,"Mohon isi Plat Kendaraan",
                                    Toast.LENGTH_LONG).show();
                        }else if(id_surveyor.equals("")){
                            Toast.makeText(AddOrder.this,"Mohon pilih Surveyor",
                                    Toast.LENGTH_LONG).show();
                        }else{
                            simpan();
                        }

                    }else if(jaminan_multiguna.equals("SERTIFIKAT RUMAH")){
                        if(jml_dana.equals("")){
                            Toast.makeText(AddOrder.this,"Mohon isi Jumlah Dana",
                                    Toast.LENGTH_LONG).show();
                        }else if(tenor_dana.equals("--")){
                            Toast.makeText(AddOrder.this,"Mohon pilih Tenor",
                                    Toast.LENGTH_LONG).show();
                        }else if(id_surveyor.equals("")){
                            Toast.makeText(AddOrder.this,"Mohon pilih Surveyor",
                                    Toast.LENGTH_LONG).show();
                        }else{
                            simpan();
                        }
                    }
                }else if(jenisPinjaman.equals("Kendaraan")){
                    if(kategori_kendaraan.equals("--")){
                        Toast.makeText(AddOrder.this,"Mohon pilih Kendaraan",
                                Toast.LENGTH_LONG).show();
                    }else if(merk_kendaraan.equals("")){
                        Toast.makeText(AddOrder.this,"Mohon pilih merk kendaraan",
                                Toast.LENGTH_LONG).show();
                    }else if(model_kendaraan.equals("")){
                        Toast.makeText(AddOrder.this,"Mohon pilih model kendaraan",
                                Toast.LENGTH_LONG).show();
                    }else if(type_kendaraan.equals("--")){
                        Toast.makeText(AddOrder.this,"Mohon pilih type kendaraan",
                                Toast.LENGTH_LONG).show();
                    }else if(warna_kendaraan.equals("--")){
                        Toast.makeText(AddOrder.this,"Mohon pilih warna kendaraan",
                                Toast.LENGTH_LONG).show();
                    }else if(tahun_kendaraan.equals("--")){
                        Toast.makeText(AddOrder.this,"Mohon pilih tahun kendaraan",
                                Toast.LENGTH_LONG).show();
                    }else if(plat_mobil_1.equals("")){
                        Toast.makeText(AddOrder.this,"Mohon isi Plat Kendaraan",
                                Toast.LENGTH_LONG).show();
                    }else if(plat_mobil_2.equals("")){
                        Toast.makeText(AddOrder.this,"Mohon isi Plat Kendaraan",
                                Toast.LENGTH_LONG).show();
                    }else if(plat_mobil_3.equals("")){
                        Toast.makeText(AddOrder.this,"Mohon isi Plat Kendaraan",
                                Toast.LENGTH_LONG).show();
                    }else if(otr.equals("")){
                        Toast.makeText(AddOrder.this,"Mohon isi Otr",
                                Toast.LENGTH_LONG).show();
                    }else if(dp.equals("")){
                        Toast.makeText(AddOrder.this,"Mohon isi Down Payment",
                                Toast.LENGTH_LONG).show();
                    }else if(tenor.equals("--")){
                        Toast.makeText(AddOrder.this,"Mohon pilih Tenor",
                                Toast.LENGTH_LONG).show();
                    }else if(asuransi.equals("--")){
                        Toast.makeText(AddOrder.this,"Mohon pilih Asuransi",
                                Toast.LENGTH_LONG).show();
                    }else if(id_surveyor.equals("")){
                        Toast.makeText(AddOrder.this,"Mohon pilih Surveyor",
                                Toast.LENGTH_LONG).show();
                    }else{
                        simpan();
                    }
                }

                // all
                Log.i("hasil inp",
                        "\n idnumber : "+idnumber+
                                "\n namalengkap : "+namalengkap+
                                "\n gender : "+gender+
                                "\n kode_hp : "+kode_hp+
                                "\n telp : "+telp+
                                "\n kode_area : "+kode_area+
                                "\n telp_rmh : "+telp_rmh+
                                "\n kode_cabang : "+kode_cabang+
                                "\n id_dealer : "+id_dealer+
                                "\n alamat : "+alamat+
                                "\n image_ktp : "+
                                "\n jenis_pinjaman : "+jenisPinjaman+
                                "\n jaminan_multiguna : "+jaminan_multiguna+
                                "\n jml_dana : "+jml_dana+
                                "\n tenor_dana : "+tenor_dana+
                                "\n kategori_kendaraan : "+kategori_kendaraan+
                                "\n merk_kendaraan : "+merk_kendaraan+
                                "\n model_kendaraan : "+model_kendaraan+
                                "\n type_kendaraan : "+type_kendaraan+
                                "\n warna_kendaraan : "+warna_kendaraan+
                                "\n tahun_kendaraan : "+tahun_kendaraan+
                                "\n plat_mobil_1 : "+plat_mobil_1+
                                "\n plat_mobil_2 : "+plat_mobil_2+
                                "\n plat_mobil_3 : "+plat_mobil_3+
                                "\n otr : "+otr+
                                "\n dp : "+dp+
                                "\n tenor : "+tenor+
                                "\n asuransi : "+asuransi+
                                "\n id_surveyor : "+id_surveyor);



                /*// kendaraan
                Log.i("hasil inp",
                        "\n idnumber : "+idnumber+
                                "\n namalengkap : "+namalengkap+
                                "\n gender : "+gender+
                                "\n kode_hp : "+kode_hp+
                                "\n telp : "+telp+
                                "\n kode_area : "+kode_area+
                                "\n telp_rmh : "+telp_rmh+
                                "\n kode_cabang : "+kode_cabang+
                                "\n id_dealer : "+id_dealer+
                                "\n alamat : "+alamat+
                                "\n image_ktp : "+
                                "\n jenis_pinjaman : "+jenisPinjaman+
                                "\n jaminan_multiguna : "+
                                "\n jml_dana : "+
                                "\n tenor_dana : "+
                                "\n kategori_kendaraan : "+kategori_kendaraan+
                                "\n merk_kendaraan : "+merk_kendaraan+
                                "\n model_kendaraan : "+model_kendaraan+
                                "\n type_kendaraan : "+type_kendaraan+
                                "\n warna_kendaraan : "+warna_kendaraan+
                                "\n tahun_kendaraan : "+tahun_kendaraan+
                                "\n plat_mobil_1 : "+plat_mobil_1+
                                "\n plat_mobil_2 : "+plat_mobil_2+
                                "\n plat_mobil_3 : "+plat_mobil_3+
                                "\n otr : "+otr+
                                "\n dp : "+dp+
                                "\n tenor : "+tenor+
                                "\n asuransi : "+asuransi+
                                "\n id_surveyor : "+id_surveyor);*/



                /*// mutiguna jaminan kendaraan
                Log.i("hasil inp",
                        "\n idnumber : "+idnumber+
                                "\n namalengkap : "+namalengkap+
                                "\n gender : "+gender+
                                "\n kode_hp : "+kode_hp+
                                "\n telp : "+telp+
                                "\n kode_area : "+kode_area+
                                "\n telp_rmh : "+telp_rmh+
                                "\n kode_cabang : "+kode_cabang+
                                "\n id_dealer : "+id_dealer+
                                "\n alamat : "+alamat+
                                "\n image_ktp : "+
                                "\n jenis_pinjaman : "+jenisPinjaman+
                                "\n jaminan_multiguna : "+jaminan_multiguna+
                                "\n jml_dana : "+jml_dana+
                                "\n tenor_dana : "+tenor_dana+
                                "\n kategori_kendaraan : " +
                                "\n merk_kendaraan : "+merk_kendaraan+
                                "\n model_kendaraan : "+model_kendaraan+
                                "\n type_kendaraan : "+type_kendaraan+
                                "\n warna_kendaraan : "+warna_kendaraan+
                                "\n tahun_kendaraan : "+tahun_kendaraan+
                                "\n plat_mobil_1 : "+plat_mobil_1+
                                "\n plat_mobil_2 : "+plat_mobil_2+
                                "\n plat_mobil_3 : "+plat_mobil_3+
                                "\n otr : " +
                                "\n dp : " +
                                "\n tenor : " +
                                "\n asuransi : " +
                                "\n id_surveyor : "+id_surveyor);*/

                /*// mutiguna sertifikat rumah
                Log.i("hasil inp",
                        "\n idnumber : "+idnumber+
                        "\n namalengkap : "+namalengkap+
                        "\n gender : "+gender+
                        "\n kode_hp : "+kode_hp+
                        "\n telp : "+telp+
                        "\n kode_area : "+kode_area+
                        "\n telp_rmh : "+telp_rmh+
                        "\n kode_cabang : "+kode_cabang+
                        "\n id_dealer : "+id_dealer+
                        "\n alamat : "+alamat+
                        "\n image_ktp : "+
                        "\n jenis_pinjaman : "+jenisPinjaman+
                        "\n jaminan_multiguna : "+jaminan_multiguna+
                        "\n jml_dana : "+jml_dana+
                        "\n tenor_dana : "+tenor_dana+
                        "\n kategori_kendaraan : " +
                        "\n merk_kendaraan : " +
                        "\n model_kendaraan : " +
                        "\n type_kendaraan : " +
                        "\n warna_kendaraan : " +
                        "\n tahun_kendaraan : " +
                        "\n plat_mobil_1 : " +
                        "\n plat_mobil_2 : " +
                        "\n plat_mobil_3 : " +
                        "\n otr : " +
                        "\n dp : " +
                        "\n tenor : " +
                        "\n asuransi : " +
                        "\n id_surveyor : "+id_surveyor);*/
            }
        });
    }


    private void setCurrencyCalculate(final EditText edt) {
        edt.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(current)) {
                    edt.removeTextChangedListener(this);

                    Locale local = new Locale("id", "id");
                    String replaceable = String.format("[Rp,.\\s]",
                            NumberFormat.getCurrencyInstance().getCurrency().getSymbol(local));
                    String cleanString = s.toString().replaceAll(replaceable, "");

                    double parsed;
                    try {
                        parsed = Double.parseDouble(cleanString);
                    } catch (NumberFormatException e) {
                        parsed = 0.00;
                    }

                    // calculateAllocation();

                    NumberFormat formatter = NumberFormat.getCurrencyInstance(local);
                    formatter.setMaximumFractionDigits(0);
                    formatter.setParseIntegerOnly(true);
                    String formatted = formatter.format((parsed));

                    String replace = String.format("[\\s]", NumberFormat.getCurrencyInstance().
                            getCurrency().getSymbol(local));
                    String clean = formatted.replaceAll(replace, "");

                    current = formatted;
                    edt.setText(clean);
                    edt.setSelection(clean.length());
                    edt.addTextChangedListener(this);
                }
            }
        });
    }


    protected int getRawVal(String s){
        Locale local = new Locale("id", "id");
        String replaceable = String.format("[ Rp,.\\s]", NumberFormat.getCurrencyInstance().getCurrency()
                .getSymbol(local));
        String cleanString = s.replaceAll(replaceable, "");

        double parsed;
        try {
            parsed = Double.parseDouble(cleanString);
        } catch (NumberFormatException e) {
            parsed = 0;
        }

        return (int)parsed;
    }

    private void simpan(){
        OtherUtil.hideAlertDialog();
        OtherUtil.showAlertDialogLoading(AddOrder.this, "Please Wait ...");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, setter.URL_SERVICE_17,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            String success = json.getString("code");
                            String message = json.getString("message");
                            if (success.equals("200")) {
                                finish();
                            }else{
                                Toast.makeText(AddOrder.this,"Gagal Menyimpan",
                                        Toast.LENGTH_LONG).show();
                            }
                            OtherUtil.hideAlertDialog();
                        } catch (Exception e) {
                            e.printStackTrace();
                            OtherUtil.hideAlertDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddOrder.this,"Tidak Terhubung "+error,
                                Toast.LENGTH_LONG).show();
                        OtherUtil.hideAlertDialog();
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
                params.put("idnumber", idnumber);
                params.put("namalengkap", namalengkap);
                params.put("gender", gender);
                params.put("kode_hp", kode_hp);
                params.put("telp", telp);
                params.put("kode_area", kode_area);
                params.put("telp_rmh", telp_rmh);
                params.put("kode_cabang", kode_cabang);
                params.put("id_dealer", id_dealer);
                params.put("alamat", alamat);
                params.put("image_ktp", image_ktp);
                params.put("jenis_pinjaman", jenisPinjaman);
                params.put("jaminan_multiguna", jaminan_multiguna);
                params.put("jml_dana", jml_dana);
                params.put("tenor_dana", tenor_dana);
                params.put("kategori_kendaraan", kategori_kendaraan);
                params.put("merk_kendaraan" , merk_kendaraan);
                params.put("model_kendaraan" , model_kendaraan);
                params.put("type_kendaraan" , type_kendaraan);
                params.put("warna_kendaraan" , warna_kendaraan);
                params.put("tahun_kendaraan" , tahun_kendaraan);
                params.put("plat_mobil_1" , plat_mobil_1);
                params.put("plat_mobil_2" , plat_mobil_2);
                params.put("plat_mobil_3" , plat_mobil_3);
                params.put("otr" , otr);
                params.put("dp" , dp);
                params.put("tenor" , tenor);
                params.put("asuransi" , asuransi);
                params.put("id_surveyor" , id_surveyor);

                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        RetryPolicy policy = new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }






    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        byte[] imageBytes = baos.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    public void tampilCabang(){
        ArrayList<ArrayList<Object>> data_cabang = dm.ambilBarisJson("List Pilih Cabang");
        if(data_cabang.size()>0){
            try {
                ArrayList<Object> baris_data_cabang = data_cabang.get(0);
                String json_response = baris_data_cabang.get(0).toString();

                JSONObject json = new JSONObject(json_response);
                String success = json.getString("code");
                String message = json.getString("message");

                if (success.equals("200")) {
                    String cek_data = json.getString("data");
                    JSONArray arrayData = new JSONArray(cek_data);
                    if (arrayData.length() > 0) {
                        for (int i=0; i<arrayData.length(); i++){
                            JSONObject ar  = arrayData.getJSONObject(i);
                            String pilih_id_cabang = ar.getString("id_cabang");
                            if(getIdKota.equals(pilih_id_cabang)){
                                id_cabang = ar.getString("kode_cabang");
                                String nama_cabang = ar.getString("nama_cabang");
                                etCabang.setText(nama_cabang);

                            }
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    /*public void tampilDealer(){
        ArrayList<ArrayList<Object>> data_dealer = dm.ambilBarisJson("List Pilih Dealer");
        if(data_dealer.size()>0){
            try {
                ArrayList<Object> baris_data_dealer = data_dealer.get(0);
                String json_response = baris_data_dealer.get(0).toString();

                cekListDealer = new ArrayList<String>();
                cekListDealer.add("--");

                JSONObject json = new JSONObject(json_response);
                String success = json.getString("code");
                String message = json.getString("message");

                if (success.equals("200")) {
                    String cek_data = json.getString("data");
                    JSONArray arrayData = new JSONArray(cek_data);
                    if (arrayData.length() > 0) {
                        for (int i=0; i<arrayData.length(); i++){
                            JSONObject ar  = arrayData.getJSONObject(i);
                            String id_dealer = ar.getString("id_dealer");
                            String nama_dealer = ar.getString("nama_dealer");
                            cekListDealer.add(nama_dealer);
                        }
                    }
                }
                spinnerDealer.setAdapter(new WrapAdapter(this, cekListDealer));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }*/


    public void tampilJenisPinjaman(){
        cekListJenisPinjaman = new ArrayList<String>();
        cekListJenisPinjaman.add("--");
        cekListJenisPinjaman.add("Pembiayaan Multiguna");
        cekListJenisPinjaman.add("Pembiayaan Mobil");
        spinnerJenisPinjaman.setAdapter(new WrapAdapter(this, cekListJenisPinjaman));
    }

    public void tampilPilihKendaraan(){
        cekListPilihKendaraan = new ArrayList<String>();
        cekListPilihKendaraan.add("--");
        cekListPilihKendaraan.add("Mobil Bekas");
        cekListPilihKendaraan.add("Motor Besar Bekas");
        spinnerPilihKendaraan.setAdapter(new WrapAdapter(this, cekListPilihKendaraan));
    }


    public void tampilJaminanMultiguna(){
        cekListJaminanMultiguna = new ArrayList<String>();
        cekListJaminanMultiguna.add("--");
        cekListJaminanMultiguna.add("BPKB MOBIL");
        cekListJaminanMultiguna.add("SERTIFIKAT RUMAH");
        spinnerJaminanMultiguna.setAdapter(new WrapAdapter(this, cekListJaminanMultiguna));
    }

    public void tampilTenor(){
        cekListTenor = new ArrayList<String>();
        cekListTenor.add("--");
        cekListTenor.add("12");
        cekListTenor.add("24");
        cekListTenor.add("36");
        cekListTenor.add("48");
        cekListTenor.add("60");
        spinnerJumlahTenorDana.setAdapter(new WrapAdapter(this, cekListTenor));
        spinnerTenor.setAdapter(new WrapAdapter(this, cekListTenor));
    }


    /*public void tampilMerk(){
        ArrayList<ArrayList<Object>> data_merk = dm.ambilBarisJson("List Pilih Merk");
        if(data_merk.size()>0){
            try {
                ArrayList<Object> baris_data_merk = data_merk.get(0);
                String json_response = baris_data_merk.get(0).toString();

                cekListMerk = new ArrayList<String>();
                cekListMerk.add("--");

                JSONObject json = new JSONObject(json_response);
                String success = json.getString("code");
                String message = json.getString("message");

                if (success.equals("200")) {
                    String cek_data = json.getString("data");
                    JSONArray arrayData = new JSONArray(cek_data);
                    if (arrayData.length() > 0) {
                        for (int i=0; i<arrayData.length(); i++){
                            JSONObject ar  = arrayData.getJSONObject(i);
                            String MerkId = ar.getString("MerkId");
                            String MerkName = ar.getString("MerkName");
                            cekListMerk.add(MerkName);

                        }
                    }
                }
                spinnerPilihMerkKendaraan.setAdapter(new WrapAdapter(this, cekListMerk));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }*/


    /*public void tampilModel(String hsMerkKendaraan){
        ArrayList<ArrayList<Object>> data_merk = dm.ambilBarisJson("List Pilih Model");
        if(data_merk.size()>0){
            try {
                ArrayList<Object> baris_data_merk = data_merk.get(0);
                String json_response = baris_data_merk.get(0).toString();

                cekListModel = new ArrayList<String>();
                cekListModel.add("--");

                JSONObject json = new JSONObject(json_response);
                String success = json.getString("code");
                String message = json.getString("message");

                if (success.equals("200")) {
                    String cek_data = json.getString("data");
                    JSONArray arrayData = new JSONArray(cek_data);
                    if (arrayData.length() > 0) {
                        for (int i=0; i<arrayData.length(); i++){
                            JSONObject ar  = arrayData.getJSONObject(i);
                            String MerkId = ar.getString("MerkId");
                            if(hsMerkKendaraan.equals(MerkId)){
                                String ModelName = ar.getString("ModelName");
                                cekListModel.add(ModelName);
                            }


                        }
                    }
                }
                spinnerPilihModelKendaraan.setAdapter(new WrapAdapter(this, cekListModel));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }*/


    /*public void tampilModelNone(){
        cekListModel = new ArrayList<String>();
        cekListModel.add("--");
        spinnerPilihModelKendaraan.setAdapter(new WrapAdapter(this, cekListModel));
    }*/


    /*public void tampilType(String hsModelId, String hsMerkId){
        ArrayList<ArrayList<Object>> data_merk = dm.ambilBarisJson("List Pilih Type Kendaraan");
        if(data_merk.size()>0){
            try {
                ArrayList<Object> baris_data_merk = data_merk.get(0);
                String json_response = baris_data_merk.get(0).toString();

                cekListType = new ArrayList<String>();
                cekListType.add("--");

                JSONObject json = new JSONObject(json_response);
                String success = json.getString("code");
                String message = json.getString("message");

                if (success.equals("200")) {
                    String cek_data = json.getString("data");
                    JSONArray arrayData = new JSONArray(cek_data);
                    if (arrayData.length() > 0) {
                        for (int i=0; i<arrayData.length(); i++){
                            JSONObject ar  = arrayData.getJSONObject(i);
                            String ModelId = ar.getString("ModelId");
                            String MerkId = ar.getString("MerkId");
                            if(hsModelId.equals(ModelId) && hsMerkId.equals(MerkId)){
                                String type = ar.getString("type");
                                cekListType.add(type);
                            }


                        }
                    }
                }
                spinnerPilihTypeKendaraan.setAdapter(new WrapAdapter(this, cekListType));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }*/


    /*public void tampilTypeNone(){
        cekListType = new ArrayList<String>();
        cekListType.add("--");
        spinnerPilihTypeKendaraan.setAdapter(new WrapAdapter(this, cekListType));
    }*/



    public void tampilWarna(){
        ArrayList<ArrayList<Object>> data_warna = dm.ambilBarisJson("List Pilih Warna");
        if(data_warna.size()>0){
            try {
                ArrayList<Object> baris_data_warna = data_warna.get(0);
                String json_response = baris_data_warna.get(0).toString();

                cekListWarna = new ArrayList<String>();
                cekListWarna.add("--");

                JSONObject json = new JSONObject(json_response);
                String success = json.getString("code");
                String message = json.getString("message");

                if (success.equals("200")) {
                    String cek_data = json.getString("data");
                    JSONArray arrayData = new JSONArray(cek_data);
                    if (arrayData.length() > 0) {
                        for (int i=0; i<arrayData.length(); i++){
                            JSONObject ar  = arrayData.getJSONObject(i);
                            String id_warna = ar.getString("id_warna");
                            String warna = ar.getString("warna");
                            cekListWarna.add(warna);

                        }
                    }
                }
                spinnerPilihWarnaKendaraan.setAdapter(new WrapAdapter(this, cekListWarna));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void tampilTahun(){
        cekListTahun = new ArrayList<String>();
        cekListTahun.add("--");

        int date = 2018;
        for (int x = 1995; x <= date; x++) {
            cekListTahun.add(""+x);
        }
        spinnerPilihTahunKendaraan.setAdapter(new WrapAdapter(this, cekListTahun));
    }


    public void tampilAsuransi(){
        ArrayList<ArrayList<Object>> data_asuransi = dm.ambilBarisJson("List Pilih Asuransi");
        if(data_asuransi.size()>0){
            try {
                ArrayList<Object> baris_data_asuransi = data_asuransi.get(0);
                String json_response = baris_data_asuransi.get(0).toString();

                cekListAsuransi = new ArrayList<String>();
                cekListAsuransi.add("--");

                JSONObject json = new JSONObject(json_response);
                String success = json.getString("code");
                String message = json.getString("message");

                if (success.equals("200")) {
                    String cek_data = json.getString("data");
                    JSONArray arrayData = new JSONArray(cek_data);
                    if (arrayData.length() > 0) {
                        for (int i=0; i<arrayData.length(); i++){
                            JSONObject ar  = arrayData.getJSONObject(i);
                            String id_asuransi = ar.getString("id_asuransi");
                            String namaasuransi = ar.getString("namaasuransi");
                            cekListAsuransi.add(namaasuransi);
                        }
                    }
                }
                spinnerAsuransi.setAdapter(new WrapAdapter(this, cekListAsuransi));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            try {
                bitmapAll           = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                cameraBmpAll        = ThumbnailUtils.extractThumbnail(bitmapAll, 1205, 1795);
                cameraBmpKecilAll   = ThumbnailUtils.extractThumbnail(bitmapAll, 100, 100);

                sphoto_link      = getStringImage(cameraBmpKecilAll);
                sphoto_bitmap    = getStringImage(cameraBmpAll);

                photoId.setImageBitmap(cameraBmpAll);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if (requestCode == reqSurveyor && resultCode == RESULT_OK) {
            hs_id_admin = data.getStringExtra("get_id_admin");
            String getNamaLengkap = data.getStringExtra("get_namalengkap");
            etSurveyor.setText(getNamaLengkap);
        }else if (requestCode == reqDealer && resultCode == RESULT_OK) {
            id_dealer = data.getStringExtra("get_id_dealer");
            String getNamaDealer = data.getStringExtra("get_nama_dealer");
            etDealer.setText(getNamaDealer);
        }else if (requestCode == reqMerkKendaraan && resultCode == RESULT_OK) {
            hsMerkId = data.getStringExtra("get_MerkId");
            String getMerkName = data.getStringExtra("get_MerkName");
            etMerkKendaraan.setText(getMerkName);
            etModelKendaraan.setText("");
            etTypeKendaraan.setText("");
            hsModelId = "";
        }else if (requestCode == reqModelKendaraan && resultCode == RESULT_OK) {
            hsModelId = data.getStringExtra("get_ModelId");
            String getModelName = data.getStringExtra("get_ModelName");
            etModelKendaraan.setText(getModelName);
            etTypeKendaraan.setText("");
        }else if (requestCode == reqTypeKendaraan && resultCode == RESULT_OK) {
            String getType = data.getStringExtra("get_type");
            etTypeKendaraan.setText(getType);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        OtherUtil.hideAlertDialog();
    }
}
