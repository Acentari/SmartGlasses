package com.example.kostas.smartglasses;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.io.IOException;
import java.util.Set;

public class NLService extends NotificationListenerService {
    public static BluetoothAdapter mmBluetoothAdapter;
    private static boolean fb = false;  //set facebbok listening state to false. This variable remembers the state even after we kill the app
    private static boolean insta;
    private static boolean connected = false;
    public static IntentFilter filter;
    private static ConnectThread cone;   //This is a variable used in creating an object of ConnectThread in which thread the bluetooth connection is made happen

    @Override
    public void onCreate() {
        super.onCreate();
        mmBluetoothAdapter = ConnectBlActivity.getMBluetoothAdapter();
        cone = ConnectBlActivity.con;

    }


    //Setter for facebook listening state
    public static void setFb(boolean f) {
        fb = f;
    }


    //Getter for facebook listening state
    public static boolean getFb() {
        return fb;
    }


    //Setter for instagram listening state
    public static void setInsta(boolean i) {
        insta = i;
    }


    //Getter for instagram listening state
    public static boolean getInsta() {
        return insta;
    }

    public static boolean isConnected() {
        return connected;
    }


    public static void setConneted(boolean c) {
        connected = c;
    }


    //This method is called every time a notification manifests itself
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {


        //Here every notification is checked and if the user wands to listen to facebook, we get the message
        if (getFb()) {
            if (sbn.getPackageName().equals("com.facebook.orca")) {   //checking if the is a notification from facebook
                String pack = "Facebook";
                Bundle extras = sbn.getNotification().extras;
                String title = extras.getString("android.text");    //This is the actual user that sends the message
                String text = extras.getCharSequence("android.title").toString();   //and that is the actual message


                Log.i("Title", title);
                Log.i("Text", text);


                //The message is being put in a String Builder
                StringBuilder msg = new StringBuilder();
                msg.append(pack);
                msg.append("\n");
                msg.append(text);
                msg.append("\n");
                msg.append(title);

                //The ui gets updated with the message
                Main.t.setText(msg);


                //The message is written to the outputStream
                try {
                    cone.write(msg.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        //Here every notification is checked and if the user wands to listen to instagram, we get the message
        if (getInsta()) {
            if (sbn.getPackageName().equals("com.instagram.android")) {   //checking if the is a notification from facebook
                String pack = "Instagram";
                Bundle extras = sbn.getNotification().extras;
                String title = extras.getString("android.text");    //This is the actual user that sends the message
                String text = extras.getCharSequence("android.title").toString();   //and that is the actual message


                Log.i("Title", title);
                Log.i("Text", text);


                //The message is being put in a String Builder
                StringBuilder msg = new StringBuilder();
                msg.append(pack);
                msg.append("\n");
                msg.append(text);
                msg.append("\n");
                msg.append(title);

                //The ui gets updated with the message
                Main.t.setText(msg);


                //The message is written to the outputStream
                try {
                    cone.write(msg.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
