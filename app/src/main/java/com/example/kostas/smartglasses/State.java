package com.example.kostas.smartglasses;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import static android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS;

public class State extends AppCompatActivity {
    private AlertDialog enableNotificationListenerAlertDialog;  //This is the Dialog for allowing the app to read the notifications
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permissions);

        //We prompt the user to allow the app to have access to read the notifications
        enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
        enableNotificationListenerAlertDialog.show();
    }


    //This is the method which prompts the user to allow access for notification
    private AlertDialog buildNotificationServiceAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.notification_listener_service);
        alertDialogBuilder.setMessage(R.string.notification_listener_service_explanation);
        alertDialogBuilder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
                    }
                });
        return (alertDialogBuilder.create());
    }


    @Override
    public void onRestart(){
        super.onRestart();
        if(!Settings.Secure.getString(this.getContentResolver(),"enabled_notification_listeners").contains(getApplicationContext().getPackageName())){
            //We prompt the user to allow the app to have access to read the notifications
            enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
            enableNotificationListenerAlertDialog.show();
        }
        else{
            Intent i = new Intent(getApplicationContext(),ConnectBlActivity.class);
            startActivity(i);
        }
    }
}
