package com.olympindo.spvolympindo;

import android.app.Activity;
import android.content.Intent;
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
import com.olympindo.spvolympindo.adapter.ListSearchDealerRViewAdapter;
import com.olympindo.spvolympindo.adapter.ListSearchKendaraanRViewAdapter;
import com.olympindo.spvolympindo.modal.DatabaseManager;
import com.olympindo.spvolympindo.modal.ListModel;
import com.olympindo.spvolympindo.modal.setter;
import com.olympindo.spvolympindo.util.RecyclerTouchListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListSearchMerkKendaraan extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private DatabaseManager dm;
    private EditText pencarian;
    private SwipeRefreshLayout swipe;
    private RecyclerView rvView;
    private RecyclerView.LayoutManager layoutManager;
    private String edit_search;
    private ArrayList<ListModel> postArrayList;
    private ListSearchKendaraanRViewAdapter adapter;
    private RequestQueue requestQueue;
    private static final String TAG = ListSurveyor.class.getSimpleName();
    private String spvIdKota;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_search_merk_kendaraan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish();
            }
        });

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
                String MerkId    = j.getData1();
                String MerkName  = j.getData2();

                Intent returnIntent = new Intent();
                returnIntent.putExtra("get_MerkId",MerkId);
                returnIntent.putExtra("get_MerkName",MerkName);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, setter.URL_SERVICE_11,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dm.deleteJsonAll("List Search Merk Kendaraan");
                            dm.addRowJson(String.valueOf(response),"List Search Merk Kendaraan");


                        } catch (Exception e) {
                            Log.e(TAG,"error "+e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(ListSearchMerkKendaraan.this,"Tidak Terhubung",Toast.LENGTH_LONG).show();
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

                /*params.put("url_base", setter.URL_SERVICE_1);
                params.put("manufacturer", manufacturer);
                params.put("model", model);
                params.put("serial", serial);
                params.put("versionName", versionName);
                params.put("versionCode", String.valueOf(versionCodeint));*/

                //returning parameter
                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        RetryPolicy policy = new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                viewData();
            }
        }, 1000);

    }


    private void viewData(){
        postArrayList=new ArrayList<>();
        ArrayList<ArrayList<Object>> data_list = dm.ambilBarisJson("List Search Merk Kendaraan");//
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
                            String MerkId    = ar.getString("MerkId");
                            String MerkName  = ar.getString("MerkName");

                            postArrayList.add(new ListModel(MerkId,MerkName));
                        }
                    }
                }
            }catch (JSONException e){
                Log.e(TAG,"errot "+e.getMessage());
            }
            adapter = new ListSearchKendaraanRViewAdapter(ListSearchMerkKendaraan.this,postArrayList);
            rvView.setAdapter(adapter);
        }
        swipe.setRefreshing(false);
    }

    @Override
    public void onRefresh() {

    }
}
