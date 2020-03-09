package com.olympindo.spvolympindo;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import com.olympindo.spvolympindo.service.BackgroundService;
import com.olympindo.spvolympindo.util.RecyclerTouchListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListOrder extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private DatabaseManager dm;
    private EditText pencarian;
    private SwipeRefreshLayout swipe;
    private RecyclerView rvView;
    private RecyclerView.LayoutManager layoutManager;
    private String edit_search;
    private ArrayList<ListModel> postArrayList;
    private ListOrderRViewAdapter adapter;
    private RequestQueue requestQueue;
    private static final String TAG = ListOrder.class.getSimpleName();
    private String getDataType;
    private String urlData;
    private int jumlah_notif;
    private String spvIdKota;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish();
            }
        });

        getDataType  = getIntent().getExtras().getString("data_type");

        dm = new DatabaseManager(this);

        ArrayList<ArrayList<Object>> dataUser = dm.ambilSemuaBaris();
        if (dataUser.size() > 0) {
            ArrayList<Object> baris = dataUser.get(0);
            spvIdKota      = baris.get(7).toString();
        }
        
        pencarian   = (EditText) findViewById(R.id.editText);
        swipe       = (SwipeRefreshLayout) findViewById(R.id.swipe);
        rvView      = (RecyclerView) findViewById(R.id.rv_main);



        swipe.setOnRefreshListener(this);
        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           calllistData();
                       }
                   }
        );


        rvView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvView.setLayoutManager(layoutManager);
        rvView.addOnItemTouchListener(new RecyclerTouchListener(this,
                rvView, new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, int position) {
                ListModel j = postArrayList.get(position);
                String id_order = j.getData1();

                finish();
                Intent listMobilLelang = new Intent(ListOrder.this,DetailOrder.class);
                Bundle detail = new Bundle();
                detail.putString("id_order", id_order);
                listMobilLelang.putExtras(detail);
                startActivity(listMobilLelang);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        pencarian.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }



            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edit_search = charSequence.toString();
                adapter.filter(edit_search);
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    public void calllistData(){
        swipe.setRefreshing(true);
        if(getDataType.equals("all")){
            urlData = setter.URL_SERVICE_3;
        }else if(getDataType.equals("survey")){
            urlData = setter.URL_SERVICE_5;
        }else if(getDataType.equals("new_order")){
            urlData = setter.URL_SERVICE_6;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("response data",response);
                        try {
                            dm.deleteJsonAll("List Order");
                            dm.addRowJson(String.valueOf(response),"List Order");


                        } catch (Exception e) {
                            Log.e(TAG,"error "+e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(ListOrder.this,"Tidak Terhubung",Toast.LENGTH_LONG).show();
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
                //Adding parameters to request
                /*String manufacturer = Build.MANUFACTURER;
                String model = Build.MODEL;
                String serial = Build.SERIAL;
                String versionName = BuildConfig.VERSION_NAME;
                int versionCodeint = BuildConfig.VERSION_CODE;*/

                params.put("tk", setter.API_KEY);
                params.put("id_kota", spvIdKota);


                /*params.put("url_base", setter.URL_SERVICE_1);
                params.put("manufacturer", manufacturer);
                params.put("model", model);
                params.put("serial", serial);
                params.put("versionName", versionName);
                params.put("versionCode", String.valueOf(versionCodeint));*/

                Log.w("DATA_POST"," list : "+params);
                //returning parameter
                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        RetryPolicy policy = new DefaultRetryPolicy(20000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                viewData();
            }
        }, 2000);

    }

    private void viewData(){
        postArrayList=new ArrayList<>();
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
                        Log.d("lengthdata",String.valueOf(data.length()));
                        for (int i=0; i<data.length(); i++){
                            JSONObject ar  = data.getJSONObject(i);

                            String id_order         = ar.getString("id_order");
                            String name             = ar.getString("name");
                            String status_data_cust = ar.getString("status_data_cust");
                            String jenis_kredit     = ar.getString("jenis_kredit");

                            postArrayList.add(new ListModel(id_order,name,status_data_cust,jenis_kredit));
                        }
                    }
                }
            }catch (JSONException e){
                Log.e(TAG,"errot "+e.getMessage());
            }
            adapter = new ListOrderRViewAdapter(ListOrder.this,postArrayList);
            rvView.setAdapter(adapter);
        }
        swipe.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        calllistData();
        pencarian.setText("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        BackgroundService.clearNotifications(getApplicationContext());
        jumlah_notif = 0;
        ArrayList<ArrayList<Object>> data_cek_notif = dm.ambilBarisNotifTugasALL();
        if (data_cek_notif.size() > 0) {
            for (int i = 0; i < data_cek_notif.size(); i++) {
                ArrayList<Object> baris = data_cek_notif.get(i);
                String status_notif = baris.get(3).toString();
                if (status_notif.equals("0")) {
                    jumlah_notif = jumlah_notif + 1;
                }
            }

            if (jumlah_notif > 0) {
                dm.updateBarisNotifTugas();
                startActivity(getIntent());
            }
        }
    }
}
