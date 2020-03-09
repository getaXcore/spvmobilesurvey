package com.olympindo.spvolympindo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.olympindo.spvolympindo.modal.DatabaseManager;

import java.util.ArrayList;

public class SplashScreen extends AppCompatActivity {
    private DatabaseManager dm;
    private int PERMISSION_ALL = 1;
    private String[] PERMISSIONS = {android.Manifest.
            permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.CAMERA};
    private TextView txDetailVersion;
    private PackageInfo pInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        dm = new DatabaseManager(this);


        txDetailVersion = (TextView)findViewById(R.id.tx_detail_version);

        try {
            pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            txDetailVersion.setText(" "+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void check(){
        ArrayList<ArrayList<Object>> data = dm.ambilSemuaBaris();//
        if (data.size() < 1) {
            directLogin();
        } else {
            directHome();
        }
    }

    private void directLogin(){
        final int welcomeScreenDisplay = 3000; // 3000 = 3 detik
        Thread welcomeThread = new Thread() {
            int wait = 0;
            @Override
            public void run() {
                try {
                    super.run();
                    while (wait < welcomeScreenDisplay) {
                        sleep(100);
                        wait += 100;
                    }
                } catch (Exception e) {
                    System.out.println("EXc=" + e);
                } finally {
                    finish();
                    Intent intent = new Intent(
                            SplashScreen.this,
                            Login.class);
                    startActivity(intent);
                }
            }
        };
        welcomeThread.start();
    }


    private void directHome(){
        final int welcomeScreenDisplay = 3000; // 3000 = 3 detik
        Thread welcomeThread = new Thread() {
            int wait = 0;
            @Override
            public void run() {
                try {
                    super.run();
                    while (wait < welcomeScreenDisplay) {
                        sleep(100);
                        wait += 100;
                    }
                } catch (Exception e) {
                    System.out.println("EXc=" + e);
                } finally {
                    finish();
                    Intent intent = new Intent(
                            SplashScreen.this,
                            Home.class);
                    startActivity(intent);
                }
            }
        };
        welcomeThread.start();
    }


    public boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null &&
                permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.
                        PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_ALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                check();
            } else {
                Toast.makeText(this, "Mohon Aktifkan Perizinan untuk dapat menggunakan Aplikasi",
                        Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (hasPermissions(SplashScreen.this, PERMISSIONS)) {
            //check(); //asalnya nyala
            Intent intent = new Intent(
                    SplashScreen.this,
                    Login.class);
            startActivity(intent);
            finish();
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

    }
}
