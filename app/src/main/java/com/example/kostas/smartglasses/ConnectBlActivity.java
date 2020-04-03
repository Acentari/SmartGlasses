package com.example.kostas.smartglasses;


import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Set;

import static android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS;


public class ConnectBlActivity extends AppCompatActivity {
    private static BluetoothAdapter mBluetoothAdapter;
    static TextView connectionState;
    private AlertDialog enableNotificationListenerAlertDialog;
    Button b;
    private static ConnectThread con;
    public BluetoothDevice mDevice ;
    public static IntentFilter filter;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.connection_screen);
        connectionState = findViewById(R.id.connect);
        b = findViewById(R.id.button);


        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enBt = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enBt, 1);
        }


        final Set<BluetoothDevice> pairedDevices = ConnectBlActivity.getMBluetoothAdapter().getBondedDevices();
        if(pairedDevices.size() > 0){
            for(BluetoothDevice device : pairedDevices){
                mDevice = device;
                if(device.getName().equals("HC-06")){
                    con = new ConnectThread(mDevice);
                    con.start();


                    filter = new IntentFilter();
                    filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
                    filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
                    filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
                }
            }
        }


        enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
        enableNotificationListenerAlertDialog.show();


        Intent in = new Intent(getApplicationContext(), NLService.class);
        startService(in);


        BluetoothConnectedReceiver mReceiver = new BluetoothConnectedReceiver();
        this.registerReceiver(mReceiver, filter);


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Main.class);
                startActivity(i);

            }
        });
    }


    class BluetoothConnectedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                connectionState.setText("CONNECTED");
                Intent i = new Intent(getApplicationContext(),Main.class);
                startActivity(i);
            }
        }
    }


    public static ConnectThread getCon(){
        return con;
    }


    public static BluetoothAdapter getMBluetoothAdapter() {
        return mBluetoothAdapter;
    }


    private AlertDialog buildNotificationServiceAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.notification_listener_service);
        alertDialogBuilder.setMessage(R.string.notification_listener_service_explanation);
        alertDialogBuilder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
                    }
                });
        return (alertDialogBuilder.create());
    }
}
