package com.example.kostas.smartglasses;


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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.OutputStream;
import java.io.Serializable;
import java.util.Set;


public class ConnectBlActivity extends AppCompatActivity {
    //crating some variables
    private static BluetoothAdapter mBluetoothAdapter;  //This is the Bluetooth Adapter which is used for enabling the bluetooth
    static TextView connectionState;                    //This variable the state of the connection
    Button b;                                           //This button is useless. It is there only for debuging reasons and it will not be there on the final app
    public static ConnectThread con;                    //This is a variable used in creating an object of ConnectThread in which thread the bluetooth connection is made happen
    public static boolean onC =false;
    public static OutputStream o;
    public static BluetoothDevice mDevice;


    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.connection_screen);
        connectionState = findViewById(R.id.connect);
        b = findViewById(R.id.button);                  //the usless button takes the reference from the xml file


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
                        int counter = 0;
                        @Override
                        public void run() {
                            if (mBluetoothAdapter.isEnabled()) {
                                counter ++;

                                if (NLService.isConnected() && counter == 1) {
                                    Intent intent = new Intent(getApplicationContext(),Main.class);
//                                    intent.putExtra("ConnectThread", (Serializable) con);
                                    startActivity(intent);
                                }

                                else if(!NLService.isConnected() && counter == 1) {
                                    connectDevice();
                                }
                            }
                            timeHandler.postDelayed(this, 100);
                        }
                    }, 100);
                }


                else if (mBluetoothAdapter.isEnabled()){
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
//                    intent.putExtra("ConnectThread", con);
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


    private void connectDevice() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        final Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();


        if(pairedDevices.size() > 0){
            for(BluetoothDevice device : pairedDevices){
                if (device.getName().equals("SMART GLASSES")){
                    mDevice = device;
                }
            }


            if(mDevice != null){
                con = new ConnectThread(mDevice);   //The object of the ConnectThread is created here. The constructor takes the wanted device as a parameter if found
                con.start();
                //The thread starts


                final Handler timeHandler = new Handler(getMainLooper());
                timeHandler.postDelayed(new Runnable() {
                    int counter = 0;
                    @Override
                    public void run() {
                        if (NLService.isConnected()) {
                            counter ++;
                            if (counter == 1) {
                                Intent intent = new Intent(getApplicationContext(),Main.class);
//                                intent.putExtra("ConnectThread", con);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_LONG);
                            }
                        }
                        timeHandler.postDelayed(this, 100);
                    }
                }, 100);
            }


            else if (mDevice == null) {
                pairDevice().show();
            }
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();

        if (NLService.isConnected()) {
            connectionState.setText("CONNECTED");
            Intent intent = new Intent(getApplicationContext(),Main.class);
//            intent.putExtra("MyClass", con);
            startActivity(intent);
        }

        else if (!NLService.isConnected()) {
            connectionState.setText("DISCONNECTED");
            stopService(new Intent(getApplicationContext(),Time.class));
            NLService.setConnected(false);
            connectDevice();
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
