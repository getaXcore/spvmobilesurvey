package com.olympindo.spvolympindo.util;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.olympindo.spvolympindo.R;

/**
 * Created by Vigaz on 8/15/2018.
 */

public class OtherUtil {

    public static AlertDialog alertDialog;

    public static double distance(String slat1, String slon1, String slat2,
                                  String slon2) {

        Double lat1 = Double.parseDouble(slat1);
        Double lon1 = Double.parseDouble(slon1);
        Double lat2 = Double.parseDouble(slat2);
        Double lon2 = Double.parseDouble(slon2);

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        //double height = el1 - el2;

        //distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }



    public static void showAlertDialogLoading(Context context, String pesan) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        android.support.v7.app.AlertDialog.Builder builder1;
        builder1 = new android.support.v7.app.AlertDialog.Builder(context);
        View view = inflater.inflate(R.layout.dialog_loading, null);
        TextView textView1 = (TextView) view.findViewById(R.id.textViewPesanDialog);
        textView1.setText(pesan);
        builder1.setView(view);
        alertDialog = builder1.create();
        //alertDialog.getWindow().setBackgroundDrawableResource(0);
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    /**
     * Hide alert dialog merupakan function untuk menutup dialog loading.
     */
    public static boolean hideAlertDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
            return true;
        }
        return false;
    }
}
