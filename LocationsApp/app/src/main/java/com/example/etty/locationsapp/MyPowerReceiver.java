package com.example.etty.locationsapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyPowerReceiver extends BroadcastReceiver {
        private Boolean isCharging=false;
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if(action.equals(Intent.ACTION_POWER_CONNECTED)) {
            // Do something when power connected
                isCharging=true;
                Log.e("isCharging","isCharging"+isCharging);
                Toast.makeText(context,"Power is now connected ",Toast.LENGTH_LONG).show();
        }
        else if(action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
            // Do something when power disconnected
                isCharging=false;
                Toast.makeText(context,"Power is now disconnected",Toast.LENGTH_LONG).show();
        }
    }
}
