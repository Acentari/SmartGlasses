package com.example.kostas.smartglasses;


import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class ConnectThread extends Thread {
    private BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream o;
    private static final UUID uid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");


    public ConnectThread(BluetoothDevice device) {
        BluetoothSocket tmp = null;
        mmDevice = device;


        try {
            tmp = device.createRfcommSocketToServiceRecord(uid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mmSocket = tmp;


        try {
            this.o = mmSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        super.run();
        try {
            mmSocket.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void write(String s) throws IOException {
        this.o.write(s.getBytes());
    }
}
