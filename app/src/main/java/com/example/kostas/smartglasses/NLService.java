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
    State s;
    public static IntentFilter filter;
    private static boolean fb = false;
    private static boolean insta;



    @Override
    public int onStartCommand(Intent intent, int flags, int id){
        super.onStartCommand(intent,flags,id);


        return START_STICKY;
    }


    public static void setFb(boolean f){
        fb = f;
    }

    public boolean getFb(){
        return this.fb;
    }

    public static void setInsta(boolean i){
        insta = i;
    }

    public boolean getInsta(){
        return insta;
    }


    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
        if(getFb()){
            if(sbn.getPackageName().equals("com.facebook.orca")){
                String pack = "Facebook";
                Bundle extras = sbn.getNotification().extras;
                String title = extras.getString("android.text");
                String text = extras.getCharSequence("android.title").toString();


                Log.i("Title",title);
                Log.i("Text",text);

                StringBuilder msg = new StringBuilder();
                msg.append(pack);
                msg.append("\n");
                msg.append(text);
                msg.append("\n");
                msg.append(title);

                Main.t.setText(msg);

                try {
                    ConnectBlActivity.getCon().write(msg.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }
    public static IntentFilter getFilter(){
        return filter;
    }
}
