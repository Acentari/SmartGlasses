package com.example.kostas.smartglasses;


import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class BluetoothConnectedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();


        //if the bluetooth is connected the main activity is being automatically started
        if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            ConnectBlActivity.connectionState.setText("CONNECTED");
            NLService.setConneted(true);
            Intent i = new Intent(context,Main.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }

        if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            ConnectBlActivity.connectionState.setText("CONNECTED");
            NLService.setConneted(false);
            Intent i = new Intent(context,ConnectBlActivity .class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}