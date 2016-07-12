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
        ComponentName comp = new ComponentName(context.getPackageName(), GCMIntentService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));

    }
}
