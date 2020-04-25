package com.example.kostas.smartglasses;


import android.app.ActivityManager;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.io.OutputStream;

import static com.example.kostas.smartglasses.ConnectBlActivity.con;


public class BluetoothConnectedReceiver extends BroadcastReceiver {
    public static OutputStream o;
    ConnectThread cth;
    Context ctx;


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();



        if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            ConnectBlActivity.connectionState.setText("CONNECTED");
            NLService.setConnected(true);
            NLService.timeRunning = false;
//            if (!isMyServiceRunning(Time.class)) {
//                context.startService(new Intent(context,Time.class));
//            }
        }


        if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            ConnectBlActivity.connectionState.setText("DISCONNECTED");
            NLService.setConnected(false);
//            NLService.timeRunning = false;
//            if (isMyServiceRunning(ti.getClass())) {
//                context.stopService(new Intent(context,ti.getClass()));
//            }
        }


    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager)  ctx.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}