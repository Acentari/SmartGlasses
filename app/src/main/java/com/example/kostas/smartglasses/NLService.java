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
    private static boolean fb = false;  //set facebbok listening state to false. This variable remembers the state even after we kill the app
    private static boolean insta;


    //The Service Starts
    @Override
    public int onStartCommand(Intent intent, int flags, int id){
        super.onStartCommand(intent,flags,id);
        return START_STICKY; //START_STICKY means that the service will "stick" to the system and stay on
    }



    //Setter for facebook listening state
    public static void setFb(boolean f){
        fb = f;
    }


    //Getter for facebook listening state
    public boolean getFb(){
        return this.fb;
    }


    //Setter for instagram listening state
    public static void setInsta(boolean i){
        insta = i;
    }


    //Getter for instagram listening state
    public boolean getInsta(){
        return insta;
    }


    //This method is called every time a notification manifests itself
    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
        //Here every notification is checked and if the user wands to listen to facebook, we get the message
        if(getFb()){
            if(sbn.getPackageName().equals("com.facebook.orca")){   //checking if the is a notification from facebook
                String pack = "Facebook";
                Bundle extras = sbn.getNotification().extras;
                String title = extras.getString("android.text");    //This is the actual user that sends the message
                String text = extras.getCharSequence("android.title").toString();   //and that is the actual message


                Log.i("Title",title);
                Log.i("Text",text);

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
                    ConnectBlActivity.getCon().write(msg.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
