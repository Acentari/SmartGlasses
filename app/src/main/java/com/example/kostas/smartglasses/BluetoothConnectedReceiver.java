package com.example.kostas.smartglasses;


import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class BluetoothConnectedReceiver extends BroadcastReceiver {
    public static ConnectThread cone;
    BluetoothAdapter bt;
    @SuppressLint("SetTextI18n")
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();


        if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            String name =device.getAddress();
            if(name.equals("00:00:00:03:ED:5E")){
                ConnectBlActivity.connectionState.setText("CONNECTED");
                cone = ConnectBlActivity.con;
                //start activity
                Intent i = new Intent();
                i.setClassName("com.example.kostas.smartglasses", "com.example.kostas.smartglasses.Main");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                bt = ConnectBlActivity.getmBluetoothAdapter();
                NLService.setConnected(true);
            }
        }


        if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            ConnectBlActivity.connectionState.setText("DISCONNECTED");
            context.stopService(new Intent(context,Time.class));
            context.stopService(new Intent(context,Re.class));
            bt.disable();
            NLService.setConnected(false);
            NLService.bl = false;
        }
    }
}