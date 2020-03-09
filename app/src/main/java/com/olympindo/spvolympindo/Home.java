package com.olympindo.spvolympindo;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
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
import com.olympindo.spvolympindo.modal.DatabaseManager;
import com.olympindo.spvolympindo.modal.setter;
import com.olympindo.spvolympindo.service.BackgroundService;
import com.olympindo.spvolympindo.util.OtherUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DatabaseManager dm;
    private String id_admin,namalengkap,alamat,spvIdKota;
    private RequestQueue requestQueue;
    private static final String TAG = Home.class.getSimpleName();
    private TextView jmlNewOrder,jmlSurvey,jmlArValid,jmlF9pd,jmlProcess,jmlApprove,jmlRewards,
            jmlReject;
    private CardView cvNewOrder,cvSurvey;
    Context ctx;
    public Context getCtx() {
        return ctx;
    }
    private BackgroundService mBackgroundService;
    Intent mServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dm = new DatabaseManager(this);

        ctx = this;
        mBackgroundService = new BackgroundService(getCtx());
        mServiceIntent = new Intent(getCtx(), mBackgroundService.getClass());
        if (!isMyServiceRunning(mBackgroundService.getClass())) {
            startService(mServiceIntent);
        }

        ArrayList<ArrayList<Object>> dataUser = dm.ambilSemuaBaris();
        if (dataUser.size() > 0) {
            ArrayList<Object> baris = dataUser.get(0);
            id_admin    = baris.get(0).toString();
            namalengkap = baris.get(1).toString();
            alamat      = baris.get(3).toString();
            spvIdKota   = baris.get(7).toString();
        }

        cvNewOrder  = (CardView)findViewById(R.id.cv_new_order);
        cvSurvey    = (CardView) findViewById(R.id.cv_survey);

        jmlNewOrder = (TextView)findViewById(R.id.tv_jml_new_order);
        jmlSurvey   = (TextView)findViewById(R.id.tv_jml_survey);
        jmlArValid  = (TextView)findViewById(R.id.tv_jml_ar_valid);
        jmlF9pd     = (TextView)findViewById(R.id.tv_jml_f9pd);
        jmlProcess  = (TextView)findViewById(R.id.tv_jml_process);
        jmlApprove  = (TextView)findViewById(R.id.tv_jml_approve);
        jmlRewards  = (TextView)findViewById(R.id.tv_jml_rewards);
        jmlReject   = (TextView)findViewById(R.id.tv_jml_reject);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        /*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
        TextView name = (TextView) header.findViewById(R.id.name);
        TextView kota = (TextView) header.findViewById(R.id.kota);

        name.setText(namalengkap);
        kota.setText(alamat);

        cvNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listOrder = new Intent(Home.this, ListOrder.class);
                Bundle detail = new Bundle();
                detail.putString("data_type", "new_order");
                listOrder.putExtras(detail);
                startActivity(listOrder);
            }
        });


        cvSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listOrder = new Intent(Home.this, ListOrder.class);
                Bundle detail = new Bundle();
                detail.putString("data_type", "survey");
                listOrder.putExtras(detail);
                startActivity(listOrder);
            }
        });
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_list_order) {
            Intent listOrder = new Intent(this, ListOrder.class);
            Bundle detail = new Bundle();
            detail.putString("data_type", "all");
            listOrder.putExtras(detail);
            startActivity(listOrder);
            // Handle the camera action
        } else if (id == R.id.nav_list_surveyor) {
            Intent listSurveyor = new Intent(this, ListSurveyor.class);
            startActivity(listSurveyor);
        } else if (id == R.id.nav_add_order) {
            Intent addOrder = new Intent(this, AddOrder.class);
            startActivity(addOrder);
        } else if (id == R.id.nav_logout) {
            dm.deleteAll();
            Intent Login = new Intent(Home.this, Login.class);
            startActivity(Login);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void jumlah_data(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, setter.URL_SERVICE_2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            String success = json.getString("code");
                            String message = json.getString("message");
                            if (success.equals("200")) {
                                String cek_data = json.getString("data");
                                if (!cek_data.equals("")) {
                                    JSONArray data = json.getJSONArray("data");
                                    JSONObject ar  = data.getJSONObject(0);

                                    String jumlah_new_order = ar.getString("jumlah_new_order");
                                    String jumlah_survey    = ar.getString("jumlah_survey");
                                    String jumlah_valid     = ar.getString("jumlah_valid");
                                    String jumlah_f9pd      = ar.getString("jumlah_f9pd");
                                    String jumlah_process   = ar.getString("jumlah_process");
                                    String jumlah_approve   = ar.getString("jumlah_approve");
                                    String jumlah_rewards   = ar.getString("jumlah_rewards");
                                    String jumlah_reject    = ar.getString("jumlah_reject");

                                    jmlNewOrder.setText(jumlah_new_order);
                                    jmlSurvey.setText(jumlah_survey);
                                    jmlArValid.setText(jumlah_valid);
                                    jmlF9pd.setText(jumlah_f9pd);
                                    jmlProcess.setText(jumlah_process);
                                    jmlApprove.setText(jumlah_approve);
                                    jmlRewards.setText(jumlah_rewards);
                                    jmlReject.setText(jumlah_reject);

                                }
                            }
                        } catch (Exception e) {
                            Log.e(TAG,"error "+e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Home.this,"Tidak Terhubung",Toast.LENGTH_LONG).show();
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

                //params.put("id_admin", id_admin);
                params.put("tk", setter.API_KEY);
                params.put("id_kota", spvIdKota);

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
    }

    private void updateStatusDataCustomer(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, setter.URL_SERVICE_7,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dm.deleteJsonAll("List Pilih Status Data Customer");
                            dm.addRowJson(String.valueOf(response),"List Pilih Status Data Customer");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
                //returning parameter
                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        RetryPolicy policy = new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }


    private void updateSurveyor(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, setter.URL_SERVICE_8,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dm.deleteJsonAll("List Pilih Surveyor");
                            dm.addRowJson(String.valueOf(response),"List Pilih Surveyor");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
                params.put("id_kota", spvIdKota);
                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        RetryPolicy policy = new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }


    private void updateCabang(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, setter.URL_SERVICE_10,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dm.deleteJsonAll("List Pilih Cabang");
                            dm.addRowJson(String.valueOf(response),"List Pilih Cabang");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        RetryPolicy policy = new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }



    private void updateMerk(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, setter.URL_SERVICE_11,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dm.deleteJsonAll("List Pilih Merk");
                            dm.addRowJson(String.valueOf(response),"List Pilih Merk");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        RetryPolicy policy = new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }



    private void updateModel(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, setter.URL_SERVICE_12,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dm.deleteJsonAll("List Pilih Model");
                            dm.addRowJson(String.valueOf(response),"List Pilih Model");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        RetryPolicy policy = new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }



    private void updateTypeKendaraan(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, setter.URL_SERVICE_13,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dm.deleteJsonAll("List Pilih Type Kendaraan");
                            dm.addRowJson(String.valueOf(response),"List Pilih Type Kendaraan");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        RetryPolicy policy = new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }


    private void updateWarna(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, setter.URL_SERVICE_14,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dm.deleteJsonAll("List Pilih Warna");
                            dm.addRowJson(String.valueOf(response),"List Pilih Warna");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        RetryPolicy policy = new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }


    /*private void updateDealer(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, setter.URL_SERVICE_15,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dm.deleteJsonAll("List Pilih Dealer");
                            dm.addRowJson(String.valueOf(response),"List Pilih Dealer");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        RetryPolicy policy = new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }*/


    private void updateAsuransi(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, setter.URL_SERVICE_16,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dm.deleteJsonAll("List Pilih Asuransi");
                            dm.addRowJson(String.valueOf(response),"List Pilih Asuransi");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        RetryPolicy policy = new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }


    @Override
    protected void onResume() {
        super.onResume();
        jumlah_data();

        updateStatusDataCustomer();
        updateSurveyor();
        updateCabang();
        //updateMerk();
        //updateModel();
        //updateTypeKendaraan();
        updateWarna();
        //updateDealer();
        updateAsuransi();
    }
}
