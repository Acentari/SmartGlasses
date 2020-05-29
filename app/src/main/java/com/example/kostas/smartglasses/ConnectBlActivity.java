package com.example.kostas.smartglasses;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;


public class ConnectBlActivity extends AppCompatActivity {
    //crating some variables
    public static BluetoothAdapter mBluetoothAdapter;  //This is the Bluetooth Adapter which is used for enabling the bluetooth
    static TextView connectionState;                   //This variable the state of the connection
    Button b;                                          //This button is useless. It is there only for debuging reasons and it will not be there on the final app
    public static ConnectThread con = null;            //This is a variable used in creating an object of ConnectThread in which thread the bluetooth connection is made happen
    public BluetoothDevice mDevice;


    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.connection_screen);
        connectionState = findViewById(R.id.connect);
        NLService.bl = true;


        if (!isMyServiceRunning(Re.class)) {
            startService(new Intent(getApplicationContext(),Re.class));
        }

        //Checking if the permission for notification listening are given
        if (!Settings.Secure.getString(this.getContentResolver(),"enabled_notification_listeners").contains(getApplicationContext().getPackageName())){
            Intent i = new Intent(getApplicationContext(),State.class);
            startActivity(i);
        }


        else {
            if(!NLService.isConnected()){
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enBt = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enBt, 1);


                    final Handler timeHandler = new Handler(getMainLooper());
                    timeHandler.postDelayed(new Runnable() {
                        boolean b = false;
                        @SuppressLint("ShowToast")
                        @Override
                        public void run() {
                            if (mBluetoothAdapter.isEnabled()) {
                                if(!NLService.isConnected()) {
                                    connectDevice();
                                    b = true;
                                }
                            }

                            if (b) {
                                timeHandler.removeCallbacks(this);
                            }

                            if (!b) {
                                timeHandler.postDelayed(this, 1000);
                            }
                        }
                    }, 1000);
                }


                else {
                    mBluetoothAdapter.isEnabled();
                    //If the bluetooth is enabled we get all the paired bluetooth devices of the phone and search for the name
                    // of the device we want to connect to
                    connectDevice();
                }
            }


            else if (NLService.isConnected()) {
                if (!mBluetoothAdapter.isEnabled()) {
                    NLService.setConnected(false);
                    startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 1);
                }


                else if(NLService.isConnected() && mBluetoothAdapter.isEnabled()) {
                    Intent intent = new Intent(getApplicationContext(),Main.class);
                    startActivity(intent);
                }
            }
        }
    }


    public static BluetoothAdapter getmBluetoothAdapter() {
        return mBluetoothAdapter;
    }


    private AlertDialog pairDevice() {
        AlertDialog.Builder ald = new AlertDialog.Builder(this);
        ald.setTitle(R.string.title_for_device_pair);
        ald.setMessage(R.string.device_pair_explanation);
        ald.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS));
            }
        });
        return (ald.create());
    }


    public void connectDevice() {

        NLService.bl = true;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        final Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();


        if(pairedDevices.size() > 0){
            for(BluetoothDevice device : pairedDevices){
                if (device.getName().equals("Turbo-x Chicago")){
                    mDevice = device;
                }
            }


            if(mDevice != null){
                NLService.ok = true;
                if (!NLService.isConnected()) {
                    con = new ConnectThread(mDevice);
                    con.start();
                }
            }


            else if (mDevice == null) {
                Handler h1 = new Handler(getMainLooper());
                h1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Set<BluetoothDevice> paired1Devices = mBluetoothAdapter.getBondedDevices();
                        if(paired1Devices.size() > 0) {
                            for (BluetoothDevice device : paired1Devices) {
                                if (device.getName().equals("Turbo-x Chicago")) {
                                    mDevice = device;
                                    con = new ConnectThread(mDevice);
                                    con.start();
                                    h1.removeCallbacks(this);
                                }


                                else{
                                    h1.postDelayed(this,600);
                                }
                            }
                        }
                    }
                },600);
            }
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onRestart() {
        super.onRestart();
        NLService.bl = true;

        if (NLService.isConnected()) {
            startActivity(new Intent(getApplicationContext(),Main.class));
        }


        else {
            if (NLService.ok && !NLService.timeRunning) {
                Toast.makeText(getApplicationContext(),"Disconnected. Killing the app",Toast.LENGTH_LONG);
                NLService.setConnected(false);
                NLService.bl = false;
                finish();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        NLService.bl = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!NLService.isConnected()) {
            stopService(new Intent(getApplicationContext(),Time.class));
            stopService(new Intent(getApplicationContext(),Re.class));
            try {
                con.mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            con.interrupt();
        }
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
