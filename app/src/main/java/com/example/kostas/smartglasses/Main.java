package com.example.kostas.smartglasses;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_main);
        //The variable take their reference from the xml file
        facebook = findViewById(R.id.switch5);
        instagram = findViewById(R.id.switch6);
        m = findViewById(R.id.message);
        t = findViewById(R.id.title);


        //If the user wands to listen to facebook turns on the switch
        facebook.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    NLService.setFb(true);  //if the switch is on the facebook listening state goes to true
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
                }
            }
        });
    }

    //Get the facebook switch state
    public Switch getFacebook(){
        return facebook;
    }


    //Get the instagram listening state
    public Switch getInstagram(){
        return instagram;
    }


    //I really have no idea why these methods still exist
    public static IntentFilter getFilterF(){
        return filterF;
    }


    public static void setFilterF(IntentFilter inf){
        filterF = inf;
    }
}
