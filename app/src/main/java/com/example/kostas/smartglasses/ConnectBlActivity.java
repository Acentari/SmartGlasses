package com.example.kostas.smartglasses;


import android.app.ActivityManager;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.Set;


public class ConnectBlActivity extends AppCompatActivity {
    //crating some variables
    private static BluetoothAdapter mBluetoothAdapter;  //This is the Bluetooth Adapter which is used for enabling the bluetooth
    static TextView connectionState;    //This variable the state of the connection
    private AlertDialog enableNotificationListenerAlertDialog;  //This is the Dialog for allowing the app to read the notifications
    Button b;   //This button is useless. It is there only for debuging reasons and it will not be there on the final app
    public static ConnectThread con;   //This is a variable used in creating an object of ConnectThread in which thread the bluetooth connection is made happen
    public BluetoothDevice mDevice ;    //the bluetooth device is kept here and this variable is being given as a parameter to the ConnectThread
    public static IntentFilter filter;  //This variable keeps the filter for recognizing the sate of the connection


    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.connection_screen);
        connectionState = findViewById(R.id.connect);
        b = findViewById(R.id.button);  //the usless button takes the reference from the xml file


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
                }

                else {
                    //If the bluetooth is enabled we get all the paired bluetooth devices of the phone and search for the name
                    // of the device we want to connect to
                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    final Set<BluetoothDevice> pairedDevices = ConnectBlActivity.mBluetoothAdapter.getBondedDevices();


                    if(pairedDevices.size() > 0){
                        for(BluetoothDevice device : pairedDevices){
                            mDevice = device;
                            if(device.getName().equals("HC-06")){
                                con = new ConnectThread(mDevice);   //The object of the ConnectThread is being created here. The constructor takes the wanted device as a parameter if found
                                con.start();    //The thread starts

                                //These are actions for the state of the connection
                                filter = new IntentFilter();
                                filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
                                filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
                                filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
//                                filter.addAction(String.valueOf(!mBluetoothAdapter.isEnabled()));
                            }
                        }
                    }


                    //Here we register the receiver which helps recognize the state of the connection
                    BluetoothConnectedReceiver mReceiver = new BluetoothConnectedReceiver();
                    this.registerReceiver(mReceiver, filter);
                }
            }


            else {
                if(NLService.isConnected()){
                    Intent intent = new Intent(getApplicationContext(),Main.class);
                    startActivity(intent);
                }
            }
        }


        //This is the useless button
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Main.class);
                startActivity(i);
            }
        });
    }


    //This is a getter which returns the ConnectThread object
//    public static ConnectThread getCon(){
//        return con;
//    }


    //This is a getter which returns the BluetoothAdapter
        public static BluetoothAdapter getMBluetoothAdapter() {
        return mBluetoothAdapter;
    }
}
