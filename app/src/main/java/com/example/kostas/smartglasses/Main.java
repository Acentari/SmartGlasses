package com.example.kostas.smartglasses;


import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.UUID;

import static com.example.kostas.smartglasses.ConnectBlActivity.con;
import static com.example.kostas.smartglasses.ConnectBlActivity.mDevice;
import static java.util.Objects.requireNonNull;

public class Main extends AppCompatActivity {

    //assignment of some variables
    public static TextView t;
    TextView m;
    private Switch facebook;
    private Switch instagram;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_main);


        //The variable take their reference from the xml file
        facebook = findViewById(R.id.switch5);
        instagram = findViewById(R.id.switch6);
        m = findViewById(R.id.message);
        t = findViewById(R.id.title);



        final BluetoothAdapter bt = ConnectBlActivity.getmBluetoothAdapter();
//        cone = ConnectBlActivity.con;

        if (NLService.timeRunning) {
            stopService(new Intent(getApplicationContext(),Time.class));
        }

        if (!NLService.timeRunning) {
            startService(new Intent(getApplicationContext(),Time.class));
            NLService.timeRunning = true;
        }



        if (NLService.getFb()) {
            facebook.setChecked(true);
        }


        if (NLService.getInsta()) {
            instagram.setChecked(true);
        }


        //If the user wands to listen to facebook turns on the switch
        facebook.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    NLService.setFb(true);//if the switch is on the facebook listening state goes to true
                }
                //If not, it goes to false
                if (!isChecked) {
                    NLService.setFb(false);
                }
            }
        });


        //If the user wands to listen to Instagram turns on the switch
        instagram.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    NLService.setInsta(true);
                }
                if (!isChecked) {
                    NLService.setInsta(false);
                }
            }
        });


        final Handler timeHandler = new Handler(getMainLooper());
        timeHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!bt.isEnabled() || !NLService.isConnected()) {
                    NLService.setConnected(false);
                    stopService(new Intent(getApplicationContext(),Time.class));
                    finish();
                }

                timeHandler.postDelayed(this, 100);
            }
        }, 100);
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

    @Override
    protected void onStop() {
        super.onStop();
    }
}
