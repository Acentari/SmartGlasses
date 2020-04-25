package com.example.kostas.smartglasses;


import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;    
import java.io.IOException;
import java.io.OutputStream;


public class NLService extends NotificationListenerService {
    private OutputStream o;
    private static boolean fb = false;   //set facebook listening state to false. This variable remembers the state even after we kill the app
    private static boolean insta;
    private static boolean connected = false;
    IntentFilter filter;
    BluetoothConnectedReceiver mmReceiver;
    private static ConnectThread cone;
    public static boolean timeRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();
        cone = ConnectBlActivity.con;

        //These are actions for the state of the connection
        filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);

        mmReceiver = new BluetoothConnectedReceiver();
        this.registerReceiver(mmReceiver, filter);
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


    public static void setConnected(boolean c) {
        connected = c;
    }


    //This method is called every time a notification manifests itself
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        if(isConnected()) {
            cone = ConnectBlActivity.con;
            //Here every notification is checked and if the user wands to listen to facebook, we get the message
            if (getFb()) {



                if (sbn.getPackageName().equals("com.facebook.orca")) {   //checking if the is a notification from facebook
                    Bundle extras = sbn.getNotification().extras;
                    String title;


                    if (!extras.getString("android.text").equals("Chat heads active")) {
                        String pack = "Facebook";
                        title = extras.getString("android.text");    //This is the actual user that sends the message
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
                        msg.append("\n");

                        //The ui gets updated with the message
                        Main.t.setText(msg);


                        //The message is written to the outputStream

                        try {
                            byte[] send = msg.toString().getBytes();
                            cone.write(send);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }


            //Here every notification is checked and if the user wands to listen to instagram, we get the message
            if (getInsta()) {
                if (sbn.getPackageName().equals("com.instagram.android")) {   //checking if the is a notification from facebook
                    Bundle extras = sbn.getNotification().extras;
                    String title = extras.getString("android.text");    //This is the actual user that sends the message
                    String text = extras.getCharSequence("android.title").toString();   //and that is the actual message


                    Log.i("Title", title);
                    Log.i("Text", text);


                    //The message is being put in a String Builder
                    StringBuilder msg = new StringBuilder();
                    msg.append("\n");
                    msg.append(text);
                    msg.append("\n");
                    msg.append(title);
                    msg.append("\n");

                    //The ui gets updated with the message
                    Main.t.setText(msg);


                    //The message is written to the outputStream
                    try {
                        byte[] send = msg.toString().getBytes();
                        cone.write(send);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
