package client.app.clientgcm.broadcast;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.WakefulBroadcastReceiver;

import client.app.clientgcm.service.GCMIntentService;

public class MyBroadcastReceiver extends WakefulBroadcastReceiver {
    public MyBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences s = context.getSharedPreferences("info_cache", 0);
        String str = s.getString("GCM_ID", "NULL");
//        String canId = ""; //Get GCMID from Intent
//        if(str.equals("NULL") || (!str.trim().equals(canId.trim())))
//        {
//            //For duplicate pushes, Process only if the GCM id is matching with incomming push, Else return.
//            return;
//        }
        ComponentName comp = new ComponentName(context.getPackageName(), GCMIntentService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        //Toast.makeText(context.getApplicationContext(), "GCM push received by clientapp.", Toast.LENGTH_LONG).show();
    }
}
