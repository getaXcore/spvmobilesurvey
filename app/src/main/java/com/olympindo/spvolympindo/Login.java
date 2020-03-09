package com.olympindo.spvolympindo;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private Button login;
    private RequestQueue requestQueue;
    private static final String TAG = Login.class.getSimpleName();
    private EditText username,password;
    private DatabaseManager dm;
    private ImageView tampilPassword;
    private int statTamPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        statTamPass = 0;

        dm = new DatabaseManager(this);

        username        = (EditText)findViewById(R.id.et_username_login);
        password        = (EditText)findViewById(R.id.et_password_login);
        tampilPassword  = (ImageView)findViewById(R.id.tampil_password);
        login           = (Button)findViewById(R.id.masuk_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent clickLogin = new Intent(Login.this,Home.class);
                startActivity(clickLogin);*/
                login();
            }
        });

        tampilPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(statTamPass == 0){
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    statTamPass = 1;
                    tampilPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_visib_on));
                }else{
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    statTamPass = 0;
                    tampilPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_visib_off));
                }


            }
        });
    }


    private void login(){
        OtherUtil.hideAlertDialog();
        OtherUtil.showAlertDialogLoading(Login.this, "Please Wait ...");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, setter.URL_SERVICE_1,
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
                                    String id_admin         = ar.getString("id_admin");
                                    String namalengkap      = ar.getString("namalengkap");
                                    String username         = ar.getString("username");
                                    String alamat           = ar.getString("alamat");
                                    String jk               = ar.getString("jk");
                                    String telp             = ar.getString("telp");
                                    String kategori_admin   = ar.getString("kategori_admin");
                                    String id_kota          = ar.getString("id_kota");

                                    dm.addRow(id_admin,namalengkap,username,alamat,jk,telp,
                                            kategori_admin,id_kota);
                                    finish();
                                    Intent intent = new Intent(Login.this,Home.class);
                                    startActivity(intent);
                                }
                            }else{
                                Toast.makeText(Login.this,"Username atau Password tidak sesuai",Toast.LENGTH_LONG).show();
                            }
                            OtherUtil.hideAlertDialog();
                        } catch (Exception e) {
                            Log.e(TAG,"error "+e);
                            OtherUtil.hideAlertDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Login.this," Tidak Terhubung "+error.getMessage(),Toast.LENGTH_LONG).show();
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
                //Adding parameters to request
                /*String manufacturer = Build.MANUFACTURER;
                String model = Build.MODEL;
                String serial = Build.SERIAL;
                String versionName = BuildConfig.VERSION_NAME;
                int versionCodeint = BuildConfig.VERSION_CODE;*/

                params.put("username", username.getText().toString().trim());
                params.put("password", password.getText().toString().trim());
                params.put("tk", setter.API_KEY);
                /*params.put("url_base", setter.URL_SERVICE_1);
                params.put("manufacturer", manufacturer);
                params.put("model", model);
                params.put("serial", serial);
                params.put("versionName", versionName);
                params.put("versionCode", String.valueOf(versionCodeint));*/

                Log.w("DATA_POST","login : "+params);
                //returning parameter
                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        RetryPolicy policy = new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        OtherUtil.hideAlertDialog();
    }
}
