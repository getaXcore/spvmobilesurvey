package com.olympindo.spvolympindo.service;

/**
 * Created by Vigaz on 11/13/2017.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BackgroundRestarterBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(BackgroundRestarterBroadcastReceiver.class.getSimpleName(), "Service Stops! Oooooooooooooppppssssss!!!!");

        context.startService(new Intent(context, BackgroundService.class));;
    }

}
