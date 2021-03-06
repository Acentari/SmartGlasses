package com.example.kostas.smartglasses;


import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;


public class Main extends AppCompatActivity {

    //assignment of some variables
    public static TextView t;
    TextView m;
    private Switch facebook;
    private Switch instagram;
    private static ConnectThread cone;
    BluetoothAdapter bt;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_main);


        //The variable take their reference from the xml file
        facebook = findViewById(R.id.switch5);
        instagram = findViewById(R.id.switch6);
        m = findViewById(R.id.message);
        t = findViewById(R.id.title);
        NLService.main = true;
        cone = ConnectBlActivity.con;


        if (!isMyServiceRunning(Time.class)) {
            startService(new Intent(getApplicationContext(),Time.class));
        }

        if (NLService.getFb()) {
            facebook.setChecked(true);
        }


        if (NLService.getInsta()) {
            instagram.setChecked(true);
        }


        //If the user wands to listen to facebook turns on the switch
        facebook.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //if the switch is on the facebook listening state goes to true
                if (isChecked) {
                    NLService.setFb(true);
                }
                //If not, it goes to false
                if (!isChecked) {
                    NLService.setFb(false);
                }
        });


        //If the user wands to listen to Instagram turns on the switch
        instagram.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //if the switch is on the instagram listening state goes to true
            if (isChecked) {
                NLService.setInsta(true);
            }
            //If not, it goes to false
            if (!isChecked) {
                NLService.setInsta(false);
            }
        });


        final Handler timeHandler = new Handler(getMainLooper());
        timeHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!NLService.isConnected()) {
                    NLService.setConnected(false);
                    cone.interrupt();
                    finish();
                }
                timeHandler.postDelayed(this, 100);
            }
        },100);
    }


    @Override
    protected void onPause() {
        super.onPause();
        NLService.main = false;
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        NLService.main = true;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
