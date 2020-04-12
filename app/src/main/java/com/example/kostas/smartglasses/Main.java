package com.example.kostas.smartglasses;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class Main extends AppCompatActivity {

    //assignment of some variables
    static TextView t;
    static TextView m;
    private static Switch facebook;
    private static Switch instagram;
    private static IntentFilter filterF;
    private static IntentFilter filterI;
    public static IntentFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_main);


        //The variable take their reference from the xml file
        facebook = findViewById(R.id.switch5);
        instagram = findViewById(R.id.switch6);
        m = findViewById(R.id.message);
        t = findViewById(R.id.title);


        //These are actions for the state of the connection
        filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
//        filter.addAction(String.valueOf(!mBluetoothAdapter.isEnabled()));

        BluetoothConnectedReceiver mmReceiver = new BluetoothConnectedReceiver();
        this.registerReceiver(mmReceiver, filter);


        if(NLService.getFb()){
            facebook.setChecked(true);
        }


        if(NLService.getInsta()){
            instagram.setChecked(true);
        }


        //If the user wands to listen to facebook turns on the switch
        facebook.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    NLService.setFb(true);//if the switch is on the facebook listening state goes to true
                }
                //If not, it goes to false
                if (!isChecked){
                    NLService.setFb(false);
                }
            }
        });


        //This is unfinished
        instagram.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    NLService.setInsta(true);
                }
                if(!isChecked){
                    NLService.setInsta(false);
                }
            }
        });
    }
}
