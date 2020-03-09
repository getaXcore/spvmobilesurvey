package com.olympindo.spvolympindo.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.olympindo.spvolympindo.ListOrder;
import com.olympindo.spvolympindo.R;
import com.olympindo.spvolympindo.modal.DatabaseManager;
import com.olympindo.spvolympindo.modal.setter;

public class BackgroundService extends Service {
    private DatabaseManager dm;
    private RequestQueue requestQueue;
    private String id_admin,namalengkap,alamat,spvIdKota;
    private static final String TAG = BackgroundService.class.getSimpleName();
    public int counter=0;
    public Context context = this;
    private String hsIdOrder,hsName,hsAddressHome;

    public BackgroundService(Context applicationContext) {
        super();
        Log.i("HERE", "here I am!");
    }

    public BackgroundService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        Intent broadcastIntent = new Intent("com.olympindo.spvolympindo.ActivityRecognition.RestartSensor");
        sendBroadcast(broadcastIntent);
        stoptimertask();
    }

    private Timer timer;
    private TimerTask timerTask;
    long oldTime=0;
    public void startTimer() {
        dm = new DatabaseManager(this);
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 10000, 12000); //
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.i("in timer", "in timer ++++  "+ (counter++));
                cek_tugas();
            }
        };
    }


    public void cek_tugas(){
        dm = new DatabaseManager(this);
        ArrayList<ArrayList<Object>> data_user = dm.ambilSemuaBaris();
        if (data_user.size() > 0) {
            ArrayList<Object> baris_user = data_user.get(0);
            id_admin    = baris_user.get(0).toString();
            namalengkap = baris_user.get(1).toString();
            alamat      = baris_user.get(3).toString();
            spvIdKota   = baris_user.get(7).toString();



            StringRequest stringRequest = new StringRequest(Request.Method.POST, setter.URL_SERVICE_6,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jObj = new JSONObject(response);
                                String code = jObj.getString("code");
                                if (code.equals("200")) {
                                    String data = jObj.getString("data");
                                    JSONArray arrayData = new JSONArray(data);
                                    if (arrayData.length() > 0) {
                                        for(int i=0; i<arrayData.length(); i++){
                                            JSONObject obj = arrayData.getJSONObject(i);
                                            hsIdOrder       = obj.getString("id_order");
                                            hsName          = obj.getString("name");
                                            hsAddressHome   = obj.getString("address_home");

                                            ArrayList<ArrayList<Object>> data_notif = dm.ambilBarisNotifTugas(hsIdOrder);
                                            if (data_notif.size() < 1) {
                                                dm.addRowNotifTugas(hsIdOrder,hsName,hsAddressHome);

                                                playNotificationSound();

                                                NotificationCompat.Builder mBuilder =
                                                        new NotificationCompat.Builder(context);
                                                //Create the intent that’ll fire when the user taps the notification//
                                                //Intent intent = new Intent(Intent.ACTION_VIEW, uri.parse("https://www.androidauthority.com/"));
                                                Intent intent = new Intent(context, ListOrder.class);
                                                Bundle detail = new Bundle();
                                                detail.putString("data_type", "new_order");
                                                intent.putExtras(detail);
                                                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

                                                mBuilder.setContentIntent(pendingIntent);

                                                mBuilder.setColor(getResources().getColor(R.color.blue));
                                                mBuilder.setSmallIcon(R.drawable.ic_jtbaru);
                                                // mBuilder.setLargeIcon(R.drawable.person_icon);
                                                mBuilder.setContentTitle("Tugas Baru ke "+hsName);
                                                mBuilder.setContentText(hsAddressHome);
                                                NotificationManager mNotificationManager =
                                                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                                mNotificationManager.notify(i, mBuilder.build());
                                            }
                                        }
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
                            Toast.makeText(context,"Tidak Terhubung",Toast.LENGTH_LONG).show();
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

                    Log.w("DATA_POST"," list : "+params);
                    return params;
                }
            };
            requestQueue = Volley.newRequestQueue(this);
            RetryPolicy policy = new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            requestQueue.add(stringRequest);
        }

    }


    public void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void playNotificationSound() {
        try {
            /*Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + this.getPackageName() + "/raw/notification");*/
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + this.getPackageName() + "/raw/arpeggio");
            Ringtone r = RingtoneManager.getRingtone(this, alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}