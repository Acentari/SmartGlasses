//package com.example.kostas.smartglasses;
//
//
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.widget.CompoundButton;
//import android.widget.Switch;
//
//public class MainActivity extends AppCompatActivity {
//    private static Switch facebook;
//    private static Switch instagram;
//    private static IntentFilter filterF;
//    private static IntentFilter filterI;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.device_main);
//        facebook = findViewById(R.id.switch5);
//        instagram = findViewById(R.id.switch6);
//
//        facebook.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    Intent i = new Intent(getApplicationContext(),NLService.class);
//                    filterF = new IntentFilter("fon");
//                }
//            }
//        });
//        instagram.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    Intent i = new Intent(getApplicationContext(),NLService.class);
//                    filterI = new IntentFilter("ion");
//                }
//            }
//        });
//
//    }
//    public Switch getFacebook(){
//        return facebook;
//    }
//    public Switch getInstagram(){
//        return instagram;
//    }
//    public static IntentFilter getFilterF(){
//        return filterF;
//    }
//}
