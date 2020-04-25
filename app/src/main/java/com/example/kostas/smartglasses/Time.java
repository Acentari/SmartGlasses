package com.example.kostas.smartglasses;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
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

    @Override
    public void onCreate() {
        cone = ConnectBlActivity.con;


        SimpleDateFormat sdff = new SimpleDateFormat("\nHH:mm", Locale.getDefault());
        final String tempf = sdff.format(new Date())+",\n";
        final Handler timeHandler = new Handler(getMainLooper());

        Main.t.setText(String.valueOf(tempf));
        try {
            cone.write(tempf.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        timeHandler.postDelayed(new Runnable() {
            String time = tempf;
            int times =0;

            @Override
            public void run() {

                SimpleDateFormat sdf = new SimpleDateFormat("\nHH:mm", Locale.getDefault());
                String temp = sdf.format(new Date())+",\n";


                if(!time.equals(temp)) {
                    times++;
                    this.time = temp;
                    Main.t.setText(String.valueOf(time));
                    try {
                        byte[] send = time.getBytes();
                        cone.write(send);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                timeHandler.postDelayed(this, 100);
            }
        }, 100);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


//    public class TestReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//
//            if(action.equals("btd")){
//                NLService.setConnected(false);
//                context.startActivity(new Intent(context,ConnectBlActivity.class));
//            }
//        }
//    }
}