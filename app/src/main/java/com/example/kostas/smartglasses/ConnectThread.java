package com.example.kostas.smartglasses;


import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import static android.os.Looper.getMainLooper;


//In this thread we make the connection. The reason why a separate thread is used for connection is because the connection of the bluetooth
//can be a painful payload for the UI thread which can cause slowdowns.

public class ConnectThread extends Thread {
    public BluetoothSocket mmSocket;                                                        //variable for bluetooth socket
    BluetoothDevice mmDevice;                                                                //The device is kept here. This variable takes its value from the constructor
    OutputStream o;
    private static final UUID uid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //This is the uuid used to create the rfcomm socket


    public ConnectThread(BluetoothDevice device) {
        BluetoothSocket tmp = null;
        mmDevice = device; //the mmDevices takes its value


        //If the creation of the rfcomm socket is successful the mmSocket variable takes its value
        try {
            tmp = device.createRfcommSocketToServiceRecord(uid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mmSocket = tmp;


        //Try to get The outputStream from the system
        try {
            assert mmSocket != null;
            this.o = mmSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //In this method the actual connection of the socket is made happen
    @Override
    public void run() {
        super.run();
        try {
            mmSocket.connect(); //This is where the magic of the connection happens
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //When this method is called some bydes of our choice are written on the outputStream
    public void write(String s) throws IOException {
        this.o.write(s.getBytes());
    }
}