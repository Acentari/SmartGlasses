package com.example.kostas.smartglasses;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



public class Time extends Service {
    private static ConnectThread cone;
    public static BluetoothAdapter bt;

    @Override
    public void onCreate() {
        super.onCreate();
        NLService.timeRunning = true;


        cone = BluetoothConnectedReceiver.cone;
        bt = ConnectBlActivity.getmBluetoothAdapter();

        SimpleDateFormat sdff = new SimpleDateFormat("HH:mm", Locale.getDefault());
        final String tempf = "," + sdff.format(new Date());

        Main.t.setText(tempf);
        try {
            cone.write(tempf);
        } catch (IOException e) {
            e.printStackTrace();
        }


        final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        final Handler timeHandler = new Handler(getMainLooper());
        timeHandler.postDelayed(new Runnable() {
            String tempff = tempf;
            String temp;
            String time = tempff;

            @Override
            public void run() {

                temp = "," + sdf.format(new Date());


                if(!time.equals(temp)) {
                    this.time = temp;
                    Main.t.setText(String.valueOf(time));


                    try {
                        cone.write(time);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                if (!bt.isEnabled() || !NLService.isConnected()) {
                    if (NLService.isConnected()) {
                        NLService.setConnected(false);
                    }


                    timeHandler.removeCallbacks(this);
                    stopService(new Intent(getApplicationContext(),Re.class));
                    stopSelf();
                }


                if (NLService.isConnected()) {
                    timeHandler.postDelayed(this, 100);
                }
            }
        }, 100);
    }

    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        NLService.timeRunning = false;
        super.onDestroy();
        try {
            cone.mmSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        cone.interrupt();
    }
}